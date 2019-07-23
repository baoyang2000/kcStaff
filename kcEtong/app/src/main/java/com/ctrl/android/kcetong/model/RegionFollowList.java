package com.ctrl.android.kcetong.model;

import java.io.Serializable;

/**
 * Created by liu on 2017/2/4.
 */

public class RegionFollowList implements Serializable {
    /*"regionalFollowList": [{
        {
		"id": "45274fc44d3f4d4da59291c71e797489",
		"memberId": "7091ebfbd949490eb7a078e8d2f197ac",
		"regionalPropertiesId": "1",
		"index": 0,
		"rowCountPerPage": 0,
		"propertiesName": "置业1",
		"propertiesImg": "http://og3hq2x84.bkt.clouddn.com/a8fd977fbe444ad4a437823fcb6d3097.jpg",
		"regionalManagementId": "1",
		"phone": "13245678998"
	},]*/
    private String id;
    private String memberId;
    private String regionalPropertiesId;
    private long createTime;
    private int index;
    private int rowCountPerPage;
    private String propertiesName;
    private String propertiesImg;
    private String regionalManagementId;
    private String phone;

    @Override
    public String toString() {
        return "RegionFollowList{" +
               "id='" + id + '\'' +
               ", memberId='" + memberId + '\'' +
               ", regionalPropertiesId='" + regionalPropertiesId + '\'' +
               ", createTime=" + createTime +
               ", index=" + index +
               ", rowCountPerPage=" + rowCountPerPage +
               ", propertiesName='" + propertiesName + '\'' +
               ", propertiesImg='" + propertiesImg + '\'' +
               ", regionalManagementId='" + regionalManagementId + '\'' +
               ", phone='" + phone + '\'' +
               '}';
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMemberId() {
        return memberId;
    }

    public void setMemberId(String memberId) {
        this.memberId = memberId;
    }

    public String getRegionalPropertiesId() {
        return regionalPropertiesId;
    }

    public void setRegionalPropertiesId(String regionalPropertiesId) {
        this.regionalPropertiesId = regionalPropertiesId;
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

    public String getRegionalManagementId() {
        return regionalManagementId;
    }

    public void setRegionalManagementId(String regionalManagementId) {
        this.regionalManagementId = regionalManagementId;
    }
}
