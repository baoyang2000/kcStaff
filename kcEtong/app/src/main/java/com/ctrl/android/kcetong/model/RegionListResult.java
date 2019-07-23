package com.ctrl.android.kcetong.model;

import java.io.Serializable;
import java.util.List;

/**
 * Created by liu on 2017/2/3.
 */

public class RegionListResult implements Serializable {
    private String code;
    private DataBean data;

    @Override
    public String toString() {
        return "RegionListResult{" +
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

    public static class DataBean {
        private List<RegionList> regionalManagementList;

        @Override
        public String toString() {
            return "DataBean{" +
                   "regionalManagementList=" + regionalManagementList +
                   '}';
        }

        public List<RegionList> getRegionalManagementList() {
            return regionalManagementList;
        }

        public void setRegionalManagementList(List<RegionList> regionalManagementList) {
            this.regionalManagementList = regionalManagementList;
        }
    }
}
