package com.jingjia.chengdi;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.jingjia.chengdi.data.NetUri;
import com.jingjia.chengdi.data.encapsulation.DemandInfo;
import com.jingjia.chengdi.dialog.QueryDialog;
import com.jingjia.chengdi.utils.ActivityUtils;
import com.jingjia.chengdi.utils.BDLocationUtils;
import com.jingjia.chengdi.utils.MyApplication;
import com.jingjia.chengdi.utils.NetUtils;
import com.zhy.http.okhttp.callback.Callback;

import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Response;

/**
 * Created by Administrator on 2016/9/18.
 */
public class PublishActivity extends AppCompatActivity implements TextWatcher {
    private EditText etLimitTime, etCommission, etTask, etRemark, etDestination, etDestRemark;
    private Spinner spCategory, spLimitTime;
    private TextView tvCTask, tvCRemarks;
    private View icLocation;
    private BDLocationUtils bdLocation;
    private QueryDialog publishDialog;
    private DemandInfo demandInfo;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_publish);
        bdLocation = BDLocationUtils.newInstance(PublishActivity.this);
        init();
    }

    private void init() {
        spCategory = (Spinner) findViewById(R.id.publish_spinner_category);
        spLimitTime = (Spinner) findViewById(R.id.publish_spinner_limitTime);

        etLimitTime = (EditText) findViewById(R.id.publish_limitTime);
        etCommission = (EditText) findViewById(R.id.publish_commission);
        etTask = (EditText) findViewById(R.id.publish_task);
        etRemark = (EditText) findViewById(R.id.publish_remarks);
        etDestination = (EditText) findViewById(R.id.publish_destination);
        etDestRemark = (EditText) findViewById(R.id.publish_destination_remarks);
        etTask.addTextChangedListener(this);
        etRemark.addTextChangedListener(this);
        tvCTask = (TextView) findViewById(R.id.publish_count_task);
        tvCRemarks = (TextView) findViewById(R.id.publish_count_remarks);

        icLocation = findViewById(R.id.publish_ic_location);
        //设置定位得到的位置
        etDestination.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String s = bdLocation.getAddress();
                etDestination.setText(s);
            }
        });
    }

    /**
     * 发布按钮
     *
     * @param view
     */
    public void publish(View view) {
        final String category = spCategory.getSelectedItem().toString();
        final String limitTime;
        String timeValue = etLimitTime.getText().toString().trim();
        if (timeValue.isEmpty() || timeValue.equals("0"))
            limitTime = "不限时";
        else
            limitTime = timeValue + " " + spLimitTime.getSelectedItem().toString();
        //货币的处理
        String commText = etCommission.getText().toString().trim();
        if (commText.isEmpty()) {
            commText = "" + 0;
        }
        final int commission = Integer.valueOf(commText);

        System.out.println("佣金：" + commission);

        final String task = etTask.getText().toString();
        final String remark = etRemark.getText().toString();
        final String destination = etDestination.getText().toString();
        final String destRemarks = etDestRemark.getText().toString();
        if (checkPublishContent(task, destination, destRemarks)) {
            //显示对话框
            publishDialog = new QueryDialog(this);
            publishDialog.show();
            publishDialog.setBnConfirmListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    publishDialog.confirm();
                    demandHand2Server(category, limitTime, commission, task, remark, destination, destRemarks);
                }
            });

        }


    }

    /**
     * 将数据提交到服务器
     *
     * @param category
     * @param limitTime
     * @param commission
     * @param task
     * @param remark
     * @param destination
     * @param destRemarks
     */
    private void demandHand2Server(String category, String limitTime, int commission, String task, String remark, String destination, String destRemarks) {
        demandInfo = new DemandInfo();
        demandInfo.setCategory(category);
        demandInfo.setLimittime(limitTime);
        demandInfo.setMoney(commission);
        demandInfo.setContent(task);
        demandInfo.setRemark(remark);
        demandInfo.setDestination(destination);
        demandInfo.setDestinationRemark(destRemarks);
        demandInfo.setUser(MyApplication.user);
        demandInfo.setPhone(MyApplication.user.getPhone());
        demandInfo.setPublishTime(ActivityUtils.getTime());
        demandInfo.setStatus("" + 0);

        Map<String, String> param = new HashMap<>();
        param.put("category", "" + spCategory.getSelectedItemPosition());//这里类别最后需要与服务器上的相对
        param.put("limittime", limitTime);
        param.put("money", "" + commission);
        param.put("content", task);
        param.put("remark", remark);
        param.put("destination", destination);
        param.put("destinationRemark", destRemarks);
        param.put("publishTime", ActivityUtils.getTime());
        param.put("phone", MyApplication.user.getPhone());
        param.put("username", MyApplication.user.getUsername());
        param.put("status", "" + 0);
        System.out.println(param.toString());
        NetUtils.post(NetUri.URI_PUBLISH_DEMAND, param, new Callback() {
            @Override
            public Object parseNetworkResponse(Response response, int i) throws Exception {
                String result = response.body().string();
                if (result.equals("" + NetUri.Status.SUCCESS)) {
                    handler.sendEmptyMessage(0x1);
                } else
                    handler.sendEmptyMessage(0x2);
                return null;
            }

            @Override
            public void onError(Call call, Exception e, int i) {

                System.out.println(call.request().body().toString() + "     " + e.toString());
                handler.sendEmptyMessage(0x2);
            }

            @Override
            public void onResponse(Object o, int i) {

            }
        });
    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 0x1) {//发布成功
                publishDialog.setContent("发布成功");
               MyApplication.newInstance().addMyDemand(demandInfo);//添加一条需求信息到内存
                handler.sendEmptyMessageDelayed(0x3, 1500);
            } else if (msg.what == 0x2) {//发布失败
                publishDialog.setContent("发布失败");
                handler.sendEmptyMessageDelayed(0x4, 1500);
            } else if (msg.what == 0x3) {
                publishDialog.cancel();
                finish();
            } else if (msg.what == 0x4) {
                publishDialog.cancel();
            }
        }
    };

    /**
     * 对发布内容的检查
     *
     * @param task
     * @param destination
     * @param destRemarks
     * @return
     */
    private boolean checkPublishContent(String task, String destination, String destRemarks) {
        if (task.length() < 5) {
            ActivityUtils.toast(this, "需求详情内容不详细");
            return false;
        }
        if (destination.isEmpty() || destRemarks.isEmpty()) {
            ActivityUtils.toast(this, "请填写详细地址");
            return false;
        }
        return true;
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    /**
     * 监听EditText内容的改变
     *
     * @param s
     * @param start
     * @param before
     * @param count
     */
    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        int count0 = etTask.getText().length();
        int count1 = etRemark.getText().length();
        tvCTask.setText("需求详情 " + count0 + "/120");
        tvCRemarks.setText("备注  " + count1 + "/32");
    }

    @Override
    public void afterTextChanged(Editable s) {

    }

    @Override
    public void finish() {
        super.finish();
        bdLocation.stopLocation();
    }
}
