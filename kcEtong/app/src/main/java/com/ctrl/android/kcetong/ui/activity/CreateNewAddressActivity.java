package com.ctrl.android.kcetong.ui.activity;

import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ctrl.android.kcetong.R;
import com.ctrl.android.kcetong.model.ReceiveAddress;
import com.ctrl.android.kcetong.toolkit.Url.Url;
import com.ctrl.android.kcetong.toolkit.util.AopUtils;
import com.ctrl.android.kcetong.toolkit.util.ConstantsData;
import com.ctrl.android.kcetong.toolkit.util.LLog;
import com.ctrl.android.kcetong.toolkit.util.MessageUtils;
import com.ctrl.android.kcetong.toolkit.util.S;
import com.ctrl.android.kcetong.toolkit.util.Utils;
import com.ctrl.android.kcetong.toolkit.webutils.BaseTSubscriber;
import com.ctrl.android.kcetong.toolkit.webutils.RetrofitUtil;
import com.ctrl.android.kcetong.ui.base.BaseActivity;
import com.ctrl.android.kcetong.ui.view.CityPicker;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.ResponseBody;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class CreateNewAddressActivity extends BaseActivity {
    @BindView(R.id.ll_linear_layout)
    LinearLayout linearLayout;
    @BindView(R.id.toolbar_right_btn)
    TextView toolbarRightBtn;
    @BindView(R.id.et_receiver)
    EditText etReceiver;
    @BindView(R.id.et_phone)
    EditText etPhone;
    @BindView(R.id.tv_area_title)
    TextView tvAreaTitle;
    @BindView(R.id.tv_area)
    TextView tvArea;
    @BindView(R.id.layout_area)
    RelativeLayout layoutArea;
    @BindView(R.id.et_address)
    EditText etAddress;

    private View mMenuView;
    private CityPicker cityPicker;
    private Button superman_ok_btn;
    private Button superman_cancel_btn;

    private String province;
    private String city;
    private String couny;
    PopupWindow Pop = new PopupWindow();
    private String id;

    @Override
    protected void initView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_create_new_address);
        ButterKnife.bind(this);
        toolbarBaseSetting("新建地址", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CreateNewAddressActivity.this.finish();
            }
        });
        toolbarRightBtn.setVisibility(View.VISIBLE);
        toolbarRightBtn.setText("保存");
    }

    @Override
    protected void initData() {
        if (null != getIntent() && null != getIntent().getSerializableExtra("data")) {
            ReceiveAddress data = (ReceiveAddress) getIntent().getSerializableExtra("data");
            etReceiver.setText(data.getReceiveName());
            etPhone.setText(data.getMobile());
            tvArea.setText(data.getProvinceName() + data.getCityName() + data.getAreaName());
            etAddress.setText(data.getAddress());
            id = data.getReceiveAddressId();
            province = data.getProvinceName();
            city = data.getCityName();
            couny = data.getAreaName();
        }
    }

    @OnClick({R.id.toolbar_right_btn, R.id.layout_area})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.toolbar_right_btn:
                if (TextUtils.isEmpty(tvArea.getText().toString())) {
                    MessageUtils.showShortToast(this, "请选择所在地区");
                    return;
                }
                if (TextUtils.isEmpty(etAddress.getText().toString().trim())) {
                    MessageUtils.showShortToast(this, "请填写详细地址");
                    return;
                }
                if (TextUtils.isEmpty(etReceiver.getText().toString().trim())) {
                    MessageUtils.showShortToast(this, "请填写收货人姓名");
                    return;
                }
                if (TextUtils.isEmpty(etPhone.getText().toString().trim())) {
                    MessageUtils.showShortToast(this, "请填写收货人联系电话");
                    return;
                }
                if (S.isNull(id)) {
                    addAddress();
                } else {
                    modifyAddress();
                }
                break;
            case R.id.layout_area:
                showPopupBottom();
                break;
        }
    }

    private void modifyAddress() {
        showProgress(true);
        Map<String, String> params = new HashMap();
        params.putAll(ConstantsData.getSystemParams());
        params.put(ConstantsData.METHOD, Url.modifyAddressUrl);
        params.put("receiveAddressId", id);
        params.put("memberId", holder.getMemberInfo().getMemberId());
        params.put("longitude", "");
        params.put("latitude", "");
        params.put("provinceName", province);
        params.put("cityName", city);
        params.put("areaName", couny);
        params.put("streetName", "");
        params.put("customize", etAddress.getText().toString());
        params.put("zip", "");
        params.put("mobile", etPhone.getText().toString());
        params.put("receiveName", etReceiver.getText().toString());
        String sign = AopUtils.sign(params, ConstantsData.SECRET_VALUE);
        params.put("sign", sign);
        params.remove(ConstantsData.METHOD);
        LLog.d(params + "");
        RetrofitUtil.Api().modifyAddress(params).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new BaseTSubscriber<ResponseBody>(this) {
            @Override
            public void onResponseCallback(JSONObject response, String resultCode) {
                LLog.d("onResponseCallback: " + response);
                showProgress(false);
                if (TextUtils.equals(resultCode, ConstantsData.success)) {
                    Utils.toastError(CreateNewAddressActivity.this, "修改地址成功");
                    setResult(RESULT_OK);
                    finish();
                }
            }

            @Override
            public void onError(Throwable e) {
                super.onError(e);
            }

            @Override
            public void onNetError(Throwable e) {
                showProgress(false);
            }
        });
    }

    private void addAddress() {
        showProgress(true);
        Map<String, String> params = new HashMap();
        params.putAll(ConstantsData.getSystemParams());
        params.put(ConstantsData.METHOD, Url.addAddressUrl);
        params.put("memberId", holder.getMemberInfo().getMemberId());
        params.put("longitude", "");
        params.put("latitude", "");
        params.put("provinceName", province);
        params.put("cityName", city);
        params.put("areaName", couny);
        params.put("streetName", "");
        params.put("customize", etAddress.getText().toString());
        params.put("zip", "");
        params.put("mobile", etPhone.getText().toString());
        params.put("receiveName", etReceiver.getText().toString());
        String sign = AopUtils.sign(params, ConstantsData.SECRET_VALUE);
        params.put("sign", sign);
        params.remove(ConstantsData.METHOD);
        LLog.d(params + "");
        RetrofitUtil.Api().addAddress(params).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new BaseTSubscriber<ResponseBody>(this) {
            @Override
            public void onResponseCallback(JSONObject response, String resultCode) {
                LLog.d("onResponseCallback: " + response);
                showProgress(false);
                if (TextUtils.equals(resultCode, ConstantsData.success)) {
                    Utils.toastError(CreateNewAddressActivity.this, "添加地址成功");
                    setResult(RESULT_OK);
                    finish();
                }
            }

            @Override
            public void onNetError(Throwable e) {
                showProgress(false);
            }
        });
    }

    private void showPopupBottom() {
        mMenuView = LayoutInflater.from(this).inflate(R.layout.choose_city_bottom_pop, null);
        cityPicker = (CityPicker) mMenuView.findViewById(R.id.cityPicker);
        superman_ok_btn = (Button) mMenuView.findViewById(R.id.superman_ok_btn);
        superman_cancel_btn = (Button) mMenuView.findViewById(R.id.superman_cancel_btn);

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
                //MessageUtils.showShortToast(AddAddressActivity.this,"确定" + cityPicker.getCity_string());
//                mAddressCity.setText(cityPicker.getCity_string());
                province = cityPicker.getProvince_Name();
                city = cityPicker.getCity_Name();
                couny = cityPicker.getCouny_Name();
                tvArea.setText(province + city + couny);
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
        Pop.showAtLocation(linearLayout, Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
    }
}
