package com.ctrl.android.kcetong.ui.base;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.GeofenceClient;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.ctrl.android.kcetong.CustomApplication;
import com.ctrl.android.kcetong.R;
import com.ctrl.android.kcetong.model.AppHolder;
import com.ctrl.android.kcetong.toolkit.util.AppManager;
import com.ctrl.android.kcetong.toolkit.util.LLog;
import com.ctrl.android.kcetong.toolkit.util.Utils;
import com.ctrl.android.kcetong.ui.FloatWindow.FloatWindowService;
import com.ctrl.android.kcetong.ui.FloatWindow.MyWindowManager;
import com.ctrl.android.kcetong.ui.dialog.AlertDialog;
import com.ctrl.android.kcetong.ui.dialog.LoadingDialog;
import com.ctrl.android.kcetong.ui.dialog.ProgressHUD;
import com.ctrl.third.common.library.base.ToolBarActivity;
import com.umeng.analytics.MobclickAgent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import static com.ctrl.android.kcetong.toolkit.util.Utils.requestCameraPermissions;

/**
 * Created by Qiu on 2016/7/14.
 */
public abstract class BaseActivity extends ToolBarActivity {
    protected long lastRefreshTime;// 部分界面防止频繁多次下拉刷新
    protected final long refreshTimeSpan = 750;
    public String comingToCall = "";
    /* 百度定位 */
    public static LocationClient mLocationClient = null;
    public GeofenceClient mGeofenceClient;
    public MyLocationListenner myListener = null;
    protected Toolbar toolbar;
    protected TextView toolBarTitle;
    private LoadingDialog progressDialog;
    protected String TAG = "--BASE--";
    protected CustomApplication application;
    protected AppHolder holder;

    private ProgressHUD mProgressHUD;

    /**
     * 初始化
     */
    @SuppressLint("NewApi")
    public void initLocation() {
        myListener = new MyLocationListenner();
        mGeofenceClient = new GeofenceClient(getApplicationContext());
        mLocationClient = new LocationClient(getApplicationContext());
        LocationClientOption option = new LocationClientOption();
        option.setLocationMode(LocationClientOption.LocationMode.Battery_Saving);// 设置定位模式
        option.setCoorType("bd09ll");// 返回的定位结果是百度经纬度，默认值gcj02

        // option.setScanSpan(1000);// 设置发起定位请求的间隔时间为5000ms
        option.setIsNeedAddress(true);// 是否返回位置信息
//		option.setOpenGps(true);// 是否返回位置信息
        mLocationClient.setLocOption(option);
        mLocationClient.start();
        mLocationClient.registerLocationListener(myListener);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        application = (CustomApplication) getApplication();
        TAG = this.getClass().getSimpleName();
        EventBus.getDefault().register(this);
        manageActivity();
        holder = AppHolder.getInstance();

        initView(savedInstanceState);
        initData();
    }

    protected abstract void initView(Bundle savedInstanceState);

    protected abstract void initData();

    protected void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
    }

    protected void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }

    @Override
    protected void onUserLeaveHint() {
        Log.d("aeon", "onUserLeaveHint");
        super.onUserLeaveHint();
        if (Utils.getFloatState(BaseActivity.this)) {
            if (!Utils.isServiceWork(BaseActivity.this, "com.ctrl.android.property.ui.FloatWindow.FloatWindowService")) {
                Intent intent = new Intent(BaseActivity.this, FloatWindowService.class);
                startService(intent);
            }
        } else {

            if (Utils.isServiceWork(BaseActivity.this, "com.ctrl.android.property.ui.FloatWindow.FloatWindowService")) {
                MyWindowManager.removeSmallWindow(BaseActivity.this);
                Intent intent = new Intent(BaseActivity.this, FloatWindowService.class);
                stopService(intent);
            }
        }

    }

    /**
     * 弹出加载框
     */
    public void showProgressDialog() {
        if (progressDialog == null) progressDialog = new LoadingDialog(this);
        if (!progressDialog.isShowing()) {
            progressDialog.show();
        }
    }

    /**
     * 关闭加载等待dialog
     */
    public void dismissProgressDialog() {
        if (progressDialog == null) return;
        if (progressDialog.isShowing()) progressDialog.dismiss();
        progressDialog = null;
    }

    /**
     * 加载等待框（诚信行）
     *
     * @param show
     */
    public void showProgress(boolean show) {
        showProgressWithText(show, "加载中...");
    }

    public void showProgressWithText(boolean show, String message) {
        if (show) {
            mProgressHUD = ProgressHUD.show(this, message, true, true, null);
        } else {
            if (mProgressHUD != null) {
                mProgressHUD.dismiss();
                mProgressHUD = null;
            }
        }
    }

    public void startCallPhoneNumberActivity(String phoneNumber) {
        Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel: " + phoneNumber));
        comingToCall = phoneNumber;
        if (Utils.selfPermissionGranted(this, Manifest.permission.CALL_PHONE)) {
            // TODO: Consider calling
            startActivity(intent);
        } else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CALL_PHONE}, Utils.RequstCodeForCALL_PHONE);
            return;
        }
    }

    /**
     * 设置通用toolbar
     *
     * @param
     */
    protected void toolbarBaseSetting(String text, Toolbar.OnClickListener navigationListener) {
        toolbar = (Toolbar) findViewById(com.ctrl.android.kcetong.R.id.toolbar);
        toolBarTitle = (TextView) findViewById(com.ctrl.android.kcetong.R.id.toolbar_title);
        toolbar.setTitle("");
        toolbar.setSubtitle("");
        setSupportActionBar(toolbar);
        if (toolbar == null) {
            return;
        }
        toolbar.setNavigationIcon(com.ctrl.android.kcetong.R.drawable.classdetail_back);
        if (toolBarTitle != null) {
            toolBarTitle.setText(text);
        }
        if (navigationListener == null) {
            toolbar.setNavigationOnClickListener(new Toolbar.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });
        } else {
            toolbar.setNavigationOnClickListener(navigationListener);
        }
    }

    protected void toolbarBaseSetting(String text) {
        toolbarBaseSetting(text, null);
    }

    public void manageActivity() {
        AppManager.getAppManager().addActivity(this);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case Utils.RequstCodeForCALL_PHONE: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel: " + comingToCall));
                    if (Utils.selfPermissionGranted(BaseActivity.this, Manifest.permission.CALL_PHONE)) {
                        startActivity(intent);
                    }
                } else {
                    Utils.toastError(BaseActivity.this, "请在系统设置中允许诚信行拨打电话");
                }
            }
            break;
            case Utils.RequstCodeForPermissionLocation: {
                if (verifyPermissions(grantResults)) {
                    initLocation();
                } else {
                    noticeChildLocationComplete(null);
                }
            }
            break;
            case Utils.RequstCodeForPermissionStorage: {
                if (verifyPermissions(grantResults)) {
                    noticeChildStorageComplete();
                }
            }
            break;
            case requestCameraPermissions: {
                if (verifyPermissions(grantResults)) {
                    noticeChildCameraComplete();
                }
            }
            break;

        }
    }

    public static boolean verifyPermissions(int[] grantResults) {
        // At least one result must be checked.
        if (grantResults.length < 1) {
            return false;
        }

        // Verify that each required permission has been granted, otherwise return false.
        for (int result : grantResults) {
            if (result != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }

    /**
     * 开启定位监听(作权限验证)
     */
    public void locationIfGranted() {

        // Verify that all required contact permissions have been granted.
        if (!Utils.selfPermissionGranted(this, Manifest.permission.ACCESS_COARSE_LOCATION) || !Utils.selfPermissionGranted(this, Manifest.permission.ACCESS_FINE_LOCATION) || !Utils.selfPermissionGranted(this, Manifest.permission.READ_PHONE_STATE)) {
            // Contacts permissions have not been granted.
            requestLocationPermissions();

        } else {
            initLocation();
        }
    }


    /**
     * 申请文件读写权限
     */
    public void requestStoragePermissions() {
        if (!Utils.selfPermissionGranted(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) || !Utils.selfPermissionGranted(this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
            // Contacts permissions have not been granted.
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE,}, Utils.RequstCodeForPermissionStorage);

        } else {
            noticeChildStorageComplete();
        }
    }

    /**
     * 申请定位监听权限
     */
    public void requestLocationPermissions() {
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.READ_PHONE_STATE}, Utils.RequstCodeForPermissionLocation);
    }

