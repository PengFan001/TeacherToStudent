package com.tts;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;
import com.entities.Course;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.view.NewCalendarView;

import java.util.ArrayList;
import java.util.Date;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class CalendarActivity extends AppCompatActivity implements NewCalendarView.NewCalendarListener{

    public static final MediaType JSON = null;
    private Date date;
    private String teacherPhone;
    private String resultMessage = null;
    private NewCalendarView calendarView;
    private ArrayList<Date> dateArrayList = new ArrayList<Date>();
    private CallBack callBack;

    @SuppressLint("ResourceType")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getValues();
        setContentView(R.layout.activity_calendar);
        calendarView = findViewById(R.id.calendar);
        calendarView.listener = this;
        if (callBack!=null)
            callBack.callBack(dateArrayList);
    }

    //获取学生预约的时间信息
    private void getValues(){
        Intent intent = getIntent();
        teacherPhone = intent.getStringExtra("phone");
    }

    @Override
    public void onItemLongPress(Date day) {
        this.date = day;
        Date today = new Date();
        if(today.after(day)){
            Toast.makeText(CalendarActivity.this, "你只能设置今天之后的时间为可预约时间", Toast.LENGTH_SHORT).show();
        }
        else {
//            Toast.makeText(CalendarActivity.this, day.toString(), Toast.LENGTH_LONG).show();
            dateArrayList.add(date);
            Course course = new Course();
            course.setDate(day);
            course.setTeacherPhone(teacherPhone);
            try{
                sendDate(course);
                Thread.sleep(200);
            }catch (Exception e){
                e.printStackTrace();
            }

            if(resultMessage == null)
                resultMessage = "fail";
            if(resultMessage.equals("succeed"))
                Toast.makeText(CalendarActivity.this, "设置成功", Toast.LENGTH_SHORT).show();
            else if(resultMessage.equals("exist"))
                Toast.makeText(CalendarActivity.this, "该日期已设置", Toast.LENGTH_SHORT).show();
            else
                Toast.makeText(CalendarActivity.this, "设置失败", Toast.LENGTH_SHORT).show();

        }
    }

    private void sendDate(final Course course){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    OkHttpClient client = new OkHttpClient();
                    Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();
                    String toJson = gson.toJson(course);
                    RequestBody requestBody = RequestBody.create(JSON, toJson);
                    Request request = new Request.Builder().url("http://192.168.1.109:8080/teacher/increaseCourse").post(requestBody).build();
                    Response response = client.newCall(request).execute();
                    if (response.isSuccessful()) {
                        String respondData = response.body().string();
                        resultMessage = gson.fromJson(respondData, String.class);
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }).start();
    }

    public interface CallBack{
        void callBack(ArrayList<Date> dates);
    }

}
