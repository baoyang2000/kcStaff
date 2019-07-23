package com.ctrl.android.kcetong.model;

import java.io.Serializable;
import java.util.List;

/**
 * Created by liu on 2017/1/22.
 */

public class UserGoodInfoResult implements Serializable {
    private String code;
    private DataBean data;

    @Override
    public String toString() {
        return "UserGoodInfoResult{" +
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
        private List<GoodPic> usedGoodPicList;
        private UsedGoodInfo usedGoodInfo;

        @Override
        public String toString() {
            return "DataBean{" +
                   "usedGoodPicList=" + usedGoodPicList +
                   ", usedGoodInfo=" + usedGoodInfo +
                   '}';
        }

        public List<GoodPic> getUsedGoodPicList() {
            return usedGoodPicList;
        }

        public void setUsedGoodPicList(List<GoodPic> usedGoodPicList) {
            this.usedGoodPicList = usedGoodPicList;
        }

        public UsedGoodInfo getUsedGoodInfo() {
            return usedGoodInfo;
        }

        public void setUsedGoodInfo(UsedGoodInfo usedGoodInfo) {
            this.usedGoodInfo = usedGoodInfo;
        }
    }
}
