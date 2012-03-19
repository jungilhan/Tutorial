package com.tutorial.requesthtmlasync;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

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

public class RequestHTMLAsyncActivity extends Activity {
    private HTTPRequestThread mHTTPRequestThread;
    private Handler mHTTPRequestHandler;
    private ProgressDialog mProgressDialog;
    
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        Button requestButton = (Button)findViewById(R.id.request_button);
        requestButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                EditText urlTextEdit = (EditText)findViewById(R.id.url_edittext);
                String addr = urlTextEdit.getText().toString();
                
                if (addr.length() > 0) {
                    mProgressDialog = ProgressDialog.show(RequestHTMLAsyncActivity.this, "Wait", "Downloading...");
                    
                    mHTTPRequestThread = new HTTPRequestThread(addr);
                    mHTTPRequestThread.start();
                } else {
                    Toast.makeText(RequestHTMLAsyncActivity.this, "Please type the url.", Toast.LENGTH_SHORT).show();
                }
            }
        });
        
        mHTTPRequestHandler = new Handler() {
           @Override
           public void handleMessage(Message msg) {
               mProgressDialog.dismiss();
               TextView htmlView = (TextView)findViewById(R.id.html_textview);
               htmlView.setText(mHTTPRequestThread.mBuffer);
           }
        };
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
                            html.append(line + '\n');
                        }
                        bufferedReader.close();
                        mBuffer = html.toString();
                    }
                    conn.disconnect();
                }
            } catch (Exception e) {
                Toast.makeText(RequestHTMLAsyncActivity.this, "[Exception] " + e.getMessage(), Toast.LENGTH_LONG).show();
            }
            
            mHTTPRequestHandler.sendEmptyMessage(0);
        }
    }
}