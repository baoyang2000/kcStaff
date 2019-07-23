package com.ctrl.android.kcetong.ui.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.ctrl.android.kcetong.R;
import com.ctrl.android.kcetong.model.Surround;
import com.ctrl.android.kcetong.toolkit.util.S;
import com.ctrl.android.kcetong.toolkit.util.Utils;
import com.ctrl.android.kcetong.ui.adapter.ServiceDetAdapter;
import com.ctrl.android.kcetong.ui.base.BaseActivity;
import com.stx.xhb.xbanner.XBanner;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ServiceDetailActivity extends BaseActivity {

    @BindView(R.id.banner)
    XBanner banner;
    @BindView(R.id.web_view)
    WebView webView;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.tv_company)
    TextView tvCompany;
    @BindView(R.id.tv_phone)
    TextView tvPhone;
    @BindView(R.id.tv_address)
    TextView tvAddress;
    @BindView(R.id.tv_online)
    TextView tvOnline;
    @BindView(R.id.tv_jiandu)
    TextView tvJiandu;

    private Surround.DataBean.ServiceListBean bean;
    private List<String> imageUrl = new ArrayList<>();
    private WebSettings settings;
    private ServiceDetAdapter adapter;

    @Override
    protected void initView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_service_detail);
        ButterKnife.bind(this);
        setBannerHeight(banner, 45, 100);//动态设置轮播图高度
        Intent intent = getIntent();
        if (intent.getSerializableExtra("data") != null) {
            bean = (Surround.DataBean.ServiceListBean) intent.getSerializableExtra("data");
        }
        toolbarBaseSetting(bean.getServiceName(), new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ServiceDetailActivity.this.finish();
            }
        });
        initWebSetting();
        initRecycler();
    }

    private void initRecycler() {
        GridLayoutManager layoutManager = new GridLayoutManager(this, 3);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new ServiceDetAdapter(this);
        recyclerView.setAdapter(adapter);
    }

    private void initWebSetting() {
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
        // 设置默认字体大小
        settings.setDefaultFontSize(14);
    }

    @Override
    protected void initData() {
        if (bean.getBannerList() != null && bean.getBannerList().size() > 0) {
            for (Surround.DataBean.ServiceListBean.BannerListBean url : bean.getBannerList()) {
                imageUrl.add(url.getPicture());
            }
        }
        banner.setData(imageUrl, null);//第二个参数为提示文字资源集合
        banner.setmAdapter(new XBanner.XBannerAdapter() {
            @Override
            public void loadBanner(XBanner banner1, View view, int position) {
                Glide.with(ServiceDetailActivity.this).load(imageUrl.get(position)).into((ImageView) view);
            }
        });

        openWebView(webView, S.getStr(bean.getContent()));
        if (bean.getBusinessList() != null && bean.getBusinessList().size() > 0) {
            adapter.setDataList(bean.getBusinessList());
            adapter.notifyDataSetChanged();
        }

        tvCompany.setText(bean.getCompany() == null ? "" : bean.getCompany());
        tvPhone.setText("电话：" + (bean.getPhone() == null ? "" : bean.getPhone()));
        tvAddress.setText("地址：" + (bean.getAddress() == null ? "" : bean.getAddress()));
        tvOnline.setText("网址：" + (bean.getUrlAddress() == null ? "" : bean.getUrlAddress()));
        tvJiandu.setText("质量监督电话：" + (bean.getSupervisionPhone() == null ? "" : bean.getSupervisionPhone()));

        tvPhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                Uri data = Uri.parse("tel:" + (bean.getPhone() == null ? "" : bean.getPhone()));
                intent.setData(data);
                startActivity(intent);
            }
        });

        tvOnline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ServiceDetailActivity.this, CommunityFinanceActivity.class);
                intent.putExtra("url", "http://" + (bean.getUrlAddress() == null ? "" : bean.getUrlAddress()));
                if (bean.getUrlAddress() == null) {
                    return;
                }
                startActivity(intent);
            }
        });

        tvJiandu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                Uri data = Uri.parse("tel:" + (bean.getSupervisionPhone() == null ? "" : bean.getSupervisionPhone()));
                intent.setData(data);
                startActivity(intent);
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        banner.startAutoPlay();
    }

    @Override
    public void onPause() {
        super.onPause();
        banner.stopAutoPlay();
    }

    private void openWebView(WebView web, String urlDate) {
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

    //动态设置轮播图高度
    private void setBannerHeight(XBanner xBanner, int arg1, int arg2) {
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) xBanner.getLayoutParams();
        int width = Utils.getScreenWidth(this);
        params.height = width * arg1 / arg2;
        xBanner.setLayoutParams(params);
    }
}
