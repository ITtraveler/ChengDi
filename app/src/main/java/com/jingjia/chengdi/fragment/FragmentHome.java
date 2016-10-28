package com.jingjia.chengdi.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.RadioButton;

import com.jingjia.chengdi.R;
import com.jingjia.chengdi.data.TestData;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/9/18.
 */
public class FragmentHome extends Fragment implements ViewPager.OnPageChangeListener, View.OnClickListener {

    private static FragmentHome fragment;
    private int screen_width = 0;//屏幕宽
    private LinearLayout linearLayout;
    private ViewPager homeViewPager;
    private MyPagerAdapter pagerAdapter;
    private FragmentManager fm;
    private String items[];
    private List<RadioButton> radioButtonList;
    private int oldPosition = 0;
    private HorizontalScrollView home_scroll;
    private List<FragmentHomePageOne> fragmentHomePageOnes = new ArrayList<>();

    public static FragmentHome newInstance() {//单例
        if (fragment == null) {
            fragment = new FragmentHome();
        }
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        fm = getActivity().getSupportFragmentManager();
        radioButtonList = new ArrayList<>();
        items = TestData.items;

        //WindowManager manager = (WindowManager)getActivity().getSystemService(getActivity().WINDOW_SERVICE);
        DisplayMetrics dm = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
        //manager.getDefaultDisplay().getMetrics(dm);
        screen_width = dm.widthPixels;//得到屏幕的宽度
        //int height=dm.heightPixels;
        fragmentHomePageOnes.clear();
        for (int i = 0; i < items.length; i++) {
            fragmentHomePageOnes.add(FragmentHomePageOne.newInstance());
        }

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        initRadioGroup(view, items);
        initViewPager(view);

        return view;
    }


    /**
     * 初始化home 界面的 viewpager
     *
     * @param view
     */
    private void initViewPager(View view) {
        homeViewPager = (ViewPager) view.findViewById(R.id.home_viewPager);
        pagerAdapter = new MyPagerAdapter(fm);
        homeViewPager.setAdapter(pagerAdapter);
        homeViewPager.addOnPageChangeListener(this);//viewPage页面改变监听器，提到了之前的set方式
    }

    /**
     * 初始化RadioGroup,动态添加item
     **/
    private void initRadioGroup(View view, String items[]) {
        radioButtonList.clear();//防止Fragment再次创建视图时，保留了之前的数据
        linearLayout = (LinearLayout) view.findViewById(R.id.home_scroll_linearLayout);
        home_scroll = (HorizontalScrollView) view.findViewById(R.id.home_scroll);
        //
        RadioButton radioButton;
        for (int i = 0; i < items.length; i++) {
            View view0 = LayoutInflater.from(getActivity()).inflate(R.layout.home_scroll_item, null);
            //radioButton = (RadioButton) View.inflate(getActivity(), R.layout.home_scroll_item, null);
            radioButton = (RadioButton) view0.findViewById(R.id.home_scroll_RadioButton);
            radioButton.setText("" + items[i]);
            if (i == 0) {
                radioButton.setChecked(true);
                radioButton.setTextColor(getResources().getColor(R.color.colorMain));
            }

            radioButton.setOnClickListener(this);
            linearLayout.addView(radioButton, i);
            radioButtonList.add(radioButton);
        }

    }


    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {//根据page的所在页面改变rb的选中

        if (oldPosition != position) {
            //RadioButton radioButton = (RadioButton) radioGroup.getChildAt(position);//通过index得到对应的radioButton

            RadioButton rb = radioButtonList.get(position);
            rb.setChecked(true);
//            //System.out.println(rb.getX()+"  "+rb.getPivotX()+"  w"+rb.getWidth()+"   mw"+rb.getMaxWidth()+"   tx"+rb.getTranslationX());
//            System.out.println(home_scroll.isFillViewport()+"   " +home_scroll.getBaseline());
//            int x = (int) rb.getX();


            scrollSelfAdapta(position);
            rb.setTextColor(getResources().getColor(R.color.colorMain));
            RadioButton rb1 = radioButtonList.get(oldPosition);
            rb1.setChecked(false);
            rb1.setTextColor(getResources().getColor(R.color.colorTextOne));
            oldPosition = position;
        }
    }

    /**
     * 滚动条显示自动根据选择的rb，显示出来，好东西
     *
     * @param position 当前rb位置
     */
    private void scrollSelfAdapta(int position) {
        if (position < oldPosition) {
            home_scroll.fling(-screen_width);

        } else {
            home_scroll.fling(screen_width);
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @Override
    public void onClick(View v) {

        if (v instanceof RadioButton) {//类别选择条，选择使viewpager跳到对应的page
            for (int i = 0; i < radioButtonList.size(); i++) {
                if (radioButtonList.get(i).isChecked() && oldPosition != i) {
                    //radioButtonList.get(i).setChecked(false);
                    //oldPosition = i;
                    homeViewPager.setCurrentItem(i);
                } else if (!radioButtonList.get(i).isChecked() && oldPosition != i) {
                    radioButtonList.get(i).setChecked(false);
                }
            }
        }

    }


    class MyPagerAdapter extends FragmentStatePagerAdapter {

        public MyPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return fragmentHomePageOnes.get(position);
        }

        @Override
        public int getCount() {
            return items.length;
        }
    }
}