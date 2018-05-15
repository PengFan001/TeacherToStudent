package com.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.entities.Teacher;
import com.tts.R;

/**
 * Created by PF on 2018/5/12.
 */

public class SelfInfoFragment extends Fragment {

    private TextView image;
    private TextView name;
    private TextView phone;
    private TextView gender;
    private TextView teacherAge;
    private TextView zone;
    private TextView subject;
    private TextView grade;
    private TextView price;

    public static SelfInfoFragment newInstance(Teacher teacher){
        SelfInfoFragment selfInfoFragment = new SelfInfoFragment();
        Bundle bundle = new Bundle();
        bundle.putString("name", teacher.getName());
        bundle.putString("phone", teacher.getPhoneNumber());
        bundle.putString("gender", teacher.getGender());
        bundle.putInt("teacherAge", teacher.getTeachAge());
        bundle.putString("zone", teacher.getZone());
        bundle.putString("grade", teacher.getGrade());
        bundle.putString("subject", teacher.getSubject());
        bundle.putInt("price", teacher.getPrice());
        selfInfoFragment.setArguments(bundle);
        return selfInfoFragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_self_info, container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        image = view.findViewById(R.id.image1);
        name = view.findViewById(R.id.name1);
        phone = view.findViewById(R.id.phoneNumber1);
        gender = view.findViewById(R.id.gender1);
        teacherAge = view.findViewById(R.id.teacherAge1);
        zone = view.findViewById(R.id.zone1);
        subject = view.findViewById(R.id.subject1);
        grade = view.findViewById(R.id.grade1);
        price = view.findViewById(R.id.price1);
        setValues();

    }

    private void setValues() {
        if(getArguments() != null){
            name.setText("姓名:   "+getArguments().getString("name"));
            phone.setText("手机号:   "+getArguments().getString("phone"));
            gender.setText("性别:   "+getArguments().getString("gender"));
            teacherAge.setText("教龄:   "+Integer.toString(getArguments().getInt("teacherAge")));
            zone.setText("所在地:   "+getArguments().getString("zone"));
            subject.setText("所教学科:   "+getArguments().getString("subject"));
            grade.setText("所教年级:   "+getArguments().getString("grade"));
            price.setText("价格:   "+Integer.toString(getArguments().getInt("price")));
        }
        else {
            Log.i("error", "error:"+ "参数未传过来");
        }
    }
}
