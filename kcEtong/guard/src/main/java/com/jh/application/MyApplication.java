package com.jh.application;


import android.app.Application;
import android.content.Context;


public class MyApplication extends Application {

    private static Context context;
    private static MyApplication instance;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        //获取Context
        context = getApplicationContext();
    }

    public synchronized static MyApplication instance() {
        if (instance != null)
            return instance;
        throw new RuntimeException("MyApplication not instantiated yet");
    }

    //返回 全局的Context
    public static Context getContextObject() {
        return context;
    }
}
