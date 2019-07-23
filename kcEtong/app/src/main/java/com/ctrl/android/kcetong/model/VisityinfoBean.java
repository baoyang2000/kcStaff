package com.ctrl.android.kcetong.model;

import java.io.Serializable;

/**
 * Created by liu on 2017/5/4.
 */

public class VisityinfoBean implements Serializable {
    private VisityinfoBean.DataBean data;
    private String code;

    public DataBean getData() {
        return data;
    }

    public void setData(VisityinfoBean.DataBean data) {
        this.data = data;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public static class DataBean {

        private Visit visitInfo;

        /*public List<Img> getPicList() {
            return picList;
        }

        public void setPicList(List<Img> picList) {
            this.picList = picList;
        }*/

        public Visit getVisitInfo() {
            return visitInfo;
        }

        public void setVisitInfo(Visit visitInfo) {
            this.visitInfo = visitInfo;
        }
    }
}
