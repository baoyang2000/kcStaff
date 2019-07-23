package com.ctrl.android.kcetong.ui.activity;

import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.ctrl.android.kcetong.R;
import com.ctrl.android.kcetong.model.PropertyHisPay;
import com.ctrl.android.kcetong.model.PropertyHisPayResult;
import com.ctrl.android.kcetong.toolkit.util.AopUtils;
import com.ctrl.android.kcetong.toolkit.util.ConstantsData;
import com.ctrl.android.kcetong.toolkit.util.D;
import com.ctrl.android.kcetong.toolkit.util.LLog;
import com.ctrl.android.kcetong.toolkit.util.S;
import com.ctrl.android.kcetong.toolkit.util.StrConstant;
import com.ctrl.android.kcetong.toolkit.util.Utils;
import com.ctrl.android.kcetong.toolkit.webutils.BaseTSubscriber;
import com.ctrl.android.kcetong.toolkit.webutils.RetrofitUtil;
import com.ctrl.android.kcetong.ui.adapter.HousePayHistoryAdapter;
import com.ctrl.android.kcetong.ui.adapter.ListItemAdapter;
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

public class HousePayHistoryActivity extends BaseActivity {
    @BindView(R.id.house_name_and_address)
    TextView house_name_and_address;
    @BindView(R.id.year_btn)
    TextView year_btn;
    @BindView(R.id.month_btn)
    TextView month_btn;
    @BindView(R.id.search_btn)
    TextView search_btn;
    @BindView(R.id.listView)
    ListView listView;

    private String TITLE = StrConstant.HOUSE_PAY_HISTORY_TITLE;

    private String communityName;
    private String building_unit_room;
    private String proprietorId;
    private String addressId;

    private ArrayList<String> listYear;
    private ArrayList<String> listMonth;

    private List<PropertyHisPay> listPropertyHisPay = new ArrayList<>();
    private ListItemAdapter        itemAdapter;
    private HousePayHistoryAdapter housePayHistoryAdapter;

    private View mMenuView;//显示pop的view
    @Override
    protected void initView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_house_pay_history);
        ButterKnife.bind(this);
        toolbarBaseSetting(TITLE, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HousePayHistoryActivity.this.finish();
            }
        });
        communityName = getIntent().getStringExtra("communityName");
        building_unit_room = getIntent().getStringExtra("building_unit_room");
        proprietorId = getIntent().getStringExtra("proprietorId");
        addressId = getIntent().getStringExtra("addressId");

        house_name_and_address.setText(communityName + "    " + building_unit_room);

    }

    @Override
    protected void initData() {

    }

    @OnClick({R.id.year_btn, R.id.month_btn, R.id.search_btn})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.year_btn:
                showYearListPop();
                break;
            case R.id.month_btn:
                showMonthListPop();
                break;
            case R.id.search_btn:
