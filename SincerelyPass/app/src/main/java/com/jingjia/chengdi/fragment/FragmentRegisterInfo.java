package com.jingjia.chengdi.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.jingjia.chengdi.MainActivity;
import com.jingjia.chengdi.R;
import com.jingjia.chengdi.utils.ActivityUtils;

/**
 * Created by Administrator on 2016/9/25.
 */
public class FragmentRegisterInfo extends Fragment {
    private static FragmentRegisterInfo fragment;
    private EditText et_name;

    public static FragmentRegisterInfo newInstance() {//单例
        if (fragment == null) {
            fragment = new FragmentRegisterInfo();
        }
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_fill_info, container, false);
        nextToComplete(view);
        et_name = (EditText) view.findViewById(R.id.register_name);
        return view;
    }

    private void nextToComplete(View view) {
        View register = view.findViewById(R.id.register_complete);
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (chackContent()) {
                    ActivityUtils.startActivity(getActivity(), MainActivity.class);
                    getActivity().finish();
                }
            }
        });
    }

    private boolean chackContent() {
        String name = et_name.getText().toString().trim();
        if (name.length() > 16) {
            ActivityUtils.toast(getActivity(), "名字太长了");
            return false;
        } else if (name.length() == 0 || name.isEmpty()) {
            ActivityUtils.toast(getActivity(), "名字不能为空");
            return false;
        }
        return true;
    }
}
