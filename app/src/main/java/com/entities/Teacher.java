package com.entities;

/**
 * Created by PF on 2018/5/6.
 */

public class Teacher {

    private Long id;
    private String name;
    private String phoneNumber;
    private String gender;
    private String password;
    private int teachTime;  //已经交了多少个小时了
    private int teachAge;   //教师工作了几年
//    private String alipay;  //支付表账号
//    private String wechat;  //微信账号
    private String zone;    //教师所在地
    private String subject; //所教的学科
    private String grade;   //所教的年级
    private int price;

    public Teacher(Long id, String name, String phoneNumber, String gender, String password, int teachTime, int teachAge, String zone, String subject, String grade, int price) {
        this.id = id;
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.gender = gender;
        this.password = password;
        this.teachTime = teachTime;
        this.teachAge = teachAge;
        this.zone = zone;
        this.subject = subject;
        this.grade = grade;
        this.price = price;
    }

    public Teacher(String name, String phoneNumber, String gender, String password) {
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.gender = gender;
        this.password = password;
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

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getTeachTime() {
        return teachTime;
    }

    public void setTeachTime(int teachTime) {
        this.teachTime = teachTime;
    }

    public int getTeachAge() {
        return teachAge;
    }

    public void setTeachAge(int teachAge) {
        this.teachAge = teachAge;
    }

    public String getZone() {
        return zone;
    }

    public void setZone(String zone) {
        this.zone = zone;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }
}
