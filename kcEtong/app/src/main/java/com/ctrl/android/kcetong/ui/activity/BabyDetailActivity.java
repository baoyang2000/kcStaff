package com.ctrl.android.kcetong.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.ctrl.android.kcetong.R;
import com.ctrl.android.kcetong.model.GoodPic;
import com.ctrl.android.kcetong.model.UsedGoodInfo;
import com.ctrl.android.kcetong.model.UserGoodInfoResult;
import com.ctrl.android.kcetong.toolkit.util.AopUtils;
import com.ctrl.android.kcetong.toolkit.util.ConstantsData;
import com.ctrl.android.kcetong.toolkit.util.LLog;
import com.ctrl.android.kcetong.toolkit.util.MessageUtils;
import com.ctrl.android.kcetong.toolkit.util.S;
import com.ctrl.android.kcetong.toolkit.util.StrConstant;
import com.ctrl.android.kcetong.toolkit.util.TimeUtil;
import com.ctrl.android.kcetong.toolkit.util.Utils;
import com.ctrl.android.kcetong.toolkit.webutils.BaseTSubscriber;
import com.ctrl.android.kcetong.toolkit.webutils.RetrofitUtil;
import com.ctrl.android.kcetong.ui.base.BaseActivity;
import com.nostra13.universalimageloader.core.assist.ImageSize;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.ResponseBody;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class BabyDetailActivity extends BaseActivity {
    @BindView(R.id.iv_baby_detail_tel)
    ImageView    iv_baby_detail_tel;
    @BindView(R.id.tv_contact_telephone_right)
    TextView     tv_contact_telephone_right;
    @BindView(R.id.tv_detail_to_buy_name)
    TextView     tv_detail_to_buy_name;
    @BindView(R.id.tv_detail_to_buy_time)
    TextView     tv_detail_to_buy_time;
    @BindView(R.id.tv_detail_to_buy_baobei_title_right)
    TextView     tv_detail_to_buy_baobei_title_right;
    @BindView(R.id.tv_detail_to_buy_baobei_price_right)
    TextView     tv_detail_to_buy_baobei_price_right;
    @BindView(R.id.tv_detail_to_buy_baobei_detail_right)
    TextView     tv_detail_to_buy_baobei_detail_right;
    @BindView(R.id.tv_detail_to_buy_baobei_location_right)
    TextView     tv_detail_to_buy_baobei_location_right;
    @BindView(R.id.tv_contact_right)
    TextView     tv_contact_right;
    @BindView(R.id.iv_want_transfer_baobei_image01)
    ImageView    iv_want_transfer_baobei_image01;
    @BindView(R.id.iv_want_transfer_baobei_image02)
    ImageView    iv_want_transfer_baobei_image02;
    @BindView(R.id.iv_want_transfer_baobei_image03)
    ImageView    iv_want_transfer_baobei_image03;
    @BindView(R.id.ll_image)
    LinearLayout ll_image;
    @BindView(R.id.tv_baobei_image)
    TextView     tv_baobei_image;
    @BindView(R.id.tv_baby_detail_delete)
    TextView     tv_baby_detail_delete;

    private Intent intent;
    private String usedGoodsId;
    private UsedGoodInfo mUsedGoodInfo;
    private List<GoodPic> goodPicList;
    private int position;

    @Override
    protected void initView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_baby_detail);
        ButterKnife.bind(this);
        toolbarBaseSetting("转让", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BabyDetailActivity.this.finish();
            }
        });
        intent=getIntent();
    }

    @Override
    protected void initData() {
        goodPicList = new ArrayList<>();
        usedGoodsId=getIntent().getStringExtra("usedGoodsId");
        requestGoodsGet(usedGoodsId);
        //我的转让信息
        if(getIntent().getFlags() == StrConstant.MY_BABY_DETAIL){
            tv_baby_detail_delete.setVisibility(View.VISIBLE);
            position = getIntent().getIntExtra("position",-1);
            Log.i("Tag", "Tag1111" + position);
        }
    }

    @OnClick({R.id.iv_want_transfer_baobei_image01, R.id.iv_want_transfer_baobei_image02, R.id.iv_want_transfer_baobei_image03, R.id.iv_baby_detail_tel, R.id.tv_baby_detail_delete})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_want_transfer_baobei_image01:
                amplificationPhoto2(1, 0, view);
                break;
            case R.id.iv_want_transfer_baobei_image02:
                amplificationPhoto2(2, 1, view);
                break;
            case R.id.iv_want_transfer_baobei_image03:
                amplificationPhoto2(3, 2, view);
                break;
            case R.id.iv_baby_detail_tel:
                Utils.dial(BabyDetailActivity.this,tv_contact_telephone_right.getText().toString());
                break;
            case R.id.tv_baby_detail_delete:
                showProgress(true);
                requestGoodsDelete(usedGoodsId);
                break;
        }
    }
    /**
     * 放大图片(返回的图片)
     */
    private void amplificationPhoto2(int size, int position, View view) {
        if (goodPicList != null && goodPicList.size() >= size) {
            List<String> imageUrlList = new ArrayList<>();
            for (GoodPic img : goodPicList) {
                imageUrlList.add(img.getOriginalImg());
            }
            ImagePagerActivity.imageSize = new ImageSize(view.getMeasuredWidth(), view.getMeasuredHeight());
            ImagePagerActivity.startImagePagerActivity(this, imageUrlList, position);
        }
    }
    /**
     * 获取二手交易详情
     * @param usedGoodId 二手物品id
     */
    public void requestGoodsGet(String usedGoodId) {
        Map<String,String> params = new HashMap<>();
        params.put(ConstantsData.METHOD,"pm.ppt.usedGoods.get");
        params.putAll(ConstantsData.getSystemParams());

        params.put("usedGoodId", usedGoodId);

        String sign = AopUtils.sign(params, ConstantsData.SECRET_VALUE);
        params.put("sign",sign);
        LLog.d(params+"");
        params.remove(ConstantsData.METHOD);

        RetrofitUtil.Api().requestGoodsGet(params).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseTSubscriber<UserGoodInfoResult>(this) {
                    @Override
                    public void onResponseCallback(JSONObject response, String resultCode) {
                        LLog.d(response+"");
                    }
                    @Override
                    public void onNext(UserGoodInfoResult userGoodInfoResult) {
                        super.onNext(userGoodInfoResult);
                        if(TextUtils.equals("000",userGoodInfoResult.getCode())){
                            mUsedGoodInfo = userGoodInfoResult.getData().getUsedGoodInfo();
                            List<GoodPic> goodPic = userGoodInfoResult.getData().getUsedGoodPicList();
                            if(null != goodPic){
                                goodPicList.addAll(goodPic);
                            }
                            tv_detail_to_buy_name.setText(mUsedGoodInfo.getContactName());
                            String releastTime = getIntent().getStringExtra("releaseTime");

                            tv_detail_to_buy_time.setText("发布时间：" + TimeUtil.date(Long.parseLong(releastTime)));
                            tv_detail_to_buy_baobei_title_right.setText(mUsedGoodInfo.getTitle());

                            tv_detail_to_buy_baobei_price_right.setText("￥"+Utils.get2Double(mUsedGoodInfo.getSellingPrice()));
                            tv_detail_to_buy_baobei_detail_right.setText(mUsedGoodInfo.getContent());
                            tv_detail_to_buy_baobei_location_right.setText(mUsedGoodInfo.getCommunityName() + " " +mUsedGoodInfo.getBuilding()+"-"+mUsedGoodInfo.getUnit()+"-"+mUsedGoodInfo.getRoom());
                            tv_contact_right.setText(mUsedGoodInfo.getContactName());
                            tv_contact_telephone_right.setText(mUsedGoodInfo.getContactMobile());
                            if(goodPicList.size()==0){
                                ll_image.setVisibility(View.GONE);
                                tv_baobei_image.setVisibility(View.GONE);
                            }
                            if(goodPicList.size()==1) {
//                                Arad.imageLoader.load(S.getStr(list.get(0).getOriginalImg()))
//                                                .placeholder(R.drawable.default_image)
//                                                .into(iv_want_transfer_baobei_image01);
                                Glide.with(BabyDetailActivity.this).load(S.getStr(goodPicList.get(0).getOriginalImg()))
                                        .diskCacheStrategy(DiskCacheStrategy.ALL).error(R.drawable.default_image).centerCrop()
                                        .into(iv_want_transfer_baobei_image01);
                                iv_want_transfer_baobei_image02.setVisibility(View.INVISIBLE);
                                iv_want_transfer_baobei_image03.setVisibility(View.INVISIBLE);
                            }
                            if(goodPicList.size()==2) {
//                                Arad.imageLoader.load(S.getStr(list.get(0).getOriginalImg()))
//                                                .placeholder(R.drawable.default_image)
//                                                .into(iv_want_transfer_baobei_image01);
//                                Arad.imageLoader.load(S.getStr(list.get(1).getOriginalImg()))
//                                                .placeholder(R.drawable.default_image)
//                                                .into(iv_want_transfer_baobei_image02);
                                Glide.with(BabyDetailActivity.this).load(S.getStr(goodPicList.get(0).getOriginalImg()))
                                     .diskCacheStrategy(DiskCacheStrategy.ALL).error(R.drawable.default_image).centerCrop()
                                     .into(iv_want_transfer_baobei_image01);
                                Glide.with(BabyDetailActivity.this).load(S.getStr(goodPicList.get(1).getOriginalImg()))
                                     .diskCacheStrategy(DiskCacheStrategy.ALL).error(R.drawable.default_image).centerCrop()
                                     .into(iv_want_transfer_baobei_image02);
                                iv_want_transfer_baobei_image03.setVisibility(View.INVISIBLE);
                            }if(goodPicList.size()==3){
//                                Arad.imageLoader.load(S.getStr(list.get(0).getOriginalImg()))
//                                                .placeholder(R.drawable.default_image)
//                                                .into(iv_want_transfer_baobei_image01);
//                                Arad.imageLoader.load(S.getStr(list.get(1).getOriginalImg()))
//                                                .placeholder(R.drawable.default_image)
//                                                .into(iv_want_transfer_baobei_image02);
//                                Arad.imageLoader.load(S.getStr(list.get(2).getOriginalImg()))
//                                                .placeholder(R.drawable.default_image)
//                                                .into(iv_want_transfer_baobei_image03);

                                Glide.with(BabyDetailActivity.this).load(S.getStr(goodPicList.get(0).getOriginalImg()))
                                     .diskCacheStrategy(DiskCacheStrategy.ALL).error(R.drawable.default_image).centerCrop()
                                     .into(iv_want_transfer_baobei_image01);
                                Glide.with(BabyDetailActivity.this).load(S.getStr(goodPicList.get(1).getOriginalImg()))
                                     .diskCacheStrategy(DiskCacheStrategy.ALL).error(R.drawable.default_image).centerCrop()
                                     .into(iv_want_transfer_baobei_image02);
                                Glide.with(BabyDetailActivity.this).load(S.getStr(goodPicList.get(2).getOriginalImg()))
                                     .diskCacheStrategy(DiskCacheStrategy.ALL).error(R.drawable.default_image).centerCrop()
                                     .into(iv_want_transfer_baobei_image03);
                            }
                        }
                    }
                    @Override
                    public void onNetError(Throwable e) {
                        super.onNetError(e);
                    }
                });
    }

    /**
     * 删除二手交易信息
     * @param usedGoodId 二手物品id
     */
    public void requestGoodsDelete(String usedGoodId) {
        Map<String,String> params = new HashMap<String,String>();
        params.put("method","pm.ppt.usedGoods.delete");
        params.putAll(ConstantsData.getSystemParams());

        params.put("usedGoodId", usedGoodId);
        String sign = AopUtils.sign(params, ConstantsData.SECRET_VALUE);
        params.put("sign",sign);
        LLog.d(params+"");
        params.remove(ConstantsData.METHOD);

        RetrofitUtil.Api().requestGoodsDelete(params).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseTSubscriber<ResponseBody>(this) {
                    @Override
                    public void onResponseCallback(JSONObject response, String resultCode) {
                        LLog.d(response+"");
                        showProgress(false);
                        try {
                            if(TextUtils.equals("000",response.getString("code"))){
                                MessageUtils.showShortToast(BabyDetailActivity.this,"信息删除成功");
                                if(getIntent().getFlags()==StrConstant.MY_BABY_DETAIL){
                                    intent.putExtra("position", position);
                                    setResult(1009,intent);
                                    finish();
                                }
                                finish();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    @Override
                    public void onNetError(Throwable e) {
                        super.onNetError(e);
                        showProgress(false);
                    }
                });
    }
}
