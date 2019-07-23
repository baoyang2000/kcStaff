package com.ctrl.android.kcetong.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ctrl.android.kcetong.R;
import com.ctrl.android.kcetong.model.AppHolder;
import com.ctrl.android.kcetong.toolkit.util.ConstantsData;
import com.ctrl.android.kcetong.toolkit.util.LLog;
import com.ctrl.android.kcetong.toolkit.util.SharePrefUtil;
import com.ctrl.android.kcetong.toolkit.util.StrConstant;
import com.ctrl.android.kcetong.toolkit.util.Utils;
import com.ctrl.android.kcetong.ui.base.BaseActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MySettingActivity extends BaseActivity {

    @BindView(R.id.set_name_change_btn)//用户名修改
            RelativeLayout set_name_change_btn;
    @BindView(R.id.set_mobile_change_btn)//手机号修改
            RelativeLayout set_mobile_change_btn;
    @BindView(R.id.set_password_change_btn)//密码修改
            RelativeLayout set_password_change_btn;
    @BindView(R.id.logout_btn)//退出登录
            TextView       logout_btn;
    @BindView(R.id.cb_float_window)
    CheckBox cb_float_window;
    @BindView(R.id.tv_version)
    TextView tv_version;

    @Override
    protected void initView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_setting);
        ButterKnife.bind(this);
        toolbarBaseSetting(StrConstant.SETTING_TITLE, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MySettingActivity.this.finish();
            }
        });
        if(Utils.getFloatState(MySettingActivity.this)){
            cb_float_window.setChecked(true);
        }else {
            cb_float_window.setChecked(false);
        }
    }

    @Override
    protected void initData() {
        //0:正常用户; 1:游客
        if (AppHolder.getInstance().getVisiterFlg() == 1) {
            cb_float_window.setClickable(false);
            cb_float_window.setFocusable(false);
        } else if (AppHolder.getInstance().getVisiterFlg() == 0) {
            if("1".equals(AppHolder.getInstance().getMemberInfo().getSupers())
               || "2".equals(AppHolder.getInstance().getMemberInfo().getSupers())){
                cb_float_window.setClickable(false);
                cb_float_window.setFocusable(false);
            }else {
                cb_float_window.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if(isChecked){

                            Utils.saveFloatState(MySettingActivity.this, true);
                        }else {
                            Utils.saveFloatState(MySettingActivity.this, false);
                        }

                        LLog.i(Utils.getFloatState(MySettingActivity.this) +"");
                    }
                });
            }
        }

        if(Utils.getVersion(this) != null){
            tv_version.setText(Utils.getVersion(this));
        }else {
            tv_version.setText("未获取到版本号");
        }

    }

    @OnClick({R.id.set_name_change_btn, R.id.set_mobile_change_btn, R.id.set_password_change_btn, R.id.logout_btn})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.set_name_change_btn:
                //0:正常用户; 1:游客
                if (AppHolder.getInstance().getVisiterFlg() == 1) {
                    Utils.toastError(this,getString(R.string.visitor_want_to_register));
                } else if (AppHolder.getInstance().getVisiterFlg() == 0) {
                    Intent intent = new Intent(MySettingActivity.this, ChangeUsernameActivity.class);
                    startActivity(intent);
                }
                break;
            case R.id.set_mobile_change_btn:
                //0:正常用户; 1:游客
                if (AppHolder.getInstance().getVisiterFlg() == 1) {
                    Utils.toastError(this,getString(R.string.visitor_want_to_register));
                } else if (AppHolder.getInstance().getVisiterFlg() == 0) {
                    Intent intent = new Intent(MySettingActivity.this, ChangeMobileActivity.class);
                    startActivity(intent);
                }
                break;
            case R.id.set_password_change_btn:
                //0:正常用户; 1:游客
                if (AppHolder.getInstance().getVisiterFlg() == 1) {
                    Utils.toastError(this,getString(R.string.visitor_want_to_register));
                } else if (AppHolder.getInstance().getVisiterFlg() == 0) {
                    Intent intent = new Intent(MySettingActivity.this, ChangePasswordActivity.class);
                    startActivity(intent);
                }
                break;
            case R.id.logout_btn:
                SharePrefUtil.saveString(this, ConstantsData.USERNAME,"");
                SharePrefUtil.saveString(this, ConstantsData.PASSWORD,"");
                Intent intent1 = new Intent(this, LoginActivity.class);
                intent1.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent1.putExtra("exit",1);
                startActivity(intent1);
                finish();
                break;
        }
    }
}
