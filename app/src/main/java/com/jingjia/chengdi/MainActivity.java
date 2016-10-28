package com.jingjia.chengdi;

import android.animation.ObjectAnimator;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
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

import com.jingjia.chengdi.BroadCast.CDNetBroadCastReceive;
import com.jingjia.chengdi.BroadCast.OnNetStatusListener;
import com.jingjia.chengdi.fragment.FragmentGo;
import com.jingjia.chengdi.fragment.FragmentHome;
import com.jingjia.chengdi.utils.ActivityUtils;
import com.jingjia.chengdi.utils.BDLocationUtils;
import com.jingjia.chengdi.utils.MyApplication;
import com.jingjia.chengdi.utils.NetStatus;
import com.jingjia.chengdi.utils.SQLiteDbUtils;

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
    private View appBar, bottomLogin, mNetHintBar;
    private FragmentHome fh;
    private FragmentGo fg;
    private TextView tv_location;
    private BDLocationUtils bdLocationUtils;
    private TextView nav_username;
    ActivityUtils activityUtils;
    private CDNetBroadCastReceive cdNetBCR;
    private View barLocation, barTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        activityUtils = new ActivityUtils();
        initDate();
        initView();
        initBottomMenu();
        initFragment(savedInstanceState);
        initLocation();
        initLoginHint();

        updateNavHead();
        registerBroadCast();
        //  NetStatus.newNetSatus(this).getNetStyle();
        //getSharedPreferences("ChengDi",Context.MODE_PRIVATE);
    }


    private void initDate() {
        if (activityUtils.getLoginStatus(this)) {//如果已登录
            String phone = activityUtils.getLoginPhone(this);
            System.out.println("curPhone:" + phone);
            MyApplication.user = new SQLiteDbUtils(getApplicationContext()).getUser(activityUtils.getLoginPhone(this));
            System.out.println("initUser:" + MyApplication.user);
        }
    }

    /**
     * 底部提示登录
     */
    private void initLoginHint() {
        bottomLogin = findViewById(R.id.hint_login);
        if (activityUtils.getLoginStatus(this)) {
            bottomLogin.setVisibility(View.GONE);
        } else {
            bottomLogin.setVisibility(View.VISIBLE);
        }
        View toLogin = findViewById(R.id.cd_bn_login);//去登陆按钮
        assert toLogin != null;
        toLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivityUtils.startActivity(MainActivity.this, LoginActivity.class);
            }
        });
    }

    /**
     * 初始化fragment
     */
    private void initFragment(Bundle savedInstanceState) {
        fragmentManager = getSupportFragmentManager();
        if (savedInstanceState != null) { // “内存重启”时调用
            //从fragmentManager里面找到fragment
            fh = (FragmentHome) fragmentManager.findFragmentByTag(FragmentHome.class.getName());
            fg = (FragmentGo) fragmentManager.findFragmentByTag(FragmentGo.class.getName());
        } else {

            fh = FragmentHome.newInstance();
            fg = FragmentGo.newInstance();
        }
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        if (!fh.isAdded()) {//防止重复添加导致异常
            transaction.add(R.id.fragment, fh, fh.getClass().getName());//为当前的fragment绑定一个tag，tag为当前绑定fragment的类名
        }
        if (!fg.isAdded()) {
            transaction.add(R.id.fragment, fg, fg.getClass().getName());
        }
        transaction.show(fh);
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
        nav_username = (TextView) navigationView.getHeaderView(0).findViewById(R.id.nav_username);

        assert sideNavBar != null;
        sideNavBar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (activityUtils.getLoginStatus(MainActivity.this)) {
                    ActivityUtils.startActivity(MainActivity.this, UserInfoActivity.class);

                } else {
                    ActivityUtils.startActivity(MainActivity.this, LoginActivity.class);

                }
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
        barLocation = findViewById(R.id.main_bar_location);
        assert barLocation != null;
        barLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, LocationSelectActivity.class);
                startActivityForResult(intent, LOCATION_REQUIR_CODE);
            }
        });
        tv_location = (TextView) findViewById(R.id.main_bar_tv_location);
        barTitle = findViewById(R.id.main_bar_title);
        //(TextView)findViewById(R.id.bar_net_status);
        mNetHintBar = findViewById(R.id.bar_net);
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
            ActivityUtils.startActivity(MainActivity.this, FeedbackActivity.class);
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
//        rb_go.setChecked(true);
//        rb_home.setChecked(false);
        if (curPage != FRAGMENT_GO) {
            fg.onResume();
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            // transaction.replace(R.id.fragment,fg);
            transaction.show(fg);
            transaction.hide(fh);
            transaction.commit();
            fh.onPause();
            curPage = FRAGMENT_GO;
            //appBar.setVisibility(View.GONE);
            barLocation.setVisibility(View.GONE);
            barTitle.setVisibility(View.VISIBLE);
        }

    }

    /**
     * home按钮点击
     */
    private void menuHomeAction() {
//        rb_home.setChecked(true);
//        rb_go.setChecked(false);
        if (curPage != FRAGMENT_HOME) {
            fh.onResume();
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            transaction.show(fh);
            transaction.hide(fg);
            transaction.commit();
            fg.onPause();
            curPage = FRAGMENT_HOME;
            //appBar.setVisibility(View.VISIBLE);
            barLocation.setVisibility(View.VISIBLE);
            barTitle.setVisibility(View.GONE);
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
            if (msg.what == 0x1) {//或许位置
                if (bdLocationUtils.getLocationList().size() > 0) {//获取到位置
                    tv_location.setText(bdLocationUtils.getLocationList().get(0));
                } else {
                    handler.sendEmptyMessageDelayed(0x1, 500);
                }
            }
        }
    };

    @Override
    protected void onRestart() {
        super.onRestart();
        System.out.println("restart");
        updateNavHead();
    }

    /**
     * 更新左滑导航栏的头部
     */
    private void updateNavHead() {
        if (activityUtils.getLoginStatus(this)) {
            String username = MyApplication.user.getUsername();
            System.out.println("curUser:" + MyApplication.user);
            nav_username.setText("" + username);
            bottomLogin.setVisibility(View.GONE);
        } else {
            nav_username.setText("点击登录");
            bottomLogin.setVisibility(View.VISIBLE);
        }
    }

    //注册广播
    private void registerBroadCast() {
        //注册网络状态监听器
        IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(cdNetBCR = new CDNetBroadCastReceive(new OnNetStatusListener() {
            @Override
            public void netCurType(String netType) {
                MyApplication.netStatus = netType;
                mNetHintBar.setVisibility(View.GONE);
                if (netType.equals(NetStatus.NET_DISCONNECTED)) {
                    mNetHintBar.setVisibility(View.VISIBLE);
                }

            }
        }), filter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        System.out.println("MainActivityOnResume");

    }

    @Override
    public void finish() {
        super.finish();
        System.out.println("MainActivityFinish");
        unregisterReceiver(cdNetBCR);
    }
}
