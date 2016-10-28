package com.jingjia.chengdi;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.jingjia.chengdi.utils.ActivityUtils;

/**
 * Created by Administrator on 2016/9/25.
 */
public class LoginActivity extends AppCompatActivity {

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

    }


    public void login(View view) {
        ActivityUtils.startActivity(LoginActivity.this, MainActivity.class);
        finish();
    }

    public void register(View view) {
        ActivityUtils.startActivity(LoginActivity.this, RegisterActivity.class);
    }
}
