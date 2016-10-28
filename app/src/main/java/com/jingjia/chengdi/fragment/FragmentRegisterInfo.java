package com.jingjia.chengdi.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.jingjia.chengdi.MainActivity;
import com.jingjia.chengdi.R;
import com.jingjia.chengdi.data.NetUri;
import com.jingjia.chengdi.data.encapsulation.RegistInfo;
import com.jingjia.chengdi.dialog.WaitingDialog;
import com.jingjia.chengdi.utils.ActivityUtils;
import com.jingjia.chengdi.utils.MyApplication;
import com.jingjia.chengdi.utils.NetUtils;
import com.jingjia.chengdi.utils.SQLiteDbUtils;
import com.jingjia.chengdi.utils.UpdateData;
import com.zhy.http.okhttp.callback.Callback;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Response;

/**
 * Created by Administrator on 2016/9/25.
 */
public class FragmentRegisterInfo extends Fragment {
    private static FragmentRegisterInfo fragment;
    private EditText et_name;
    private WaitingDialog wDialog;

    public static FragmentRegisterInfo newInstance() {//单例
        if (fragment == null) {
            fragment = new FragmentRegisterInfo();
        }
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        wDialog = new WaitingDialog(getActivity());
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_fill_info, container, false);
        nextToComplete(view);
        et_name = (EditText) view.findViewById(R.id.register_name);
        return view;
    }

    /**
     * 完成注册，将数据提交到服务器
     *
     * @param view
     */
    private void nextToComplete(View view) {
        View register = view.findViewById(R.id.register_complete);
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (chackContent()) {
                    wDialog.show();
                    handToServer();//将数据提交到服务器
                }
            }
        });
    }

    /**
     * 检查用户名
     *
     * @return
     */
    private boolean chackContent() {
        String name = et_name.getText().toString().trim();
        if (name.length() > 16) {
            ActivityUtils.toast(getActivity(), "名字太长了");
            return false;
        } else if (name.length() == 0 || name.isEmpty()) {
            ActivityUtils.toast(getActivity(), "名字不能为空");
            return false;
        }
        //设置下用户名
        MyApplication.registInfo.setUsername(name);
        return true;
    }

    /**
     * 将注册的数据提交到服务器
     *
     * @return
     */
    private boolean handToServer() {
        Map<String, String> params = new HashMap<>();
        RegistInfo registInfo = MyApplication.registInfo;
        params.put("phone", registInfo.getPhone());
        params.put("password", registInfo.getPassword());
        params.put("username", registInfo.getUsername());
        System.out.println(registInfo.toString());
        NetUtils.post(NetUri.URI_REGISTER, params, new Callback() {
            @Override
            public Object parseNetworkResponse(Response response, int i) throws Exception {
                String result = response.body().string();
                System.out.println("register:------" + result);
                //成功
                if (result.equals("" + NetUri.Status.SUCCESS)) {
                    handle.sendEmptyMessage(0x1);
                } else if (result.equals("" + NetUri.Status.EXIST)) {//明明result = 300 ，却不会执行此，很奇怪
                    ActivityUtils.toast(getActivity(), "账号已存在！");
                    wDialog.setContent("账号已存在！");
                    handle.sendEmptyMessageDelayed(0x2, 1500);
                }

                return null;
            }

            @Override
            public void onError(Call call, Exception e, int i) {
                System.out.println(e);
                wDialog.setContent("注册失败！");
                ActivityUtils.toast(getActivity(), "注册失败！可能账号已存在。");
                handle.sendEmptyMessageDelayed(0x2, 1500);
            }

            @Override
            public void onResponse(Object o, int i) {
            }
        });
        return true;
    }

    Handler handle = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 0x1) {
                wDialog.cancel();
                MyApplication.user.setPhone(MyApplication.registInfo.getPhone());
                MyApplication.user.setUsername(MyApplication.registInfo.getUsername());
                // MyApplication.user.setPassword(MyApplication.registInfo.getPassword());
                new SQLiteDbUtils(getContext()).addUser(MyApplication.user);
                UpdateData.initAll();
                UpdateData.newUpdateDate().reGetData();

                //设置为登录状态
                new ActivityUtils().setLoginStatus(getActivity(), true, MyApplication.registInfo.getPhone());
                ActivityUtils.startActivity(getActivity(), MainActivity.class);
                getActivity().finish();
            } else if (msg.what == 0x2) {
                wDialog.cancel();
                getActivity().finish();
            }
        }
    };

}
