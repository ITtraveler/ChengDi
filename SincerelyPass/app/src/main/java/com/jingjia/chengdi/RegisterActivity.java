package com.jingjia.chengdi;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;

import com.jingjia.chengdi.fragment.FragmentRegisterPhone;

/**
 * Created by Administrator on 2016/9/26.
 */
public class RegisterActivity extends AppCompatActivity{
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_framelayout);
        FragmentManager fm = getSupportFragmentManager();
        fm.beginTransaction().replace(R.id.register_frameLayout, FragmentRegisterPhone.newInstance()).commit();
    }
}
