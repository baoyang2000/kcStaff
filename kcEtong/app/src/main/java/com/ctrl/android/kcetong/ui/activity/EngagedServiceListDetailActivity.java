package com.ctrl.android.kcetong.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.ctrl.android.kcetong.R;
import com.ctrl.android.kcetong.model.AppHolder;
import com.ctrl.android.kcetong.model.ServiceProductResult;
import com.ctrl.android.kcetong.toolkit.util.AopUtils;
import com.ctrl.android.kcetong.toolkit.util.ConstantsData;
import com.ctrl.android.kcetong.toolkit.util.LLog;
import com.ctrl.android.kcetong.toolkit.util.StrConstant;
import com.ctrl.android.kcetong.toolkit.util.Utils;
import com.ctrl.android.kcetong.toolkit.webutils.BaseTSubscriber;
import com.ctrl.android.kcetong.toolkit.webutils.RetrofitUtil;
import com.ctrl.android.kcetong.ui.base.BaseActivity;
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

public class EngagedServiceListDetailActivity extends BaseActivity  {

    @BindView(R.id.banner_1)
    XBanner  mXBanner;
    @BindView(R.id.tv_name)//服务名称
    TextView tv_name;
    @BindView(R.id.tv_price)//服务价格
    TextView tv_price;
    @BindView(R.id.tv_order)//预约
    TextView tv_order;
    @BindView(R.id.tv_information)//服务介绍
    TextView tv_information;

    private String TITLE = StrConstant.ENGAGED_SERVICE;
    private List<String> imgesUrl;
    private int mPage = 1;

    private String name = "";
    private String price = "";

    @Override
    protected void initView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_engaged_service_list_detail);
        ButterKnife.bind(this);
        toolbarBaseSetting(TITLE, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EngagedServiceListDetailActivity.this.finish();
            }
        });
    }
    @Override
    protected void initData() {
        imgesUrl = new ArrayList<>();
        LLog.d("我的id---"+getIntent().getStringExtra("serviceKindId"));
        Map<String,String> data = new HashMap<>();
        data = (Map<String,String>) getIntent().getSerializableExtra("data");
        if(data != null){
            name = data.get("serviceName");
            price = data.get("sellingPrice");

            tv_name.setText(data.get("serviceName"));
            tv_price.setText("￥" + data.get("sellingPrice"));
            tv_information.setText(data.get("infomation"));
            String serviceUrl = data.get("img_url");
            if(serviceUrl != null&& !"".equals(serviceUrl)){
                String str2 = serviceUrl.replace(" ", "");//去掉所用空格
                imgesUrl = Arrays.asList(str2.split(","));
            }
            mXBanner.setData(imgesUrl,null);//第二个参数为提示文字资源集合
            mXBanner.setmAdapter(new XBanner.XBannerAdapter() {
                @Override
                public void loadBanner(XBanner banner, View view, int position) {
                    Glide.with(EngagedServiceListDetailActivity.this).load(imgesUrl.get(position)).into((ImageView) view);
                }
            });
        }

//        requestServiceInfo(getIntent().getStringExtra("id"), mPage + "");

    }
    /**
     * 获取特约服务详情
     * @param serviceProductId 当前选中的服务类别Id
     * @param currentPage 当前页码
     * */
    public void requestServiceInfo(String serviceProductId,String currentPage){
        Map<String,String> map = new HashMap<>();
        map.put(ConstantsData.METHOD,"pm.ppt.serviceProduct.list");//方法名称
        map.putAll(ConstantsData.getSystemParams());

        map.put("serviceKindId",getIntent().getStringExtra("serviceKindId"));
        map.put("serviceProductId",serviceProductId);
        map.put("communityId", AppHolder.getInstance().getHouse().getCommunityId());
        map.put("currentPage",currentPage);
        map.put("rowCountPerPage",ConstantsData.ROW_COUNT_PER_PAGE+"");

        String sign = AopUtils.sign(map, ConstantsData.SECRET_VALUE);
        map.put("sign",sign);
        LLog.d(map + "");
        map.remove(ConstantsData.METHOD);
        RetrofitUtil.Api().requestServiceList(map).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new BaseTSubscriber<ServiceProductResult>(this) {
                        @Override
                        public void onResponseCallback(JSONObject response, String resultCode) {
                            LLog.d(response+"");
                        }

                        @Override
                        public void onNext(ServiceProductResult serviceProductResult) {
                            super.onNext(serviceProductResult);
                            if(TextUtils.equals("000",serviceProductResult.getCode())){

                                name = serviceProductResult.getData().getServiceProductList().get(0).getServiceName();
                                price = serviceProductResult.getData().getServiceProductList().get(0).getSellingPrice();

                                tv_name.setText(serviceProductResult.getData().getServiceProductList().get(0).getServiceName());
                                tv_price.setText("￥"+serviceProductResult.getData().getServiceProductList().get(0).getSellingPrice());
                                tv_information.setText(serviceProductResult.getData().getServiceProductList().get(0).getInfomation());
                                String serviceUrl = serviceProductResult.getData().getServiceProductList().get(0).getServiceUrl();
                                if(serviceUrl != null&& !"".equals(serviceUrl)){
                                    String str2 = serviceUrl.replace(" ", "");//去掉所用空格
                                    imgesUrl = Arrays.asList(str2.split(","));
                                }
                                mXBanner.setData(imgesUrl,null);//第二个参数为提示文字资源集合
                                mXBanner.setmAdapter(new XBanner.XBannerAdapter() {
                                    @Override
                                    public void loadBanner(XBanner banner, View view, int position) {
                                        Glide.with(EngagedServiceListDetailActivity.this).load(imgesUrl.get(position)).into((ImageView) view);
                                    }
                                });
                            }
                        }
                    });
    }
    @OnClick(R.id.tv_order)
    public void onClick() {
        if("0".equals(AppHolder.getInstance().getMemberInfo().getSupers())){
            Intent i =new Intent(EngagedServiceListDetailActivity.this,EngagedServiceListOrderActivity.class);
            i.putExtra("name",name);
            i.putExtra("price",price);
            i.putExtra("id",getIntent().getStringExtra("id"));
            i.putExtra("serviceKindId",getIntent().getStringExtra("serviceKindId"));
            startActivity(i);
        }else if("1".equals(AppHolder.getInstance().getMemberInfo().getSupers())
                 || "2".equals(AppHolder.getInstance().getMemberInfo().getSupers())){
            Utils.showShortToast(EngagedServiceListDetailActivity.this, getString(R.string.manager_cannot));
        }

    }
}
