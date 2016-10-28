package com.jingjia.chengdi;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.jingjia.chengdi.R;
import com.jingjia.chengdi.utils.ActivityUtils;

/**
 * Created by Administrator on 2016/9/25.
 */
public class SetActivity extends AppCompatActivity implements View.OnClickListener {
    private View vAboutUs, vUpApp, vUpPhone, vUpPSW, vLoginO;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set);
        vAboutUs = findViewById(R.id.set_about_us);
        vUpApp = findViewById(R.id.set_update_app);
        vUpPhone = findViewById(R.id.set_update_phone);
        vUpPSW = findViewById(R.id.set_update_passwd);
        vLoginO = findViewById(R.id.set_login_out);

        vAboutUs.setOnClickListener(this);
        vUpApp.setOnClickListener(this);
        vUpPhone.setOnClickListener(this);
        vUpPSW.setOnClickListener(this);
        vLoginO.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.set_about_us:
                break;
            case R.id.set_update_app:
                break;
            case R.id.set_update_phone:
                break;
            case R.id.set_update_passwd:
                break;
            case R.id.set_login_out:
                ActivityUtils.startActivity(SetActivity.this, LoginActivity.class);
                this.finish();
                break;
        }
    }
}
