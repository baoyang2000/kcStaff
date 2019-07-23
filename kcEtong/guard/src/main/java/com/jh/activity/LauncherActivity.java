package com.jh.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;

import com.jh.R;
import com.jh.server.JHServer;
import com.jhsdk.permissions.PermissionsManager;
import com.jhsdk.permissions.PermissionsResultAction;

import java.lang.ref.WeakReference;

/**
 * Created by Administrator on 2016/12/20.
 */

public class LauncherActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launcher);
    }

    private Handler handler = new ServiceHandler(this);
    @SuppressLint("HandlerLeak")
    private class ServiceHandler extends Handler {
        WeakReference<LauncherActivity> s;

        public ServiceHandler(LauncherActivity launcherActivity) {
            s = new WeakReference<>(launcherActivity);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            LauncherActivity launcherActivity = s.get();
            if (launcherActivity != null) {
                Intent it = new Intent();
                it.setClass(LauncherActivity.this, JHServer.class);
                it.setPackage(getPackageName());
                //启动服务
                startService(it);

                it = new Intent();
                if(getIntent().getIntExtra("index", -1) == 0){
                    it.putExtra("do", "unlock");
                    it.setClass(LauncherActivity.this, UnlockingActivity.class);
                    //跳转到activity
                    startActivity(it);
                    LauncherActivity.this.finish();
                }else if(getIntent().getIntExtra("index", -1) == 1){
                    it.putExtra("do", "monitor");
                    it.setClass(LauncherActivity.this, UnlockingActivity.class);
                    //跳转到activity
                    startActivity(it);
                    LauncherActivity.this.finish();
                }else if(getIntent().getIntExtra("index", -1) == 2){
                    it.setClass(LauncherActivity.this, DialActivity.class);
                    //跳转到activity
                    startActivity(it);
                    LauncherActivity.this.finish();
                }
            }
        }
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        //初始化SD卡权限，摄像头权限
        PermissionsManager.getInstance().requestPermissionsIfNecessaryForResult(this,
                new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,
                             Manifest.permission.CAMERA}, new PermissionsResultAction() {
                    @Override
                    public void onGranted() {
                        handler.sendEmptyMessageDelayed(1, 200);
                    }

                    @Override
                    public void onDenied(String permission) {
                        handler.sendEmptyMessageDelayed(1, 200);
                    }
                }
        );
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
            @NonNull String[] permissions,
            @NonNull int[] grantResults) {
        PermissionsManager.getInstance().notifyPermissionsChange(permissions, grantResults);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return keyCode != KeyEvent.KEYCODE_BACK && super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
