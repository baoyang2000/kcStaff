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
import com.ctrl.android.kcetong.ui.adapter.HousePayCartAdapter;
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

public class HousePayCartActivity extends BaseActivity {

    @BindView(R.id.listView)
    ListView listView;
    @BindView(R.id.amount_text)
    TextView amount_text;
    @BindView(R.id.house_pay_btn)
    TextView house_pay_btn;
    @BindView(R.id.toolbar_right_btn)
    TextView toolbar_right_btn;

    private String TITLE = StrConstant.HOUSE_PAY_TITLE;

    private String communityName;
    private String building_unit_room;

    private HousePayCartAdapter housePayCartAdapter;

    private List<PropertyPay> listPay = new ArrayList<>();

    private double debt;
    @Override
    protected void initView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_house_pay_cart);
        ButterKnife.bind(this);
        toolbarBaseSetting(TITLE, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HousePayCartActivity.this.finish();
            }
        });
        toolbar_right_btn.setVisibility(View.VISIBLE);
        toolbar_right_btn.setText(StrConstant.CLEAR);
    }

    @Override
    protected void initData() {
        amount_text.setText("0元");

        communityName = getIntent().getStringExtra("communityName");
        building_unit_room = getIntent().getStringExtra("building_unit_room");

        housePayCartAdapter = new HousePayCartAdapter(this);
        if((AppHolder.getInstance().getListPropertyPay()) != null && (AppHolder.getInstance().getListPropertyPay()).size() > 0){

            for(int i = 0 ; i < AppHolder.getInstance().getListPropertyPay().size() ; i ++){
                if(AppHolder.getInstance().getListPropertyPay().get(i).getPayFlg() == 1){
                    listPay.add(AppHolder.getInstance().getListPropertyPay().get(i));
                }
            }

            housePayCartAdapter.setList(listPay);
        }
        listView.setAdapter(housePayCartAdapter);

        calAmount();
    }

    @OnClick({R.id.house_pay_btn,R.id.toolbar_right_btn})
    public void onClick(View view ) {
        switch (view.getId()){
            case R.id.house_pay_btn:
                if(!S.isNull(getStrs(AppHolder.getInstance().getListPropertyPay()))){
                    Intent intent = new Intent(HousePayCartActivity.this, PayStyleActivity2.class);
                    intent.putExtra("orderIds",getStrs(AppHolder.getInstance().getListPropertyPay()));
                    intent.putExtra("communityName",communityName);
                    intent.putExtra("building_unit_room",building_unit_room);
                    intent.putExtra("debt",String.valueOf(debt));
                    startActivity(intent);
                    finish();
                } else {
                    Utils.toastError(this,"支付项目为空");
                }
                break;
            case R.id.toolbar_right_btn://清空
                for(int i = 0 ; i < AppHolder.getInstance().getListPropertyPay().size() ; i ++){
                    AppHolder.getInstance().getListPropertyPay().get(i).setPayFlg(0);
                }

                listPay = new ArrayList<PropertyPay>();
                housePayCartAdapter.setList(listPay);
                listView.setAdapter(housePayCartAdapter);
                housePayCartAdapter.notifyDataSetChanged();

                amount_text.setText("0元");
//                for(int i = 0;i <listPay.size();i++){
//                    listPay.get(i).setPayFlg(0);//清空后，设置为未加入结算状态
//                }
//                Intent data = new Intent();
//                data.putExtra("listPay",(Serializable) listPay);
//                setResult(RESULT_OK,data);
                break;
        }
    }
    /**
     * 计算 总金额
     * */
    public void calAmount(){
        //AppHolder.getInstance().getListPropertyPay().get(position).setPayFlg(1);
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

        final String sign = AopUtils.sign(map, ConstantsData.SECRET_VALUE);
        map.put("sign",sign);
        LLog.d(map + "");
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

                                showProgress(false);
                            }
                        }

                        @Override
                        public void onNetError(Throwable e) {
                            super.onNetError(e);
                            showProgress(false);
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
}
