package com.jh.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.jh.R;
import com.jh.utils.StringUtils;
import com.jh.view.MaterialDialog;
import com.jhsdk.core.JHSDKCore;
import com.jhsdk.core.callback.JHCallback;

/**
 * @author yangtao
 * @date 2016/12/27
 */

public class RegisterAccountActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText eTextAccount, eTextPassword, eTextAgainPassword;
    private Button buttonAccountClear, buttonPasswordEye, buttonPasswordClear,
            buttonAgainPasswordEye, buttonAgainPasswordClear, buttonRegister;

    private TextWatcher accountWatcher, passwordWatcher, againPasswordWatcher;
    private MaterialDialog alert;
    private ImageView      iv_register_back;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_account);
        initWatcher();
        eTextAccount = (EditText) findViewById(R.id.eTextAccount);
        eTextAccount.addTextChangedListener(accountWatcher);
        eTextPassword = (EditText) findViewById(R.id.eTextPassword);
        eTextPassword.addTextChangedListener(passwordWatcher);
        eTextAgainPassword = (EditText) findViewById(R.id.eTextAgainPassword);
        eTextAgainPassword.addTextChangedListener(againPasswordWatcher);

        buttonAccountClear = (Button) findViewById(R.id.buttonAccountClear);
        buttonAccountClear.setOnClickListener(this);
        buttonPasswordEye = (Button) findViewById(R.id.buttonPasswordEye);
        buttonPasswordEye.setOnClickListener(this);
        buttonPasswordClear = (Button) findViewById(R.id.buttonPasswordClear);
        buttonPasswordClear.setOnClickListener(this);
        buttonAgainPasswordEye = (Button) findViewById(R.id.buttonAgainPasswordEye);
        buttonAgainPasswordEye.setOnClickListener(this);
        buttonAgainPasswordClear = (Button) findViewById(R.id.buttonAgainPasswordClear);
        buttonAgainPasswordClear.setOnClickListener(this);
        buttonRegister = (Button) findViewById(R.id.buttonRegister);
        buttonRegister.setOnClickListener(this);
        iv_register_back = (ImageView) findViewById(R.id.iv_register_back);
        iv_register_back.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
