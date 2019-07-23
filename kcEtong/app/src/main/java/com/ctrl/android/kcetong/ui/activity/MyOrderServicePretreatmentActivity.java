package com.ctrl.android.kcetong.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ctrl.android.kcetong.R;
import com.ctrl.android.kcetong.model.AppHolder;
import com.ctrl.android.kcetong.model.ServiceOrder;
import com.ctrl.android.kcetong.model.ServiceOrderResult;
import com.ctrl.android.kcetong.toolkit.util.AopUtils;
import com.ctrl.android.kcetong.toolkit.util.ConstantsData;
import com.ctrl.android.kcetong.toolkit.util.LLog;
import com.ctrl.android.kcetong.toolkit.util.MessageUtils;
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

/*我的预约（处理前）activity*/

public class MyOrderServicePretreatmentActivity extends BaseActivity implements View.OnClickListener {
    @BindView(R.id.tv_my_repairs_pretreament_progress)
    TextView       tv_my_repairs_pretreament_progress;
    @BindView(R.id.tv_my_repairs_pretreament_time)
    TextView       tv_my_repairs_pretreament_time;
    @BindView(R.id.tv_my_repairs_pretreament_price)
    TextView       tv_my_repairs_pretreament_price;
    @BindView(R.id.tv_my_repairs_pretreament_type)
    TextView       tv_my_repairs_pretreament_type;
    @BindView(R.id.tv_my_repairs_pretreament_content)
    TextView       tv_my_repairs_pretreament_content;
    @BindView(R.id.tv_my_repairs_pretreament_wuye)
    TextView       tv_my_repairs_pretreament_wuye;
    @BindView(R.id.tv_my_repairs_pretreament_result)
    TextView       tv_my_repairs_pretreament_result;
    @BindView(R.id.iv01_my_repairs_pretreament)
    ImageView      iv01_my_repairs_pretreament;
    @BindView(R.id.iv02_my_repairs_pretreament)
    ImageView      iv02_my_repairs_pretreament;
    @BindView(R.id.iv03_my_repairs_pretreament)
    ImageView      iv03_my_repairs_pretreament;
    @BindView(R.id.iv04_my_repairs_pretreament)
    ImageView      iv04_my_repairs_pretreament;
    @BindView(R.id.iv05_my_repairs_pretreament)
    ImageView      iv05_my_repairs_pretreament;
    @BindView(R.id.iv06_my_repairs_pretreament)
    ImageView      iv06_my_repairs_pretreament;
    @BindView(R.id.ll_my_repairs_wuye)
    LinearLayout   ll_my_repairs_wuye;
    @BindView(R.id.tv_baoxiu_image)
    TextView       tv_baoxiu_image;
    @BindView(R.id.tv_wuye_image)
    TextView       tv_wuye_image;
    @BindView(R.id.tv_my_repairs_pretreament_room)//报修房间
    TextView       tv_my_repairs_pretreament_room;
    @BindView(R.id.rl_go_pay)//去付款布局
    RelativeLayout rl_go_pay;
    @BindView(R.id.tv_go_pay)//去付款
    TextView       tv_go_pay;
    @BindView(R.id.tv_offline_pay)//已线下付款
    TextView       tv_offline_pay;


    @BindView(R.id.tv_cancel)
    TextView     tv_cancel;
    @BindView(R.id.rg_repairs)
    RadioGroup   rg_repairs;
    @BindView(R.id.rb_repairs_01)
    RadioButton  rb_repairs_01;
    @BindView(R.id.rb_repairs_02)
    RadioButton  rb_repairs_02;
    @BindView(R.id.rb_repairs_03)
    RadioButton  rb_repairs_03;
    @BindView(R.id.et_my_repairs_pretreament_pingjia)
    EditText     et_my_repairs_pretreament_pingjia;
    @BindView(R.id.ll_my_repairs_pingjia)
    LinearLayout ll_my_repairs_pingjia;

    private String progressState;//状态

