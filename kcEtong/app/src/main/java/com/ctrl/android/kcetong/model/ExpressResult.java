package com.ctrl.android.kcetong.model;

import java.io.Serializable;
import java.util.List;

/**
 * Created by liu on 2017/1/17.
 */

public class ExpressResult implements Serializable {
    private String code;
    private DataBean data;

    @Override
    public String toString() {
        return "ExpressResult{" +
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
        private List<Express> expressList;

        @Override
        public String toString() {
            return "DataBean{" +
                   "expressList=" + expressList +
                   '}';
        }

        public List<Express> getExpressList() {
            return expressList;
        }

        public void setExpressList(List<Express> expressList) {
            this.expressList = expressList;
        }
    }
}
