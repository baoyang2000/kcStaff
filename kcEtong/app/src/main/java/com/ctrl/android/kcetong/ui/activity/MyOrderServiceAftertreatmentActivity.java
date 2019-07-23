package com.ctrl.android.kcetong.ui.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
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
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class MyOrderServiceAftertreatmentActivity extends BaseActivity {
    @BindView(R.id.tv_my_repairs_aftertreament_progress)//
            TextView     tv_my_repairs_aftertreament_progress;
    @BindView(R.id.tv_my_repairs_aftertreament_time)
            TextView         tv_my_repairs_aftertreament_time;
    @BindView(R.id.tv_my_repairs_aftertreament_type)
            TextView         tv_my_repairs_aftertreament_type;
    @BindView(R.id.tv_my_repairs_aftertreament_content)
            TextView         tv_my_repairs_aftertreament_content;
    @BindView(R.id.tv_my_repairs_aftertreament_wuye)
            TextView         tv_my_repairs_aftertreament_wuye;
    @BindView(R.id.tv_my_repairs_aftertreament_result)
            TextView         tv_my_repairs_aftertreament_result;
    @BindView(R.id.tv_my_repairs_aftertreament_pingjia)
            TextView         tv_my_repairs_aftertreament_pingjia;
    @BindView(R.id.tv_my_repairs_aftertreament_pingjia_content)
            TextView         tv_my_repairs_aftertreament_pingjia_content;
    @BindView(R.id.iv01_my_repairs_aftertreament)
            ImageView    iv01_my_repairs_aftertreament;
    @BindView(R.id.iv02_my_repairs_aftertreament)
            ImageView        iv02_my_repairs_aftertreament;
    @BindView(R.id.iv03_my_repairs_aftertreament)
            ImageView        iv03_my_repairs_aftertreament;
    @BindView(R.id.iv04_my_repairs_aftertreament)
            ImageView        iv04_my_repairs_aftertreament;
    @BindView(R.id.iv05_my_repairs_aftertreament)
            ImageView        iv05_my_repairs_aftertreament;
    @BindView(R.id.iv06_my_repairs_aftertreament)
            ImageView        iv06_my_repairs_aftertreament;
    @BindView(R.id.tv_my_repairs_aftertreament_room)
            TextView         tv_my_repairs_aftertreament_room;
    @BindView(R.id.tv_my_repairs_aftertreament_price)
            TextView         tv_my_repairs_aftertreament_price;
    @BindView(R.id.tv_baoxiu_image)
            TextView         tv_baoxiu_image;
    @BindView(R.id.tv_wuye_image)
            TextView         tv_wuye_image;
    @BindView(R.id.ll_yi_pingjia)
            LinearLayout ll_yi_pingjia;
    @BindView(R.id.ll_wei_pingjia)
            LinearLayout     ll_wei_pingjia;
    @BindView(R.id.rg_repairs)
            RadioGroup   rg_repairs;
    @BindView(R.id.rb_repairs_01)
            RadioButton  rb_repairs_01;
    @BindView(R.id.rb_repairs_02)
            RadioButton      rb_repairs_02;
    @BindView(R.id.rb_repairs_03)
            RadioButton      rb_repairs_03;

    @BindView(R.id.et_wei_pingjia_content)
    EditText et_wei_pingjia_content;
    private ServiceOrder serviceOrder;

    @Override
    protected void initView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_my_order_service_aftertreatment);
        ButterKnife.bind(this);
        toolbarBaseSetting("预约详情",new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyOrderServiceAftertreatmentActivity.this.finish();
            }
        });
    }

    @Override
    protected void initData() {
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

                                tv_my_repairs_aftertreament_progress.setText("预约进度：已结束");
                                tv_my_repairs_aftertreament_wuye.setText("物业服务");
                                tv_my_repairs_aftertreament_type.setText("服务名称："+serviceOrder.getProductName());
                                tv_my_repairs_aftertreament_price.setText("服务价格："+serviceOrder.getSellingPrice()+"元");
                                tv_my_repairs_aftertreament_time.setText("预约时间："+serviceOrder.getAppointmentTime());
                                tv_my_repairs_aftertreament_room.setText("预约房间："+serviceOrder.getBuilding()+"-"+serviceOrder.getUnit()+"-"+serviceOrder.getRoom());
                                tv_my_repairs_aftertreament_content.setText(serviceOrder.getContent());
                                tv_my_repairs_aftertreament_result.setText(serviceOrder.getResult());

                                if(serviceOrder.getEvaluateLevel().equals("0")){
                                    tv_my_repairs_aftertreament_pingjia.setText("非常满意");
                                    tv_my_repairs_aftertreament_pingjia_content.setText(serviceOrder.getEvaluateContent());
                                }
                                if(serviceOrder.getEvaluateLevel().equals("1")){
                                    tv_my_repairs_aftertreament_pingjia.setText("基本满意");
                                    tv_my_repairs_aftertreament_pingjia_content.setText(serviceOrder.getEvaluateContent());
                                }
                                if(serviceOrder.getEvaluateLevel().equals("2")){
                                    tv_my_repairs_aftertreament_pingjia.setText("不满意");
                                    tv_my_repairs_aftertreament_pingjia_content.setText(serviceOrder.getEvaluateContent());
                                }
                            }
                        }
                    });
    }
}
