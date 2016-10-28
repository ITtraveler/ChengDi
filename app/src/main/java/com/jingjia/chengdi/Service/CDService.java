package com.jingjia.chengdi.Service;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;

/**
 * Created by Administrator on 2016/10/6.
 */
public class CDService extends IntentService {
    public static final String TAG = "CDService";

    public static Intent newIntent(Context context) {
        return new Intent(context, CDService.class);
    }

    public CDService(){
        super(TAG);
    }

    @Override
    protected void onHandleIntent(Intent intent) {

    }



}
