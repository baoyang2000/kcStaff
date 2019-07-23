package com.ctrl.android.kcetong.model;

import java.io.Serializable;
import java.util.List;

/**
 * Created by liu on 2017/1/19.
 */

public class ServiceOrderListResult implements Serializable {
    private String code;
    private DataBean data;

    @Override
    public String toString() {
        return "ServiceOrderListResult{" +
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
        private List<ServiceOrder> serviceOrderList;

        @Override
        public String toString() {
            return "DataBean{" +
                   "serviceOrderList=" + serviceOrderList +
                   '}';
        }

        public List<ServiceOrder> getServiceOrderList() {
            return serviceOrderList;
        }

        public void setServiceOrderList(List<ServiceOrder> serviceOrderList) {
            this.serviceOrderList = serviceOrderList;
        }
    }
}
