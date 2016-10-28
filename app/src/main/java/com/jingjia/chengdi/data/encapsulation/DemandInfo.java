package com.jingjia.chengdi.data.encapsulation;

import com.jingjia.chengdi.data.DemandStatus;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/10/3.
 * 发布信息的内容封装，提交到服务器端
 */

public class DemandInfo implements Serializable {
    private static final long demandInfoUid = 100l;
    private int id;
    private String userId;
    private String category;
    private String limittime;
    private double money;
    private String content;
    private String remark;
    private String destination;
    private String destinationRemark;

    //时间字段
    private String publishTime;
    private String phone;
    private String acceptancePhone;
    private User user;
    private String status;



    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getLimittime() {
        return limittime;
    }

    public void setLimittime(String limittime) {
        this.limittime = limittime;
    }

    public double getMoney() {
        return money;
    }

    public void setMoney(double money) {
        this.money = money;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String location) {
        this.destination = location;
    }

    public String getDestinationRemark() {
        return destinationRemark;
    }

    public void setDestinationRemark(String locationRemark) {
        this.destinationRemark = locationRemark;
    }

    public String getPublishTime() {
        return publishTime;
    }

    public void setPublishTime(String publishTime) {
        this.publishTime = publishTime;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getStatus() {
        DemandStatus[] demandStatuses = DemandStatus.values();
        return demandStatuses[Integer.parseInt(status)].name();
    }

    public void setStatus(String status) {
        //DemandStatus[] demandStatuses = DemandStatus.values();
        this.status = status;///demandStatuses[Integer.parseInt(status)].name();
    }
    public String getAcceptancePhone() {
        return acceptancePhone;
    }

    public void setAcceptancePhone(String acceptancePhone) {
        this.acceptancePhone = acceptancePhone;
    }

    @Override
    public String toString() {
        return "DemandInfo{" +
                "id=" + id +
                ", userId='" + userId + '\'' +
                ", category='" + category + '\'' +
                ", limittime='" + limittime + '\'' +
                ", money=" + money +
                ", content='" + content + '\'' +
                ", remark='" + remark + '\'' +
                ", destination='" + destination + '\'' +
                ", destinationRemark='" + destinationRemark + '\'' +
                ", publishTime='" + publishTime + '\'' +
                ", phone='" + phone + '\'' +
                ", acceptancePhone='" + acceptancePhone + '\'' +
                ", user="  +
                ", status='" + status + '\'' +
                '}';
    }
}
