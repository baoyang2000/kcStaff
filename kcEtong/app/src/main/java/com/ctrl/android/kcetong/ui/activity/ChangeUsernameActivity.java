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

public class ChangeUsernameActivity extends BaseActivity {
    @BindView(R.id.username_text)//用户名
            EditText username_text;

    @BindView(R.id.submit_btn)//提交按钮
            Button submit_btn;

    private String TITLE = StrConstant.MODIFY_USERNAME_TITLE;

    @Override
    protected void initView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_change_username);
        ButterKnife.bind(this);
        toolbarBaseSetting(TITLE, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ChangeUsernameActivity.this.finish();
            }
        });
    }

    @Override
    protected void initData() {
        username_text.setText(AppHolder.getInstance().getMemberInfo().getNickName());

    }

    @OnClick(R.id.submit_btn)
    public void onClick() {
        if(checkInput()){
            String memberId = AppHolder.getInstance().getMemberInfo().getMemberId();
            String oldMobile = "";
            String mobile = "";
            String nickName = username_text.getText().toString();
            showProgress(true);
            requestModifyMemberInfo(memberId,oldMobile,mobile,nickName);
        }
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
                                    AppHolder.getInstance().getMemberInfo().setNickName(username_text.getText().toString());
                                    showDialog("恭喜您修改用户名成功");
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
                ChangeUsernameActivity.this.finish();
            }
        },"返回",false);
    }
    /**
     * 检查输入内容格式
     * */
    private boolean checkInput(){

        if(S.isNull(username_text.getText().toString())){
            Utils.toastError(this, "请输入用户名");
            return false;
        }

        return true;
    }
}
