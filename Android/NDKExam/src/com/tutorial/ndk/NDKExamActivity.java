package com.tutorial.ndk;

import android.app.*;
import android.os.*;
import android.widget.*;

public class NDKExamActivity extends Activity {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        TextView tv = new TextView(this);
        int x = 1000;
        int y = 42;
        
        System.loadLibrary("ndk-exam");
        
        int z = add(x, y);
        
        tv.setText("The sum of " + x + " and " + y + " is " + z);
        setContentView(tv);
    }
    
    public native int add(int x, int y);
}