package com.jingjia.chengdi.data;

/**
 * Created by Administrator on 2016/10/2.
 * 命名规则URI_*
 */
public class NetUri {
    public static class Status {
        public static final int SUCCESS = 200;
        public static final int EXIST = 300;
        public static final int ERROR = 400;

    }

    private static final String URL = "http://182.254.145.193/chengdi";
    public static final String URI_LOGIN = URL + "/login";
    public static final String URI_REGISTER = URL + "/register";
    public static final String URI_USERHEAD = URL + "";
    public static final String URI_FEEDBACK = URL + "";
    public static final String URI_UPDATEPSW = URL + "/updatePwd";
    public static final String URI_UPDATE_USERINFO = URL + "/updateUser";
    public static final String URI_GET_USERINFO = URL + "/toUser";

    public static final String URI_PUBLISH_DEMAND = URL + "/issueRequire";
    public static final String URI_SHOW_ALL_DEMAND = URL + "/showRequire";
    /**
     * 得到我发布的需求，传入我的手机号+page+status
     */
    public static final String URI_GET_MY_REQUIRES = URL + "/myRequires";
    /**
     * 获取所有的需求，需传入页数，关键字page
     */
    public static final String URI_GET_REQUIRES = URL + "/requires";
    /**
     * 我接受订单动作，传入我的phone和被接受订单的id
     */
    public static final String URI_ACCEPTING = URL + "/accept";
    /**
     * 完成订单需要传入订单id和status，status=2是申请完成，status=3是确认完成
     */
    public static final String URI_COMPLETE_ORDER = URL + "/complete";
    /**
     * 获取 我接受的订单，传入我的手机号和page
     */
    public static final String URI_MY_ACCEPTED_ORDER=URL+"/acceptedRequire";

    /**
     * 用户投诉某个订单userPhone ,context,requireId
     */
    public static final String URI_ISSUE_COMPLAIN = URL+"/issueComplain";
}
