package com.ctrl.android.kcetong.ui.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RelativeLayout;

import com.ctrl.android.kcetong.R;
import com.ctrl.android.kcetong.toolkit.Url.Url;
import com.ctrl.android.kcetong.toolkit.util.AopUtils;
import com.ctrl.android.kcetong.toolkit.util.ConstantsData;
import com.ctrl.android.kcetong.toolkit.util.LLog;
import com.ctrl.android.kcetong.toolkit.util.StrConstant;
import com.ctrl.android.kcetong.toolkit.util.Utils;
import com.ctrl.android.kcetong.toolkit.webutils.BaseTSubscriber;
import com.ctrl.android.kcetong.toolkit.webutils.RetrofitUtil;
import com.ctrl.android.kcetong.ui.MaintabActivity;
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

public class RegisterActivity extends BaseActivity implements View.OnClickListener{

    @BindView(R.id.username)
    EditText       username;
    @BindView(R.id.password)
    EditText       password;
    @BindView(R.id.checkbox)
    CheckBox       checkbox;
    @BindView(R.id.password_layout)
    RelativeLayout password_layout;
    @BindView(R.id.registe_btn)
    Button         registe_btn;

    private String username_text;
    private String password_text;

    private String visiter_register ;//游客登录后进入注册页面
    @Override
    protected void initView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_register);
        ButterKnife.bind(this);
        initListener();
    }
    @Override
    protected void initData() {
        toolbarBaseSetting(StrConstant.REGESTE_TITLE, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RegisterActivity.this.finish();
            }
        });
        Intent in = getIntent();
        visiter_register = in.getStringExtra("visiter_register");
    }
    private void initListener(){


        registe_btn.setOnClickListener(this);
        password_layout.setOnClickListener(this);
        registe_btn.setClickable(false);
        checkbox.setOnClickListener(this);

        username.addTextChangedListener(new TextWatcher() {
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void afterTextChanged(Editable s) {
                if (s.length() == 0) {
                    registe_btn.setBackgroundResource(R.drawable.gray_bg_shap);
                    registe_btn.setClickable(false);
                } else {
                    if (!TextUtils.isEmpty(password.getText().toString())) {
                        registe_btn.setBackgroundResource(R.drawable.orange_bg_shap);
                        registe_btn.setClickable(true);
                    } else {
                        registe_btn.setBackgroundResource(R.drawable.gray_bg_shap);
                        registe_btn.setClickable(false);
                    }
                }
            }
        });

        password.addTextChangedListener(new TextWatcher() {
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void afterTextChanged(Editable s) {
                if (s.length() == 0) {
                    //checkbox.setChecked(false);
                    registe_btn.setBackgroundResource(R.drawable.gray_bg_shap);
                    registe_btn.setClickable(false);
                } else {

                    //if (password.getText().toString().length() < 6) {
                    //    checkbox.setChecked(false);
                    //} else {
                    //    checkbox.setChecked(true);
                    //}

                    if (!TextUtils.isEmpty(username.getText().toString())) {
                        registe_btn.setBackgroundResource(R.drawable.orange_bg_shap);
                        registe_btn.setClickable(true);
                    } else {
                        registe_btn.setBackgroundResource(R.drawable.gray_bg_shap);
                        registe_btn.setClickable(false);
                    }
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.password_layout:
                password.setFocusable(true);
                password.setFocusableInTouchMode(true);
                password.requestFocus();
                InputMethodManager imm = (InputMethodManager) password.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.toggleSoftInput(0, InputMethodManager.SHOW_FORCED);
                break;
            case R.id.registe_btn:
                if(checkInput()){
                    username_text = username.getText().toString();
                    password_text = password.getText().toString();
                    showProgress(true);
                    goRegister();
                }
                break;
            case R.id.checkbox:
                if(checkbox.isChecked()){
                    // 显示密码
                    password.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                } else {
                    // 隐藏密码
                    password.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                }
                break;
        }
    }
    private void goRegister(){
        Log.d("initListener---","fdsjfkdnf");
        Map<String,String> params = new HashMap<>();
        params.put(ConstantsData.METHOD, Url.registeUrl);
        params.put("userName", username_text);
        params.put("password", password_text);
        params.put("ip", "");

        params.putAll(ConstantsData.getSystemParams());
        String sign = AopUtils.sign(params,ConstantsData.SECRET_VALUE);
        params.put("sign",sign);
        LLog.d("----register-----" + params);
        params.remove(ConstantsData.METHOD);
        RetrofitUtil.Api().register(params).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new BaseTSubscriber<ResponseBody>(this) {
                        @Override
                        public void onResponseCallback(JSONObject response, String resultCode) {
                            showProgress(false);
                            LLog.d(response+"");
                            try {
                                if("000".equals(response.getString("code"))){
                                    showAlertDialog();
                                }else {
                                    Utils.toastError(RegisterActivity.this,response.getString("description"));
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
     * 检查输入内容格式
     * */
    private boolean checkInput(){
        if(TextUtils.isEmpty(username.getText().toString())){
            Utils.toastError(this,"邮箱或手机号不可为空");
            return false;
        }
        if(TextUtils.isEmpty(password.getText().toString())){
            Utils.toastError(this,"密码不可为空");
            return false;
        }

        if(Utils.getStr(password.getText().toString()).length() > 20 || Utils.getStr(password.getText().toString()).length() < 6){
            Utils.toastError(this,"请设置6-20位登录密码");
            return false;
        }
        return true;
    }

    /**
     * 弹出自定义弹出框
     * */
    public void showAlertDialog() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancleFlg(false);
        builder.setMessage("恭喜您注册成功");
        builder.setReturnButton("返回",new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                if("1".equals(visiter_register)){
                    Intent intent = new Intent(RegisterActivity.this, MaintabActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent.putExtra("selectFragmentIndex", 0);
                    startActivity(intent);
                }else{
                    RegisterActivity.this.finish();
                }
            }
        });
        builder.create().show();

    }
}