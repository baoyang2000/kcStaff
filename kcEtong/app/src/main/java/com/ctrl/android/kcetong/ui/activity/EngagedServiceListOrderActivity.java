package com.ctrl.android.kcetong.ui.activity;

import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.ctrl.android.kcetong.R;
import com.ctrl.android.kcetong.model.AppHolder;
import com.ctrl.android.kcetong.toolkit.util.AopUtils;
import com.ctrl.android.kcetong.toolkit.util.ConstantsData;
import com.ctrl.android.kcetong.toolkit.util.InputMethodUtils;
import com.ctrl.android.kcetong.toolkit.util.LLog;
import com.ctrl.android.kcetong.toolkit.util.MessageUtils;
import com.ctrl.android.kcetong.toolkit.util.S;
import com.ctrl.android.kcetong.toolkit.util.StrConstant;
import com.ctrl.android.kcetong.toolkit.webutils.BaseTSubscriber;
import com.ctrl.android.kcetong.toolkit.webutils.RetrofitUtil;
import com.ctrl.android.kcetong.ui.base.BaseActivity;
import com.ctrl.android.kcetong.ui.view.TimePicker;

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

public class EngagedServiceListOrderActivity extends BaseActivity {

    @BindView(R.id.tv_order)//预约
            TextView     tv_order;
    @BindView(R.id.tv_name)//服务名称
            TextView     tv_name;
    @BindView(R.id.tv_price)//服务价格
            TextView     tv_price;
    @BindView(R.id.tv_time_content)//预约时间
            TextView     tv_time_content;
    @BindView(R.id.tv_backup_content)//备注
            EditText     tv_backup_content;
    @BindView(R.id.activity_layout)
            LinearLayout activity_layout;

    private String TITLE = StrConstant.ENGAGED_SERVICE;
    private View mMenuView;
    private TimePicker timePicker;
    private Button superman_ok_btn;
    private Button superman_cancel_btn;
    private String year;
    private String month;
    private String day;
    private String hour;
    private String minute;
    private String time_str;

    @Override
    protected void initView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_engaged_service_list_order);
        ButterKnife.bind(this);
        toolbarBaseSetting(TITLE, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EngagedServiceListOrderActivity.this.finish();
            }
        });
    }

    @Override
    protected void initData() {
        tv_name.setText("服务名称：" + getIntent().getStringExtra("name"));
        tv_price.setText("服务价格：" + getIntent().getStringExtra("price") + "元");
    }

    @OnClick({R.id.tv_time_content, R.id.tv_order})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_time_content:
                InputMethodUtils.hide(this);
                showTimePickerPop(tv_time_content);
                break;
            case R.id.tv_order:
                if(TextUtils.isEmpty(tv_time_content.getText().toString().trim())){
                    MessageUtils.showShortToast(this,"请选择预约时间");
                    return;
                }
                requestServiceAdd(getIntent().getStringExtra("id"),
                        AppHolder.getInstance().getHouse().getCommunityId(),
                        getIntent().getStringExtra("serviceKindId"),
                        AppHolder.getInstance().getProprietor().getProprietorId(),
                        AppHolder.getInstance().getHouse().getAddressId(),
                        AppHolder.getInstance().getMemberInfo().getMemberId(),
                        S.getStr(tv_time_content.getText().toString()) + ":00",
                        tv_backup_content.getText().toString().trim());
                break;
        }
    }
    /**
     * 服务预约
     * @param servicePeoductId 当前选中的服务Id
     * @param communityId 社区id
     * @param repairKindId 服务分类id
     * @param proprietorId 业主id
     * @param addressId 业主房屋id
     * @param memberId 会员id
     * @param appointmentTime 预约时间
     * @param content 备注
     * */
    public void requestServiceAdd(String servicePeoductId,String communityId,String repairKindId,String proprietorId,String addressId,String memberId,String appointmentTime,String content){
        showProgress(true);
        Map<String,String> map = new HashMap<String,String>();
        map.put(ConstantsData.METHOD,"pm.ppt.serviceOrder.add");//方法名称
        map.putAll(ConstantsData.getSystemParams());


        map.put("serviceProductId",servicePeoductId);
        map.put("communityId",communityId);
        map.put("repairKindId",repairKindId);
        map.put("proprietorId",proprietorId);
        map.put("addressId",addressId);
        map.put("memberId",memberId);
        map.put("appointmentTime",appointmentTime);
        map.put("content",content);

        String sign = AopUtils.sign(map, ConstantsData.SECRET_VALUE);
        map.put("sign",sign);

        RetrofitUtil.Api().requestServiceAdd(map).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseTSubscriber<ResponseBody>(this) {
                    @Override
                    public void onResponseCallback(JSONObject response, String resultCode) {
                        LLog.d(response+"");
                        showProgress(false);
                        try {
                            if(TextUtils.equals("000",response.getString("code"))){
                                MessageUtils.showShortToast(EngagedServiceListOrderActivity.this,"预约成功");
                                finish();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                });
    }
    /**
     * 显示时间拾取器的 弹出
     * */
    private void showTimePickerPop(final TextView editText){
        //setRoomData();
        mMenuView = LayoutInflater.from(EngagedServiceListOrderActivity.this).inflate(R.layout.choose_time_bottom_pop, null);
        timePicker = (TimePicker)mMenuView.findViewById(R.id.timePicker);
        superman_ok_btn = (Button)mMenuView.findViewById(R.id.superman_ok_btn);
        superman_cancel_btn = (Button)mMenuView.findViewById(R.id.superman_cancel_btn);

        final PopupWindow Pop = new PopupWindow();


        // 设置SelectPicPopupWindow的View
        Pop.setContentView(mMenuView);
        // 设置SelectPicPopupWindow弹出窗体的宽
        Pop.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        // 设置SelectPicPopupWindow弹出窗体的高
        Pop.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        // 设置SelectPicPopupWindow弹出窗体可点击
        Pop.setFocusable(true);
        // 设置SelectPicPopupWindow弹出窗体动画效果
        Pop.setAnimationStyle(R.style.AnimBottom);
        // 实例化一个ColorDrawable颜色为半透明
        ColorDrawable dw = new ColorDrawable(0xb0000000);
        // 设置SelectPicPopupWindow弹出窗体的背景
        Pop.setBackgroundDrawable(dw);

        superman_ok_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                year = timePicker.getYear();
                month = timePicker.getMonth();
                day = timePicker.getDay();
                hour = timePicker.getHour();
                minute = timePicker.getMinute();
                time_str = timePicker.getTime_string();
                Log.d("demo", "time_str: " + time_str);
                editText.setText(time_str);
                Pop.dismiss();
            }
        });

        superman_cancel_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Pop.dismiss();
            }
        });

        //mMenuView添加OnTouchListener监听判断获取触屏位置如果在选择框外面则销毁弹出框
        mMenuView.setOnTouchListener(new View.OnTouchListener() {

            public boolean onTouch(View v, MotionEvent event) {

                int height = mMenuView.findViewById(R.id.pop_layout).getTop();
                int y = (int) event.getY();
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (y < height) {
                        Pop.dismiss();
                    }
                }
                return true;
            }
        });
        mMenuView.setFocusable(true);
        mMenuView.setFocusableInTouchMode(true);
        Pop.setFocusable(true);
        Pop.showAtLocation(activity_layout, Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
    }
}
