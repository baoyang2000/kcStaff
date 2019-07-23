package com.ctrl.android.kcetong.model;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/1/18.
 */

public class memberIcon implements Serializable {

    /**
     * code : 000
     * data : {"imgUrl":"http://115.28.243.3:8002/attached/headImg/img/2017/1/18/3341B61E-D821-4FF7-9F52-4EBD50AF3BD1.png"}
     * level : Info
     * method : pm.ppt.members.modifyHeadImg
     */

    private String code;
    private DataBean data;
    private String   level;
    private String   method;

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

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public static class DataBean {
        /**
         * imgUrl : http://115.28.243.3:8002/attached/headImg/img/2017/1/18/3341B61E-D821-4FF7-9F52-4EBD50AF3BD1.png
         */

        private String imgUrl;

        public String getImgUrl() {
            return imgUrl;
        }

        public void setImgUrl(String imgUrl) {
            this.imgUrl = imgUrl;
        }
    }
}
