package com.ctrl.android.kcetong.model;

import java.io.Serializable;
import java.util.List;

/**
 * Created by liu on 2017/2/3.
 */

public class RegionDetailListResult implements Serializable {
    private String code;
    private DataBean data;

    @Override
    public String toString() {
        return "RegionDetailListResult{" +
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
        private List<RegionDetailList> regionalPropertiesList;

        @Override
        public String toString() {
            return "DataBean{" +
                   "regionalPropertiesList=" + regionalPropertiesList +
                   '}';
        }

        public List<RegionDetailList> getRegionalPropertiesList() {
            return regionalPropertiesList;
        }

        public void setRegionalPropertiesList(List<RegionDetailList> regionalPropertiesList) {
            this.regionalPropertiesList = regionalPropertiesList;
        }
    }
}
