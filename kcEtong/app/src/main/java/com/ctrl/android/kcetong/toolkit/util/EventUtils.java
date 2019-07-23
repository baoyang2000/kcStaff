package com.ctrl.android.kcetong.toolkit.util;

import android.content.Intent;

import org.greenrobot.eventbus.EventBus;

/**
 * Created by Administrator on 2016/12/12.
 * 发送事件（使用方法，类似于广播）
 */

public class EventUtils {

    //登录成功，发送事件，进行数据同步
    public static void sendEventLogin(){
        Intent login = new Intent();
        login.setAction(ConstantsData.LOGIN_SUCCESS);
        EventBus.getDefault().post(login);
    }

    //退出登录，发送事件，进行数据同步
    public static void sendEventLogout(){
        Intent login = new Intent();
        login.setAction(ConstantsData.LOGOUT_SUCCESS);
        EventBus.getDefault().post(login);
    }

    //当金额发生改变的时候，发送事件，在个人信息里同步信息
    public static void sentEventAsyncBalance(){
        Intent balance = new Intent();
        balance.setAction(ConstantsData.BALANCE_CHANGED);
        EventBus.getDefault().post(balance);
    }

    //申请退款
    public static void sentEventTuiKuan(){
        Intent balance = new Intent();
        balance.setAction(ConstantsData.TUIKUAN);
        EventBus.getDefault().post(balance);
    }

}
