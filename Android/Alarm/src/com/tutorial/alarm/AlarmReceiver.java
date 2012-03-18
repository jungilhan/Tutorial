package com.tutorial.alarm;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

public class AlarmReceiver extends BroadcastReceiver {
    public void onReceive(Context context, Intent intent) {
        Toast.makeText(context, "Alarm received", Toast.LENGTH_LONG).show();
        
        Intent notify = new Intent(context, AlarmNotification.class);
        PendingIntent sender = PendingIntent.getActivity(context, 0, notify, 0);
        try {
            sender.send();
        } catch(Exception e) {
            Toast.makeText(context, "Failed to send pending intent", Toast.LENGTH_SHORT).show();
        }
    }
}
