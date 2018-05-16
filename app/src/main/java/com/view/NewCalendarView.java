package com.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.entities.CalendarAdapter;
import com.tts.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by PF on 2018/5/16.
 */

public class NewCalendarView extends LinearLayout {

    private ImageView btnPre, btnNext;
    private TextView txtDate;
    private GridView grid;

    private Calendar curDate = Calendar.getInstance();
    public String displayFormat = null;


    public NewCalendarView(Context context) {
        super(context);
    }

    public NewCalendarView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initControl(context, attrs);
    }

    public NewCalendarView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initControl(context, attrs);
    }



    private void initControl(Context context, @Nullable AttributeSet attrs) {

        LayoutInflater inflater = LayoutInflater.from(context);
        inflater.inflate(R.layout.calendar_view, this);
        btnPre = findViewById(R.id.btnPre);
        btnNext = findViewById(R.id.btnNext);
        txtDate = findViewById(R.id.txtDate);
        grid = findViewById(R.id.calendar_grid);

        btnPre.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                curDate.add(Calendar.MONTH, -1);
                renderCalender();
            }
        });

        btnNext.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                curDate.add(Calendar.MONTH, +1);
                renderCalender();
            }
        });

        TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.NewCalendar);
        String format = typedArray.getString(R.styleable.NewCalendar_dataFormat);

        try {
            if(format == null)
                displayFormat = "MMM yyyy";
            else
                displayFormat = format;
        }finally {
            typedArray.recycle();
        }
        
        renderCalender();

    }

    private void renderCalender() {

        SimpleDateFormat sdf = new SimpleDateFormat(displayFormat);
        txtDate.setText(sdf.format(curDate.getTime()));
        ArrayList<Date> cells = new ArrayList<>();
        Calendar calendar = (Calendar) curDate.clone();

//        b = calendar.add(Calendar.DAY_OF_MONTH, 1);//表示b为calender的后一天， b.after(calender) 返回true
        calendar.set(Calendar.DAY_OF_MONTH, 1);//设置这个月的第一天
        int prevDays = calendar.get(Calendar.DAY_OF_WEEK)-1;//计算这个月第一天前有几天是前一个月的
        calendar.add(calendar.DAY_OF_MONTH, -prevDays);

        int maxCellCount = 6*7;
        while (cells.size()<maxCellCount){
            cells.add(calendar.getTime());
            calendar.add(Calendar.DAY_OF_MONTH, 1);
        }

        CalendarAdapter calendarAdapter = new CalendarAdapter(getContext(), cells);
        grid.setAdapter(calendarAdapter);

    }


}
