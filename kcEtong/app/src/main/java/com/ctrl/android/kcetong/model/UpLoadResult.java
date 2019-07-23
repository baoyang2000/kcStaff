package com.ctrl.android.kcetong.model;

import java.io.Serializable;

/**
 * Created by liu on 2017/1/12.
 */

public class UpLoadResult implements Serializable{
    private String method;
    private String level;
    private String code;
    private DataBean data;

    @Override
    public String toString() {
        return "UpLoadResult{" +
               "method='" + method + '\'' +
               ", level='" + level + '\'' +
               ", code='" + code + '\'' +
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
        private Img2 img2;

        public Img2 getImg2() {
            return img2;
        }

        public void setImg2(Img2 img2) {
            this.img2 = img2;
        }
    }
}
