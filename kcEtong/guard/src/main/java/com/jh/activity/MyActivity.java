package com.jh.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jh.R;
import com.jh.constant.APPConstant;
import com.jhsdk.bean.api.JHRole;
import com.jhsdk.constant.JHConstant;
import com.jhsdk.core.JHSDKCore;
import com.jhsdk.core.callback.JHSDKListener;
import com.jhsdk.utils.JHReturnCode;
import com.jhsdk.utils.SharedPreUtils;

public class MyActivity extends AppCompatActivity implements View.OnClickListener
        , JHSDKListener.RegistrStateChangedListener {
    private TextView tViewAccount, tViewCommName, tViewRegistrStatus;

    private ImageView iv_personal_back;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my);
        JHSDKCore.addListener(this);

        LinearLayout lLayoutUserInfo = (LinearLayout) findViewById(R.id.lLayoutUserInfo);
        lLayoutUserInfo.setOnClickListener(this);
        RelativeLayout rlayoutConfig = (RelativeLayout) findViewById(R.id.rlayoutConfig);
        rlayoutConfig.setOnClickListener(this);
        iv_personal_back = (ImageView) findViewById(R.id.iv_personal_back);
        iv_personal_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyActivity.this.finish();
            }
        });
        tViewAccount = (TextView) findViewById(R.id.tViewAccount);
        tViewCommName = (TextView) findViewById(R.id.tViewCommName);
        tViewRegistrStatus = (TextView) findViewById(R.id.tViewRegistrStatus);
    }

    @Override
    public void onResume() {
        super.onResume();
        JHSDKCore.addListener(this);
        String account = SharedPreUtils.getStringValue(MyActivity.this, JHConstant.KEY_USER_ACCOUNT, "");
        tViewAccount.setText(account);
        JHRole role = JHSDKCore.getUserService().getCurrentRole();
        if (role != null) {
            tViewCommName.setText(role.getCommName());
        }

        tViewRegistrStatus.setText(
                JHReturnCode.showReqStatus(JHSDKCore.getUserService().registerStateCode(), MyActivity.this));
    }

    @Override
    public void onClick(View view) {
        int    id = view.getId();
        Intent intent;
        if(id == R.id.lLayoutUserInfo){
            boolean isLogin = SharedPreUtils.getBooleanValue(MyActivity.this, APPConstant.IS_LOGIN, false);
            if (!isLogin) {
                intent = new Intent(MyActivity.this, com.jh.activity.LoginActivity.class);
                startActivity(intent);
            } else {
                intent = new Intent(MyActivity.this, UserInfoActivity.class);
                startActivity(intent);
            }
        }else if(id == R.id.rlayoutConfig){
            intent = new Intent(MyActivity.this, ConfigActivity.class);
            startActivity(intent);
        }
    }

//    @Override
//    public void setMenuVisibility(boolean menuVisible) {
//        super.setMenuVisibility(menuVisible);
//        View view = this.getView();
//        if (view != null) {
//            view.setVisibility(menuVisible ? View.VISIBLE : View.GONE);
//        }
//    }

    private int status;
    /**i : 401--帐号或密码错误
     200--注册成功
     701--注销成功
     */
    @Override
    public void onRegistrStateChanged(int i, String s) {
        JHSDKCore.addListener(this);
        Log.w("-----MyActivity-----", i + "--" + s);
//        Log.w("-----mommmmmmmmm--",JHReturnCode.showReqStatus(code, getActivity()));
        if(tViewRegistrStatus != null){
            tViewRegistrStatus.setText(JHReturnCode.showReqStatus(i, MyActivity.this));
        }
        if(i == 200){
            Log.w("-----MyActivity-----", "注册成功");
            status = i;
        }else if(i == 701){
            Log.w("-----MyActivity-----", "注销成功");
            status = i;
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            Intent intent = new Intent();
            intent.putExtra("status", status);
            setResult(RESULT_OK);
            finish();
        }
        return false;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
