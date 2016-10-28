package com.jingjia.chengdi.fragment;

import android.animation.AnimatorInflater;
import android.animation.AnimatorSet;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.jingjia.chengdi.R;

import org.w3c.dom.Text;

import java.security.PrivateKey;

/**
 * Created by Administrator on 2016/9/18.
 */
public class FragmentGo extends Fragment implements View.OnClickListener, RadioGroup.OnCheckedChangeListener, ViewPager.OnPageChangeListener {
    public static final int MODE_ING = 0;
    public static final int MODE_COMPLETE = 1;
    private static FragmentGo fragment;
    private RadioButton rb_going, rb_complete, rb_acceptance, rb_publish;
    private ViewPager viewPager;
    private MyPagerAdapter pagerAdapter;
    private FragmentManager fm;
    private Fragment[] fragments = {new FragmentGoAcceptanceContent(), new FragmentGoAcceptanceContent(),
            new FragmentGoPublishContent(), new FragmentGoPublishContent()};
    private int ACCETANCE = 0;
    private int PUBLISH = 1;
    private int curCategory = 0;
    private AnimatorSet animCategoryBarShow, animCategoryBarhide;
    private RadioGroup rbGroupCategory;
    private TextView barTitle;

    public static FragmentGo newInstance() {//单例
        if (fragment == null) {
            fragment = new FragmentGo();
        }
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fm = getActivity().getSupportFragmentManager();
        setRetainInstance(true);
        for (int i = 0; i < fragments.length; i++) {
            Bundle bundle = new Bundle();
            bundle.putInt("MODE", i % 2);//设置模式 0未完成 1 已完成
            fragments[i].setArguments(bundle);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_go, container, false);
        barTitle = (TextView) getActivity().findViewById(R.id.main_bar_title);
        initStatus(view);
        initCategory(view);
        initViewPager(view);
      //  initAnimator();
        return view;
    }

    private void initAnimator() {
        animCategoryBarShow = (AnimatorSet) AnimatorInflater.loadAnimator(getActivity(), R.animator.ic_right_out);
        animCategoryBarhide = (AnimatorSet) AnimatorInflater.loadAnimator(getActivity(), R.animator.ic_right_in);
        animCategoryBarShow.setTarget(rbGroupCategory);
        animCategoryBarhide.setTarget(rbGroupCategory);
    }


    private void initViewPager(View view) {
        viewPager = (ViewPager) view.findViewById(R.id.go_viewpager);
        pagerAdapter = new MyPagerAdapter(fm);
        viewPager.setAdapter(pagerAdapter);
        viewPager.addOnPageChangeListener(this);//viewPage页面改变监听器，提到了之前的set方式
       // viewPager.setOnTouchListener(this);
    }

    /**
     * 初始化我的发布与我的接单
     *
     * @param view
     */
    private void initCategory(View view) {
        rb_acceptance = (RadioButton) view.findViewById(R.id.go_rb_acceptance);
        rb_publish = (RadioButton) view.findViewById(R.id.go_rb_publish);
        rb_acceptance.setOnClickListener(this);
        rb_publish.setOnClickListener(this);

        rbGroupCategory = (RadioGroup) view.findViewById(R.id.go_rb);
        //rbGroupCategory.setOnTouchListener(this);
        //rg_status.setOnCheckedChangeListener(this);//RadioGroup改变监听器
    }

    /**
     * 初始化ing与complete
     *
     * @param view
     */
    private void initStatus(View view) {
        rb_going = (RadioButton) view.findViewById(R.id.go_state_going);
        rb_complete = (RadioButton) view.findViewById(R.id.go_state_complete);
        rb_going.setOnClickListener(this);
        rb_complete.setOnClickListener(this);
        //RadioGroup rg_status = (RadioGroup) view.findViewById(R.id.go_state);
        // rg_status.setOnCheckedChangeListener(this);//RadioGroup改变监听器
    }

