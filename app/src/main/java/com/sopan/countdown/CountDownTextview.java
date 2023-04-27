package com.sopan.countdown;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import java.util.Calendar;

/**
 * Created by Sopan on 8/18/2017 AD.
 */

public class CountDownTextview extends LinearLayout {

    private Context context;
    private TextView topHoursTxt, belowHoursTxt, hoursTxt,
            belowMinuteTxt, topMinuteTxt, minuteTxt,
            secondTxt, belowSecondTxt, topSecondTxt,
            colon1, colon2,
            topDaysTxt, belowDaysTxt, daysTxt,
            daysLbl;
    private boolean useBanglaNumeral;

    private RelativeLayout hrLayout, minLayout, secLayout, daysLayout;
    private CountDownInterface countDownInterface, newCountDownInterface;
    private int days, hours, minute, second;
    private MyCountDown myCountDown;
    private boolean setAnim, isOnlySecond, showDays, showHours;

    public CountDownTextview(Context context) {
        this(context, null, 0);
    }

    public CountDownTextview(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CountDownTextview(final Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        this.context = context;
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        assert inflater != null;
        View view = inflater.inflate(R.layout.view_textview, this);
        hoursTxt = view.findViewById(R.id.hours_txt);
        belowHoursTxt = view.findViewById(R.id.below_hours_txt);
        topHoursTxt = view.findViewById(R.id.top_hours_txt);
        minuteTxt = view.findViewById(R.id.minute_txt);
        belowMinuteTxt = view.findViewById(R.id.below_minute_txt);
        topMinuteTxt = view.findViewById(R.id.top_minute_txt);
        secondTxt = view.findViewById(R.id.second_txt);
        belowSecondTxt = view.findViewById(R.id.below_second_txt);
        topSecondTxt = view.findViewById(R.id.top_second_txt);

        colon1 = view.findViewById(R.id.colon1);
        colon2 = view.findViewById(R.id.colon2);
        daysLbl = view.findViewById(R.id.days_lbl);

        hrLayout = view.findViewById(R.id.hours_layout);
        minLayout = view.findViewById(R.id.minute_layout);
        secLayout = view.findViewById(R.id.second_layout);

        daysTxt = view.findViewById(R.id.days_txt);
        topDaysTxt = view.findViewById(R.id.top_days_txt);
        belowDaysTxt = view.findViewById(R.id.below_days_txt);


        TypedArray attr = context.obtainStyledAttributes(attrs, R.styleable.CountDownTextview, 0, 0);

        useBanglaNumeral = attr.getBoolean(R.styleable.CountDownTextview_useBanglaNumeral, false);
        int days = attr.getInteger(R.styleable.CountDownTextview_days, 0);
        int hours = attr.getInteger(R.styleable.CountDownTextview_hours, 0);
        int minute = attr.getInteger(R.styleable.CountDownTextview_minute, 0);
        final int second = attr.getInteger(R.styleable.CountDownTextview_second, 0);

        int color = attr.getColor(R.styleable.CountDownTextview_textColor, 0xff000000);
        int colonColor = attr.getColor(R.styleable.CountDownTextview_colonColor, 0xff000000);

        int size = attr.getDimensionPixelSize(R.styleable.CountDownTextview_textSize, 0);

        showDays = attr.getBoolean(R.styleable.CountDownTextview_showDays, true);
        showHours = attr.getBoolean(R.styleable.CountDownTextview_showHours, true);

        String daysLabel = attr.getString(R.styleable.CountDownTextview_daysLabel);

        int digitBackgroundColor = -1;
        int digitBackgroundResource = attr.getResourceId(R.styleable.CountDownTextview_digitBackground, -1);

        if (digitBackgroundResource <= 0)
            digitBackgroundColor = attr.getColor(R.styleable.CountDownTextview_digitBackground, -1);

        setAnim = attr.getBoolean(R.styleable.CountDownTextview_setAnimation, false);
        boolean startAutomatically = attr.getBoolean(R.styleable.CountDownTextview_start_automatically, true);
        isOnlySecond = attr.getBoolean(R.styleable.CountDownTextview_showOnlySecond, false);

        if (!showHours) {
            showDays = false;
            hours = 0;
            days = 0;
        }

        if (isOnlySecond) {
            showHours = false;
            showDays = false;
            hours = 0;
            minute = 0;
            days = 0;
        }

        if (!showDays)
            days = 0;

        if (countDownInterface == null) {
            countDownInterface = new CountDownInterface() {
                @Override
                public void onTick(long time) {
                    if (newCountDownInterface != null)
                        newCountDownInterface.onTick(time);
                }

                @Override
                public void onFinish() {
                    if (newCountDownInterface != null)
                        newCountDownInterface.onFinish();
                }
            };
        }

        if (size > 0)
            setTextSize(size);

        if (digitBackgroundResource > 0)
            setDigitBackgroundResource(digitBackgroundResource);
        else
            setDigitBackgroundColor(digitBackgroundColor);

        if (daysLabel != null)
            setBanglaNumberText(daysLbl, daysLabel);

        setTextColor(color);
        setColonColor(colonColor);
        showOnlySecond(isOnlySecond);
        setShowHours(showHours);
        setShowDays(showDays);

        setTime(days, hours, minute, second);
        if (startAutomatically)
            startTimer();
    }

    public void setTextColor(int color) {

        hoursTxt.setTextColor(color);
        topHoursTxt.setTextColor(color);
        belowHoursTxt.setTextColor(color);
        minuteTxt.setTextColor(color);
        topMinuteTxt.setTextColor(color);
        belowMinuteTxt.setTextColor(color);
        secondTxt.setTextColor(color);
        topSecondTxt.setTextColor(color);
        belowSecondTxt.setTextColor(color);

        daysTxt.setTextColor(color);
        topDaysTxt.setTextColor(color);
        belowDaysTxt.setTextColor(color);
    }

    public void setTextSize(int size) {
        daysTxt.setTextSize(TypedValue.COMPLEX_UNIT_PX, size);
        topDaysTxt.setTextSize(TypedValue.COMPLEX_UNIT_PX, size);
        belowDaysTxt.setTextSize(TypedValue.COMPLEX_UNIT_PX, size);
        hoursTxt.setTextSize(TypedValue.COMPLEX_UNIT_PX, size);
        topHoursTxt.setTextSize(TypedValue.COMPLEX_UNIT_PX, size);
        belowHoursTxt.setTextSize(TypedValue.COMPLEX_UNIT_PX, size);
        minuteTxt.setTextSize(TypedValue.COMPLEX_UNIT_PX, size);
        topMinuteTxt.setTextSize(TypedValue.COMPLEX_UNIT_PX, size);
        belowMinuteTxt.setTextSize(TypedValue.COMPLEX_UNIT_PX, size);
        secondTxt.setTextSize(TypedValue.COMPLEX_UNIT_PX, size);
        topSecondTxt.setTextSize(TypedValue.COMPLEX_UNIT_PX, size);
        belowSecondTxt.setTextSize(TypedValue.COMPLEX_UNIT_PX, size);
        colon1.setTextSize(TypedValue.COMPLEX_UNIT_PX, size);
        colon2.setTextSize(TypedValue.COMPLEX_UNIT_PX, size);
        daysLbl.setTextSize(TypedValue.COMPLEX_UNIT_PX, size);
    }

    public void setShowHours(boolean show) {
        if (!show) {
            hoursTxt.setVisibility(GONE);
            topHoursTxt.setVisibility(GONE);
            belowHoursTxt.setVisibility(GONE);
            colon1.setVisibility(GONE);
        } else {
            hoursTxt.setVisibility(VISIBLE);
            if (setAnim) {
                topHoursTxt.setVisibility(VISIBLE);
                belowHoursTxt.setVisibility(VISIBLE);
                hoursTxt.setVisibility(GONE);
            }
            colon1.setVisibility(VISIBLE);
        }
    }

    public void setShowDays(boolean show) {
        if (!show) {
            daysTxt.setVisibility(GONE);
            topDaysTxt.setVisibility(GONE);
            belowDaysTxt.setVisibility(GONE);
            daysLbl.setVisibility(GONE);

        } else {
            daysTxt.setVisibility(VISIBLE);
            if (setAnim) {
                topDaysTxt.setVisibility(VISIBLE);
                belowDaysTxt.setVisibility(VISIBLE);
                daysTxt.setVisibility(GONE);
            }
            daysLbl.setVisibility(VISIBLE);
        }
    }

    public void showOnlySecond(boolean onlySecond) {
        if (onlySecond) {
            this.minute = 0;
            this.hours = 0;
            hoursTxt.setVisibility(GONE);
            minuteTxt.setVisibility(GONE);
            daysTxt.setVisibility(GONE);

            topHoursTxt.setVisibility(GONE);
            topMinuteTxt.setVisibility(GONE);
            topDaysTxt.setVisibility(GONE);

            belowHoursTxt.setVisibility(GONE);
            belowMinuteTxt.setVisibility(GONE);
            belowDaysTxt.setVisibility(GONE);

            colon1.setVisibility(GONE);
            colon2.setVisibility(GONE);
            daysLbl.setVisibility(GONE);
        } else {
            hoursTxt.setVisibility(VISIBLE);
            minuteTxt.setVisibility(VISIBLE);
            daysTxt.setVisibility(VISIBLE);

            if (setAnim) {

                hoursTxt.setVisibility(GONE);
                minuteTxt.setVisibility(GONE);
                daysTxt.setVisibility(GONE);

                topHoursTxt.setVisibility(VISIBLE);
                belowHoursTxt.setVisibility(VISIBLE);

                topDaysTxt.setVisibility(VISIBLE);
                belowDaysTxt.setVisibility(VISIBLE);

                topMinuteTxt.setVisibility(VISIBLE);
                belowMinuteTxt.setVisibility(VISIBLE);
            }

            colon1.setVisibility(VISIBLE);
            colon2.setVisibility(VISIBLE);

            daysLbl.setVisibility(VISIBLE);
        }
    }

    public void setColonColor(int color) {
        colon1.setTextColor(color);
        colon2.setTextColor(color);
    }

    public void setDigitBackgroundColor(int color) {
        hoursTxt.setBackgroundColor(color);
        topHoursTxt.setBackgroundColor(color);
        belowHoursTxt.setBackgroundColor(color);
        minuteTxt.setBackgroundColor(color);
        topMinuteTxt.setBackgroundColor(color);
        belowMinuteTxt.setBackgroundColor(color);
        secondTxt.setBackgroundColor(color);
        topSecondTxt.setBackgroundColor(color);
        belowSecondTxt.setBackgroundColor(color);
    }

    public void setDigitBackgroundResource(int resId) {
        hoursTxt.setBackgroundResource(resId);
        topHoursTxt.setBackgroundResource(resId);
        belowHoursTxt.setBackgroundResource(resId);
        minuteTxt.setBackgroundResource(resId);
        topMinuteTxt.setBackgroundResource(resId);
        belowMinuteTxt.setBackgroundResource(resId);
        secondTxt.setBackgroundResource(resId);
        topSecondTxt.setBackgroundResource(resId);
        belowSecondTxt.setBackgroundResource(resId);
    }

    public void setOnTick(CountDownInterface countDownInterface) {
        this.newCountDownInterface = countDownInterface;
    }

    public void pause(){
        myCountDown.pause();
    }

    public void resume(){
        myCountDown.resume();
    }

    public void startTimer() {
        stopTimer();

        setTime(days, hours, minute, second);

        long secMilSec = 1000;
        long minMilSec = 60 * secMilSec;
        long hourMilSec = 60 * minMilSec;
        long dayMilSec = 24 * hourMilSec;


        if (minute > 59)
            minute = 59;

        if (second > 59)
            second = 59;

        if (hours > 23)
            hours = 23;


        long millisin = (days * dayMilSec) + (hours * hourMilSec) + (minute * minMilSec) + (second * secMilSec);

        if (millisin > 0) {
            myCountDown = new MyCountDown(millisin, 1000,daysTxt, hoursTxt, minuteTxt, secondTxt, countDownInterface, false);
            myCountDown.start();
        }
    }

    public void startTimer(Calendar expireTime) {
        Calendar nowCalendar = Calendar.getInstance();
        long diff = expireTime.getTimeInMillis() - nowCalendar.getTimeInMillis();

        long secondsInMilli = 1000;
        long minutesInMilli = secondsInMilli * 60;
        long hoursInMilli = minutesInMilli * 60;
        long daysInMilli = hoursInMilli * 24;

        long elapsedDays = diff / daysInMilli;
        diff = diff % daysInMilli;

        long elapsedHours = diff / hoursInMilli;
        diff = diff % hoursInMilli;

        long elapsedMinutes = diff / minutesInMilli;
        diff = diff % minutesInMilli;

        long elapsedSeconds = diff / secondsInMilli;

        days = (int) elapsedDays;
        hours = (int) elapsedHours;
        minute = (int) elapsedMinutes;
        second = (int) elapsedSeconds;

        setTime(days, hours, minute, second);

        startTimer();
    }

    public void stopTimer() {
        if (myCountDown != null) {
            myCountDown.cancel();
            myCountDown = null;
        }
    }

    public void setTime(int days, int hours, int minute, int second) {
        this.days = days;
        this.hours = hours;
        this.minute = minute;
        this.second = second;

        daysTxt.removeTextChangedListener(daysTextWatcher);
        hoursTxt.removeTextChangedListener(hoursTextWatcher);
        minuteTxt.removeTextChangedListener(minutesTextWatcher);
        secondTxt.removeTextChangedListener(secondsTextWatcher);

        setBanglaNumberText(topDaysTxt, String.valueOf(days).length() == 1 ? "0" + days : String.valueOf(days));
        setBanglaNumberText(topHoursTxt, String.valueOf(hours).length() == 1 ? "0" + hours : String.valueOf(hours));
        setBanglaNumberText(topMinuteTxt, String.valueOf(minute).length() == 1 ? "0" + minute : String.valueOf(minute));
        setBanglaNumberText(topSecondTxt, String.valueOf(second).length() == 1 ? "0" + second : String.valueOf(second));

        setBanglaNumberText(belowDaysTxt, String.valueOf(days).length() == 1 ? "0" + days : String.valueOf(days));
        setBanglaNumberText(belowHoursTxt, String.valueOf(hours).length() == 1 ? "0" + hours : String.valueOf(hours));
        setBanglaNumberText(belowMinuteTxt, String.valueOf(minute).length() == 1 ? "0" + minute : String.valueOf(minute));
        setBanglaNumberText(belowSecondTxt, String.valueOf(second).length() == 1 ? "0" + second : String.valueOf(second));

        setBanglaNumberText(daysTxt, String.valueOf(days).length() == 1 ? "0" + days : String.valueOf(days));
        setBanglaNumberText(hoursTxt, String.valueOf(hours).length() == 1 ? "0" + hours : String.valueOf(hours));
        setBanglaNumberText(minuteTxt, String.valueOf(minute).length() == 1 ? "0" + minute : String.valueOf(minute));
        setBanglaNumberText(secondTxt, String.valueOf(second).length() == 1 ? "0" + second : String.valueOf(second));

        setAnimation(setAnim);
    }

    TextWatcher daysTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
           // int bottomDigit = Integer.parseInt(BanglaNumber.convertToDecimal(charSequence.toString()));
            int bottomDigit = Integer.parseInt(charSequence.toString());
            int topDigit = bottomDigit + 1;

            setBanglaNumberText(topDaysTxt, String.valueOf(topDigit).length() == 1 ? "0" + topDigit : String.valueOf(topDigit));
            setBanglaNumberText(belowDaysTxt, String.valueOf(bottomDigit).length() == 1 ? "0" + bottomDigit : String.valueOf(bottomDigit));

            Animation animation = AnimationUtils.loadAnimation(context, R.anim.push_up_out);
            Animation animation1 = AnimationUtils.loadAnimation(context, R.anim.push_up_in);
            topDaysTxt.startAnimation(animation);
            belowDaysTxt.startAnimation(animation1);
        }

