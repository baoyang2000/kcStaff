package com.jh.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.InputType;
import android.text.Selection;
import android.text.Spannable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.jh.R;
import com.jh.constant.APPConstant;
import com.jh.view.MaterialDialog;
import com.jhsdk.core.JHSDKCore;
import com.jhsdk.core.callback.JHSDKListener;
import com.jhsdk.utils.JHReturnCode;
import com.jhsdk.utils.SharedPreUtils;

import java.lang.ref.WeakReference;

/**
 * Created by Administrator on 2016/12/21.
 */

public class LoginActivity extends AppCompatActivity implements View.OnClickListener
        , JHSDKListener.RegistrStateChangedListener{
    private TextView tViewLoginInfo;
    private final int STATE_SIGN = 1;
    private MaterialDialog alert;
    private EditText       eTextUsername, eTextPassword;
    private Button buttonUsernameClear, buttonPasswordClear, buttonPasswordEye;
    private TextWatcher usernameWatcher;
    private TextWatcher passwordWatcher;
    private ImageView iv_login_back;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guard_login);
        JHSDKCore.addListener(this);
        eTextUsername = (EditText) findViewById(R.id.et_Username);
        eTextPassword = (EditText) findViewById(R.id.eTextPassword);
        buttonUsernameClear = (Button) findViewById(R.id.buttonUsernameClear);
        buttonPasswordClear = (Button) findViewById(R.id.buttonPasswordClear);
        buttonPasswordEye = (Button) findViewById(R.id.buttonPasswordEye);
        buttonUsernameClear.setOnClickListener(this);
        buttonPasswordClear.setOnClickListener(this);
        buttonPasswordEye.setOnClickListener(this);
        initWatcher();
        eTextUsername.addTextChangedListener(usernameWatcher);
        eTextPassword.addTextChangedListener(passwordWatcher);
        Button login = (Button) findViewById(R.id.login);
        login.setOnClickListener(this);

        tViewLoginInfo = (TextView) findViewById(R.id.tViewLoginInfo);
        TextView tViewReset = (TextView) findViewById(R.id.tViewReset);
        tViewReset.setOnClickListener(this);
        TextView tViewRetrieve = (TextView) findViewById(R.id.tViewRetrieve);
        tViewRetrieve.setOnClickListener(this);
        TextView tViewRegisteredAccount = (TextView) findViewById(R.id.tViewRegisteredAccount);
        tViewRegisteredAccount.setOnClickListener(this);
        iv_login_back = (ImageView) findViewById(R.id.iv_login_back);
        iv_login_back.setOnClickListener(this);
    }

