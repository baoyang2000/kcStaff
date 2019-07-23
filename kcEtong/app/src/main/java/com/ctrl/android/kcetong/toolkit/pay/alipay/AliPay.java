package com.ctrl.android.kcetong.toolkit.pay.alipay;

import android.app.Activity;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;

import com.alipay.sdk.app.PayTask;
import com.ctrl.android.kcetong.toolkit.pay.KeyStore;
import com.ctrl.android.kcetong.toolkit.util.ConstantsData;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Map;
import java.util.Random;

public class AliPay {
	private Handler payHandler;

	private Activity activity;

	public AliPay() {
	}

	public static final String AlipayNotifyUrl = ConstantsData.PARENT_URL + "alipay/mobileNotify";

	/**
	 * @param payHandler 接收支付宝回调handler
	 * @param activity context
	 * @param notifyType 0:外卖/正餐支付 1:余额充值 2:大厨
	 */
	public AliPay(Handler payHandler, Activity activity, int notifyType) {
		this.payHandler = payHandler;
		this.activity = activity;
		if (notifyType==0)
		{
			notifyUrl = AlipayNotifyUrl;
		}
	}

	public static final String PARTNER = KeyStore.getAliPartner();
	public static final String SELLER = KeyStore.getAliSeller();
	public static final String RSA_PRIVATE = KeyStore.alipayKey.equals("") ? KeyStore
			.getAliRsaPrivate() : KeyStore.alipayKey;
	public static  String notifyUrl = AlipayNotifyUrl;
	public static final int SDK_PAY_FLAG = 1;

	public static final int SDK_CHECK_FLAG = 2;

	/**
	 * call alipay sdk pay. 调用SDK支付
	 * 
	 */
	public void pay(String traderId, String subject, String body, String price) {
		/**
		 * 这里只是为了方便直接向商户展示支付宝的整个支付流程；所以Demo中加签过程直接放在客户端完成；
		 * 真实App里，privateKey等数据严禁放在客户端，加签过程务必要放在服务端完成；
		 * 防止商户私密数据泄露，造成不必要的资金损失，及面临各种安全风险；
		 *
		 * orderInfo的获取必须来自服务端；
		 */
		Map<String, String> params = AlipayOrderInfoUtil.buildOrderParamMap(traderId, price, subject, body);
		String orderParam = AlipayOrderInfoUtil.buildOrderParam(params);
		String sign = AlipayOrderInfoUtil.getSign(params, RSA_PRIVATE);
		final String orderInfo = orderParam + "&" + sign;

		Runnable payRunnable = new Runnable() {

			@Override
			public void run() {
				PayTask alipay = new PayTask(activity);
				Map<String, String> result = alipay.payV2(orderInfo, true);
				Log.i("msp", result.toString());

				Message msg = new Message();
				msg.what = SDK_PAY_FLAG;
				msg.obj = result;
				payHandler.sendMessage(msg);
			}
		};

		Thread payThread = new Thread(payRunnable);
		payThread.start();

	}

	/**
	 * 测试计算sign结果
	 */
	public void paySignTest() {
		String orderInfo = getOrderInfo("201501100937538370657", notifyUrl,
				"团购测试", "wangshanghui", "0.01");
		String sign = sign(orderInfo);
		Log.d("sign...0", "" + sign);
		try {
			// 仅需对sign 做URL编码
			sign = URLEncoder.encode(sign, "UTF-8");
			Log.d("sign...1", "" + sign);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		String payInfo = orderInfo + "&sign=\"" + sign + "\"&" + getSignType();

		Log.d("payInfo", "" + payInfo);
	}

	/**
	 * get the sdk version. 获取SDK版本号
	 * 
	 */
	public String getSDKVersion() {
		PayTask payTask = new PayTask(activity);
		String version = payTask.getVersion();
		return version;
	}

	/**
	 * create the order info. 创建订单信息
	 * 
	 */
	public String getOrderInfo(String traderNo, String notifyUrl,
			String subject, String body, String price) {
		// 合作者身份ID
		String orderInfo = "partner=" + "\"" + PARTNER + "\"";

		// 卖家支付宝账号
		orderInfo += "&seller_id=" + "\"" + SELLER + "\"";

		// 商户网站唯一订单号
		orderInfo += "&out_trade_no=" + "\"" + traderNo + "\"";

		// 商品名称
		orderInfo += "&subject=" + "\"" + subject + "\"";

		// 商品详情
		orderInfo += "&body=" + "\"" + body + "\"";

		// 商品金额
		orderInfo += "&total_fee=" + "\"" + price + "\"";

		// 服务器异步通知页面路径
		orderInfo += "&notify_url=" + "\"" + notifyUrl + "\"";

		// 接口名称， 固定值
		orderInfo += "&service=\"mobile.securitypay.pay\"";

		// 支付类型， 固定值
		orderInfo += "&payment_type=\"1\"";

		// 参数编码， 固定值
		orderInfo += "&_input_charset=\"utf-8\"";

		// 设置未付款交易的超时时间
		// 默认30分钟，一旦超时，该笔交易就会自动被关闭。
		// 取值范围：1m～15d。
		// m-分钟，h-小时，d-天，1c-当天（无论交易何时创建，都在0点关闭）。
		// 该参数数值不接受小数点，如1.5h，可转换为90m。
		orderInfo += "&it_b_pay=\"30m\"";

		// 支付宝处理完请求后，当前页面跳转到商户指定页面的路径，可空
		orderInfo += "&show_url=\"m.alipay.com\"";

		// 调用银行卡支付，需配置此参数，参与签名， 固定值
		// orderInfo += "&paymethod=\"expressGateway\"";

		return orderInfo;
	}

	/**
	 * get the out_trade_no for an order. 获取外部订单号
	 * 
	 */
	public String getOutTradeNo() {
		SimpleDateFormat format = new SimpleDateFormat("MMddHHmmss",
				Locale.getDefault());
		Date date = new Date();
		String key = format.format(date);

		Random r = new Random();
		key = key + r.nextInt();
		key = key.substring(0, 15);
		return key;
	}

	/**
	 * sign the order info. 对订单信息进行签名
	 * 
	 * @param content
	 *            待签名订单信息
	 */
	public String sign(String content) {
		return SignUtils.sign(content, RSA_PRIVATE);
	}

	/**
	 * get the sign type we use. 获取签名方式
	 * 
	 */
	public String getSignType() {
		return "sign_type=\"RSA\"";
	}

	public static String getInfoByErrorCode(String errorcode) {
		if (TextUtils.equals(errorcode, "9000")) {
			return "订单支付成功";
		} else if (TextUtils.equals(errorcode, "8000")) {
			return "正在处理中";
		} else if (TextUtils.equals(errorcode, "4000")) {
			return "订单支付失败";
		} else if (TextUtils.equals(errorcode, "6001")) {
			return "支付取消";
		} else if (TextUtils.equals(errorcode, "6002")) {
			return "网络连接出错";
		}

		return "订单支付失败";
	}

}
