package com.ctrl.android.kcetong.ui.activity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.SupportMapFragment;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.overlayutil.PoiOverlay;
import com.baidu.mapapi.search.core.CityInfo;
import com.baidu.mapapi.search.core.PoiInfo;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.poi.OnGetPoiSearchResultListener;
import com.baidu.mapapi.search.poi.PoiDetailResult;
import com.baidu.mapapi.search.poi.PoiDetailSearchOption;
import com.baidu.mapapi.search.poi.PoiNearbySearchOption;
import com.baidu.mapapi.search.poi.PoiResult;
import com.baidu.mapapi.search.poi.PoiSearch;
import com.baidu.mapapi.search.poi.PoiSortType;
import com.baidu.mapapi.search.sug.OnGetSuggestionResultListener;
import com.baidu.mapapi.search.sug.SuggestionResult;
import com.baidu.mapapi.search.sug.SuggestionSearch;
import com.baidu.mapapi.utils.DistanceUtil;
import com.ctrl.android.kcetong.R;
import com.ctrl.android.kcetong.model.AppHolder;
import com.ctrl.android.kcetong.model.BusStation;
import com.ctrl.android.kcetong.toolkit.util.BaseLinearLayoutManager;
import com.ctrl.android.kcetong.toolkit.util.ConstantsData;
import com.ctrl.android.kcetong.toolkit.util.LLog;
import com.ctrl.android.kcetong.toolkit.util.StrConstant;
import com.ctrl.android.kcetong.toolkit.util.Utils;
import com.ctrl.android.kcetong.ui.adapter.EasyBusStationListAdapter;
import com.ctrl.android.kcetong.ui.base.BaseActivity;
import com.ctrl.android.kcetong.ui.view.DividerDecoration;
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

public class EasyBusActivity extends BaseActivity implements OnGetPoiSearchResultListener, OnGetSuggestionResultListener {

    @BindView(R.id.pull_to_refresh_listView)//可刷新的列表
            LRecyclerView mPullToRefreshListView;
    @BindView(R.id.map_Layout)//地图所在layout
            LinearLayout  map_Layout;
    @BindView(R.id.list_Layout)//列表所在layout
            LinearLayout  list_Layout;
    /**
     * 车站信息列表
     */
    private List<BusStation> listBusStation;
    /**
     * 经度
     */
    private double      longitude    = application.getLocation() == null ? 0.0 : application.getLocation().getLongitude();
    /**
     * 纬度
     */
    private double      latitude     = application.getLocation() == null ? 0.0 : application.getLocation().getLatitude();
    /**
     * 搜索关键字
     */
    private String      keyword1      = ConstantsData.SEARCH_KEY_WORD;
    /**
     * 检索经纬度坐标
     */
    private LatLng      location     = new LatLng(latitude, longitude);
    /**
     * 每页容量
     */
    private int         pageCapacity = ConstantsData.PAGE_CAPACITY;
    /**
     * 分页编号
     */
    private int         pageNum      = ConstantsData.DEFAULT_PAGE_NUM;
    /**
     * 检索半径 单位:米
     */
    private int         radius       = ConstantsData.SEARCH_RADIUS;
    /**
     * 检索结果排序规则
     */
    private PoiSortType sortType     = PoiSortType.distance_from_near_to_far;

    /**
     * 搜索入口类, 定义此类开始搜索
     */
    private PoiSearch        mPoiSearch        = null;
    /**
     * 建议查询接口
     */
    private SuggestionSearch mSuggestionSearch = null;
    /**
     * 定义百度地图对象的操作方法与接口
     */
    private BaiduMap         mBaiduMap         = null;

    private String TITLE = StrConstant.EASY_BUS_TITLE;
    private EasyBusStationListAdapter adapter;
    private LRecyclerViewAdapter      mLRecyclerViewAdapter;
    private int                       parentRowCountPerPage;

