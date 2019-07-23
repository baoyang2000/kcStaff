package com.ctrl.android.kcetong.model;

import java.io.Serializable;
import java.util.List;

/**
 * Created by liu on 2017/2/7.
 */

public class HouseEstateResult implements Serializable {
    private String code;
    private DataBean data;

    @Override
    public String toString() {
        return "HouseEstateResult{" +
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
        private List<HouseEstateInfo> homeEstateInfo;

        @Override
        public String toString() {
            return "DataBean{" +
                   "homeEstateInfo=" + homeEstateInfo +
                   '}';
        }

        public List<HouseEstateInfo> getHomeEstateInfo() {
            return homeEstateInfo;
        }

        public void setHomeEstateInfo(List<HouseEstateInfo> homeEstateInfo) {
            this.homeEstateInfo = homeEstateInfo;
        }
    }
}
