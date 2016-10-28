package com.jingjia.chengdi.data.encapsulation;

import java.util.List;

/**
 * 用于Gson解析地区数据用，地区数据存放在assets
 */
public class Province {
    private List<City> city;
    private String id;
    private String province;

    public List<City> getCity() {
        return city;
    }

    public void setCityList(List<City> city) {
        this.city = city;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    @Override
    public String toString() {
        return "Province [cityList=" + city + ", id=" + id + ", province="
                + province + "]";
    }

}
