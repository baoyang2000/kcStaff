package com.ctrl.android.kcetong.model;

import java.io.Serializable;

/**
 * Created by liu on 2017/1/4.
 */

public class AuthCodeResult implements Serializable {
    private String method;
    private String level;
    private String code;
    private AuthCode data;

    @Override
    public String toString() {
        return "AuthCodeResult{" +
               "method='" + method + '\'' +
               ", level='" + level + '\'' +
               ", code='" + code + '\'' +
               ", data=" + data +
               '}';
    }

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

    public AuthCode getData() {
        return data;
    }

    public void setData(AuthCode data) {
        this.data = data;
    }
}
