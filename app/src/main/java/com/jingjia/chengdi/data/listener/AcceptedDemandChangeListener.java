package com.jingjia.chengdi.data.listener;

/**
 * Created by Administrator on 2016/10/17.
 */
public interface AcceptedDemandChangeListener {
    void acceptedDemandChange();
    void acceptingDemandChange();
    void removeAll();
    void notificationFlash();
}
