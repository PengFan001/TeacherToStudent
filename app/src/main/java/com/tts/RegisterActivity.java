package com.tts;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import com.entities.Teacher;
import com.google.gson.Gson;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class RegisterActivity extends AppCompatActivity {

    public static final MediaType JSON = null;
    private String resultMessage = null;
    private RadioGroup genderGroup;
    private RadioButton radioButton;
    private EditText name_et;
    private EditText phoneNumber_et;
    private EditText password_et;
    private EditText identityPassword_et;
    private EditText teacherAge_et;
    private EditText zone_et;
//    private EditText wechat_et;
//    private EditText alipay_et;
    private Spinner subject_sp;
    private Spinner grade_sp;
    private Spinner price_sp;
    private String gender = "男";
    private Button submit;
    private Teacher teacher;
    private String name, phoneNumber, password, identityPassword, subject, grade, zone;
    private int price, teacherAge;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        register();
    }

    private void register() {
        submit = findViewById(R.id.submit);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                initValues();
                if(!isNull(name, phoneNumber, password, identityPassword, gender, zone)){
                    if(identityPassword(password, identityPassword)){
                        //此处很重要，设置等待后才能实现数据的同步
                        try {
                            postTeacherList();
                            Thread.sleep(300);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }

                        if(resultMessage == null)
                            resultMessage = "registerFail";
                        if(resultMessage.equals("existTeacher"))
                            Toast.makeText(RegisterActivity.this, "用户已存在", Toast.LENGTH_LONG).show();
                        if(resultMessage.equals("registerSuccessfully")){
                            Toast.makeText(RegisterActivity.this, "注册成功", Toast.LENGTH_LONG).show();
                            Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                            startActivity(intent);
                        }
                        if(resultMessage.equals("registerFail"))
                            Toast.makeText(RegisterActivity.this, "注册失败", Toast.LENGTH_LONG).show();
                    }
                    else
                        Toast.makeText(RegisterActivity.this, "密码不一致", Toast.LENGTH_LONG).show();
                }
                else
                    Toast.makeText(RegisterActivity.this, "除教龄以外信息不能为空且确保手机号的合法性", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void initValues() {
        teacher = new Teacher(null,null,null,null,null,0,0,null,null,null, 0);
        name_et = findViewById(R.id.name);
        phoneNumber_et = findViewById(R.id.phoneNumber);
        password_et = findViewById(R.id.password);
        identityPassword_et = findViewById(R.id.identityPassword);
        zone_et = findViewById(R.id.zone);
        teacherAge_et = findViewById(R.id.teachAge);
//        alipay_et = findViewById(R.id.alipay);
//        wechat_et = findViewById(R.id.wechat);
        subject_sp = findViewById(R.id.subjectSpinner);
        price_sp = findViewById(R.id.priceSpinner);
        grade_sp = findViewById(R.id.gradeSpinner);

        //获取用户输入的值
        this.name = name_et.getText().toString();
        this.phoneNumber = phoneNumber_et.getText().toString();
        this.password = password_et.getText().toString();
        this.identityPassword = identityPassword_et.getText().toString();

        if(teacherAge_et.getText().toString().equals(""))
            teacherAge = 0;
        else
            this.teacherAge  = Integer.parseInt(teacherAge_et.getText().toString());

        this.zone = zone_et.getText().toString();
        this.grade = grade_sp.getSelectedItem().toString();
//        this.wechat = wechat_et.getText().toString();
//        this.alipay = alipay_et.getText().toString();
        this.price = Integer.parseInt(price_sp.getSelectedItem().toString());
        this.subject = subject_sp.getSelectedItem().toString();

        //获取用户选择的性别
        genderGroup = findViewById(R.id.gender);
        genderGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int checkId) {
                radioButton = genderGroup.findViewById(checkId);
                gender = radioButton.getText().toString();
                Log.i("gender", "gender:"+gender);
            }
        });

        //将值写入Teacher 实例中去
        teacher.setId(null);
        teacher.setName(name);
        teacher.setPhoneNumber(phoneNumber);
        teacher.setPassword(password);
        teacher.setGender(gender);
//        teacher.setAlipay(alipay);
//        teacher.setWechat(wechat);
        teacher.setTeachAge(teacherAge);
        teacher.setTeachTime(0);
        teacher.setGrade(grade);
        teacher.setSubject(subject);
        teacher.setPrice(price);
        teacher.setZone(zone);
    }

    //确定两次输入的密码是否一致
    private boolean identityPassword(String password, String identityPassword){
        if(password.equals(identityPassword))
            return true;
        else
            return false;
    }

    //判断输入的信息是否为空
    private boolean isNull(String name, String phoneNumber, String password, String identityPassword, String gender, String zone){
        boolean isNull = false;
        int length = phoneNumber.length();
        if (length != 11)
            isNull = true;
        if(name.equals(""))
            isNull = true;
        if(password.equals(""))
            isNull = true;
        if(phoneNumber.equals(""))
            isNull = true;
        if(identityPassword.equals(""))
            isNull = true;
        if(gender.equals(""))
            isNull = true;
        if (zone.equals(""))
            isNull = true;
        return isNull;
    }

    //将用户注册的信息发个后端
    private void postTeacherList(){
//        final String registerMessage = null;
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    OkHttpClient client = new OkHttpClient();
                    Gson gson = new Gson();
                    String toJson = gson.toJson(teacher);
                    RequestBody requestBody = RequestBody.create(JSON, toJson);
                    Request request = new Request.Builder().url("http://192.168.1.109:8080/teacher/register").post(requestBody).build();
                    Response response = client.newCall(request).execute();
                    if(response.isSuccessful()){
                        String respondData = response.body().string();
                        resultMessage = gson.fromJson(respondData, String.class);
                    }

                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }).start();
    }

    //后端返回注册是否成功
//    private void isRegister(String respondData) {
//        Gson gson = new Gson();
//        String registerMessage = gson.fromJson(respondData, String.class);
//        Toast.makeText(RegisterActivity.this, registerMessage, Toast.LENGTH_LONG).show();
//        Log.i("registerMessage", "registerMessage:"+ registerMessage);
//    }

}
