package com.ctrl.android.kcetong.model;

import java.io.Serializable;
import java.util.List;

/**
 * Created by liu on 2017/2/4.
 */

public class RegionFollowListResult implements Serializable {
    private String code;
    private DataBean data;

    @Override
    public String toString() {
        return "RegionFollowListResult{" +
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
        private List<RegionFollowList> regionalFollowList;

        @Override
        public String toString() {
            return "DataBean{" +
                   "regionalFollowList=" + regionalFollowList +
                   '}';
        }

        public List<RegionFollowList> getRegionalFollowList() {
            return regionalFollowList;
        }

        public void setRegionalFollowList(List<RegionFollowList> regionalFollowList) {
            this.regionalFollowList = regionalFollowList;
        }
    }
}
