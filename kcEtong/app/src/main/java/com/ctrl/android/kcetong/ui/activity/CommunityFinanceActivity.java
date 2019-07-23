package com.ctrl.android.kcetong.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import com.ctrl.android.kcetong.R;
import com.ctrl.android.kcetong.toolkit.util.MessageUtils;
import com.ctrl.android.kcetong.toolkit.util.Utils;
import com.ctrl.android.kcetong.ui.base.BaseActivity;
import com.mob.tools.utils.UIHandler;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.wechat.friends.Wechat;

import static android.R.attr.action;

public class CommunityFinanceActivity extends BaseActivity implements PlatformActionListener ,Handler.Callback{

    @BindView(R.id.web_finance)
    WebView     web_finance;
    @BindView(R.id.progressBar1)
    ProgressBar pg1;

    private String url;

    private static final int MSG_TOAST = 1;
    private static final int MSG_ACTION_CCALLBACK = 2;
    private static final int MSG_CANCEL_NOTIFY = 3;
    private String mTitle;

    @Override
    protected void initView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_community_finance);
        ButterKnife.bind(this);
    }

    @Override
    protected void initData() {

        JavaScriptInterface javaScriptInterface = new JavaScriptInterface(CommunityFinanceActivity.this);
        WebSettings mWebSettings = web_finance.getSettings();
        mWebSettings.setJavaScriptEnabled(true);
        mWebSettings.setAllowFileAccess(true);
        mWebSettings.setDomStorageEnabled(true);//允许DCOM
        mWebSettings.setUseWideViewPort(true);
        mWebSettings.setLoadWithOverviewMode(true);
        mWebSettings.setAppCacheEnabled(true);
        String ua = mWebSettings.getUserAgentString();
        mWebSettings.setUserAgentString(ua + "; anfangapp");
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT){
            mWebSettings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }
        //启用地理定位
        mWebSettings.setGeolocationEnabled(true);
        //设置定位的数据库路径
        //webSettings.setGeolocationDatabasePath(dir);
        //最重要的方法，一定要设置，这就是出不来的主要原因
        mWebSettings.setDomStorageEnabled(true);
        web_finance.addJavascriptInterface(javaScriptInterface,"JSInterface");

        if(getIntent().getStringExtra("url") != null && !"".equals(getIntent().getStringExtra("url")) ){
            url = getIntent().getStringExtra("url");
            //覆盖WebView默认使用第三方或系统默认浏览器打开网页的行为，使网页用WebView打开
            web_finance.setWebViewClient(new WebViewClient(){
                @Override
                public boolean shouldOverrideUrlLoading(WebView view, String url) {
                    // TODO Auto-generated method stub
                    if( url.startsWith("http:") || url.startsWith("https:") ) {
                        return false;
                    }
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                    startActivity( intent );
                    //返回值是true的时候控制去WebView打开，为false调用系统浏览器或第三方浏览器
                    // view.loadUrl(url);
                    return true;
                }

                @Override
                public void onPageFinished(WebView view, String url) {
                    super.onPageFinished(view, url);
                    showProgress(false);
                }
            });
            web_finance.loadUrl(url);

            web_finance.setWebChromeClient(new WebChromeClient(){
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
                public void onReceivedTitle(WebView view, String title) {
                    super.onReceivedTitle(view, title);
                    Log.d("ANDROID_LAB", "TITLE=" + title);
                    //title 就是网页的title
                    mTitle = title;
                    toolbarBaseSetting(mTitle, new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            CommunityFinanceActivity.this.finish();
                        }
                    });

                }
            });
        }else {
            Utils.showShortToast(CommunityFinanceActivity.this, "请求的url地址不正确");
        }

    }


    @Override
    public boolean handleMessage(Message msg) {
        switch (msg.arg1) {
            case 1: {
                // 成功
                //Toast.makeText(this,"分享成功", 10000).show();
                //System.out.println("分享回调成功------------");
                MessageUtils.showShortToast(this,"分享成功");
            }
            break;
            case 2: {
                // 失败
                //Toast.makeText(MainActivity.this, "分享失败", 10000).show();
                MessageUtils.showShortToast(this,"分享失败");
            }
            break;
            case 3: {
                // 取消
                //Toast.makeText(MainActivity.this,"分享取消", 10000).show();
                MessageUtils.showShortToast(this,"分享取消");
            }
            break;
        }

        return false;
    }


    @Override
    public void onComplete(Platform platform, int i, HashMap<String, Object> hashMap) {
        Message msg = new Message();
        msg.what = MSG_ACTION_CCALLBACK;
        msg.arg1 = 1;
        msg.arg2 = action;
        msg.obj = platform;
        UIHandler.sendMessage(msg,this);
    }

    @Override
    public void onError(Platform platform, int i, Throwable t) {
        // 失敗
        //打印错误信息,print the error msg
        t.printStackTrace();
        //错误监听,handle the error msg
        Message msg = new Message();
        msg.what = MSG_ACTION_CCALLBACK;
        msg.arg1 = 2;
        msg.arg2 = action;
        msg.obj = t;
        UIHandler.sendMessage(msg, this);
    }

    @Override
    public void onCancel(Platform platform, int i) {
        //showProgress(false);
        // 取消
        Message msg = new Message();
        msg.what = MSG_ACTION_CCALLBACK;
        msg.arg1 = 3;
        msg.arg2 = action;
        msg.obj = platform;
        UIHandler.sendMessage(msg, this);

    }

    public class JavaScriptInterface {
        Context mContext;
        JavaScriptInterface(Context c) {
            mContext = c;
        }

        @JavascriptInterface
        public void sharedInWeb(String title,String url,String titleUrl,String text,String imageUrl) {

            Platform.ShareParams wxsp        = new Platform.ShareParams();
            Platform         wechat      = ShareSDK.getPlatform(mContext, Wechat.NAME);
            boolean          clientValid = wechat.isClientValid();
            if(!clientValid){
                MessageUtils.showLongToast(mContext,"请安装微信客户端");
                return;
            }
            MessageUtils.showLongToast(mContext, "正在启动微信");
            wxsp.setShareType(Platform.SHARE_WEBPAGE);
            wxsp.setTitle("二维码");
//            wxsp.setUrl(visitDetail.getQrImgUrl());//分享后点击跳转链接
            wxsp.setTitleUrl("到访二维码");
            wxsp.setText("请扫描二维码");
//            wxsp.setImageUrl(visitDetail.getQrImgUrl());
            Platform wxPlatform = ShareSDK.getPlatform(mContext,Wechat.NAME);
            wxPlatform.setPlatformActionListener((CommunityFinanceActivity)mContext);
            wxPlatform.share(wxsp);
        }
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK) && web_finance.canGoBack()) {
            web_finance.goBack();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

}
