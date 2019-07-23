package com.ctrl.android.kcetong.model;

import java.io.Serializable;

/**
 * Created by zxs on 2017/1/4.
 */

public class InitResult implements Serializable {
    private String method;
    private String level;
    private String code;
    private Init   data;
    private String description;

    @Override
    public String toString() {
        return "InitResult{" +
               "method='" + method + '\'' +
               ", level='" + level + '\'' +
               ", code='" + code + '\'' +
               ", data=" + data +
               ", description='" + description + '\'' +
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

    public Init getData() {
        return data;
    }

    public void setData(Init data) {
        this.data = data;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
