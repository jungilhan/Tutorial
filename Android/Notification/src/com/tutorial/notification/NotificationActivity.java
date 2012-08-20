package com.tutorial.notification;

import android.R;
import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Button;

public class NotificationActivity extends Activity {
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);
        
        Button registerButton = (Button)findViewById(R.id.register_notification_button);
        registerButton.setOnClickListener(new View.OnClickListener() {
        	public void onClick(View v) {
        		registerNotification();
        	}
        });        
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_notification, menu);
        return true;
    }
    
    private void registerNotification() {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
			/*
			Intent intent = new Intent(this, NotificationReceiver.class);
	    	PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);
	    	
			Notification notification = new Notification.Builder(this)
			.setTicker("Hello Notification!")
			.setContentTitle("This is the title")
			.setContentText("This is the message.")
			.setSmallIcon(android.R.drawable.ic_input_add)
			.setWhen(System.currentTimeMillis())
			.setAutoCancel(true)
			.setContentIntent(pendingIntent)
			.build();
			
			NotificationManager notificationManager = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
			notificationManager.notify(0, notification);
			*/
		} else {
	    	NotificationManager notificationManager = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
	    	Notification notification = new Notification(android.R.drawable.ic_input_add, "Hello Notification!", System.currentTimeMillis());
	    	notification.flags |= Notification.FLAG_AUTO_CANCEL;
	    	
	    	Intent intent = new Intent(this, NotificationReceiver.class);
	    	PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);
	    	notification.setLatestEventInfo(this, "This is the title", "This is the message.", pendingIntent);
	    	notification.number += 1;
	    	notificationManager.notify(0, notification);
		}
    }
}
