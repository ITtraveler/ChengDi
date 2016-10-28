package com.jingjia.chengdi.utils;

import android.annotation.TargetApi;
import android.content.ContentValues;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;

/**
 * Created by Administrator on 2016/10/6.
 */
public class NetStatus {
    private static NetStatus netUtils;
    private final ConnectivityManager cm;
    public static final String NET_TYPE_MOBILE = "MOBILE";
    public static final String NET_TYPE_WIFI = "WIFI";
    public static final String NET_DISCONNECTED = "DISCONNECTED";

    public static NetStatus newNetSatus(Context context) {
        if (netUtils == null) {
            netUtils = new NetStatus(context);
        }
        return netUtils;
    }

    private Context mContext;

    public NetStatus(Context context) {
        this.mContext = context;
        cm = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);

    }

    /**
     * 检测网络连接状态
     *
     * @return
     */
    public boolean isNetworkAvailableAndConnected() {
        boolean isNetworkAvailable = cm.getActiveNetworkInfo() != null;
        boolean isConnected = false;
        if (isNetworkAvailable) {
            isConnected = cm.getActiveNetworkInfo().isConnected();
        }
        return isConnected;
    }

    /**
     * 约定WIFI状态加载广告，其他情况不加载。
     *
     * @return
     */
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public String getNetStyle() {
        if (isNetworkAvailableAndConnected()) {
            NetworkInfo netInfo = cm.getActiveNetworkInfo();
            System.out.println("netType:" + netInfo.getTypeName() + "   state:" + netInfo.getState().name());
            return netInfo.getTypeName();
        }
        return NET_DISCONNECTED;
    }


}
