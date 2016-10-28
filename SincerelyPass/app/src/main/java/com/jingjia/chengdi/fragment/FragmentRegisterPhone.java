package com.jingjia.chengdi.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.jingjia.chengdi.R;
import com.jingjia.chengdi.utils.ActivityUtils;
import com.jingjia.chengdi.utils.SMSUtils;

/**
 * Created by Administrator on 2016/9/25.
 */
public class FragmentRegisterPhone extends Fragment {
    private static FragmentRegisterPhone fragment;
    private FragmentManager fm;
    private EditText et_Phone, et_Code;
    private Button bn_getCode;
    private String curPhone;//当前电话
    private final int PHONE_COUNT = 11;
    private SMSUtils smsUtils;
    private boolean canGetCode = true;//能够得到验证码
    private int second = 60;

    public static FragmentRegisterPhone newInstance() {//单例
        if (fragment == null) {
            fragment = new FragmentRegisterPhone();
        }
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fm = getActivity().getSupportFragmentManager();
        smsUtils = new SMSUtils(getActivity());
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_register_phone, container, false);
        nextToPsw(view);
        getCode(view);
        return view;
    }

    /**
     * 获取验证码
     *
     * @param view
     */
    private void getCode(View view) {
        et_Phone = (EditText) view.findViewById(R.id.register_phone);
        et_Code = (EditText) view.findViewById(R.id.register_code);
        bn_getCode = (Button) view.findViewById(R.id.register_get_code);

        bn_getCode.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                curPhone = et_Phone.getText().toString().trim();
                if (curPhone.length() != PHONE_COUNT) {
                    ActivityUtils.toast(getActivity(), "手机号输入输入有误！");
                } else if (canGetCode) {
                    //得到一条验证码
                    smsUtils.sendCode2Phone("" + SMSUtils.CHINA_AREA_CODE, curPhone);
                    handler.sendEmptyMessage(0x1);
                    canGetCode = false;
                }
            }
        });
    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 0x1) {//实现计时功能
                if (second > 0) {
                    bn_getCode.setText((--second) + " s");
                    handler.sendEmptyMessageDelayed(0x1, 1000);
                } else {
                    bn_getCode.setText("验证");
                    second = 60;
                    canGetCode = true;
                }
            }
        }
    };

    /**
     * 切换到密码设置界面
     *
     * @param view
     */
    private void nextToPsw(View view) {
        View register = view.findViewById(R.id.register_next_psw);
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String code = et_Code.getText().toString().trim();
                smsUtils.handCode("" + SMSUtils.CHINA_AREA_CODE, curPhone, code);//提交验证码验证
                if (smsUtils.getVerificationStatus() == SMSUtils.VERIFICATION_OK) {
                    fm.beginTransaction().replace(R.id.register_frameLayout, FragmentRegisterPsw.newInstance()).commit();
                    smsUtils.unRegisterEventHandler();
                } else {
                    ActivityUtils.toast(getActivity(), "验证失败，请重试！");
                }
            }
        });
    }


}
