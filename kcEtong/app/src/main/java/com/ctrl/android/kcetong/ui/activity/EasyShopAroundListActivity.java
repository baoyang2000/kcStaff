package com.ctrl.android.kcetong.ui.activity;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.CityInfo;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.poi.OnGetPoiSearchResultListener;
import com.baidu.mapapi.search.poi.PoiDetailResult;
import com.baidu.mapapi.search.poi.PoiNearbySearchOption;
import com.baidu.mapapi.search.poi.PoiResult;
import com.baidu.mapapi.search.poi.PoiSearch;
import com.baidu.mapapi.search.poi.PoiSortType;
import com.baidu.mapapi.search.sug.OnGetSuggestionResultListener;
import com.baidu.mapapi.search.sug.SuggestionResult;
import com.baidu.mapapi.search.sug.SuggestionSearch;
import com.ctrl.android.kcetong.R;
import com.ctrl.android.kcetong.model.AppHolder;
import com.ctrl.android.kcetong.model.ShopArround_Easy;
import com.ctrl.android.kcetong.toolkit.util.BaseLinearLayoutManager;
import com.ctrl.android.kcetong.toolkit.util.ConstantsData;
import com.ctrl.android.kcetong.toolkit.util.LLog;
import com.ctrl.android.kcetong.toolkit.util.MessageUtils;
import com.ctrl.android.kcetong.toolkit.util.S;
import com.ctrl.android.kcetong.toolkit.util.StrConstant;
import com.ctrl.android.kcetong.toolkit.util.Utils;
import com.ctrl.android.kcetong.ui.adapter.EasyShopArroundListAdapter;
import com.ctrl.android.kcetong.ui.base.BaseActivity;
import com.ctrl.android.kcetong.ui.view.DividerDecoration;
import com.github.jdsjlzx.interfaces.OnItemClickListener;
import com.github.jdsjlzx.interfaces.OnLoadMoreListener;
import com.github.jdsjlzx.interfaces.OnRefreshListener;
import com.github.jdsjlzx.recyclerview.LRecyclerView;
import com.github.jdsjlzx.recyclerview.LRecyclerViewAdapter;
import com.github.jdsjlzx.recyclerview.ProgressStyle;
import com.github.jdsjlzx.util.RecyclerViewStateUtils;
import com.github.jdsjlzx.view.LoadingFooter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class EasyShopAroundListActivity extends BaseActivity implements View.OnClickListener,OnGetPoiSearchResultListener, OnGetSuggestionResultListener {


    @BindView(R.id.recyclerView_shop_around)
    LRecyclerView  recyclerViewShopAround;
    /**经度*/
    private double           longitude         = AppHolder.getInstance().getBdLocation() == null ? 0.0 : AppHolder.getInstance().getBdLocation().getLongitude();
    /**纬度*/
    private double           latitude          = AppHolder.getInstance().getBdLocation() == null ? 0.0 : AppHolder.getInstance().getBdLocation().getLatitude();
    /**搜索关键字*/
    private String           keyword           = "";
    /**检索经纬度坐标*/
    private LatLng           location          = new LatLng(latitude,longitude);
    /**每页容量*/
    private int              pageCapacity      = ConstantsData.PAGE_CAPACITY;
    /**分页编号*/
    private int              pageNum           = ConstantsData.DEFAULT_PAGE_NUM;
    /**检索半径 单位:米*/
    private int              radius            = ConstantsData.SEARCH_RADIUS;
    /**检索结果排序规则*/
    private PoiSortType      sortType          = PoiSortType.distance_from_near_to_far;
    /**搜索入口类, 定义此类开始搜索 */
    private PoiSearch        mPoiSearch        = null;
    /**建议查询接口*/
    private SuggestionSearch mSuggestionSearch = null;
    /**定义百度地图对象的操作方法与接口*/

    private String           TITLE             = StrConstant.EASY_SHOP_ARROUND_TITLE;
    /**本页所显示的主题关键字*/
    private String           key_word_arg      = "";
    private int                       parentRowCountPerPage;

    private List<ShopArround_Easy>     listShop;
    private EasyShopArroundListAdapter easyShopArroundListAdapter;
    private LRecyclerViewAdapter       mLRecyclerViewAdapter;
    @Override
    protected void initView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_easy_shop_around_list);
        ButterKnife.bind(this);
    }

    @Override
    protected void initData() {
        toolbarBaseSetting(TITLE, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utils.hideKeyBoard(EasyShopAroundListActivity.this);
                EasyShopAroundListActivity.this.finish();
            }
        });
        /**初始化搜索模块*/
        mPoiSearch = PoiSearch.newInstance();
        /**注册搜索事件监听*/
        mPoiSearch.setOnGetPoiSearchResultListener(this);
        /**获取建议检索实例*/
        mSuggestionSearch = SuggestionSearch.newInstance();
        /**设置建议请求结果监听器*/
        mSuggestionSearch.setOnGetSuggestionResultListener(this);

        listShop = new ArrayList<ShopArround_Easy>();
        key_word_arg = getIntent().getStringExtra(ConstantsData.ARG_FLG);
        if(!S.isNull(key_word_arg)){
            keyword = key_word_arg;
           // startSearch();
        }
        settingListView();
    }

    /**
     * 启动检索的方法
     * */
    private void startSearch(){
        //Log.d("demo", "startSearch");
        /**附近检索参数*/
        PoiNearbySearchOption option = new PoiNearbySearchOption();
        option.keyword(keyword);
        option.location(location);
        option.pageCapacity(pageCapacity);
        option.pageNum(pageNum);
        option.radius(radius);
        option.sortType(sortType);
        mPoiSearch.searchNearby(option);

//        mPoiSearch.searchInCity((new PoiCitySearchOption())
//                //.city("济南")
//                .keyword(keyword)
//                .pageNum(10));


    }

    @Override
    public void onClick(View v) {

    }

    /**
     * 根据条件搜索结束后 调用此方法
     * */
    public void onGetPoiResult(PoiResult result) {

        Log.d("demo", "onGetPoiResult");

        if (result == null || result.error == SearchResult.ERRORNO.RESULT_NOT_FOUND) {
            MessageUtils.showShortToast(this, getString(R.string.sorry_find_no_result));
            recyclerViewShopAround.refreshComplete();
            return;
        }
        List<ShopArround_Easy> list = new ArrayList<>();
        if (result.error == SearchResult.ERRORNO.NO_ERROR) {
            for(int i = 0 ; i < result.getAllPoi().size() ; i ++){
                ShopArround_Easy shop = new ShopArround_Easy();
                shop.setShopName(S.getStr(result.getAllPoi().get(i).name));
                shop.setShopAddress(S.getStr(result.getAllPoi().get(i).address));
                shop.setShopPhoneNum(S.getStr(result.getAllPoi().get(i).phoneNum));
                shop.setLoction(result.getAllPoi().get(i).location);
                list.add(shop);
                parentRowCountPerPage=list.size();
            }
            if (pageNum ==ConstantsData.DEFAULT_PAGE_NUM ) {
                listShop.clear();
            }
            listShop.addAll(list);
            easyShopArroundListAdapter.notifyDataSetChanged();
            mLRecyclerViewAdapter.notifyDataSetChanged();
            recyclerViewShopAround.refreshComplete();
            return;
        }
        if (result.error == SearchResult.ERRORNO.AMBIGUOUS_KEYWORD) {

            recyclerViewShopAround.refreshComplete();

            // 当输入关键字在本市没有找到，但在其他城市找到时，返回包含该关键字信息的城市列表
            String strInfo = "在";
            for (CityInfo cityInfo : result.getSuggestCityList()) {
                strInfo += cityInfo.city;
                strInfo += ",";
            }
            strInfo += "找到结果";
            MessageUtils.showShortToast(this, strInfo);
            //Toast.makeText(EasyBusActivity.this, strInfo, Toast.LENGTH_LONG).show();
        }

        recyclerViewShopAround.refreshComplete();
    }

    @Override
    public void onGetPoiDetailResult(PoiDetailResult result) {
        if (result.error != SearchResult.ERRORNO.NO_ERROR) {
            MessageUtils.showShortToast(this, getString(R.string.sorry_find_no_result));
            recyclerViewShopAround.refreshComplete();
        } else {
            MessageUtils.showShortToast(this, result.getName() + ": " + result.getAddress());
        }
        recyclerViewShopAround.refreshComplete();
    }

    @Override
    public void onGetSuggestionResult(SuggestionResult suggestionResult) {

    }
    private void settingListView(){
        easyShopArroundListAdapter = new EasyShopArroundListAdapter(this);
        easyShopArroundListAdapter.setListData(listShop);
        mLRecyclerViewAdapter= new LRecyclerViewAdapter(easyShopArroundListAdapter);
        recyclerViewShopAround.setLayoutManager(new BaseLinearLayoutManager(this));
        //设置分割线
        final DividerDecoration divider = new DividerDecoration.Builder(this)
                .setHeight(R.dimen.default_divider_height)
                .setColorResource(R.color.main_bg)
                .build();
        recyclerViewShopAround.addItemDecoration(divider);
        recyclerViewShopAround.setRefreshProgressStyle(ProgressStyle.BallSpinFadeLoader);
        recyclerViewShopAround.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh() {
                pageNum = ConstantsData.DEFAULT_PAGE_NUM;
                startSearch();
            }
        });
        recyclerViewShopAround.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore() {

                pageNum= pageNum + 1;
                if (parentRowCountPerPage == 10) {
                    RecyclerViewStateUtils.setFooterViewState(EasyShopAroundListActivity.this, recyclerViewShopAround, 5, LoadingFooter.State.Loading, null);
                    startSearch();

                } else {
                    RecyclerViewStateUtils.setFooterViewState(EasyShopAroundListActivity.this, recyclerViewShopAround, 10, LoadingFooter.State.Normal, null);
                }
            }
        });
        recyclerViewShopAround.setRefreshing(true);
        recyclerViewShopAround.setAdapter(mLRecyclerViewAdapter);
        mLRecyclerViewAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int i) {
                if(isAvilible("com.baidu.BaiduMap")){
                    LLog.d("---------11111111111");
                    LatLng location = listShop.get(i).getLoction();
                    String location2 = location.latitude + "," + location.longitude;
                    String title    =(listShop.get(i).getShopName());
                    String content  =listShop.get(i).getShopAddress()+listShop.get(i).getShopPhoneNum();
                    Intent i1       = new Intent();
                    i1.setData(Uri.parse("baidumap://map/marker?location=" + location2 + "&title=" + title + "&content=" + content + "&traffic=on"));
                    startActivity(i1);
                }
                //未安装，跳转至market下载该程序
                else {
                    LLog.d("---------22222222222");
                    LatLng location =listShop.get(i).getLoction();
                    // http://api.map.baidu.com/marker?location=39.916979519873,116.41004950566&title=我的位置&content=百度奎科大厦&output=html
                    String location2 = location.latitude + "," + location.longitude;
                    String city = AppHolder.getInstance().getBdLocation().getCity();
                    String title    =(listShop.get(i).getShopName());
                    String content  =listShop.get(i).getShopAddress()+listShop.get(i).getShopPhoneNum();
                    Intent i1   = new Intent(EasyShopAroundListActivity.this, StationWebActivity.class);
                    i1.putExtra("type","1");
                    i1.putExtra("url", "http://api.map.baidu.com/marker?location=" + location2+"&title="+ title +"&content="+ content + "&output=html");
                    startActivity(i1);
                }
            }

            @Override
            public void onItemLongClick(View view, int i) {

            }
        });
    }
    /**
     * 检查手机上是否安装了指定的软件
     * @param packageName：应用包名
     * @return
     */
    private boolean isAvilible(String packageName){
        //获取packagemanager
        final PackageManager packageManager = this.getPackageManager();
        //获取所有已安装程序的包信息
        List<PackageInfo> packageInfos = packageManager.getInstalledPackages(0);
        //用于存储所有已安装程序的包名
        List<String> packageNames = new ArrayList<String>();
        //从pinfo中将包名字逐一取出，压入pName list中
        if(packageInfos != null){
            for(int i = 0; i < packageInfos.size(); i++){
                String packName = packageInfos.get(i).packageName;
                packageNames.add(packName);
            }
        }
        //判断packageNames中是否有目标程序的包名，有TRUE，没有FALSE
        return packageNames.contains(packageName);
    }
}
