package com.sopan.countdown;

import android.widget.TextView;

/**
 * Created by Sopan on 8/17/2017 AD.
 */

public class MyCountDown extends CountDownTimer {

    private TextView dayTxt, hrTxt, minTxt, scndTxt;
    private CountDownInterface countDownInterface;
    private boolean useBanglaNumeral;

    public MyCountDown(long millisInFuture, long countDownInterval,
                       TextView dayTxt, TextView hrTxt, TextView minTxt,
                       TextView scndTxt, CountDownInterface countDownInterface, boolean useBanglaNumeral) {
        super(millisInFuture, countDownInterval);
        this.dayTxt = dayTxt;
        this.hrTxt = hrTxt;
        this.minTxt = minTxt;
        this.scndTxt = scndTxt;
        this.countDownInterface = countDownInterface;
        this.useBanglaNumeral = useBanglaNumeral;
    }

    @Override
    public void onTick(long l) {
        printTime(l);
        countDownInterface.onTick(l);
    }

    @Override
    public void onFinish() {
        /*int d = Integer.parseInt(BanglaNumber.convertToDecimal(dayTxt.getText().toString().trim()));
        int hr = Integer.parseInt(BanglaNumber.convertToDecimal(hrTxt.getText().toString().trim()));
        int min = Integer.parseInt(BanglaNumber.convertToDecimal(minTxt.getText().toString().trim()));
        int second = Integer.parseInt(BanglaNumber.convertToDecimal(scndTxt.getText().toString().trim()));*/

        int d = Integer.parseInt(dayTxt.getText().toString().trim());
        int hr = Integer.parseInt(hrTxt.getText().toString().trim());
        int min = Integer.parseInt(minTxt.getText().toString().trim());
        int second = Integer.parseInt(scndTxt.getText().toString().trim());

        if (hr > 0)
            setBanglaNumberText(hrTxt, "00");
        if (min > 0)
            setBanglaNumberText(minTxt, "00");
        if (second > 0)
            setBanglaNumberText(scndTxt, "00");
        if (d > 0)
            setBanglaNumberText(dayTxt, "00");

        countDownInterface.onFinish();
    }

    private void printTime(long time) {
        int minute, second, hours, days;
        long secMilSec = 1000;
        long minMilSec = 60 * secMilSec;
        long hourMilSec = 60 * minMilSec;
        long dayMilSec = 24 * hourMilSec;

        int d = Integer.parseInt(dayTxt.getText().toString().trim());
        int hr = Integer.parseInt(hrTxt.getText().toString().trim());
        int min = Integer.parseInt(minTxt.getText().toString().trim());


        hours = 0;
        minute = 0;
        days = 0;

        if (time >= dayMilSec) {
            days = (int) (time / dayMilSec);
            hours = (int) ((time % dayMilSec) / hourMilSec);
            minute = (int) (((time % dayMilSec) % hourMilSec) / minMilSec);
            second = (int) ((((time % dayMilSec) % hourMilSec) % minMilSec) / secMilSec);
        } else if (time >= hourMilSec) {
            hours = (int) (time / hourMilSec);
            minute = (int) ((time % hourMilSec) / minMilSec);
            second = (int) (((time % hourMilSec) % minMilSec) / secMilSec);
        } else if (time >= minMilSec) {
            minute = (int) (time / minMilSec);
            second = (int) ((time % minMilSec) / secMilSec);
        } else {
            second = (int) (time / secMilSec);
        }

        if (d != days)
            setBanglaNumberText(dayTxt, String.valueOf(days).length() == 1 ? "0" + days : String.valueOf(days));
        if (hr != hours)
            setBanglaNumberText(hrTxt, String.valueOf(hours).length() == 1 ? "0" + hours : String.valueOf(hours));
        if (min != minute)
            setBanglaNumberText(minTxt, String.valueOf(minute).length() == 1 ? "0" + minute : String.valueOf(minute));

        setBanglaNumberText(scndTxt, String.valueOf(second).length() == 1 ? "0" + second : String.valueOf(second));
    }

    private void setBanglaNumberText(TextView textView, String s) {

        textView.setText(s);
        /*if (useBanglaNumeral)
            textView.setText(BanglaNumber.convertToFarsi(s));
        else
            textView.setText(s);*/
    }
}
