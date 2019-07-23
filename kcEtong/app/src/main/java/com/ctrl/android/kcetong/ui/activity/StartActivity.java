package com.ctrl.android.kcetong.ui.activity;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;

import com.baidu.location.BDLocation;
import com.ctrl.android.kcetong.CustomApplication;
import com.ctrl.android.kcetong.R;
import com.ctrl.android.kcetong.listener.HintDialogListener;
import com.ctrl.android.kcetong.model.ApkVersionsBean;
import com.ctrl.android.kcetong.model.AppHolder;
import com.ctrl.android.kcetong.toolkit.Url.Url;
import com.ctrl.android.kcetong.toolkit.util.AopUtils;
import com.ctrl.android.kcetong.toolkit.util.ConstantsData;
import com.ctrl.android.kcetong.toolkit.util.LLog;
import com.ctrl.android.kcetong.toolkit.util.Utils;
import com.ctrl.android.kcetong.toolkit.webutils.BaseTSubscriber;
import com.ctrl.android.kcetong.toolkit.webutils.RetrofitUtil;
import com.ctrl.android.kcetong.ui.base.BaseActivity;
import com.ctrl.android.kcetong.ui.dialog.HintMessageDialog;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import pub.devrel.easypermissions.EasyPermissions;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Administrator on 2017/1/3.
 */

public class StartActivity extends BaseActivity implements EasyPermissions.PermissionCallbacks {

    private String[] mPerms;
    Handler x = new Handler();

    @Override
    protected void initView(Bundle savedInstanceState) {
        setContentView(R.layout.start_activity);
    }

    @Override
    protected void initData() {
        if (Utils.isConn(this)) {
            mPerms = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.READ_PHONE_STATE, Manifest.permission.CAMERA};

            if (EasyPermissions.hasPermissions(this, mPerms)) {
                x.postDelayed(new splashhandler(), 3000);
            } else {
                EasyPermissions.requestPermissions(this, "App正常运行需要存储权限、媒体权限", 1, mPerms);
            }
            // timer.start();
        } else {
            showHintDialog("无可用网络连接，查看网络设置？");
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    //成功
    @Override
    public void onPermissionsGranted(int requestCode, List<String> list) {
        x.postDelayed(new splashhandler(), 3000);
    }

    //失败
    @Override
    public void onPermissionsDenied(int requestCode, List<String> list) {

    }

    class splashhandler implements Runnable {
        public void run() {

            if (application.getLocation() != null) {
                AppHolder.getInstance().setBdLocation(application.getLocation());
                updateApkVersions();
            } else {
                locationSetting();
            }
        }
    }

    /**
     * 定位
     */
    private void locationSetting() {
        if (application.getLocation() != null) {
            x.postDelayed(new splashhandler(), 0);
        } else {
            if (Utils.isNetWorkConnected(this)) {
                locationIfGranted();
            }
        }
    }

    /**
     * 实现没有网络时的操作
     *
     * @param info
     */
    private void showHintDialog(String info) {

        if (Utils.ifCurrentActivityTopStack(StartActivity.this)) {
            final HintMessageDialog hintDialog = new HintMessageDialog(this);

            hintDialog.showHintDialog("提示", info, new HintDialogListener() {

                @Override
                public void submitListener() {
                    hintDialog.dismiss();
                    if (android.os.Build.VERSION.SDK_INT > 13) {// 3.2以上打开设置界面，也可以直接用ACTION_WIRELESS_SETTINGS打开到wifi界面

                        startActivity(new Intent(android.provider.Settings.ACTION_SETTINGS));
                    } else {
                        startActivity(new Intent(android.provider.Settings.ACTION_WIRELESS_SETTINGS));
                    }
                    StartActivity.this.finish();
                }

                @Override
                public void cancelListener() {
                    hintDialog.dismiss();
                    startActivity(new Intent(StartActivity.this, ActivityRestart.class));
                    StartActivity.this.finish();
                }
            });
        }
    }


    @Override
    public void noticeChildLocationComplete(BDLocation location) {
        super.noticeChildLocationComplete(location);
        CustomApplication application = (CustomApplication) getApplication();
        application.setLocation(location);
        LLog.d("------------定位到位置了-----------");

        if (location != null) {
            AppHolder.getInstance().setBdLocation(location);
        } else {
            AppHolder.getInstance().setBdLocation(new BDLocation());
        }
        x.postDelayed(new splashhandler(), 0);
    }

    /**
     * 获取apk版本信息
     */
    private void updateApkVersions() {

        Map<String, String> map = new HashMap<>();
        map.putAll(ConstantsData.getSystemParams());
        map.put(ConstantsData.METHOD, Url.updateApkVersions);
        map.put("clientType", "1");
        map.put("osType", "0");
        map.put("currentPage", "");
        map.put("rowCountPerPage", "");
        String sign = AopUtils.sign(map, ConstantsData.SECRET_VALUE);
        map.put("sign", sign);
        map.remove(ConstantsData.METHOD);

        RetrofitUtil.Api().updateApkVersions(map).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new BaseTSubscriber<ApkVersionsBean>(this) {
            @Override
            public void onResponseCallback(JSONObject response, String resultCode) {
                LLog.w(response.toString());
            }

            @Override
            public void onNext(ApkVersionsBean apkVersionsBean) {
                super.onNext(apkVersionsBean);
                if (TextUtils.equals(ConstantsData.success, apkVersionsBean.getCode())) {
                    LLog.w("apkVersionsBean.getData().getInfo() =" + apkVersionsBean.getData().getApkVersions());
                    AppHolder.getInstance().setVersionInfo(apkVersionsBean.getData().getApkVersions());

                    startActivity(new Intent(StartActivity.this, LoginActivity.class));
                    finish();
                }
            }

            @Override
            public void onNetError(Throwable e) {
                super.onNetError(e);
                showHintDialog("网络错误，查看网络设置？");
            }
        });
    }
}
