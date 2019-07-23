package com.jh.utils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;

/**
 * Created by Administrator on 2016/12/20.
 */

public class ViewUtils {
    /**
     * 描述：layout转view
     *
     * @param context 上下文
     * @param id      layout id
     * @return view
     */
    public static View layoutToView(Context context, int id) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        return inflater.inflate(id, null);
    }
}
