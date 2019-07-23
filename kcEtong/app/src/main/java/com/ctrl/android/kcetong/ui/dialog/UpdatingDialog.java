package com.ctrl.android.kcetong.ui.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.WindowManager.LayoutParams;
import android.widget.ProgressBar;
import android.widget.TextView;


/**
 * 升级下载进度显示对话框
 *
 * @author Administrator
 */
public class UpdatingDialog extends Dialog {


    /*文本录入*/
    private TextView mTextView = null;
    /*进度条*/
    private ProgressBar mProgressBar = null;

    public UpdatingDialog(Context context, int theme) {
        super(context, theme);

    }

    /**
     * 构造方法
     *
     * @param context
     */
    public UpdatingDialog(Context context) {
        super(context, com.ctrl.android.kcetong.R.style.CommonDialog);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(com.ctrl.android.kcetong.R.layout.updata_dialog);
        this.getWindow().setGravity(Gravity.CENTER);
        LayoutParams lp = this.getWindow().getAttributes();
//		lp.alpha = 0.80f;
        lp.alpha = 1.0f;
        this.getWindow().setAttributes(lp);

        mTextView = (TextView) findViewById(com.ctrl.android.kcetong.R.id.dialog_status_edittext);
        mProgressBar = (ProgressBar) findViewById(com.ctrl.android.kcetong.R.id.status_progressBar);
        mProgressBar.setMax(100);
    }

    /**
     * 设置进度显示
     *
     * @param progressNum
     */
    public void setProgress(int progressNum) {
        this.mTextView.setText(progressNum + "%");
        this.mProgressBar.setProgress(progressNum);
    }

    /**
     * 获得当前下载进度指数
     *
     * @return
     */
    public int getProgress() {
        return this.mProgressBar.getProgress();
    }
}
