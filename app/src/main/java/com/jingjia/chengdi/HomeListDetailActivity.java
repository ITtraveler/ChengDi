package com.jingjia.chengdi;

import android.os.Bundle;
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
 * home主页list点开后展现的详细内容
 */
public class HomeListDetailActivity extends AppCompatActivity implements View.OnClickListener {
    private Button bn_acceptance, bn_phone, bn_complaint;
    private View destination;
    private TextView tv_state, tv_username, tv_destination, tv_publich_time, tv_info, tv_remark, tv_task;
    private DemandInfo demandInfo;
    private QueryDialog queryDialog;
    private int position;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homelist_detail);
        Bundle bundle = getIntent().getExtras();
        demandInfo = (DemandInfo) bundle.getSerializable("demandInfo");
        position = bundle.getInt("position");
        //ActivityUtils.toast(HomeListDetailActivity.this, "" + position);
        initView();
    }

    private void initView() {
        bn_acceptance = (Button) findViewById(R.id.detail_bn_acceptance);//我要接单按钮
        bn_phone = (Button) findViewById(R.id.detail_bn_phone);//电话
        bn_complaint = (Button) findViewById(R.id.detail_bn_complaint);//投诉
        destination = findViewById(R.id.detail_destination);//目的地
        tv_username = (TextView) findViewById(R.id.detail_username);
        tv_state = (TextView) findViewById(R.id.detail_status);
        tv_destination = (TextView) findViewById(R.id.detail_destination);
        tv_publich_time = (TextView) findViewById(R.id.detail_publish_time);
        tv_info = (TextView) findViewById(R.id.detail_info);//类型：买东西\n\n佣金：5元\n\n限时：45分钟
        tv_remark = (TextView) findViewById(R.id.detail_remark);
        tv_task = (TextView) findViewById(R.id.detail_task);
        tv_publich_time.setText(demandInfo.getPublishTime());

        tv_username.setText(demandInfo.getUser().getUsername());
        //tv_status.setText(demandInfo);
        tv_info.setText("类型：" + Category.values()[Integer.valueOf(demandInfo.getCategory())] + "\n\n佣金：" + demandInfo.getMoney() + " 元\n\n限时：" + demandInfo.getLimittime());
        tv_task.setText(demandInfo.getContent());
        tv_remark.setText(demandInfo.getRemark());
        tv_destination.setText(demandInfo.getDestination() + "\n" + demandInfo.getDestinationRemark());
        bn_acceptance.setOnClickListener(this);//接单按钮
        bn_phone.setOnClickListener(this);
        bn_complaint.setOnClickListener(this);
        destination.setOnClickListener(this);

        queryDialog = new QueryDialog(this);
        queryDialog.setContent("确认接单");
        queryDialog.setBnConfirmListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                queryDialog.setContent("接单中...");
                queryDialog.confirm();//开始动画等等
                ActivityUtils.toast(HomeListDetailActivity.this, "" + demandInfo.getId());
                actionAccepting();//接单动作
            }


        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.detail_bn_acceptance:
                if (check())
                    queryDialog.show();
                break;
            case R.id.detail_bn_phone:
                ActivityUtils.toast(HomeListDetailActivity.this, "您还没有接单！");
                break;
            case R.id.detail_bn_complaint:
                if (check()) {
                    ActionUtils.newInstance().actionComplaint(this,MyApplication.user.getPhone(),""+demandInfo.getId());
                }
                break;
            case R.id.detail_destination:
                BDLocationUtils.startBDClient(this, demandInfo.getDestination() + demandInfo.getDestinationRemark());

                break;

        }
    }

    /**
     * 进行接单
     */
    private void actionAccepting() {
        Map<String, String> param = new HashMap<>();
        param.put("phone", MyApplication.user.getPhone());
        param.put("id", "" + demandInfo.getId());
        NetUtils.get(NetUri.URI_ACCEPTING, param, new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int i) {
                queryDialog.setContent("接单失败");
                queryDialog.delayCancel(1000);
            }

            @Override
            public void onResponse(String s, int i) {
                String result = s.trim();
                System.out.println("rr:" + result);
                if (result.equals("" + NetUri.Status.SUCCESS)) {
                    queryDialog.setContent("接单成功");
                    queryDialog.delayCancel(1000);
                    MyApplication.newInstance().removeAroundDemand(position);
                    //MyApplication.aroundDemandList.remove(demandInfo);
                } else {
                    queryDialog.setContent("接单失败");
                    queryDialog.delayCancel(1000);
                }
            }
        });
    }

    /**
     * 检查
     * 包括是否登录，是否为自己发布的
     */

    private boolean check() {
        //检查是否登录，否则调转到登录界面
        if (!ActivityUtils.newInstance().getLoginStatus(this)) {
            ActivityUtils.startActivity(HomeListDetailActivity.this, LoginActivity.class);
            return false;
        }
        //判断是否为自己发布的订单
        if (demandInfo.getPhone().equals(MyApplication.user.getPhone())) {
            ActivityUtils.toast(HomeListDetailActivity.this, "自己的订单，不能操作！");
            return false;
        }

        return true;
    }
}
