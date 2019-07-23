package com.ctrl.android.kcetong.ui.activity;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.ctrl.android.kcetong.R;
import com.ctrl.android.kcetong.model.AppHolder;
import com.ctrl.android.kcetong.model.BuildingListBean;
import com.ctrl.android.kcetong.model.House;
import com.ctrl.android.kcetong.model.HouseBean;
import com.ctrl.android.kcetong.model.Proprietorinfo;
import com.ctrl.android.kcetong.model.RoomListBean;
import com.ctrl.android.kcetong.model.UnitListBean;
import com.ctrl.android.kcetong.toolkit.Url.Url;
import com.ctrl.android.kcetong.toolkit.util.AopUtils;
import com.ctrl.android.kcetong.toolkit.util.ConstantsData;
import com.ctrl.android.kcetong.toolkit.util.S;
import com.ctrl.android.kcetong.toolkit.util.StrConstant;
import com.ctrl.android.kcetong.toolkit.util.Utils;
import com.ctrl.android.kcetong.toolkit.webutils.BaseTSubscriber;
import com.ctrl.android.kcetong.toolkit.webutils.RetrofitUtil;
import com.ctrl.android.kcetong.ui.adapter.ListCommunityAdapter;
import com.ctrl.android.kcetong.ui.adapter.ListItemAdapter;
import com.ctrl.android.kcetong.ui.base.BaseActivity;
import com.ctrl.android.kcetong.ui.view.CityPicker;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.ResponseBody;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class HouseConfirmActivity extends BaseActivity {

    @BindView(R.id.mall_main_pop_layout)
    LinearLayout mall_main_pop_layout;

    @BindView(R.id.owner_name)//户主姓名
            EditText owner_name;
    @BindView(R.id.owner_mobile)//户主手机
            EditText owner_mobile;
    @BindView(R.id.owner_P_c_d)//省市区
            TextView owner_P_c_d;
    @BindView(R.id.owner_community)//小区
            TextView owner_community;

    @BindView(R.id.building_btn)//楼栋/弄
            LinearLayout building_btn;
    @BindView(R.id.building)//楼栋/弄
            TextView     building;
    @BindView(R.id.unit_btn)//单元/号
            LinearLayout unit_btn;
    @BindView(R.id.unit)//楼栋/弄
            TextView     unit;
    @BindView(R.id.room_btn)//门牌/房号
            LinearLayout room_btn;
    @BindView(R.id.room)//楼栋/弄
            TextView     room;

    @BindView(R.id.confirm_btn)//认证按钮
            Button confirm_btn;
    private int flg = 0;//flg为0时可以显示小区列表
    private View       mMenuView;//显示pop的view
    private CityPicker cityPicker;
    private Button     superman_ok_btn;
    private Button     superman_cancel_btn;
    private String province = "";
    private String city     = "";
    private String couny    = "";
    private List<House>      list;
    private String               communityId;
    private ListCommunityAdapter listCommunityAdapter;
    private List<String>         buildList;
    private List<String>         unitList;
    private List<String>         roomList;
    private ListItemAdapter      listItemAdapter;
    private String               building_num;
    private String               unit_num;
    private String               name_arg;
    private String               mobile_arg;
    private String               memberId;
    private String               room_num;
    private String TITLE = StrConstant.FAMILY_CONFIRM;

    @Override
    protected void initView(Bundle savedInstanceState) {

        setContentView(R.layout.activity_house_confirm);
        ButterKnife.bind(this);
        toolbarBaseSetting(StrConstant.FAMILY_CONFIRM, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HouseConfirmActivity.this.finish();
            }
        });
    }

    @Override
    protected void initData() {

        confirm_btn.setBackgroundResource(R.drawable.orange_bg_shap);
        list = new ArrayList<>();
        buildList = new ArrayList<>();
        unitList = new ArrayList<>();
        roomList = new ArrayList<>();
        memberId = AppHolder.getInstance().getMemberInfo().getMemberId();
    }

    @OnClick({R.id.building_btn, R.id.unit_btn, R.id.room_btn, R.id.confirm_btn, R.id.owner_P_c_d, R.id.owner_community})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.owner_P_c_d:
                showPopupBottom();
                break;
            case R.id.owner_community:
                if (S.isNull(province) || S.isNull(city) || S.isNull(couny)) {
                    Utils.showShortToast(HouseConfirmActivity.this, "请选择省市区");
                } else {
                    communityList();
                }
                break;
            case R.id.building_btn:
                if (TextUtils.isEmpty(communityId)) {
                    Utils.showShortToast(this, "请选择小区");
                    unit.setText("");
                    room.setText("");
                } else {
                    buildingList();
                }
                break;
            case R.id.unit_btn:
                if (communityId == null || communityId.equals("")) {
                    Utils.showShortToast(this, "请选择小区");
                    room.setText("");
                } else {
                    building_num = building.getText().toString();
                    if (building_num == null || building_num.equals("") || building_num.equals("请选择")) {
                        Utils.showShortToast(this, "请选择楼号");
                    } else {
                        unitList();
                    }
                }
                break;
            case R.id.room_btn:
                if (communityId == null || communityId.equals("")) {
                    Utils.showShortToast(this, "请选择小区");
                } else {
                    building_num = building.getText().toString();
                    if (building_num == null || building_num.equals("") || building_num.equals("请选择")) {
                        Utils.showShortToast(this, "请选择楼号");
                    } else {
                        unit_num = unit.getText().toString();
                        if (unit_num == null || unit_num.equals("") || unit_num.equals("请选择")) {
                            Utils.showShortToast(this, "请选择单元号");
                        } else {
                            roomList();
                        }

                    }
                }
                break;
            case R.id.confirm_btn:
                if (checkInput()) {
                    name_arg = owner_name.getText().toString();
                    mobile_arg = owner_mobile.getText().toString();
                    proprietyVerify();
                }
                break;
        }
    }

    /**
     * 显示省市区pop
     */
    private void showPopupBottom() {
        mMenuView = LayoutInflater.from(this).inflate(R.layout.choose_city_bottom_pop, null);
        cityPicker = (CityPicker) mMenuView.findViewById(R.id.cityPicker);
        superman_ok_btn = (Button) mMenuView.findViewById(R.id.superman_ok_btn);
        superman_cancel_btn = (Button) mMenuView.findViewById(R.id.superman_cancel_btn);
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
                //MessageUtils.showShortToast(AddAddressActivity.this,"确定" + cityPicker.getCity_string());
                owner_P_c_d.setText(cityPicker.getCity_string());
                province = cityPicker.getProvince_Name();
                city = cityPicker.getCity_Name();
                couny = cityPicker.getCouny_Name();
                owner_community.setText("");
                building.setText("");
                unit.setText("");
                room.setText("");
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
                int y      = (int) event.getY();
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
        Pop.showAtLocation(mall_main_pop_layout, Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
    }

    /**
     * 小区列表
     */
    private void communityList() {

        showProgress(true);
        Map<String, String> map = new HashMap();
        map.putAll(ConstantsData.getSystemParams());
        map.put(ConstantsData.METHOD, Url.communityList);
        map.put("provinceName", province);
        map.put("cityName", city);
        map.put("areaName", couny);
        map.put("keyword", "");
        String sign = AopUtils.sign(map, ConstantsData.SECRET_VALUE);
        map.put("sign", sign);
        map.remove(ConstantsData.METHOD);

        RetrofitUtil.Api().communityList(map).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new BaseTSubscriber<HouseBean>(this) {
            @Override
            public void onResponseCallback(JSONObject response, String resultCode) {

            }

            @Override
            public void onNext(HouseBean houseBean) {
                super.onNext(houseBean);

                if (TextUtils.equals(houseBean.getCode(), ConstantsData.success)) {
                    list = houseBean.getData().getCommunityList();
                }
                if (list != null && list.size() > 0) {

                    if (!TextUtils.isEmpty(owner_community.getText().toString())) {
                        for (int i = 0; i < list.size(); i++) {
                            House house = list.get(i);
                            if ((owner_community.getText().toString()).equals(house.getCommunityName())) {
                                communityId = house.getId();
                                break;
                            }
                        }
                    }
                }
                if(TextUtils.equals(houseBean.getCode(), "002")){
                    Utils.showShortToast(HouseConfirmActivity.this, "暂无小区");
                }
                showProgress(false);
                showCommunityListPop(list);
            }
        });
    }

    /**
     * 显示小区列表的 popwindow
     */
    private void showCommunityListPop(final List<House> list) {

        listCommunityAdapter = new ListCommunityAdapter(this);
        listCommunityAdapter.setList(list);

        mMenuView = LayoutInflater.from(HouseConfirmActivity.this).inflate(R.layout.choose_list_pop, null);
        ListView listView = (ListView) mMenuView.findViewById(R.id.listView);
        listView.setAdapter(listCommunityAdapter);

        final PopupWindow pop = new PopupWindow();

        // 设置SelectPicPopupWindow的View
        pop.setContentView(mMenuView);
        // 设置SelectPicPopupWindow弹出窗体的宽
        pop.setWidth(owner_community.getWidth());
        // 设置SelectPicPopupWindow弹出窗体的高
        pop.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        // 设置SelectPicPopupWindow弹出窗体可点击
        pop.setFocusable(true);
        // 设置SelectPicPopupWindow弹出窗体动画效果
        pop.setAnimationStyle(R.style.AnimBottom);
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
                //MessageUtils.showShortToast(HouseConfirmActivity.this, listYear.get(position));
                flg = 1;
                owner_community.setText(S.getStr(list.get(position).getCommunityName()));
                communityId = list.get(position).getId();
                building.setText("");
                unit.setText("");
                room.setText("");
                pop.dismiss();
            }
        });

        //mMenuView添加OnTouchListener监听判断获取触屏位置如果在选择框外面则销毁弹出框
        mMenuView.setOnTouchListener(new View.OnTouchListener() {

            public boolean onTouch(View v, MotionEvent event) {

                int height = mMenuView.findViewById(R.id.pop_layout).getTop();
                int y      = (int) event.getY();
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (y < height) {
                        pop.dismiss();
                    }
                }
                return true;
            }
        });
        mMenuView.setFocusable(true);
        mMenuView.setFocusableInTouchMode(true);
        pop.setFocusable(true);

        int[] location = new int[2];
        owner_community.getLocationOnScreen(location);
        //Pop.showAtLocation(year_btn, Gravity.NO_GRAVITY, location[0], location[1] - Pop.getHeight());
        pop.showAsDropDown(owner_community);
        pop.showAtLocation(mall_main_pop_layout, Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
    }

    /**
     * 选择楼栋
     */
    private void buildingList() {

        showProgress(true);
        Map<String, String> map = new HashMap();
        map.putAll(ConstantsData.getSystemParams());
        map.put(ConstantsData.METHOD, Url.buildingList);
        map.put("communityId", communityId);
        map.put("building", "");
        map.put("unit", "");
        map.put("room", "");

        String sign = AopUtils.sign(map, ConstantsData.SECRET_VALUE);
        map.put("sign", sign);
        map.remove(ConstantsData.METHOD);

        RetrofitUtil.Api().buildingList(map).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new BaseTSubscriber<BuildingListBean>(this) {
            @Override
            public void onResponseCallback(JSONObject response, String resultCode) {

            }

            @Override
            public void onNext(BuildingListBean buildingListBean) {
                super.onNext(buildingListBean);

                if (TextUtils.equals(buildingListBean.getCode(), ConstantsData.success)) {
                    buildList = buildingListBean.getData().getBuildList();
                    if (buildList != null || buildList.size() > 0) {
                        showBuildingListPop();
                    }
                }else if(TextUtils.equals(buildingListBean.getCode(), "002")){
                    Utils.showShortToast(HouseConfirmActivity.this, "无数据");
                }
                showProgress(false);
            }
        });
    }

    /**
     * 显示单元号列表的 popwindow
     */
    private void showUnitListPop() {

        listItemAdapter = new ListItemAdapter(this);
        listItemAdapter.setList(unitList);

        mMenuView = LayoutInflater.from(HouseConfirmActivity.this).inflate(R.layout.choose_list_pop2, null);
        ListView listView = (ListView) mMenuView.findViewById(R.id.listView);
        listView.setAdapter(listItemAdapter);

        final PopupWindow pop = new PopupWindow();

        // 设置SelectPicPopupWindow的View
        pop.setContentView(mMenuView);
        // 设置SelectPicPopupWindow弹出窗体的宽
        pop.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        // 设置SelectPicPopupWindow弹出窗体的高
        pop.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        // 设置SelectPicPopupWindow弹出窗体可点击
        pop.setFocusable(true);
        // 设置SelectPicPopupWindow弹出窗体动画效果
        pop.setAnimationStyle(R.style.AnimBottom);
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
                //MessageUtils.showShortToast(HouseConfirmActivity.this, listYear.get(position));
                //owner_community.setText(S.getStr(list.get(position).getCommunityName()));
                //communityId = listBuilding.get(position).getId();
                unit.setText(unitList.get(position));
                room.setText("");
                pop.dismiss();
            }
        });

        //mMenuView添加OnTouchListener监听判断获取触屏位置如果在选择框外面则销毁弹出框
        mMenuView.setOnTouchListener(new View.OnTouchListener() {

            public boolean onTouch(View v, MotionEvent event) {

                int height = mMenuView.findViewById(R.id.pop_layout).getTop();
                int y      = (int) event.getY();
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (y < height) {
                        pop.dismiss();
                    }
                }
                return true;
            }
        });
        mMenuView.setFocusable(true);
        mMenuView.setFocusableInTouchMode(true);
        pop.setFocusable(true);
        pop.showAtLocation(mall_main_pop_layout, Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
    }

    /**
     * 显示楼号列表的 popwindow
     */
    private void showBuildingListPop() {

        listItemAdapter = new ListItemAdapter(this);
        listItemAdapter.setList(buildList);

        mMenuView = LayoutInflater.from(HouseConfirmActivity.this).inflate(R.layout.choose_list_pop2, null);
        ListView listView = (ListView) mMenuView.findViewById(R.id.listView);
        listView.setAdapter(listItemAdapter);

        final PopupWindow pop = new PopupWindow();

        // 设置SelectPicPopupWindow的View
        pop.setContentView(mMenuView);
        // 设置SelectPicPopupWindow弹出窗体的宽
        pop.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        // 设置SelectPicPopupWindow弹出窗体的高
        pop.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        // 设置SelectPicPopupWindow弹出窗体可点击
        pop.setFocusable(true);
        // 设置SelectPicPopupWindow弹出窗体动画效果
        pop.setAnimationStyle(R.style.AnimBottom);
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
                //MessageUtils.showShortToast(HouseConfirmActivity.this, listYear.get(position));
                //owner_community.setText(S.getStr(list.get(position).getCommunityName()));
                //communityId = listBuilding.get(position).getId();
                building.setText(buildList.get(position));
                unit.setText("");
                room.setText("");
                pop.dismiss();
            }
        });

        //mMenuView添加OnTouchListener监听判断获取触屏位置如果在选择框外面则销毁弹出框
        mMenuView.setOnTouchListener(new View.OnTouchListener() {

            public boolean onTouch(View v, MotionEvent event) {

                int height = mMenuView.findViewById(R.id.pop_layout).getTop();
                int y      = (int) event.getY();
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (y < height) {
                        pop.dismiss();
                    }
                }
                return true;
            }
        });
        mMenuView.setFocusable(true);
        mMenuView.setFocusableInTouchMode(true);
        pop.setFocusable(true);
        pop.showAtLocation(mall_main_pop_layout, Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
    }

    /**
     * 显示房号列表的 popwindow
     */
    private void showRoomListPop() {

        listItemAdapter = new ListItemAdapter(this);
        listItemAdapter.setList(roomList);

        mMenuView = LayoutInflater.from(HouseConfirmActivity.this).inflate(R.layout.choose_list_pop2, null);
        ListView listView = (ListView) mMenuView.findViewById(R.id.listView);
        listView.setAdapter(listItemAdapter);

        final PopupWindow pop = new PopupWindow();

        // 设置SelectPicPopupWindow的View
        pop.setContentView(mMenuView);
        // 设置SelectPicPopupWindow弹出窗体的宽
        pop.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        // 设置SelectPicPopupWindow弹出窗体的高
        pop.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        // 设置SelectPicPopupWindow弹出窗体可点击
        pop.setFocusable(true);
        // 设置SelectPicPopupWindow弹出窗体动画效果
        pop.setAnimationStyle(R.style.AnimBottom);
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
                //MessageUtils.showShortToast(HouseConfirmActivity.this, listYear.get(position));
                //owner_community.setText(S.getStr(list.get(position).getCommunityName()));
                //communityId = listBuilding.get(position).getId();
                room.setText(roomList.get(position));
                pop.dismiss();
            }
        });

        //mMenuView添加OnTouchListener监听判断获取触屏位置如果在选择框外面则销毁弹出框
        mMenuView.setOnTouchListener(new View.OnTouchListener() {

            public boolean onTouch(View v, MotionEvent event) {

                int height = mMenuView.findViewById(R.id.pop_layout).getTop();
                int y      = (int) event.getY();
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (y < height) {
                        pop.dismiss();
                    }
                }
                return true;
            }
        });
        mMenuView.setFocusable(true);
        mMenuView.setFocusableInTouchMode(true);
        pop.setFocusable(true);
        pop.showAtLocation(mall_main_pop_layout, Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
    }

    /**
     * 单元列表
     */
    private void unitList() {

        showProgress(true);
        Map<String, String> map = new HashMap();
        map.putAll(ConstantsData.getSystemParams());
        map.put(ConstantsData.METHOD, Url.buildingList);
        map.put("communityId", communityId);
        map.put("building", building_num);
        map.put("unit", "");
        map.put("room", "");

        String sign = AopUtils.sign(map, ConstantsData.SECRET_VALUE);
        map.put("sign", sign);
        map.remove(ConstantsData.METHOD);

        RetrofitUtil.Api().unitList(map).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new BaseTSubscriber<UnitListBean>(this) {
            @Override
            public void onResponseCallback(JSONObject response, String resultCode) {

            }

            @Override
            public void onNext(UnitListBean unitListBean) {
                super.onNext(unitListBean);

                if (TextUtils.equals(unitListBean.getCode(), ConstantsData.success)) {
                    unitList = unitListBean.getData().getUnitList();
                    if (unitList != null || unitList.size() > 0) {
                        showUnitListPop();
                    }
                }else if(TextUtils.equals(unitListBean.getCode(), "002")){
                    Utils.showShortToast(HouseConfirmActivity.this, "无数据");
                }
                showProgress(false);
            }
        });
    }

    /**
     * 房间列表
     */
    private void roomList() {

        showProgress(true);
        Map<String, String> map = new HashMap();
        map.putAll(ConstantsData.getSystemParams());
        map.put(ConstantsData.METHOD, Url.buildingList);
        map.put("communityId", communityId);
        map.put("building", building_num);
        map.put("unit", unit_num);
        map.put("room", "");

        String sign = AopUtils.sign(map, ConstantsData.SECRET_VALUE);
        map.put("sign", sign);
        map.remove(ConstantsData.METHOD);

        RetrofitUtil.Api().roomList(map).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new BaseTSubscriber<RoomListBean>(this) {
            @Override
            public void onResponseCallback(JSONObject response, String resultCode) {

            }

            @Override
            public void onNext(RoomListBean roomListBean) {
                super.onNext(roomListBean);

                if (TextUtils.equals(roomListBean.getCode(), ConstantsData.success)) {
                    roomList = roomListBean.getData().getRoomList();
                    if (roomList != null || roomList.size() > 0) {
                        showRoomListPop();
                    }
                }else if(TextUtils.equals(roomListBean.getCode(), "002")){
                    Utils.showShortToast(HouseConfirmActivity.this, "无数据");
                }
                showProgress(false);
            }
        });
    }

    /**
     * 验证 输入信息
     */
    private boolean checkInput() {

        if (S.isNull(owner_name.getText().toString())) {
            Utils.showShortToast(this, "业主姓名不可为空");
            return false;
        }

        if (S.isNull(owner_mobile.getText().toString())) {
            Utils.showShortToast(this, "业主电话不可为空");
            return false;
        }

        if (S.isNull(owner_P_c_d.getText().toString())) {
            Utils.showShortToast(this, "省市区不可为空");
            return false;
        }

        if (S.isNull(owner_community.getText().toString())) {
            Utils.showShortToast(this, "小区不可为空");
            return false;
        }

        if (S.isNull(building.getText().toString())) {
            Utils.showShortToast(this, "楼号不可为空");
            return false;
        }

        if (S.isNull(unit.getText().toString())) {
            Utils.showShortToast(this, "单元号不可为空");
            return false;
        }

        if (S.isNull(room.getText().toString())) {
            Utils.showShortToast(this, "房号不可为空");
            return false;
        }

        return true;
    }

    /**
     * 认证
     */
    private void proprietyVerify() {

        showProgress(true);
        room_num = room.getText().toString();
        Map<String, String> map = new HashMap();
        map.putAll(ConstantsData.getSystemParams());
        map.put(ConstantsData.METHOD, Url.proprietyVerify);
        map.put("communityId", communityId);
        map.put("memberId", memberId);
        map.put("name", name_arg);
        map.put("mobile", mobile_arg);
        map.put("building", building_num);
        map.put("unit", unit_num);
        map.put("room", room_num);

        String sign = AopUtils.sign(map, ConstantsData.SECRET_VALUE);
        map.put("sign", sign);
        map.remove(ConstantsData.METHOD);

        RetrofitUtil.Api().proprietyVerify(map).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new BaseTSubscriber<ResponseBody>(this) {
            @Override
            public void onResponseCallback(JSONObject response, String resultCode) {
                if (TextUtils.equals(resultCode, ConstantsData.success)) {
                    Utils.showShortToast(HouseConfirmActivity.this, StrConstant.proprietyVerifySuccess);
                    proprietorInfo();
                } else {
                    try {
                        Utils.showShortToast(HouseConfirmActivity.this, String.valueOf(response.get("description")));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                showProgress(false);
            }
        });
    }

    private void proprietorInfo() {

        showProgress(true);
        Map<String, String> params = new HashMap<>();
        params.putAll(ConstantsData.getSystemParams());
        params.put(ConstantsData.METHOD, Url.ProprietorUrl);
//        params.put("communityId", AppHolder.getInstance().getHouse().getCommunityId());
        params.put("communityId", communityId);
        params.put("memberId", AppHolder.getInstance().getMemberInfo().getMemberId());
        String sign = AopUtils.sign(params, ConstantsData.SECRET_VALUE);
        params.put("sign", sign);
        params.remove(ConstantsData.METHOD);

        RetrofitUtil.Api().userProprietor(params).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new BaseTSubscriber<Proprietorinfo>(this) {
            @Override
            public void onResponseCallback(JSONObject response, String resultCode) {

            }

            @Override
            public void onNext(Proprietorinfo proprietorinfo) {
                super.onNext(proprietorinfo);

                House house = proprietorinfo.getData().getHousesInfo();
                AppHolder.getInstance().getProprietor().setProprietorId(house.getProprietorId());
                AppHolder.getInstance().setHouse(house);
                showProgress(false);
                Intent intent = new Intent();
                setResult(1001, intent);
                finish();
            }
        });
    }
}
