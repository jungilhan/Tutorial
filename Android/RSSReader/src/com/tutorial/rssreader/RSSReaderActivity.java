package com.tutorial.rssreader;

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
import org.w3c.dom.NodeList;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class RSSReaderActivity extends Activity {
    private HTTPRequestThread mHTTPRequestThread;
    private Handler mHTTPRequestHandler;
    private ProgressDialog mProgressDialog;
    private final int MSG_ON_HTTP_REQUEST_COMPLETE = 0;
    
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        Button startButton = (Button)findViewById(R.id.start_button);
        startButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                EditText rssInput = (EditText)findViewById(R.id.rss_input);
                String url = rssInput.getText().toString();
                if (url.length() == 0) {
                    Toast.makeText(RSSReaderActivity.this, "RSS 주소를 입력하세요!", Toast.LENGTH_LONG).show();
                    return;
                }
                
                mProgressDialog = ProgressDialog.show(RSSReaderActivity.this, "잠시만 기다려주세요!", "RSS 정보를 내려받는 중입니다.");
                
                mHTTPRequestThread = new HTTPRequestThread(url);
                mHTTPRequestThread.start();
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
                        String rssTitle = RSSTitle(root);
                        String articleTitle = ArticleTitle(root);
                        
                        TextView rssView = (TextView)findViewById(R.id.rss_view);
                        rssView.setText(rssView.getText() + rssTitle + "\n\n" + articleTitle + "\n\n");
                        
                    } catch (Exception e) {
                        Toast.makeText(RSSReaderActivity.this, "[Handler Exception] " + e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                    break;
                }
            }
         };
    }
    
    private String RSSTitle(Element root) {
        Node title = root.getElementsByTagName("title").item(0);
        return title.getFirstChild().getNodeValue();
    }
    
    private String ArticleTitle(Element root) {
        String titles = new String();
        NodeList item = root.getElementsByTagName("item");
        for (int index = 0; index < item.getLength(); index++) {
            titles += ((Element)item.item(index)).getElementsByTagName("title").item(0).getFirstChild().getNodeValue() + "\n";
        }
        return titles;
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
                            line = line.replace("&quot;", "\"");
                            line = line.replace("&apos;", "'");
                            html.append(line);
                        }
                        bufferedReader.close();
                        mBuffer = html.toString();
                    }
                    conn.disconnect();
                }
            } catch (Exception e) {
                Log.e("HTTPRequestThread", "[HTTP Exception] " + e.getMessage());
            }
            
            mHTTPRequestHandler.sendMessage(Message.obtain(mHTTPRequestHandler, 
                                                           MSG_ON_HTTP_REQUEST_COMPLETE, 
                                                           mBuffer));
        }
    }
}