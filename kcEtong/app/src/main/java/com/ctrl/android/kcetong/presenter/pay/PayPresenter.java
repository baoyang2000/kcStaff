package com.ctrl.android.kcetong.presenter.pay;


import java.text.DecimalFormat;

import com.ctrl.android.kcetong.entity.PaymentPatternEnum;
import com.ctrl.android.kcetong.toolkit.pay.alipay.AliPay;
import com.ctrl.android.kcetong.toolkit.pay.wechat.WechatPay;
import com.ctrl.android.kcetong.ui.base.BaseOrderPayActivity;

/**
 * Created by Qiu on 2016/4/1.
 */
public class PayPresenter implements IPay {
    public void pay(PaymentPatternEnum payment, BaseOrderPayActivity activity, String orderId, double price) {
        if (payment == PaymentPatternEnum.Alipay) {
            AliPay aliPay = new AliPay(activity.mHandler,
                    activity, 0);
            aliPay.pay(orderId, "指动订单", "null", new DecimalFormat(
                    "0.00").format(price));
        } else if (payment == PaymentPatternEnum.WxPay) {
            WechatPay pay = new WechatPay(activity.mHandler,
                    activity, "指动订单", orderId, price, 0);
            pay.GetAccessToken();
        }
    }
}
