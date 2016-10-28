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
import com.jingjia.chengdi.data.DemandStatus;
import com.jingjia.chengdi.data.NetUri;
import com.jingjia.chengdi.data.encapsulation.DemandInfo;
import com.jingjia.chengdi.dialog.QueryDialog;
import com.jingjia.chengdi.utils.ActivityUtils;
import com.jingjia.chengdi.utils.MyApplication;
import com.jingjia.chengdi.utils.NetUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;

/**
 * Created by Administrator on 2016/9/21.
 */
public class GoPublishDetailActivity extends AppCompatActivity implements View.OnClickListener {
    private static GoPublishDetailActivity gpdActivity;
    private Button bn_action, bn_agree, bn_disagree;
    private View bottom0, destination;
    private TextView tv_acceptor, tv_username, tv_status, tv_destination, tv_publich_time, tv_info, tv_remark, tv_task;

    private DemandInfo demandInfo;
    private View acceptedBar;

//
//    public static GoPublishDetailActivity newInstance(DemandInfo demandInfo) {
//        if (gpdActivity == null)
//            gpdActivity = new GoPublishDetailActivity();
//        GoPublishDetailActivity.demandInfo = demandInfo;
//        return gpdActivity;
//    }

//    public GoPublishDetailActivity(DemandInfo demandInfo) {
//        super();
//        this.demandInfo = demandInfo;
//    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        demandInfo = (DemandInfo) getIntent().getSerializableExtra("demandInfo");
        System.out.println("GPDAcurDemandInfo:" + demandInfo.toString());
        setContentView(R.layout.activity_publishlist_detail);
        initView();
    }

    private void initView() {
        bottom0 = findViewById(R.id.detail_bottom_confirm_bar);//是否同意放弃
        bn_agree = (Button) findViewById(R.id.detail_bn_agree);
        bn_disagree = (Button) findViewById(R.id.detail_bn_disagree);
        bn_action = (Button) findViewById(R.id.detail_bn_publish_cancel);//取消按钮
        destination = findViewById(R.id.destination);//目的地
        tv_username = (TextView) findViewById(R.id.detail_username);
        tv_status = (TextView) findViewById(R.id.detail_status);
        tv_destination = (TextView) findViewById(R.id.detail_destination);
        tv_publich_time = (TextView) findViewById(R.id.detail_publish_time);
        tv_info = (TextView) findViewById(R.id.detail_info);
        tv_remark = (TextView) findViewById(R.id.detail_remark);
        tv_task = (TextView) findViewById(R.id.detail_task);
        acceptedBar = findViewById(R.id.detail_accepted_bar);


        bn_action.setOnClickListener(this);//撤销订单按钮监听器
        bn_agree.setOnClickListener(this);//同意放弃（由接单人申请）
        bn_disagree.setOnClickListener(this);//不同意放弃

        tv_publich_time.setText(demandInfo.getPublishTime());
        tv_username.setText(MyApplication.user.getUsername());


        tv_info.setText("类型：" + Category.values()[Integer.valueOf(demandInfo.getCategory())] + "\n\n佣金：" + demandInfo.getMoney() + "元\n\n限时：" + demandInfo.getLimittime());
        tv_task.setText(demandInfo.getContent());
        tv_remark.setText(demandInfo.getRemark());
        tv_destination.setText(demandInfo.getDestination() + "\n" + demandInfo.getDestinationRemark());
        String status = demandInfo.getStatus();
        tv_status.setText(status);
        //被接单状态
        if (!status.equals(DemandStatus.待接单.name()) && !status.equals(DemandStatus.已完成.name()) && !status.equals(DemandStatus.已撤销.name())) {
            acceptedBar.setVisibility(View.VISIBLE);
            tv_acceptor = (TextView) findViewById(R.id.detail_acceptor);
            tv_acceptor.setText(demandInfo.getUser().getUsername());
            findViewById(R.id.detail_bn_acceptor_phone).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //ActivityUtils.toast(GoPublishDetailActivity.this, "" + demandInfo.getAcceptancePhone());
                    ActivityUtils.startCallPhone(GoPublishDetailActivity.this, demandInfo.getAcceptancePhone());
                }
            });
        }
        initBottomStatus();
    }

    @Override
    public void onClick(View v) {
        String status = demandInfo.getStatus();
        switch (v.getId()) {
            case R.id.detail_bn_publish_cancel:
                if (status.equals("待接单"))
                    action("" + demandInfo.getId(), 5, "确认撤销发布");
                else if (status.equals("进行中")) {
                    action("" + demandInfo.getId(), 4, "确认撤销");
                } else if (status.equals("完成待处理")) {
                    action("" + demandInfo.getId(), 3, "确认完成");
                } else if (status.equals("撤销待处理")) {
                    action("" + demandInfo.getId(), 1, "确认取消撤销");
                }
                break;
            case R.id.detail_bn_agree:
                action("" + demandInfo.getId(), 5, "确认同意放弃");
                break;
            case R.id.detail_bn_disagree:
                action("" + demandInfo.getId(), 1, "确认不同意放弃");
                break;
        }

    }

    /**
     * 初始化View的状态，根据订单的状态改变bottom bar
     */
    private void initBottomStatus() {
        String status = demandInfo.getStatus();
        tv_status.setText(status);
        switch (status) {
            case "待接单":
                break;
            case "进行中":
                break;
            case "已完成":
                bn_action.setVisibility(View.GONE);
                break;
            case "已撤销":
                bn_action.setVisibility(View.GONE);
                break;
            case "撤销待处理":
                bn_action.setText("取消撤销");
                break;
            case "放弃待处理":
                bottom0.setVisibility(View.VISIBLE);
                bn_action.setVisibility(View.GONE);
                break;
            case "完成待处理":
                bn_action.setText("确认完成");
                break;
        }
    }

    /**
     * @param orderId        订单号
     * @param status         传入要改变的状态
     * @param dialogContent0
     */
    private void action(final String orderId, final int status, String dialogContent0) {
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
                            bn_action.setVisibility(View.GONE);
                            handler.sendEmptyMessageDelayed(0x1, 1250);
                            MyApplication.newInstance().notificationFlashPublish();
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
//    private void actionCancel() {
//        final QueryDialog queryDialog = new QueryDialog(this);
//        queryDialog.setContent("确认撤销");
//        queryDialog.show();
//        queryDialog.setBnConfirmListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                cancelAccepting(queryDialog);
//            }
//        });
//    }
//
//    /**
//     * 进行接单
//     */
//    private void cancelAccepting(final QueryDialog queryDialog) {
//        Map<String, String> param = new HashMap<>();
//        param.put("id", "" + demandInfo.getId());
//        if (demandInfo.getStatus().equals(DemandStatus.进行中.name())) {
//            param.put("status", "" + 4);//状态改为4为撤销待处理
//        } else if (demandInfo.getStatus().equals(DemandStatus.撤销待处理.name())) {
//            param.put("status", "" + 1);//状态改为进行中，取消撤销
//        } else
//            param.put("status", "" + 5);//状态5为撤销发布
//
//        NetUtils.get(NetUri.URI_COMPLETE_ORDER, param, new StringCallback() {
//            @Override
//            public void onError(Call call, Exception e, int i) {
//                queryDialog.setContent("处理失败");
//                queryDialog.delayCancel(1000);
//            }
//
//            @Override
//            public void onResponse(String s, int i) {
//                String result = s.trim();
//                if (result.equals("" + NetUri.Status.SUCCESS)) {
//                    queryDialog.setContent("处理成功");
//                    queryDialog.confirm();
//                    queryDialog.delayCancel(1000);
//                    MyApplication.newInstance().notificationFlashPublish();
//                    handler.sendEmptyMessageDelayed(0x1, 1500);
//                    //MyApplication.aroundDemandList.remove(demandInfo);
//                } else {
//                    queryDialog.setContent("处理失败");
//                    queryDialog.delayCancel(1000);
//                }
//            }
//        });
//    }
//
//    Handler handler = new Handler() {
//        @Override
//        public void handleMessage(Message msg) {
//            super.handleMessage(msg);
//            if (msg.what == 0x1) {
//                finish();
//            }
//        }
//    };
}
