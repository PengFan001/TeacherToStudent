package com.entities;

import java.util.Date;

/**
 * Created by PF on 2018/5/4.
 */

public class Student {

    private String name;
    private String phoneNumber;
    private Date date;

    public Student(String name, String phoneNumber, Date date) {
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.date = date;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
