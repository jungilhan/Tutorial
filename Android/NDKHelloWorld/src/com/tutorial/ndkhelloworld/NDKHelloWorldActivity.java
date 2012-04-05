package com.tutorial.ndkhelloworld;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class NDKHelloWorldActivity extends Activity {
    
    public native String userName(String greetMsg);
    
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        
        findViewById(R.id.hello).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                System.loadLibrary("hello");
                
                String text = userName("Hi!");
                Toast.makeText(NDKHelloWorldActivity.this, text, Toast.LENGTH_LONG).show();
            }
        });
    }
}