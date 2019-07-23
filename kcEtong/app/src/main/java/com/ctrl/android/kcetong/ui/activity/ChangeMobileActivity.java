package com.ctrl.android.kcetong.ui.activity;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.ctrl.android.kcetong.R;
import com.ctrl.android.kcetong.listener.HintDialogListener;
import com.ctrl.android.kcetong.model.AppHolder;
import com.ctrl.android.kcetong.model.AuthCodeResult;
import com.ctrl.android.kcetong.toolkit.util.AopUtils;
import com.ctrl.android.kcetong.toolkit.util.ConstantsData;
import com.ctrl.android.kcetong.toolkit.util.LLog;
import com.ctrl.android.kcetong.toolkit.util.MessageUtils;
import com.ctrl.android.kcetong.toolkit.util.S;
import com.ctrl.android.kcetong.toolkit.util.StrConstant;
import com.ctrl.android.kcetong.toolkit.util.Utils;
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

public class ChangeMobileActivity extends BaseActivity {
    @BindView(R.id.old_mobile)//旧手机号
            EditText old_mobile;
    @BindView(R.id.new_mobile)//新手机号
            EditText new_mobile;
    @BindView(R.id.vertify_code)//验证码
            EditText vertify_code;
    @BindView(R.id.get_vertify_code_btn)//获取验证码
            TextView get_vertify_code_btn;

    @BindView(R.id.submit_btn)//提交按钮
            Button submit_btn;
    private String TITLE = StrConstant.MODIFY_MOBILE_TITLE;

    private String code;
    @Override
    protected void initView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_change_mobile);
        ButterKnife.bind(this);
        toolbarBaseSetting(TITLE, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ChangeMobileActivity.this.finish();
            }
        });
    }

    @Override
    protected void initData() {

    }

    @OnClick({R.id.get_vertify_code_btn, R.id.submit_btn})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.get_vertify_code_btn:
                if(new_mobile.getText().toString() == null || new_mobile.getText().toString().equals("")){
                    MessageUtils.showShortToast(this,"新手机号不可为空");
                } else if(new_mobile.getText().toString().length() != 11){
                    MessageUtils.showShortToast(this,"新手机号格式不正确");
                } else {
                    showProgress(true);

                    getAuthCode(new_mobile.getText().toString(),timer);
                }
                break;
            case R.id.submit_btn:
                if(checkInput()){

                    if(code.equals(vertify_code.getText().toString())){
                        String memberId = AppHolder.getInstance().getMemberInfo().getMemberId();
                        String oldMobile = old_mobile.getText().toString();
                        String mobile = new_mobile.getText().toString();
                        String nickName = "";
                        showProgress(true);
                        requestModifyMemberInfo(memberId,oldMobile,mobile,nickName);
                    } else {
                        MessageUtils.showShortToast(this,"验证码输入错误");
                    }


                }
                break;
        }
    }
    /**
     * 获取验证码
     * @param mobile 手机号
     * */
    public void getAuthCode(String mobile,final CountDownTimer timer){
        Map<String,String> map = new HashMap<>();
        map.put(ConstantsData.METHOD,"pm.ppt.messageRecord.getAuthCode");//方法名称
        map.putAll(ConstantsData.getSystemParams());

        map.put("mobile",mobile);

        String sign = AopUtils.sign(map, ConstantsData.SECRET_VALUE);
        map.put("sign",sign);
        LLog.d(map+"");
        map.remove(ConstantsData.METHOD);
        RetrofitUtil.Api().getAuthCode(map).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new BaseTSubscriber<AuthCodeResult>(this) {
                        @Override
                        public void onResponseCallback(JSONObject response, String resultCode) {
                            LLog.d(response+"");
                            showProgress(false);
                        }

                        @Override
                        public void onNext(AuthCodeResult authCodeResult) {
                            super.onNext(authCodeResult);
                            showProgress(false);
                            if("000".equals(authCodeResult.getCode())){
                                Utils.toastError(ChangeMobileActivity.this, "成功发送验证码");
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
                            super.onNetError(e);
                            showProgress(false);
                        }
                    });
    }
    /**
     * 修改用户信息
     * @param memberId 用户ID
     * @param oldMobile 旧手机号码
     * @param mobile （新）手机号码
     * @param nickName 昵称（用户名）
     * */
    public void requestModifyMemberInfo(String memberId,String oldMobile
            ,String mobile,String nickName){
        Map<String,String> map = new HashMap<>();
        map.put(ConstantsData.METHOD,"pm.ppt.members.modify");//方法名称
        map.putAll(ConstantsData.getSystemParams());

        map.put("memberId",memberId);
        map.put("oldMobile",oldMobile);
        map.put("mobile",mobile);
        map.put("nickName",nickName);

        String sign = AopUtils.sign(map, ConstantsData.SECRET_VALUE);
        map.put("sign",sign);
        LLog.d(map+"");
        map.remove(ConstantsData.METHOD);
        RetrofitUtil.Api().requestModifyMemberInfo(map).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new BaseTSubscriber<ResponseBody>(this) {
                        @Override
                        public void onResponseCallback(JSONObject response, String resultCode) {
                            LLog.d(response+"");
                            showProgress(false);
                            try {
                                if(TextUtils.equals("000",response.getString("code"))){
                                    showDialog("恭喜您修改手机号成功");
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
     * 检查输入内容格式
     * */
    private boolean checkInput(){

        if(S.isNull(old_mobile.getText().toString())){
            MessageUtils.showShortToast(this,"请输入旧手机号");
            return false;
        }

        if(old_mobile.getText().toString().length() != 11){
            MessageUtils.showShortToast(this,"旧手机号格式不正确");
            return false;
        }

        if(S.isNull(new_mobile.getText().toString())){
            MessageUtils.showShortToast(this,"请输入手机号");
            return false;
        }

        if(new_mobile.getText().toString().length() != 11){
            MessageUtils.showShortToast(this,"新手机号格式不正确");
            return false;
        }

        if(S.isNull(vertify_code.getText().toString())){
            MessageUtils.showShortToast(this,"请输入验证码");
            return false;
        }

        if(!((vertify_code.getText().toString()).equals(code))){
            MessageUtils.showShortToast(this,"验证码错误");
            return false;
        }

        return true;
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
                ChangeMobileActivity.this.finish();
            }
        },"返回",false);
    }
}
