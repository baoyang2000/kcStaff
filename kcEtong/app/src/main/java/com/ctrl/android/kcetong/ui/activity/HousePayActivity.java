package com.ctrl.android.kcetong.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.ctrl.android.kcetong.R;
import com.ctrl.android.kcetong.model.AppHolder;
import com.ctrl.android.kcetong.model.PaymentListResult;
import com.ctrl.android.kcetong.model.PaymentResult;
import com.ctrl.android.kcetong.model.PropertyPay;
import com.ctrl.android.kcetong.toolkit.util.AopUtils;
import com.ctrl.android.kcetong.toolkit.util.ConstantsData;
import com.ctrl.android.kcetong.toolkit.util.LLog;
import com.ctrl.android.kcetong.toolkit.util.S;
import com.ctrl.android.kcetong.toolkit.util.StrConstant;
import com.ctrl.android.kcetong.toolkit.util.Utils;
import com.ctrl.android.kcetong.toolkit.webutils.BaseTSubscriber;
import com.ctrl.android.kcetong.toolkit.webutils.RetrofitUtil;
import com.ctrl.android.kcetong.ui.adapter.HousePayAdapter;
import com.ctrl.android.kcetong.ui.base.BaseActivity;

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

public class HousePayActivity extends BaseActivity {

    @BindView(R.id.house_owner)
    TextView house_owner;
    @BindView(R.id.house_pay_history_btn)
    TextView house_pay_history_btn;
    @BindView(R.id.listView)
    ListView listView;
    @BindView(R.id.amount_text)
    TextView amount_text;
    @BindView(R.id.house_pay_btn)
    TextView house_pay_btn;

    private String TITLE = StrConstant.HOUSE_PAY_TITLE;

    private HousePayAdapter housePayAdapter;
    private String          communityName;
    private String          building_unit_room;
    private String          proprietorId;
    private String          addressId;

    private double debt;

