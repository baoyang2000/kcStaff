package com.ctrl.android.kcetong.model;

import java.io.Serializable;

/**
 * Created by liu on 2017/2/3.
 */

public class RegionList implements Serializable {

    /*"regionalManagementList": [{
        "id": "a4e6c3e46413484391955b4ef6a1b14c",
                "regionalName": "22222",
                "img": "http://og3hq2x84.bkt.clouddn.com/4c1b6505a798434db15da8375a3d3880.jpg",
                "createTime": 1484209494000,
                "remarks": "2222222222",
                "disabled": "0",
                "index": 0,
                "rowCountPerPage": 0
    }*/
    private String id;
    private String regionalName;
    private String img;
    private long createTime;
    private String remarks;
    private String disabled;
    private int index;
    private int rowCountPerPage;

    @Override
    public String toString() {
        return "RegionList{" +
               "id='" + id + '\'' +
               ", regionalName='" + regionalName + '\'' +
               ", img='" + img + '\'' +
               ", createTime=" + createTime +
               ", remarks='" + remarks + '\'' +
               ", disabled='" + disabled + '\'' +
               ", index=" + index +
               ", rowCountPerPage=" + rowCountPerPage +
               '}';
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getRegionalName() {
        return regionalName;
    }

    public void setRegionalName(String regionalName) {
        this.regionalName = regionalName;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getDisabled() {
        return disabled;
    }

    public void setDisabled(String disabled) {
        this.disabled = disabled;
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
