package com.jingjia.chengdi.utils;

import android.content.Context;

import cn.smssdk.EventHandler;
import cn.smssdk.OnSendMessageHandler;
import cn.smssdk.SMSSDK;

/**
 * Created by Administrator on 2016/9/26.
 */
public class SMSUtils implements OnSendMessageHandler, SMSSDK.VerifyCodeReadListener {
    private final String APP_KEY = "17747bd17895a";
    private final String APP_SECRET = "3af49dfcb98f7385e141afdc8ff9e226";
    public static final int CHINA_AREA_CODE = 86;//中国区号
    public static final int VERIFICATION_OK = 1;//验证成功
    public static final int VERIFICATION_ERROR = 2;//验证失败
    private int verificationStatus;
    private EventHandler eh;

    public SMSUtils(Context context) {
        initSMS(context);
    }

    private void initSMS(Context context) {
        SMSSDK.initSDK(context, APP_KEY, APP_SECRET);
        eh = new EventHandler() {
            @Override
            public void afterEvent(int event, int result, Object data) {

                if (result == SMSSDK.RESULT_COMPLETE) {
                    //回调完成
                    if (event == SMSSDK.EVENT_SUBMIT_VERIFICATION_CODE) {
                        //提交验证码成功
                        System.out.println("提交验证码成功");
                        verificationStatus = VERIFICATION_OK;
                    } else if (event == SMSSDK.EVENT_GET_VERIFICATION_CODE) {
                        //获取验证码成功
                        System.out.println("获取验证码成功");

                    } else if (event == SMSSDK.EVENT_GET_SUPPORTED_COUNTRIES) {
                        //返回支持发送验证码的国家列表
                    }
                } else {
                    ((Throwable) data).printStackTrace();
//                    HashMap<String,Object> phoneMap = (HashMap<String, Object>) data;
//                    String state = (String) phoneMap.get("status");
//                    String re = (String) phoneMap.get("detail");
//                    System.out.println(state+"   "+re);
                    verificationStatus = VERIFICATION_ERROR;
                }
            }
        };
        SMSSDK.registerEventHandler(eh);
    }

    /**
     * 发送一条验证码到手机
     *
     * @param areaCode 区号
     * @param phoneNum 手机号码
     */
    public void sendCode2Phone(String areaCode, String phoneNum) {
        SMSSDK.getVerificationCode("86", phoneNum, this);
    }

    /**
     * 提交验证码进行验证
     *
     * @param areaCode
     * @param phoneNum
     * @param code
     */
    public void handCode(String areaCode, String phoneNum, String code) {
        SMSSDK.submitVerificationCode("86", phoneNum, code);
    }

    /**
     * 验证状态
     *
     * @return
     */
    public int getVerificationStatus() {
        return verificationStatus;
    }

    /**
     * 注销事件绑定
     */
    public void unRegisterEventHandler() {
        SMSSDK.unregisterEventHandler(eh);
    }

    @Override
    public boolean onSendMessage(String s, String s1) {
        return false;
    }

    @Override
    public void onReadVerifyCode(String s) {

    }
}
