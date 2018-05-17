package com.fragment;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.entities.Student;
import com.tts.CalendarActivity;
import com.tts.R;
import java.util.ArrayList;
import java.util.Date;


/**
 * Created by PF on 2018/5/14.
 */

public class SettingFragment extends Fragment implements CalendarActivity.CallBack{

    private TextView set1;
    private String phone;
    private ArrayList<Date> dateArrayList = new ArrayList<Date>();

    public void instance(String phone){
        this.phone = phone;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_setting, container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {

        set1 = view.findViewById(R.id.set1);
        set1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), CalendarActivity.class);
                intent.putExtra("phone", phone);
                startActivity(intent);
            }
        });
    }

    @Override
    public void callBack(ArrayList<Date> dates) {
        this.dateArrayList = dates;
    }
}
