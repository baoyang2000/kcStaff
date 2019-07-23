package com.jh.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.jh.R;
import com.jh.utils.StringUtils;
import com.jh.view.MaterialDialog;
import com.jhsdk.core.JHSDKCore;
import com.jhsdk.core.callback.JHCallback;

/**
 * Created by Administrator on 2016/12/23.
 */

public class AuthorizedFamilyActivity extends AppCompatActivity implements View.OnClickListener {
    private TextView setting_authorize_name_text, setting_authorize_phone_text;
    private InputMethodManager imm;
    private MaterialDialog     alert;

    private ImageView iv_family_back;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authorized_family);
        imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);

        setting_authorize_name_text = (TextView) findViewById(R.id.setting_authorize_name_text);
        setting_authorize_phone_text = (TextView) findViewById(R.id.setting_authorize_phone_text);

        FrameLayout commit_btn = (FrameLayout) findViewById(R.id.commit_btn);
        commit_btn.setOnClickListener(this);
        RelativeLayout setting_authorize_name = (RelativeLayout) findViewById(R.id.setting_authorize_name);
        setting_authorize_name.setOnClickListener(this);
        RelativeLayout setting_authorize_phone = (RelativeLayout) findViewById(R.id.setting_authorize_phone);
        setting_authorize_phone.setOnClickListener(this);
        iv_family_back = (ImageView) findViewById(R.id.iv_family_back);
        iv_family_back.setOnClickListener(this);
    }

    private void authorizeSubmit() {
        String authorizeName, authorizePhone;
        authorizeName = setting_authorize_name_text.getText().toString();
        authorizePhone = setting_authorize_phone_text.getText().toString();
        if (StringUtils.isEmpty(authorizeName)) {
            Toast.makeText(AuthorizedFamilyActivity.this, "必须填写被授权人真实姓名！", Toast.LENGTH_SHORT).show();
            return;
        } else if (StringUtils.isEmpty(authorizePhone) && !StringUtils.isPhone(authorizePhone)) {
            Toast.makeText(AuthorizedFamilyActivity.this, "必须填写被授权人真实电话！", Toast.LENGTH_SHORT).show();
            return;
        }
        showDialog("正在授权...");
        JHSDKCore.getUserService().authorizeOwner(authorizeName, authorizePhone, new JHCallback<String>() {
            @Override
            public void onSuccess(String s) {
                Toast.makeText(AuthorizedFamilyActivity.this, s, Toast.LENGTH_SHORT).show();
                finish();
            }

            @Override
            public void onFailure(int errorNo, String strMsg) {
                tips("注册提示", "授权失败: " + strMsg, false);
            }

            @Override
            public void onFinish() {
                clossDialog();
            }
        });
    }

    private void dialogEditShow(final int id, String title) {
        final EditText editText = new EditText(AuthorizedFamilyActivity.this);
        if (id == R.id.rlayoutCommCode || id == R.id.setting_building_id || id == R.id.setting_unit_id
                || id == R.id.rLayoutRoomId || id == R.id.setting_authorize_phone) {
            editText.setInputType(InputType.TYPE_CLASS_NUMBER);
        }
        new MaterialDialog(AuthorizedFamilyActivity.this)
                .setTitle(title)
                .setContentView(editText)
                .setPositiveButton("确定", new MaterialDialog.DialogOnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String s;
                        s = editText.getText().toString();
                        if (id == R.id.setting_authorize_name) {
                            setting_authorize_name_text.setText(s);
                        } else if (id == R.id.setting_authorize_phone) {
                            setting_authorize_phone_text.setText(s);
                        }

                        imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
                    }
                })
                .setNegativeButton("取消", new MaterialDialog.DialogOnClickListener() {
                    @Override
                    public void onClick(View v) {
                    }
                }).show();
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
//        switch (id) {
//            case R.id.setting_authorize_name:
//                dialogEditShow(R.id.setting_authorize_name, "真实姓名");
//                break;
//            case R.id.setting_authorize_phone:
//                dialogEditShow(R.id.setting_authorize_phone, "被授权人电话号码");
//                break;
//            case R.id.commit_btn:
//                authorizeSubmit();
//                break;
//        }
        if(id == R.id.setting_authorize_name){
            dialogEditShow(R.id.setting_authorize_name, "真实姓名");
        }else if(id == R.id.setting_authorize_phone){
            dialogEditShow(R.id.setting_authorize_phone, "被授权人电话号码");
        }else if(id == R.id.commit_btn){
            authorizeSubmit();
        }else if(id == R.id.iv_family_back){
            AuthorizedFamilyActivity.this.finish();
        }
    }

    private void showDialog(String text) {
        clossDialog();
        // 获取Dialog布局
        View view = LayoutInflater.from(AuthorizedFamilyActivity.this).inflate(R.layout.dialog_loading_guard, null);
        TextView textView = (TextView) view.findViewById(R.id.tv_loading);
        textView.setText(text);
        alert = new MaterialDialog(AuthorizedFamilyActivity.this)
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
        new MaterialDialog(AuthorizedFamilyActivity.this)
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