        @Override
        public void afterTextChanged(Editable editable) {

        }
    };

    TextWatcher hoursTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
           // int bottomDigit = Integer.parseInt(BanglaNumber.convertToDecimal(charSequence.toString()));
            int bottomDigit = Integer.parseInt(charSequence.toString());
            int topDigit = bottomDigit + 1;

            if (topDigit > 23)
                topDigit = 23;

            setBanglaNumberText(topHoursTxt, String.valueOf(topDigit).length() == 1 ? "0" + topDigit : String.valueOf(topDigit));
            setBanglaNumberText(belowHoursTxt, String.valueOf(bottomDigit).length() == 1 ? "0" + bottomDigit : String.valueOf(bottomDigit));

            Animation animation = AnimationUtils.loadAnimation(context, R.anim.push_up_out);
            Animation animation1 = AnimationUtils.loadAnimation(context, R.anim.push_up_in);
            topHoursTxt.startAnimation(animation);
            belowHoursTxt.startAnimation(animation1);
        }

        @Override
        public void afterTextChanged(Editable editable) {

        }
    };

    TextWatcher minutesTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
          //  int bottomDigit = Integer.parseInt(BanglaNumber.convertToDecimal(charSequence.toString()));
            int bottomDigit = Integer.parseInt(charSequence.toString());
            int topDigit = bottomDigit + 1;

            if (topDigit > 59)
                topDigit = 0;

            setBanglaNumberText(topMinuteTxt, String.valueOf(topDigit).length() == 1 ? "0" + topDigit : String.valueOf(topDigit));
            setBanglaNumberText(belowMinuteTxt, String.valueOf(bottomDigit).length() == 1 ? "0" + bottomDigit : String.valueOf(bottomDigit));

            Animation animation = AnimationUtils.loadAnimation(context, R.anim.push_up_out);
            Animation animation1 = AnimationUtils.loadAnimation(context, R.anim.push_up_in);
            topMinuteTxt.startAnimation(animation);
            belowMinuteTxt.startAnimation(animation1);
        }

        @Override
        public void afterTextChanged(Editable editable) {

        }
    };

    TextWatcher secondsTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
           // int bottomDigit = Integer.parseInt(BanglaNumber.convertToDecimal(charSequence.toString()));
            int bottomDigit = Integer.parseInt(charSequence.toString());
            int topDigit = bottomDigit + 1;
            if (topDigit > 59)
                topDigit = 0;

            setBanglaNumberText(topSecondTxt, String.valueOf(topDigit).length() == 1 ? "0" + topDigit : String.valueOf(topDigit));
            setBanglaNumberText(belowSecondTxt, String.valueOf(bottomDigit).length() == 1 ? "0" + bottomDigit : String.valueOf(bottomDigit));

            Animation animation = AnimationUtils.loadAnimation(context, R.anim.push_up_out);
            Animation animation1 = AnimationUtils.loadAnimation(context, R.anim.push_up_in);
            topSecondTxt.startAnimation(animation);
            belowSecondTxt.startAnimation(animation1);
        }

        @Override
        public void afterTextChanged(Editable editable) {

        }
    };

    public void setAnimation(final boolean anim) {

        if (anim) {
            if (showDays) {
                daysTxt.addTextChangedListener(daysTextWatcher);
                daysTxt.setVisibility(GONE);
                topDaysTxt.setVisibility(VISIBLE);
                belowDaysTxt.setVisibility(VISIBLE);
            }
            if (showHours) {
                hoursTxt.addTextChangedListener(hoursTextWatcher);
                hoursTxt.setVisibility(GONE);
                topHoursTxt.setVisibility(VISIBLE);
                belowHoursTxt.setVisibility(VISIBLE);
            }
            if (!isOnlySecond) {
                minuteTxt.addTextChangedListener(minutesTextWatcher);
                minuteTxt.setVisibility(GONE);
                topMinuteTxt.setVisibility(VISIBLE);
                belowMinuteTxt.setVisibility(VISIBLE);
            }
            secondTxt.addTextChangedListener(secondsTextWatcher);
            secondTxt.setVisibility(GONE);
            topSecondTxt.setVisibility(VISIBLE);
            belowSecondTxt.setVisibility(VISIBLE);
        } else {
            if (showDays) {
                daysTxt.removeTextChangedListener(daysTextWatcher);
                daysTxt.setVisibility(VISIBLE);
                topDaysTxt.setVisibility(GONE);
                belowDaysTxt.setVisibility(GONE);
            }
            if (showHours) {
                hoursTxt.removeTextChangedListener(hoursTextWatcher);
                hoursTxt.setVisibility(VISIBLE);
                topHoursTxt.setVisibility(GONE);
                belowHoursTxt.setVisibility(GONE);
            }
            if (!isOnlySecond) {
                minuteTxt.removeTextChangedListener(minutesTextWatcher);
                minuteTxt.setVisibility(VISIBLE);
                topMinuteTxt.setVisibility(GONE);
                belowMinuteTxt.setVisibility(GONE);
            }
            secondTxt.removeTextChangedListener(secondsTextWatcher);
            secondTxt.setVisibility(VISIBLE);
            topSecondTxt.setVisibility(GONE);
            belowSecondTxt.setVisibility(GONE);
        }
    }

    public void setTypeFace(Typeface typeFace) {
        try {
            secondTxt.setTypeface(typeFace);
            minuteTxt.setTypeface(typeFace);
            hoursTxt.setTypeface(typeFace);
            daysTxt.setTypeface(typeFace);
            belowSecondTxt.setTypeface(typeFace);
            belowMinuteTxt.setTypeface(typeFace);
            belowHoursTxt.setTypeface(typeFace);
            belowDaysTxt.setTypeface(typeFace);
            topSecondTxt.setTypeface(typeFace);
            topMinuteTxt.setTypeface(typeFace);
            topHoursTxt.setTypeface(typeFace);
            topDaysTxt.setTypeface(typeFace);
            daysLbl.setTypeface(typeFace);
            colon1.setTypeface(typeFace);
            colon2.setTypeface(typeFace);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setBanglaNumberText(TextView textView, String s) {

        textView.setText(s);
        /*if (useFarsiNumeral)
            textView.setText(BanglaNumber.convertToFarsi(s));
        else
            textView.setText(s);*/
    }


}
