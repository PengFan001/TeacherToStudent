package com.entities;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.tts.R;
import com.view.CalenderDayView;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by PF on 2018/5/16.
 */

public class CalendarAdapter extends ArrayAdapter {

    LayoutInflater inflater;

    public CalendarAdapter(@NonNull Context context, ArrayList<Date> days) {
        super(context, R.layout.calender_day_view, days);
        inflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        if(convertView == null)
            convertView = inflater.inflate(R.layout.calender_day_view, parent, false);
        Date date = (Date) getItem(position);
        Date now = new Date();
        int day = date.getDate();

        ((TextView)convertView).setText(String.valueOf(day));

        if(date.getMonth() == now.getMonth())
            ((TextView)convertView).setTextColor(Color.parseColor("#000000"));
        else
            ((TextView)convertView).setTextColor(Color.parseColor("#CCCCCC"));
        if(now.getDate() == date.getDate() && now.getMonth() == date.getMonth() && now.getYear() == date.getYear())
            ((CalenderDayView)convertView).isToday = true;


        return convertView;
    }

}