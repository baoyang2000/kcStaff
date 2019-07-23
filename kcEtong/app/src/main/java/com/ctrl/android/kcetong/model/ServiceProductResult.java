package com.ctrl.android.kcetong.model;

import java.io.Serializable;
import java.util.List;

/**
 * Created by liu on 2017/1/20.
 */

public class ServiceProductResult implements Serializable{
    private String code;
    private DataBean data;

    @Override
    public String toString() {
        return "ServiceProductResult{" +
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
        private List<ServiceProduct> serviceProductList;

        @Override
        public String toString() {
            return "DataBean{" +
                   "serviceProductList=" + serviceProductList +
                   '}';
        }

        public List<ServiceProduct> getServiceProductList() {
            return serviceProductList;
        }

        public void setServiceProductList(List<ServiceProduct> serviceProductList) {
            this.serviceProductList = serviceProductList;
        }
    }
}
