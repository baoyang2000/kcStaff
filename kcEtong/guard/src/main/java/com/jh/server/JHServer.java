package com.jh.server;

import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.jh.activity.VideoScreenActivity;
import com.jh.constant.APPConstant;
import com.jhsdk.bean.sip.JHCallInfo;
import com.jhsdk.constant.JHConstant;
import com.jhsdk.core.JHSDKCore;
import com.jhsdk.core.callback.JHSDKListener;
import com.jhsdk.utils.SharedPreUtils;

/**
 * Created by Administrator on 2016/12/20.
 */

public class JHServer extends Service implements JHSDKListener.CallStateChangedListener{

    @Override
    public void onCreate() {
        super.onCreate();
        //初始化, 上下文最好使用整个app上下文
        JHSDKCore.init(getApplicationContext(), "bb9f088c90");
        JHSDKCore.addListener(this);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @SuppressWarnings("WrongConstant")
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        String account = SharedPreUtils.getStringValue(JHServer.this, APPConstant.KEY_USER_ACCOUNT, "");
        String password = SharedPreUtils.getStringValue(JHServer.this, APPConstant.KEY_USER_PASSWORD, "");
        //这里为了兼容老版本升级，数据不正确问题
        //boolean isLogin = SharedPreUtils.getBooleanValue(this, MyConfig.IS_SIGN_IN, false);
        JHSDKCore.getUserService().login(account, password);
        flags = START_STICKY;
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        JHSDKCore.removeListener(this);
        JHSDKCore.onDestroy();
    }


    @Override
    public void onCallStateChanged(JHCallInfo callInfo) {
        Intent mIntent = new Intent(getBaseContext(), VideoScreenActivity.class);
        mIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        int callState = callInfo.getCallState();
        if (callState == JHConstant.RECEIVE_VIDEO_REQUEST) {
            Bundle bundle = new Bundle();
            bundle.putSerializable("JHCallInfo", callInfo);
            mIntent.putExtras(bundle);
            getApplication().startActivity(mIntent);
        } else if (callState == JHConstant.INVITE_VIDEO_REQUEST) {
            Bundle bundle = new Bundle();
            bundle.putSerializable("JHCallInfo", callInfo);
            mIntent.putExtras(bundle);
            getApplication().startActivity(mIntent);
        }
    }
}
