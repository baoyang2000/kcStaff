package com.ctrl.android.kcetong.ui.activity;

import android.os.Bundle;
import android.util.Patterns;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.GeolocationPermissions;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.ctrl.android.kcetong.R;
import com.ctrl.android.kcetong.model.AppHolder;
import com.ctrl.android.kcetong.toolkit.util.LLog;
import com.ctrl.android.kcetong.toolkit.webutils.RetrofitUtil;
import com.ctrl.android.kcetong.ui.base.BaseActivity;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ZhidongActivity extends BaseActivity {

    @BindView(R.id.progressBar1)
    ProgressBar  pg1;
    @BindView(R.id.web_view)
    WebView      webView;
    @BindView(R.id.go_back)
    ImageView    back;
    @BindView(R.id.layout_back)
    LinearLayout layout_back;

//    private String mTitle;
    private String url;
    @Override
    protected void initView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_zhidong);
        ButterKnife.bind(this);
    }

    @Override
    protected void initData() {

        WebSettings         mWebSettings        = webView.getSettings();
        mWebSettings.setJavaScriptEnabled(true);
        mWebSettings.setAllowFileAccess(true);
        mWebSettings.setDomStorageEnabled(true);//允许DCOM
        mWebSettings.setUseWideViewPort(true);
        mWebSettings.setLoadWithOverviewMode(true);
        //启用地理定位
        mWebSettings.setGeolocationEnabled(true);
        //设置定位的数据库路径
        //webSettings.setGeolocationDatabasePath(dir);
        //最重要的方法，一定要设置，这就是出不来的主要原因
        mWebSettings.setDomStorageEnabled(true);


        url = RetrofitUtil.zhiDongUrl + AppHolder.getInstance().getMemberInfo().getUserName();
        //覆盖WebView默认使用第三方或系统默认浏览器打开网页的行为，使网页用WebView打开
        webView.setWebViewClient(new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                // TODO Auto-generated method stub
                LLog.d("---1111----"+ url);
                return false;
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                LLog.d("----22222----"+ url);
                ZhidongActivity.this.url = url;
                if(url.contains("/zhidongH5/html/index.html")){
                    layout_back.setVisibility(View.VISIBLE);
                }else {
                    layout_back.setVisibility(View.GONE);
                }
                showProgress(false);
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!url.contains("/zhidongH5/html/index.html")){
                    layout_back.setVisibility(View.GONE);
                }else {
                    layout_back.setVisibility(View.VISIBLE);
                    ZhidongActivity.this.finish();
                }
            }
        });

        if (Patterns.WEB_URL.matcher(url).matches()){
            webView.loadUrl(url);
        }
        if(!url.contains("/zhidongH5/html/index.html")){
            back.setVisibility(View.GONE);
        }else {
            back.setVisibility(View.VISIBLE);
        }

        webView.setWebChromeClient(new WebChromeClient(){
            @Override
            public void onProgressChanged(WebView view, int newProgress) {

                if(newProgress==100){
                    pg1.setVisibility(View.GONE);//加载完网页进度条消失
                }
                else{
                    pg1.setVisibility(View.VISIBLE);//开始加载网页时显示进度条
                    pg1.setProgress(newProgress);//设置进度值
                }

            }

            @Override
            public void onGeolocationPermissionsShowPrompt(String origin, GeolocationPermissions.Callback callback) {
                callback.invoke(origin, true, false);
                super.onGeolocationPermissionsShowPrompt(origin, callback);
            }

        });

    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            // 返回上一页面
            webView.goBack();
            if(!url.contains("/zhidongH5/html/index.html")){
                layout_back.setVisibility(View.GONE);
            }else {
                layout_back.setVisibility(View.VISIBLE);
                ZhidongActivity.this.finish();
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
