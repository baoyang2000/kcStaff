/**
 *
 */
package com.ctrl.android.kcetong.toolkit.util;

import android.util.Log;


/**
 * @author Qiu
 */
public class LLog {

    public interface Debug {
        public final boolean logable = true;
    }
    public static final String TAG = "------mall---------";
    public static void d(String msg) {
        if (Debug.logable)
            Log.d(TAG, msg);
    }

    public static void e(String msg) {
        if (Debug.logable)
            Log.e(TAG, msg);
    }

    public static void i(String msg) {
        if (Debug.logable)
            Log.i(TAG, msg);
    }

    public static void v(String msg) {
        if (Debug.logable)
            Log.v(TAG, msg);
    }

    public static void w(String msg) {
        if (Debug.logable)
            Log.w(TAG, msg);
    }
}
