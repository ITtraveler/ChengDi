package com.jingjia.chengdi;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jingjia.chengdi.data.CDDbSchema;
import com.jingjia.chengdi.data.NetUri;
import com.jingjia.chengdi.data.encapsulation.User;
import com.jingjia.chengdi.dialog.UpdatePSWDialog;
import com.jingjia.chengdi.utils.ActivityUtils;
import com.jingjia.chengdi.utils.MyApplication;
import com.jingjia.chengdi.utils.NetUtils;
import com.jingjia.chengdi.utils.SQLiteDbUtils;
import com.jingjia.chengdi.utils.UpdateData;
import com.zhy.http.okhttp.callback.Callback;
import com.zhy.http.okhttp.callback.StringCallback;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Response;

/**
 * Created by Administrator on 2016/9/25.
 * 登录界面
 */
public class LoginActivity extends AppCompatActivity {

    private EditText et_username, et_psw;
    //    private String curUserPhone;
    private String username;//此处的username皆为手机号
    private SQLiteDbUtils dbUtils;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        et_username = (EditText) findViewById(R.id.login_username);
        et_psw = (EditText) findViewById(R.id.login_psw);
        dbUtils = new SQLiteDbUtils(getApplicationContext());
    }


    public void login(View view) {
        username = et_username.getText().toString().trim();
        String psw = et_psw.getText().toString().trim();
        if (checkContent(username, psw)) {
            // ActivityUtils.startActivity(LoginActivity.this, MainActivity.class);

            //登录，将数据提交到服务器
            Map<String, String> params = new HashMap<>();
            params.put("phone", username);
            params.put("password", psw);
            NetUtils.post(NetUri.URI_LOGIN, params, new Callback() {
                @Override
                public Object parseNetworkResponse(Response response, int i) throws Exception {
                    String result = response.body().string();
                    System.out.println(result);
                    if (!result.isEmpty()) {
                        Gson gson = new Gson();
                        User user = gson.fromJson(result, User.class);
                        MyApplication.user = user;//更新内存中的User
                        System.out.println("MyApplicatiomUser:" + user);
                        //如果用户已经存在与数据库中则更新否则添加
                        //h还有些用户的数据都需在这完成本地数据库的更新
                        //System.out.println("have:" + dbUtils.haveExistUser(username));
                        if (dbUtils.haveExistUser(username)) {
                            dbUtils.updateUser(user, username);
                        } else {
                            dbUtils.addUser(user);
                        }
                        new ActivityUtils().setLoginStatus(LoginActivity.this, true, username);
                        //初始化更新设置的原始状态
                        UpdateData.initAll();
                        //初始化数据
                        UpdateData.newUpdateDate().reGetData();
                        // ActivityUtils.startActivity(LoginActivity.this, MainActivity.class);
                        finish();
                    } else {
                        ActivityUtils.toast(LoginActivity.this, "登录失败，请检查用户名或密码！");
                    }

                    // if (result.equals("" + NetUri.Status.SUCCESS)) {
                    //  handler.sendEmptyMessage(0x1);
                    // } else {
                    //  ActivityUtils.toast(LoginActivity.this, "登录失败，请检查用户名或密码！");
                    //}
                    return null;
                }

                @Override
                public void onError(Call call, Exception e, int i) {
                    ActivityUtils.toast(LoginActivity.this, "登录失败，请检查用户名或密码！");
                }

                @Override
                public void onResponse(Object o, int i) {

                }
            });
        } else {
            ActivityUtils.toast(LoginActivity.this, "登录信息有误，请检查用户名或密码!");
        }
    }

    public void register(View view) {
        ActivityUtils.startActivity(LoginActivity.this, RegisterActivity.class);
    }

    /**
     * 校验用户名密码
     *
     * @param username
     * @param psw
     * @return
     */
    private boolean checkContent(String username, String psw) {
        if (username.isEmpty() || psw.isEmpty()) {
            return false;
        }
        return true;
    }


    /**
     * 随便逛逛
     */
    public void loginStroll(View view) {
        //ActivityUtils.startActivity(LoginActivity.this, MainActivity.class);
        finish();
    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 0x1) {//登录成功
                //设置登录的状态为true
                new ActivityUtils().setLoginStatus(LoginActivity.this, true, username);
                // initUserInfo();
                //  finish();
            }
        }
    };

    /**
     * 从服务器中得到用户的基本信息
     */
//    private void initUserInfo() {
//        Map<String, String> params = new HashMap<>();
//        params.put("phone", username);
//        NetUtils.get(NetUri.URI_GET_USERINFO, params, new StringCallback() {
//            @Override
//            public void onError(Call call, Exception e, int i) {
//
//            }
//
//            @Override
//            public void onResponse(String s, int i) {
//                System.out.println("用户信息：" + s);
//                Type listType = new TypeToken<LinkedList<User>>() {
//                }.getType();
//                Gson gson = new Gson();
//                LinkedList<User> uList = gson.fromJson(s, listType);
//                for (User user : uList) {
//                    MyApplication.user = user;//更新内存中的User
//                    System.out.println("MyApplicatiomUser:" + user);
//                    //如果用户已经存在与数据库中则更新否则添加
//                    //h还有些用户的数据都需在这完成本地数据库的更新
//                    //System.out.println("have:" + dbUtils.haveExistUser(username));
//                    if (dbUtils.haveExistUser(username)) {
//                        dbUtils.updateUser(user, username);
//                    } else {
//                        dbUtils.addUser(user);
//                    }
//
//                }
//                //初始化更新设置的原始状态
//                UpdateData.initAll();
//                //初始化数据
//                UpdateData.newUpdateDate().reGetData();
//                // ActivityUtils.startActivity(LoginActivity.this, MainActivity.class);
//                finish();
//            }
//        });
//    }

    /**
     * 忘记密码
     */
    public void forgetPSW(View view) {
        new UpdatePSWDialog(LoginActivity.this).show();
    }
}
