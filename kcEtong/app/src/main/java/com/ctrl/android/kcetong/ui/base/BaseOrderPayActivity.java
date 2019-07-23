package com.ctrl.android.kcetong.ui.base;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.widget.Toast;

import com.ctrl.android.kcetong.entity.PaymentPatternEnum;
import com.ctrl.android.kcetong.presenter.pay.IPay;
import com.ctrl.android.kcetong.presenter.pay.PayPresenter;
import com.ctrl.android.kcetong.toolkit.pay.alipay.PayResult;
import com.ctrl.android.kcetong.toolkit.pay.wechat.WechatPay;

import java.util.Map;

import com.ctrl.android.kcetong.toolkit.pay.alipay.AliPay;

/**
 * Created by Administrator on 2016/11/16.
 */
public abstract class BaseOrderPayActivity<P extends IPay> extends BaseActivity {

    protected P presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            if (getPresenterClass() != null) {
                presenter = (P) getPresenterClass().newInstance();
            }
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    protected Class<? extends IPay> getPresenterClass() {
        return PayPresenter.class;
    }

    protected abstract void payResultCallback(PaymentPatternEnum plat, boolean paySuccess, String resultCode);

    public Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case AliPay.SDK_PAY_FLAG: {
                    PayResult payResult = new PayResult((Map<String, String>) msg.obj);
                    String resultInfo = payResult.getResult();// 同步返回需要验证的信息
                    String resultStatus = payResult.getResultStatus();
                    // 判断resultStatus 为“9000”则代表支付成功，具体状态码代表含义可参考接口文档
                    if (TextUtils.equals(resultStatus, "9000")) {
                        payResultCallback(PaymentPatternEnum.Alipay, true, "9000");
                    } else {
                        // 判断resultStatus 为非“9000”则代表可能支付失败
                        // “8000”
                        // 代表支付结果因为支付渠道原因或者系统原因还在等待支付结果确认，最终交易是否成功以服务端异步通知为准（小概率状态）
                        payResultCallback(PaymentPatternEnum.Alipay, false, resultStatus);
                    }
                }
                break;

                case AliPay.SDK_CHECK_FLAG: {
                    Toast.makeText(BaseOrderPayActivity.this, "检查结果为：" + msg.obj,
                            Toast.LENGTH_SHORT).show();
                }
                break;

                case WechatPay.WechatPayMessageFlag: {
                    String errorCode = msg.obj.toString();
                    if (errorCode.equals("ERR_OK")) {
                        payResultCallback(PaymentPatternEnum.WxPay, true, "ERR_OK");

                    } else if (errorCode.equals("ERR_USER_CANCEL")) {
                        payResultCallback(PaymentPatternEnum.WxPay, true, "ERR_USER_CANCEL");
                    } else {
                        payResultCallback(PaymentPatternEnum.WxPay, true, errorCode);
                    }
                }
                break;
                default:
                    break;
            }
        }
    };
}
