package com.ctrl.android.kcetong.toolkit.util;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by Administrator on 2017/1/16.
 */

public class MessageUtils {
    public static void showLongToast(Context context, int res) {
        try {
            Toast.makeText(context, res, Toast.LENGTH_LONG).show();
        } catch (Exception e) {
        }

    }

    public static void showShortToast(Context context, int res) {
        try {
            Toast.makeText(context, res, Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
        }

    }

    public static void showLongToast(Context context, String text) {
        try {
            if (!text.equals(""))
                Toast.makeText(context, text, Toast.LENGTH_LONG).show();
        } catch (Exception e) {
        }

    }

    public static void showShortToast(Context context, String text) {
        try {
            if (!text.equals(""))
                Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
        }

    }
}
