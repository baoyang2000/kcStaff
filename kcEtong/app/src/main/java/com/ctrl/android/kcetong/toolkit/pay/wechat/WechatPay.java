/**
 * 
 */
package com.ctrl.android.kcetong.toolkit.pay.wechat;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Handler;
import android.util.Xml;
import android.widget.Toast;

import com.ctrl.android.kcetong.toolkit.pay.KeyStore;
import com.ctrl.android.kcetong.toolkit.util.ConstantsData;
import com.ctrl.android.kcetong.toolkit.util.LLog;
import com.ctrl.android.kcetong.toolkit.util.Utils;
import com.tencent.mm.sdk.constants.Build;
import com.tencent.mm.sdk.modelpay.PayReq;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.xmlpull.v1.XmlPullParser;

import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * @author Qiu
 * 
 */
public class WechatPay {

	private WechatPay() {
	}
	private final String WechatNotifyUrl = ConstantsData.PARENT_URL + "weChat/payNotify";
	private Context context;
	private String body;
	private String out_trade_no;
	private double total_fee;
	IWXAPI msgApi;
	PayReq req;
	Map<String, String> resultunifiedorder;
	public static Handler WechatHandler;
	public static final int WechatPayMessageFlag = 1103;
	public static String wxApiKey = KeyStore.wechatKey.equals("") ? KeyStore
			.getWechatApiKey() : KeyStore.wechatKey;
	private String notifyUrl = WechatNotifyUrl;

	/**
	 * 构造函数
	 * 
	 * @param context
	 *            activity
	 * @param body
	 *            订单描述
	 * @param out_trade_no
	 *            订单号
	 * @param total_fee
	 *            支付金额
	 * @param notifyType
	 *            0:外卖/正餐支付 1:余额充值 2：大厨上门
	 */
	public WechatPay(Handler mHandler, Context context, String body,
			String out_trade_no, double total_fee, int notifyType) {
		WechatHandler = mHandler;
		this.context = context;
		this.body = body;
		this.out_trade_no = out_trade_no;
		this.total_fee = total_fee;
		notifyUrl = WechatNotifyUrl;
		msgApi = WXAPIFactory.createWXAPI(context, null);
		req = new PayReq();
		msgApi.registerApp(KeyStore.getWechatAppId());
	}

	public void GetAccessToken() {
		// TODO Auto-generated method stub
		boolean isPaySupported = msgApi.getWXAppSupportAPI() >= Build.PAY_SUPPORTED_SDK_INT;
		if (isPaySupported) {
			new GetPrepayIdGenSendTask().execute();
		} else {
			Toast.makeText(context, "没有安装微信，或微信版本过低！", Toast.LENGTH_SHORT)
					.show();
		}
	}

