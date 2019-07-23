package com.ctrl.android.kcetong.ui.activity;

import android.os.Bundle;
import android.webkit.WebView;
import android.widget.ProgressBar;

import com.ctrl.android.kcetong.R;
import com.ctrl.android.kcetong.ui.base.BaseActivity;

import butterknife.BindView;
import butterknife.ButterKnife;

public class WebActivity extends BaseActivity {

    @BindView(R.id.web_view)
    WebView web_view;
    @BindView(R.id.progressBar1)
    ProgressBar pg1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web);
        ButterKnife.bind(this);
    }

    @Override
    protected void initView(Bundle savedInstanceState) {

    }

    @Override
    protected void initData() {

    }
}
