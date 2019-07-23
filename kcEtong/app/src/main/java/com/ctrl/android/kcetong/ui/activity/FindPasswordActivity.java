package com.ctrl.android.kcetong.ui.activity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.design.widget.AppBarLayout;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ctrl.android.kcetong.R;
import com.ctrl.android.kcetong.model.AuthCodeResult;
import com.ctrl.android.kcetong.toolkit.Url.Url;
import com.ctrl.android.kcetong.toolkit.util.AopUtils;
import com.ctrl.android.kcetong.toolkit.util.ConstantsData;
import com.ctrl.android.kcetong.toolkit.util.LLog;
import com.ctrl.android.kcetong.toolkit.util.StrConstant;
import com.ctrl.android.kcetong.toolkit.util.Utils;
import com.ctrl.android.kcetong.toolkit.webutils.BaseTSubscriber;
import com.ctrl.android.kcetong.toolkit.webutils.RetrofitUtil;
import com.ctrl.android.kcetong.ui.base.BaseActivity;
import com.ctrl.android.kcetong.ui.dialog.AlertDialog;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.ResponseBody;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;


public class FindPasswordActivity extends BaseActivity implements View.OnClickListener{

    @BindView(R.id.toolbar_title)
    TextView     toolbar_title;
    @BindView(R.id.toolbar)
    Toolbar      toolbar;
    @BindView(R.id.appbar)
    AppBarLayout appbar;
    @BindView(R.id.username)
    EditText     username;
    @BindView(R.id.mobile)
    EditText     mobile;
    @BindView(R.id.vertify_code)
    EditText     vertify_code;
    @BindView(R.id.get_vertify_code_btn)
    TextView     get_vertify_code_btn;
    @BindView(R.id.password_layout)
    LinearLayout password_layout;
    @BindView(R.id.new_password)
    EditText     new_password;
    @BindView(R.id.comfirm_password)
    EditText     comfirm_password;
    @BindView(R.id.submit_btn)
    Button       submit_btn;

