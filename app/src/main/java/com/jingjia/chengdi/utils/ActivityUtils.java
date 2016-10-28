package com.jingjia.chengdi.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.text.format.DateFormat;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jingjia.chengdi.data.encapsulation.DemandInfo;

import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.Timer;

/**
 * Created by Administrator on 2016/9/18.
 */
public class ActivityUtils {
    private static ActivityUtils activityUtils;

    public static ActivityUtils newInstance() {
        if (activityUtils == null) {
            activityUtils = new ActivityUtils();
        }
        return activityUtils;
    }

    public static void startActivity(Context context, Class c) {
        Intent intent = new Intent(context, c);
        context.startActivity(intent);
    }

    public static void startActivityForResult(Context context, Class c) {
        Intent intent = new Intent(context, c);

    }

    public static void toast(Context context, String content) {
        Toast.makeText(context, content, Toast.LENGTH_SHORT).show();
    }

    /**
     * @param context
     * @param loginStatus
     * @param username    这里的username指电话phone
     */
    public void setLoginStatus(Context context, boolean loginStatus, String username) {
        SharedPreferences sp = context.getSharedPreferences("cdLogin", Context.MODE_PRIVATE);
        SharedPreferences.Editor e = sp.edit();
        e.putBoolean("LOGIN_STATUS", loginStatus);
        e.putString("PHONE", username);
        e.apply();
    }

    public boolean getLoginStatus(Context context) {
        SharedPreferences sp = context.getSharedPreferences("cdLogin", Context.MODE_PRIVATE);
        return sp.getBoolean("LOGIN_STATUS", false);
    }

    public String getLoginPhone(Context context) {
        SharedPreferences sp = context.getSharedPreferences("cdLogin", Context.MODE_PRIVATE);
        return sp.getString("PHONE", "");
    }

    private static void sharedPreference(Context context, String name, String key, String value) {
        SharedPreferences sp = context.getSharedPreferences("ChengDi", Context.MODE_PRIVATE);
        SharedPreferences.Editor e = sp.edit();
        e.putString(key, value);
        e.apply();
    }

    public static String getTime() {
        Date date = new Date();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        return df.format(date);
    }

    public static LinkedList<DemandInfo> parasJsonDemandArray(String content) {
        Type type = new TypeToken<LinkedList<DemandInfo>>() {
        }.getType();
        Gson gson = new Gson();
        LinkedList<DemandInfo> demandInfos = gson.fromJson(content, type);
        return demandInfos;
    }

    public static void startCallPhone(Context context, String phone) {
        Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + phone));
        context.startActivity(intent);
    }
}
