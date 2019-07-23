package com.ctrl.android.kcetong.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.ctrl.android.kcetong.R;
import com.ctrl.android.kcetong.model.AppHolder;
import com.ctrl.android.kcetong.model.ChoiceGroup;
import com.ctrl.android.kcetong.model.Img;
import com.ctrl.android.kcetong.model.ProductDetail;
import com.ctrl.android.kcetong.toolkit.Url.Url;
import com.ctrl.android.kcetong.toolkit.util.AopUtils;
import com.ctrl.android.kcetong.toolkit.util.ConstantsData;
import com.ctrl.android.kcetong.toolkit.util.LLog;
import com.ctrl.android.kcetong.toolkit.util.MessageUtils;
import com.ctrl.android.kcetong.toolkit.util.StrConstant;
import com.ctrl.android.kcetong.toolkit.util.Utils;
import com.ctrl.android.kcetong.toolkit.webutils.BaseTSubscriber;
import com.ctrl.android.kcetong.toolkit.webutils.RetrofitUtil;
import com.ctrl.android.kcetong.ui.adapter.SdfViewPaperAdapter;
import com.ctrl.android.kcetong.ui.base.BaseActivity;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class ChoiceDetailActivity extends BaseActivity implements Runnable {

    @BindView(R.id.viewpager)
    ViewPager mViewPager;
    @BindView(R.id.viewGroup)
    LinearLayout group;
    @BindView(R.id.tv_choice_price)
    TextView mChoicePrice;
    @BindView(R.id.iv_choice_buy)
    TextView mChoiceBuy;
    @BindView(R.id.tv_sysj)
    TextView tvSysj;
    @BindView(R.id.tv_tg_time)
    TextView mTgTime;
    @BindView(R.id.tv_tg_sell_num)
    TextView mTgSellNum;
    @BindView(R.id.tv_choice)
    TextView mChoice;
    @BindView(R.id.tv_choice_address)
    TextView mChoiceAddress;
    @BindView(R.id.iv_choice_phone)
    ImageView mChoicePhone;
    @BindView(R.id.wb_choice_detail)
    WebView wv;
    @BindView(R.id.sv_bus)
    ScrollView svBus;

    public static ChoiceDetailActivity instance = null;
    String id;
    String sellState;
    String overTime;
    ChoiceGroup choiceGroup = new ChoiceGroup();
    List<Img> companyPicList = new ArrayList<>();
    @BindView(R.id.toolbar_right_btn)
    TextView toolbarRightBtn;
    //    private ChoiceCommentAdapter choiceCommentAdapter;
    private SdfViewPaperAdapter mAdapter;
    private List<View> mViewPicture;
    private ImageView[] mImageViews = null;
    private ImageView imageView = null;
    private AtomicInteger what = new AtomicInteger(0);
    private boolean isContinue = true;
    private final Handler viewHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            mViewPager.setCurrentItem(msg.what);
            super.handleMessage(msg);
        }
    };

    @Override
    protected void initView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_choice_detail);
        ButterKnife.bind(this);
        toolbarBaseSetting("商品详情", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ChoiceDetailActivity.this.finish();
            }
        });
        toolbarRightBtn.setVisibility(View.VISIBLE);
        toolbarRightBtn.setText("评论");
    }

    @Override
    protected void initData() {
        instance = this;
        id = getIntent().getStringExtra("id");
        sellState = getIntent().getStringExtra("sellState");
        overTime = getIntent().getStringExtra("overTime");

        getChoiceCompany();
        AppHolder.getInstance().setCommnitu_flush(true);
    }

    private void getChoiceCompany() {
        Map<String, String> map = new HashMap();
        map.putAll(ConstantsData.getSystemParams());
        map.put(ConstantsData.METHOD, Url.productDetUrl);
        map.put("id", id);
        String sign = AopUtils.sign(map, ConstantsData.SECRET_VALUE);
        map.put("sign", sign);
        map.remove(ConstantsData.METHOD);
        LLog.d(map + "");
        RetrofitUtil.Api().productDetList(map).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new BaseTSubscriber<ProductDetail>(this) {
            @Override
            public void onResponseCallback(JSONObject response, String resultCode) {
                LLog.d("onResponseCallback: " + response);
            }

            @Override
            public void onNext(ProductDetail bean) {
                super.onNext(bean);
                if (TextUtils.equals("000", bean.getCode())) {
                    choiceGroup = bean.getData().getProductInfo();
                    companyPicList = bean.getData().getProductPicList();
                    initView();
                } else if (TextUtils.equals("002", bean.getCode())) {
                    Utils.toastError(ChoiceDetailActivity.this, StrConstant.NODATA);
                }
            }

            @Override
            public void onNetError(Throwable e) {
            }

        });
    }

    private void initView() {
        ViewGroup.LayoutParams para;
        para = mViewPager.getLayoutParams();
//        para.height = this.getWindowManager().getDefaultDisplay().getWidth()>>1;
        para.height = getResources().getDisplayMetrics().widthPixels >> 1;
        mViewPager.setLayoutParams(para);
        if (companyPicList != null && companyPicList.size() > 0) {
            mImageViews = new ImageView[companyPicList.size()];
            mViewPicture = new ArrayList<View>();
            for (int i = 0; i < companyPicList.size(); i++) {
                ImageView img = new ImageView(this);
                LinearLayout.LayoutParams image_params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                img.setLayoutParams(image_params);
                img.setScaleType(ImageView.ScaleType.FIT_XY);
                Glide.with(this).load(companyPicList.get(i).getOriginalImg()).diskCacheStrategy(DiskCacheStrategy.ALL).error(R.drawable.default_image).centerCrop().into(img);
                mViewPicture.add(img);
                imageView = new ImageView(this);
                imageView.setLayoutParams(new ViewGroup.LayoutParams(20, 20));
                imageView.setPadding(5, 5, 5, 5);
                mImageViews[i] = imageView;
                if (i == 0) {
                    mImageViews[i].setBackgroundResource(R.drawable.ic_viewpager_select);
                } else {
                    mImageViews[i].setBackgroundResource(R.drawable.ic_viewpager_noselect);
                }
                group.addView(mImageViews[i]);
            }
        } else {
            mViewPicture = new ArrayList<>();
            ImageView img = new ImageView(this);
            img.setBackgroundResource(R.drawable.default_image);
            mViewPicture.add(img);
        }
        mViewPager.setOnPageChangeListener(new GuidePageChangeListener());
        mAdapter = new SdfViewPaperAdapter(mViewPicture);
        mViewPager.setAdapter(mAdapter);
        mViewPager.setOnTouchListener(new GuideOnTouchListener());
        new Thread(this).start();
        if (choiceGroup.getSellType().equals("0")) {
            mChoicePrice.setText("￥" + choiceGroup.getSellingPrice() + "元");
        } else {
            mChoicePrice.setText(choiceGroup.getRequiredPoint() + "积分");
        }
        mTgTime.setText(overTime);
        mTgSellNum.setText("已售" + choiceGroup.getSalesVolume());
        mChoiceAddress.setText(choiceGroup.getCompanyAddress());

        wv.setWebChromeClient(new WebChromeClient());
        WebSettings settings = wv.getSettings();
        //支持javascript
        settings.setJavaScriptEnabled(true);
        settings.setJavaScriptCanOpenWindowsAutomatically(true);
        //自适应屏幕
        settings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        settings.setLoadWithOverviewMode(true);
        settings.setUseWideViewPort(true);

        settings.setDisplayZoomControls(false);
        settings.setAllowFileAccess(true); // 允许访问文件

        settings.setDefaultFontSize(50);

        settings.setLoadsImagesAutomatically(true);
        settings.setDatabaseEnabled(true);
        // 启用地理定位
        settings.setGeolocationEnabled(true);
        settings.setDomStorageEnabled(true);
        settings.setDefaultTextEncodingName("utf-8");
        settings.setCacheMode(WebSettings.LOAD_NO_CACHE);

        wv.loadDataWithBaseURL(null, HEAD + choiceGroup.getInfomation() + END, "text/html", "utf-8", null);
    }

    @OnClick({R.id.iv_choice_buy, R.id.iv_choice_phone, R.id.tv_choice, R.id.toolbar_right_btn})
    public void onViewClicked(View view) {
        Intent i = new Intent();
        switch (view.getId()) {
            case R.id.iv_choice_buy:
                if (sellState.equals("0")) {
                    MessageUtils.showShortToast(this, "团购未开始");
                } else if (sellState.equals("2")) {
                    MessageUtils.showShortToast(this, "团购已结束");
                } else {
                    i.setClass(this, OrderSubmitActivity.class);
                    i.putExtra("productId", id);
                    i.putExtra("productName", choiceGroup.getProductName());
                    i.putExtra("productPrice", choiceGroup.getSellingPrice());
                    i.putExtra("sellType", choiceGroup.getSellType());
                    i.putExtra("point", choiceGroup.getRequiredPoint());
                    startActivity(i);
                }
                break;
            case R.id.iv_choice_phone:
                if (!TextUtils.isEmpty(choiceGroup.getCompanyMobile())) {
                    Utils.dial(this, choiceGroup.getCompanyMobile());
                }
                break;
            case R.id.toolbar_right_btn:
                Intent intent = new Intent(this, CommentActivity.class);
                intent.putExtra("productId", id);
                startActivity(intent);
                break;
        }
    }

    private final class GuidePageChangeListener implements ViewPager.OnPageChangeListener {

        @Override
        public void onPageScrollStateChanged(int arg0) {

        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {

        }

        @Override
        public void onPageSelected(int arg0) {
            what.getAndSet(arg0);
            for (int i = 0; i < mImageViews.length; i++) {
                mImageViews[arg0].setBackgroundResource(R.drawable.ic_viewpager_select);
                if (arg0 != i) {
                    mImageViews[i].setBackgroundResource(R.drawable.ic_viewpager_noselect);
                }
            }
        }
    }

    private final class GuideOnTouchListener implements View.OnTouchListener {

        /* (non-Javadoc)
         * @see android.view.View.OnTouchListener#onTouch(android.view.View, android.view.MotionEvent)
         */
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                case MotionEvent.ACTION_MOVE:
                    isContinue = false;
                    break;
                case MotionEvent.ACTION_UP:
                    isContinue = true;
                    break;
                default:
                    isContinue = true;
                    break;
            }
            return false;
        }

    }

    private void whatOption() {
        what.incrementAndGet();
        if (mImageViews != null && mImageViews.length > 0) {
            if (what.get() > mImageViews.length - 1) {
                what.getAndAdd(-mImageViews.length);
            }
        }
    }

    /* (non-Javadoc)
     * @see java.lang.Runnable#run()
     */
    @Override
    public void run() {
        while (true) {
            if (isContinue) {
                try {
                    viewHandler.sendEmptyMessage(what.get());
                    whatOption();
                    Thread.sleep(5000);
                } catch (InterruptedException e) {

                }
            }
        }
    }

    public static final String HEAD = "<!DOCTYPE html>\n" +
            "<html lang=\"en\">\n" +
            "    <head>\n" +
            "        <meta charset=\"UTF-8\">\n" +
            "        <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0, user-scalable=no, minimum-scale=1.0, maximum-scale=1.0\">\n" +
            "        <meta name=\"apple-mobile-web-app-capable\" content=\"yes\">\n" +
            "        <meta name=\"apple-mobile-web-app-status-bar-style\" content=\"black\">\n" +
            "        <meta content=\"telephone=no\" name=\"format-detection\">\n" +
            "        <title>商品详情</title>\n" +
            "    <style>\n" +
//            "       body{ margin:0; border:0}\n" +
            "       img{width:98%;\n" +
            "       \tmargin-left:1%;\n" +
            "       \tmargin-right:1%;\n" +
            "       }\n" +
            "   </style>" +
            "    </head>\n" +
            "    <body>";

    public static final String END = "</body>\n" +
            "</html>";
}
