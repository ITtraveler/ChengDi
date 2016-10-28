package com.jingjia.chengdi;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

/**
 * Created by Administrator on 2016/9/21.
 * home主页list点开后展现的详细内容
 */
public class HomeListDetailActivity extends AppCompatActivity implements View.OnClickListener {
    private Button bn_acceptance, bn_phone, bn_complaint;
    private View destination;
    private TextView tv_state, tv_destination, tv_publich_time, tv_info, tv_require, tv_task;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homelist_detail);
        initView();
    }

    private void initView() {
        bn_acceptance = (Button) findViewById(R.id.detail_bn_acceptance_cancel);//取消按钮
        bn_phone = (Button) findViewById(R.id.detail_bn_phone);//电话
        bn_complaint = (Button) findViewById(R.id.detail_bn_complaint);//投诉
        destination = findViewById(R.id.detail_destination);//目的地
        tv_state = (TextView) findViewById(R.id.detail_state);
        tv_destination = (TextView) findViewById(R.id.detail_destination);
        tv_publich_time = (TextView) findViewById(R.id.detail_publish_time);
        tv_info = (TextView) findViewById(R.id.detail_info);//类型：买东西\n\n佣金：5元\n\n限时：45分钟
        tv_require = (TextView) findViewById(R.id.detail_require);
        tv_task = (TextView) findViewById(R.id.detail_task);

        bn_acceptance.setOnClickListener(this);
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
