package com.ctrl.android.kcetong.ui.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ctrl.android.kcetong.R;
import com.ctrl.android.kcetong.model.ServiceOrder;
import com.ctrl.android.kcetong.model.ServiceOrderResult;
import com.ctrl.android.kcetong.toolkit.util.AopUtils;
import com.ctrl.android.kcetong.toolkit.util.ConstantsData;
import com.ctrl.android.kcetong.toolkit.util.LLog;
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

public class MyOrderServiceObligationActivity extends BaseActivity {
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
    @BindView(R.id.rb_repairs_01)
    RadioButton    rbRepairs01;
    @BindView(R.id.rb_repairs_02)
    RadioButton    rbRepairs02;
    @BindView(R.id.rb_repairs_03)
    RadioButton    rbRepairs03;
    @BindView(R.id.et_my_repairs_pretreament_pingjia)
    EditText       etMyRepairsPretreamentPingjia;

    private String progressState;//状态

    private ServiceOrder serviceOrder;
    @Override
    protected void initView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_my_order_service_obligation);
        ButterKnife.bind(this);
        toolbarBaseSetting("预约详情", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyOrderServiceObligationActivity.this.finish();
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
        LLog.d(map + "");
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
//                                    tv_cancel.setVisibility(View.VISIBLE);
                                    tv_my_repairs_pretreament_progress.setText("服务进程：待受理");
                                    tv_my_repairs_pretreament_price.setText("服务价格："+serviceOrder.getSellingPrice()+"元");
                                    tv_my_repairs_pretreament_time.setText("预约时间：" + serviceOrder.getAppointmentTime());
                                    tv_my_repairs_pretreament_type.setText("服务名称："+serviceOrder.getProductName());
                                    tv_my_repairs_pretreament_room.setText("预约房间："+serviceOrder.getBuilding()+"-"+serviceOrder.getUnit()+"-"+serviceOrder.getRoom());
                                    tv_my_repairs_pretreament_content.setText(serviceOrder.getContent());
                                }

                                if("1".equals(progressState)){
                                    ll_my_repairs_wuye.setVisibility(View.GONE);
                                    tv_my_repairs_pretreament_progress.setText("服务进程：已受理");
                                    tv_my_repairs_pretreament_price.setText("服务价格："+serviceOrder.getSellingPrice()+"元");
                                    tv_my_repairs_pretreament_time.setText("预约时间："+serviceOrder.getAppointmentTime());
                                    tv_my_repairs_pretreament_type.setText("服务名称："+serviceOrder.getProductName());
                                    tv_my_repairs_pretreament_room.setText("预约房间："+serviceOrder.getBuilding()+"-"+serviceOrder.getUnit()+"-"+serviceOrder.getRoom());
                                    tv_my_repairs_pretreament_content.setText(serviceOrder.getContent());

                                }
                                if("2".equals(progressState)) {
                                    tv_my_repairs_pretreament_progress.setText("服务进程：服务中");
                                    tv_my_repairs_pretreament_wuye.setText("物业服务");
                                    rl_go_pay.setVisibility(View.VISIBLE);
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
    @OnClick({R.id.rb_repairs_01, R.id.rb_repairs_02, R.id.rb_repairs_03, R.id.tv_go_pay})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.rb_repairs_01:
                break;
            case R.id.rb_repairs_02:
                break;
            case R.id.rb_repairs_03:
                break;
            case R.id.tv_go_pay:
                break;
        }
    }
}
