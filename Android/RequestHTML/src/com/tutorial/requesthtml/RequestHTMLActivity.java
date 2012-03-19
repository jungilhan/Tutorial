package com.tutorial.requesthtml;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class RequestHTMLActivity extends Activity {
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
                    String html = RequestHTML(addr);
                    TextView htmlView = (TextView)findViewById(R.id.html_textview);
                    htmlView.setText(html);
                } else {
                    Toast.makeText(RequestHTMLActivity.this, "Please type the url.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    
    private String RequestHTML(String addr) {
        StringBuilder html = new StringBuilder();
        try {
            URL url = new URL(addr);
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
                }
                conn.disconnect();
            }
        } catch (Exception e) {
            Toast.makeText(RequestHTMLActivity.this, "[Exception] " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
        return html.toString();
    }
}