    private ServiceOrder serviceOrder;
    private String evaluateLevel = "0";
    @Override
    protected void initView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_my_order_service_pretreatment);
        ButterKnife.bind(this);
        toolbarBaseSetting("预约详情", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyOrderServicePretreatmentActivity.this.finish();
            }
        });
    }

    @Override
    protected void initData() {
        progressState = getIntent().getStringExtra("progressState");
        requestServiceOrderInfo(getIntent().getStringExtra("repairDemandId"));

    }

    /**
     * 获取我的预约服务详情
     * @param serviceOrderId 业主ID
     * */
    public void requestServiceOrderInfo(String serviceOrderId){
        Map<String,String> map = new HashMap<>();
        map.put(ConstantsData.METHOD,"pm.ppt.serviceOrder.get");//方法名称
        map.putAll(ConstantsData.getSystemParams());

        map.put("serviceOrderId",serviceOrderId);

        String sign = AopUtils.sign(map, ConstantsData.SECRET_VALUE);
        map.put("sign",sign);
        LLog.d(map+"");
        map.remove(ConstantsData.METHOD);

        RetrofitUtil.Api().requestServiceOrderInfo(map).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseTSubscriber<ServiceOrderResult>(this) {
                    @Override
                    public void onResponseCallback(JSONObject response, String resultCode) {
                        LLog.d(response+"");
                    }
                    @Override
                    public void onNext(ServiceOrderResult serviceOrderResult) {
                        super.onNext(serviceOrderResult);
                        if(TextUtils.equals("000",serviceOrderResult.getCode())){
                            serviceOrder = serviceOrderResult.getData().getServiceOrderInfo();

                            if("0".equals(progressState)){
                                ll_my_repairs_wuye.setVisibility(View.GONE);
                                ll_my_repairs_pingjia.setVisibility(View.GONE);
                                rl_go_pay.setVisibility(View.GONE);
                                tv_cancel.setVisibility(View.VISIBLE);
                                tv_my_repairs_pretreament_progress.setText("服务进程：待受理");
                                tv_my_repairs_pretreament_price.setText("服务价格："+serviceOrder.getSellingPrice()+"元");
                                tv_my_repairs_pretreament_time.setText("预约时间：" + serviceOrder.getAppointmentTime());
                                tv_my_repairs_pretreament_type.setText("服务名称："+serviceOrder.getProductName());
                                tv_my_repairs_pretreament_room.setText("预约房间："+serviceOrder.getBuilding()+"-"+serviceOrder.getUnit()+"-"+serviceOrder.getRoom());
                                tv_my_repairs_pretreament_content.setText(serviceOrder.getContent());
                            }

                            if("1".equals(progressState)){
                                ll_my_repairs_wuye.setVisibility(View.GONE);
                                ll_my_repairs_pingjia.setVisibility(View.GONE);
                                rl_go_pay.setVisibility(View.GONE);
                                tv_my_repairs_pretreament_progress.setText("服务进程：已受理");
                                tv_my_repairs_pretreament_price.setText("服务价格："+serviceOrder.getSellingPrice()+"元");
                                tv_my_repairs_pretreament_time.setText("预约时间："+serviceOrder.getAppointmentTime());
                                tv_my_repairs_pretreament_type.setText("服务名称："+serviceOrder.getProductName());
                                tv_my_repairs_pretreament_room.setText("预约房间："+serviceOrder.getBuilding()+"-"+serviceOrder.getUnit()+"-"+serviceOrder.getRoom());
                                tv_my_repairs_pretreament_content.setText(serviceOrder.getContent());

                            }
                            if("2".equals(progressState)) {
                                ll_my_repairs_wuye.setVisibility(View.VISIBLE);
                                tv_my_repairs_pretreament_progress.setText("服务进程：待付款");
                                tv_my_repairs_pretreament_wuye.setText("物业服务");
                                rl_go_pay.setVisibility(View.VISIBLE);
                                ll_my_repairs_pingjia.setVisibility(View.VISIBLE);
//                                1.线上支付 2.线下支付
                                if("1".equals(serviceOrder.getPayMode())){
                                    tv_go_pay.setVisibility(View.VISIBLE);
                                    tv_offline_pay.setVisibility(View.GONE);
                                }else if("2".equals(serviceOrder.getPayMode())){
                                    tv_offline_pay.setVisibility(View.VISIBLE);
                                    tv_go_pay.setVisibility(View.GONE);
                                }
                                tv_my_repairs_pretreament_price.setText("服务价格："+serviceOrder.getSellingPrice()+"元");
                                tv_my_repairs_pretreament_time.setText("预约时间："+serviceOrder.getAppointmentTime());
                                tv_my_repairs_pretreament_type.setText("服务名称："+serviceOrder.getProductName());
                                tv_my_repairs_pretreament_room.setText("预约房间："+serviceOrder.getBuilding()+"-"+serviceOrder.getUnit()+"-"+serviceOrder.getRoom());
                                tv_my_repairs_pretreament_content.setText(serviceOrder.getContent());
                                tv_my_repairs_pretreament_result.setText(serviceOrder.getResult());
                            }
                        }
                    }
                });
    }

    @OnClick({R.id.rb_repairs_01, R.id.rb_repairs_02, R.id.rb_repairs_03,R.id.tv_cancel, R.id.tv_go_pay,R.id.tv_offline_pay})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.rb_repairs_01:
                if("0".equals(AppHolder.getInstance().getMemberInfo().getSupers())){
                    hide();
                    rb_repairs_01.setChecked(true);
                    rb_repairs_01.setTextColor(getResources().getColor(R.color.white));
                    evaluateLevel = "0";
                }else if("1".equals(AppHolder.getInstance().getMemberInfo().getSupers())
                         || "2".equals(AppHolder.getInstance().getMemberInfo().getSupers())){
                    Utils.showShortToast(MyOrderServicePretreatmentActivity.this, getString(R.string.manager_cannot));
                }

                break;
            case R.id.rb_repairs_02:
                if("0".equals(AppHolder.getInstance().getMemberInfo().getSupers())){
                    hide();
                    rb_repairs_02.setChecked(true);
                    rb_repairs_02.setTextColor(getResources().getColor(R.color.white));
                    evaluateLevel = "1";
                }else if("1".equals(AppHolder.getInstance().getMemberInfo().getSupers())
                         || "2".equals(AppHolder.getInstance().getMemberInfo().getSupers())){
                    Utils.showShortToast(MyOrderServicePretreatmentActivity.this, getString(R.string.manager_cannot));
                }

                break;
            case R.id.rb_repairs_03:
                if("0".equals(AppHolder.getInstance().getMemberInfo().getSupers())){
                    hide();
                    rb_repairs_03.setChecked(true);
                    rb_repairs_03.setTextColor(getResources().getColor(R.color.white));
                    evaluateLevel = "2";
                }else if("1".equals(AppHolder.getInstance().getMemberInfo().getSupers())
                         || "2".equals(AppHolder.getInstance().getMemberInfo().getSupers())){
                    Utils.showShortToast(MyOrderServicePretreatmentActivity.this, getString(R.string.manager_cannot));
                }

                break;
            case R.id.tv_cancel:
                if("0".equals(AppHolder.getInstance().getMemberInfo().getSupers())){
                    showProgress(true);
                    requestCancelServiceOrder(getIntent().getStringExtra("repairDemandId"));
                }else if("1".equals(AppHolder.getInstance().getMemberInfo().getSupers())
                         || "2".equals(AppHolder.getInstance().getMemberInfo().getSupers())){
                    Utils.showShortToast(MyOrderServicePretreatmentActivity.this, getString(R.string.manager_cannot));
                }

                break;
            case R.id.tv_go_pay:
                if("0".equals(AppHolder.getInstance().getMemberInfo().getSupers())){
                    showProgress(true);
                    requestCheckService(AppHolder.getInstance().getMemberInfo().getMemberId(), getIntent().getStringExtra("repairDemandId"));
                }else if("1".equals(AppHolder.getInstance().getMemberInfo().getSupers())
                         || "2".equals(AppHolder.getInstance().getMemberInfo().getSupers())){
                    Utils.showShortToast(MyOrderServicePretreatmentActivity.this, getString(R.string.manager_cannot));
                }

                break;
            case R.id.tv_offline_pay:
                if("0".equals(AppHolder.getInstance().getMemberInfo().getSupers())){
                    requestAfterServiceOrderPay(getIntent().getStringExtra("repairDemandId"), tv_my_repairs_pretreament_price.getText()+"",
                            "1", "2", "", "", "", "", "", "", "");
                }else if("1".equals(AppHolder.getInstance().getMemberInfo().getSupers())
                         || "2".equals(AppHolder.getInstance().getMemberInfo().getSupers())){
                    Utils.showShortToast(MyOrderServicePretreatmentActivity.this, getString(R.string.manager_cannot));
                }

                break;
        }
    }

    private void hide(){
        rb_repairs_01.setTextColor(getResources().getColor(R.color.text_gray));
        rb_repairs_02.setTextColor(getResources().getColor(R.color.text_gray));
        rb_repairs_03.setTextColor(getResources().getColor(R.color.text_gray));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK){
            switch (requestCode){
                case 3333:
                    setResult(RESULT_OK);
                    finish();
                    break;
            }
        }
    }

    /**
     * 服务订单支付前校验
     * @param memberId 会员id
     * @param serviceOrderId 订单id
     * */
    public void requestCheckService(String memberId, final String serviceOrderId){
        Map<String,String> map = new HashMap<>();
        map.put(ConstantsData.METHOD,"pm.ppt.serviceOrder.checkServicePay");//方法名称
        map.putAll(ConstantsData.getSystemParams());

        map.put("memberId",memberId);
        map.put("serviceOrderId",serviceOrderId);

        String sign = AopUtils.sign(map, ConstantsData.SECRET_VALUE);
        map.put("sign",sign);
        LLog.d(map+"");
        map.remove(ConstantsData.METHOD);

        RetrofitUtil.Api().requestCheckService(map).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseTSubscriber<ResponseBody>(this) {
                    @Override
                    public void onResponseCallback(JSONObject response, String resultCode) {
                        LLog.d(response+"");
                        showProgress(false);
                        try {
                            if("000".equals(response.getString("code"))){
                                Intent i=new Intent(MyOrderServicePretreatmentActivity.this, ServiceOrderPayStyleActivity.class);
                                i.putExtra("name",serviceOrder.getProductName());
                                i.putExtra("id",getIntent().getStringExtra("repairDemandId"));
                                i.putExtra("price",serviceOrder.getSellingPrice());
                                i.putExtra("evaluateLevel",evaluateLevel);
                                if(et_my_repairs_pretreament_pingjia.getText() != null){
                                    i.putExtra("evaluateContent",et_my_repairs_pretreament_pingjia.getText()+"");
                                }else {
                                    i.putExtra("evaluateContent","");
                                }

                                startActivityForResult(i, 3333);
                            }else if("030".equals(response.getString("code"))){
                                MessageUtils.showShortToast(MyOrderServicePretreatmentActivity.this,"支付失败，订单已经支付！");
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
    /**
     * 会员取消订单
     * @param serviceOrderId 订单id
     * */
    public void requestCancelServiceOrder(String serviceOrderId){
        Map<String,String> map = new HashMap<>();
        map.put(ConstantsData.METHOD,"pm.ppt.serviceOrder.cancel");//方法名称
        map.putAll(ConstantsData.getSystemParams());

        map.put("serviceOrderId",serviceOrderId);

        String sign = AopUtils.sign(map, ConstantsData.SECRET_VALUE);
        map.put("sign",sign);
        LLog.d(map+"");
        map.remove(ConstantsData.METHOD);
        RetrofitUtil.Api().requestCancelServiceOrder(map).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseTSubscriber<ResponseBody>(this) {
                    @Override
                    public void onResponseCallback(JSONObject response, String resultCode) {
                        LLog.d(response+"");
                        showProgress(false);
                        try {
                            if("000".equals(response.getString("code"))){
                                MessageUtils.showShortToast(MyOrderServicePretreatmentActivity.this, "取消订单成功");
                                Intent intent =new Intent();
                                setResult(2001,intent);
                                MyOrderServicePretreatmentActivity.this.finish();
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
    /**
     * 订单支付成功后
     * @param serviceOrderId 订单id
     * @param totalCost 订单总金额
     * @param payStatus 支付状（0：未付款、1：已付款）
     * @param payMode 支付方式（1：余额支付、2：线下支付[货到付款]、3、支付宝、4：微信、5：银联卡）
     * @param dealId 支付反馈的流水单号
     * @param dealType 获取支付返回银行类型（alipay：支付宝、weixin：微信）
     * @param dealFee 交易金额
     * @param dealState 交易状态
     * @param dealSigunre 返回签名数据
     * @param bankcardNo 支付卡号
     * @param bankcardName 发卡行
     * */
    public void requestAfterServiceOrderPay(String serviceOrderId,String totalCost,
            String payStatus,
            String payMode,
            String dealId,
            String dealType,
            String dealFee,
            String dealState,
            String dealSigunre,
            String bankcardNo,
            String bankcardName){
        Map<String,String> map = new HashMap<String,String>();
        map.put(ConstantsData.METHOD,"pm.ppt.serviceOrder.afterServiceOrderPay");//方法名称
        map.putAll(ConstantsData.getSystemParams());

        map.put("serviceOrderId",serviceOrderId);
        map.put("totalCost",totalCost);
        map.put("payStatus",payStatus);
        map.put("payMode",payMode);
        map.put("dealId",dealId);
        map.put("dealType",dealType);
        map.put("dealFee",dealFee);
        map.put("dealState",dealState);
        map.put("dealSigunre",dealSigunre);
        map.put("bankcardNo",bankcardNo);
        map.put("bankcardName",bankcardName);
        map.put("evaluateContent",et_my_repairs_pretreament_pingjia.getText()+"");
        map.put("evaluateLevel",evaluateLevel);


        String sign = AopUtils.sign(map, ConstantsData.SECRET_VALUE);
        map.put("sign",sign);

        LLog.d(map+"");
        map.remove(ConstantsData.METHOD);
        RetrofitUtil.Api().requestAfterServiceOrderPay(map).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new BaseTSubscriber<ResponseBody>(this) {
                        @Override
                        public void onResponseCallback(JSONObject response, String resultCode) {
                            LLog.d(response+"");
                            showProgress(false);
                            try {
                                if("000".equals(response.getString("code"))){
                                    setResult(3334);
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
