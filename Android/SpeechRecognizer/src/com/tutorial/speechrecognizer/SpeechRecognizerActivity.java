package com.tutorial.speechrecognizer;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

public class SpeechRecognizerActivity extends Activity {
    private static final int VOICE_RECOGNITION_REQUEST_CODE = 0;
    private ListView mListView;
    
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        mListView = (ListView)findViewById(R.id.result_listview);
        
        Button inputButton = (Button)findViewById(R.id.input_button);
        inputButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                startVoiceRecognitionActivity();
            }
        });
        
        PackageManager packageManager = getPackageManager();
        List<ResolveInfo> activities = packageManager.queryIntentActivities(new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH), 0);
        if (activities.size() != 0) {
            inputButton.setEnabled(true);
        } else {
            inputButton.setEnabled(false);
            inputButton.setText("Recognizer not present");
        }
    }
    
    private void startVoiceRecognitionActivity() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "ko-KR");
        
        startActivityForResult(intent, VOICE_RECOGNITION_REQUEST_CODE);
    }
    
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == VOICE_RECOGNITION_REQUEST_CODE && resultCode == RESULT_OK) {
            ArrayList<String> matches = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            mListView.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, matches));
        }
        
        super.onActivityResult(requestCode, resultCode, data);
    }
}