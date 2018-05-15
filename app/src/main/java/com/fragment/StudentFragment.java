package com.fragment;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.entities.Student;
import com.entities.StudentAdapter;
import com.tts.ChatActivity;
import com.tts.R;

import java.util.ArrayList;


/**
 * Created by PF on 2018/5/13.
 */

public class StudentFragment extends Fragment{

    private String teacherId = "001";
    private RecyclerView studentList;
    private TextView textView;
    private ArrayList<Student> studentArrayList = new ArrayList<Student>();
    private String ip;
    private int port;

    public void newInstance(String ip, int port, ArrayList<Student> studentArrayList){
        this.ip = ip;
        this.port = port;
        this.studentArrayList =studentArrayList;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_student_info, container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        textView = view.findViewById(R.id.title);
        studentList = view.findViewById(R.id.studentList1);
        LinearLayoutManager layoutManager = new LinearLayoutManager(view.getContext());
        studentList.setLayoutManager(layoutManager);
        StudentAdapter studentAdapter = new StudentAdapter(studentArrayList);
        studentList.setAdapter(studentAdapter);
        studentAdapter.setOnItemClickListener(new StudentAdapter.OnItemClickListener() {
            @Override
            public void onClick(int position) {
                Intent intent = new Intent(getActivity(), ChatActivity.class);
                intent.putExtra("ip", ip);
                intent.putExtra("port", port);
                intent.putExtra("teacherId", teacherId);
                intent.putExtra("studentId", "002");
                startActivity(intent);
            }

            @Override
            public void onLongClick(int position) {

            }
        });
    }
}