//    @Override
//    public void onRegistrStateChanged(int code) {
//        Message message = new Message();
//        message.what = STATE_SIGN;
//        message.obj = code;
//        mhandler.sendMessage(message);
//    }

    private final MyHandler mhandler = new MyHandler(this);

    @Override
    public void onRegistrStateChanged(int i, String s) {
        Message message = new Message();
        message.what = STATE_SIGN;
        message.obj = i;
        mhandler.sendMessage(message);
    }

    @SuppressLint("HandlerLeak")
    private class MyHandler extends Handler {
        private final WeakReference<LoginActivity> mActivity;

        MyHandler(LoginActivity activity) {
            mActivity = new WeakReference<>(activity);
        }

        @SuppressLint("SetTextI18n")
        @Override
        public void handleMessage(Message msg) {
            LoginActivity activity = mActivity.get();
            if (activity != null) {
                switch (msg.what) {
                    case STATE_SIGN:
                        int code = (int) msg.obj;
                        if (code == 200) {
                            SharedPreUtils.put(LoginActivity.this, APPConstant.IS_LOGIN, true);
                            clossDialog();
                            //关闭软键盘
//                            InputMethodManager imm =  (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
//                            if(imm != null) {
//                                imm.hideSoftInputFromWindow(getWindow().getDecorView().getWindowToken(),
//                                        0);
//                            }
                            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                            imm.hideSoftInputFromWindow(eTextPassword.getWindowToken(), 0) ;
                            finish();
                        } else {
                            tViewLoginInfo.setTextColor(Color.parseColor("#FF0000"));
                            tViewLoginInfo.setText(JHReturnCode.showReqStatus(code,
                                    LoginActivity.this));
                            clossDialog();
                        }
                        break;
                    default:
                        break;
                }
            }
        }
    }

    @Override
    public void onClick(View view) {
        Intent intent;
        int id = view.getId();
//        switch (id) {
//            case R.id.buttonUsernameClear:
//                eTextUsername.setText("");
//                break;
//            case R.id.buttonPasswordClear:
//                eTextPassword.setText("");
//                break;
//            case R.id.buttonPasswordEye:
//                if (eTextPassword.getInputType() == (InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD)) {
//                    buttonPasswordEye.setSelected(true);
//                    eTextPassword.setInputType(InputType.TYPE_CLASS_TEXT);
//                } else {
//                    buttonPasswordEye.setSelected(false);
//                    eTextPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
//                }
//                eTextPassword.setSelection(eTextPassword.getText().toString().length());
//                break;
//            case R.id.login:
//                showDialog("正在登录...");
//                String account = eTextUsername.getText().toString();
//                String passward = eTextPassword.getText().toString();
//                SharedPreUtils.put(LoginActivity.this, APPConstant.KEY_USER_ACCOUNT, account);
//                SharedPreUtils.put(LoginActivity.this, APPConstant.KEY_USER_PASSWORD, passward);
//                JHSDKCore.getUserService().register(account, passward);
//                break;
//            case R.id.tViewReset:
//                /*intent = new Intent();
//                intent.setClass(this, ResetActivity.class);
//                intent.putExtra("password_key", 1);
//                startActivity(intent);*/
//                break;
//            case R.id.tViewRetrieve:
//                /*intent = new Intent();
//                intent.setClass(this, ResetActivity.class);
//                intent.putExtra("password_key", 0);
//                startActivity(intent);*/
//                break;
//            case R.id.tViewRegisteredAccount:
//                intent = new Intent();
//                intent.setClass(this, RegisterAccountActivity.class);
//                startActivity(intent);
//                break;
//        }
        if(id == R.id.buttonUsernameClear){
            eTextUsername.setText("");
        }else if(id == R.id.buttonPasswordClear){
            eTextPassword.setText("");
        }else if(id == R.id.buttonPasswordEye){
            if (eTextPassword.getInputType() == (InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD)) {
                buttonPasswordEye.setSelected(true);
                eTextPassword.setInputType(InputType.TYPE_CLASS_TEXT);
            } else {
                buttonPasswordEye.setSelected(false);
                eTextPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
            }
            eTextPassword.setSelection(eTextPassword.getText().toString().length());
        }else if(id == R.id.login){
            showDialog("正在登录...");
            String account = eTextUsername.getText().toString();

            String passward = eTextPassword.getText().toString();
//            String passward = "";
            SharedPreUtils.put(LoginActivity.this, APPConstant.KEY_USER_ACCOUNT, account);
            SharedPreUtils.put(LoginActivity.this, APPConstant.KEY_USER_PASSWORD, passward);
            JHSDKCore.getUserService().login(account, passward);
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(eTextPassword.getWindowToken(), 0) ;
        }else if(id == R.id.tViewRegisteredAccount){
            intent = new Intent();
            intent.setClass(this, RegisterAccountActivity.class);
            startActivity(intent);
        }else if(id == R.id.iv_login_back){
            LoginActivity.this.finish();
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(eTextPassword.getWindowToken(), 0) ;
        }else if(id == R.id.tViewReset){//修改密码
            intent = new Intent();
            intent.setClass(this, ResetActivity.class);
            intent.putExtra("password_key", 1);
            startActivity(intent);
        }else if(id == R.id.tViewRetrieve){//找回密码
            intent = new Intent();
            intent.setClass(this, ResetActivity.class);
            intent.putExtra("password_key", 0);
            startActivity(intent);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        String account = SharedPreUtils.getStringValue(this, APPConstant.KEY_USER_ACCOUNT, "");
        eTextUsername.setText(account);
        CharSequence text = eTextUsername.getText();
        if (text != null) {
            Spannable spanText = (Spannable) text;
            Selection.setSelection(spanText, text.length());
        }
    }

    @SuppressWarnings("ConstantConditions")
    @Override
    protected void onDestroy() {
        JHSDKCore.removeListener(this);
        super.onDestroy();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            finish();
        }
        return false;
    }

    private void showDialog(String text) {
        clossDialog();
        // 获取Dialog布局
        View view = LayoutInflater.from(LoginActivity.this).inflate(R.layout.dialog_loading_guard, null);
        TextView textView = (TextView) view.findViewById(R.id.tv_loading);
        textView.setText(text);
        alert = new MaterialDialog(LoginActivity.this)
                .setContentView(view);
        alert.setNegativeButton("取消", new MaterialDialog.DialogOnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });
        alert.show();
    }

    private void clossDialog() {
        if (alert != null && alert.isShow()) {
            alert.dismiss();
        }
    }

    private void initWatcher() {
        usernameWatcher = new TextWatcher() {
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void afterTextChanged(Editable s) {
                if (s.toString().length() > 0) {
                    buttonUsernameClear.setVisibility(View.VISIBLE);
                } else {
                    buttonUsernameClear.setVisibility(View.INVISIBLE);
                }
            }
        };

        passwordWatcher = new TextWatcher() {
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void afterTextChanged(Editable s) {
                if (s.toString().length() > 0) {
                    buttonPasswordClear.setVisibility(View.VISIBLE);
                } else {
                    buttonPasswordClear.setVisibility(View.INVISIBLE);
                }
            }
        };
    }
}