    /**
     * RadioGroup的状态监听器
     * 在此次可以根据cheack来显示对应的
     *
     * @param group
     * @param checkedId
     */
    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {

    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        if (position == 0 || position == 2)
            rb_going.setChecked(true);
        else if (position == 1 || position == 3)
            rb_complete.setChecked(true);

        if (position == 0 || position == 1) {
            barTitle.setText("我的接单");
            rb_acceptance.setChecked(true);
            curCategory = ACCETANCE;
        } else {
            barTitle.setText("我的发布");
            rb_publish.setChecked(true);
            curCategory = PUBLISH;
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.go_state_going:
                if (curCategory == ACCETANCE)
                    viewPager.setCurrentItem(0);
                else
                    viewPager.setCurrentItem(2);
                break;
            case R.id.go_state_complete:
                if (curCategory == ACCETANCE)
                    viewPager.setCurrentItem(1);
                else
                    viewPager.setCurrentItem(3);
                break;
            case R.id.go_rb_acceptance:
                curCategory = ACCETANCE;
                viewPager.setCurrentItem(0);
                break;
            case R.id.go_rb_publish:
                curCategory = PUBLISH;
                viewPager.setCurrentItem(2);
                break;
        }

    }

//    boolean categoryBarShow = true;
//
//    //viewPager Touch事件，改变类别选择按钮的出现于隐藏
//    @Override
//    public boolean onTouch(View v, MotionEvent event) {
//        System.out.println("touch");
//        int action = event.getAction();
//        switch (action) {
//            case MotionEvent.ACTION_DOWN:
//                if (categoryBarShow) {
//                    animCategoryBarhide.start();
//                    categoryBarShow = false;
//                }
//                break;
//            case MotionEvent.ACTION_UP:
//                if (!categoryBarShow) {
//                    animCategoryBarShow.start();
//                    categoryBarShow = true;
//                }
//                break;
//        }
//
//        return false;
//    }


    class MyPagerAdapter extends FragmentStatePagerAdapter {

        public MyPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {

            return fragments[position];
        }

        @Override
        public int getCount() {
            return fragments.length;
        }

        /**
         * 防止每次滑动  都会 销毁 重新加载
         *
         * @param container
         * @param position
         * @param object
         */
        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {

        }

    }

    @Override
    public void onStop() {
        super.onStop();
        Log.i("fragment", "FragmentGoStop");
    }
}


//    private void initFragment(Bundle savedInstanceState) {
//        fm = getActivity().getSupportFragmentManager();
//
//        if (savedInstanceState != null) {
//            //从fragmentManager里面找到fragment
//            fragmentGoPublish = (FragmentGoPublish) fm.findFragmentByTag(FragmentGoPublish.class.getName());
//            fragmentGoAcceptance = (FragmentGoAcceptance) fm.findFragmentByTag(FragmentGoAcceptance.class.getName());
//
//        } else {
//            fragmentGoAcceptance = FragmentGoAcceptance.newInstance();
//            fragmentGoPublish = FragmentGoPublish.newInstance();
//        }
//        transaction = fm.beginTransaction();
//        if (!fragmentGoAcceptance.isAdded()) {
//            transaction.add(R.id.go_fragment, fragmentGoAcceptance, fragmentGoAcceptance.getClass().getName());
//        }
//        if (!fragmentGoPublish.isAdded()) {
//            transaction.add(R.id.go_fragment, fragmentGoPublish, fragmentGoPublish.getClass().getName());
//        }
//        // transaction.show(fragmentGoAcceptance).commit();
//        //transaction.show(fragmentGoAcceptance).hide(fragmentGoPublish).commit();
//        // fragmentGoPublish.onPause();//暂停它，防止乱窜
//        fm.beginTransaction().replace(R.id.go_fragment, fragmentGoAcceptance).commit();
//    }
//
//    private void initGoBar(View view) {
//        rb_accp = (RadioButton) view.findViewById(R.id.go_rb_acceptance);//我的接单
//        rb_publish = (RadioButton) view.findViewById(R.id.go_rb_publish);//我的发布
//        rb_accp.setOnClickListener(this);
//        rb_publish.setOnClickListener(this);
//    }
//
//    @Override
//    public void onClick(View v) {
//        switch (v.getId()) {
//            case R.id.go_rb_acceptance:
//                rb_accp.setChecked(true);
//                rb_publish.setChecked(false);
//                if (curFragment != 0) {
//                    //  fragmentGoAcceptance.onResume();
//                    fm.beginTransaction().replace(R.id.go_fragment, new FragmentGoAcceptance()).commit();
//                    // fm.beginTransaction().show(fragmentGoAcceptance).hide(fragmentGoPublish).commit();
//                    //   fragmentGoPublish.onPause();
//                   // fm.beginTransaction().show(fragmentGoPublish).commit();
//                    curFragment = 0;
//
//                }
//                break;
//            case R.id.go_rb_publish:
//                if (curFragment != 1) {
//                    //fragmentGoPublish.onResume();
//
//                    // System.out.println("fragmentcount:" + fm.getBackStackEntryCount());
//                    fm.beginTransaction().replace(R.id.go_fragment,new FragmentGoPublish()).commit();
//                    //fm.beginTransaction().show(fragmentGoPublish).hide(fragmentGoAcceptance).commit();
//                   // fm.beginTransaction().show(fragmentGoAcceptance).commit();
//                    //  fragmentGoAcceptance.onPause();
//                    curFragment = 1;
//                }
//                rb_accp.setChecked(false);
//                rb_publish.setChecked(true);
//                break;
//        }
//    }
//}
