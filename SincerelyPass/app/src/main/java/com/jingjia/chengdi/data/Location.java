package com.jingjia.chengdi.data;

import com.baidu.location.Poi;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/9/24.
 */
public class Location {
    //private String describe;//
    private double latitude = 0;//纬度
    private double longitude = 0;//经度
    private String locationDescribe;//区域较详细的描述
    private String address;//大概地址

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getLocationdescribe() {
        return locationDescribe;
    }

    public void setLocationdescribe(String locationdescribe) {
        this.locationDescribe = locationdescribe;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