//                Utils.toastError(this, year_btn.getText().toString() + "-" + month_btn.getText().toString());
                if(checkInput()){

                    showProgress(true);
                    requestPayHistory(proprietorId,addressId,year_btn.getText().toString(),month_btn.getText().toString(),"","");
                }
                break;
        }
    }
    /**
     * 查询物业账单记录列表
     * @param proprietorId 业主id
     * @param addressId 地址id
     * @param year 年份（形式2014）
     * @param month 月份（形式01）
     * @param currentPageStr 当前页码
     * @param rowCountPerPageStr 每页条数
     * */
    public void requestPayHistory(String proprietorId,String addressId,String year
            ,String month,String currentPageStr,String rowCountPerPageStr){
        Map<String,String> map = new HashMap<String,String>();
        map.put(ConstantsData.METHOD,"pm.ppt.propertyPaymentRecord.queryPropertyPaymentRecordList");//方法名称
        map.putAll(ConstantsData.getSystemParams());

        map.put("proprietorId",proprietorId);
        map.put("addressId",addressId);
        map.put("year",year);
        map.put("month",month);
        map.put("currentPageStr",currentPageStr);
        map.put("rowCountPerPageStr",rowCountPerPageStr);

        String sign = AopUtils.sign(map, ConstantsData.SECRET_VALUE);
        map.put("sign",sign);
        LLog.d(map+"");
        map.remove(ConstantsData.METHOD);
        RetrofitUtil.Api().requestPayHistory(map).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseTSubscriber<PropertyHisPayResult>(this) {
                    @Override
                    public void onResponseCallback(JSONObject response, String resultCode) {
                        LLog.d(response+"");
                        showProgress(false);
                    }
                    @Override
                    public void onNext(PropertyHisPayResult propertyHisPayResult) {
                        super.onNext(propertyHisPayResult);
                        if(TextUtils.equals("000",propertyHisPayResult.getCode())){
                            listPropertyHisPay = propertyHisPayResult.getData().getRecordList();
                            housePayHistoryAdapter = new HousePayHistoryAdapter(HousePayHistoryActivity.this);
                            housePayHistoryAdapter.setList(listPropertyHisPay);
                            listView.setAdapter(housePayHistoryAdapter);
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
     * 显示年份的 popwindow
     * */
    private void showYearListPop(){

        listYear = new ArrayList<>();
        listYear = D.getRecent5Years();
        itemAdapter = new ListItemAdapter(this);
        itemAdapter.setList(listYear);

        mMenuView = LayoutInflater.from(HousePayHistoryActivity.this).inflate(R.layout.choose_list_pop, null);
        ListView listView = (ListView)mMenuView.findViewById(R.id.listView);
        listView.setAdapter(itemAdapter);

        final PopupWindow pop = new PopupWindow();

        // 设置SelectPicPopupWindow的View
        pop.setContentView(mMenuView);
        // 设置SelectPicPopupWindow弹出窗体的宽
        pop.setWidth(year_btn.getWidth());
        // 设置SelectPicPopupWindow弹出窗体的高
        pop.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        // 设置SelectPicPopupWindow弹出窗体可点击
        pop.setFocusable(true);
        // 设置SelectPicPopupWindow弹出窗体动画效果
        //Pop.setAnimationStyle(R.style.AnimBottom);
        // 实例化一个ColorDrawable颜色为半透明
        ColorDrawable dw = new ColorDrawable(0xb0000000);
        // 设置SelectPicPopupWindow弹出窗体的背景
        pop.setBackgroundDrawable(dw);

        mMenuView.setFocusable(true);
        mMenuView.setFocusableInTouchMode(true);
        pop.setFocusable(true);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //MessageUtils.showShortToast(HousePayHistoryActivity.this, listYear.get(position));
                year_btn.setText(S.getStr(listYear.get(position)));
                pop.dismiss();
            }
        });

        int[] location = new int[2];
        year_btn.getLocationOnScreen(location);
        //Pop.showAtLocation(year_btn, Gravity.NO_GRAVITY, location[0], location[1] - Pop.getHeight());
        pop.showAsDropDown(year_btn);
        //Pop.showAtLocation(mall_main_pop_layout, Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
    }


    /**
     * 显示月份的 popwindow
     * */
    private void showMonthListPop(){

        listMonth = new ArrayList<>();
        listMonth = D.getAllMonths();

        itemAdapter = new ListItemAdapter(this);
        itemAdapter.setList(listMonth);

        mMenuView = LayoutInflater.from(HousePayHistoryActivity.this).inflate(R.layout.choose_list_pop, null);
        ListView listView = (ListView)mMenuView.findViewById(R.id.listView);
        listView.setAdapter(itemAdapter);



        final PopupWindow pop = new PopupWindow();

        // 设置SelectPicPopupWindow的View
        pop.setContentView(mMenuView);
        // 设置SelectPicPopupWindow弹出窗体的宽
        pop.setWidth(month_btn.getWidth());
        // 设置SelectPicPopupWindow弹出窗体的高
        pop.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        // 设置SelectPicPopupWindow弹出窗体可点击
        pop.setFocusable(true);
        // 设置SelectPicPopupWindow弹出窗体动画效果
        //Pop.setAnimationStyle(R.style.AnimBottom);
        // 实例化一个ColorDrawable颜色为半透明
        ColorDrawable dw = new ColorDrawable(0xb0000000);
        // 设置SelectPicPopupWindow弹出窗体的背景
        pop.setBackgroundDrawable(dw);

        mMenuView.setFocusable(true);
        mMenuView.setFocusableInTouchMode(true);
        pop.setFocusable(true);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //MessageUtils.showShortToast(HousePayHistoryActivity.this, listMonth.get(position));
                month_btn.setText(S.getStr(listMonth.get(position)));
                pop.dismiss();
            }
        });

        int[] location = new int[2];
        month_btn.getLocationOnScreen(location);
        //Pop.showAtLocation(year_btn, Gravity.NO_GRAVITY, location[0], location[1] - Pop.getHeight());
        pop.showAsDropDown(month_btn);
        //Pop.showAtLocation(mall_main_pop_layout, Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
    }

    private boolean checkInput(){

        if(S.isNull(year_btn.getText().toString()) || (year_btn.getText().toString()).equals("年份")){
            Utils.toastError(this,"未选择年份");
            return false;
        }

        if(S.isNull(month_btn.getText().toString()) || (month_btn.getText().toString()).equals("年份")){
            Utils.toastError(this,"未选择月份");
            return false;
        }

        return true;
    }
}
