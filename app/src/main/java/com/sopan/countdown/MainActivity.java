package com.sopan.countdown;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    private CountDownTextview easyCountDownTextview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        easyCountDownTextview = findViewById(R.id.easyCountDownTextview);
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MINUTE, 2 * 1440);
        easyCountDownTextview.startTimer(calendar);
        //Typeface typeface = ResourcesCompat.getFont(this, R.font.iranyekanmediumfanum);
        // easyCountDownTextview.setTypeFace(typeface);
    }

    public void startBtn_onClick(View view) {
        easyCountDownTextview.startTimer();
    }

    public void pauseBtn_onClick(View view) {
        easyCountDownTextview.pause();
    }

    public void resumeBtn_onClick(View view) {
        easyCountDownTextview.resume();
    }
}
