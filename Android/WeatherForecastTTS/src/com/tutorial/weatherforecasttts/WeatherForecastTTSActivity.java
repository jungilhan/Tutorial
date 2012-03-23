package com.tutorial.weatherforecasttts;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Locale;

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
import android.speech.tts.TextToSpeech;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class WeatherForecastTTSActivity extends Activity {
    private HTTPRequestThread mHTTPRequestThread;
    private Handler mHTTPRequestHandler;
    private ProgressDialog mProgressDialog;
    private TextToSpeech mTextToSpeech;
    private final int MSG_ON_HTTP_REQUEST_COMPLETE = 0;
    
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
                
                mProgressDialog = ProgressDialog.show(WeatherForecastTTSActivity.this, "잠시만 기다려주세요", "기상 정보를 내려받는 중입니다.");
                
                mHTTPRequestThread = new HTTPRequestThread("http://www.kma.go.kr/weather/forecast/mid-term-xml.jsp?stnId=" + locationCode);
                mHTTPRequestThread.start();
            }
        });
        
        final Button speakButton = (Button)findViewById(R.id.speak_button);
        speakButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                TextView resultView = (TextView)findViewById(R.id.result_textview);
                mTextToSpeech.speak(resultView.getText().toString(), TextToSpeech.QUEUE_FLUSH, null);
            }
        });
        
        mTextToSpeech = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
            public void onInit(int status) {
                 if (status == TextToSpeech.SUCCESS) {
                     int result = mTextToSpeech.setLanguage(Locale.KOREAN);
                     if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED)
                         Toast.makeText(WeatherForecastTTSActivity.this, "TTS engine does not support the Korean", Toast.LENGTH_SHORT).show();
                     
                } else {
                    Toast.makeText(WeatherForecastTTSActivity.this, "Could not initialize TextToSpeech", Toast.LENGTH_SHORT).show();
                }
            }
        });
        
        mHTTPRequestHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what) {
                case MSG_ON_HTTP_REQUEST_COMPLETE:
                    mProgressDialog.dismiss();

                    try {
                        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
                        DocumentBuilder builder = factory.newDocumentBuilder();
                        InputStream istream = new ByteArrayInputStream(((String)msg.obj).getBytes("utf-8"));
                        Document doc = builder.parse(istream);
                        
                        Element root = doc.getDocumentElement();
                        String location = retrieveLocation(root);
                        String forecast = retrieveForecast(root);
                        
                        TextView resultView = (TextView)findViewById(R.id.result_textview);
                        resultView.setText(location + ".\n\n" + forecast);
                        
                        speakButton.setEnabled(true);
                        
                    } catch (Exception e) {
                        Toast.makeText(WeatherForecastTTSActivity.this, "[Handler Exception] " + e.getMessage(), Toast.LENGTH_LONG).show();
                        speakButton.setEnabled(false);
                    }
                    break;
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
        forecast = forecast.replaceAll("\\(.*?\\)|<br.*?>", "");
        forecast = forecast.replace(".", ". ");
        forecast = forecast.replace("  ", " ");
        forecast = forecast.replace("~", "에서 ");
        forecast = forecast.replace("m", "미터");
        return forecast;
    }
    
    class HTTPRequestThread extends Thread {
        private final String mAddr;
        private String mBuffer;
        
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
                Toast.makeText(WeatherForecastTTSActivity.this, "[HTTP Exception] " + e.getMessage(), Toast.LENGTH_LONG).show();
            }
            
            mHTTPRequestHandler.sendMessage(Message.obtain(mHTTPRequestHandler, 
                                                           MSG_ON_HTTP_REQUEST_COMPLETE, 
                                                           mBuffer));
        }
    }
}