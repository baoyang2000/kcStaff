package com.ctrl.android.kcetong.ui.activity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ctrl.android.kcetong.R;
import com.ctrl.android.kcetong.model.AppHolder;
import com.ctrl.android.kcetong.model.Img;
import com.ctrl.android.kcetong.model.Noticedetail;
import com.ctrl.android.kcetong.toolkit.Url.Url;
import com.ctrl.android.kcetong.toolkit.util.AopUtils;
import com.ctrl.android.kcetong.toolkit.util.ConstantsData;
import com.ctrl.android.kcetong.toolkit.util.LLog;
import com.ctrl.android.kcetong.toolkit.util.S;
import com.ctrl.android.kcetong.toolkit.util.Utils;
import com.ctrl.android.kcetong.toolkit.webutils.BaseTSubscriber;
import com.ctrl.android.kcetong.toolkit.webutils.RetrofitUtil;
import com.ctrl.android.kcetong.ui.base.BaseActivity;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.ResponseBody;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

;

/**
 * @项目名称: 诚信行业主端<br>
 * @类描述: 公告详情<br>
 * @创建人： whs <br>
 * @创建时间： 2017/1/12 14:38 <br>
 * @修改人： <br>
 * @修改时间: 2017/1/12 14:38 <br>
 */
public class NoticeDetailActivity extends BaseActivity {

    @BindView(R.id.tv_notice_detail_title)
    TextView     tvNoticeDetailTitle;
    @BindView(R.id.tv_notice_detail_releaser)
    TextView     tvNoticeDetailReleaser;
    @BindView(R.id.title_layout)
    LinearLayout titleLayout;
    @BindView(R.id.tv_notice_detail_content)
    WebView      webView;
    @BindView(R.id.iv_notice_detail_content)
    ImageView    ivNoticeDetailContent;
    @BindView(R.id.tv_notice_detail_confirm)
    TextView     tvNoticeDetailConfirm;
    @BindView(R.id.ll_qianshou)
    LinearLayout llQianshou;
    @BindView(R.id.activity_notice_detail)
    LinearLayout activityNoticeDetail;
    private String propertyNoticeId;
    private String activityId;
    private String    title   = "";
    private List<Img> listImg = new ArrayList<>();
    private ArrayList<String> imagelist;//传入到图片放大类 用
    private int position = 0;
    private WebSettings settings;

    @Override
    protected void initView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_notice_detail);
        ButterKnife.bind(this);
        propertyNoticeId = getIntent().getStringExtra("propertNoticeId");
        title = getIntent().getStringExtra("title");
        if("0".equals(holder.getMemberInfo().getSupers())){
            activityId = AppHolder.getInstance().getProprietor().getProprietorId();
        }else if("1".equals(holder.getMemberInfo().getSupers())
                 || "2".equals(AppHolder.getInstance().getMemberInfo().getSupers())){
            activityId = "";
        }
    }

    @Override
    protected void initData() {
        toolbarBaseSetting("公告详情", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utils.hideKeyBoard(NoticeDetailActivity.this);
                Intent intent = new Intent();
                setResult(6001, intent);
                NoticeDetailActivity.this.finish();
            }
        });

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

        settings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        settings.setLoadWithOverviewMode(true);

        requestNotice(propertyNoticeId, activityId);
    }

    private void openWebView(WebView web,String urlDate) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            web.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.TEXT_AUTOSIZING);
        } else {
            web.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NORMAL);
        }
        web.loadDataWithBaseURL(null, getHtmlData(urlDate), "text/html", "utf-8", null);
        web.getSettings().setJavaScriptEnabled(false);
    }

    private String getHtmlData(String bodyHTML) {
        String head = "<head><style>img{max-width: 100%; width:auto; height: auto;}</style></head>";
        return "<html>" + head + "<body>" + bodyHTML + "</body></html>";
    }


    //获取物业通知公告详情
    private void requestNotice(final String propertyNoticeId, final String activityId) {
        showProgress(true);
        Map<String, String> map = new HashMap<>();
        map.putAll(ConstantsData.getSystemParams());
        map.put(ConstantsData.METHOD, Url.noticedetailsUrl);
        map.put("propertyNoticeId", propertyNoticeId);
        map.put("activityId", activityId);

        String sign = AopUtils.sign(map, ConstantsData.SECRET_VALUE);
        map.put("sign", sign);
        map.remove(ConstantsData.METHOD);
        RetrofitUtil.Api().PropertyNoticeInfo(map).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new BaseTSubscriber<Noticedetail>(this) {
            @Override
            public void onResponseCallback(JSONObject response, String resultCode) {
                LLog.d("onResponseCallback: " + response);
                showProgress(false);

            }

            @Override
            public void onNext(Noticedetail noticedetail) {
                super.onNext(noticedetail);
                if (TextUtils.equals("000", noticedetail.getCode())) {
                    if("0".equals(holder.getMemberInfo().getSupers())){
                        if (noticedetail.getData().getPropertyNoticeInfo().getStatus().equals("0")) {
                            changeNoticeModify(propertyNoticeId, activityId);
                        }
                    }

                    listImg = noticedetail.getData().getListImg();
                    tvNoticeDetailTitle.setText(title);
                    tvNoticeDetailReleaser.setText(noticedetail.getData().getPropertyNoticeInfo().getUserName() + "   " + Utils.getDateStrFromStamp(ConstantsData.YYYY_MM_DD, noticedetail.getData().getPropertyNoticeInfo().getCreateTime()));

                    // 设置默认字体大小
                    settings.setDefaultFontSize(14);
                    openWebView(webView,S.getStr(noticedetail.getData().getPropertyNoticeInfo().getContent()));
//                    webView.loadDataWithBaseURL("about:blank", "<head><style>img{max-width:"+Utils.getScreenWidth(NoticeDetailActivity.this) +" !important;}</style></head>"+S.getStr(noticedetail.getData().getPropertyNoticeInfo().getContent()),"text/html", "utf-8", null);
                    /*if (listImg == null || listImg.size() < 1) {
                        ivNoticeDetailContent.setVisibility(View.GONE);
                    } else {
                        //有图片的话加载图片
                        Glide.with(NoticeDetailActivity.this).load(listImg.get(0).getOriginalImg()).into(ivNoticeDetailContent);

                        *//*ivNoticeDetailContent.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                imagelist = new ArrayList<String>();
                                imagelist.add(listImg.get(0).getOriginalImg());
                                ImagPagerUtil imagPagerUtil = new ImagPagerUtil(NoticeDetailActivity.this, imagelist);
                               // imagPagerUtil.setContentText();
                                imagPagerUtil.show();
                            }
                        });*//*

                    }*/
                    showProgress(false);
                }
            }
        });
    }


    //更改公告状态为已读
    private void changeNoticeModify(String propertyNoticeId, String activityId) {
        Map<String, String> map = new HashMap<>();
        map.putAll(ConstantsData.getSystemParams());
        map.put(ConstantsData.METHOD, Url.NoticeModifyUrl);
        map.put("propertyNoticeId", propertyNoticeId);
        map.put("activityId", activityId);
        String sign = AopUtils.sign(map, ConstantsData.SECRET_VALUE);
        map.put("sign", sign);
        map.remove(ConstantsData.METHOD);
        RetrofitUtil.Api().chengenoticmodify(map).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new BaseTSubscriber<ResponseBody>(this) {
            @Override
            public void onResponseCallback(JSONObject response, String resultCode) {
                LLog.d(response + "");
                    if (TextUtils.equals("000", (CharSequence) resultCode)) {
                        LLog.d("已更改");
                        showProgress(false);
                    }
            }
        });

    }
}
