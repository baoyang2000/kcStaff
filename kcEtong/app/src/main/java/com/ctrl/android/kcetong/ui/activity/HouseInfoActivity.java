package com.ctrl.android.kcetong.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.SparseArray;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.ctrl.android.kcetong.R;
import com.ctrl.android.kcetong.model.City;
import com.ctrl.android.kcetong.model.CityResult;
import com.ctrl.android.kcetong.model.HouseEstateInfo;
import com.ctrl.android.kcetong.model.HouseEstateResult;
import com.ctrl.android.kcetong.toolkit.util.AopUtils;
import com.ctrl.android.kcetong.toolkit.util.BaseLinearLayoutManager;
import com.ctrl.android.kcetong.toolkit.util.ConstantsData;
import com.ctrl.android.kcetong.toolkit.util.LLog;
import com.ctrl.android.kcetong.toolkit.util.StrConstant;
import com.ctrl.android.kcetong.toolkit.util.Utils;
import com.ctrl.android.kcetong.toolkit.webutils.BaseTSubscriber;
import com.ctrl.android.kcetong.toolkit.webutils.RetrofitUtil;
import com.ctrl.android.kcetong.ui.adapter.HouseInfoAdapter;
import com.ctrl.android.kcetong.ui.base.BaseActivity;
import com.ctrl.android.kcetong.ui.view.ExpandTabView;
import com.ctrl.android.kcetong.ui.view.ViewLeft;
import com.ctrl.android.kcetong.ui.view.ViewMiddle;
import com.ctrl.android.kcetong.ui.view.ViewRight;
import com.github.jdsjlzx.interfaces.OnLoadMoreListener;
import com.github.jdsjlzx.interfaces.OnRefreshListener;
import com.github.jdsjlzx.recyclerview.LRecyclerView;
import com.github.jdsjlzx.recyclerview.LRecyclerViewAdapter;
import com.github.jdsjlzx.recyclerview.ProgressStyle;
import com.github.jdsjlzx.util.RecyclerViewStateUtils;
import com.github.jdsjlzx.view.LoadingFooter;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class HouseInfoActivity extends BaseActivity {
    @BindView(R.id.expandtab_view)
    ExpandTabView         expandtab_view;
    @BindView(R.id.spinner_total_price)//价格
    Spinner               spinner_total_price;
    @BindView(R.id.spinner_room)//户型
    Spinner               spinner_room;
    @BindView(R.id.spinner_area)//面积
    Spinner               spinner_area;
    @BindView(R.id.toolbar_right_btn)
    TextView toolbar_right_btn;
    @BindView(R.id.recyclerView_house)
    LRecyclerView mLRecyclerView;
    private LRecyclerViewAdapter mLRecyclerViewAdapter;

    private HouseInfoAdapter adapter;
    private int mPage = 1;
    private int parentRowCountPerPage = 0;

    private ViewLeft   viewLeft;
    private ViewMiddle viewMiddle;
    private ViewRight  viewRight;
    private static final String TITLE= StrConstant.HOUSE_INFO;
    private ArrayList<View> mViewArray = new ArrayList<>();
    private ArrayList<String> listCity=new ArrayList<>();
    private String[] mTotalPrice;
    private String[] mHouseType;
    private String[] mHouseArea;
    private HashMap<Integer, ArrayList<String>> couny_map_code = new HashMap<>();
    private HashMap<Integer, ArrayList<String>> couny_map      = new HashMap<>();
    private SparseArray<LinkedList<String>>     children       = new SparseArray<>();

    private ArrayList<String> listCouny2=new ArrayList<>();
    private ArrayList<String> listCounyCode=new ArrayList<>();
    private int pos;

    private String arecode="";
    private boolean isFirst=true;
    private boolean isFirst2=true;
    private boolean isFirst3=true;
    private String totalPriceMin="";
    private String totalPriceMax="";
    private String roomAcreageMin="";
    private String roomAcreageMax="";
    private int sittingRoom=0;
    private String province_code="";//省份值
    List<HouseEstateInfo> listHouseInfo = new ArrayList<>();

    private Handler handler =new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 001:
                    toolbar_right_btn.setText(province_code);
                    expandtab_view.setTitle("区域", 0);
                    if(province_code.equals("全国")){
                        arecode="";
                        province_code= "" ;
                        listHouseInfo.clear();
                        showProgress(true);
                        requestHouseEstate("", arecode, "", totalPriceMin, totalPriceMax, sittingRoom, roomAcreageMin, roomAcreageMax, mPage + "",province_code);
                    }else {
                        showProgress(true);
                        listHouseInfo.clear();
                        requestHouseEstate("", arecode, "", totalPriceMin, totalPriceMax, sittingRoom, roomAcreageMin, roomAcreageMax, mPage + "",province_code);
                        requestCitiesList(province_code);
                    }
                    break;
                case 002:
                    toolbar_right_btn.setText(province_code);
                    break;
            }
        }
    };

    @Override
    protected void initView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_house_info);
        ButterKnife.bind(this);
        toolbarBaseSetting(TITLE, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HouseInfoActivity.this.finish();
            }
        });
        toolbar_right_btn.setVisibility(View.VISIBLE);
        toolbar_right_btn.setText("全国");
        toolbar_right_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                expandtab_view.onPressBack();
                Intent i=new Intent(HouseInfoActivity.this ,ProvinceListActivity.class);
                startActivityForResult(i, 2000);
            }
        });
        viewLeft = new ViewLeft(this);
        viewMiddle = new ViewMiddle(this);
        viewRight = new ViewRight(this);
    }

    @Override
    protected void initData() {
        mViewArray.add(viewMiddle);
        ArrayList<String> mTextArray = new ArrayList<>();
        mTextArray.add("区域");
        expandtab_view.setValue(mTextArray, mViewArray,listCity);

        initListener();
        initSpinner();
        adapter = new HouseInfoAdapter(this);
        mLRecyclerViewAdapter = new LRecyclerViewAdapter(adapter);
        mLRecyclerView.setLayoutManager(new BaseLinearLayoutManager(this));
        mLRecyclerView.setRefreshProgressStyle(ProgressStyle.BallSpinFadeLoader);
        mLRecyclerView.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh() {
                mPage = 1;
                requestHouseEstate("", arecode, "", totalPriceMin, totalPriceMax, sittingRoom, roomAcreageMin, roomAcreageMax, mPage + "",province_code);
            }
        });
        mLRecyclerView.setRefreshing(true);

        mLRecyclerView.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                mPage++;
                LLog.d(mPage + "");
                if(parentRowCountPerPage == ConstantsData.PAGE_CAPACITY){
                    RecyclerViewStateUtils.setFooterViewState(HouseInfoActivity.this, mLRecyclerView, 10, LoadingFooter.State.Normal, null);
                    requestHouseEstate("", arecode, "", totalPriceMin, totalPriceMax, sittingRoom, roomAcreageMin, roomAcreageMax, mPage + "",province_code);
                }else {
                    RecyclerViewStateUtils.setFooterViewState(HouseInfoActivity.this, mLRecyclerView, 15, LoadingFooter.State.Normal, null);
                    Utils.toastError(HouseInfoActivity.this,"没有更多内容了");
                }
            }
        });
        adapter.setOnItemClickListener(new HouseInfoAdapter.OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(View view, String data) {
                Intent intent = new Intent(HouseInfoActivity.this, HouseInfoDetailActivity.class);
                intent.putExtra("id",data);
//                intent.putExtra("title",data.getTitle());
//                intent.putExtra("time",data.getCreateTime());
//                intent.putExtra("price",data.getTotalPrice());
//                intent.putExtra("sittingRoom",data.getSittingRoom());
//                intent.putExtra("livingRoom",data.getLivingRoom());
//                intent.putExtra("bathRoom",data.getBathRoom());
//                intent.putExtra("area",data.getRoomAcreage());
//                intent.putExtra("singlePrice",data.getSinglePrice());
//
//                intent.putExtra("orientation",data.getOrientation());
//                intent.putExtra("floor",data.getFloor());
//                intent.putExtra("places",data.getPlaces());
//                intent.putExtra("roomType",data.getRoomType());
//                intent.putExtra("renovation",data.getRenovation());
//                intent.putExtra("propertyRight",data.getPropertyRight());
//                intent.putExtra("roomAge",data.getRoomAge());
//                intent.putExtra("housingProfile",data.getHousingProfile());
//                intent.putExtra("roomOwner",data.getRoomOwner());
//                intent.putExtra("phone",data.getPhone());
//                intent.putExtra("originalImg",data.getOriginalImg());

                startActivity(intent);
            }
        });
        mLRecyclerView.setAdapter(mLRecyclerViewAdapter);

    }

    private void initListener(){
        viewLeft.setOnSelectListener(new ViewLeft.OnSelectListener() {

            @Override
            public void getValue(String distance, String showText) {
                onRefresh(viewLeft, showText, 0);
            }
        });

        viewMiddle.setOnSelectListener(new ViewMiddle.OnSelectListener() {

            @Override
            public void getValue(String showText, int position) {

                onRefresh(viewMiddle, showText, position);

            }
        });

        viewRight.setOnSelectListener(new ViewRight.OnSelectListener() {

            @Override
            public void getValue(String distance, String showText) {
                onRefresh(viewRight, showText, 0);
            }
        });
    }

    private void onRefresh(View view, String showText,int pos) {
        expandtab_view.onPressBack();
        int position = getPositon(view);
        if (position >= 0 && !expandtab_view.getTitle(position).equals(showText)) {
            expandtab_view.setTitle(showText, position);
            if (couny_map_code.get(viewMiddle.getPos()) != null)
                arecode = couny_map_code.get(viewMiddle.getPos()).get(pos);
            if (listHouseInfo != null) listHouseInfo.clear();
            mPage = 1;
            showProgress(true);
            //  mHouseInfoAdapter.notifyDataSetChanged();
            requestHouseEstate("", arecode, "", totalPriceMin, totalPriceMax, sittingRoom, roomAcreageMin, roomAcreageMax, mPage + "", province_code);
        }
    }

    private int getPositon(View tView) {
        for (int i = 0; i < mViewArray.size(); i++) {
            if (mViewArray.get(i) == tView) {
                return i;
            }
        }
        return -1;
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==2000&&resultCode==RESULT_CANCELED){
            if(province_code!=null&&!province_code.equals("")){
                handler.sendEmptyMessage(002);
            }
            return;
        }
        if(requestCode==2000&&resultCode==RESULT_OK){
            if(listCity!=null){
                listCity.clear();
            }
            if(listCouny2!=null){
                listCouny2.clear();
            }
            if(listCounyCode!=null){
                listCounyCode.clear();
            }
            if(couny_map!=null){
                couny_map.clear();
            }
            if(couny_map_code!=null)
            {
                couny_map_code.clear();
            }
            province_code = data.getStringExtra("province");
            pos = data.getIntExtra("pos", -1);
            handler.sendEmptyMessage(001);
        }
    }
    /**
     * 房产信息列表
     * @param id 房产信息的id
     * @param areaCode 3级的市区code值
     * @param communityId 社区的id
     * @param totalPriceMin 房屋价格总价的最小值(当条件为不限不用传参,默认为null)
     * @param totalPriceMax 房屋价格总价的最大值(当条件为不限不用传参,默认为null)
     * @param sittingRoom 房屋的室个数(当条件为不限不用传参,默认为0),当条件4室以上时:参数为5
     * @param roomAcreageMin 房屋的总面积最小值(当条件为不限不用传参,默认为null)
     * @param roomAcreageMax 房屋的总面积最大值(当条件为不限不用传参,默认为null)
     * @param currentPage 当前页码
     * @param provinceName 省code值
     * */
    public void requestHouseEstate(String id,
            String areaCode,
            String communityId,
            String totalPriceMin,
            String totalPriceMax,
            int sittingRoom,
            String roomAcreageMin,
            String roomAcreageMax,
            String currentPage,
            String provinceName
    ){
        Map<String,String> map = new HashMap<>();
        map.put(ConstantsData.METHOD,"pm.ppt.homeEstate.list");//方法名称
        map.putAll(ConstantsData.getSystemParams());

        map.put("id",id);
        map.put("areaCode",areaCode);
        map.put("communityId",communityId);
        map.put("totalPriceMin",totalPriceMin+"");
        map.put("totalPriceMax",totalPriceMax+"");
        map.put("sittingRoom",sittingRoom+"");
        map.put("roomAcreageMin",roomAcreageMin+"");
        map.put("roomAcreageMax",roomAcreageMax+"");
        map.put("currentPage",currentPage);
        map.put("rowCountPerPage",ConstantsData.ROW_COUNT_PER_PAGE+"");
        map.put("provinceName",provinceName);

        String sign = AopUtils.sign(map, ConstantsData.SECRET_VALUE);
        map.put("sign",sign);
        LLog.d(map+"");
        map.remove(ConstantsData.METHOD);
        RetrofitUtil.Api().requestHouseEstate(map).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseTSubscriber<HouseEstateResult>(this) {
                    @Override
                    public void onResponseCallback(JSONObject response, String resultCode) {
                        LLog.d(response+"");
                        showProgress(false);
                    }

                    @Override
                    public void onNext(HouseEstateResult houseEstateResult) {
                        super.onNext(houseEstateResult);
                        showProgress(false);
                        if(TextUtils.equals("000",houseEstateResult.getCode())){
//                            listHouseInfo = JsonUtil.node2pojoList(result.findValue("homeEstateInfo"), HouseEstateInfo.class);
                            if(mPage == 1){
                                adapter.clear();
                            }
                            parentRowCountPerPage = houseEstateResult.getData().getHomeEstateInfo().size();
                            List<HouseEstateInfo> list_result = houseEstateResult.getData().getHomeEstateInfo();
                            if(houseEstateResult.getData().getHomeEstateInfo() != null && list_result.size()>0){
                                if(mPage > 1){//当前页大于1，将加载出来的数据添加到expressList后面
                                    listHouseInfo.addAll(list_result);
                                }else if(mPage == 1){
                                    listHouseInfo = list_result;
                                }
                                adapter.setList(listHouseInfo);
                            }
                            mLRecyclerViewAdapter.notifyDataSetChanged();
                        }else if(TextUtils.equals("002",houseEstateResult.getCode())){
                            mLRecyclerViewAdapter.notifyDataSetChanged();
                        }

                        mLRecyclerView.refreshComplete();
                    }
                    @Override
                    public void onNetError(Throwable e) {
                        super.onNetError(e);
                        if(mPage > 1){
                            mPage--;
                        }
                        mLRecyclerView.refreshComplete();
                        showProgress(false);
                    }
                });
    }

    /**
     * 房产信息中省市区信息接口
     * @param provinceName 省名称(如:山东省)
     * */
    public void requestCitiesList(String provinceName){
        Map<String,String> map = new HashMap<>();
        map.put(ConstantsData.METHOD,"pm.ppt.getCitiesList.list");//方法名称
        map.putAll(ConstantsData.getSystemParams());

        map.put("provinceName",provinceName);

        String sign = AopUtils.sign(map, ConstantsData.SECRET_VALUE);
        map.put("sign",sign);
        RetrofitUtil.Api().requestCitiesList(map).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseTSubscriber<CityResult>(this) {
                    @Override
                    public void onResponseCallback(JSONObject response, String resultCode) {
                        LLog.d(response+"");
                        showProgress(false);
                    }
                    @Override
                    public void onNext(CityResult cityResult) {
                        super.onNext(cityResult);
                        showProgress(false);
                        expandtab_view.onPressBack();
                        List<City> cityList = cityResult.getData().getCitiesList();
                        for(int i=0;i < cityList.size();i++){
                            listCity.add(cityList.get(i).getName());
                            ArrayList<String> listCouny=new ArrayList<>();
                            ArrayList<String> listCounycod=new ArrayList<>();
                            for(int j = 0;j < cityList.get(i).getAreaList().size(); j++){
                                listCouny.add(cityList.get(i).getAreaList().get(j).getName());
                                listCounycod.add(cityList.get(i).getAreaList().get(j).getId());
                            }
                            couny_map.put(i,listCouny);
                            couny_map_code.put(i,listCounycod);

                        }
                        for(int i=0;i<listCity.size();i++){
                            LinkedList<String> tItem = new LinkedList<>();
                            listCouny2=couny_map.get(i);
                            for (int j = 0; j < listCouny2.size(); j++) {
                                tItem.add(listCouny2.get(j));
                            }
                            children.put(i, tItem);
                        }

                        viewMiddle.setData(listCity, children);
                    }

                    @Override
                    public void onNetError(Throwable e) {
                        super.onNetError(e);
                        showProgress(false);
                    }
                });
    }

    private void initSpinner() {
        // 建立数据源
        mTotalPrice = getResources().getStringArray(R.array.total_price);
        mHouseType = getResources().getStringArray(R.array.house_type);
        mHouseArea = getResources().getStringArray(R.array.house_area);
        // 建立Adapter并且绑定数据源
        ArrayAdapter<String> adapter1 =new ArrayAdapter<>(this,R.layout.spinner_house_style, mTotalPrice);
        ArrayAdapter<String> adapter2 =new ArrayAdapter<>(this,R.layout.spinner_house_style, mHouseType);
        ArrayAdapter<String> adapter3 =new ArrayAdapter<>(this,R.layout.spinner_house_style, mHouseArea);
        adapter1.setDropDownViewResource(R.layout.spinner_house_style_drop);
        adapter2.setDropDownViewResource(R.layout.spinner_house_style_drop);
        adapter3.setDropDownViewResource(R.layout.spinner_house_style_drop);
        spinner_total_price.setAdapter(adapter1);
        spinner_room.setAdapter(adapter2);
        spinner_area.setAdapter(adapter3);
        spinner_total_price.setDropDownWidth(Utils.getScreenWidth(HouseInfoActivity.this));
        spinner_room.setDropDownWidth(Utils.getScreenWidth(HouseInfoActivity.this));
        spinner_area.setDropDownWidth(Utils.getScreenWidth(HouseInfoActivity.this));

        spinner_total_price.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                expandtab_view.onPressBack();
                return false;
            }
        });
        spinner_room.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                expandtab_view.onPressBack();
                return false;
            }
        });
        spinner_area.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                expandtab_view.onPressBack();
                return false;
            }
        });
        spinner_total_price.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (isFirst) {
                    TextView tv = (TextView) view;
                    tv.setText("总价");
                }else {
                    if(listHouseInfo!=null)
                        listHouseInfo.clear();
                    setPriceValue(position);
                    mPage=1;
                    requestHouseEstate("", arecode, "", totalPriceMin, totalPriceMax, sittingRoom, roomAcreageMin, roomAcreageMax, mPage + "",province_code);
                }
                isFirst = false;

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                isFirst=true;
            }
        });
        spinner_room.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (isFirst2) {
                    TextView tv = (TextView) view;
                    tv.setText("厅室");
                }else {
                    if(listHouseInfo!=null)
                        listHouseInfo.clear();
                    setRoomValue(position);
                    mPage=1;
                    requestHouseEstate("", arecode, "", totalPriceMin, totalPriceMax, sittingRoom, roomAcreageMin, roomAcreageMax, mPage + "",province_code);
                }
                isFirst2 = false;

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        spinner_area.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(isFirst3) {
                    TextView tv = (TextView) view;
                    tv.setText("面积");
                }else {
                    if(listHouseInfo!=null)
                        listHouseInfo.clear();
                    setAreaValue(position);
                    mPage=1;
                    requestHouseEstate("", arecode, "", totalPriceMin, totalPriceMax, sittingRoom, roomAcreageMin, roomAcreageMax, mPage + "",province_code);
                }
                isFirst3=false;

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    private void setPriceValue(int position) {
        if(position==0){
            totalPriceMin="";
            totalPriceMax="";
        }
        if(position==1){
            totalPriceMin="";
            totalPriceMax="30";
        }
        if(position==2){
            totalPriceMin="30";
            totalPriceMax="50";
        }
        if(position==3){
            totalPriceMin="50";
            totalPriceMax="80";
        }
        if(position==4){
            totalPriceMin="80";
            totalPriceMax="100";
        }
        if(position==5){
            totalPriceMin="100";
            totalPriceMax="150";
        }
        if(position==6){
            totalPriceMin="150";
            totalPriceMax="200";
        }
        if(position==7){
            totalPriceMin="200";
            totalPriceMax="300";
        }
        if(position==8){
            totalPriceMin="300";
            totalPriceMax="400";
        }
        if(position==9){
            totalPriceMin="400";
            totalPriceMax="";
        }
    }

    private void setAreaValue(int position) {
        if(position==0){
            roomAcreageMin="";
            roomAcreageMax="";
        }
        if(position==1){
            roomAcreageMin="";
            roomAcreageMax="50";
        }
        if(position==2){
            roomAcreageMin="50";
            roomAcreageMax="70";
        }
        if(position==3){
            roomAcreageMin="70";
            roomAcreageMax="90";
        }
        if(position==4){
            roomAcreageMin="90";
            roomAcreageMax="110";
        }
        if(position==5){
            roomAcreageMin="110";
            roomAcreageMax="130";
        }
        if(position==6){
            roomAcreageMin="130";
            roomAcreageMax="150";
        }
        if(position==7){
            roomAcreageMin="150";
            roomAcreageMax="200";
        }
        if(position==8){
            roomAcreageMin="200";
            roomAcreageMax="300";
        }
        if(position==9){
            roomAcreageMin="300";
            roomAcreageMax="500";
        }
        if(position==10){
            roomAcreageMin="500";
            roomAcreageMax="";
        }

    }

    private void setRoomValue(int position) {
        if(position==0){
            sittingRoom=0;
        }
        if(position==1){
            sittingRoom=1;
        }
        if(position==2){
            sittingRoom=2;
        }
        if(position==3){
            sittingRoom=3;
        }
        if(position==4){
            sittingRoom=4;
        }
        if(position==5){
            sittingRoom=5;
        }
    }
}
