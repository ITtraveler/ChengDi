package com.jingjia.chengdi.data;

import android.text.StaticLayout;

import com.jingjia.chengdi.encapsulation.GoListAcceptance;
import com.jingjia.chengdi.encapsulation.GoListPublish;
import com.jingjia.chengdi.encapsulation.HomeListContent;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by Administrator on 2016/9/19.
 */
public class TestData {
    static Random random = new Random();
    /**
     * go 数据
     */
    public static List<GoListAcceptance> goListAcceptances = new ArrayList<>();

    static {
        Category[] category = Category.values();
        for (int i = 0; i < 300; i++) {
            goListAcceptances.add(new GoListAcceptance("" + category[random.nextInt(category.length)], "已完成" + i, "一根冰棒，一份正新鸡排，一杯奶茶，一块德芙巧克力。", "5￥", "信阳师范学院19号楼416"));
        }
    }

    /**
     * home scroll item 数据
     */
    public static String[] items = {"身边最新", "买东西", "取东西", "代办事", "其他","代步","帮忙","娱乐天地"};
    public static List<HomeListContent> homeListContents = new ArrayList<>();

    static {
        Category[] category = Category.values();

        for (int i = 0; i < 300; i++) {

            homeListContents.add(new HomeListContent("下一秒记忆" + i, "" + category[random.nextInt(category.length)]
                    , "2016-9-19", "https://ss0.bdstatic.com/70cFuHSh_Q1YnxGkpoWK1HF6hhy/it/u=3654727249,1549819339&fm=116&gp=0.jpg"
                    , "一根冰棒，一份正新鸡排，一杯奶茶，一块德芙巧克力。", random.nextInt(20) + "￥", "信阳师范学院19号楼416"));
        }
    }

    public  static  List<GoListPublish> goListPublishs = new ArrayList<>();

    static {
        for(int i = 0;i < 20;i++){
            goListPublishs.add(new GoListPublish("2016-9-23 13:38","进行中","一根冰棒，一份正新鸡排，一杯奶茶，一块德芙巧克力。","7￥","信阳师范学院","程同学","18593726564-"+i));
        }
    }
}
