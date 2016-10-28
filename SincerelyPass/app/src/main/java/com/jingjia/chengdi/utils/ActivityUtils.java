package com.jingjia.chengdi.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

/**
 * Created by Administrator on 2016/9/18.
 */
public class ActivityUtils {
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

}
