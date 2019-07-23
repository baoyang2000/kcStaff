package com.ctrl.android.kcetong.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.baidu.location.BDLocation;
import com.ctrl.android.kcetong.R;
import com.ctrl.android.kcetong.listener.HintDialogListener;
import com.ctrl.android.kcetong.model.AppHolder;
import com.ctrl.android.kcetong.model.House;
import com.ctrl.android.kcetong.model.HouseBean;
import com.ctrl.android.kcetong.model.LoginBean;
import com.ctrl.android.kcetong.model.Proprietorinfo;
import com.ctrl.android.kcetong.toolkit.Url.Url;
import com.ctrl.android.kcetong.toolkit.util.AopUtils;
import com.ctrl.android.kcetong.toolkit.util.AppManager;
import com.ctrl.android.kcetong.toolkit.util.ConstantsData;
import com.ctrl.android.kcetong.toolkit.util.LLog;
import com.ctrl.android.kcetong.toolkit.util.S;
import com.ctrl.android.kcetong.toolkit.util.SharePrefUtil;
import com.ctrl.android.kcetong.toolkit.util.Utils;
import com.ctrl.android.kcetong.toolkit.webutils.BaseTSubscriber;
import com.ctrl.android.kcetong.toolkit.webutils.RetrofitUtil;
import com.ctrl.android.kcetong.ui.FloatWindow.FloatWindowService;
import com.ctrl.android.kcetong.ui.FloatWindow.MyWindowManager;
import com.ctrl.android.kcetong.ui.MaintabActivity;
import com.ctrl.android.kcetong.ui.base.BaseActivity;
import com.ctrl.android.kcetong.ui.dialog.HintMessageDialog;
import com.ctrl.third.common.library.utils.AnimUtil;

import org.json.JSONObject;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static com.ctrl.android.kcetong.R.id.password_text;
import static com.ctrl.android.kcetong.R.id.username_text;

public class LoginActivity extends BaseActivity {

    @BindView(username_text)
    EditText usernameText;
    @BindView(password_text)
    EditText passwordText;
    @BindView(R.id.login_btn)
    Button   loginBtn;
    @BindView(R.id.forget_password_btn)
    TextView forgetPasswordBtn;
    @BindView(R.id.regest_btn)
    TextView regestBtn;
    @BindView(R.id.visiter_btn)
    TextView visiterBtn;
    private String     userName;
    private String     passWord;
    private BDLocation location;

    @Override
    protected void initView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
    }

    @Override
    protected void initData() {
        File file = new File(Environment.getExternalStorageDirectory()
                             + "/cxh.apk");
        if(file.exists()){
            boolean dele = file.delete();
            if(dele){
                Log.d("--","删除安装包成功");
            }
        }
        if (application.getLocation() == null) {
            locationSetting();
        }

        userName = SharePrefUtil.getString(this, ConstantsData.USERNAME, "");
        passWord = SharePrefUtil.getString(this, ConstantsData.PASSWORD, "");

        if (!TextUtils.isEmpty(userName)) {
            usernameText.setText(userName);
        }

        if (!TextUtils.isEmpty(passWord)) {
            passwordText.setText(passWord);
        }

        //如果登录成功过，则自动登录
        if(!S.isNull(userName) && !S.isNull(passWord)){
            userLogin();
        }
    }

    @OnClick({R.id.login_btn, R.id.forget_password_btn, R.id.regest_btn, R.id.visiter_btn})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.login_btn:
                //6期
                /*location = application.getLocation();
                if(location != null){
                    if (checkInput() && checkLocationinfo(location)) {
                        userLogin();
                    }
                }else {
                    Utils.showShortToast(LoginActivity.this, "请打开定位权限");
                    //弹窗设置
                    new AlertDialog.Builder(this)
                            .setMessage("app需要开启权限才能使用此功能")
                            .setPositiveButton("设置", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                    intent.setData(Uri.parse("package:" + getPackageName()));
                                    startActivity(intent);
                                }
                            })
                            .setNegativeButton("取消", null)
                            .create()
                            .show();
                }*/
                if (checkInput()) {
                    userLogin();
                }
                break;
            case R.id.forget_password_btn:
                findPassword();
                break;
            case R.id.regest_btn:
                userRegister();
                break;
            case R.id.visiter_btn:
                //游客标识 0:正常用户; 1:游客
                //6期
                /*location = application.getLocation();
                if (checkLocationinfo(location)) {
                    AppHolder.getInstance().setVisiterFlg(1);
                    Intent intent = new Intent(LoginActivity.this, MaintabActivity.class);
                    startActivity(intent);
//                    requestInit2("");
                }else {
                    //弹窗设置
                    new AlertDialog.Builder(this)
                            .setMessage("app需要开启权限才能使用此功能")
                            .setPositiveButton("设置", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                    intent.setData(Uri.parse("package:" + getPackageName()));
                                    startActivity(intent);
                                }
                            })
                            .setNegativeButton("取消", null)
                            .create()
                            .show();
                }*/
                AppHolder.getInstance().setVisiterFlg(1);
                Intent intent = new Intent(LoginActivity.this, MaintabActivity.class);
                startActivity(intent);
                break;
        }
    }
    /*
      * 退出程序时两次点击的间隔时间
      */
