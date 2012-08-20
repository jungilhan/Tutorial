package com.tutorial.notification;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;

public class NotificationReceiver extends Activity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_receiver);
	}

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_notification, menu);
        return true;
    }
}
