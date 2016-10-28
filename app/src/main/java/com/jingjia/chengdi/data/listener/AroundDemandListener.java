package com.jingjia.chengdi.data.listener;

import com.jingjia.chengdi.data.encapsulation.DemandInfo;

import java.util.List;

/**
 * Created by Administrator on 2016/10/13.
 */
public interface AroundDemandListener {
    void haveAroundDemand(List<DemandInfo> demandInfoList);
    void haveAroundDemandRemoved(int position);
}
