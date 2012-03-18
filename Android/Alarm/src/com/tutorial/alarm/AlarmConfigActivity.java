package com.tutorial.alarm;

import java.util.Calendar;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

public class AlarmConfigActivity extends Activity {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        Button alarmButton = (Button)findViewById(R.id.alarm_button);
        alarmButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                onButtonClick(v);  
            }
        });
    }
    
    private void onButtonClick(View v) {
        AlarmManager alarmManager = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
        Intent intent;
        PendingIntent sender;
        
        switch (v.getId()) {
        case R.id.alarm_button:
            intent = new Intent(this, AlarmReceiver.class);
            sender = PendingIntent.getBroadcast(this, 0, intent, 0);
            
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(System.currentTimeMillis());
            calendar.add(Calendar.SECOND, 5);
            
            alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), sender);
            break;
        }
    }
}