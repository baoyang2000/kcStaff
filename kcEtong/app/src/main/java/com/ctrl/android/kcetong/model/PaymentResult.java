package com.ctrl.android.kcetong.model;

import java.io.Serializable;

/**
 * Created by liu on 2017/1/16.
 */

public class PaymentResult implements Serializable{
    private String code;
    private DataBean data;

    @Override
    public String toString() {
        return "PaymentResult{" +
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
        private double debt;

        @Override
        public String toString() {
            return "DataBean{" +
                   "debt=" + debt +
                   '}';
        }

        public double getDebt() {
            return debt;
        }

        public void setDebt(double debt) {
            this.debt = debt;
        }
    }
}
