package com.jingjia.chengdi.encapsulation;

/**
 * Created by Administrator on 2016/9/19.
 * 封装的数据用于进行中界面的list
 */
public class GoListAcceptance {
    private String category;//类别
    private String state;//状态
    private String task;//任务
    private String commission;//佣金
    private String destination;//目的地

    public GoListAcceptance() {
    }

    public GoListAcceptance(String category, String status, String task, String commission, String distination) {
        this.category = category;
        this.state = status;
        this.task = task;
        this.commission = commission;
        this.destination = distination;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getStatus() {
        return state;
    }

    public void setStatus(String status) {
        this.state = status;
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
