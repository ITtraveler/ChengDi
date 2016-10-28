package com.jingjia.chengdi;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.jingjia.chengdi.data.Category;
import com.jingjia.chengdi.data.NetUri;
import com.jingjia.chengdi.data.encapsulation.DemandInfo;
import com.jingjia.chengdi.dialog.QueryDialog;
import com.jingjia.chengdi.utils.ActionUtils;
import com.jingjia.chengdi.utils.ActivityUtils;
import com.jingjia.chengdi.utils.BDLocationUtils;
import com.jingjia.chengdi.utils.MyApplication;
import com.jingjia.chengdi.utils.NetUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;

/**
 * Created by Administrator on 2016/9/21.
 */
public class GoAcceptsDetailActivity extends AppCompatActivity implements View.OnClickListener {
    private Button bn_agree, bn_disagree, bn_complete, bn_cancel, bn_phone, bn_complaint, bn_continue;
    private View destination;
    private TextView tv_status, tv_username, tv_destination, tv_publich_time, tv_info, tv_remark, tv_task;
    private DemandInfo demandInfo;
    private View bottom0, bottom1;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_acceptancelist_detail);
        demandInfo = (DemandInfo) getIntent().getSerializableExtra("demandInfo");
        // System.out.println("name"+demandInfo.ge);
        initView();
    }

    private void initView() {
        bottom0 = findViewById(R.id.detail_bottom_bar);
        bottom1 = findViewById(R.id.detail_bottom_confirm_bar);
        bn_agree = (Button) findViewById(R.id.detail_bn_agree);
        bn_disagree = (Button) findViewById(R.id.detail_bn_disagree);
        bn_continue = (Button) findViewById(R.id.detail_bn_continue);
        bn_complete = (Button) findViewById(R.id.detail_bn_acceptance);//申请完成
        bn_cancel = (Button) findViewById(R.id.detail_bn_cancel); //申请撤销

        bn_phone = (Button) findViewById(R.id.detail_bn_phone);//电话
        tv_username = (TextView) findViewById(R.id.detail_username);
        bn_complaint = (Button) findViewById(R.id.detail_bn_complaint);//投诉
        destination = findViewById(R.id.destination);//目的
        tv_status = (TextView) findViewById(R.id.detail_status);
        tv_destination = (TextView) findViewById(R.id.detail_destination);
        tv_publich_time = (TextView) findViewById(R.id.detail_publish_time);
        tv_info = (TextView) findViewById(R.id.detail_info);
        tv_remark = (TextView) findViewById(R.id.detail_remark);
        tv_task = (TextView) findViewById(R.id.detail_task);
        tv_publich_time.setText(demandInfo.getPublishTime());
        tv_username.setText(demandInfo.getUser().getUsername());
        //tv_status.setText(demandInfo);

        tv_info.setText("类型：" + Category.values()[Integer.valueOf(demandInfo.getCategory())] + "\n\n佣金：" + demandInfo.getMoney() + "元\n\n限时：" + demandInfo.getLimittime());
        tv_task.setText(demandInfo.getContent());
        tv_remark.setText(demandInfo.getRemark());
        tv_destination.setText(demandInfo.getDestination() + "\n" + demandInfo.getDestinationRemark());

        bn_agree.setOnClickListener(this);
        bn_disagree.setOnClickListener(this);
        bn_complete.setOnClickListener(this);
        bn_cancel.setOnClickListener(this);
        bn_phone.setOnClickListener(this);
        bn_complaint.setOnClickListener(this);
        destination.setOnClickListener(this);
        bn_continue.setOnClickListener(this);
        initBottomStatus();
    }

    /**
     * 初始化View的状态，根据订单的状态改变bottom bar
     */
    private void initBottomStatus() {
        String status = demandInfo.getStatus();
        tv_status.setText(status);
        switch (status) {
            case "进行中":
                bn_complete.setText("申请完成");
                bn_cancel.setVisibility(View.VISIBLE);
                break;

            case "已完成":
                bottom0.setVisibility(View.GONE);//底部bottom剔除
                break;

            case "已撤销":
                bottom0.setVisibility(View.GONE);//底部bottom剔除
                break;

            case "撤销待处理":
                bottom0.setVisibility(View.GONE);
                bottom1.setVisibility(View.VISIBLE);
                break;

            case "放弃待处理":
                bottom0.setVisibility(View.GONE);
                bn_continue.setVisibility(View.VISIBLE);
                break;
            case "完成待处理":
                //bn_complete.setText("提醒完成");
                bn_complete.setText("处理中......");
                bn_complete.setClickable(false);
                break;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.detail_bn_acceptance://底部完成按钮
                actionComplete("" + demandInfo.getId(), 2, "确认申请完成");
                break;
            case R.id.detail_bn_cancel:
                actionComplete("" + demandInfo.getId(), 6, "确认申请放弃");
                break;
            case R.id.detail_bn_phone:
                ActivityUtils.startCallPhone(this, demandInfo.getPhone());
                break;
            case R.id.detail_bn_complaint:
                ActionUtils.newInstance().actionComplaint(this,MyApplication.user.getPhone(),""+demandInfo.getId());
                break;
            case R.id.detail_destination:
                break;
            case R.id.detail_bn_continue:
                actionComplete("" + demandInfo.getId(), 1, "确认申请继续");
                break;
            case R.id.detail_bn_agree:
                actionComplete("" + demandInfo.getId(), 5, "确认同意撤销");
                break;
            case R.id.detail_bn_disagree:
                actionComplete("" + demandInfo.getId(), 1, "确认不同意撤销");
                break;
            case R.id.destination:
                BDLocationUtils.startBDClient(this, demandInfo.getDestination() + demandInfo.getDestinationRemark());
                break;

        }
    }

    /**
     * @param orderId        订单号
     * @param status         传入要改变的状态
     * @param dialogContent0
     */
    private void actionComplete(final String orderId, final int status, String dialogContent0) {
        final QueryDialog queryDialog = new QueryDialog(this);
        queryDialog.setContent(dialogContent0);
        queryDialog.show();
        queryDialog.setBnConfirmListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                queryDialog.setContent("请稍后...");
                queryDialog.confirm();
                Map<String, String> param = new HashMap<>();
                param.put("id", orderId);
                param.put("status", "" + status);
//                if (style == 0) {
//                    param.put("status", "" + 2);//2为完成处理状态
//                } else if (style == 1) {
//                    param.put("status", "" + 6);//6为放弃待处理状态
//                }
                NetUtils.get(NetUri.URI_COMPLETE_ORDER, param, new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int i) {
                        queryDialog.setContent("申请失败");
                        queryDialog.delayCancel(1000);
                    }

                    @Override
                    public void onResponse(String s, int i) {
                        if (s.equals("" + NetUri.Status.SUCCESS)) {
                            queryDialog.setContent("申请已提交");
                            queryDialog.delayCancel(1000);
                            bn_complete.setText("处理中......");
                            bn_complete.setClickable(false);
                            bn_cancel.setVisibility(View.GONE);
                            handler.sendEmptyMessageDelayed(0x1, 1250);
                            MyApplication.newInstance().notificationFlashAcceptance();
                        } else {
                            queryDialog.setContent("申请失败");
                            queryDialog.delayCancel(1000);
                        }
                    }
                });

            }
        });

    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 0x1) {
                finish();
            }
        }
    };
}