    private static final int RequestPayCart = 100;
    @Override
    protected void initView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_house_pay);
        ButterKnife.bind(this);
        toolbarBaseSetting(TITLE, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HousePayActivity.this.finish();
            }
        });
    }

    @Override
    protected void initData() {
        AppHolder.getInstance().setListPropertyPay(new ArrayList<PropertyPay>());
        amount_text.setText("0元");

        communityName = getIntent().getStringExtra("communityName");
        building_unit_room = getIntent().getStringExtra("building_unit_room");
        proprietorId = getIntent().getStringExtra("proprietorId");
        addressId = getIntent().getStringExtra("addressId");

        house_owner.setText(communityName + "    " + building_unit_room);


    }

    @Override
    protected void onResume() {
        super.onResume();
        housePayAdapter = new HousePayAdapter(this);

        if(AppHolder.getInstance().getListPropertyPay() != null && AppHolder.getInstance().getListPropertyPay().size() > 0){
            housePayAdapter.setList(AppHolder.getInstance().getListPropertyPay());
            listView.setAdapter(housePayAdapter);
            housePayAdapter.notifyDataSetChanged();
        } else {
            showProgress(true);
            requestPropertyPayList(proprietorId, addressId, "0", "", "");
        }
    }

    @OnClick({R.id.house_pay_history_btn, R.id.house_pay_btn})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.house_pay_history_btn://缴费记录
                Intent intent = new Intent(HousePayActivity.this, HousePayHistoryActivity.class);
                intent.putExtra("communityName",communityName);
                intent.putExtra("building_unit_room",building_unit_room);
                intent.putExtra("proprietorId",proprietorId);
                intent.putExtra("addressId",addressId);
                startActivity(intent);
                break;
            case R.id.house_pay_btn:
                if("0".equals(AppHolder.getInstance().getMemberInfo().getSupers())){
                    if(!S.isNull(getStrs(AppHolder.getInstance().getListPropertyPay()))){
                        Intent intent_pay = new Intent(HousePayActivity.this, HousePayCartActivity.class);
                        intent_pay.putExtra("communityName",communityName);
                        intent_pay.putExtra("building_unit_room",building_unit_room);
                        startActivityForResult(intent_pay,RequestPayCart);
                    } else {
                        Utils.toastError(this,"请选择缴费项目");
                    }
                }else if("1".equals(AppHolder.getInstance().getMemberInfo().getSupers())
                         || "2".equals(AppHolder.getInstance().getMemberInfo().getSupers())){
                    Utils.showShortToast(HousePayActivity.this, getString(R.string.manager_cannot));
                }

                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK){
            switch (requestCode){
                case RequestPayCart:
//                    List<PropertyPay> listPay = (List<PropertyPay>) data.getSerializableExtra("data");
//                    for(int i = 0;i < listPay.size(); i++){
//                        PropertyPay propertyPay = listPay.get(i);
//                        if(propertyPay.getPayFlg()==0){
//
//                        }
//                    }
                    break;
            }
        }
    }

    /**
     * 物业缴费列表
     * @param proprietorId 业主id
     * @param addressId 地址id
     * @param status 缴费状态（0：等待缴费、1：已缴费）
     * @param currentPageStr 当前页码
     * @param rowCountPerPageStr 每页条数
     * */
    public void requestPropertyPayList(String proprietorId,String addressId,String status,
            String currentPageStr,String rowCountPerPageStr){
        Map<String,String> map = new HashMap<String,String>();
        map.put(ConstantsData.METHOD,"pm.ppt.propertyPayment.queryPropertyPaymentList");//方法名称
        map.putAll(ConstantsData.getSystemParams());

        map.put("proprietorId",proprietorId);
        map.put("addressId",addressId);
        map.put("status",status);
        map.put("currentPageStr",currentPageStr);
        map.put("rowCountPerPageStr",rowCountPerPageStr);

        String sign = AopUtils.sign(map, ConstantsData.SECRET_VALUE);
        map.put("sign",sign);
        RetrofitUtil.Api().requestPropertyPayList(map).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseTSubscriber<PaymentListResult>(this) {
                    @Override
                    public void onResponseCallback(JSONObject response, String resultCode) {
                        LLog.d(response+"");
                        showProgress(false);
                    }
                    @Override
                    public void onNext(PaymentListResult paymentListResult) {
                        super.onNext(paymentListResult);
                        if (TextUtils.equals("000",paymentListResult.getCode())){

                            AppHolder.getInstance().setListPropertyPay(paymentListResult.getData().getPaymentList());

                            //housePayAdapter = new HousePayAdapter(this);
                            housePayAdapter.setList(AppHolder.getInstance().getListPropertyPay());
                            listView.setAdapter(housePayAdapter);

                            listView.setDividerHeight(20);
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
    /**
     * 计算 总金额
     * */
    public void calAmount(int position){
        AppHolder.getInstance().getListPropertyPay().get(position).setPayFlg(1);
        //PropertyPay pay = AppHolder.getInstance().getListPropertyPay().get(position);
        //AppHolder.getInstance().getListPropertyPay().add(pay);
        Log.d("demo", "ListPropertyPay: " + getStrs(AppHolder.getInstance().getListPropertyPay()));
        String strs = getStrs(AppHolder.getInstance().getListPropertyPay());
        showProgress(true);
        requestPropertyPayAmount(strs);

    }

    /**
     * 计算物业账单总额
     * @param propertyPaymentIdStr 物业账单id串
     * */
    public void requestPropertyPayAmount(String propertyPaymentIdStr){
        Map<String,String> map = new HashMap<String,String>();
        map.put(ConstantsData.METHOD,"pm.ppt.propertyPayment.calcPropertyPaymentPrice");//方法名称
        map.putAll(ConstantsData.getSystemParams());

        map.put("propertyPaymentIdStr",propertyPaymentIdStr);

        String sign = AopUtils.sign(map, ConstantsData.SECRET_VALUE);
        map.put("sign",sign);
        LLog.d(map+"");
        map.remove(ConstantsData.METHOD);

        RetrofitUtil.Api().requestPropertyPayAmount(map).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseTSubscriber<PaymentResult>(this) {
                    @Override
                    public void onResponseCallback(JSONObject response, String resultCode) {
                        LLog.d(response+"");
                        showProgress(false);
                    }
                    @Override
                    public void onNext(PaymentResult paymentResult) {
                        super.onNext(paymentResult);
                        if(TextUtils.equals("000",paymentResult.getCode())){
                            debt = paymentResult.getData().getDebt();
                            amount_text.setText(Utils.get2Double(debt) + "元");

                            housePayAdapter.setList(AppHolder.getInstance().getListPropertyPay());
                            listView.setAdapter(housePayAdapter);
                            housePayAdapter.notifyDataSetChanged();
                            Utils.toastError(HousePayActivity.this, "成功加入结算");
                            showProgress(false);
                        }
                    }
                });
    }
    /**获得物业账单id串*/
    private String getStrs(List<PropertyPay> listPropertyPay){
        String str = "";
        if(listPropertyPay != null && listPropertyPay.size() > 0){
            if(listPropertyPay.size() == 1){
                if(listPropertyPay.get(0).getPayFlg() == 1){
                    str = listPropertyPay.get(0).getPropertyPaymentId();
                }
            } else {
                StringBuilder sb = new StringBuilder();
                List<PropertyPay> list = new ArrayList<>();

                for(int i = 0 ; i < listPropertyPay.size() ; i ++){
                    if(listPropertyPay.get(i).getPayFlg() == 1){
                        list.add(listPropertyPay.get(i));
                    }
                }

                for(int i = 0 ; i < list.size() ; i ++){
                    if(i == (list.size() - 1)){
                        sb.append(list.get(i).getPropertyPaymentId());
                    } else {
                        sb.append(list.get(i).getPropertyPaymentId());
                        sb.append(",");
                    }
                }
                str = sb.toString();
            }
        }
        return str;
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        AppHolder.getInstance().setListPropertyPay(new ArrayList<PropertyPay>());
    }
}
