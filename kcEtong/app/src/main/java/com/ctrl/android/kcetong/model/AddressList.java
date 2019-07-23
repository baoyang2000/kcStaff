package com.ctrl.android.kcetong.model;

import java.io.Serializable;
import java.util.List;

/**
 * Created by liu on 2018/3/6.
 */

public class AddressList implements Serializable{
    /**
     * method : pm.ppt.receiveAddress.list
     * level : Info
     * code : 000
     * data : {"receiveAddressList":[{"areaName":"道里区","isDefault":"1","address":"道里区道里区道里区llvhkjvvjcvncg！我辜负对方虎岛和夫幸福和谐回复了可口可乐了22222","cityName":"哈尔滨市","receiveAddressId":"e05da46d73b54c99b4048ccf21e9d5c2","receiveName":"成功是","streetName":"","provinceName":"黑龙江省","mobile":"18256523568"},{"areaName":"博山区","isDefault":"0","address":"博山区崇文区崇文区cghcvjnvccnvgnc！你给成功 vv 巨好yfihifgfuudg统筹兼顾价格从也不会低的情况和你联系吧、这些是你给你发个","cityName":"淄博市","receiveAddressId":"b9a7133af46c4e25b63e4e822f3c278c","receiveName":"经常会遇到","streetName":"","provinceName":"山东省","mobile":"18853176543"}]}
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

    public static class DataBean implements Serializable{
        private List<ReceiveAddress> receiveAddressList;

        public List<ReceiveAddress> getReceiveAddressList() {
            return receiveAddressList;
        }

        public void setReceiveAddressList(List<ReceiveAddress> receiveAddressList) {
            this.receiveAddressList = receiveAddressList;
        }
    }
}
