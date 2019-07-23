package com.ctrl.android.kcetong.toolkit.util;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;

import java.util.Stack;

/**
 * Activity 栈管理类
 * 
 * @author liux (http://my.oschina.net/liux)
 * @version 1.0
 * @created 2012-3-21
 */
public class AppManager {

	private static Stack<Activity> activityStack;
	private static AppManager instance;
	public static int pageNumber = 5;//每页显示的条数

	public static String PageIndex = "1";//显示第几页
	/**
	 * 私有的构造方法
	 */
	private AppManager() {
	}

	/**
	 * 单例的获取当前类的实例
	 */
	public static AppManager getAppManager() {
		if (instance == null) {
			instance = new AppManager();
		}
		return instance;
	}

	/**
	 * 将当前activity添加到activity管理栈
	 */
	public void addActivity(Activity activity) {
		if (activityStack == null) {
			activityStack = new Stack<Activity>();
		}
		activityStack.add(activity);
	}

	/**
	 * 获取在前台运行的activity
	 */
	public Activity currentActivity() {
		if (activityStack == null) {
			return null;
		}
		Activity activity = activityStack.lastElement();
		return activity;
	}

	/**
	 * 获得当前栈中数量
	 */
	public int getActivityStackCount()
	{
		if (activityStack==null)
			return 0;
		return activityStack.size();
	}
	
	/**
	 * 结束当前运行的activity
	 */
	public void finishActivity() {
		if (activityStack == null) {
			return;
		}
		Activity activity = activityStack.lastElement();
		finishActivity(activity);
	}

	/**
	 * 结束制定的activity
	 */
	public void finishActivity(Activity activity) {
		if (activityStack == null) {
			return;
		}
		if (activity != null) {
			activityStack.remove(activity);
			activity.finish();
			activity = null;
		}
	}

	/**
	 * 结束所有的activity
	 */
	public void finishActivity(Class<?> cls) {
		if (activityStack == null) {
			return;
		}
		
		for(int i=0;i<activityStack.size();i++){
			if (activityStack.get(i).getClass().equals(cls)) {
				finishActivity(activityStack.get(i));
			}
		}
	}

	/**
	 * 清空activity栈
	 */
	public void finishAllActivity() {
		if (activityStack == null) {
			return;
		}
		for (int i = 0, size = activityStack.size(); i < size; i++) {
			if (null != activityStack.get(i)) {
				activityStack.get(i).finish();
			}
		}
		activityStack.clear();
	}

	/**
	 * 退出应用
	 */
	@SuppressWarnings("deprecation")
	public void AppExit(Context context) {
		try {
			finishAllActivity();
			ActivityManager activityMgr = (ActivityManager) context
					.getSystemService(Context.ACTIVITY_SERVICE);
			activityMgr.restartPackage(context.getPackageName());
			System.exit(0);
		} catch (Exception e) {
		}
	}
	/**
	 * 结束所有Activity
	 */
	public void killAllActivity() {
		if (activityStack == null) {
			return;
		}
		for (int i = 0, size = activityStack.size(); i < size; i++) {
			if (null != activityStack.get(i)) {
				activityStack.get(i).finish();
			}
		}
		activityStack.clear();
	}
	
	/**
	 * 结束指定的Activity
	 */
	public void killActivity(Activity activity) {

		if (activity != null && activityStack != null) {
			activityStack.remove(activity);
			activity.finish();
			activity = null;
			return;
		}
		if (activity != null) {
			activity.finish();
		}
	}
}