package com.tutorial.tts;

import java.util.*;

import android.app.*;
import android.os.*;
import android.speech.tts.*;
import android.util.*;
import android.view.*;
import android.widget.*;

public class TextToSpeechActivity extends Activity {
    private TextToSpeech mTextToSpeech;
    
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        final EditText textEdit = (EditText)findViewById(R.id.speak_edittext);
        
        final Button speakButton = (Button)findViewById(R.id.speak_button);
        speakButton.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                String text = textEdit.getText().toString(); 
                mTextToSpeech.speak(text, TextToSpeech.QUEUE_FLUSH, null);
            }
        });
        
        mTextToSpeech = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
            public void onInit(int status) {
                 if (status == TextToSpeech.SUCCESS) {
                     Log.d("TextToSpeech", "Init success");
                     int result = mTextToSpeech.setLanguage(Locale.US);
                     if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED)
                         Toast.makeText(TextToSpeechActivity.this, "TTS engine does not support the English", Toast.LENGTH_SHORT).show();
                     else 
                         speakButton.setEnabled(true);
                } else {
                    Toast.makeText(TextToSpeechActivity.this, "Could not initialize TextToSpeech", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    
    @Override
    protected void onDestroy() {
        mTextToSpeech.stop();
        mTextToSpeech.shutdown();
        super.onDestroy();
    }
}