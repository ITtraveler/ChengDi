package com.jingjia.chengdi;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

/**
 * Created by Administrator on 2016/9/21.
 */
public class GoAcceptsDetailActivity extends AppCompatActivity implements View.OnClickListener {
    private Button bn_cancel, bn_phone, bn_complaint;
    private View destination;
    private TextView tv_state, tv_destination, tv_publich_time, tv_info, tv_require, tv_task;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_acceptancelist_detail);
        bn_cancel = (Button) findViewById(R.id.detail_bn_acceptance_cancel);//取消按钮
        bn_phone = (Button) findViewById(R.id.detail_bn_phone);//电话
        bn_complaint = (Button) findViewById(R.id.detail_bn_complaint);//投诉
        destination = findViewById(R.id.destination);//目的
        tv_state = (TextView) findViewById(R.id.detail_state);
        tv_destination = (TextView) findViewById(R.id.detail_destination);
        tv_publich_time = (TextView) findViewById(R.id.detail_publish_time);
        tv_info = (TextView) findViewById(R.id.detail_info);
        tv_require = (TextView) findViewById(R.id.detail_require);
        tv_task = (TextView) findViewById(R.id.detail_task);


        bn_cancel.setText("撤销订单");

        bn_cancel.setOnClickListener(this);
        bn_phone.setOnClickListener(this);
        bn_complaint.setOnClickListener(this);
        destination.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.detail_bn_acceptance_cancel:
                break;
            case R.id.detail_bn_phone:
                break;
            case R.id.detail_bn_complaint:
                break;
            case R.id.detail_destination:
                break;
        }
    }
}
