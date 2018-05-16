package com.view;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by PF on 2018/5/15.
 */

public class CalenderDayView extends TextView {

    public boolean isToday = false;
    private Paint paint = new Paint();

    public CalenderDayView(Context context) {
        super(context);
    }

    public CalenderDayView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initControl();
    }

    public CalenderDayView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initControl();
    }

    private void initControl() {
        paint.setStyle(Paint.Style.STROKE);
        paint.setColor(Color.parseColor("#FF0033"));

    }

    @Override
    protected void onDraw(Canvas canvas){

        super.onDraw(canvas);
        if(isToday) {
            canvas.translate(getWidth() / 2, getHeight() / 2);
            canvas.drawCircle(0, 0, getWidth() / 2, paint);
        }
    }
}
