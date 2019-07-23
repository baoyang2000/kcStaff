/**
 * 
 */
package com.ctrl.android.kcetong.toolkit.pay;

/**
 * 存放3方支付信息，NDK读取
 * 
 * @author Qiu
 */
public class KeyStore {

	public static  String getAliPartner()
	{
		return "2088102180191540";
	}

	public static String getAliSeller()
	{
		return "cjmrgu0575@sandbox.com";//cjmrgu0575@sandbox.com
	}


	public static String getAliRsaPrivate()
	{
		return "";
	}

	public static String getWechatAppId()
	{
		return "wx384df7158cd6b219";
	}


	public static String getWechatParternerId(){
		return "1417075202";
	}


	public static String getWechatAppSecret()
	{
		return "858ea1c90baf1dc445b99dc73ca55822";
	}

	public static String getWechatApiKey()
	{
		return "NrUbuAcAdAiKeChidheHCMSimjnXQaL3McbUDlI2nxvvej0HfAIWdoMLgqheRvxU";
	}

	public static String alipayKey = "";//网络传输Key (alipay)
	public static String wechatKey = "";//网络获取Key (wx)
}
