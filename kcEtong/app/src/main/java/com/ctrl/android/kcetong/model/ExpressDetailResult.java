package com.ctrl.android.kcetong.model;

import java.io.Serializable;
import java.util.List;

/**
 * Created by liu on 2017/1/18.
 */

public class ExpressDetailResult implements Serializable{
    private String code;
    private DataBean data;

    @Override
    public String toString() {
        return "ExpressDetailResult{" +
               "code='" + code + '\'' +
               ", data=" + data +
               '}';
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

    public static class DataBean{
        private ExpressRecive expressInfo;
        private List<Img> expressPicList;

        @Override
        public String toString() {
            return "DataBean{" +
                   "expressInfo=" + expressInfo +
                   ", expressPicList=" + expressPicList +
                   '}';
        }

        public ExpressRecive getExpressInfo() {
            return expressInfo;
        }

        public void setExpressInfo(ExpressRecive expressInfo) {
            this.expressInfo = expressInfo;
        }

        public List<Img> getExpressPicList() {
            return expressPicList;
        }

        public void setExpressPicList(List<Img> expressPicList) {
            this.expressPicList = expressPicList;
        }
    }
}
