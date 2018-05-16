package com.tts;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.view.NewCalendarView;

public class CalendarActivity extends AppCompatActivity {

    private NewCalendarView calendarView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);
    }
}
