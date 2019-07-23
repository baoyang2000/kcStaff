package com.ctrl.android.kcetong.ui.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ctrl.android.kcetong.R;
import com.ctrl.android.kcetong.model.AppHolder;
import com.ctrl.android.kcetong.model.BeforePropertyPayResult;
import com.ctrl.android.kcetong.toolkit.pay.alipay.AliplyPayUtil;
import com.ctrl.android.kcetong.toolkit.pay.wechat.WeixinPayUtil;
import com.ctrl.android.kcetong.toolkit.util.AopUtils;
import com.ctrl.android.kcetong.toolkit.util.ConstantsData;
import com.ctrl.android.kcetong.toolkit.util.LLog;
import com.ctrl.android.kcetong.toolkit.util.StrConstant;
import com.ctrl.android.kcetong.toolkit.util.Utils;
import com.ctrl.android.kcetong.toolkit.util.ViewUtil;
import com.ctrl.android.kcetong.toolkit.webutils.BaseTSubscriber;
import com.ctrl.android.kcetong.toolkit.webutils.RetrofitUtil;
import com.ctrl.android.kcetong.ui.base.BaseActivity;
import com.ctrl.android.kcetong.wxapi.WXPayEntryActivity2;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class PayStyleActivity2 extends BaseActivity {

    @BindView(R.id.pay_house)
    TextView     pay_house;
    @BindView(R.id.pay_amount)//金额
    TextView     pay_amount;
    @BindView(R.id.zhi_fu_bao_checkBox)//支付宝checkBox
    CheckBox     zhi_fu_bao_checkBox;
    @BindView(R.id.zhi_fu_bao_btn)//支付宝按钮
    LinearLayout zhi_fu_bao_btn;
    @BindView(R.id.wei_xin_checkBox)//微信checkBox
    CheckBox     wei_xin_checkBox;
    @BindView(R.id.wei_xin_btn)//微信按钮
    LinearLayout wei_xin_btn;
    @BindView(R.id.pay_btn)
    TextView     pay_btn;

    private List<CheckBox> listCheckBox ;

    private double totalPrice;
    private String orderIds;
    private String communityName;
    private String building_unit_room;
    private String TITLE = StrConstant.PAY_STYLE_TITLE;

    private String uniteId;
    @Override
    protected void initView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_pay_style2);
        ButterKnife.bind(this);
        toolbarBaseSetting(TITLE, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PayStyleActivity2.this.finish();
            }
        });

    }
    @Override
    protected void initData() {
        orderIds = getIntent().getStringExtra("orderIds");
        communityName = getIntent().getStringExtra("communityName");
        building_unit_room = getIntent().getStringExtra("building_unit_room");
        totalPrice = Double.parseDouble(getIntent().getStringExtra("debt"));

        pay_house.setText(communityName + "     " + building_unit_room);
        pay_amount.setText("￥ " + Utils.get2Double(totalPrice));

        listCheckBox = new ArrayList<>();
        listCheckBox.add(zhi_fu_bao_checkBox);
        listCheckBox.add(wei_xin_checkBox);

        zhi_fu_bao_checkBox.setChecked(true);
    }

    @OnClick({R.id.zhi_fu_bao_checkBox, R.id.zhi_fu_bao_btn, R.id.wei_xin_checkBox, R.id.wei_xin_btn, R.id.pay_btn})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.zhi_fu_bao_checkBox:
                ViewUtil.setOneChecked(listCheckBox, zhi_fu_bao_checkBox);
                break;
            case R.id.zhi_fu_bao_btn:
                ViewUtil.setOneChecked(listCheckBox, zhi_fu_bao_checkBox);
                break;
            case R.id.wei_xin_checkBox:
                ViewUtil.setOneChecked(listCheckBox,wei_xin_checkBox);
                break;
            case R.id.wei_xin_btn:
                ViewUtil.setOneChecked(listCheckBox,wei_xin_checkBox);
                break;
            case R.id.pay_btn:
                showProgress(true);
                requestBeforePropertyPay(orderIds, AppHolder.getInstance().getMemberInfo().getMemberId());
                break;
        }
    }
    /**
     * 物业账单  支付前校验
     * @param propertyPaymentIdStr 物业账单id串
     * @param memberId 会员id
     * */
    public void requestBeforePropertyPay(String propertyPaymentIdStr,String memberId){
        Map<String,String> map = new HashMap<String,String>();
        map.put(ConstantsData.METHOD,"pm.ppt.propertyPayment.checkBeforePay");//方法名称
        map.putAll(ConstantsData.getSystemParams());

        map.put("propertyPaymentIdStr",propertyPaymentIdStr);
        map.put("memberId",memberId);

        String sign = AopUtils.sign(map, ConstantsData.SECRET_VALUE);
        map.put("sign",sign);
        LLog.d(map+"");
        map.remove(ConstantsData.METHOD);
        RetrofitUtil.Api().requestBeforePropertyPay(map).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseTSubscriber<BeforePropertyPayResult>(this) {
                    @Override
                    public void onResponseCallback(JSONObject response, String resultCode) {
                        LLog.d(response+"");
                        showProgress(false);
                    }
                    @Override
                    public void onNext(BeforePropertyPayResult beforePropertyPayResult) {
                        super.onNext(beforePropertyPayResult);
                        if(TextUtils.equals("000",beforePropertyPayResult.getCode())){
                            uniteId = beforePropertyPayResult.getData().getUniteId();

                            for(int i = 0 ; i < listCheckBox.size() ; i ++){
                                //支付宝支付
                                if(listCheckBox.get(i) == zhi_fu_bao_checkBox){
                                    if(zhi_fu_bao_checkBox.isChecked()){
                                        Utils.toastError(PayStyleActivity2.this, "正在启动支付宝");
                                        AliplyPayUtil aliplyPayUtil = new AliplyPayUtil(PayStyleActivity2.this, new AliplyPayUtil.PayStateListener() {
                                            @Override
                                            public void doAfterAliplyPay(boolean isSuccess) {
                                                if (isSuccess) {
                                                    Utils.toastError(PayStyleActivity2.this,"支付成功");
                                                    finish();
                                                } else {
                                                    Utils.toastError(PayStyleActivity2.this,"支付失败");
                                                    finish();
                                                }
                                            }
                                        });
                                        /***   此处金额 写了0.01元  实际情况具体问题具体分析   ***/
                                        aliplyPayUtil.pay(uniteId, ConstantsData.ALIPLY_URL_PROPERTY, "物业缴费", "诚信行物业缴费", totalPrice);
                                    }
                                }

                                //微信支付
                                if(listCheckBox.get(i) == wei_xin_checkBox){
                                    if(wei_xin_checkBox.isChecked()){

                                        //MessageUtils.showLongToast(this, "微信支付暂未开通,敬请期待");

                                        WeixinPayUtil weixinPayUtil = new WeixinPayUtil(PayStyleActivity2.this);
                                        WXPayEntryActivity2.setPayStateListener(new WXPayEntryActivity2.PayStateListener() {
                                            @Override
                                            public void doAfterWeixinPay(int payStatus) {
                                                if (payStatus == ConstantsData.PAY_STATUS_SUCCESS) {
                                                    Utils.toastError(PayStyleActivity2.this, "支付成功");
                                                    finish();
                                                } else if (payStatus == ConstantsData.PAY_STATUS_FAILED) {
                                                    Utils.toastError(PayStyleActivity2.this, "支付失败");
                                                    finish();
                                                } else if (payStatus == ConstantsData.PAY_STATUS_CANCLE) {
                                                    Utils.toastError(PayStyleActivity2.this, "支付被取消");
                                                    finish();
                                                }
                                            }
                                        });
                                        //(int)Double.parseDouble(totalPrice)*100
                                        weixinPayUtil.pay(uniteId,ConstantsData.NOTICE_URL_PROPERTY, "诚信行物业缴费", (int)(totalPrice*100));
                                    }
                                }
                            }
                        }
                        showProgress(false);
                    }

                    @Override
                    public void onNetError(Throwable e) {
                        super.onNetError(e);
                        showProgress(false);
                    }
                });
    }

}
