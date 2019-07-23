package com.ctrl.android.kcetong.model;

import java.io.Serializable;

/**
 * Created by liu on 2017/1/16.
 */

public class BeforePropertyPayResult implements Serializable{
    private String code;
    private DataBean data;

    @Override
    public String toString() {
        return "BeforePropertyPayResult{" +
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
        private String uniteId;

        @Override
        public String toString() {
            return "DataBean{" +
                   "uniteId='" + uniteId + '\'' +
                   '}';
        }

        public String getUniteId() {
            return uniteId;
        }

        public void setUniteId(String uniteId) {
            this.uniteId = uniteId;
        }
    }
}
