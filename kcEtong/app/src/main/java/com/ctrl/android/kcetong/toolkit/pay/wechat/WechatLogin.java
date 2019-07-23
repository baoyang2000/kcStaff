/**
 * 
 */
package com.ctrl.android.kcetong.toolkit.pay.wechat;

import android.content.Context;
import android.os.Handler;

import com.ctrl.android.kcetong.toolkit.pay.KeyStore;
import com.tencent.mm.sdk.modelmsg.SendAuth;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

/**
 * @author Qiu
 * 
 */
public class WechatLogin {
	public static Handler WechatHandler;
	IWXAPI msgApi;
	Context activity;
	SendAuth.Req req;
	
	public static final int WechatLoginMessageWhat = 0x112;
	public static final int WechatLoginCancelMessageWhat = 0x113;// 取消登录

	public WechatLogin(Handler weHandler, int mtype, Context context,
			String scope, String state) {
		WechatHandler = weHandler;
		this.activity = context;
		WechatUtil.WechatOperationType = mtype;
		req = new SendAuth.Req();
		req.scope = scope;
		req.state = state;
		msgApi = WXAPIFactory.createWXAPI(activity, null);
		msgApi.registerApp(KeyStore.getWechatAppId());
	}

	
	public void authReq() {
		WechatUtil.WechatOperationType = 1;
		msgApi.sendReq(req);
	}
}