	private class GetPrepayIdGenSendTask extends
			AsyncTask<Void, Void, Map<String, String>> {

		private ProgressDialog dialog;

		@Override
		protected void onPreExecute() {
			dialog = ProgressDialog.show(context, "提示", "初始化微信支付中...");
		}

		@Override
		protected void onPostExecute(Map<String, String> result) {
			if (dialog != null && dialog.isShowing()) {
				dialog.dismiss();
			}
			resultunifiedorder = result;
			if (result.get("return_code").equals("FAIL")) {
				Utils.toastError(context, result.get("return_msg"));
			} else {
				genPayReq();
			}

		}

		@Override
		protected void onCancelled() {
			super.onCancelled();
		}

		@Override
		protected Map<String, String> doInBackground(Void... params) {

			String url = String
					.format("https://api.mch.weixin.qq.com/pay/unifiedorder");
			String entity = genProductArgs();
			String isoEntity = "";
			try {
				isoEntity = new String(entity.toString().getBytes(),
						"ISO8859-1");
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			LLog.e("GetPrepayIdGenSendTask get Entity"+ entity);

			byte[] buf = WechatUtil.httpPost(url, isoEntity);
			String content = "";
			if (buf != null) {
				content = new String(buf);
			}
			LLog.e("GetPrepayIdGenSendTask get httpPost"+ content);
			Map<String, String> xml = decodeXml(content);
			if (content == null || content.equals("")) {
				if (dialog != null && dialog.isShowing()) {
					dialog.dismiss();
					Utils.toastError(context, "微信获取prepaid信息失败，请联系客服");
				}
			}
			return xml;
		}
	}

	public Map<String, String> decodeXml(String content) {

		try {
			Map<String, String> xml = new HashMap<String, String>();
			XmlPullParser parser = Xml.newPullParser();
			parser.setInput(new StringReader(content));
			int event = parser.getEventType();
			while (event != XmlPullParser.END_DOCUMENT) {

				String nodeName = parser.getName();
				switch (event) {
				case XmlPullParser.START_DOCUMENT:

					break;
				case XmlPullParser.START_TAG:

					if ("xml".equals(nodeName) == false) {
						// 实例化student对象
						xml.put(nodeName, parser.nextText());
					}
					break;
				case XmlPullParser.END_TAG:
					break;
				}
				event = parser.next();
			}

			return xml;
		} catch (Exception e) {
			LLog.e("decodeXml Exception"+ e.toString());
		}
		return null;

	}

	private String genNonceStr() {
		Random random = new Random();
		return MD5.getMessageDigest(String.valueOf(random.nextInt(10000))
				.getBytes());
	}

	private String genProductArgs() {
		StringBuffer xml = new StringBuffer();

		try {
			String nonceStr = genNonceStr();
			LLog.d("Wx genProductArgs"+
					"appId---------:" + KeyStore.getWechatAppId()
							+ ":pId----------:"
							+ KeyStore.getWechatParternerId());
			xml.append("</xml>");
			List<NameValuePair> packageParams = new LinkedList<NameValuePair>();
			packageParams.add(new BasicNameValuePair("appid", KeyStore
					.getWechatAppId()));
			packageParams.add(new BasicNameValuePair("body",
					WechatPay.this.body));
			packageParams.add(new BasicNameValuePair("mch_id", KeyStore
					.getWechatParternerId()));
			packageParams.add(new BasicNameValuePair("nonce_str", nonceStr));
			packageParams.add(new BasicNameValuePair("notify_url", notifyUrl));
			packageParams.add(new BasicNameValuePair("out_trade_no",
					WechatPay.this.out_trade_no));
			packageParams.add(new BasicNameValuePair("spbill_create_ip",
					"127.0.0.1"));
			BigDecimal bg = new BigDecimal(total_fee * 100);
			double doubleValue = bg.setScale(2,BigDecimal.ROUND_HALF_UP).doubleValue();
			packageParams.add(new BasicNameValuePair("total_fee",
					(int) doubleValue + ""));
			packageParams.add(new BasicNameValuePair("trade_type", "APP"));

			String sign = genPackageSign(packageParams);
			packageParams.add(new BasicNameValuePair("sign", sign));

			String xmlstring = toXml(packageParams);

			return xmlstring;

		} catch (Exception e) {
			LLog.e("genProductArgs"+
					"genProductArgs fail, ex = " + e.getMessage());
			return null;
		}
	}

	private String toXml(List<NameValuePair> params) {
		StringBuilder sb = new StringBuilder();
		sb.append("<xml>");
		for (int i = 0; i < params.size(); i++) {
			sb.append("<" + params.get(i).getName() + ">");

			sb.append(params.get(i).getValue());
			sb.append("</" + params.get(i).getName() + ">");
		}
		sb.append("</xml>");

		LLog.e("toXml"+ sb.toString());
		return sb.toString();
	}

	/**
	 * 生成签名
	 */
	private String genPackageSign(List<NameValuePair> params) {
		StringBuilder sb = new StringBuilder();

		for (int i = 0; i < params.size(); i++) {
			sb.append(params.get(i).getName());
			sb.append('=');
			sb.append(params.get(i).getValue());
			sb.append('&');
		}
		sb.append("key=");
		sb.append(wxApiKey);

		String packageSign = MD5.getMessageDigest(sb.toString().getBytes())
				.toUpperCase();
		LLog.e("genPackageSign"+ packageSign);
		return packageSign;
	}

	/**
	 * 生成APP微信支付参数 生成签名参数
	 */
	protected void genPayReq() {

		req.appId = KeyStore.getWechatAppId();
		req.partnerId = KeyStore.getWechatParternerId();
		req.prepayId = resultunifiedorder.get("prepay_id");
		req.packageValue = "prepay_id=" + resultunifiedorder.get("prepay_id");
		req.nonceStr = genNonceStr();
		req.timeStamp = String.valueOf(genTimeStamp());

		List<NameValuePair> signParams = new LinkedList<NameValuePair>();
		signParams.add(new BasicNameValuePair("appid", req.appId));
		signParams.add(new BasicNameValuePair("noncestr", req.nonceStr));
		signParams.add(new BasicNameValuePair("package", req.packageValue));
		signParams.add(new BasicNameValuePair("partnerid", req.partnerId));
		signParams.add(new BasicNameValuePair("prepayid", req.prepayId));
		signParams.add(new BasicNameValuePair("timestamp", req.timeStamp));

		req.sign = genAppSign(signParams);

		// show.setText(sb.toString());
		sendPayReq();
		LLog.e("genPayReq"+ signParams.toString());

	}

	private long genTimeStamp() {
		return System.currentTimeMillis() / 1000;
	}

	private String genAppSign(List<NameValuePair> params) {
		StringBuilder sb = new StringBuilder();

		for (int i = 0; i < params.size(); i++) {
			sb.append(params.get(i).getName());
			sb.append('=');
			sb.append(params.get(i).getValue());
			sb.append('&');
		}
		sb.append("key=");
		sb.append(wxApiKey);

		String appSign = MD5.getMessageDigest(sb.toString().getBytes());
		LLog.e("genAppSign"+ appSign);
		return appSign;
	}

	private void sendPayReq() {
		msgApi.registerApp(KeyStore.getWechatAppId());
		msgApi.sendReq(req);
	}
}
