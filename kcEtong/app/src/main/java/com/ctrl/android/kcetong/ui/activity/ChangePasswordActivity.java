package com.ctrl.android.kcetong.ui.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.ctrl.android.kcetong.R;
import com.ctrl.android.kcetong.listener.HintDialogListener;
import com.ctrl.android.kcetong.model.AppHolder;
import com.ctrl.android.kcetong.toolkit.util.AopUtils;
import com.ctrl.android.kcetong.toolkit.util.ConstantsData;
import com.ctrl.android.kcetong.toolkit.util.LLog;
import com.ctrl.android.kcetong.toolkit.util.MessageUtils;
import com.ctrl.android.kcetong.toolkit.util.S;
import com.ctrl.android.kcetong.toolkit.util.SharePrefUtil;
import com.ctrl.android.kcetong.toolkit.util.StrConstant;
import com.ctrl.android.kcetong.toolkit.webutils.BaseTSubscriber;
import com.ctrl.android.kcetong.toolkit.webutils.RetrofitUtil;
import com.ctrl.android.kcetong.ui.base.BaseActivity;
import com.ctrl.android.kcetong.ui.dialog.HintMessageDialog;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.ResponseBody;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class ChangePasswordActivity extends BaseActivity {
    @BindView(R.id.old_password)//旧密码
            EditText old_password;
    @BindView(R.id.new_password)//新密码
            EditText new_password;
    @BindView(R.id.comfirm_password)//密码确认
            EditText comfirm_password;

    @BindView(R.id.submit_btn)//提交按钮
            Button submit_btn;

    private String TITLE = StrConstant.MODIFY_PASSWORD_TITLE;

    @Override
    protected void initView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_change_password);
        ButterKnife.bind(this);
        toolbarBaseSetting(TITLE, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ChangePasswordActivity.this.finish();
            }
        });
    }

    @Override
    protected void initData() {

    }

    @OnClick(R.id.submit_btn)
    public void onClick() {
        if(checkInput()){
            String memberId = AppHolder.getInstance().getMemberInfo().getMemberId();
            String userName = "";
            String oldPassword = old_password.getText().toString();
            String password = new_password.getText().toString();
            String obtainType = "0";
            showProgress(true);
            requestChangePassword(memberId,userName,oldPassword,password,obtainType);
        }
    }
    /**
     * 登录 修改密码
     * @param memberId 会员ID
     * @param userName 用户名
     * @param oldPassword 原登录密码
     * @param password 登录密码
     * @param obtainType 修改类型（0：登录后修改、1：忘记密码时修改）
     * */
    public void requestChangePassword(String memberId,String userName,String oldPassword,
            String password,String obtainType){
        Map<String,String> map = new HashMap<>();
        map.put(ConstantsData.METHOD,"pm.ppt.members.modifyPassword");//方法名称
        map.putAll(ConstantsData.getSystemParams());

        map.put("memberId",memberId);
        map.put("userName",userName);
        map.put("oldPassword",oldPassword);
        map.put("password",password);
        map.put("obtainType",obtainType);

        String sign = AopUtils.sign(map, ConstantsData.SECRET_VALUE);
        map.put("sign",sign);
        LLog.d(map+"");
        map.remove(ConstantsData.METHOD);
        RetrofitUtil.Api().changePassword(map).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new BaseTSubscriber<ResponseBody>(this) {
                        @Override
                        public void onResponseCallback(JSONObject response, String resultCode) {
                            LLog.d(response+"");
                            showProgress(false);
                            try {
                                if(TextUtils.equals("000",response.getString("code"))){
                                    SharePrefUtil.saveString(ChangePasswordActivity.this,ConstantsData.PASSWORD,new_password.getText().toString());

                                    showDialog("恭喜您修改密码成功");
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

    private void showDialog(String info){
        final HintMessageDialog dialog = new HintMessageDialog(this);
        dialog.showDefaultDialogOneButton("提示", info, new HintDialogListener() {
            @Override
            public void submitListener() {
                dialog.dismiss();
            }
            @Override
            public void cancelListener() {
                dialog.dismiss();
                ChangePasswordActivity.this.finish();
            }
        },"返回",false);
    }
    /**
     * 检查输入内容格式
     * */
    private boolean checkInput(){


        if(S.isNull(old_password.getText().toString())){
            MessageUtils.showShortToast(this,"请输入旧密码");
            return false;
        }

        if(S.isNull(new_password.getText().toString())){
            MessageUtils.showShortToast(this,"请输入新密码");
            return false;
        }

        if(S.isNull(comfirm_password.getText().toString())){
            MessageUtils.showShortToast(this,"请第二次输入密码");
            return false;
        }

        if(!((new_password.getText().toString()).equals(comfirm_password.getText().toString()))){
            MessageUtils.showShortToast(this,"二次输入密码不一致");
            return false;
        }

        return true;
    }
}
