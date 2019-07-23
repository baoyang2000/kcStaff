package com.ctrl.android.kcetong.model;

import java.util.List;

/**
 * Created by cxl on 2017/1/16.
 */

public class SuperCommunity {


    /**
     * method : pm.ppt.memberBind.list
     * level : Info
     * code : 000
     * data : {"housesList":[{"id":"203e542d7855411ebf8b52476bea673e","communityId":"8588212e1ea248c18723b8b7e12244a1","communityName":"邮电新村","memberId":"7091ebfbd949490eb7a078e8d2f197ac","proprietorId":"4b3e30948a194c23859e54626e9a8014","addressId":"aa93f8a6b5794621ac8bec59ac545a58","building":"1","unit":"1","room":"1907","isDefault":"1","createTime":1450679818000,"proprietorName":"胡志军","index":0,"rowCountPerPage":0,"isPayment":"1"}]}
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
        private List<House> communityList;

        public List<House> getHousesList() {
            return communityList;
        }

        public void setHousesList(List<House> housesList) {
            this.communityList = housesList;
        }

    }
}
