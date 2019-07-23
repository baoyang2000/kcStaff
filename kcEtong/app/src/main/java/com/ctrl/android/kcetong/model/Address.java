package com.ctrl.android.kcetong.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/10/19.
 */

public class Address implements Serializable {
    /*
    "provinceId": "山东省",
            "belong": "0",
            "cityId": "济南",
            "street": "12345",
            "zipcode": "",
            "areaId": "历下区",
            "cityIds": "370100",
            "id": "e63b613e43c041aebfd3d830965420c3",
            "address": "湖西桥",
            "isDefault": "1",
            "userName": "赵先生",
            "mapx": "117.02304",
            "areaIds": "370102",
            "provinceIds": "370000",
            "mapy": "36.68113",
            "mobile": "15753191585"
    */
    private String provinceId;//省份Id
    private int belong;
    private String cityId; //城市Id
    private String street; //街道号
    private String zipcode; //
    private String areaId;//区Id
    private String cityName; //
    @SerializedName(value = "id", alternate = {"addressId"})
    private String id;
    private String address;
    private int isDefault;
    private String userName;
    private String areaName;
    private String provinceName;
    private String mobile;
    @SerializedName(value = "mapx", alternate = {"longitude"})
    private double longitude;
    @SerializedName(value = "mapy", alternate = {"latitude"})
    private double latitude;//

    //百度搜索详细地址别名
    private String detailName;
    //是否是当前搜索地址（用于百度地图选址）
    private boolean isCurrent;

    public String getProvinceId() {
        return provinceId;
    }

    public void setProvinceId(String provinceId) {
        this.provinceId = provinceId;
    }

    public int getBelong() {
        return belong;
    }

    public void setBelong(int belong) {
        this.belong = belong;
    }

    public String getCityId() {
        return cityId;
    }

    public void setCityId(String cityId) {
        this.cityId = cityId;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getZipcode() {
        return zipcode;
    }

    public void setZipcode(String zipcode) {
        this.zipcode = zipcode;
    }

    public String getAreaId() {
        return areaId;
    }

    public void setAreaId(String areaId) {
        this.areaId = areaId;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getIsDefault() {
        return isDefault;
    }

    public void setIsDefault(int isDefault) {
        this.isDefault = isDefault;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getAreaName() {
        return areaName;
    }

    public void setAreaName(String areaName) {
        this.areaName = areaName;
    }

    public String getProvinceName() {
        return provinceName;
    }

    public void setProvinceName(String provinceName) {
        this.provinceName = provinceName;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public String getDetailName() {
        return detailName;
    }

    public void setDetailName(String detailName) {
        this.detailName = detailName;
    }

    public boolean isCurrent() {
        return isCurrent;
    }

    public void setCurrent(boolean current) {
        isCurrent = current;
    }

}
