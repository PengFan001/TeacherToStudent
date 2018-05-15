package com.tts;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.entities.Student;
import com.entities.StudentAdapter;

import java.util.ArrayList;
import java.util.List;

public class TeacherActivity extends AppCompatActivity {

    /**
     * param students 只是模拟，实际数据从服务器调用
     */
    private String teacherId = "001"; //实际程序中有phone_number和password从数据库中查询的得到
    private RecyclerView studentList;
    private List<Student> students = new ArrayList<Student>();
    private String ip, phone_number, password;
    private int port;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher);
        getValues();
        initView();
    }

//    private void initStudent() {
////        Student student1 = new Student("彭帆", "13397595523");
////        students.add(student1);
//        Student student1 = new Student("陈琪凯", "18800296791");
//        students.add(student1);
//        Student student2 = new Student("李佳楠", "14967898584");
//        students.add(student2);
//    }

    private void initView() {
//        initStudent();
        studentList = findViewById(R.id.studentList);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        studentList.setLayoutManager(layoutManager);
        StudentAdapter adapter = new StudentAdapter(students);
        studentList.setAdapter(adapter);
        adapter.setOnItemClickListener(new StudentAdapter.OnItemClickListener() {
            @Override
            public void onClick(int position) {
                Intent intent = new Intent(TeacherActivity.this, ChatActivity.class);
                intent.putExtra("studentId", "002");
                intent.putExtra("ip", ip);
                intent.putExtra("port", port);
                intent.putExtra("teacherId", teacherId);
                startActivity(intent);
            }

            @Override
            public void onLongClick(int position) {

            }
        });
    }

    public void getValues() {
        Intent intent = getIntent();
        ip = intent.getStringExtra("ip");
        port = intent.getIntExtra("port", 0);
        phone_number = intent.getStringExtra("teacherPhoneNumber");
        password = intent.getStringExtra("password");
        Log.i("ip", "1:"+ip);
        Log.i("port", "2:"+port);
        Log.i("phone_number", "3:"+phone_number);
        Log.i("password", "4:"+password);
    }
}
