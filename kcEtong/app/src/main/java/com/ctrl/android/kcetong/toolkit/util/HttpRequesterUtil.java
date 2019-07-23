package com.ctrl.android.kcetong.toolkit.util;

import android.util.Log;

import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Administrator on 2016/11/10.
 */

public class HttpRequesterUtil {

    public static final String USER_INFO_ACT_EDITNICKNAME="userInfo/1204";
    public static Object USER_INFO_ACT_EDITPHONENUM = "userInfo/1221";

    /**
     * 检查更新
     */
    public static RequestParams updateVersion(String appcode) {
        RequestParams params = new RequestParams();
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("{\"type\":\"").append(appcode).append("\"}");
        JSONObject obj = null;
        try {
            obj = new JSONObject(stringBuffer.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        params.put("data", obj);
        return params;
    }

    public static RequestParams editNickName(String id, String content) {
        return null;
    }

    public static RequestParams editPayPassword(String id, String oldPass, String newPass) {
        RequestParams params = new RequestParams();
        StringBuffer stringBuffer = new StringBuffer();

        stringBuffer.append("{\"userId\":\"").append(id)
                .append("\",\"oldPaypwd\":\"").append(oldPass)
                .append("\",\"paypwd\":\"").append(newPass).append("\"}");
        JSONObject obj = null;
        try {
            obj = new JSONObject(stringBuffer.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        params.put("data", obj);
        return params;
    }

    public static RequestParams findPayPass(String phoneNum, String newPass, String code) {
        RequestParams params = new RequestParams();
        StringBuffer buffer = new StringBuffer();
        buffer.append("{'mobile':'").append(phoneNum).append("','payPwd':'")
                .append(newPass).append("','ranCode':'").append(code)
                .append("'}");
        JSONObject obj = null;
        try {
            obj = new JSONObject(buffer.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        params.put("data", obj.toString());
        Log.e("支付密码找回", params.toString());
        return params;
    }

    public static RequestParams editPhoneNum(String id, String phone, String edCode) {
        RequestParams params = new RequestParams();
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("{\"userId\":\"").append(id)
                .append("\",\"mobile\":\"").append(phone)
                .append("\",\"ranCode\":\"").append(edCode).append("\"}");
        JSONObject obj = null;
        try {
            obj = new JSONObject(stringBuffer.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        params.put("data", obj);
        return params;
    }

    /**
     * 获得 【积分明细】RequestParams
     */
    public static RequestParams requestIntegralDetailList(String userId, int pageNo, int pageSize) {
        RequestParams params = new RequestParams();
        StringBuffer str = new StringBuffer("{\"userId\":\"");
        str.append(userId).append("\",\"pageNo\":\"").append(pageNo)
                .append("\",\"pageSize\":\"").append(pageSize).append("\"}");
        JSONObject obj = null;
        try {
            obj = new JSONObject(str.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        params.put("data", obj);
        return params;
    }

    public static RequestParams getOnlineRechargeParams(String version, String id, String type, double paycount) {
        RequestParams params = new RequestParams();
        StringBuffer stringBuffer = new StringBuffer();

        stringBuffer.append("{\"userId\":\"").append(id)
                .append("\",\"type\":\"").append(type)
                .append("\",\"overType\":\"").append(1)
                .append("\",\"version\":\"").append(version)
                .append("\",\"money\":\"").append(paycount).append("\"}");
        Log.d("【在线支付余额充值】", "====" + stringBuffer.toString());
        JSONObject obj = null;
        try {
            obj = new JSONObject(stringBuffer.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        params.put("data", obj);
        return params;
    }
}
