package com.tts;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.entities.Teacher;
import com.google.gson.Gson;

import org.w3c.dom.Text;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class LoginActivity extends AppCompatActivity {

    public static final MediaType JSON = null;
    private boolean isLogin = false;
    private EditText phone_number, password;
    private Button login, register;
    private String ip = "203.76.212.115";
    private int port = 12345;
    private Teacher  teacher = new Teacher(null,null,null,null,null,0,0,null,null,null, 0);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initView();
    }

    private void initView() {
        phone_number = findViewById(R.id.phone_number);
        password = findViewById(R.id.password);
        login = findViewById(R.id.login);
        register = findViewById(R.id.register);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                intent.putExtra("teacherPhoneNumber",phone_number.getText().toString());
                intent.putExtra("password", password.getText().toString());
                intent.putExtra("ip", ip);
                intent.putExtra("port", port);
                teacher.setPhoneNumber(phone_number.getText().toString());
                teacher.setPassword(password.getText().toString());

                //此处很重要，设置等待后才能实现数据的同步
                try {
                    isLogin();
                    Thread.sleep(300);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
//
//                startActivity(intent);
                if(isLogin)
                    startActivity(intent);
                else
                    Toast.makeText(LoginActivity.this, "手机号或密码错误", Toast.LENGTH_LONG).show();
            }
        });

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });
    }

    private void isLogin() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    OkHttpClient client = new OkHttpClient();
                    Gson gson = new Gson();
                    String toJson = gson.toJson( teacher);
                    RequestBody requestBody = RequestBody.create(JSON, toJson);
                    Request request = new Request.Builder().url("http://192.168.1.109:8080/teacher/login").post(requestBody).build();
                    Response response = client.newCall(request).execute();
                    if(response.isSuccessful()){
                        String respondData = response.body().string();
                        isLogin = gson.fromJson(respondData, Boolean.class);
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }).start();
    }
}
