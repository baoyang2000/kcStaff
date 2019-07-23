package com.ctrl.android.kcetong.model;

import java.io.Serializable;
import java.util.List;

/**
 * Created by liu on 2017/1/16.
 */

public class PropertyHisPayResult implements Serializable {
    private String code;
    private DataBean data;

    @Override
    public String toString() {
        return "PropertyHisPayResult{" +
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
        private List<PropertyHisPay> recordList;

        @Override
        public String toString() {
            return "DataBean{" +
                   "recordList=" + recordList +
                   '}';
        }

        public List<PropertyHisPay> getRecordList() {
            return recordList;
        }

        public void setRecordList(List<PropertyHisPay> recordList) {
            this.recordList = recordList;
        }
    }
}
