package com.ctrl.android.kcetong.listener;
/**
 * 第三方用户电话验证，支付密码设置接口回调
 * @author Qiu
 *
 */
public interface Hint3rdPhonePaypwdDialogListener {
	void submit(String mobile, String rancode, String password);

	void sendRancode(String mobile);

	void cancel();
}
