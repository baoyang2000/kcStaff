package com.ctrl.android.kcetong.ui.activity;

import android.os.Bundle;
import android.view.View;

import com.ctrl.android.kcetong.CustomApplication;
import com.ctrl.android.kcetong.R;
import com.ctrl.android.kcetong.toolkit.util.Utils;
import com.ctrl.android.kcetong.ui.base.BaseActivity;

public class ActivityRestart extends BaseActivity implements View.OnClickListener {

    @Override
    protected void initView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_restart);
        findViewById(R.id.reload_btn).setOnClickListener(this);
    }

    @Override
    protected void initData() {

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.reload_btn:
                if (Utils.isConn(this)) {
                    if (mLocationClient != null) {
                        mLocationClient.stop();
                    }
                    locationSetting();
                } else {
                    Utils.toastError(ActivityRestart.this, R.string.socket_time_out);
                }
                break;
        }

    }

    /**
     * 定位
     */
    private void locationSetting() {
        CustomApplication application = (CustomApplication) getApplication();
        if (application.getLocation() != null) {
        } else {
            if (Utils.isNetWorkConnected(this)) {
                locationIfGranted();
            }
        }
    }
}
