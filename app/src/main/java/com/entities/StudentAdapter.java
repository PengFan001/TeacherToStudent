package com.entities;


import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.tts.R;

import java.text.SimpleDateFormat;
import java.util.List;

/**
 * Created by PF on 2018/5/4.
 */

public class StudentAdapter extends RecyclerView.Adapter<StudentAdapter.ViewHolder>{

    private List<Student> students;
    private OnItemClickListener mOnItemClickListener;

    static class ViewHolder extends  RecyclerView.ViewHolder {
        View studentView;
        TextView name, phone, date;
        public ViewHolder(View itemView) {
            super(itemView);
            studentView = itemView;
            name = itemView.findViewById(R.id.name);
            phone = itemView.findViewById(R.id.phone);
            date = itemView.findViewById(R.id.date);
        }
    }

    public StudentAdapter(List<Student> students) {
        this.students = students;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_layout_item_1, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        Student student = students.get(position);
        holder.name.setText(student.getName());
        holder.phone.setText(student.getPhoneNumber());
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        holder.date.setText( simpleDateFormat.format(student.getDate()));
        if(mOnItemClickListener != null){
            holder.studentView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mOnItemClickListener.onClick(position);
                }
            });
            holder.studentView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    mOnItemClickListener.onLongClick(position);
                    return false;
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return students.size();
    }


    /**
     * 此处给RecycleView设置点击事件，实现页面跳转
     */
    public interface OnItemClickListener{
        void onClick( int position);
        void onLongClick( int position);
    }
    public void setOnItemClickListener(OnItemClickListener onItemClickListener){
        this.mOnItemClickListener=onItemClickListener;
    }

}
