package com.ctrl.android.kcetong.model;

import java.util.List;

/**
 * 市
 * Created by Eric on 2015/10/29.
 */
public class City {

    private String     id;//id
    private String     ptree;//id
    private String     name;//id
    private List<Area> areaList;//id

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPtree() {
        return ptree;
    }

    public void setPtree(String ptree) {
        this.ptree = ptree;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Area> getAreaList() {
        return areaList;
    }

    public void setAreaList(List<Area> areaList) {
        this.areaList = areaList;
    }
}
