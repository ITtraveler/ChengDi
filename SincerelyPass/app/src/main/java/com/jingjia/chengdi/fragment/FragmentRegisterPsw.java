package com.jingjia.chengdi.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.jingjia.chengdi.R;
import com.jingjia.chengdi.utils.ActivityUtils;

/**
 * Created by Administrator on 2016/9/25.
 */
public class FragmentRegisterPsw extends Fragment {
    private static FragmentRegisterPsw fragment;
    private FragmentManager fm;
    private EditText et_psw_one, et_psw_two;

    public static FragmentRegisterPsw newInstance() {//单例
        if (fragment == null) {
            fragment = new FragmentRegisterPsw();
        }
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fm = getActivity().getSupportFragmentManager();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_passwrod, container, false);
        nextToInfo(view);
        et_psw_one = (EditText) view.findViewById(R.id.register_psw);
        et_psw_two = (EditText) view.findViewById(R.id.register_psw_confirm);

        return view;
    }

    private void nextToInfo(View view) {
        View register = view.findViewById(R.id.register_next_info);
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (chackPSW())
                    fm.beginTransaction().replace(R.id.register_frameLayout, FragmentRegisterInfo.newInstance()).commit();
            }
        });
    }

    private boolean chackPSW() {
        String pswOne = et_psw_one.getText().toString().trim();
        String pswTwo = et_psw_one.getText().toString().trim();
        if (pswOne.length() < 6 && pswOne.length() > 24) {
            ActivityUtils.toast(getActivity(), "密码长度保持在6到24个字符");
            return false;
        } else if (!pswOne.equals(pswTwo)) {
            ActivityUtils.toast(getActivity(), "密码不一致");
            return false;
        }
        return true;
    }
}
