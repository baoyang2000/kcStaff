package com.ctrl.android.kcetong.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.ctrl.android.kcetong.R;
import com.ctrl.android.kcetong.listener.HintDialogListener;
import com.ctrl.android.kcetong.model.AddressList;
import com.ctrl.android.kcetong.model.AppHolder;
import com.ctrl.android.kcetong.model.ReceiveAddress;
import com.ctrl.android.kcetong.toolkit.Url.Url;
import com.ctrl.android.kcetong.toolkit.util.AopUtils;
import com.ctrl.android.kcetong.toolkit.util.ConstantsData;
import com.ctrl.android.kcetong.toolkit.util.LLog;
import com.ctrl.android.kcetong.toolkit.util.Utils;
import com.ctrl.android.kcetong.toolkit.webutils.BaseTSubscriber;
import com.ctrl.android.kcetong.toolkit.webutils.RetrofitUtil;
import com.ctrl.android.kcetong.ui.adapter.AddressAdapter;
import com.ctrl.android.kcetong.ui.base.BaseActivity;
import com.ctrl.android.kcetong.ui.dialog.HintMessageDialog;

import org.json.JSONObject;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.ResponseBody;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class OrderAddressActivity extends BaseActivity implements AddressAdapter.OnClickButtonListener{

    @BindView(R.id.toolbar_right_btn)
    TextView     toolbarRightBtn;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    private AddressAdapter adapter;
    private String SELECT ;
    @Override
    protected void initView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_order_address);
        ButterKnife.bind(this);
        toolbarBaseSetting("地址管理", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OrderAddressActivity.this.finish();
            }
        });
        toolbarRightBtn.setVisibility(View.VISIBLE);
        toolbarRightBtn.setText("新建");
        if(getIntent() != null && getIntent().getStringExtra("select") != null){
            SELECT = getIntent().getStringExtra("select");
        }
    }

    @Override
    protected void initData() {
        adapter = new AddressAdapter(this);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        adapter.setListener(this);
        recyclerView.setAdapter(adapter);
        getDelivery();
    }
    private void getDelivery() {
        showProgress(true);
        Map<String, String> map = new HashMap();
        map.putAll(ConstantsData.getSystemParams());
        map.put(ConstantsData.METHOD, Url.addressListUrl);
        map.put("memberId", holder.getMemberInfo().getMemberId());
        map.put("currentPage", "1");
        map.put("rowCountPerPage", "1000");
        String sign = AopUtils.sign(map, ConstantsData.SECRET_VALUE);
        map.put("sign", sign);
        map.remove(ConstantsData.METHOD);
        LLog.d(map + "");
        RetrofitUtil.Api().getAddressList(map).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new BaseTSubscriber<AddressList>(this) {
            @Override
            public void onResponseCallback(JSONObject response, String resultCode) {
                LLog.d("onResponseCallback: " + response);
            }

            @Override
            public void onNext(AddressList bean) {
                super.onNext(bean);
                showProgress(false);
                if (TextUtils.equals("000", bean.getCode())) {
                    adapter.setDataList(bean.getData().getReceiveAddressList());
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onNetError(Throwable e) {
                showProgress(false);
            }

        });
    }

    @OnClick(R.id.toolbar_right_btn)
    public void onViewClicked() {
        Intent intent = new Intent(this, CreateNewAddressActivity.class);
        startActivityForResult(intent, 101);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 101 && resultCode == RESULT_OK) {
            getDelivery();
        }
    }

    @Override
    public void edit(int position, ReceiveAddress data) {
        Intent intent = new Intent(this, CreateNewAddressActivity.class);
        intent.putExtra("data", (Serializable) data);
        startActivityForResult(intent, 101);
    }

    @Override
    public void delete(int position, final String id) {
        final HintMessageDialog dialog = new HintMessageDialog(this);
        dialog.showDefaultDialogVariableButtons("是否删除该地址?", "", new HintDialogListener() {
            @Override
            public void submitListener() {
                showProgress(true);
                Map<String, String> map = new HashMap();
                map.putAll(ConstantsData.getSystemParams());
                map.put(ConstantsData.METHOD, Url.deleteAddressUrl);
                map.put("receiveAddressId", id);
                String sign = AopUtils.sign(map, ConstantsData.SECRET_VALUE);
                map.put("sign", sign);
                map.remove(ConstantsData.METHOD);
                LLog.d(map + "");
                RetrofitUtil.Api().deleteAddress(map).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new BaseTSubscriber<ResponseBody>(OrderAddressActivity.this) {
                    @Override
                    public void onResponseCallback(JSONObject response, String resultCode) {
                        LLog.d("onResponseCallback: " + response);
                        showProgress(false);
                        if (TextUtils.equals(resultCode, ConstantsData.success)) {
                            Utils.toastError(OrderAddressActivity.this, "删除地址成功");
                            getDelivery();
                        }
                    }

                    @Override
                    public void onNetError(Throwable e) {
                        showProgress(false);
                    }

                });
            }

            @Override
            public void cancelListener() {
                dialog.dismiss();
            }
        }, "取消", "确定");
    }

    @Override
    public void select(int position, ReceiveAddress data) {
        if("1".equals(SELECT)){
            Intent intent = new Intent();
            intent.putExtra("receiveAddresse", data);
            AppHolder.getInstance().setDefaultReceiveAddress(data);
            setResult(4, intent);
            finish();
        }
    }

    /**
     * 设置默认地址
     */
    @Override
    public void check(String isDefault, String id) {
        if(!"1".equals(isDefault)){
            showProgress(true);
            Map<String, String> map = new HashMap();
            map.putAll(ConstantsData.getSystemParams());
            map.put(ConstantsData.METHOD, Url.setDefaultAddressUrl);
            map.put("receiveAddressId", id);
            map.put("memberId", holder.getMemberInfo().getMemberId());
            String sign = AopUtils.sign(map, ConstantsData.SECRET_VALUE);
            map.put("sign", sign);
            map.remove(ConstantsData.METHOD);
            LLog.d(map + "");
            RetrofitUtil.Api().setDefaultAddress(map).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new BaseTSubscriber<ResponseBody>(OrderAddressActivity.this) {
                @Override
                public void onResponseCallback(JSONObject response, String resultCode) {
                    showProgress(false);
                    if (TextUtils.equals(resultCode, ConstantsData.success)) {
                        Utils.toastError(OrderAddressActivity.this, "设置默认收货地址成功");
                        getDelivery();
                    }else {
                        Utils.toastError(OrderAddressActivity.this, response.optString("description"));
                    }
                }

                @Override
                public void onNetError(Throwable e) {
                    showProgress(false);
                }

            });
        }
    }
}
