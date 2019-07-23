package com.ctrl.android.kcetong.model;

import java.io.Serializable;
import java.util.List;

/**
 * Created by liu on 2017/1/21.
 */

public class UserGoodsResult implements Serializable{
    private String code;
    private DataBean data;

    @Override
    public String toString() {
        return "UserGoodsResult{" +
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
        private List<UsedGoods> usedGoodList;

        @Override
        public String toString() {
            return "DataBean{" +
                   "usedGoodList=" + usedGoodList +
                   '}';
        }

        public List<UsedGoods> getUsedGoodList() {
            return usedGoodList;
        }

        public void setUsedGoodList(List<UsedGoods> usedGoodList) {
            this.usedGoodList = usedGoodList;
        }
    }
}
