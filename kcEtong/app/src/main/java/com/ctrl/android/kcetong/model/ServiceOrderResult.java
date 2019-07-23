package com.ctrl.android.kcetong.model;

import java.io.Serializable;

/**
 * Created by liu on 2017/1/19.
 */

public class ServiceOrderResult implements Serializable {
    private String code;
    private DataBean data;

    @Override
    public String toString() {
        return "ServiceOrderResult{" +
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
        private ServiceOrder serviceOrderInfo;

        @Override
        public String toString() {
            return "DataBean{" +
                   "serviceOrderInfo=" + serviceOrderInfo +
                   '}';
        }

        public ServiceOrder getServiceOrderInfo() {
            return serviceOrderInfo;
        }

        public void setServiceOrderInfo(ServiceOrder serviceOrderInfo) {
            this.serviceOrderInfo = serviceOrderInfo;
        }
    }
}
