package com.ctrl.android.kcetong.model;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2017/1/11.
 */

public class CommunituList implements Serializable{

    /**
     * code : 000
     * data : {"communityList":[{"address":"西客站腊山河西路与聊城路交汇处","adminId":"506cf194e92848e597bf64c377223682","areaCode":"370104","areaName":"槐荫区","cityCode":"370100","cityName":"济南市","communityName":"济水上苑售楼处","disabled":"0","distance":0,"id":"0611030a87e6403c829ffd24ed042289","latitude":"36.691617","longitude":"116.913051","provinceCode":"370000","provinceName":"山东省"},{"address":"济南高铁西客站片区顺安路与烟台路交汇处东北角","adminId":"506cf194e92848e597bf64c377223682","areaCode":"370104","areaName":"槐荫区","cityCode":"370100","cityName":"济南市","communityName":"西元大厦","disabled":"0","distance":0,"id":"2fa869a8c4424bbf925b934309ba099d","latitude":"36.6688580000","longitude":"116.8959830000","provinceCode":"370000","provinceName":"山东省"}]}
     * level : Info
     * method : pm.ppt.community.list
     */

    private String code;
    private DataBean data;
    private String   level;
    private String   method;

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

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
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
