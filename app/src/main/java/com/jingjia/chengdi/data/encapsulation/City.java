package com.jingjia.chengdi.data.encapsulation;

import java.util.List;

/**
 * 用于Gson解析地区数据用，地区数据存放在assets
 */
public class City {
    private String city;
    private List<District> district;

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public List<District> getDistrict() {
        return district;
    }

    public void setDistrictList(List<District> district) {
        this.district = district;
    }
}
