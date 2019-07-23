package com.ctrl.android.kcetong.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.ctrl.android.kcetong.R;
import com.ctrl.android.kcetong.model.OrderBean;
import com.ctrl.android.kcetong.toolkit.Url.Url;
import com.ctrl.android.kcetong.toolkit.util.AopUtils;
import com.ctrl.android.kcetong.toolkit.util.ConstantsData;
import com.ctrl.android.kcetong.toolkit.util.LLog;
import com.ctrl.android.kcetong.toolkit.util.Utils;
import com.ctrl.android.kcetong.toolkit.webutils.BaseTSubscriber;
import com.ctrl.android.kcetong.toolkit.webutils.RetrofitUtil;
import com.ctrl.android.kcetong.ui.base.BaseActivity;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.ResponseBody;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class MyOrderDetail extends BaseActivity {
    @BindView(R.id.iv_my_order_img)
    ImageView    myOrderImg;
    @BindView(R.id.tv_order_buy_name)
    TextView     orderBuyName;
    @BindView(R.id.tv_order_buy_total)
    TextView     orderBuyTotal;
    @BindView(R.id.tv_order_buy_count)
    TextView     orderBuyCount;
    @BindView(R.id.tv_order_number)
    TextView     orderNumber;
    @BindView(R.id.tv_order_buy_time)
    TextView     orderBuyTime;
    @BindView(R.id.tv_order_buyer_address)
    TextView     orderBuyAddress;
    @BindView(R.id.tv_order_buyer_name)
    TextView     orderBuyerName;
    @BindView(R.id.tv_order_buyer_mobile)
    TextView     orderBuyMobile;
    @BindView(R.id.tv_order_pay_type)
    TextView     orderPayType;
    @BindView(R.id.tv_order_detail_btn)
    TextView     orderDetailBtn;
    @BindView(R.id.ll_order_detail)
    LinearLayout llOrderDetail;
    @BindView(R.id.tv_order_evaluate)
    TextView     orderEvaluate;
    @BindView(R.id.tv_order_evaluate_content)
    TextView     orderEvaluateContent;
    @BindView(R.id.ll_img)
    LinearLayout llImg;
    @BindView(R.id.iv_comment_one)
    ImageView    commentOne;
    @BindView(R.id.iv_comment_two)
    ImageView    commentTwo;
    @BindView(R.id.iv_comment_three)
    ImageView    commentThree;

    String mType;
    private OrderBean.DataBean.OrderListBean order;
    @Override
    protected void initView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_my_order_detail);
        ButterKnife.bind(this);
        toolbarBaseSetting("订单详情", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyOrderDetail.this.finish();
            }
        });
        mType = getIntent().getStringExtra("type");
        order = (OrderBean.DataBean.OrderListBean) getIntent().getSerializableExtra("order");
    }

    @Override
    protected void initData() {
        if(order.getListPicUrl()!=null && !order.getListPicUrl().equals("")){
            Glide.with(this).load(order.getListPicUrl())
                 .diskCacheStrategy(DiskCacheStrategy.ALL).error(R.drawable.default_image).centerCrop()
                 .into(myOrderImg);
        }
        orderBuyName.setText(order.getProductName());
        if(order.getSellType().equals("0")){
            orderBuyTotal.setText(order.getTotalCost()+"元");
        }else{
            orderBuyTotal.setText(order.getPointsDeductions()+"积分");
        }
        orderBuyCount.setText("数量"+order.getNums());
        orderNumber.setText(order.getOrderNum());
        orderBuyTime.setText(Utils.getDataFormatString(order.getCreateTime(), "yyyy-MM-dd HH:mm"));
        orderBuyAddress.setText(order.getAddress());
        orderBuyerName.setText(order.getReceiverName());
        orderBuyMobile.setText(order.getReceiverMobile());
        switch (order.getPayType()) {
            case "1":
                orderPayType.setText("支付宝");
                break;
            case "2":
                orderPayType.setText("微信");
                break;
            case "3":
                orderPayType.setText("银联卡");
                break;
            case "4":
                orderPayType.setText("积分支付");
                break;
            default:
                orderPayType.setText("无");
                break;
        }
        if("1".equals(mType)){
            orderDetailBtn.setVisibility(View.VISIBLE);
            orderDetailBtn.setText("收货");
        }else if("2".equals(mType)){
            orderDetailBtn.setVisibility(View.VISIBLE);
            orderDetailBtn.setText("评价");
        }else if("3".equals(mType)){
            llOrderDetail.setVisibility(View.VISIBLE);
            if(order.getLevel().equals("0")){
                orderEvaluate.setText("不满意");
                orderEvaluate.setTextColor(getResources().getColor(R.color.text_gray));
                orderEvaluate.setBackgroundResource(R.drawable.edit_shap);
            }else{
                orderEvaluate.setText("满意");
            }
            orderEvaluateContent.setText(order.getContent());
            /*if(order.getEvaluationPicList()!=null){
                final Intent i = new Intent();
                i.setClass(this,SpaceImageDetailActivity.class);
                if(order.getEvaluationPicList().size()>0){
                    llImg.setVisibility(View.VISIBLE);
                    commentOne.setVisibility(View.VISIBLE);
                    Arad.imageLoader.load(order.getEvaluationPicList().get(0).getZipImg()).placeholder(R.drawable.img_post_mr).into(commentOne);
                    commentOne.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            int[] location = new int[2];
                            commentOne.getLocationOnScreen(location);
                            i.putExtra("locationX", location[0]);
                            i.putExtra("locationY", location[1]);
                            i.putExtra("width",  commentOne.getWidth());
                            i.putExtra("height",  commentOne.getHeight());
                            i.putExtra("imgUrl", order.getEvaluationPicList().get(0).getOriginalImg());
                            startActivity(i);
                            overridePendingTransition(0, 0);
                        }
                    });
                }
                if(order.getEvaluationPicList().size()>1){
                    commentTwo.setVisibility(View.VISIBLE);
                    Arad.imageLoader.load(order.getEvaluationPicList().get(1).getZipImg()).placeholder(R.drawable.img_post_mr).into(commentTwo);
                    commentTwo.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            int[] location = new int[2];
                            commentTwo.getLocationOnScreen(location);
                            i.putExtra("locationX", location[0]);
                            i.putExtra("locationY", location[1]);
                            i.putExtra("width", commentTwo.getWidth());
                            i.putExtra("height", commentTwo.getHeight());
                            i.putExtra("imgUrl", order.getEvaluationPicList().get(1).getOriginalImg());
                            startActivity(i);
                            overridePendingTransition(0, 0);
                        }
                    });
                }
                if(order.getEvaluationPicList().size()>2) {
                    commentThree.setVisibility(View.VISIBLE);
                    Arad.imageLoader.load(order.getEvaluationPicList().get(2).getZipImg()).placeholder(R.drawable.img_post_mr).into(commentThree);
                    commentThree.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            int[] location = new int[2];
                            commentThree.getLocationOnScreen(location);
                            i.putExtra("locationX", location[0]);
                            i.putExtra("locationY", location[1]);
                            i.putExtra("width", commentThree.getWidth());
                            i.putExtra("height", commentThree.getHeight());
                            i.putExtra("imgUrl", order.getEvaluationPicList().get(2).getOriginalImg());
                            startActivity(i);
                            overridePendingTransition(0, 0);
                        }
                    });
                }
            }*/
        }
        orderDetailBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if("1".equals(mType)){
                    orderReceive(order.getId());
                }else if("2".equals(mType)){
                    Intent i = new Intent();
                    i.setClass(MyOrderDetail.this,OrderEvaluateRelease.class);
                    i.putExtra("order",order);
                    startActivity(i);
                }
            }
        });
    }

    private void orderReceive(String orderId) {
        showProgress(true);
        Map<String, String> map = new HashMap();
        map.putAll(ConstantsData.getSystemParams());
        map.put(ConstantsData.METHOD, Url.orderReceiveUrl);
        map.put("orderId", orderId);
        String sign = AopUtils.sign(map, ConstantsData.SECRET_VALUE);
        map.put("sign", sign);
        map.remove(ConstantsData.METHOD);
        LLog.d(map + "");
        RetrofitUtil.Api().orderReceive(map).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new BaseTSubscriber<ResponseBody>(this) {
            @Override
            public void onResponseCallback(JSONObject response, String resultCode) {
                showProgress(false);
                if (TextUtils.equals(resultCode, ConstantsData.success)) {
                    Utils.toastError(MyOrderDetail.this, "确认收货成功");
                    finish();
                }
            }

            @Override
            public void onNetError(Throwable e) {
            }
        });
    }
}
