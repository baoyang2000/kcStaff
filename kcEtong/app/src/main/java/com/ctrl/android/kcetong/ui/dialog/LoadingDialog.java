package com.ctrl.android.kcetong.ui.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ctrl.android.kcetong.R;


/**
 * Created by Qiu on 2016/4/21.
 */
public class LoadingDialog extends Dialog {

    private TextView tv;

    public LoadingDialog(Context context) {
        super(context, com.ctrl.android.kcetong.R.style.CommonDialog);
    }

    public LoadingDialog(Context context, int themeResId) {
        super(context, themeResId);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_loading_guard);
        tv = (TextView) this.findViewById(R.id.tv_loading);
        tv.setText("正在加载,请稍等");
        LinearLayout linearLayout = (LinearLayout) this.findViewById(R.id.lLayout_bg);
        linearLayout.getBackground().setAlpha(210);
    }
}
