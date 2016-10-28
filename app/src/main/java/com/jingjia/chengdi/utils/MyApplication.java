package com.jingjia.chengdi.utils;

import android.app.Application;

import com.jingjia.chengdi.data.encapsulation.DemandInfo;
import com.jingjia.chengdi.data.encapsulation.Location;
import com.jingjia.chengdi.data.encapsulation.RegistInfo;
import com.jingjia.chengdi.data.encapsulation.User;
import com.jingjia.chengdi.data.listener.AcceptedDemandChangeListener;
import com.jingjia.chengdi.data.listener.AroundDemandListener;
import com.jingjia.chengdi.data.listener.PublishDemandListener;
import com.zhy.http.okhttp.OkHttpUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;

/**
 * Created by Administrator on 2016/9/24.
 */
public class MyApplication extends Application {
    public static Location location = new Location();
    //public static boolean LoginStatus = true;//登录状态
    public static String netStatus;//网络状态
    public static RegistInfo registInfo = new RegistInfo();

    public static User user = new User();
    /**
     * 附近所有的需求数据
     */
    public static List<DemandInfo> aroundDemandList = new ArrayList<>();
    /**
     * 我的需求数据
     */
    public static List<DemandInfo> myDemandList = new ArrayList<>();
    public static List<DemandInfo> myDemandListComp = new ArrayList<>();
    //发布需求监听器
    private List<PublishDemandListener> publishDemandListenerList = new ArrayList<>();

    /**
     * 我的接单数据
     */
    public static List<DemandInfo> myAcceptedList = new ArrayList<>();
    public static List<DemandInfo> myAcceptedListComp = new ArrayList<>();
    private List<AcceptedDemandChangeListener> acceptedDemandChangeListenerList = new ArrayList<>();
    /**
     * 周边需求
     */
    //周边所有需求监听器
    private List<AroundDemandListener> aroundDemandListenerList = new ArrayList<>();

    //  private static PublishDemandListener pdListener;
    private static MyApplication mApplication;

    public static MyApplication newInstance() {
        if (mApplication == null)
            mApplication = new MyApplication();
        return mApplication;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        initOKHttp();
    }

    /************我的需求****************/
    /**
     * 添加一个监听器
     *
     * @param pdListener
     */
    public void addDemandListener(PublishDemandListener pdListener) {
        publishDemandListenerList.add(pdListener);
        //publishDemandListenerList.add(pdListener);
    }

    /**
     * 添加一个需求信息，除非 监听器
     * MyDemand的数据
     *
     * @param demandInfo
     */
    public void addMyDemand(DemandInfo demandInfo) {
        myDemandList.add(0, demandInfo);
        for (PublishDemandListener pdListener : publishDemandListenerList)
            pdListener.haveMyDemand(demandInfo);
    }

    public void addMyDemands(List<DemandInfo> myDemandList) {
        MyApplication.myDemandList.addAll(myDemandList);
        for (PublishDemandListener pdListener : publishDemandListenerList)
            pdListener.haveMyDemands(myDemandList);
    }

    public void removeAllMyDemand() {
        MyApplication.myDemandList.clear();
        MyApplication.myDemandListComp.clear();
        for (PublishDemandListener pdListener : publishDemandListenerList)
            pdListener.removeAll();
    }


    /**
     * 添加我的完成的数据
     *
     * @param myDemandListComp
     */
    public void addMyDemandComps(List<DemandInfo> myDemandListComp) {
        MyApplication.myDemandListComp.addAll(myDemandListComp);
        for (PublishDemandListener pdListener : publishDemandListenerList)
            pdListener.haveMyDemandComps(myDemandListComp);
    }

    /**
     * 通知刷新我的发布数据
     */
    public void notificationFlashPublish() {
        for (PublishDemandListener pdListener : publishDemandListenerList)
            pdListener.notificationFlash();
    }
    /***************我的接单*************/
    /**
     * 添加我接受订单数据改变监听器
     *
     * @param adcListener
     */
    public void addAcceptedChangeListener(AcceptedDemandChangeListener adcListener) {
        acceptedDemandChangeListenerList.add(adcListener);
    }

    public void addMyAcceptedDemands(List<DemandInfo> myAcceptedList) {
        MyApplication.myAcceptedList.addAll(myAcceptedList);
        for (AcceptedDemandChangeListener changeListener : acceptedDemandChangeListenerList) {
            changeListener.acceptingDemandChange();
        }
    }

    public void addMyAcceptedDemandComps(List<DemandInfo> myAcceptedList) {
        MyApplication.myAcceptedListComp.addAll(myAcceptedList);
        for (AcceptedDemandChangeListener changeListener : acceptedDemandChangeListenerList) {
            changeListener.acceptedDemandChange();
        }
    }

    public void removeAllMyAcceptance() {
        MyApplication.myAcceptedListComp.clear();
        MyApplication.myAcceptedList.clear();
        for (AcceptedDemandChangeListener changeListener : acceptedDemandChangeListenerList) {
            changeListener.removeAll();
        }
    }

    //通知刷新数据
    public void notificationFlashAcceptance() {
        for (AcceptedDemandChangeListener changeListener : acceptedDemandChangeListenerList) {
            changeListener.notificationFlash();
        }
    }
/***********周围需求*****************/
    /**
     * 添加周围需求监听器
     *
     * @param aroundDemandListener
     */
    public void addAroundDemandListener(AroundDemandListener aroundDemandListener) {
        aroundDemandListenerList.add(aroundDemandListener);
    }

    /**
     * 将网络获取到的周边需求添加到内存中
     *
     * @param demandList
     */
    public void addAroundDemands(List<DemandInfo> demandList) {
        aroundDemandList.addAll(demandList);
        for (AroundDemandListener aroundDemandListener : aroundDemandListenerList) {
            aroundDemandListener.haveAroundDemand(demandList);
        }
    }

    public void removeAroundDemand(int position) {
        aroundDemandList.remove(position);
        for (AroundDemandListener aroundDemandListener : aroundDemandListenerList) {
            aroundDemandListener.haveAroundDemandRemoved(position);
        }
    }

    /**
     * 对网络的配置
     */
    private void initOKHttp() {
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
//                .addInterceptor(new LoggerInterceptor("TAG"))
                .connectTimeout(10000L, TimeUnit.MILLISECONDS)
                .readTimeout(10000L, TimeUnit.MILLISECONDS)
                //其他配置
                .build();

        OkHttpUtils.initClient(okHttpClient);
    }

    public void init() {
        user.clear();
        removeAllMyDemand();
        removeAllMyAcceptance();
    }
}
