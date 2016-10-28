package com.jingjia.chengdi;

import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jingjia.chengdi.fragment.FragmentGo;
import com.jingjia.chengdi.fragment.FragmentHome;
import com.jingjia.chengdi.utils.ActivityUtils;
import com.jingjia.chengdi.utils.BDLocationUtils;
import com.jingjia.chengdi.utils.MyApplication;

import java.util.List;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener {
    private final int LOCATION_REQUIR_CODE = 1;
    private DrawerLayout drawer;
    private RadioButton rb_status, rb_home, rb_go;
    private boolean isHide = false;
    private RelativeLayout bm;
    private FragmentManager fragmentManager;
    private int curPage = 0;//当前在home/go 界面
    private final int FRAGMENT_HOME = 0;//home界面
    private final int FRAGMENT_GO = 1;//go界面
    private View appBar;
    private FragmentHome fh;
    private FragmentGo fg;
    private TextView tv_location;
    private BDLocationUtils bdLocationUtils;
    private static MainActivity mainActivity;

    public MainActivity newInstance() {
        if (mainActivity == null) {
            mainActivity = new MainActivity();
        }
        return mainActivity;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        initBottomMenu();
        initFragment();
        initLocation();
    }

    /**
     * 初始化fragment
     */
    private void initFragment() {
        fragmentManager = getSupportFragmentManager();
        fh = FragmentHome.newInstance();
        fg = FragmentGo.newInstance();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        // transaction.replace(R.id.fragment,FragmentHome.newInstance());
       // if (!fh.isAdded()) {//防止重复添加导致异常

            transaction.add(R.id.fragment, fh);
        //} else if (!fg.isAdded()) {
            transaction.add(R.id.fragment, fg);
        //}
        transaction.hide(fg);
        fg.onPause();//暂停此，防止与其他fragment乱窜现象
        transaction.commit();
    }

    /**
     * 初始化底部菜单
     */

    private void initBottomMenu() {
        bm = (RelativeLayout) findViewById(R.id.bottom_menu);
        rb_status = (RadioButton) findViewById(R.id.menu_rb_hide);
        rb_home = (RadioButton) findViewById(R.id.menu_rb_home);
        rb_go = (RadioButton) findViewById(R.id.menu_rb_go);
        rb_status.setOnClickListener(this);
        rb_home.setOnClickListener(this);
        rb_go.setOnClickListener(this);
    }

    private void initView() {
        //右滑抽屉式布局
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        if (navigationView != null) {
            navigationView.setNavigationItemSelectedListener(this);
        }

        //头像位置,注意头像部分的点击必须通过navigatiomView.getheaderView来得到子view，进行事件监听，否则将会抛出空指针异常
        LinearLayout sideNavBar = (LinearLayout) navigationView.getHeaderView(0).findViewById(R.id.side_nav_bar);
        assert sideNavBar != null;
        sideNavBar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivityUtils.startActivity(MainActivity.this, UserInfoActivity.class);
            }
        });

        //右滑抽屉式布局
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ImageView imageView = (ImageView) findViewById(R.id.ic_bar_expansion);
        assert imageView != null;
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawer.openDrawer(GravityCompat.START);
            }
        });


        appBar = findViewById(R.id.app_bar);

        //发布按钮
        ImageButton iv_publish = (ImageButton) findViewById(R.id.menu_bn_publish);
        assert iv_publish != null;
        iv_publish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivityUtils.startActivity(MainActivity.this, PublishActivity.class);
            }
        });
        //定位
        LinearLayout linearLayout = (LinearLayout) findViewById(R.id.main_bar_location);
        assert linearLayout != null;
        linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, LocationSelectActivity.class);
                startActivityForResult(intent, LOCATION_REQUIR_CODE);
            }
        });
        tv_location = (TextView) findViewById(R.id.main_bar_tv_location);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    /**
     * 右滑导航栏的按钮事件
     *
     * @param item
     * @return
     */
    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_pocket) {
            // Handle the camera action
        } else if (id == R.id.nav_notice) {

        } else if (id == R.id.nav_service) {

        } else if (id == R.id.nav_set) {//导航栏设置
            ActivityUtils.startActivity(MainActivity.this, SetActivity.class);
        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_advice) {

        }


        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.menu_rb_home://主页按钮
                menuHomeAction();
                break;
            case R.id.menu_rb_go://进行中按钮
                menuGoAction();
                break;
            case R.id.menu_rb_hide://菜单隐藏按钮
                if (isHide) {
                    rb_status.setChecked(true);
                    isHide = false;
                    changeBottomMenuState(bm, "show");
                } else {
                    rb_status.setChecked(false);
                    isHide = true;
                    changeBottomMenuState(bm, "hide");
                }
                break;
        }
    }

    /**
     * go按钮点击
     */
    private void menuGoAction() {
        rb_go.setChecked(true);
        rb_home.setChecked(false);
        if (curPage != FRAGMENT_GO) {
            fg.onResume();
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            // transaction.replace(R.id.fragment,fg);
            transaction.show(fg);
            transaction.hide(fh);
            transaction.commit();
            fh.onPause();
            curPage = FRAGMENT_GO;
            appBar.setVisibility(View.GONE);
        }
    }

    /**
     * home按钮点击
     */
    private void menuHomeAction() {
        rb_home.setChecked(true);
        rb_go.setChecked(false);
        if (curPage != FRAGMENT_HOME) {
            fh.onResume();
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            //  transaction.replace(R.id.fragment, fh);
            transaction.show(fh);
            transaction.hide(fg);
            transaction.commit();
            fg.onPause();
            curPage = FRAGMENT_HOME;
            appBar.setVisibility(View.VISIBLE);
        }

    }

    /**
     * 让底部菜单栏视图隐藏与出现
     *
     * @param view
     * @param command
     */
    private void changeBottomMenuState(View view, String command) {
        float scrollLong = getResources().getDimension(R.dimen.bottom_scroll_long);//利用此种方法可以解决因手机分辨率不同，而值不同
        if (command.equals("hide")) {
            ObjectAnimator.ofFloat(view, "translationY", 0f, scrollLong).setDuration(500).start();
        } else if (command.equals("show")) {
            ObjectAnimator.ofFloat(view, "translationY", scrollLong, 0f).setDuration(500).start();
        }

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == LOCATION_REQUIR_CODE && resultCode == RESULT_OK) {
            String curLocation = data.getStringExtra(LocationSelectActivity.LOCATION);
            tv_location.setText(curLocation);
        }
    }

    private void initLocation() {
        bdLocationUtils = BDLocationUtils.newInstance(getApplicationContext());
        handler.sendEmptyMessageDelayed(0x1, 500);
    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 0x1) {
                if (bdLocationUtils.getLocationList().size() > 0) {
                    tv_location.setText(bdLocationUtils.getLocationList().get(0));
                } else {
                    handler.sendEmptyMessageDelayed(0x1, 500);
                }
            }
        }
    };

}
