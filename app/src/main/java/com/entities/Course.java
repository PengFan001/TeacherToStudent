package com.entities;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by PF on 2018/5/17.
 */

public class Course {

    private String teacherPhone;
    private Date date;

    public String getTeacherPhone() {
        return teacherPhone;
    }

    public void setTeacherPhone(String teacherPhone) {
        this.teacherPhone = teacherPhone;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date dates) {
        this.date = dates;
    }

}
