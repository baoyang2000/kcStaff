package com.jh.activity;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.jh.R;
import com.jh.utils.StringUtils;
import com.jh.view.T9TelephoneDialpadView;
import com.jhsdk.core.JHSDKCore;
import com.jhsdk.core.callback.JHCallback;

public class DialActivity extends AppCompatActivity implements  T9TelephoneDialpadView.OnT9TelephoneDialpadView {
    private EditText eTextDialPlate;

    private ImageView iv_call_back;

    @Override
    protected void onCreate( @Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dial);
        eTextDialPlate = (EditText) findViewById(R.id.eTextDialPlate);
        iv_call_back = (ImageView) findViewById(R.id.iv_call_back);
        iv_call_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialActivity.this.finish();
            }
        });
        T9TelephoneDialpadView t9TelephoneDialpadLayout = (T9TelephoneDialpadView) findViewById(R.id.t9TelephoneDialpadLayout);
        t9TelephoneDialpadLayout.setOnT9TelephoneDialpadView(this, eTextDialPlate);
    }

    @Override
    public void onCall() {
        String account = eTextDialPlate.getText().toString();
        if (!StringUtils.isEmail(account)) {
            JHSDKCore.getCallService().makeCall(account);
        }
    }

    @Override
    public void addContacts() {
        JHSDKCore.getCallService().callManager(new JHCallback<Object>() {
            @Override
            public void onFailure(int i, String s) {
                Log.e("onFailure", "失败：" + s);
                Toast.makeText(DialActivity.this, s, Toast.LENGTH_SHORT).show();
            }
        });
    }

//    @Override
//    public void setMenuVisibility(boolean menuVisible) {
//        super.setMenuVisibility(menuVisible);
//        View view = this.getView();
//        if (view != null) {
//            view.setVisibility(menuVisible ? View.VISIBLE : View.GONE);
//        }
//    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            finish();
        }
        return false;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}