package com.ctrl.android.kcetong.model;

import java.io.Serializable;
import java.util.List;

/**
 * Created by liu on 2017/1/18.
 */

public class ServiceListResult implements Serializable {
    private String code;
    private DataBean data;

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

    public static class DataBean{
        private List<ServiceKind> serviceKindList;

        @Override
        public String toString() {
            return "DataBean{" +
                   "serviceKindList=" + serviceKindList +
                   '}';
        }

        public List<ServiceKind> getServiceKindList() {
            return serviceKindList;
        }

        public void setServiceKindList(List<ServiceKind> serviceKindList) {
            this.serviceKindList = serviceKindList;
        }
    }
}
