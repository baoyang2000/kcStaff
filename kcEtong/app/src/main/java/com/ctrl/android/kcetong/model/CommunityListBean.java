package com.ctrl.android.kcetong.model;

import java.util.List;

/**
 * Created by cxl on 2017/1/17.
 */

public class CommunityListBean {

    /**
     * method : pm.ppt.community.list
     * level : Info
     * code : 000
     * data : {"communityList":[{"id":"b550e266f01e4eb796d225d2de83a672","communityName":"济南市公安局","provinceCode":"370000","provinceName":"山东省","cityCode":"370100","cityName":"济南市","areaCode":"370102","areaName":"历下区","address":"山东省济南市旅游路17777号","adminId":"506cf194e92848e597bf64c377223682","latitude":"36.6552000000","longitude":"117.1246989771","disabled":"0","distance":0}]}
     */

    private String   method;
    private String   level;
    private String   code;
    private DataBean data;

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        private List<Community> communityList;

        public List<Community> getCommunityList() {
            return communityList;
        }

        public void setCommunityList(List<Community> communityList) {
            this.communityList = communityList;
        }
    }
}
