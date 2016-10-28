package com.jingjia.chengdi.utils;

import com.jingjia.chengdi.data.NetUri;
import com.jingjia.chengdi.data.encapsulation.DemandInfo;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;

/**
 * Created by Administrator on 2016/10/11.
 * 初始化数据，从服务器中下载下来。保存于内存中，使用地方：用户登录需调用，用户 重启应用需调用；
 */
public class UpdateData {

    private static UpdateData updateDate;

    public static UpdateData newUpdateDate() {
        if (updateDate == null) {
            updateDate = new UpdateData();
        }
        return updateDate;
    }

    /**
     * 初始化所有数据，除主页的需求信息
     * 在登录和注册完成时会调用
     */
    public static void initAll() {
        initMyPublishing();
        initMyPublished();
        initMyAccepting();
        initMyAccepted();
    }

    public void reGetData() {
        updateMyPublishingData();
        updateMyPublishData();
        updateMyAcceptedData();
        updateMyAcceptingData();
    }

    /************************
     * 我的发布信息获取
     *************************/
    // public static int myDemandPage = 0;
    // public static int myDemandPageComp = 0;
    public static int myDemandPages[] = {0, 0, 0, 0, 0, 0, 0};//0123456状态

    public static boolean haveMyDemand = true;//在调用的类中可以通过此来决定是否还需要加载数据
    public static boolean haveMyDemandComp = true;//在调用的类中可以通过此来决定是否还需要加载数据

    //(0待接单 1未完成，2接单人申请完成，3确认完成，4申请撤销订单，5是已撤销订单，6，申请放弃接单)
    //对应状态：待接单,进行中,完成待处理,已完成,撤销待处理,已撤销,放弃待处理
    public void updateMyPublishData(final int status) {
        Map<String, String> param = new HashMap<>();
        param.put("phone", MyApplication.user.getPhone());
        param.put("page", "" + myDemandPages[status]);
        param.put("status", "" + status);
        NetUtils.get(NetUri.URI_GET_MY_REQUIRES, param, new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int i) {

            }

            @Override
            public void onResponse(String s, int i) {
                List<DemandInfo> resultDemands = ActivityUtils.parasJsonDemandArray(s);
                if (resultDemands.size() > 0) {
                    if (status == 0 || status == 1 || status == 2 || status == 4 || status == 6) {//我的发布，
//                        if (myDemandPages[status] == 0) {
//                            MyApplication.myDemandList.clear();
//                            MyApplication.myDemandList.addAll(resultDemands);
//                        } else {
                        MyApplication.newInstance().addMyDemands(resultDemands);//会触发监听器
                        //      }
                    } else if (status == 3 || status == 5) {
//                        if (myDemandPages[status] == 0) {
//                            MyApplication.myDemandListComp.clear();
//                            MyApplication.myDemandListComp.addAll(resultDemands);
//                        } else {
                        MyApplication.newInstance().addMyDemandComps(resultDemands);
//                        }
                    }
                    myDemandPages[status]++;
                }
            }
        });
    }

    /**
     * 更新我的发布信息ing
     */
    public void updateMyPublishingData() {
        updateMyPublishData(2);
        updateMyPublishData(4);
        updateMyPublishData(6);
        updateMyPublishData(1);
        updateMyPublishData(0);
    }

    // 更新我的发布信息ed
    public void updateMyPublishData() {
        updateMyPublishData(3);
        updateMyPublishData(5);
    }

    /**
     * 初始化数据，在登录、注册登录或者刷新时调用此方法
     */
    public static void initMyPublishing() {
        MyApplication.myDemandList.clear();
        haveMyDemand = true;
        myDemandPages[0] = 0;
        myDemandPages[1] = 0;
        myDemandPages[2] = 0;
        myDemandPages[4] = 0;
        myDemandPages[6] = 0;

    }

    public static void initMyPublished() {
        MyApplication.myDemandListComp.clear();
        haveMyDemandComp = true;
        myDemandPages[3] = 0;
        myDemandPages[5] = 0;
    }

    /********************
     * 我的接单信息获取
     ***************/
    public static int myAcceptedPages[] = {0, 0, 0, 0, 0, 0, 0};//0123456状态只有1236状态可用

    public static boolean haveMyAccepted = true;//在调用的类中可以通过此来决定是否还需要加载数据
    public static boolean haveMyAcceptedComp = true;//在调用的类中可以通过此来决定是否还需要加载数据

    public void updateMyAcceptedData(final int status) {
        Map<String, String> param = new HashMap<>();
        param.put("phone", MyApplication.user.getPhone());
        param.put("page", "" + myAcceptedPages[status]);
        param.put("status", "" + status);
        NetUtils.get(NetUri.URI_MY_ACCEPTED_ORDER, param, new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int i) {

            }

            @Override
            public void onResponse(String s, int i) {
                List<DemandInfo> resultDemands = ActivityUtils.parasJsonDemandArray(s);
                if (resultDemands.size() > 0) {
                    if (status == 1 || status == 2 || status == 4 || status == 6) {//我的发布，
                        MyApplication.newInstance().addMyAcceptedDemands(resultDemands);//会触发监听器
                    } else if (status == 3 || status == 5) {
                        MyApplication.newInstance().addMyAcceptedDemandComps(resultDemands);
                    }
                    myAcceptedPages[status]++;
                    if(status == 1)
                    System.out.println("status1:"+myAcceptedPages[status]);
                }
            }
        });
    }

    public static void initMyAccepting() {
        MyApplication.myAcceptedList.clear();
        haveMyAccepted = true;
        myAcceptedPages[1] = 0;
        myAcceptedPages[2] = 0;
        myAcceptedPages[4] = 0;
        myAcceptedPages[6] = 0;
    }

    public static void initMyAccepted() {
        MyApplication.myAcceptedListComp.clear();
        haveMyAcceptedComp = true;
        myAcceptedPages[3] = 0;
        myAcceptedPages[5] = 0;
    }

    /**
     * 跟新所有我的发布信息，不管状态如何
     */
    public void updateMyAcceptingData() {
        updateMyAcceptedData(2);
        updateMyAcceptedData(4);
        updateMyAcceptedData(6);
        updateMyAcceptedData(1);
    }

    public void updateMyAcceptedData() {
        updateMyAcceptedData(3);
        updateMyAcceptedData(5);
    }
    /**************************周围最新信息获取*******************************/
    /**
     * 获取周边所有的需求信息
     */
    public static int aroundDemandsPage = 0;
    public static boolean haveAroundDemands = true;

    public void updateAroundDemandData() {
        Map<String, String> param = new HashMap<>();
        param.put("page", "" + aroundDemandsPage);
        NetUtils.get(NetUri.URI_GET_REQUIRES, param, new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int i) {

            }

            @Override
            public void onResponse(String s, int i) {
                List<DemandInfo> resultDemands = ActivityUtils.parasJsonDemandArray(s);
                if (resultDemands.size() > 0) {
                    if (aroundDemandsPage == 0) {
                        MyApplication.aroundDemandList.clear();
                        MyApplication.aroundDemandList.addAll(resultDemands);
                    } else {
                        MyApplication.newInstance().addAroundDemands(resultDemands);//会触发监听器
                    }
                    aroundDemandsPage++;
                }
            }
        });
    }


    public static void initAroundDemands() {
        aroundDemandsPage = 0;
        haveAroundDemands = true;
        UpdateData.newUpdateDate().updateAroundDemandData();
    }
}
