package com.ctrl.android.kcetong.model;

/**
 * Created by liu on 2018/3/5.
 */

public class DefaultAddress {
    /**
     * method : pm.ppt.receiveAddress.getdefault
     * level : Info
     * code : 000
     * description : 查询成功！
     * data : {"dftRcvAddress":{"id":"b9a7133af46c4e25b63e4e822f3c278c","receiveAddress":"崇文区崇文区cghcvjnvccnvgnc！你给成功 vv 巨好yfihifgfuudg统筹兼顾价格从也不会低的情况和你联系吧、这些是你给你发个","mobile":"18853176543","provinceName":"山东省","cityName":"淄博市","areaName":"博山区","receiveName":"经常会遇到","index":0,"rowCountPerPage":0}}
     */

    private String method;
    private String   level;
    private String   code;
    private String   description;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * dftRcvAddress : {"id":"b9a7133af46c4e25b63e4e822f3c278c","receiveAddress":"崇文区崇文区cghcvjnvccnvgnc！你给成功 vv 巨好yfihifgfuudg统筹兼顾价格从也不会低的情况和你联系吧、这些是你给你发个","mobile":"18853176543","provinceName":"山东省","cityName":"淄博市","areaName":"博山区","receiveName":"经常会遇到","index":0,"rowCountPerPage":0}
         */

        private ReceiveAddress dftRcvAddress;

        public ReceiveAddress getDftRcvAddress() {
            return dftRcvAddress;
        }

        public void setDftRcvAddress(ReceiveAddress dftRcvAddress) {
            this.dftRcvAddress = dftRcvAddress;
        }
    }
}
