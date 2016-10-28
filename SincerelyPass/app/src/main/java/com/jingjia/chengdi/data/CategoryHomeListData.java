package com.jingjia.chengdi.data;

import com.jingjia.chengdi.encapsulation.HomeListContent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by Administrator on 2016/9/22.
 */
public class CategoryHomeListData {
    private List<HomeListContent> homeLists;
    private  Map<String, List<HomeListContent>> categoryMap = new HashMap<>();//分类的数据
    private Set<String> categorySet = new HashSet<>();

    public CategoryHomeListData(List<HomeListContent> homeLists) {
        this.homeLists = homeLists;
        for (int i = 0; i < homeLists.size(); i++) {
            HomeListContent hlc = homeLists.get(i);
            addInCategory(hlc);
        }
    }

    private void addInCategory(HomeListContent hlc) {//分类的数据
        String category = hlc.getCategory();
        if (categorySet.contains(category)) {//如果类别中已经存在了此类型
            categoryMap.get(category).add(hlc);//将对应的封装数据添加到Map对应的list中
        } else {//如果不存在此类型，则从新加入一块内存用于保存此类型的数据
            homeLists = new ArrayList<>();
            homeLists.add(hlc);
            categoryMap.put(category, homeLists);
            categorySet.add(category);
        }
    }
}
