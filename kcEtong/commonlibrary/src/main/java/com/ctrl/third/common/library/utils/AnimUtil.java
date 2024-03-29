package com.ctrl.third.common.library.utils;

import android.app.Activity;

import com.ctrl.third.common.library.R;


/**
 * 页面跳转时的动画效果
 */
public class AnimUtil {

    /**
     * slip in
     */
    public static void intentSlidIn(Activity activity) {
        activity.overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }

    /**
     * Slip off
     */
    public static void intentSlidOut(Activity activity) {
        activity.overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }

//	/** push up */
//	public static void intentPushUp(Activity activity) {
//		activity.overridePendingTransition(R.anim.push_up, R.anim.push_out);
//	}
//
//	/** push down */
//	public static void intentPushDown(Activity activity) {
//		activity.overridePendingTransition(R.anim.push_out, R.anim.push_down);
//	}
}
