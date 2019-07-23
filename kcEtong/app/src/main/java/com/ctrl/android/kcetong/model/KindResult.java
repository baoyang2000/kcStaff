package com.ctrl.android.kcetong.model;

import java.io.Serializable;
import java.util.List;

/**
 * Created by liu on 2017/1/22.
 */

public class KindResult implements Serializable {
    private String code;
    private DataBean data;

    @Override
    public String toString() {
        return "KindResult{" +
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
        private List<Kind> kindList;

        @Override
        public String toString() {
            return "DataBean{" +
                   "kindList=" + kindList +
                   '}';
        }

        public List<Kind> getKindList() {
            return kindList;
        }

        public void setKindList(List<Kind> kindList) {
            this.kindList = kindList;
        }
    }
}
