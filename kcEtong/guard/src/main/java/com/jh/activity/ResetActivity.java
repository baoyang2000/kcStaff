package com.jh.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.jh.R;
import com.jh.view.MaterialDialog;
import com.jhsdk.core.JHSDKCore;
import com.jhsdk.core.callback.JHCallback;

public class ResetActivity extends AppCompatActivity implements View.OnClickListener{

    private ImageView      ivRegisterBack;
    private TextView       tvTitle;
    private EditText       eTextAccount;
    private Button         buttonAccountClear;
    private EditText       eTextOldPassword;
    private RelativeLayout layoutOldPassword;
    private EditText       eTextPassword;
    private EditText       eTextAgainPassword;
    private Button         btnConfirm;
    private TextWatcher accountWatcher ;

    private int password_key = -1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset);
        initWatcher();
        initView();

    }

    private void initView() {
        ivRegisterBack = (ImageView) findViewById(R.id.iv_register_back);

        tvTitle = (TextView) findViewById(R.id.tv_title);
        eTextAccount = (EditText) findViewById(R.id.eTextAccount);
        eTextAccount.addTextChangedListener(accountWatcher);

        buttonAccountClear = (Button) findViewById(R.id.buttonAccountClear);
        layoutOldPassword = (RelativeLayout) findViewById(R.id.layout_old_password);
        eTextOldPassword = (EditText) findViewById(R.id.eTextOldPassword);

        eTextPassword = (EditText) findViewById(R.id.eTextPassword);

        eTextAgainPassword = (EditText) findViewById(R.id.eTextAgainPassword);
        btnConfirm = (Button) findViewById(R.id.btn_confirm);

        password_key = getIntent().getIntExtra("password_key", -1);
        if(password_key != -1){
            if(password_key == 0){//找回密码
                layoutOldPassword.setVisibility(View.GONE);
                tvTitle.setText("找回密码");
            }else if(password_key == 1){
                tvTitle.setText("修改密码");
            }
        }

        buttonAccountClear.setOnClickListener(this);
        btnConfirm.setOnClickListener(this);
        ivRegisterBack.setOnClickListener(this);
    }

    private void initWatcher() {
        accountWatcher = new TextWatcher() {
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void afterTextChanged(Editable s) {
                if (s.toString().length() > 0) {
                    buttonAccountClear.setVisibility(View.VISIBLE);
                } else {
                    buttonAccountClear.setVisibility(View.INVISIBLE);
                }
            }
        };
    }


    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.buttonAccountClear){
            eTextAccount.setText("");
        }else if(v.getId() == R.id.btn_confirm){
//            String account = (String) SharedPreUtils.get(ResetActivity.this, "APPConstant.KEY_USER_ACCOUNT", "");
//            String oldPassword = (String) SharedPreUtils.get(ResetActivity.this, "APPConstant.KEY_USER_PASSWORD", "");
            if(password_key == 0){//找回密码
                if(checkInput()){


                    JHSDKCore.getUserService().retrievePassword(eTextAccount.getText().toString(), eTextPassword.getText().toString(), new JHCallback<String>() {
                        @Override
                        public void onSuccess(String s) {
                            Toast.makeText(ResetActivity.this,"找回密码成功", Toast.LENGTH_SHORT ).show();
                            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                            imm.hideSoftInputFromWindow(eTextPassword.getWindowToken(), 0) ;
                            ResetActivity.this.finish();
                        }
                        @Override
                        public void onFailure(int i, String s) {
                            tips("找回密码提示", "找回密码失败: " + s, false);
//                            Toast.makeText(ResetActivity.this,"找回密码失败", Toast.LENGTH_SHORT ).show();
                        }
                        @Override
                        public void onFinish() {

                        }
                    });
                }
            }else if(password_key == 1){
                if(checkInput()){
                    JHSDKCore.getUserService().modifyPassword(eTextAccount.getText().toString(), eTextOldPassword.getText().toString(), eTextPassword.getText().toString(), new JHCallback<String>() {
                        @Override
                        public void onSuccess(String s) {
                            Toast.makeText(ResetActivity.this,"修改密码成功", Toast.LENGTH_SHORT ).show();
                            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                            imm.hideSoftInputFromWindow(eTextPassword.getWindowToken(), 0) ;
                            ResetActivity.this.finish();
                        }

                        @Override
                        public void onFailure(int i, String s) {
                            tips("修改密码提示", "修改密码失败: " + s, false);
                        }

                        @Override
                        public void onFinish() {

                        }
                    });
                }

            }
        }else if(v.getId() == R.id.iv_register_back){
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(eTextPassword.getWindowToken(), 0) ;
            ResetActivity.this.finish();
        }
    }

    private boolean checkInput(){
        if("".equals(eTextAccount.getText().toString())){
            Toast.makeText(this,"请输入手机号", Toast.LENGTH_SHORT ).show();
            return false;
        }

        if(password_key == 1){
            if("".equals(eTextOldPassword.getText().toString())){
                Toast.makeText(this,"请输入原密码", Toast.LENGTH_SHORT ).show();
                return false;
            }
        }
        if("".equals(eTextPassword.getText().toString())){
            Toast.makeText(this,"请输入新密码", Toast.LENGTH_SHORT ).show();
            return false;
        }
        if("".equals(eTextAgainPassword.getText().toString())){
            Toast.makeText(this,"请再次输入新密码", Toast.LENGTH_SHORT ).show();
            return false;
        }
        if(!eTextPassword.getText().toString().equals(eTextAgainPassword.getText().toString())){
            Toast.makeText(this,"两次密码输入不一致！", Toast.LENGTH_SHORT ).show();
            return false;
        }else {
            return true;
        }

    }
    private void tips(String title, String text, final boolean isOk) {
        new MaterialDialog(ResetActivity.this)
                .setTitle(title)
                .setMessage(text)
                .setPositiveButton("确定", new MaterialDialog.DialogOnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (isOk) {
                            finish();
                        }
                    }
                })
                .setNegativeButton("取消", new MaterialDialog.DialogOnClickListener() {
                    @Override
                    public void onClick(View v) {
                    }
                }).show();
    }
}
