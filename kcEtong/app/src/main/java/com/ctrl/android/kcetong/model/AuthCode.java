package com.ctrl.android.kcetong.model;

import java.io.Serializable;

/**
 * Created by liu on 2017/1/4.
 */

public class AuthCode implements Serializable{
    private String authCode;

    @Override
    public String toString() {
        return "AuthCode{" +
               "authCode='" + authCode + '\'' +
               '}';
    }

    public String getAuthCode() {
        return authCode;
    }

    public void setAuthCode(String authCode) {
        this.authCode = authCode;
    }
}
