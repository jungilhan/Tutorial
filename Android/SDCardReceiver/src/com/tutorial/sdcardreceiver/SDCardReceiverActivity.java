package com.tutorial.sdcardreceiver;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.widget.Toast;

public class SDCardReceiverActivity extends Activity {
    private BroadcastReceiver mReceiver;
    
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        mReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (intent.getAction().equals(Intent.ACTION_MEDIA_MOUNTED))
                    Toast.makeText(context, "ACTION_MEDIA_MOUNTED", Toast.LENGTH_LONG).show();
                
                if (intent.getAction().equals(Intent.ACTION_MEDIA_UNMOUNTED))
                    Toast.makeText(context, "ACTION_MEDIA_UNMOUNTED", Toast.LENGTH_LONG).show();
            }
        };
        
        IntentFilter intentFinter = new IntentFilter();
        intentFinter.addAction(Intent.ACTION_MEDIA_MOUNTED);
        intentFinter.addAction(Intent.ACTION_MEDIA_UNMOUNTED);
        intentFinter.addDataScheme("file");
        registerReceiver(mReceiver, intentFinter);
    }
    
    
    @Override
    protected void onDestroy() {
        super.onDestroy();
        this.unregisterReceiver(mReceiver);
    }
    
}