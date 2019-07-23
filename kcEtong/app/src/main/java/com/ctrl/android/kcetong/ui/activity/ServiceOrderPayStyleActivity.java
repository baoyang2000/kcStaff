package com.ctrl.android.kcetong.ui.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ctrl.android.kcetong.R;
import com.ctrl.android.kcetong.toolkit.pay.alipay.AliplyPayUtil;
import com.ctrl.android.kcetong.toolkit.pay.wechat.WeixinPayUtil;
import com.ctrl.android.kcetong.toolkit.util.AopUtils;
import com.ctrl.android.kcetong.toolkit.util.ConstantsData;
import com.ctrl.android.kcetong.toolkit.util.LLog;
import com.ctrl.android.kcetong.toolkit.util.MessageUtils;
import com.ctrl.android.kcetong.toolkit.util.StrConstant;
import com.ctrl.android.kcetong.toolkit.util.ViewUtil;
import com.ctrl.android.kcetong.toolkit.webutils.BaseTSubscriber;
import com.ctrl.android.kcetong.toolkit.webutils.RetrofitUtil;
import com.ctrl.android.kcetong.ui.base.BaseActivity;
import com.ctrl.android.kcetong.wxapi.WXPayEntryActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.ResponseBody;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class ServiceOrderPayStyleActivity extends BaseActivity {

    @BindView(R.id.zhi_fu_bao_btn)//支付宝按钮
            LinearLayout zhi_fu_bao_btn;
    @BindView(R.id.zhi_fu_bao_checkBox)//支付宝checkBox
            CheckBox     zhi_fu_bao_checkBox;
    @BindView(R.id.wei_xin_btn)//微信按钮
            LinearLayout wei_xin_btn;
    @BindView(R.id.wei_xin_checkBox)//微信checkBox
            CheckBox     wei_xin_checkBox;
    @BindView(R.id.pay_amount)//
            TextView     pay_amount;
    @BindView(R.id.tv_name)//
            TextView     tv_name;
    @BindView(R.id.pay_btn)
            TextView     pay_btn;

    private String TITLE = StrConstant.PAY_STYLE_TITLE;

    private String cartIdStr;
    private double totalPrice;
    private String companyName;

    private List<CheckBox> listCheckBox ;
    private String evaluateLevel = "0";
    private String evaluateContent = "";

    @Override
    protected void initView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_service_order_pay_style);
        ButterKnife.bind(this);
        toolbarBaseSetting(TITLE, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ServiceOrderPayStyleActivity.this.finish();
            }
        });
    }

    @Override
    protected void initData() {
        cartIdStr = getIntent().getStringExtra("id");
        if(getIntent().getStringExtra("price") != null){
            totalPrice = Double.parseDouble(getIntent().getStringExtra("price"));
        }

        companyName = getIntent().getStringExtra("name");
        evaluateLevel = getIntent().getStringExtra("evaluateLevel");
        evaluateContent = getIntent().getStringExtra("evaluateContent");

        listCheckBox = new ArrayList<>();
        listCheckBox.add(zhi_fu_bao_checkBox);
        listCheckBox.add(wei_xin_checkBox);

        zhi_fu_bao_checkBox.setChecked(true);

        pay_amount.setText("￥ " + String.valueOf(totalPrice)+"元");
        tv_name.setText(companyName);
    }

    @OnClick({R.id.zhi_fu_bao_checkBox, R.id.zhi_fu_bao_btn, R.id.wei_xin_checkBox, R.id.wei_xin_btn, R.id.pay_btn})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.zhi_fu_bao_checkBox:
            case R.id.zhi_fu_bao_btn:
                ViewUtil.setOneChecked(listCheckBox, zhi_fu_bao_checkBox);
                break;
            case R.id.wei_xin_checkBox:
            case R.id.wei_xin_btn:
                ViewUtil.setOneChecked(listCheckBox, wei_xin_checkBox);
                break;
            case R.id.pay_btn:
                for(int i = 0 ; i < listCheckBox.size() ; i ++){
                    //支付宝支付
                    if(listCheckBox.get(i) == zhi_fu_bao_checkBox){
                        if(zhi_fu_bao_checkBox.isChecked()){
                            AliplyPayUtil aliplyPayUtil = new AliplyPayUtil(ServiceOrderPayStyleActivity.this, new AliplyPayUtil.PayStateListener() {
                                @Override
                                public void doAfterAliplyPay(boolean isSuccess) {
                                    if (isSuccess) {
                                        MessageUtils.showShortToast(ServiceOrderPayStyleActivity.this, "支付成功");
                                        showProgress(true);
                                        requestAfterServiceOrderPay(cartIdStr, totalPrice + "", "1", "3", "", "支付宝", "", "", "", "", "");
                                    } else {
                                        MessageUtils.showShortToast(ServiceOrderPayStyleActivity.this,"支付失败");
                                        showProgress(true);
                                        requestAfterServiceOrderPay(cartIdStr, totalPrice + "", "0", "3", "", "支付宝", "", "", "", "", "");
                                    }
                                }
                            });
                            /***   此处金额 写了0.01元  实际情况具体问题具体分析   ***/
                            aliplyPayUtil.pay(cartIdStr, ConstantsData.ALIPLY_URL_SERVICE, "诚信行特约服务", "诚信行特约服务", totalPrice);
                        }
                    }

                    //微信支付
                    if(listCheckBox.get(i) == wei_xin_checkBox){
                        if(wei_xin_checkBox.isChecked()){
                            //MessageUtils.showLongToast(this, "key值尚未申请, 支付暂未开通");
                            WeixinPayUtil weixinPayUtil = new WeixinPayUtil(this);
                            WXPayEntryActivity.setPayStateListener(new WXPayEntryActivity.PayStateListener() {
                                @Override
                                public void doAfterWeixinPay(int payStatus) {

                                    if (payStatus == ConstantsData.PAY_STATUS_SUCCESS) {
                                        MessageUtils.showLongToast(ServiceOrderPayStyleActivity.this, "支付成功");
                                        showProgress(true);
                                        requestAfterServiceOrderPay(cartIdStr, totalPrice + "", "1", "4", "", "微信", "", "", "", "", "");
                                        // edao.requestAfterOrderPay_weixin(orderIdStr, totalCost, payStatus_X, payMent, dealType);
                                    } else if (payStatus == ConstantsData.PAY_STATUS_FAILED) {
                                        MessageUtils.showLongToast(ServiceOrderPayStyleActivity.this, "支付失败");
                                        showProgress(true);
                                        requestAfterServiceOrderPay(cartIdStr,totalPrice+"","0","4","","微信","","","","","");
                                        //   finish();
                                        //goToOrderDetail();
                                    } else if (payStatus == ConstantsData.PAY_STATUS_CANCLE) {
                                        MessageUtils.showLongToast(ServiceOrderPayStyleActivity.this, "支付被取消");
                                        finish();
                                        //goToOrderDetail();
                                    }
                                }
                            });
                            //(int)Double.parseDouble(totalPrice)*100
                            weixinPayUtil.pay(cartIdStr, ConstantsData.NOTICE_URL_SERVICE,"诚信行特约服务", (int)(totalPrice*100));
                        }
                    }
                }
                break;
        }
    }
    /**
     * 订单支付成功后
     * @param serviceOrderId 订单id
     * @param totalCost 订单总金额
     * @param payStatus 支付状（0：未付款、1：已付款）
     * @param payMode 支付方式（1：余额支付、2：线下支付[货到付款]、3、支付宝、4：微信、5：银联卡）
     * @param dealId 支付反馈的流水单号
     * @param dealType 获取支付返回银行类型（alipay：支付宝、weixin：微信）
     * @param dealFee 交易金额
     * @param dealState 交易状态
     * @param dealSigunre 返回签名数据
     * @param bankcardNo 支付卡号
     * @param bankcardName 发卡行
     * */
    public void requestAfterServiceOrderPay(String serviceOrderId,String totalCost,
            String payStatus,
            String payMode,
            String dealId,
            String dealType,
            String dealFee,
            String dealState,
            String dealSigunre,
            String bankcardNo,
            String bankcardName){
        Map<String,String> map = new HashMap<>();
        map.put(ConstantsData.METHOD,"pm.ppt.serviceOrder.afterServiceOrderPay");//方法名称
        map.putAll(ConstantsData.getSystemParams());

        map.put("serviceOrderId",serviceOrderId);
        map.put("totalCost",totalCost);
        map.put("payStatus",payStatus);
        map.put("payMode",payMode);
        map.put("dealId",dealId);
        map.put("dealType",dealType);
        map.put("dealFee",dealFee);
        map.put("dealState",dealState);
        map.put("dealSigunre",dealSigunre);
        map.put("bankcardNo",bankcardNo);
        map.put("bankcardName",bankcardName);
        map.put("evaluateContent",evaluateContent);
        map.put("evaluateLevel",evaluateLevel);


        String sign = AopUtils.sign(map, ConstantsData.SECRET_VALUE);
        map.put("sign",sign);

        LLog.d(map+"");
        map.remove(ConstantsData.METHOD);
        RetrofitUtil.Api().requestAfterServiceOrderPay(map).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseTSubscriber<ResponseBody>(this) {
                    @Override
                    public void onResponseCallback(JSONObject response, String resultCode) {
                        LLog.d(response+"");
                        showProgress(false);
                        try {
                            if("000".equals(response.getString("code"))){
                                setResult(RESULT_OK);
                                finish();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    @Override
                    public void onNext(ResponseBody responseBody) {
                        super.onNext(responseBody);
                        showProgress(false);
                    }
                });
    }
}
