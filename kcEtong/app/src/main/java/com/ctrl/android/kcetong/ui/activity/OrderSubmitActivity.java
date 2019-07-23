package com.ctrl.android.kcetong.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ctrl.android.kcetong.R;
import com.ctrl.android.kcetong.model.AppHolder;
import com.ctrl.android.kcetong.model.DefaultAddress;
import com.ctrl.android.kcetong.model.GenerateOrder;
import com.ctrl.android.kcetong.model.ReceiveAddress;
import com.ctrl.android.kcetong.toolkit.Url.Url;
import com.ctrl.android.kcetong.toolkit.util.AopUtils;
import com.ctrl.android.kcetong.toolkit.util.ConstantsData;
import com.ctrl.android.kcetong.toolkit.util.LLog;
import com.ctrl.android.kcetong.toolkit.util.MessageUtils;
import com.ctrl.android.kcetong.toolkit.webutils.BaseTSubscriber;
import com.ctrl.android.kcetong.toolkit.webutils.RetrofitUtil;
import com.ctrl.android.kcetong.ui.base.BaseActivity;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class OrderSubmitActivity extends BaseActivity {

    @BindView(R.id.iv_location)
    ImageView      mLocation;
    @BindView(R.id.iv_right)
    ImageView      mRight;
    @BindView(R.id.tv_sh)
    TextView       mSh;
    @BindView(R.id.tv_buyer_name)
    TextView       mBuyerName;
    @BindView(R.id.tv_buyer_mobile)
    TextView       mBuyerMobile;
    @BindView(R.id.tv_buyer_address)
    TextView       mBuyerAddress;
    @BindView(R.id.rl_delivery_details)
    RelativeLayout rlDeliveryDetails;
    @BindView(R.id.tv_product_name)
    TextView       mProductName;
    @BindView(R.id.tv_product_price)
    TextView       mProductPrice;
    @BindView(R.id.iv_order_add)
    ImageView      mOrderAdd;
    @BindView(R.id.tv_product_number)
    TextView       mProductNumber;
    @BindView(R.id.iv_order_minus)
    ImageView      mOrderMinus;
    @BindView(R.id.et_order_ask)
    EditText       mOrderAsk;
    @BindView(R.id.tv_order_number)
    TextView       mOrderNumber;
    @BindView(R.id.tv_order_total)
    TextView       mOrderTotal;
    @BindView(R.id.tv_order_submit)
    TextView       mOrderSubmit;
//    @BindView(R.id.view_bg)
//    View           viewBg;
//    @BindView(R.id.tv_window_name)
//    TextView       tvWindowName;
//    @BindView(R.id.tv_window_price)
//    TextView       tvWindowPrice;
//    @BindView(R.id.tv_window_number)
//    TextView       tvWindowNumber;
//    @BindView(R.id.tv_order_window_number)
//    TextView       tvOrderWindowNumber;
//    @BindView(R.id.tv_window_total)
//    TextView       tvWindowTotal;
//    @BindView(R.id.tv_window_exit)
//    TextView       tvWindowExit;
//    @BindView(R.id.tv_window_submit)
//    TextView       tvWindowSubmit;
//    @BindView(R.id.rl_order_window)
//    RelativeLayout rlOrderWindow;

    public static OrderSubmitActivity instance = null;
    int number = 1;
    double total;
    String productId;
    String productName;
    String productPrice;
    String point;
    String addressId;
    String address;
    String sellType = "0";
    String appTotalPoint = "0";
    String appTotalCost = "0";
    ReceiveAddress receiveAddress = new ReceiveAddress();

    @Override
    protected void initView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_order_submit);
        ButterKnife.bind(this);
        toolbarBaseSetting("提交订单", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OrderSubmitActivity.this.finish();
            }
        });
        instance = this;
        productId = getIntent().getStringExtra("productId");
        productName = getIntent().getStringExtra("productName");
        productPrice = getIntent().getStringExtra("productPrice");
        point = getIntent().getStringExtra("point");
        if(getIntent().getStringExtra("sellType")!=null){
            sellType =getIntent().getStringExtra("sellType");
        }
        if(sellType.equals("0")){
            total = Double.parseDouble(productPrice);
        }else {
            total = Double.parseDouble(point);
        }
    }

    @Override
    protected void initData() {
        Map<String, String> map = new HashMap();
        map.putAll(ConstantsData.getSystemParams());
        map.put(ConstantsData.METHOD, Url.defaultAddressUrl);
        map.put("memberId", holder.getMemberInfo().getMemberId());
        String sign = AopUtils.sign(map, ConstantsData.SECRET_VALUE);
        map.put("sign", sign);
        map.remove(ConstantsData.METHOD);
        LLog.d(map + "");
        RetrofitUtil.Api().getdefaultAddress(map).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new BaseTSubscriber<DefaultAddress>(this) {
            @Override
            public void onResponseCallback(JSONObject response, String resultCode) {
                LLog.d("onResponseCallback: " + response);
            }

            @Override
            public void onNext(DefaultAddress bean) {
                super.onNext(bean);
                if (TextUtils.equals("000", bean.getCode())) {
                    holder.setDefaultReceiveAddress(bean.getData().getDftRcvAddress());
                    if(AppHolder.getInstance().getDefaultReceiveAddress() != null){
                        mBuyerName.setText(holder.getDefaultReceiveAddress().getReceiveName());
                        mBuyerMobile.setText(holder.getDefaultReceiveAddress().getMobile());
                        mBuyerAddress.setText(holder.getDefaultReceiveAddress().getReceiveAddress());
                        addressId = holder.getDefaultReceiveAddress().getId();
                    }else{
                        mBuyerName.setText("");
                        mBuyerMobile.setText("");
                        mBuyerAddress.setText("");
                    }
                }
            }

            @Override
            public void onNetError(Throwable e) {
            }

        });

        mProductName.setText(productName);
        if(sellType.equals("0")){
            mProductPrice.setText(productPrice+"元");
            mOrderTotal.setText("￥"+productPrice+"元");
            appTotalCost = productPrice;
        }else{
            mProductPrice.setText(point+"积分");
            mOrderTotal.setText(point+"积分");
            appTotalPoint = point;
        }
        mProductNumber.setText(number+"");
        mOrderNumber.setText("共计"+number+"件商品");
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case 4:
                if(resultCode == 4){
                    receiveAddress = (ReceiveAddress) data.getSerializableExtra("receiveAddresse");
                    mBuyerName.setText(receiveAddress.getReceiveName());
                    mBuyerMobile.setText(receiveAddress.getMobile());
                    mBuyerAddress.setText(receiveAddress.getAddress());
                    addressId = receiveAddress.getReceiveAddressId();
                    address = receiveAddress.getAddress();
                }
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @OnClick({R.id.rl_delivery_details, R.id.iv_order_add, R.id.iv_order_minus, R.id.tv_order_submit})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.rl_delivery_details:
                Intent i = new Intent();
                i.setClass(this, OrderAddressActivity.class);
                i.putExtra("name", mBuyerName.getText().toString());
                i.putExtra("mobile",mBuyerMobile.getText().toString());
                i.putExtra("address",mBuyerAddress.getText().toString());
                i.putExtra("select","1");
                startActivityForResult(i, 4);
                break;
            case R.id.iv_order_add:
                number+=1;
                mProductNumber.setText(number + "");
                mOrderNumber.setText("共计" + number + "件商品");
                if(sellType.equals("0")){
                    total = Double.parseDouble(productPrice)*number;
                    mOrderTotal.setText("￥"+total+"元");
                    appTotalCost = total+"";
                }else{
                    total = Double.parseDouble(point)*number;
                    mOrderTotal.setText((int)total+"积分");
                    appTotalPoint = (int)total+"";
                }
                break;
            case R.id.iv_order_minus:
                if(number > 1){
                    number-=1;
                    mProductNumber.setText(number+"");
                    mOrderNumber.setText("共计"+number+"件商品");
                    if(sellType.equals("0")){
                        total = Double.parseDouble(productPrice)*number;
                        mOrderTotal.setText("￥"+total+"元");
                        appTotalCost = total+"";
                    }else{
                        total = Double.parseDouble(point)*number;
                        mOrderTotal.setText((int)total+"积分");
                        appTotalPoint = (int)total+"";
                    }
                }else{
                    MessageUtils.showShortToast(this,"团购商品数量不能少于一件");
                }
                break;
            case R.id.tv_order_submit:
                if(TextUtils.isEmpty(addressId)){
                    MessageUtils.showShortToast(this,"请选择收货地址");
                    return;
                }
                generateOrder();
                break;
        }
    }

    private void generateOrder() {
        showProgress(true);
        Map<String, String> map = new HashMap();
        map.putAll(ConstantsData.getSystemParams());
        map.put(ConstantsData.METHOD, Url.generateOrderUrl);
        map.put("memberId", holder.getMemberInfo().getMemberId());
        map.put("productId", productId);
        map.put("productNum", number +"");
        map.put("addressId", addressId);
        map.put("sellType", sellType);
        map.put("appTotalPoint", appTotalPoint);
        map.put("appTotalCost", appTotalCost);
        String sign = AopUtils.sign(map, ConstantsData.SECRET_VALUE);
        map.put("sign", sign);
        map.remove(ConstantsData.METHOD);
        LLog.d(map + "");
        RetrofitUtil.Api().generateOrder(map).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new BaseTSubscriber<GenerateOrder>(this) {
            @Override
            public void onResponseCallback(JSONObject response, String resultCode) {
                LLog.d("onResponseCallback: " + response);
            }

            @Override
            public void onNext(GenerateOrder bean) {
                super.onNext(bean);
                if (TextUtils.equals("000", bean.getCode())) {
                    if(sellType.equals("0")){
                        Intent i = new Intent();
                        i.setClass(OrderSubmitActivity.this, OrderPayActivity.class);
                        i.putExtra("productName", productName);
                        i.putExtra("productPrice",productPrice);
                        i.putExtra("number",number+"");
                        i.putExtra("total",total);
                        i.putExtra("address",address);
                        i.putExtra("orderId",bean.getData().getOrderId());
                        startActivity(i);
                    }else{
                        ChoiceDetailActivity.instance.finish();
                        finish();
                    }
                }
                showProgress(false);
            }

            @Override
            public void onNetError(Throwable e) {
                showProgress(false);
            }

        });
    }
}
