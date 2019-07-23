package com.ctrl.android.kcetong.model;

/**
 * Created by liu on 2018/3/5.
 */

public class GenerateOrder {
    /**
     * method : pm.ppt.order.generateOrder
     * level : Info
     * code : 000
     * data : {"orderId":"86976e615d684b90829bb5299a398b06","totalPrice":11}
     */

    private String method;
    private String   level;
    private String   code;
    private DataBean data;

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
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
        /**
         * orderId : 86976e615d684b90829bb5299a398b06
         * totalPrice : 11.0
         */

        private String orderId;
        private double totalPrice;

        public String getOrderId() {
            return orderId;
        }

        public void setOrderId(String orderId) {
            this.orderId = orderId;
        }

        public double getTotalPrice() {
            return totalPrice;
        }

        public void setTotalPrice(double totalPrice) {
            this.totalPrice = totalPrice;
        }
    }
}
