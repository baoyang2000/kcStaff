package com.ctrl.android.kcetong.ui.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ctrl.android.kcetong.R;
import com.ctrl.android.kcetong.toolkit.pay.alipay.AliplyPayUtil;
import com.ctrl.android.kcetong.toolkit.pay.wechat.WeixinPayUtil;
import com.ctrl.android.kcetong.toolkit.util.ConstantsData;
import com.ctrl.android.kcetong.toolkit.util.Utils;
import com.ctrl.android.kcetong.ui.base.BaseActivity;
import com.ctrl.android.kcetong.wxapi.WXPayEntryActivity2;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class OrderPayActivity extends BaseActivity {
    @BindView(R.id.tv_pay_name)
    TextView       mPayName;
    @BindView(R.id.tv_pay_price)
    TextView       mPayPrice;
    @BindView(R.id.tv_pay_number)
    TextView       mPayNumber;
    @BindView(R.id.tv_pay_address)
    TextView       mPayAddress;
    @BindView(R.id.tv_pay_total)
    TextView       mPayTotal;
    @BindView(R.id.rl_pay_wx)
    RelativeLayout payWx;
    @BindView(R.id.iv_pay_wx_state)
    ImageView      payWxState;
    @BindView(R.id.rl_pay_zfb)
    RelativeLayout payZfb;
    @BindView(R.id.iv_pay_zfb_state)
    ImageView      payZfbState;
    @BindView(R.id.tv_pay_submit)
    TextView       mPaySubmit;

    String number;
    private double total;
    String productName;
    String productPrice;
    String address;
    String orderId;
    private              int     paytype        = 0;
    @Override
    protected void initView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_order_pay);
        ButterKnife.bind(this);
        toolbarBaseSetting("确定订单", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OrderPayActivity.this.finish();
            }
        });
        
    }

    @Override
    protected void initData() {
        productName = getIntent().getStringExtra("productName");
        productPrice = getIntent().getStringExtra("productPrice");
        number = getIntent().getStringExtra("number");
        total = getIntent().getDoubleExtra("total", 0);
        address = getIntent().getStringExtra("address");
        orderId = getIntent().getStringExtra("orderId");
        mPayName.setText(productName);
        mPayPrice.setText(productPrice + "元");
        mPayNumber.setText(number);
        mPayAddress.setText(address);
        mPayTotal.setText(total + "元");
    }

    @OnClick({R.id.rl_pay_wx, R.id.rl_pay_zfb, R.id.tv_pay_submit})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.rl_pay_wx:
                payWxState.setImageResource(R.drawable.img_pay_select);
                payZfbState.setImageResource(R.drawable.img_pay_no);
                paytype = 0;
                break;
            case R.id.rl_pay_zfb:
                payWxState.setImageResource(R.drawable.img_pay_no);
                payZfbState.setImageResource(R.drawable.img_pay_select);
                paytype = 1;
                break;
            case R.id.tv_pay_submit:
                if(paytype == 1){//支付宝
                    showProgress(true);
                    Utils.toastError(OrderPayActivity.this, "正在启动支付宝");
                    AliplyPayUtil aliplyPayUtil = new AliplyPayUtil(OrderPayActivity.this, new AliplyPayUtil.PayStateListener() {
                        @Override
                        public void doAfterAliplyPay(boolean isSuccess) {
                            if (isSuccess) {
                                Utils.toastError(OrderPayActivity.this,"支付成功");
                                finish();
                            } else {
                                Utils.toastError(OrderPayActivity.this,"支付失败");
                                finish();
                            }
                        }
                    });
                    /***   此处金额 写了0.01元  实际情况具体问题具体分析   ***/
                    aliplyPayUtil.pay(orderId, ConstantsData.ALIPLY_URL_ORDER, "团购商品", "凯程家园团购商品", total);
                }else {//微信
                    showProgress(true);
                    WeixinPayUtil weixinPayUtil = new WeixinPayUtil(OrderPayActivity.this);
                    WXPayEntryActivity2.setPayStateListener(new WXPayEntryActivity2.PayStateListener() {
                        @Override
                        public void doAfterWeixinPay(int payStatus) {
                            if (payStatus == ConstantsData.PAY_STATUS_SUCCESS) {
                                Utils.toastError(OrderPayActivity.this, "支付成功");
                                finish();
                            } else if (payStatus == ConstantsData.PAY_STATUS_FAILED) {
                                Utils.toastError(OrderPayActivity.this, "支付失败");
                                finish();
                            } else if (payStatus == ConstantsData.PAY_STATUS_CANCLE) {
                                Utils.toastError(OrderPayActivity.this, "支付被取消");
                                finish();
                            }
                        }
                    });
                    weixinPayUtil.pay(orderId,ConstantsData.NOTICE_URL_ORDER, "凯程家园团购商品", (int)(total*100));
                }
                showProgress(false);
                break;
        }
    }
}
