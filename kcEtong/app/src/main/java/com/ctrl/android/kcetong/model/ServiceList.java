package com.ctrl.android.kcetong.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by liu on 2017/1/18.
 */

public class ServiceList implements Serializable {
    private String code;
    private DataBean data = new DataBean();

    @Override
    public String toString() {
        return "ServiceListResult{" +
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
        private List<ServiceKind> serviceList = new ArrayList<>();

        @Override
        public String toString() {
            return "DataBean{" +
                    "serviceKindList=" + serviceList +
                    '}';
        }

        public List<ServiceKind> getServiceKindList() {
            return serviceList;
        }

        public void setServiceKindList(List<ServiceKind> serviceKindList) {
            this.serviceList = serviceKindList;
        }
    }
}