//        switch (view.getId()) {
//
//            case R.id.buttonAccountClear:
//                eTextAccount.setText("");
//                break;
//            case R.id.buttonPasswordClear:
//                eTextPassword.setText("");
//                break;
//            case R.id.buttonAgainPasswordClear:
//                eTextAgainPassword.setText("");
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
//            case R.id.buttonAgainPasswordEye:
//                if (eTextAgainPassword.getInputType() == (InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD)) {
//                    buttonAgainPasswordEye.setSelected(true);
//                    eTextAgainPassword.setInputType(InputType.TYPE_CLASS_TEXT);
//                } else {
//                    buttonAgainPasswordEye.setSelected(false);
//                    eTextAgainPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
//                }
//                eTextAgainPassword.setSelection(eTextAgainPassword.getText().toString().length());
//                break;
//            case R.id.buttonRegister:
//                String phone = eTextAccount.getText().toString();
//                String password = eTextPassword.getText().toString();
//                String againPassword = eTextAgainPassword.getText().toString();
//
//                if (StringUtils.isEmpty(password) || StringUtils.isEmpty(againPassword)) {
//                    Toast.makeText(RegisterAccountActivity.this,
//                            "密码输入错误！"
//                            , Toast.LENGTH_SHORT).show();
//                    return;
//                } else if (!password.equals(againPassword)) {
//                    Toast.makeText(RegisterAccountActivity.this,
//                            "两次密码输入不一致！"
//                            , Toast.LENGTH_SHORT).show();
//                    return;
//                }
//                showDialog("正在注册帐号...");
//                JHSDKCore.getUserService().registeredAccount(phone, password, new JHCallback<String>() {
//                    @Override
//                    public void onSuccess(String s) {
//                        Toast.makeText(RegisterAccountActivity.this, "注册成功！", Toast.LENGTH_SHORT).show();
//                        finish();
//                    }
//
//                    @Override
//                    public void onFailure(int errorNo, String strMsg) {
//                        tips("注册提示", "注册失败: " + strMsg, false);
//                    }
//
//                    @Override
//                    public void onFinish() {
//                        clossDialog();
//                    }
//                });
//                break;
//        }
        if(view.getId() == R.id.buttonAccountClear){
            eTextAccount.setText("");
        }else if(view.getId() == R.id.buttonPasswordClear){
            eTextPassword.setText("");
        }else if(view.getId() == R.id.buttonAgainPasswordClear){
            eTextAgainPassword.setText("");
        }else if(view.getId() == R.id.buttonPasswordEye){
            if (eTextPassword.getInputType() == (InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD)) {
                buttonPasswordEye.setSelected(true);
                eTextPassword.setInputType(InputType.TYPE_CLASS_TEXT);
            } else {
                buttonPasswordEye.setSelected(false);
                eTextPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
            }
            eTextPassword.setSelection(eTextPassword.getText().toString().length());
        }else if(view.getId() == R.id.buttonAgainPasswordEye){
            if (eTextAgainPassword.getInputType() == (InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD)) {
                buttonAgainPasswordEye.setSelected(true);
                eTextAgainPassword.setInputType(InputType.TYPE_CLASS_TEXT);
            } else {
                buttonAgainPasswordEye.setSelected(false);
                eTextAgainPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
            }
            eTextAgainPassword.setSelection(eTextAgainPassword.getText().toString().length());
        }else if(view.getId() == R.id.buttonRegister){
            String phone = eTextAccount.getText().toString();
            String password = eTextPassword.getText().toString();
            String againPassword = eTextAgainPassword.getText().toString();

            if (StringUtils.isEmpty(password) || StringUtils.isEmpty(againPassword)) {
                Toast.makeText(RegisterAccountActivity.this,
                        "密码输入错误！"
                        , Toast.LENGTH_SHORT).show();
                return;
            } else if (!password.equals(againPassword)) {
                Toast.makeText(RegisterAccountActivity.this,
                        "两次密码输入不一致！"
                        , Toast.LENGTH_SHORT).show();
                return;
            }
            showDialog("正在注册帐号...");
            JHSDKCore.getUserService().registeredAccount(phone, password, new JHCallback<String>() {
                @Override
                public void onSuccess(String s) {
                    Toast.makeText(RegisterAccountActivity.this, "注册成功！", Toast.LENGTH_SHORT).show();
                    finish();
                }

                @Override
                public void onFailure(int errorNo, String strMsg) {
                    tips("注册提示", "注册失败: " + strMsg, false);
                }

                @Override
                public void onFinish() {
                    clossDialog();
                }
            });
        }else if(view.getId() == R.id.iv_register_back){
            RegisterAccountActivity.this.finish();
        }
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

        passwordWatcher = new TextWatcher() {
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void afterTextChanged(Editable s) {
                if (s.toString().length() > 0) {
                    eTextPassword.setVisibility(View.VISIBLE);
                } else {
                    eTextPassword.setVisibility(View.INVISIBLE);
                }
            }
        };

        againPasswordWatcher = new TextWatcher() {
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void afterTextChanged(Editable s) {
                if (s.toString().length() > 0) {
                    buttonAgainPasswordClear.setVisibility(View.VISIBLE);
                } else {
                    buttonAgainPasswordClear.setVisibility(View.INVISIBLE);
                }
            }
        };
    }

    private void showDialog(String text) {
        clossDialog();
        // 获取Dialog布局
        View view = LayoutInflater.from(RegisterAccountActivity.this).inflate(R.layout.dialog_loading_guard, null);
        TextView textView = (TextView) view.findViewById(R.id.tv_loading);
        textView.setText(text);
        alert = new MaterialDialog(RegisterAccountActivity.this)
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

    private void tips(String title, String text, final boolean isOk) {
        new MaterialDialog(RegisterAccountActivity.this)
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
