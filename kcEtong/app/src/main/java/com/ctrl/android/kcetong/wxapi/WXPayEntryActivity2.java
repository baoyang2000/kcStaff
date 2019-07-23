package com.ctrl.android.kcetong.wxapi;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.ctrl.android.kcetong.R;
import com.ctrl.android.kcetong.toolkit.util.ConstantsData;
import com.ctrl.android.kcetong.ui.base.BaseActivity;
import com.tencent.mm.sdk.constants.ConstantsAPI;
import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

public class WXPayEntryActivity2 extends BaseActivity implements IWXAPIEventHandler {

    private static final String TAG = "MicroMsg.SDKSample.WXPayEntryActivity";
    private IWXAPI api;

    private static PayStateListener payStateListener;

    public WXPayEntryActivity2(){

    }

    public WXPayEntryActivity2(PayStateListener payStateListener){
        this.payStateListener = payStateListener;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pay_result);

        api = WXAPIFactory.createWXAPI(this, ConstantsData.APP_ID);
        api.handleIntent(getIntent(), this);
    }

    @Override
    protected void initView(Bundle savedInstanceState) {

    }

    @Override
    protected void initData() {

    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        api.handleIntent(intent, this);
    }

    @Override
    public void onReq(BaseReq req) {
    }

    @Override
    public void onResp(BaseResp resp) {
//		Log.d(TAG, "onPayFinish, errCode = " + resp.errCode);
//		if (resp.getType() == ConstantsAPI.COMMAND_PAY_BY_WX) {
//			AlertDialog.Builder builder = new AlertDialog.Builder(this);
//			builder.setTitle(R.string.app_tip);
//			builder.setMessage(getString(R.string.pay_result_callback_msg, resp.errStr +";code=" + String.valueOf(resp.errCode)));
//			builder.show();
//		}
        Log.d("demo", "resp: " + resp);

        if(resp.getType() == ConstantsAPI.COMMAND_PAY_BY_WX){
            Log.d("demo","onPayFinish,errCode="+resp.errCode);
            AlertDialog.Builder builder =new AlertDialog.Builder(this);
            builder.setTitle(R.string.app_tip);
            if(resp.errCode == 0){
                //builder.setMessage("支付成功");
                payStateListener.doAfterWeixinPay(ConstantsData.PAY_STATUS_SUCCESS);
            } else if(resp.errCode == -1){
                //builder.setMessage("错误");
                payStateListener.doAfterWeixinPay(ConstantsData.PAY_STATUS_FAILED);
            } else if(resp.errCode == -2){
                //builder.setMessage("取消支付");
                payStateListener.doAfterWeixinPay(ConstantsData.PAY_STATUS_CANCLE);
            }
            //builder.show();
            finish();
        }


    }


    /**
     * 用于支付后回调
     */
    public interface PayStateListener {
        public abstract void doAfterWeixinPay(int payStatus);
    }

    public PayStateListener getPayStateListener() {
        return payStateListener;
    }

    public static void setPayStateListener(PayStateListener payStateLis) {
        payStateListener = payStateLis;
    }
}