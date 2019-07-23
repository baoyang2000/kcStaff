package com.ctrl.android.kcetong.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.ctrl.android.kcetong.R;
import com.ctrl.android.kcetong.model.AppHolder;
import com.ctrl.android.kcetong.model.House;
import com.ctrl.android.kcetong.model.HouseResult;
import com.ctrl.android.kcetong.toolkit.util.AopUtils;
import com.ctrl.android.kcetong.toolkit.util.ConstantsData;
import com.ctrl.android.kcetong.toolkit.util.LLog;
import com.ctrl.android.kcetong.toolkit.util.StrConstant;
import com.ctrl.android.kcetong.toolkit.util.Utils;
import com.ctrl.android.kcetong.toolkit.webutils.BaseTSubscriber;
import com.ctrl.android.kcetong.toolkit.webutils.RetrofitUtil;
import com.ctrl.android.kcetong.ui.adapter.PropertyPaymentAdapter;
import com.ctrl.android.kcetong.ui.base.BaseActivity;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;


public class PropertyPaymentActivity extends BaseActivity implements View.OnClickListener{

    @BindView(R.id.listView)
    ListView listView;
    @BindView(R.id.toolbar_right_btn)
    TextView toolbar_right_btn;

    private List<House> listHouse = new ArrayList<>();

    private String TITLE = StrConstant.HOUSES_TITLE;
    private PropertyPaymentAdapter houseListAdapter;

    @Override
    protected void initView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_property_payment);
        ButterKnife.bind(this);
        toolbarBaseSetting(TITLE, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PropertyPaymentActivity.this.finish();
            }
        });
        toolbar_right_btn.setVisibility(View.VISIBLE);
        toolbar_right_btn.setText(StrConstant.ADD);
        toolbar_right_btn.setOnClickListener(this);
    }

    @Override
    protected void initData() {
        showProgress(true);
        String             memberId = AppHolder.getInstance().getMemberInfo().getMemberId();
        Map<String,String> map      = new HashMap<>();
        map.put(ConstantsData.METHOD,"pm.ppt.memberBind.list");//方法名称

        map.put("memberId",memberId);
        map.put("currentPage","");
        map.put("rowCountPerPage","");
        map.putAll(ConstantsData.getSystemParams());
        String sign = AopUtils.sign(map, ConstantsData.SECRET_VALUE);
        map.put("sign",sign);
        LLog.d(map + "");
        map.remove(ConstantsData.METHOD);
        /*
         * 获取房屋列表*/
        RetrofitUtil.Api().requestHouseList(map).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new BaseTSubscriber<HouseResult>(this) {
                        @Override
                        public void onResponseCallback(JSONObject response, String resultCode) {
                            LLog.d(response+"");
                            showProgress(false);
                        }
                        @Override
                        public void onNext(HouseResult houseResult) {
                            super.onNext(houseResult);
                            if(TextUtils.equals("000",houseResult.getCode())){

                                listHouse = houseResult.getData().getHousesList();

                                houseListAdapter = new PropertyPaymentAdapter(PropertyPaymentActivity.this);
                                houseListAdapter.setList(listHouse);
                                listView.setAdapter(houseListAdapter);
                                listView.setDivider(null);
                                listView.setDividerHeight(20);
//                            if(listHouse.size() > 0){
//                                for(int i = 0 ; i < listHouse.size() ; i ++){
//                                    if(listHouse.get(i).getIsDefault() == 1){
//                                        AppHolder.getInstance().setHouse(listHouse.get(i));
//                                        AppHolder.getInstance().getProprietor().setProprietorId(listHouse.get(i).getProprietorId());
//                                        AppHolder.getInstance().getCommunity().setCommunityName(listHouse.get(i).getCommunityName());
//                                        AppHolder.getInstance().getCommunity().setId(listHouse.get(i).getCommunityId());
//                                    }
//                                }
//                            } else {
//                                AppHolder.getInstance().setHouse(new House());
//                                AppHolder.getInstance().getProprietor().setProprietorId("");
//                            }
                                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                    @Override
                                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                        //MessageUtils.showShortToast(HouseListActivity.this, listHouse.get(position).getCommunityName());
                                        Intent intent = new Intent(PropertyPaymentActivity.this, HousePayActivity.class);
                                        intent.putExtra("communityName",listHouse.get(position).getCommunityName());
                                        intent.putExtra("building_unit_room",(listHouse.get(position).getBuilding() + "-" + listHouse.get(position).getUnit() + "-" + listHouse.get(position).getRoom()));
                                        intent.putExtra("proprietorId",listHouse.get(position).getProprietorId());
                                        intent.putExtra("addressId",listHouse.get(position).getAddressId());
                                        startActivity(intent);
                                    }
                                });
                            }
                            showProgress(false);
                        }
                    });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.toolbar_right_btn:
                if("0".equals(AppHolder.getInstance().getMemberInfo().getSupers())){
                    Intent intent = new Intent(PropertyPaymentActivity.this, HouseConfirmActivity.class);
                    startActivity(intent);
                }else if("1".equals(AppHolder.getInstance().getMemberInfo().getSupers())
                         || "2".equals(AppHolder.getInstance().getMemberInfo().getSupers())){
                    Utils.showShortToast(PropertyPaymentActivity.this, getString(R.string.manager_cannot));
                }
                break;
        }
    }
}
