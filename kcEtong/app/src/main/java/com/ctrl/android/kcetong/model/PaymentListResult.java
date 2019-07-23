package com.ctrl.android.kcetong.model;

import java.io.Serializable;
import java.util.List;

/**
 * Created by liu on 2017/1/16.
 */

public class PaymentListResult implements Serializable {
    private String code;
    private DataBean data;

    @Override
    public String toString() {
        return "PaymentListResult{" +
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
        private List<PropertyPay> paymentList;

        @Override
        public String toString() {
            return "DataBean{" +
                   "paymentList=" + paymentList +
                   '}';
        }

        public List<PropertyPay> getPaymentList() {
            return paymentList;
        }

        public void setPaymentList(List<PropertyPay> paymentList) {
            this.paymentList = paymentList;
        }
    }
}
