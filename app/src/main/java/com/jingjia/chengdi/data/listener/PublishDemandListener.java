package com.jingjia.chengdi.data.listener;

import com.jingjia.chengdi.data.encapsulation.DemandInfo;

import java.util.List;

/**
 * Created by Administrator on 2016/10/11.
 */
public interface PublishDemandListener {
    void haveMyDemand(DemandInfo demandInfo);

    void haveMyDemands(List<DemandInfo> demandInfoList);

    void haveMyDemandComps(List<DemandInfo> demandInfoList);

    void removeAll();

    void notificationFlash();
}