//    private long waitTime  = 1000;
//    private long touchTime = 0;
//    /**
//    * 退出应用提示
//    */
//    @Override
//    public boolean onKeyDown(int keyCode, KeyEvent event) {
//        // 两次返回键，退出程序
//        if (event.getAction() == KeyEvent.ACTION_DOWN && KeyEvent.KEYCODE_BACK == keyCode) {
//            long currentTime = System.currentTimeMillis();
//            if ((currentTime - touchTime) >= waitTime) {
//                Toast.makeText(getApplicationContext(), "再按一次退应用", Toast.LENGTH_SHORT).show();
//                touchTime = currentTime;
//            } else {
//                AppManager.getAppManager().AppExit(this);
//                System.exit(0);
//            }
//            return true;
//        }
//        return super.onKeyDown(keyCode, event);
//    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (event.getAction() == KeyEvent.ACTION_DOWN && KeyEvent.KEYCODE_BACK == keyCode) {
            if (getIntent().getIntExtra("exit", -1) == 1) {
                AppManager.getAppManager().AppExit(this);
                System.exit(0);
            }
        }

        return super.onKeyDown(keyCode, event);
    }

    /**
     * 定位
     */
    private void locationSetting() {
        if (application.getLocation() != null) {
        } else {
            if (Utils.isNetWorkConnected(this)) {
                locationIfGranted();
            }
        }
    }

    @Override
    public void noticeChildLocationComplete(BDLocation location) {
        super.noticeChildLocationComplete(location);
        if (location != null) {

        }
    }


    /**
     * 用户登录
     */
    private void userLogin() {

        userName = usernameText.getText().toString();
        passWord = passwordText.getText().toString();
        showProgress(true);
        Map<String, String> map = new HashMap<>();
        map.putAll(ConstantsData.getSystemParams());
        map.put(ConstantsData.METHOD, Url.loginUrl);
        map.put("userName", userName);
        map.put("password", passWord);
        String sign = AopUtils.sign(map, ConstantsData.SECRET_VALUE);
        map.put("sign", sign);
        LLog.d("Login----" + map);
        map.remove(ConstantsData.METHOD);

        RetrofitUtil.Api().UserLogin(map).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new BaseTSubscriber<LoginBean>(this) {
            @Override
            public void onResponseCallback(JSONObject response, String resultCode) {
                LLog.d("onResponseCallback: " + response);
                showProgress(false);
            }

            @Override
            public void onNext(LoginBean loginBean) {
                super.onNext(loginBean);
                showProgress(false);
                if (TextUtils.equals("000", loginBean.getCode())) {
                    holder.setMemberInfo(loginBean.getData().getMemberInfo());
                    holder.setListReceiveAddress(loginBean.getData().getReceiveAddList());

                    SharePrefUtil.saveString(LoginActivity.this, ConstantsData.USERNAME, userName);
                    SharePrefUtil.saveString(LoginActivity.this, ConstantsData.PASSWORD, passWord);

                    AppHolder.getInstance().setVisiterFlg(0);
                    getHouseList(loginBean.getData().getMemberInfo().getMemberId());
//                    initUserCommunity(loginBean.getData().getMemberInfo().getMemberId());
                } else if (TextUtils.equals("018", loginBean.getCode())) {
                    Utils.toastError(LoginActivity.this, "用户名或者密码错误");
                }

            }

            @Override
            public void onNetError(Throwable e) {
                super.onNetError(e);
                showProgress(false);
            }
        });
    }

    /**
     * 注册
     */
    private void userRegister() {
        startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
        AnimUtil.intentSlidIn(this);
    }

    /**
     * 找回密码
     */
    private void findPassword() {
        startActivity(new Intent(LoginActivity.this, FindPasswordActivity.class));
        AnimUtil.intentSlidIn(this);
    }

    /**
     * 初始化个人社区信息
     */
    /*private void initUserCommunity(String memberId) {
        showProgress(true);
        String              provinceName = location.getProvince();
        String              cityName     = location.getCity();
        String              areaName     = location.getDistrict();
        double              longitude    = location.getLongitude();
        double              latitude     = location.getLatitude();
        Map<String, String> params       = new HashMap<>();
        params.putAll(ConstantsData.getSystemParams());
        params.put(ConstantsData.METHOD, Url.communityUrl);
        params.put("memberId", memberId);
        params.put("provinceName", provinceName);
        params.put("cityName", cityName);
        params.put("areaName", areaName);
        params.put("longitude", longitude + "");
        params.put("latitude", latitude + "");
        String sign = AopUtils.sign(params, ConstantsData.SECRET_VALUE);
        params.put("sign", sign);
        LLog.d(params + "");
        params.remove(ConstantsData.METHOD);

        RetrofitUtil.Api().userCommunity(params).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new BaseTSubscriber<InitResult>(this) {
            @Override
            public void onResponseCallback(JSONObject response, String resultCode) {
                showProgress(false);
            }

            @Override
            public void onNext(InitResult init) {
                super.onNext(init);
                showProgress(false);
                if (TextUtils.equals("000", init.getCode())) {

                    AppHolder.getInstance().setCommunity(init.getData().getCommunityInfo());

                    requestProprietorInfo(AppHolder.getInstance().getCommunity().getId(), AppHolder.getInstance().getMemberInfo().getMemberId());

                } else {
                    Utils.toastError(LoginActivity.this, "登录失败");
                }
            }

            @Override
            public void onNetError(Throwable e) {
                super.onNetError(e);
                showProgress(false);
            }
        });
    }*/

    private int    currentPage     = 1;
    private int    rowCountPerPage = 10;
    /**
     * 房屋列表
     */
    private void getHouseList(String memberId) {

        showProgress(true);
        Map<String, String> map = new HashMap();
        map.putAll(ConstantsData.getSystemParams());
        map.put(ConstantsData.METHOD, Url.houseListUrl);
        map.put("memberId", memberId);
        map.put("currentPage", currentPage + "");
        map.put("rowCountPerPage", rowCountPerPage + "");
        String sign = AopUtils.sign(map, ConstantsData.SECRET_VALUE);
        map.put("sign", sign);
        map.remove(ConstantsData.METHOD);
        RetrofitUtil.Api().houseList(map).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new BaseTSubscriber<HouseBean>(this) {
            @Override
            public void onResponseCallback(JSONObject response, String resultCode) {
            }

            @Override
            public void onNext(HouseBean houseBean) {
                super.onNext(houseBean);

                if (TextUtils.equals(houseBean.getCode(), "000")) {
                    List<House> list = houseBean.getData().getHousesList();
                    if(list != null && list.size() > 0){
                        for (House house : list) {
                            if (house.getIsDefault() == 1) {
                                AppHolder.getInstance().setHouse(house);
                                AppHolder.getInstance().getProprietor().setProprietorId(house.getProprietorId());
                                AppHolder.getInstance().getCommunity().setCommunityName(house.getCommunityName());
                                AppHolder.getInstance().getCommunity().setId(house.getCommunityId());
                            }
                        }
                    }

                    requestProprietorInfo(AppHolder.getInstance().getCommunity().getId(), AppHolder.getInstance().getMemberInfo().getMemberId());
                }else if (TextUtils.equals("002", houseBean.getCode())){
                    if("1".equals(AppHolder.getInstance().getMemberInfo().getSupers())
                       || "2".equals(AppHolder.getInstance().getMemberInfo().getSupers())){
                        Intent intent = new Intent(LoginActivity.this, MaintabActivity.class);
                        startActivity(intent);
                        finish();
                    }else {
                        final HintMessageDialog hintDialog = new HintMessageDialog(LoginActivity.this);
                        hintDialog.showHintDialog("提示", getString(R.string.not_found_owner_information_and_go_on), new HintDialogListener() {
                            @Override
                            public void submitListener() {
                                hintDialog.dismiss();
                                Intent intent = new Intent(LoginActivity.this, MaintabActivity.class);
                                startActivity(intent);
                                finish();
                            }

                            @Override
                            public void cancelListener() {
                                hintDialog.dismiss();
                            }
                        });
                    }

                }

                showProgress(false);
            }

            @Override
            public void onNetError(Throwable e) {
                super.onNetError(e);

                if (e.getMessage() == null) {

                } else if (e.getMessage().contains("Failed to connect") || e.getMessage().contains("failed to connect") || e.getMessage().contains("SocketTimeoutException")) {
                    Utils.toastError(LoginActivity.this, "服务器连接超时,请检查网络");
                } else if ("Invalid index 0, size is 0".equals(e.getMessage())) {//连接超时

                } else if (e.getMessage().contains("Unable to resolve host")) {//网络访问错误
                    Utils.toastError(LoginActivity.this, "网络请求错误");
                } else {
                    Utils.toastError(LoginActivity.this, "网络请求错误");
                }
                showProgress(false);
            }
        });
    }
    /**
     * 初始化  游客登录
     *
     * @param memberId 会员id
     */
    /*public void requestInit2(String memberId) {
//        showProgress(true);
        String              provinceName = location.getProvince();
        String              cityName     = location.getCity();
        String              areaName     = location.getDistrict();
        double              longitude    = location.getLongitude();
        double              latitude     = location.getLatitude();
        Map<String, String> params       = new HashMap<>();
        params.putAll(ConstantsData.getSystemParams());
        params.put(ConstantsData.METHOD, Url.communityUrl);

        params.put("memberId", memberId);
        params.put("provinceName", provinceName);
        params.put("cityName", cityName);
        params.put("areaName", areaName);
        params.put("longitude", longitude + "");
        params.put("latitude", latitude + "");

        String sign = AopUtils.sign(params, ConstantsData.SECRET_VALUE);
        params.put("sign", sign);
        LLog.d(params + "");
        params.remove(ConstantsData.METHOD);

        RetrofitUtil.Api().userCommunity(params).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new BaseTSubscriber<InitResult>(this) {
            @Override
            public void onResponseCallback(JSONObject response, String resultCode) {
                LLog.d(response + "");
            }

            @Override
            public void onNext(InitResult init) {
                super.onNext(init);
                if (TextUtils.equals("000", init.getCode())) {

                    AppHolder.getInstance().setCommunity(init.getData().getCommunityInfo());

                    Intent intent = new Intent(LoginActivity.this, MaintabActivity.class);
                    startActivity(intent);
                    finish();
                }
                showProgress(false);
            }

            @Override
            public void onNetError(Throwable e) {
                super.onNetError(e);
                showProgress(false);
            }
        });
    }*/

    /**
     * 获取业主认证信息
     *
     * @param communityId 社区ID
     * @param memberId 会员ID
     */
    private void requestProprietorInfo(String communityId, String memberId) {
        showProgress(true);
        Map<String, String> map = new HashMap<>();
        map.putAll(ConstantsData.getSystemParams());
        map.put(ConstantsData.METHOD, Url.ProprietorUrl);

        map.put("communityId", communityId);
        map.put("memberId", memberId);

        String sign = AopUtils.sign(map, ConstantsData.SECRET_VALUE);
        map.put("sign", sign);
        LLog.d("requestProprietorInfo----" + map);
        map.remove(ConstantsData.METHOD);
        RetrofitUtil.Api().userProprietor(map).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new BaseTSubscriber<Proprietorinfo>(this) {
            @Override
            public void onResponseCallback(JSONObject response, String resultCode) {
                LLog.d("onResponseCallback: " + response);
                showProgress(false);
            }

            @Override
            public void onNext(Proprietorinfo proprietorinfo) {
                super.onNext(proprietorinfo);
                showProgress(false);
                if (TextUtils.equals("000", proprietorinfo.getCode())) {
//                    nowCommunityName.setText(getResources().getString(R.string.current_plot) + ": " + S.getStr(AppHolder.getInstance().getCommunity().getCommunityName()));
                    AppHolder.getInstance().getProprietor().setProprietorId(proprietorinfo.getData().getHousesInfo().getProprietorId());
                    AppHolder.getInstance().setHouse(proprietorinfo.getData().getHousesInfo());


                    startActivity(new Intent(LoginActivity.this, MaintabActivity.class));
                    finish();

                } else if (TextUtils.equals("009", proprietorinfo.getCode())) {

                    final HintMessageDialog hintDialog = new HintMessageDialog(LoginActivity.this);
                    hintDialog.showHintDialog("提示", getString(R.string.not_found_owner_information_and_go_on), new HintDialogListener() {
                        @Override
                        public void submitListener() {
                            hintDialog.dismiss();
                            Intent intent = new Intent(LoginActivity.this, MaintabActivity.class);
                            startActivity(intent);
                            finish();
                        }

                        @Override
                        public void cancelListener() {
                            hintDialog.dismiss();
                        }
                    });
                }
            }

            @Override
            public void onNetError(Throwable e) {
                super.onNetError(e);
                showProgress(false);
            }
        });
    }

    /**
     * 检查用户名、密码是否为空
     *
     *
     */
    private boolean checkInput() {

        if (TextUtils.isEmpty(usernameText.getText().toString())) {
            Utils.toastError(this, getString(R.string.usename_null));
            return false;
        }

        if (TextUtils.isEmpty(passwordText.getText().toString())) {
            Utils.toastError(this, getString(R.string.pwd_null));
            return false;
        }

        return true;
    }

    /**
     * 检查定位城市、区县、经度、纬度、BDLocation
     */
    private boolean checkLocationinfo(BDLocation location) {
        if (location != null) {
            if (location.getCity() == null || location.getDistrict() == null || location.getProvince() == null || location.getLongitude() < 0.000001 || location.getLatitude() < 0.000001) {
                return false;
            } else {
                return true;
            }
        } else {
            return false;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if(Utils.isServiceWork(LoginActivity.this,"com.ctrl.android.property.ui.FloatWindow.FloatWindowService")){

            MyWindowManager.removeSmallWindow(LoginActivity.this);
            Intent intent = new Intent(LoginActivity.this, FloatWindowService.class);
            stopService(intent);
        }
        finish();
        AnimUtil.intentSlidOut(this);
    }
}
