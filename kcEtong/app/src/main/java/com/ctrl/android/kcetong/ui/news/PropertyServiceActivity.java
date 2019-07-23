package com.ctrl.android.kcetong.ui.news;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.ctrl.android.kcetong.R;
import com.ctrl.android.kcetong.listener.HintDialogListener;
import com.ctrl.android.kcetong.model.Ad;
import com.ctrl.android.kcetong.model.AppHolder;
import com.ctrl.android.kcetong.toolkit.util.AopUtils;
import com.ctrl.android.kcetong.toolkit.util.ConstantsData;
import com.ctrl.android.kcetong.toolkit.util.LLog;
import com.ctrl.android.kcetong.toolkit.util.S;
import com.ctrl.android.kcetong.toolkit.util.Utils;
import com.ctrl.android.kcetong.toolkit.webutils.BaseTSubscriber;
import com.ctrl.android.kcetong.toolkit.webutils.RetrofitUtil;
import com.ctrl.android.kcetong.ui.activity.CommunityFinanceActivity;
import com.ctrl.android.kcetong.ui.activity.CommunityHomeActivity;
import com.ctrl.android.kcetong.ui.activity.FamillyHotLineListActivity;
import com.ctrl.android.kcetong.ui.activity.ForumActivity;
import com.ctrl.android.kcetong.ui.activity.HouseListActivity;
import com.ctrl.android.kcetong.ui.activity.NoticeListActivity;
import com.ctrl.android.kcetong.ui.activity.PropertyPaymentActivity;
import com.ctrl.android.kcetong.ui.activity.SurveyListActivity;
import com.ctrl.android.kcetong.ui.base.BaseActivity;
import com.ctrl.android.kcetong.ui.dialog.HintMessageDialog;
import com.stx.xhb.xbanner.XBanner;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class PropertyServiceActivity extends BaseActivity {

    @BindView(R.id.banner_property_service)
    XBanner      bannerPropertyService;
    @BindView(R.id.complaint_repair)
    LinearLayout complaintRepair;
    @BindView(R.id.community_act)
    LinearLayout communityAct;
    @BindView(R.id.property_notice)
    LinearLayout propertyNotice;
    @BindView(R.id.community_survey)
    LinearLayout communitySurvey;
    @BindView(R.id.payment)
    LinearLayout payment;
    @BindView(R.id.community_bbs)
    LinearLayout communityBbs;
    @BindView(R.id.banner_property_service_bottom)
    XBanner banner_property_service_bottom;

    @Override
    protected void initView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_property_service);
        ButterKnife.bind(this);
        initBanner("PPT_HOME_BOTTOM");
    }

    @Override
    protected void initData() {
        banner_property_service_bottom.setOnItemClickListener(new XBanner.OnItemClickListener() {
            @Override
            public void onItemClick(XBanner xBanner, final int i) {
                if (targetUrl_bottom == null) return;
                if (targetUrl_bottom.size() > i)
                    if (Patterns.WEB_URL.matcher(targetUrl_bottom.get(i)).matches()) {
                        //符合标准
                        Intent intent = new Intent(PropertyServiceActivity.this, CommunityFinanceActivity.class);
                        intent.putExtra("url", targetUrl_bottom.get(i));
                        startActivity(intent);
                    }
            }
        });
    }

    @OnClick({R.id.complaint_repair, R.id.community_act, R.id.property_notice, R.id.community_survey, R.id.payment, R.id.community_bbs})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.complaint_repair:
                confrim(FamillyHotLineListActivity.class);
                break;
            case R.id.community_act:
                confrim(CommunityHomeActivity.class);
                break;
            case R.id.property_notice:
                confrim(NoticeListActivity.class);
                break;
            case R.id.community_survey:
                confrim(SurveyListActivity.class);
                break;
            case R.id.payment:
                confrim(PropertyPaymentActivity.class);
//                Utils.toastError(this, StrConstant.NOT_YET_OPENED);
                break;
            case R.id.community_bbs://论坛
                confrim(ForumActivity.class);
                break;
        }
    }

    private void confrim(Class activity){

        if (AppHolder.getInstance().getProprietor() == null || S.isNull(AppHolder.getInstance().getProprietor().getProprietorId())) {//未认证
            showProperDialog(getResources().getString(R.string.not_found_owner_information));
        } else {
            if (AppHolder.getInstance().getHouse().getProprietorId() == null) {
                Utils.toastError(this, getString(R.string.please_));
            } else {
                startActivity(new Intent(this, activity));
            }
        }
    }

    private void showProperDialog(String info) {
        final HintMessageDialog hintDialog = new HintMessageDialog(this);
        hintDialog.showHintDialog("提示", info, new HintDialogListener() {
            @Override
            public void submitListener() {
                hintDialog.dismiss();
                Intent intent = new Intent(PropertyServiceActivity.this, HouseListActivity.class);
                startActivity(intent);
            }

            @Override
            public void cancelListener() {
                hintDialog.dismiss();
            }
        });
    }
    private List<String> imgesUrl = new ArrayList<>();//轮播图顶部的图片
    private List<String> targetUrl_bottom        = new ArrayList<>();//轮播图顶部的地址
    //请求轮播图顶部
    private void initBanner(String location) {
        showProgress(true);
        Map<String, String> params = new HashMap<>();
        params.put(ConstantsData.METHOD, "pm.ppt.advertisement.list");//方法名称
        params.put("location", location);
        params.putAll(ConstantsData.getSystemParams());
        String sign = AopUtils.sign(params, ConstantsData.SECRET_VALUE);
        params.put("sign", sign);
        params.remove(ConstantsData.METHOD);
        LLog.d(params + "");
        RetrofitUtil.Api().Banner(params).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new BaseTSubscriber<Ad>(this) {
            @Override
            public void onResponseCallback(JSONObject response, String resultCode) {
                LLog.d("onResponseCallback: " + response);
                showProgress(false);
            }

            @Override
            public void onNext(Ad ad) {
                if (ad.getCode().equals("000")) {
                    String str = ad.getData().getAdvertisementList().get(0).getImgUrl().replace(" ", "");
                    imgesUrl = Arrays.asList(str.split(","));
                    String str2 = ad.getData().getAdvertisementList().get(0).getTargetUrl().replace(" ", "");//去掉所用空格
                    targetUrl_bottom = Arrays.asList(str2.split(","));
                    LLog.d(imgesUrl + "126");
                    banner_property_service_bottom.setData(imgesUrl, null);//第二个参数为提示文字资源集合
                    banner_property_service_bottom.setmAdapter(new XBanner.XBannerAdapter() {
                        @Override
                        public void loadBanner(XBanner banner1, View view, int position) {
                            Glide.with(PropertyServiceActivity.this).load(imgesUrl.get(position)).into((ImageView) view);
                        }
                    });
                }
                showProgress(false);
            }

            @Override
            public void onError(Throwable e) {
                super.onError(e);
                showProgress(false);
            }
        });

    }
}
