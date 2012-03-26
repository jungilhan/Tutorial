package com.tutorial.timeslider;

import java.util.Calendar;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.googlecode.android.widgets.DateSlider.DateSlider;
import com.googlecode.android.widgets.DateSlider.TimeSlider;

public class TimeSliderActivity extends Activity {
    static final int TIMESELECTOR_ID = 0;
    private TextView mTimeText;
    
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        mTimeText = (TextView)findViewById(R.id.result_textview);
        Button timesliderButton = (Button) this.findViewById(R.id.timeslider_button);
        timesliderButton.setOnClickListener(new OnClickListener() {
            public void onClick(View arg0) {
                showDialog(TIMESELECTOR_ID);
            }
        });
    }
    
    @Override
    protected Dialog onCreateDialog(int id) {
        final Calendar c = Calendar.getInstance();
        
        switch (id) {
        case TIMESELECTOR_ID:
            return new TimeSlider(this, new DateSlider.OnDateSetListener() {
                public void onDateSet(DateSlider view, Calendar selectedDate) {
                    Toast.makeText(TimeSliderActivity.this, String.format("The chosen time:%n%tR", selectedDate), Toast.LENGTH_LONG).show();
                    mTimeText.setText(String.format("The chosen time:%n%tR", selectedDate));
                }}, c, 5);
        }
        return null;
    }
}