    private String code;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_password);
        ButterKnife.bind(this);
        initListener();
    }

    @Override
    protected void initView(Bundle savedInstanceState) {

    }

    @Override
    protected void initData() {

    }

    private void initListener(){
        toolbarBaseSetting(StrConstant.FIND_PASSWORD_TITLE, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FindPasswordActivity.this.finish();
            }
        });
        get_vertify_code_btn.setOnClickListener(this);
        submit_btn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.get_vertify_code_btn://获取验证码
                if(checkMobile()){
                    getVertifyCode(timer);
                }
                break;
            case R.id.submit_btn://提交
                if(checkInput()){
                    submit();
                }
                break;
        }
    }

    /**
     * 获取验证码
     */
    private void getVertifyCode(final CountDownTimer timer){
        showProgress(true);
        Map<String,String> params = new HashMap<>();
        params.put(ConstantsData.METHOD,Url.codeUrl);
        params.put("mobile",mobile.getText()+"");
        params.putAll(ConstantsData.getSystemParams());
        String sign = AopUtils.sign(params,ConstantsData.SECRET_VALUE);
        params.put("sign",sign);
        LLog.d("----vertifyCode---"+params);
        params.remove(ConstantsData.METHOD);
        RetrofitUtil.Api().getAuthCode(params).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new BaseTSubscriber<AuthCodeResult>(this) {
                        @Override
                        public void onResponseCallback(JSONObject response, String resultCode) {
                            showProgress(false);
                            LLog.d("onResponseCallback: " + response);
                        }

                        @Override
                        public void onNext(AuthCodeResult authCodeResult) {
                            showProgress(false);
                            super.onNext(authCodeResult);
                            if("000".equals(authCodeResult.getCode())){
                                Utils.toastError(FindPasswordActivity.this, "成功发送验证码");
                                if(timer!= null){
                                    timer.start();
                                }
                                //设置获取验证码按钮颜色和不能点击
                                get_vertify_code_btn.setTextColor(getResources().getColor(R.color.text_gray));
                                get_vertify_code_btn.setEnabled(false);

                                code = authCodeResult.getData().getAuthCode();
                            }
                        }

                        @Override
                        public void onNetError(Throwable e) {
                            showProgress(false);
                            super.onNetError(e);
                        }
                    });
    }
    /**
     * 提交
     */
    private void submit(){
        showProgress(true);
        Map<String,String> params = new HashMap<>();
        params.put(ConstantsData.METHOD, Url.findPasswordUrl);
        params.put("memberId","");
        params.put("userName",mobile.getText().toString());
        params.put("oldPassword","");
        params.put("password",new_password.getText().toString());
        params.put("obtainType","1");
        params.putAll(ConstantsData.getSystemParams());

        String sign = AopUtils.sign(params,ConstantsData.SECRET_VALUE);
        params.put("sign",sign);
        params.remove(ConstantsData.METHOD);
        LLog.d("----submit-----"+params);
        RetrofitUtil.Api().changePassword(params).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new BaseTSubscriber<ResponseBody>(this) {
                        @Override
                        public void onResponseCallback(JSONObject response, String resultCode) {
                            showProgress(false);
                            LLog.d("onResponseCallback: " + response);
                            try {
                                if("000".equals(response.getString("code"))){
                                    showAlertDialog();
                                }else {
                                    Utils.toastError(FindPasswordActivity.this,response.getString("description"));
                                    return;
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
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
     * 倒计时
     */
    private CountDownTimer timer = new CountDownTimer(60000, 1000) {

        @Override
        public void onTick(long millisUntilFinished) {
            get_vertify_code_btn.setText((millisUntilFinished / 1000) + "秒重新获取");
            get_vertify_code_btn.setEnabled(false);
            //btn_getCode.setBackgroundResource(R.drawable.register_againacquires);
//            get_vertify_code_btn.setBackgroundResource(R.drawable.status_btn_not_able);
            get_vertify_code_btn.setTextColor(getResources().getColor(R.color.text_gray));
        }

        @Override
        public void onFinish() {
            get_vertify_code_btn.setEnabled(true);
//            get_vertify_code_btn.setBackgroundResource(R.drawable.takeout_order_status_btn);
            get_vertify_code_btn.setText("获取验证码");
            get_vertify_code_btn.setTextColor(getResources().getColor(R.color.text_black));
        }
    };
    /**
     * 弹出自定义弹出框
     * */
    public void showAlertDialog() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancleFlg(false);
        builder.setMessage("恭喜您修改密码成功");
        builder.setReturnButton("返回",new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                finish();
            }
        });
        builder.create().show();

    }
    /**
     * 检查输入内容格式
     * */
    private boolean checkInput(){

        if(TextUtils.isEmpty(mobile.getText().toString())){
            Utils.toastError(this,"请输入手机号");
            return false;
        }

        if(TextUtils.isEmpty(vertify_code.getText().toString())){
            Utils.toastError(this,"请输入验证码");
            return false;
        }

        if(!((vertify_code.getText().toString()).equals(code))){
            Utils.toastError(this,"验证码错误");
            return false;
        }

        if(TextUtils.isEmpty(new_password.getText().toString())){
            Utils.toastError(this,"请输入新密码");
            return false;
        }

        if(TextUtils.isEmpty(comfirm_password.getText().toString())){
            Utils.toastError(this,"请第二次输入密码");
            return false;
        }

        if(!((new_password.getText().toString()).equals(comfirm_password.getText().toString()))){
            Utils.toastError(this,"二次输入密码不一致");
            return false;
        }

        return true;
    }
    /**
     * 检查手机号是否符合规定
     * @return
     */
    private boolean checkMobile(){
        String phoneNum = mobile.getText().toString().trim();
        if (TextUtils.isEmpty(phoneNum)) {
            Utils.toastError(FindPasswordActivity.this,R.string.mobile_not_empty);
            return false;
        } else if (!Utils.isMobileNO(phoneNum)) {
            Utils.toastError(FindPasswordActivity.this,R.string.mobile_wrong_format);
            return false;
        }
        return true;
    }
}
