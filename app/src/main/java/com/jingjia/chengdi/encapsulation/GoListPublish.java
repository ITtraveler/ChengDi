package com.jingjia.chengdi.encapsulation;

/**
 * Created by Administrator on 2016/9/23.
 * 用于显示 我的发布list的信息
 */
public class GoListPublish {
    private String time;
    private String state;
    private String task;
    private String commission;
    private String destination;
    private String acceptor;
    private String acceptorphone;

    public GoListPublish() {
    }

    public GoListPublish(String time, String state, String task, String commission, String destination, String acceptor, String acceptorphone) {
        this.time = time;
        this.state = state;
        this.task = task;
        this.commission = commission;
        this.destination = destination;
        this.acceptor = acceptor;
        this.acceptorphone = acceptorphone;
    }

    public String getAcceptorphone() {
        return acceptorphone;
    }

    public void setAcceptorphone(String acceptorphone) {
        this.acceptorphone = acceptorphone;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
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

    public String getAcceptor() {
        return acceptor;
    }

    public void setAcceptor(String acceptor) {
        this.acceptor = acceptor;
    }
}
