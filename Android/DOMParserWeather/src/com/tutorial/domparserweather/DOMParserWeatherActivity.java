package com.tutorial.domparserweather;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class DOMParserWeatherActivity extends Activity {
    private HTTPRequestThread mHTTPRequestThread;
    private Handler mHTTPRequestHandler;
    private ProgressDialog mProgressDialog;
    
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        Button startButton = (Button)findViewById(R.id.start_button);
        startButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                EditText locationInput = (EditText)findViewById(R.id.location_input);
                String locationCode = locationInput.getText().toString();
                if (locationCode.length() == 0)
                    locationCode = "108";
                
                mProgressDialog = ProgressDialog.show(DOMParserWeatherActivity.this, "잠시만 기다려주세요", "기상 정보를 내려받는 중입니다.");
                
                mHTTPRequestThread = new HTTPRequestThread("http://www.kma.go.kr/weather/forecast/mid-term-xml.jsp?stnId=" + locationCode);
                mHTTPRequestThread.start();
            }
        });
        
        mHTTPRequestHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                mProgressDialog.dismiss();

                try {
                    DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
                    DocumentBuilder builder = factory.newDocumentBuilder();
                    InputStream istream = new ByteArrayInputStream(mHTTPRequestThread.mBuffer.getBytes("utf-8"));
                    Document doc = builder.parse(istream);
                    
                    Element root = doc.getDocumentElement();
                    String location = retrieveLocation(root);
                    String forecast = retrieveForecast(root);
                    
                    TextView resultView = (TextView)findViewById(R.id.result_textview);
                    resultView.setText(location + '\n' + forecast);
                    
                } catch (Exception e) {
                    Toast.makeText(DOMParserWeatherActivity.this, "[Handler Exception] " + e.getMessage(), Toast.LENGTH_LONG).show();
                }
            }
         };
    }
    
    private String retrieveLocation(Element root) {
        Node title = root.getElementsByTagName("title").item(0);
        return title.getFirstChild().getNodeValue();
    }
    
    private String retrieveForecast(Element root) {
        Node wf = root.getElementsByTagName("wf").item(0);
        String forecast = wf.getFirstChild().getNodeValue();
        return forecast.replaceAll("\\(.*?\\)|<br.*?>", "");
    }
    
    class HTTPRequestThread extends Thread {
        String mAddr;
        String mBuffer;
        
        HTTPRequestThread(String addr) {
            mAddr = addr;
            mBuffer = "";
        }
        
        @Override
        public void run() {
            StringBuilder html = new StringBuilder();
            try {
                URL url = new URL(mAddr);
                HttpURLConnection conn = (HttpURLConnection)url.openConnection();
                if (conn != null) {
                    if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                        
                        for (;;) {
                            String line = bufferedReader.readLine();
                            if (line == null) break;
                            html.append(line);
                        }
                        bufferedReader.close();
                        mBuffer = html.toString();
                    }
                    conn.disconnect();
                }
            } catch (Exception e) {
                Toast.makeText(DOMParserWeatherActivity.this, "[HTTP Exception] " + e.getMessage(), Toast.LENGTH_LONG).show();
            }
            
            mHTTPRequestHandler.sendEmptyMessage(0);
        }
    }
}