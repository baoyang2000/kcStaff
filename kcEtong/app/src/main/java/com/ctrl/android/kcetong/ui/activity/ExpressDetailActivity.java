package com.ctrl.android.kcetong.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.ctrl.android.kcetong.R;
import com.ctrl.android.kcetong.model.ExpressDetailResult;
import com.ctrl.android.kcetong.model.ExpressRecive;
import com.ctrl.android.kcetong.model.Img;
import com.ctrl.android.kcetong.toolkit.util.AopUtils;
import com.ctrl.android.kcetong.toolkit.util.ConstantsData;
import com.ctrl.android.kcetong.toolkit.util.LLog;
import com.ctrl.android.kcetong.toolkit.util.S;
import com.ctrl.android.kcetong.toolkit.util.StrConstant;
import com.ctrl.android.kcetong.toolkit.webutils.BaseTSubscriber;
import com.ctrl.android.kcetong.toolkit.webutils.RetrofitUtil;
import com.ctrl.android.kcetong.ui.base.BaseActivity;
import com.ctrl.android.kcetong.ui.common.TestanroidpicActivity;
import com.nostra13.universalimageloader.core.assist.ImageSize;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class ExpressDetailActivity extends BaseActivity {

    @BindView(R.id.iv_express_img)//快递图片
            ImageView iv_express_img;
    @BindView(R.id.tv_express_name)//收件人姓名
            TextView  tv_express_name;
    @BindView(R.id.tv_express_tel)//收件人电话
            TextView  tv_express_tel;
    @BindView(R.id.tv_express_room)//收件房间号
            TextView  tv_express_room;
    @BindView(R.id.tv_express_company)//快递名称
            TextView  tv_express_company;
    @BindView(R.id.tv_express_number)//快递编号
            TextView  tv_express_number;
    @BindView(R.id.iv_express_qrc)//快递二维码
            ImageView iv_express_qrc;

    private String TITLE = StrConstant.EXPRESS_DETAIL_TITLE;
    private String expressId;

    private ExpressRecive expressRecive;
    private List<Img>     listImg;

    @Override
    protected void initView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_express_detail);
        ButterKnife.bind(this);
        toolbarBaseSetting(TITLE, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ExpressDetailActivity.this.finish();
            }
        });
    }

    @Override
    protected void initData() {
        expressId = getIntent().getStringExtra("expressId");
        showProgress(true);
        requestExpressDetail(expressId);
    }

    /**
     * 获取快递信息详情
     * @param expressId 快递id
     */
    public void requestExpressDetail(String expressId) {
        Map<String,String> map = new HashMap<String,String>();
        map.put(ConstantsData.METHOD,"pm.ppt.express.get");//方法名称
        map.putAll(ConstantsData.getSystemParams());

        map.put("expressId", expressId);
        String sign = AopUtils.sign(map, ConstantsData.SECRET_VALUE);
        map.put("sign",sign);
        LLog.d(map+"");
        map.remove(ConstantsData.METHOD);

        RetrofitUtil.Api().requestExpressDetail(map).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseTSubscriber<ExpressDetailResult>(this) {
                    @Override
                    public void onResponseCallback(JSONObject response, String resultCode) {
                        LLog.d(response+"");
                        showProgress(false);
                    }
                    @Override
                    public void onNext(ExpressDetailResult expressDetailResult) {
                        super.onNext(expressDetailResult);
                        if(TextUtils.equals("000",expressDetailResult.getCode())){
                            expressRecive = expressDetailResult.getData().getExpressInfo();
                            listImg = expressDetailResult.getData().getExpressPicList();

                            tv_express_name.setText(S.getStr(expressRecive.getRecipientName()));
                            tv_express_tel.setText(S.getStr(expressRecive.getMobile()));
                            tv_express_room.setText(S.getStr(expressRecive.getBuilding() + "-" + expressRecive.getUnit() + "-" + expressRecive.getRoom()));
                            tv_express_company.setText(S.getStr(expressRecive.getLogisticsCompanyName()));
                            tv_express_number.setText(S.getStr(expressRecive.getLogisticsNum()));

                            if(listImg == null || listImg.size() < 1){
//                                Arad.imageLoader
//                                        .load("aa")
//                                        .placeholder(R.drawable.default_image)
//                                        .into(mExpressImg);
                                Glide.with(ExpressDetailActivity.this).load("aa").diskCacheStrategy(DiskCacheStrategy.ALL)
                                     .error(R.drawable.default_image).centerCrop().into(iv_express_img);
                            } else {
//                                Arad.imageLoader
//                                        .load(S.getStr(listImg.get(0).getOriginalImg()).equals("") ? "aa" : S.getStr(listImg.get(0).getOriginalImg()))
//                                        .placeholder(R.drawable.default_image)
//                                        .into(mExpressImg);
                                Glide.with(ExpressDetailActivity.this).load(S.getStr(listImg.get(0).getOriginalImg()).equals("") ? "aa" : S.getStr(listImg.get(0).getOriginalImg())).diskCacheStrategy(DiskCacheStrategy.ALL)
                                     .error(R.drawable.default_image).centerCrop().into(iv_express_img);
                            }
//                            Arad.imageLoader
//                                    .load(S.getStr(expressRecive.getQrImgUrl()).equals("") ? "aa" : S.getStr(expressRecive.getQrImgUrl()))
//                                    .placeholder(R.drawable.default_image)
//                                    .into(mExpressQrc);
                            Glide.with(ExpressDetailActivity.this).load(S.getStr(expressRecive.getQrImgUrl()).equals("") ? "aa" : S.getStr(expressRecive.getQrImgUrl())).diskCacheStrategy(DiskCacheStrategy.ALL)
                                 .error(R.drawable.default_image).centerCrop().into(iv_express_qrc);
                        }
                        showProgress(false);
                    }
                    @Override
                    public void onNetError(Throwable e) {
                        super.onNetError(e);
                        showProgress(false);
                    }
                });
    }

    @OnClick({R.id.iv_express_img, R.id.iv_express_qrc})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_express_img:
                if(listImg!=null&&listImg.size()>0){
//                    scaleImage(this,iv_express_img,listImg.get(0).getOriginalImg());
                    amplificationPhoto2(1, 0, view);
                }

                break;
            case R.id.iv_express_qrc://二维码
                if(expressRecive.getQrImgUrl()!=null && !expressRecive.getQrImgUrl().equals("")){
//                    scaleImage(this,iv_express_qrc,expressRecive.getQrImgUrl());
                    amplificationPhoto1(1, 0, view);
                }

                break;
        }
    }
    private void scaleImage(Context context,ImageView view,String url){
        Intent intent =new Intent(context, TestanroidpicActivity.class);
        intent.putExtra("imageUrl", url);
        int[] location = new int[2];
        view.getLocationOnScreen(location);
        intent.putExtra("locationX", location[0]);
        intent.putExtra("locationY", location[1]);
        intent.putExtra("width", view.getWidth());
        intent.putExtra("height", view.getHeight());
        startActivity(intent);
        overridePendingTransition(0, 0);
    }
    /**
     * 放大图片(发布的图片)
     */
    private void amplificationPhoto1(int size, int position, View view) {
        List<String> imageUrlList = new ArrayList<>();
        imageUrlList.add(expressRecive.getQrImgUrl());
        ImagePagerActivity.imageSize = new ImageSize(view.getMeasuredWidth(), view.getMeasuredHeight());
        ImagePagerActivity.startImagePagerActivity(this, imageUrlList, position);
    }
    /**
     * 放大图片(返回的图片)
     */
    private void amplificationPhoto2(int size, int position, View view) {
        if (listImg != null && listImg.size() >= size) {
            List<String> imageUrlList = new ArrayList<>();
            for (Img img : listImg) {
                imageUrlList.add(img.getOriginalImg());
            }
            ImagePagerActivity.imageSize = new ImageSize(view.getMeasuredWidth(), view.getMeasuredHeight());
            ImagePagerActivity.startImagePagerActivity(this, imageUrlList, position);
        }
    }
}