    @Override
    protected void initView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_easy_bus);
        ButterKnife.bind(this);
        toolbarBaseSetting(TITLE, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EasyBusActivity.this.finish();
            }
        });
    }

    @Override
    protected void initData() {

        listBusStation = new ArrayList();
//        map_Layout.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, ConstantsData.ScreenWidth * 1 / 2 - 100));
//        list_Layout.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, ConstantsData.ScreenHeight * 1 / 2 - 100));
        initMap();
        adapter = new EasyBusStationListAdapter(this);
        adapter.setDataList(listBusStation);
        mLRecyclerViewAdapter = new LRecyclerViewAdapter(adapter);
        mPullToRefreshListView.setLayoutManager(new BaseLinearLayoutManager(this));
        final DividerDecoration divider = new DividerDecoration.Builder(this).setHeight(R.dimen.between_element_margin).setColorResource(R.color.main_bg).build();
        mPullToRefreshListView.addItemDecoration(divider);
        mPullToRefreshListView.setRefreshProgressStyle(ProgressStyle.BallSpinFadeLoader);
        mPullToRefreshListView.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh() {
                LLog.w("111111111111");
                pageNum = ConstantsData.DEFAULT_PAGE_NUM;
                startSearch();
            }
        });
        mPullToRefreshListView.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                pageNum=pageNum+1;
                if (parentRowCountPerPage == 10) {
                    RecyclerViewStateUtils.setFooterViewState(EasyBusActivity.this, mPullToRefreshListView, 5, LoadingFooter.State.Loading, null);
                    startSearch();
                } else {
                    RecyclerViewStateUtils.setFooterViewState(EasyBusActivity.this, mPullToRefreshListView, 10, LoadingFooter.State.Normal, null);
                }
            }
        });
        mPullToRefreshListView.setRefreshing(true);
        mPullToRefreshListView.setAdapter(mLRecyclerViewAdapter);

    }

    /**
     * 初始化地图
     */
    private void initMap() {
        /**初始化搜索模块*/
        mPoiSearch = PoiSearch.newInstance();
        /**注册搜索事件监听*/
        mPoiSearch.setOnGetPoiSearchResultListener(this);
        /**获取建议检索实例*/
        mSuggestionSearch = SuggestionSearch.newInstance();
        /**设置建议请求结果监听器*/
        mSuggestionSearch.setOnGetSuggestionResultListener(this);
        /**获取百度地图的控件*/
        mBaiduMap = ((SupportMapFragment) (getSupportFragmentManager().findFragmentById(R.id.map))).getBaiduMap();

        /**开启定位图层*/
        mBaiduMap.setMyLocationEnabled(true);
        MyLocationData locData = new MyLocationData.Builder().accuracy(AppHolder.getInstance().getBdLocation().getRadius())
                                                             // 此处设置开发者获取到的方向信息，顺时针0-360
                                                             .direction(100).latitude(AppHolder.getInstance().getBdLocation().getLatitude()).longitude(AppHolder.getInstance().getBdLocation().getLongitude()).build();
        mBaiduMap.setMyLocationData(locData);
    }

    /**
     * 启动检索的方法
     */
    private void startSearch() {
        LLog.w("2222222222222");
        //Log.d("demo", "startSearch");
        /**附近检索参数*/
        PoiNearbySearchOption option = new PoiNearbySearchOption();
        option.keyword(keyword1);
        option.location(location);
        option.pageCapacity(pageCapacity);
        option.pageNum(pageNum);
        option.radius(radius);
        option.sortType(sortType);
        mPoiSearch.searchNearby(option);
    }

    /**
     * 影响搜索按钮点击事件
     * 点击搜索后会调用此方法
     *
     * @param v
     */
    public void searchButtonProcess(View v) {

        /**附近检索参数*/
        PoiNearbySearchOption option = new PoiNearbySearchOption();
        option.keyword(keyword1);
        option.location(location);
        option.pageCapacity(pageCapacity);
        option.pageNum(pageNum);
        option.radius(radius);
        option.sortType(sortType);
        mPoiSearch.searchNearby(option);

    }

    private class MyPoiOverlay extends PoiOverlay {

        public MyPoiOverlay(BaiduMap baiduMap) {
            super(baiduMap);
        }

        /**
         * 覆写此方法以改变默认点击行为
         */
        @Override
        public boolean onPoiClick(int index) {
            super.onPoiClick(index);
            Log.d("demo", "MyPoiOverlay_onPoiClick");
            PoiInfo poi = getPoiResult().getAllPoi().get(index);
            /**POI的详情检索*/
            mPoiSearch.searchPoiDetail((new PoiDetailSearchOption()).poiUid(poi.uid));
            // }
            return true;
        }
    }

    @Override
    public void onGetPoiResult(PoiResult result) {
        LLog.w("333333333333");
        LLog.w(result.error + "");
        if (result == null || result.error == SearchResult.ERRORNO.RESULT_NOT_FOUND) {
            Utils.showShortToast(this, getString(R.string.sorry_find_no_result));
            mPullToRefreshListView.refreshComplete();
            return;
        }
        List<BusStation> list = new ArrayList<>();
        if (result.error == SearchResult.ERRORNO.NO_ERROR) {
            for (int i = 0; i < result.getAllPoi().size(); i++) {
                Log.d("demo", " " + (i) + ": " + result.getAllPoi().get(i).type);
                Log.d("demo", " " + (i) + ": " + result.getAllPoi().get(i).address);

                BusStation bus = new BusStation();
                bus.setStationName(result.getAllPoi().get(i).name);
                bus.setStationDistance((int) (DistanceUtil.getDistance(location, result.getAllPoi().get(i).location)));
                bus.setStationAddress(result.getAllPoi().get(i).address);
                bus.setLoction(result.getAllPoi().get(i).location);
                list.add(bus);
                parentRowCountPerPage=list.size();
            }
            if (pageNum == ConstantsData.DEFAULT_PAGE_NUM) {
                listBusStation.clear();
            }
            listBusStation.addAll(list);
            adapter.setDataList(listBusStation);
            adapter.notifyDataSetChanged();
            mLRecyclerViewAdapter.notifyDataSetChanged();
            mPullToRefreshListView.refreshComplete();
            mBaiduMap.clear();
            mBaiduMap.setMapStatus(MapStatusUpdateFactory.newMapStatus(new MapStatus.Builder().zoom(15).build()));
            /**用于显示poi的overly*/
            PoiOverlay overlay = new MyPoiOverlay(mBaiduMap);
            /**设置地图marker点击事件监听*/
            mBaiduMap.setOnMarkerClickListener(overlay);
            /**设置POI数据*/
            overlay.setData(result);
            /**将所有的overly添加到地图上*/
            overlay.addToMap();
            /**
             * 缩放地图，使所有Overlay都在合适的视野内
             * 注: 该方法只对Marker类型的overlay有效
             * */
            overlay.zoomToSpan();
            return;
        }
        if (result.error == SearchResult.ERRORNO.AMBIGUOUS_KEYWORD) {

            mPullToRefreshListView.refreshComplete();

            // 当输入关键字在本市没有找到，但在其他城市找到时，返回包含该关键字信息的城市列表
            String strInfo = "在";
            for (CityInfo cityInfo : result.getSuggestCityList()) {
                strInfo += cityInfo.city;
                strInfo += ",";
            }
            strInfo += "找到结果";
            Utils.showShortToast(this, strInfo);
            //Toast.makeText(EasyBusActivity.this, strInfo, Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onGetPoiDetailResult(PoiDetailResult poiDetailResult) {
        if (poiDetailResult.error != SearchResult.ERRORNO.NO_ERROR) {
            Utils.showShortToast(this, getString(R.string.sorry_find_no_result));
            mPullToRefreshListView.refreshComplete();
        } else {
            Utils.showShortToast(this, poiDetailResult.getName() + ": " + poiDetailResult.getAddress());
        }
        mPullToRefreshListView.refreshComplete();
    }

    @Override
    public void onGetSuggestionResult(SuggestionResult suggestionResult) {
        mPullToRefreshListView.refreshComplete();
        if (suggestionResult == null || suggestionResult.getAllSuggestions() == null) {
            return;
        }
    }

    @Override
    protected void onDestroy() {
        mPoiSearch.destroy();
        mSuggestionSearch.destroy();
        mBaiduMap.setMyLocationEnabled(false);
        super.onDestroy();
    }
}
