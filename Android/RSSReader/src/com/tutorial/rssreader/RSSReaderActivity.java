package com.tutorial.rssreader;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

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
                
                if (!url.toLowerCase().contains("http://"))
                    url = "http://" + url;
                
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
                        Document doc = builder.parse(new InputSource(new StringReader((String)msg.obj)));
                        
                        Element root = doc.getDocumentElement();
                        String rssTitle = rssTitle(root);
                        String articleTitle = articleTitle(root);
                        String articleLink = articleLink(root);
                        String articlePubData = articlePubData(root);
                        
                        TextView rssView = (TextView)findViewById(R.id.title_view);
                        rssView.setText(rssTitle + "\n\n" + articleTitle + "\n\n" + articleLink + "\n\n" + articlePubData);
                        
                    } catch (Exception e) {
                        Toast.makeText(RSSReaderActivity.this, "[Handler Exception] " + e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                    break;
                }
            }
         };
    }
    
    private String rssTitle(Element root) {
        Element e = (Element)root.getElementsByTagName("title").item(0);
        return getElementValue(e);
    }
    
    private String articleTitle(Element root) {
        String titles = new String();
        NodeList item = root.getElementsByTagName("item");
        for (int index = 0; index < item.getLength(); index++) {
            Element e = (Element)((Element)item.item(index)).getElementsByTagName("title").item(0);
            titles += "[" + index + "] " + getElementValue(e) + "\n";
        }
        return titles;
    }
    
    private String articleLink(Element root) {
        String links = new String();
        NodeList item = root.getElementsByTagName("item");
        for (int index = 0; index < item.getLength(); index++) {
            Element e = (Element)((Element)item.item(index)).getElementsByTagName("link").item(0);
            links += "[" + index + "] " + getElementValue(e) + "\n";
        }
        
        return links;
    }
    
    private String articlePubData(Element root) {
        String pubDatas = new String();
        NodeList item = root.getElementsByTagName("item");
        for (int index = 0; index < item.getLength(); index++) {
            Element e = (Element)((Element)item.item(index)).getElementsByTagName("pubDate").item(0);
            pubDatas += "[" + index + "] " + getElementValue(e) + "\n";
        }
        
        return pubDatas;
    }
    
    private String getElementValue(Element e) {
        if (e == null)
            return "";

        StringBuilder stringBuilder = new StringBuilder();
          
        Node node = e.getFirstChild();
        while (node != null) {
            stringBuilder.append(node.getNodeValue());
            node = node.getNextSibling();
        }
        
        return stringBuilder.toString();
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
                Log.e("HTTPRequestThread", "[HTTP Exception] " + e.getMessage());
            }
            
            mHTTPRequestHandler.sendMessage(Message.obtain(mHTTPRequestHandler, 
                                                           MSG_ON_HTTP_REQUEST_COMPLETE, 
                                                           mBuffer));
        }
    }
}