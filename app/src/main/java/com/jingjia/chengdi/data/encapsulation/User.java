package com.jingjia.chengdi.data.encapsulation;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/10/2.
 * 用户数据表
 */
public class User implements Serializable{
    private static final long userUid = 200L;
    private int id;
    private String username;
    private String password;
    private String portrait;
    private String phone;
    private String name;
    private String sex;//用于使String类型，且数据库中是用0表示男，1表示女，因此这里要做下处理

    private String education;
    // //date
    private String location;
    private String hometown;
    private int score;
    private String subScript;
    private int haveAuthentication;
    private int age;
    private String job;

    public User() {
    }


    public User(int id, String username, String password, String portrait, String phone, String name, String sex, String education, String location, String hometown, int score, String subScript, int haveAuthentication, int age, String job) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.portrait = portrait;
        this.phone = phone;
        this.name = name;
        this.sex = sex;
        this.education = education;
        this.location = location;
        this.hometown = hometown;
        this.score = score;
        this.subScript = subScript;
        this.haveAuthentication = haveAuthentication;
        this.age = age;
        this.job = job;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPortrait() {
        return portrait;
    }

    public void setPortrait(String portrait) {
        this.portrait = portrait;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        if (sex.equals("0") || sex.equals("男"))
            this.sex = "男";
        else
            this.sex = "女";
    }

    public String getEducation() {
        return education;
    }

    public void setEducation(String education) {
        this.education = education;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getHometown() {
        return hometown;
    }

    public void setHometown(String hometown) {
        this.hometown = hometown;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public String getSubScript() {
        return subScript;
    }

    public void setSubScript(String subScript) {
        this.subScript = subScript;
    }

    public int getHaveAuthentication() {
        return haveAuthentication;
    }

    public void setHaveAuthentication(int haveAuthentication) {
        this.haveAuthentication = haveAuthentication;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getJob() {
        return job;
    }

    public void setJob(String job) {
        this.job = job;
    }

    @Override
    public String toString() {
        return "User [id=" + id + ", username=" + username + ", password="
                + password + ", portrait=" + portrait + ", phone=" + phone
                + ", name=" + name + ", sex=" + sex + ", age=" + age
                + ", education=" + education + ", location="
                + location + ", hometown=" + hometown + ", subScript="
                + subScript + ", job=" + job + ", score=" + score
                + ", haveAuthentication=" + haveAuthentication + "]";
    }

    public void clear(){
        id = 0;
        username = "";
        password = "";
        portrait = "";
        phone = "";
        name = "";
        sex = "";
        education = "";
        location = "";
        hometown = "";
        score = 0;
        subScript = "";
        haveAuthentication = 0;
        age = 0;
        job = "";
    }
}
