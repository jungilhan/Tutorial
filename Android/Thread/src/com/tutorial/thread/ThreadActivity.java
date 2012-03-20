package com.tutorial.thread;

import android.R;
import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class ThreadActivity extends Activity {
    private final int MSG_ON_ADD_COMPLETE = 0;
    private final int MSG_ON_SUB_COMPLETE = 1;
    private Handler mHandler;
    
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        final TextView addResultView = (TextView)findViewById(R.id.add_result_view);
        final TextView subResultView = (TextView)findViewById(R.id.sub_result_view);
        
        Button startButton = (Button)findViewById(R.id.start_button);
        startButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                addResultView.setText("");
                subResultView.setText("");
                
                Thread addThread = new AddThread(1000, 1000);
                addThread.start();
                
                Thread subThread = new SubThread(5000, 5000);
                subThread.start();
            }
        });
        
        mHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what) {
                case MSG_ON_ADD_COMPLETE:
                    addResultView.setText(((StringBuilder)msg.obj).toString());
                    break;
                case MSG_ON_SUB_COMPLETE:
                    subResultView.setText(((StringBuilder)msg.obj).toString());
                    break;
                }
            }
        };
    }
    
    private class AddThread extends Thread {
        private int mStartNumber;
        private final int mLoopCount;
        
        AddThread(int startNumber, int loopCount) {
            mStartNumber = startNumber;
            mLoopCount = loopCount;
        }
        @Override
        public void run() {
            StringBuilder result = new StringBuilder();
            
            for (int index = 0; index < mLoopCount; index++)
                result.append(++mStartNumber + "\n");
            
            mHandler.sendMessage(Message.obtain(mHandler, MSG_ON_ADD_COMPLETE, result));
        }
    }
    
    private class SubThread extends Thread {
        private int mStartNumber;
        private final int mLoopCount;
        
        SubThread(int startNumber, int loopCount) {
            mStartNumber = startNumber;
            mLoopCount = loopCount;
        }
        @Override
        public void run() {
            StringBuilder result = new StringBuilder();
            
            for (int index = 0; index < mLoopCount; index++)
                result.append(--mStartNumber + "\n");
            
            mHandler.sendMessage(Message.obtain(mHandler, MSG_ON_SUB_COMPLETE, result));
        }
    }
}