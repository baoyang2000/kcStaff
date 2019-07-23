package com.ctrl.android.kcetong.model;

import java.io.Serializable;

/**
 * Created by liu on 2017/2/3.
 */

public class RegionDetailList implements Serializable {
    /*"regionalPropertiesList": [{
        "id": "5f427aa725ef4eb295f1d670654ba2c2",
                "regionalManagementId": "7b9127d3d91a4eafac99c3a6dac2e78a",
                "propertiesName": "1111111",
                "propertiesImg": "http://og3hq2x84.bkt.clouddn.com/3c800c05e6d849cdaf86529bae0fd41f.jpeg",
                "phone": "111",
                "information": "11",
                "disabled": "0",
                "createTime": 1484209539000,
                "index": 0,
                "rowCountPerPage": 0
    }]*/
//    [{"id":"5f427aa725ef4eb295f1d670654ba2c2",
//            "regionalManagementId":"7b9127d3d91a4eafac99c3a6dac2e78a",
//            "propertiesName":"1111111",
//            "propertiesImg":"http://og3hq2x84.bkt.clouddn.com/3c800c05e6d849cdaf86529bae0fd41f.jpeg",
//            "phone":"111"
//            ,"information":"11",
//            "disabled":"0",
//            "createTime":1484209539000,
//            "index":0,
//            "rowCountPerPage":0,
//            "follow":"1"}
    private String id;
    private String regionalManagementId;
    private String propertiesName;
    private String propertiesImg;
    private String phone;
    private String information;
    private String disabled;
    private long createTime;
    private int index;
    private int rowCountPerPage;
    private String follow;

    @Override
    public String toString() {
        return "RegionDetailList{" +
               "id='" + id + '\'' +
               ", regionalManagementId='" + regionalManagementId + '\'' +
               ", propertiesName='" + propertiesName + '\'' +
               ", propertiesImg='" + propertiesImg + '\'' +
               ", phone='" + phone + '\'' +
               ", information='" + information + '\'' +
               ", disabled='" + disabled + '\'' +
               ", createTime=" + createTime +
               ", index=" + index +
               ", rowCountPerPage=" + rowCountPerPage +
               ", follow='" + follow + '\'' +
               '}';
    }

    public String getFollow() {
        return follow;
    }

    public void setFollow(String follow) {
        this.follow = follow;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getRegionalManagementId() {
        return regionalManagementId;
    }

    public void setRegionalManagementId(String regionalManagementId) {
        this.regionalManagementId = regionalManagementId;
    }

    public String getPropertiesName() {
        return propertiesName;
    }

    public void setPropertiesName(String propertiesName) {
        this.propertiesName = propertiesName;
    }

    public String getPropertiesImg() {
        return propertiesImg;
    }

    public void setPropertiesImg(String propertiesImg) {
        this.propertiesImg = propertiesImg;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getInformation() {
        return information;
    }

    public void setInformation(String information) {
        this.information = information;
    }

    public String getDisabled() {
        return disabled;
    }

    public void setDisabled(String disabled) {
        this.disabled = disabled;
    }

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public int getRowCountPerPage() {
        return rowCountPerPage;
    }

    public void setRowCountPerPage(int rowCountPerPage) {
        this.rowCountPerPage = rowCountPerPage;
    }
}
