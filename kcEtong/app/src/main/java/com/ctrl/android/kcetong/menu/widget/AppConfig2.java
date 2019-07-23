package com.ctrl.android.kcetong.menu.widget;

import android.annotation.SuppressLint;
import android.content.Context;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Properties;

/**
 * 应用程序配置类：用于保存用户相关信息及设置
 * 
 */
@SuppressLint("NewApi")
public class AppConfig2 {
	public static String APP_ID = "1104510714";
	private final static String APP_CONFIG2 = "config";

	public final static String BOTTOM_KEY_USER = "Users_bottom";
	public final static String BOTTOM_KEY_USER_TEMP = "UsersTemp_bottom";//临时保存
	public final static String BOTTOM_KEY_All = "All_bottom";

	private        Context    mContext;
	private static AppConfig2 appConfig;

	public static AppConfig2 getAppConfig(Context context) {
		if (appConfig == null) {
			appConfig = new AppConfig2();
			appConfig.mContext = context;
		}
		return appConfig;
	}

	public String get(String key) {
		Properties props = get();
		return (props != null) ? props.getProperty(key) : null;
	}

	public Properties get() {
		FileInputStream fis = null;
		Properties props = new Properties();
		try {
			// 读取files目录下的config
			// fis = activity.openFileInput(APP_CONFIG);

			// 读取app_config目录下的config
			File dirConf = mContext.getDir(APP_CONFIG2, Context.MODE_PRIVATE);
			fis = new FileInputStream(dirConf.getPath() + File.separator + APP_CONFIG2);

			props.load(fis);
		} catch (Exception e) {
		} finally {
			try {
				fis.close();
			} catch (Exception e) {
			}
		}
		return props;
	}

	private void setProps(Properties p) {
		FileOutputStream fos = null;
		try {
			// 把config建在files目录下
			// fos = activity.openFileOutput(APP_CONFIG, Context.MODE_PRIVATE);

			// 把config建在(自定义)app_config的目录下
			File dirConf = mContext.getDir(APP_CONFIG2, Context.MODE_PRIVATE);
			File conf = new File(dirConf, APP_CONFIG2);
			fos = new FileOutputStream(conf);

			p.store(fos, null);
			fos.flush();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				fos.close();
			} catch (Exception e) {
			}
		}
	}

	public void set(Properties ps) {
		Properties props = get();
		props.putAll(ps);
		setProps(props);
	}

	public void set(String key, String value) {
		Properties props = get();
		props.setProperty(key, value);
		setProps(props);
	}

	public void remove(String... key) {
		Properties props = get();
		for (String k : key)
			props.remove(k);
		setProps(props);
	}
}
