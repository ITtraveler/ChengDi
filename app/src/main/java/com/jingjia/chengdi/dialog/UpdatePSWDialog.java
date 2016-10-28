package com.jingjia.chengdi.dialog;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.jingjia.chengdi.R;
import com.jingjia.chengdi.data.NetUri;
import com.jingjia.chengdi.utils.ActivityUtils;
import com.jingjia.chengdi.utils.NetUtils;
import com.jingjia.chengdi.utils.SMSUtils;
import com.zhy.http.okhttp.callback.Callback;

import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Response;

/**
 * Created by Administrator on 2016/10/3.
 * 用户修改密码专用对话框
 */
public class UpdatePSWDialog extends AlertDialog implements View.OnClickListener {
    private Button bnCode, bnNext;
    private EditText etPhone, etCode;
    private boolean canGetCode = true;
    private View codeLayout;
    private EditText etPSW;
    private boolean havePassCode = false;
    private String curPhone;
    private SMSUtils smsUtils;

    public UpdatePSWDialog(Context context) {
        super(context);
        smsUtils = new SMSUtils(context);
        init();
    }

    private void init() {
        setCanceledOnTouchOutside(false);
        View v = getLayoutInflater().inflate(R.layout.dialog_update_password, null);
        bnCode = (Button) v.findViewById(R.id.dialog_get_code);
        bnNext = (Button) v.findViewById(R.id.dialog_next);
        etPhone = (EditText) v.findViewById(R.id.dialog_phone);
        etCode = (EditText) v.findViewById(R.id.dialog_code);
        codeLayout = v.findViewById(R.id.dialog_update_psw_code);
        etPSW = (EditText) v.findViewById(R.id.dialog_update_psw);

        bnCode.setOnClickListener(this);
        bnNext.setOnClickListener(this);

        setView(v);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.dialog_get_code:
                getCode();
                break;
            case R.id.dialog_next:
                dialogNext();
                break;
        }
    }


    private void getCode() {
        curPhone = etPhone.getText().toString().trim();
        if (curPhone.length() != 11) {
            ActivityUtils.toast(getContext(), "手机号输入输入有误！");
        } else if (canGetCode) {
            //得到一条验证码
            smsUtils.sendCode2Phone("" + SMSUtils.CHINA_AREA_CODE, curPhone);
            handler.sendEmptyMessage(0x1);
            canGetCode = false;
        }
    }

    private int second = 60;

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 0x1) {//实现计时功能
                if (second > 0) {
                    bnCode.setText((--second) + " s");
                    handler.sendEmptyMessageDelayed(0x1, 1000);
                } else {
                    bnCode.setText("获取");
                    second = 60;
                    canGetCode = true;
                }
            } else if (msg.what == 0x2) {
                //验证码通过进行
                if (smsUtils.getVerificationStatus() == SMSUtils.VERIFICATION_OK) {
                    codeLayout.setVisibility(View.GONE);
                    etPSW.setVisibility(View.VISIBLE);
                    havePassCode = true;//改变验证通过
                } else {
                    ActivityUtils.toast(getContext(), "验证失败，请重试！");
                }
            } else if (msg.what == 0x3) {//但密码修改成功时
                ActivityUtils.toast(getContext(), "密码修改成功");
                cancel();

            } else if (msg.what == 0x4) {//但密码修改失败
                ActivityUtils.toast(getContext(), "密码修改失败！注意网络连接。");

            }
        }
    };

    private void dialogNext() {
        if (!havePassCode) {//没有通过代码验证
            String code = etCode.getText().toString().trim();
            smsUtils.handCode("" + SMSUtils.CHINA_AREA_CODE, curPhone, code);//提交验证码验证
            handler.sendEmptyMessageDelayed(0x2, 1500);
        } else {//通过点验证
            String psw = etPSW.getText().toString().trim();
            if (chackPSW(psw)) {//用户输入的密码ok
                updatePsw(psw);
            }
        }
    }

    /**
     * 更新密码
     *
     * @param psw
     */
    private void updatePsw(String psw) {
        Map<String, String> params = new HashMap<>();
        params.put("phone", curPhone);
        params.put("password", psw);
        NetUtils.post(NetUri.URI_UPDATEPSW, params, new Callback() {
            @Override
            public Object parseNetworkResponse(Response response, int i) throws Exception {
                String result = response.body().string().trim();
                System.out.println(result);
                if (result.equals("" + NetUri.Status.SUCCESS)) {
                    handler.sendEmptyMessage(0x3);
                } else {
                    handler.sendEmptyMessage(0x4);
                    System.out.println("密码修改失败!");
                }
                return null;
            }

            @Override
            public void onError(Call call, Exception e, int i) {
                System.out.println("eee:" + e);
                // ActivityUtils.toast(getContext(), "密码修改失败！");
            }

            @Override
            public void onResponse(Object o, int i) {

            }
        });
    }

    /**
     * 检查密码
     *
     * @param psw
     * @return
     */
    private boolean chackPSW(String psw) {

        if (psw.length() < 6 || psw.length() > 24) {
            ActivityUtils.toast(getContext(), "密码长度保持在6到24个字符");
            return false;
        }
        return true;
    }
}
