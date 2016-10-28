package com.jingjia.chengdi.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;

import com.jingjia.chengdi.R;

/**
 * Created by Administrator on 2016/9/18.
 */
public class FragmentGo extends Fragment implements View.OnClickListener {
    private static FragmentGo fragment;
    private RadioButton rb_accp;
    private RadioButton rb_publish;
    private FragmentManager fm;
    private int curFragment = 0;
    private FragmentTransaction transaction;
    private FragmentGoAcceptance fragmentGoAcceptance;
    private FragmentGoPublish fragmentGoPublish;

    public static FragmentGo newInstance() {//单例
        if (fragment == null) {
            fragment = new FragmentGo();
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
        View view = inflater.inflate(R.layout.fragment_go, container, false);
        initGoBar(view);
        initFragment();
        return view;
    }

    private void initFragment() {
        fm = getActivity().getSupportFragmentManager();
        fragmentGoAcceptance = FragmentGoAcceptance.newInstance();
        fragmentGoPublish = FragmentGoPublish.newInstance();
        transaction = fm.beginTransaction();
        if (!fragmentGoAcceptance.isAdded()) {
            transaction.add(R.id.go_fragment, fragmentGoAcceptance);
        } else if (!fragmentGoPublish.isAdded()) {
            transaction.add(R.id.go_fragment, fragmentGoPublish);
        }
        fragmentGoPublish.onPause();//暂停它，防止乱窜
        transaction.show(fragmentGoAcceptance).hide(fragmentGoPublish).commit();

        //fm.beginTransaction().replace(R.id.go_fragment, FragmentGoAcceptance.newInstance()).commit();
    }

    private void initGoBar(View view) {
        rb_accp = (RadioButton) view.findViewById(R.id.go_rb_acceptance);//我的接单
        rb_publish = (RadioButton) view.findViewById(R.id.go_rb_publish);//我的发布
        rb_accp.setOnClickListener(this);
        rb_publish.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.go_rb_acceptance:
                rb_accp.setChecked(true);
                rb_publish.setChecked(false);
                if (curFragment != 0) {
                    fm.beginTransaction().replace(R.id.go_fragment, new FragmentGoAcceptance()).commit();
// fm.beginTransaction().show(fragmentGoAcceptance).hide(fragmentGoPublish).commit();
                    //fragmentGoPublish.onPause();
                    curFragment = 0;

                }
                break;
            case R.id.go_rb_publish:
                if (curFragment != 1) {
                    // fragmentGoPublish.onResume();

                    System.out.println("fragmentcount:" + fm.getBackStackEntryCount());
                    fm.beginTransaction().replace(R.id.go_fragment, new FragmentGoPublish()).commit();
                    //fm.beginTransaction().show(fragmentGoPublish).hide(fragmentGoAcceptance).commit();
                    // fragmentGoAcceptance.onPause();
                    curFragment = 1;
                }
                rb_accp.setChecked(false);
                rb_publish.setChecked(true);
                break;
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.i("fragment", "FragmentGoStop");
    }
}
