package com.jingjia.chengdi.dialog;

import android.app.AlertDialog;
import android.content.Context;
import android.view.View;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jingjia.chengdi.R;
import com.jingjia.chengdi.custom.view.OnWheelViewListener;
import com.jingjia.chengdi.custom.view.WheelView;
import com.jingjia.chengdi.data.encapsulation.City;
import com.jingjia.chengdi.data.encapsulation.District;
import com.jingjia.chengdi.data.encapsulation.Province;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2016/10/7.
 */
public class ProvinceDialog extends AlertDialog {
    public static List<Province> provinces;
    private WheelView wheelProvince, wheelCity, wheelDistrict;
    private OnDialogProvinceListener provinceListener;
    private String province = "北京市", city = "北京市", district = "石景山区";
    private List<String> provinceList;
    private Map<String, List<String>> cityMaps, districtMaps;

    public ProvinceDialog(Context context, OnDialogProvinceListener provinceListener) {
        super(context);
        this.provinceListener = provinceListener;
        initDate();
        initView();
    }

    /**
     * 初始化数据，从asses中读取。
     */
    private void initDate() {
        if (provinces == null) {
            Gson gson = new Gson();
            Type t = new TypeToken<LinkedList<Province>>() {
            }.getType();
            StringBuilder sb = new StringBuilder();
            String s;
            try {
                InputStream is = getContext().getAssets().open("provinces.txt");
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(is));
                while ((s = bufferedReader.readLine()) != null) {
                    sb.append(s);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            provinces = gson.fromJson(sb.toString(), t);
        }

        provinceList = new ArrayList<>();
        cityMaps = new HashMap<>();
        districtMaps = new HashMap<>();

        for (Province p : provinces) {//省
            provinceList.add(p.getProvince());
            //System.out.print(p.getProvince() + "  " + p.getCity().size() + "  ");
            List<String> citys = new ArrayList<>();
            for (City city : p.getCity()) {//城市
                citys.add(city.getCity());
                //System.out.print("  " + city.getCity() + ":");
                List<String> districts = new ArrayList<>();
                for (District district : city.getDistrict()) {//地区
                    //  System.out.print(" " + district.getDistrict());
                    districts.add(district.getDistrict());
                }
                districtMaps.put(city.getCity(), districts);
            }
            cityMaps.put(p.getProvince(), citys);
        }
    }

    /**
     * 初始化view，包括wheelView的监听
     */
    private void initView() {
        View v = getLayoutInflater().inflate(R.layout.dialog_province_select, null);
        wheelProvince = (WheelView) v.findViewById(R.id.dialog_wheel_province);
        wheelCity = (WheelView) v.findViewById(R.id.dialog_wheel_city);
        wheelDistrict = (WheelView) v.findViewById(R.id.dialog_wheel_district);
        wheelProvince.setItems(provinceList);
        wheelCity.setItems(cityMaps.get(provinceList.get(0)));
        wheelDistrict.setItems(districtMaps.get(cityMaps.get(provinceList.get(0)).get(0)));
        wheelProvince.setOnWheelViewListener(new OnWheelViewListener() {
            @Override
            public void onSelected(int selectedIndex, String item) {
                wheelCity.setItems(cityMaps.get(item));
                province = item;
                city = cityMaps.get(item).get(0);
                district = districtMaps.get(cityMaps.get(item).get(0)).get(0);
                wheelDistrict.setItems(districtMaps.get(cityMaps.get(item).get(0)));
                wheelCity.setSeletion(0);
                wheelDistrict.setSeletion(0);
                provinceListener.getProvince(province, city, district);
            }
        });
        wheelCity.setOnWheelViewListener(new OnWheelViewListener() {
            @Override
            public void onSelected(int selectedIndex, String item) {
                city = item;
                district = districtMaps.get(item).get(0);
                wheelDistrict.setItems(districtMaps.get(item));
                wheelDistrict.setSeletion(0);
                System.out.println(districtMaps.get(item).toString());
                provinceListener.getProvince(province, city, district);
            }
        });
        wheelDistrict.setOnWheelViewListener(new OnWheelViewListener() {
            @Override
            public void onSelected(int selectedIndex, String item) {
                district = item;
                provinceListener.getProvince(province, city, district);
            }
        });
        setView(v);
    }


}
