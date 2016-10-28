package com.jingjia.chengdi.data.encapsulation;

/**
 * Created by Administrator on 2016/10/2.
 * 注册信息的封装
 */
public class RegistInfo {
    private String phone;
    private String password;
    private String username;

    public RegistInfo() {
    }

    public RegistInfo(String phone, String password, String username) {
        this.phone = phone;
        this.password = password;
        this.username = username;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public String toString() {
        return "RegistInfo{" +
                "phone='" + phone + '\'' +
                ", password='" + password + '\'' +
                ", username='" + username + '\'' +
                '}';
    }
}
