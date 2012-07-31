package com.example.youtubeapi;

import java.io.IOException;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;

import com.google.api.client.youtube.YouTubeSample;

public class YoutubeActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_youtube);
        
        
        try {
        	YouTubeSample.run();
        } catch (IOException e) {
        	System.err.println(e.getMessage());
        }
        
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_youtube, menu);
        return true;
    }
}
