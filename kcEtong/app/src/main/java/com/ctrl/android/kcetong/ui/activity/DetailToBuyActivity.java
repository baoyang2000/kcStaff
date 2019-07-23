package com.ctrl.android.kcetong.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.ctrl.android.kcetong.R;
import com.ctrl.android.kcetong.model.UsedGoodInfo;
import com.ctrl.android.kcetong.model.UserGoodInfoResult;
import com.ctrl.android.kcetong.toolkit.util.AopUtils;
import com.ctrl.android.kcetong.toolkit.util.ConstantsData;
import com.ctrl.android.kcetong.toolkit.util.LLog;
import com.ctrl.android.kcetong.toolkit.util.MessageUtils;
import com.ctrl.android.kcetong.toolkit.util.StrConstant;
import com.ctrl.android.kcetong.toolkit.util.TimeUtil;
import com.ctrl.android.kcetong.toolkit.util.Utils;
import com.ctrl.android.kcetong.toolkit.webutils.BaseTSubscriber;
import com.ctrl.android.kcetong.toolkit.webutils.RetrofitUtil;
import com.ctrl.android.kcetong.ui.base.BaseActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.ResponseBody;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class DetailToBuyActivity extends BaseActivity {
    @BindView(R.id.iv_detail_to_buy_tel)
    ImageView iv_detail_to_buy_tel;
    @BindView(R.id.et_contact_telephone)
    TextView  et_contact_telephone;
    @BindView(R.id.tv_detail_to_buy_name)
    TextView  tv_detail_to_buy_name;
    @BindView(R.id.tv_detail_to_buy_time)
    TextView  tv_detail_to_buy_time;
    @BindView(R.id.tv_detail_to_buy_baobei_title_right)
    TextView  tv_detail_to_buy_baobei_title_right;
    @BindView(R.id.tv_detail_to_buy_baobei_price_right)
    TextView  tv_detail_to_buy_baobei_price_right;
    @BindView(R.id.tv_detail_to_buy_baobei_detail_right)
    TextView  tv_detail_to_buy_baobei_detail_right;
    @BindView(R.id.tv_detail_to_buy_baobei_location_right)
    TextView  tv_detail_to_buy_baobei_location_right;
    @BindView(R.id.et_contact)
    TextView  et_contact;
    @BindView(R.id.tv_baby_detail_delete)
    TextView  tv_baby_detail_delete;

    private UsedGoodInfo mUsedGoodInfo;
    private String       usedGoodsId;
    private int          position;
    private Intent       intent;

    @Override
    protected void initView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_detail_to_buy);
        ButterKnife.bind(this);
        toolbarBaseSetting("求购", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DetailToBuyActivity.this.finish();
            }
        });
    }

    @Override
    protected void initData() {
        usedGoodsId=getIntent().getStringExtra("usedGoodsId");

        requestGoodsGet(usedGoodsId);
        intent=getIntent();
        if(getIntent().getFlags() == StrConstant.MY_BABY_BUY_DETAIL){
            tv_baby_detail_delete.setVisibility(View.VISIBLE);
            position=getIntent().getIntExtra("position",-1);
            Log.i("Tag", "Tag1008" + position);
        }
    }

    @OnClick({R.id.iv_detail_to_buy_tel, R.id.tv_baby_detail_delete})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_detail_to_buy_tel:
                Utils.dial(DetailToBuyActivity.this,et_contact_telephone.getText().toString());
                break;
            case R.id.tv_baby_detail_delete:
                showProgress(true);
                requestGoodsDelete(usedGoodsId);
                break;
        }
    }
    /**
     * 获取二手交易详情
     * @param usedGoodId 二手物品id
     */
    public void requestGoodsGet(String usedGoodId) {
        Map<String,String> params = new HashMap<>();
        params.put("method","pm.ppt.usedGoods.get");
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
//                                List<GoodPic> goodPic = userGoodInfoResult.getData().getUsedGoodPicList();
//                                if(null != goodPic){
//                                    .addAll(goodPic);
//                                }
                                tv_detail_to_buy_name.setText(mUsedGoodInfo.getContactName());
                                tv_detail_to_buy_baobei_detail_right.setText(mUsedGoodInfo.getContent());
                                tv_detail_to_buy_baobei_location_right.setText(mUsedGoodInfo.getCommunityName() + " " +mUsedGoodInfo.getBuilding()+"-"+mUsedGoodInfo.getUnit()+"-"+mUsedGoodInfo.getRoom());
                                String releastTime = getIntent().getStringExtra("releaseTime");
                                tv_detail_to_buy_time.setText("发布时间：" + TimeUtil.date(Long.parseLong(releastTime)));
                                tv_detail_to_buy_baobei_title_right.setText(mUsedGoodInfo.getTitle());
                                tv_detail_to_buy_baobei_price_right.setText("￥" + Utils.get2Double(mUsedGoodInfo.getSellingPrice()));
                                et_contact.setText(mUsedGoodInfo.getContactName());
                                et_contact_telephone.setText(mUsedGoodInfo.getContactMobile());
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
        Map<String,String> params = new HashMap<>();
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
                                    MessageUtils.showShortToast(DetailToBuyActivity.this,"信息删除成功");
                                    if(getIntent().getFlags()==StrConstant.MY_BABY_BUY_DETAIL){
                                        intent.putExtra("position",position);
                                        //  Log.i("Tag", "Tag1001" + position);
                                        setResult(1003, intent);
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
