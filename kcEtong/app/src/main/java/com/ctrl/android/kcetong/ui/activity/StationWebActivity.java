package com.ctrl.android.kcetong.ui.activity;

import android.os.Bundle;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import com.ctrl.android.kcetong.R;
import com.ctrl.android.kcetong.toolkit.util.StrConstant;
import com.ctrl.android.kcetong.ui.base.BaseActivity;

import butterknife.BindView;
import butterknife.ButterKnife;


public class StationWebActivity extends BaseActivity {

    @BindView(R.id.web_view)
    WebView webView;
    private WebSettings settings;
    @BindView(R.id.progressbar)
    ProgressBar progressBar;
    private String URL;
    private String TITLE = StrConstant.EASY_BUS_TITLE;
    @Override
    protected void initView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_station_web);
        ButterKnife.bind(this);
        if(getIntent().getStringExtra("type") != null && "1".equals(getIntent().getStringExtra("type"))){
            TITLE = StrConstant.EASY_SHOP_ARROUND_TITLE;
        }
        toolbarBaseSetting(TITLE, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StationWebActivity.this.finish();
            }
        });
    }

    @Override
    protected void initData() {
        URL = getIntent().getStringExtra("url");
        settings = webView.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setAllowFileAccess(true);
        settings.setLoadsImagesAutomatically(true);
        settings.setDatabaseEnabled(true);
        // 启用地理定位
        settings.setGeolocationEnabled(true);
        settings.setDomStorageEnabled(true);
        settings.setJavaScriptCanOpenWindowsAutomatically(true);
        settings.setDefaultTextEncodingName("utf-8");
        settings.setCacheMode(WebSettings.LOAD_NO_CACHE);

        webView.setWebViewClient(new WebViewClient() { // 调用自身，不调用系统浏览器
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                if (!url.startsWith("baidumap"))
                    view.loadUrl(url);
                return true;
            }

            @Override
            public void onPageFinished(WebView view, String url) {

                super.onPageFinished(view, url);
            }
        });
        webView.setWebChromeClient(new WebChromeClient() {
            public void onProgressChanged(WebView view, int progress) {
                // Activity和Webview根据加载程度决定进度条的进度大小
                // 当加载到100%的时候 进度条自动消失
                if (progress == 100) {
                    progressBar.setVisibility(View.GONE);
                }
                progressBar.setProgress(progress);
            }

            @Override
            public void onGeolocationPermissionsShowPrompt(String origin,
                    android.webkit.GeolocationPermissions.Callback callback) {
                callback.invoke(origin, true, false);
                super.onGeolocationPermissionsShowPrompt(origin, callback);
            }

        });
        webView.loadUrl(URL);
    }
}