//    /**
//     * 申请摄像头权限
//     */
//    public void requestCameraPermissions() {
//        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, requestCameraPermissions);
//    }

    /**
     * 申请摄像头权限
     */
    public void requestCameraPermissions() {
        // ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, Utils.requestCameraPermissions);
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED || !Utils.selfPermissionGranted(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            //申请权限，REQUEST_TAKE_PHOTO_PERMISSION是自定义的常量
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, requestCameraPermissions);
        } else {
            //有权限，直接拍照
            noticeChildCameraComplete();
        }
    }

    /**
     * 子类申请摄像头权限后处理
     */
    public void noticeChildCameraComplete() {

    }

    /**
     * 申请录音权限
     */
    public void requestRecorderPermissions(){
        if (!Utils.selfPermissionGranted(this, Manifest.permission.RECORD_AUDIO) || !Utils.selfPermissionGranted(this, Manifest.permission.MODIFY_AUDIO_SETTINGS)) {
            // Contacts permissions have not been granted.
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.RECORD_AUDIO, Manifest.permission.MODIFY_AUDIO_SETTINGS}, Utils.requestRecorderPermissions);

        } else {
            noticeChildRecorderComplete();
        }
    }

    public void noticeChildRecorderComplete(){

    }

    /**
     * 定位的回调监听
     */
    public class MyLocationListenner implements BDLocationListener {

        @Override
        public void onReceiveLocation(BDLocation location) {
            if (location == null) {
                noticeChildLocationComplete(null);
                return;
            }
/*            if (location.getLongitude() < 0.000001 || location.getLatitude() < 0.000001) {
                noticeChildLocationComplete(null);
                return;
            }
*/            double            longitude   = location.getLongitude();
            double            latitude    = location.getLatitude();
            CustomApplication application = (CustomApplication) BaseActivity.this.getApplication();
            application.setLocation(location);
            LLog.d("BaseActivity [longitude=" + longitude + "],[latitude=" + latitude + "],[address=" + location.getAddrStr() + "]");
            mLocationClient.stop();
            noticeChildLocationComplete(location);
        }
    }

    /**
     * 子类复写此方法，定位结果通知
     */
    public void noticeChildLocationComplete(BDLocation location) {

    }

    /**
     * 子类申请读写权限后处理
     */
    public void noticeChildStorageComplete() {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        showProgress(false);
        EventBus.getDefault().unregister(this);
        if (mLocationClient != null) {
            mLocationClient.stop();
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(Intent intent) {

    }

    /**
     * 提示对话框
     */
    protected void showSuccessDialog(String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancleFlg(false);
        builder.setMessage(message);
        builder.setReturnButton(getResources().getString(R.string.app_back), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                finish();
            }
        });
        builder.create().show();
    }

    /**
     * 跳到当前App的主页
     * 使用时再修改相关部分代码
     * */
//    protected void toHomePage(){
//        Intent intent = new Intent(BaseActivity.this, MainTabActivity.class);
//        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//        startActivity(intent);
//        AnimUtil.intentSlidOut(BaseActivity.this);
//    }
}
