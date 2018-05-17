package com.tts;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.entities.Student;
import com.entities.Teacher;
import com.fragment.SelfInfoFragment;
import com.fragment.SettingFragment;
import com.fragment.StudentFragment;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {

    public static final MediaType JSON = null;
    private Button oneToOne, appointment, selfInfo;
    private String ip, phone_number, password;
    private int port;
    private Teacher teacher;
    private ArrayList<Student> studentArrayList = new ArrayList<Student>();
    private SelfInfoFragment selfInfoFragment;
    private StudentFragment studentFragment;
    private SettingFragment settingFragment;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        oneToOne = findViewById(R.id.one_to_one);
        appointment = findViewById(R.id.appointment);
        selfInfo = findViewById(R.id.selfInfo);
        getValues();
        //个人信息按钮点击事件
        selfInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    getTeacherInfo();
                    Thread.sleep(200);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if(selfInfoFragment == null) {
                    selfInfoFragment = new SelfInfoFragment();
                    selfInfoFragment = SelfInfoFragment.newInstance(teacher);
                    if(studentFragment == null){
                        if(settingFragment == null){
                            getFragmentManager().beginTransaction().add(R.id.main_ly, selfInfoFragment).commitNowAllowingStateLoss();
                        }
                        else{
                            getFragmentManager().beginTransaction().remove(settingFragment);
                            getFragmentManager().beginTransaction().replace(R.id.main_ly, selfInfoFragment).commitNowAllowingStateLoss();
                        }
                    }
                    else {
                        getFragmentManager().beginTransaction().remove(studentFragment);
                        getFragmentManager().beginTransaction().replace(R.id.main_ly, selfInfoFragment).commitNowAllowingStateLoss();
                    }
                }
                else {
                    getFragmentManager().beginTransaction().remove(selfInfoFragment);
                    getFragmentManager().beginTransaction().replace(R.id.main_ly, selfInfoFragment).commitNowAllowingStateLoss();
                }
//                getFragmentManager().beginTransaction().add(R.id.main_ly, selfInfoFragment).commitNowAllowingStateLoss();
            }
        });


        //预约的学生按钮点击事件
        appointment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    getCorRespondInfo();
                    Thread.sleep(200);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if(studentFragment == null) {
                    studentFragment = new StudentFragment();
                    studentFragment.newInstance(ip, port, studentArrayList);
                    if(selfInfoFragment == null){
                        if(settingFragment == null){
                            getFragmentManager().beginTransaction().add(R.id.main_ly, studentFragment).commitNowAllowingStateLoss();
                        }else {
                            getFragmentManager().beginTransaction().remove(settingFragment);
                            getFragmentManager().beginTransaction().replace(R.id.main_ly, studentFragment).commitNowAllowingStateLoss();
                        }
                    }
                    else {
                        getFragmentManager().beginTransaction().remove(selfInfoFragment);
                        getFragmentManager().beginTransaction().replace(R.id.main_ly, studentFragment).commitNowAllowingStateLoss();
                    }
                }
                else {
                    getFragmentManager().beginTransaction().remove(studentFragment);
                    getFragmentManager().beginTransaction().replace(R.id.main_ly, studentFragment).commitNowAllowingStateLoss();
                }
//                getFragmentManager().beginTransaction().add(R.id.main_ly, studentFragment).commitNowAllowingStateLoss();
            }
        });

        //设置按钮点击事件
        oneToOne.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(settingFragment == null){
                    settingFragment = new SettingFragment();
                    settingFragment.instance(phone_number);
                    if(selfInfoFragment == null){
                        if(studentFragment == null){
                            getFragmentManager().beginTransaction().add(R.id.main_ly, settingFragment).commitNowAllowingStateLoss();
                        }
                        else {
                            getFragmentManager().beginTransaction().remove(studentFragment);
                            getFragmentManager().beginTransaction().replace(R.id.main_ly, settingFragment).commitNowAllowingStateLoss();
                        }
                    }else {
                        getFragmentManager().beginTransaction().remove(selfInfoFragment);
                        getFragmentManager().beginTransaction().replace(R.id.main_ly, settingFragment).commitNowAllowingStateLoss();
                    }
                }
                else {
                    getFragmentManager().beginTransaction().remove(settingFragment);
                    getFragmentManager().beginTransaction().replace(R.id.main_ly, settingFragment).commitNowAllowingStateLoss();
                }
            }
        });


    }

    //获得登录界面中传过来的信息
    public void getValues() {
        teacher = new Teacher(null,null,null,null,null,0,0,null,null,null, 0);
        Intent intent = getIntent();
        ip = intent.getStringExtra("ip");
        port = intent.getIntExtra("port", 0);
        phone_number = intent.getStringExtra("teacherPhoneNumber");
        password = intent.getStringExtra("password");
//        teacher.setPhoneNumber(phone_number);
//        teacher.setPassword(password);
        Log.i("ip", "1:"+ip);
        Log.i("port", "2:"+port);
        Log.i("phone_number", "3:"+phone_number);
        Log.i("password", "4:"+password);
    }

    //从服务其中获取老师的信息
    private void getTeacherInfo(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    OkHttpClient client = new OkHttpClient();
                    Gson gson = new Gson();
                    String toJson = gson.toJson(phone_number);
                    RequestBody requestBody = RequestBody.create(JSON, toJson);
                    Request request = new Request.Builder().url("http://192.168.1.109:8080/teacher/getInfo").post(requestBody).build();
                    Response response = client.newCall(request).execute();
                    if(response.isSuccessful()){
                        String respondData = response.body().string();
                        teacher = gson.fromJson(respondData, Teacher.class);
                    }

                }catch (Exception e){
                    e.printStackTrace();
                }

            }
        }).start();
    }

//    从服务器中获取和该老师预约的学生信息
    private void getCorRespondInfo(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    OkHttpClient client = new OkHttpClient();
                    Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();
                    String toJson = gson.toJson(phone_number);
                    RequestBody requestBody = RequestBody.create(JSON, toJson);
                    Request request = new Request.Builder().url("http://192.168.1.109:8080/teacher/getOneToOneStudents").post(requestBody).build();
                    Response response = client.newCall(request).execute();
                    if(response.isSuccessful()){
                        String respondData = response.body().string();
                        studentArrayList = gson.fromJson(respondData, new TypeToken<ArrayList<Student>>(){}.getType());
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }).start();
    }

}
