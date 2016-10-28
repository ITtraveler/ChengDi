package com.jingjia.chengdi.BroadCast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.jingjia.chengdi.utils.NetStatus;

/**
 * Created by Administrator on 2016/10/6.
 * 网络状态广播 ，得到网络类型 wifi、mobile等
 */
public class CDNetBroadCastReceive extends BroadcastReceiver {
    private OnNetStatusListener netStatusListener;

    public CDNetBroadCastReceive(OnNetStatusListener netStatusLinstener) {
        this.netStatusListener = netStatusLinstener;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        NetStatus netStatus = NetStatus.newNetSatus(context);
        netStatusListener.netCurType(netStatus.getNetStyle());
    }
}
