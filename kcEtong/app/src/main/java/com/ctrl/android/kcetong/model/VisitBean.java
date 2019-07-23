package com.ctrl.android.kcetong.model;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2017/1/12.
 */

public class VisitBean  implements Serializable{

    /**
     * code : 000
     * data : {"communityList":
     * method : pm.ppt.community.list
     */

    private String                 code;
    private VisitBean.DataBean data;
    private String                 level;
    private String                 method;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public VisitBean.DataBean getData() {
        return data;
    }

    public void setData(VisitBean.DataBean data) {
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
        private List<Visit> communityVisitList;

        public List<Visit> getVisitList() {
            return communityVisitList;
        }

        public void setVisitList(List<Visit> communityVisitList) {
            this.communityVisitList = communityVisitList;
        }
    }
}
