package com.jingjia.chengdi.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.jingjia.chengdi.R;

/**
 * Created by Administrator on 2016/9/18.
 */
public class FragmentGoAcceptance extends Fragment implements RadioGroup.OnCheckedChangeListener, ViewPager.OnPageChangeListener {
    private static FragmentGoAcceptance fragment;
    private ViewPager viewPager;
    private FragmentManager fm;
    private MyPagerAdapter pagerAdapter;
    private RadioButton rb_goint, rb_complete;

    public static FragmentGoAcceptance newInstance() {//单例
        if (fragment == null) {
            fragment = new FragmentGoAcceptance();
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
        View view = inflater.inflate(R.layout.go_content, container,false);
        initViewPager(view);
        initStatus(view);
        return view;
    }


    private void initViewPager(View view) {
        viewPager = (ViewPager) view.findViewById(R.id.go_viewpager);
        pagerAdapter = new MyPagerAdapter(fm);
        viewPager.setAdapter(pagerAdapter);
        viewPager.addOnPageChangeListener(this);//viewPage页面改变监听器，提到了之前的set方式
    }

    private void initStatus(View view) {
        rb_goint = (RadioButton) view.findViewById(R.id.go_state_going);
        rb_complete = (RadioButton) view.findViewById(R.id.go_state_complete);
        RadioGroup rg_status = (RadioGroup) view.findViewById(R.id.go_state);
        rg_status.setOnCheckedChangeListener(this);//RadioGroup改变监听器
    }

    /**
     * RadioGroup的状态监听器
     *
     * @param group
     * @param checkedId
     */
    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        if (checkedId == R.id.go_state_going) {
            viewPager.setCurrentItem(0);
        } else {
            viewPager.setCurrentItem(1);
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        if (position == 0)
            rb_goint.setChecked(true);
        else if (position == 1)
            rb_complete.setChecked(true);
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }


    class MyPagerAdapter extends FragmentStatePagerAdapter {

        public MyPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return new FragmentGoAcceptanceContent();
        }

        @Override
        public int getCount() {
            return 2;
        }
    }
}
