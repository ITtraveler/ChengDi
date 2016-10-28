package com.jingjia.chengdi.encapsulation;

/**
 * Created by Administrator on 2016/9/20.
 *封装了用于主页列表显示的list数据
 */
public class HomeListContent {
    private String username;
    private String category;
    private String time;
    private String headUri;//头像的Uri地址
    private String task;
    private String commission;
    private String destination;

    public HomeListContent() {
    }

    public HomeListContent(String username, String category, String time, String headUri, String task, String commission, String destination) {
        this.username = username;
        this.category = category;
        this.time = time;
        this.headUri = headUri;
        this.task = task;
        this.commission = commission;
        this.destination = destination;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getHeadUri() {
        return headUri;
    }

    public void setHeadUri(String headUri) {
        this.headUri = headUri;
    }

    public String getTask() {
        return task;
    }

    public void setTask(String task) {
        this.task = task;
    }

    public String getCommission() {
        return commission;
    }

    public void setCommission(String commission) {
        this.commission = commission;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }
}
