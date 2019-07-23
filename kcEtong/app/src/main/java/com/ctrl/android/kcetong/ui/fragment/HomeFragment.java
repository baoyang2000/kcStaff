package com.ctrl.android.kcetong.ui.fragment;


import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.ctrl.android.kcetong.CustomApplication;
import com.ctrl.android.kcetong.R;
import com.ctrl.android.kcetong.adapter.HomeBannerAdapter;
import com.ctrl.android.kcetong.adapter.HomeListBottomAdapter;
import com.ctrl.android.kcetong.listener.HintDialogListener;
import com.ctrl.android.kcetong.menu.AppConfig;
import com.ctrl.android.kcetong.menu.MenuEntity;
import com.ctrl.android.kcetong.menu.MenuManageActivity;
import com.ctrl.android.kcetong.menu.adapter.IndexDataAdapter;
import com.ctrl.android.kcetong.menu.widget.LineGridView;
import com.ctrl.android.kcetong.model.Ad;
import com.ctrl.android.kcetong.model.AppHolder;
import com.ctrl.android.kcetong.model.BannerList;
import com.ctrl.android.kcetong.model.House;
import com.ctrl.android.kcetong.model.HouseBean;
import com.ctrl.android.kcetong.model.ServiceKind;
import com.ctrl.android.kcetong.model.ServiceList;
import com.ctrl.android.kcetong.model.SuperCommunity;
import com.ctrl.android.kcetong.toolkit.Url.Url;
import com.ctrl.android.kcetong.toolkit.util.AopUtils;
import com.ctrl.android.kcetong.toolkit.util.ConstantsData;
import com.ctrl.android.kcetong.toolkit.util.LLog;
import com.ctrl.android.kcetong.toolkit.util.S;
import com.ctrl.android.kcetong.toolkit.util.SharePrefUtil;
import com.ctrl.android.kcetong.toolkit.util.StrConstant;
import com.ctrl.android.kcetong.toolkit.util.Utils;
import com.ctrl.android.kcetong.toolkit.webutils.BaseTSubscriber;
import com.ctrl.android.kcetong.toolkit.webutils.RetrofitUtil;
import com.ctrl.android.kcetong.ui.SheQuTuangouActivity;
import com.ctrl.android.kcetong.ui.activity.CommunityFinanceActivity;
import com.ctrl.android.kcetong.ui.activity.CommunityHomeActivity;
import com.ctrl.android.kcetong.ui.activity.FamillyHotLineListActivity;
import com.ctrl.android.kcetong.ui.activity.ForumActivity;
import com.ctrl.android.kcetong.ui.activity.HouseListActivity;
import com.ctrl.android.kcetong.ui.activity.LoginActivity;
import com.ctrl.android.kcetong.ui.activity.NoticeListActivity;
import com.ctrl.android.kcetong.ui.activity.SurveyListActivity;
import com.ctrl.android.kcetong.ui.adapter.ListCommunityAdapter;
import com.ctrl.android.kcetong.ui.base.BaseActivity;
import com.ctrl.android.kcetong.ui.base.BaseFragment;
import com.ctrl.android.kcetong.ui.dialog.HintMessageDialog;
import com.ctrl.android.kcetong.ui.news.EasyServiceActivity;
import com.ctrl.android.kcetong.ui.news.PropertyServiceActivity;
import com.ctrl.android.kcetong.ui.news.SurroundingMerchantsActivity;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.jh.server.JHServer;
import com.stx.xhb.xbanner.XBanner;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;


/**
 * @项目名称: 诚信行<br>
 * @类描述: 首页<br>
 * @创建人： whs <br>
 * @创建时间： 2016/12/30 11:15 <br>
 * @修改人： <br>
 * @修改时间: 2016/12/30 11:15 <br>
 */
public class HomeFragment extends BaseFragment {

    public static final String TOP_LOCATION = "PPT_MALL_HOME_TOP";
    public static final String BOTTOM_LOATION = "PPT_HOME_BOTTOM";
    public static final String SHEQU_JINRONG = "SHEQU_JINRONG";
    @BindView(R.id.banner_1)//顶部的轮播图
            XBanner banner1;
    @BindView(R.id.logo)
    ImageView logo;
    @BindView(R.id.now_community_name)//小区名称
            TextView nowCommunityName;
    @BindView(R.id.log_out)//退出登录
            TextView log_out;
    @BindView(R.id.change_community_btn)
    TextView changeCommunityBtn;
    @BindView(R.id.banner_2)
    XBanner banner2;
    @BindView(R.id.parentScrollView)
    LinearLayout parentScrollView;
    @BindView(R.id.btn_unlocking)
    TextView btnUnlocking;
    @BindView(R.id.monitor)
    TextView monitor;
    @BindView(R.id.call_police)
    TextView callPolice;
    @BindView(R.id.smart_home)
    TextView smartHome;
    @BindView(R.id.call_property)
    TextView callProperty;
    @BindView(R.id.dial_plate)
    TextView dialPlate;
    @BindView(R.id.personal)
    TextView personal;
    @BindView(R.id.community_notice)
    TextView communityNotice;
    @BindView(R.id.easy_service)
    TextView easy_service;
    @BindView(R.id.surrounding_merchants)
    LinearLayout surrounding_merchants;
    @BindView(R.id.express)
    LinearLayout express;
    @BindView(R.id.retirement_care)
    LinearLayout retirement_care;
    @BindView(R.id.second_hand_goods)
    LinearLayout second_hand_goods;
    @BindView(R.id.community_group_buy)
    LinearLayout community_group_buy;
    @BindView(R.id.duty_lawyer_service)
    LinearLayout duty_lawyer_service;
    @BindView(R.id.property_service)
    LinearLayout property_service;
    @BindView(R.id.community_mall)
    LinearLayout community_mall;
    @BindView(R.id.community_finance)
    LinearLayout community_finance;
    @BindView(R.id.complaint_repair)
    LinearLayout complaint_repair;
    @BindView(R.id.community_act)
    LinearLayout communityAct;
    @BindView(R.id.community_survey)
    LinearLayout communitySurvey;
    @BindView(R.id.property_notice)
    LinearLayout propertyNotice;
    @BindView(R.id.community_bbs)
    LinearLayout communityBbs;
    @BindView(R.id.payment)
    LinearLayout payment;
    @BindView(R.id.car_inquiry)
    LinearLayout car_inquiry;
    @BindView(R.id.gv_lanuch_start)
    LineGridView gridView;
    //    @BindView(R.id.gv_lanuch_start2)
//    LineGridView gridView2;
    @BindView(R.id.banner_recycler)
    RecyclerView mBannerRecycler;
    @BindView(R.id.banner_recycler2)
    RecyclerView mBannerRecycler2;

    private Context context;
    private List<String> imgesUrl = new ArrayList<>();//轮播图顶部的图片
    private List<String> imgesUrl2 = new ArrayList<>();
    private List<String> targetUrl = new ArrayList<>();//轮播图顶部的地址
    private List<String> targetUrl_bottom = new ArrayList<>();//轮播图的地址
    private List<String> financeList = new ArrayList<>();//社区金融地址
    private List<House> listCommunity;//小区列表list
    private ListCommunityAdapter communityAdapter;//小区列表适配器
    private View mMenuView;//显示pop的view
    private Context mContext;
    Unbinder unbinder;
    /**
     * 经度
     */
    private double longitude;
    /**
     * 纬度
     */
    private double latitude;

    private final static String fileName = "menulist";
    private final static String fileNameUser = "menulist_user";
    private List<MenuEntity> indexDataAll = new ArrayList<MenuEntity>();
    private List<MenuEntity> indexDataUser = new ArrayList<MenuEntity>();
    private List<MenuEntity> indexDataList = new ArrayList<MenuEntity>();
    private IndexDataAdapter adapter;
    private static CustomApplication appContext;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.fragment_home, container, false);
            ButterKnife.bind(this, rootView);
            mContext = getActivity();
            context = this.getActivity();
            setBannerHeight(banner1, 45, 100);//动态设置轮播图高度
            setBannerHeight(banner2, 38, 100);

            if (AppHolder.getInstance().getVisiterFlg() == 1) {
                nowCommunityName.setText("未认证");
            } else if (AppHolder.getInstance().getVisiterFlg() == 0) {
                if (!S.isNull(AppHolder.getInstance().getHouse().getCommunityName())) {
                    nowCommunityName.setText(getResources().getString(R.string.current_plot) + ": " + S.getStr(AppHolder.getInstance().getHouse().getCommunityName()));
                } else {
                    if ("1".equals(AppHolder.getInstance().getMemberInfo().getSupers()) || "2".equals(AppHolder.getInstance().getMemberInfo().getSupers())) {//管理员
                        nowCommunityName.setText("请选择社区");
                    } else if ("0".equals(AppHolder.getInstance().getMemberInfo().getSupers())) {
                        nowCommunityName.setText("未认证");
                    }

                }
            }

            initBanner(TOP_LOCATION, 1);
            initBanner(BOTTOM_LOATION, 2);
            initBanner(SHEQU_JINRONG, 3);

            initGridView();
            initServiceKind();
            initAdList();
        }
        banner1.setOnItemClickListener(new XBanner.OnItemClickListener() {
            @Override
            public void onItemClick(XBanner xBanner, final int i) {
                if (targetUrl == null) return;
                if (targetUrl.size() > i)
                    if (Patterns.WEB_URL.matcher(targetUrl.get(i)).matches()) {
                        Intent intent = new Intent(HomeFragment.this.getActivity(), CommunityFinanceActivity.class);
                        intent.putExtra("url", targetUrl.get(i));
                        startActivity(intent);
                    }
            }
        });
        banner2.setOnItemClickListener(new XBanner.OnItemClickListener() {
            @Override
            public void onItemClick(XBanner xBanner, final int i) {
                if (targetUrl_bottom == null) return;
                if (targetUrl_bottom.size() > i)
                    if (Patterns.WEB_URL.matcher(targetUrl_bottom.get(i)).matches()) {
                        Intent intent = new Intent(HomeFragment.this.getActivity(), CommunityFinanceActivity.class);
                        intent.putExtra("url", targetUrl_bottom.get(i));
                        startActivity(intent);
                    }
            }
        });

        ButterKnife.bind(this, rootView);
        longitude = AppHolder.getInstance().getBdLocation().getLongitude();
        latitude = AppHolder.getInstance().getBdLocation().getLatitude();
        LLog.i(AppHolder.getInstance().getMemberInfo().getSupers() + "");
        unbinder = ButterKnife.bind(this, rootView);

        Intent it = new Intent();
        it.setClass(context, JHServer.class);
        //启动服务
        context.startService(it);
        return rootView;
    }

    /**
     * TODO: 加载广告
     */
    private void initAdList() {

        mBannerRecycler.setLayoutManager(new LinearLayoutManager(mContext));
        mBannerRecycler.addItemDecoration(new DividerItemDecoration(mContext, DividerItemDecoration.VERTICAL));
        mBannerRecycler.setNestedScrollingEnabled(false);
        final HomeBannerAdapter homeBannerAdapter = new HomeBannerAdapter(R.layout.item_home_banner, null);
        mBannerRecycler.setAdapter(homeBannerAdapter);
        homeBannerAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                Intent intent = new Intent(mContext, CommunityFinanceActivity.class);
                intent.putExtra("url", homeBannerAdapter.getItem(position).getRedirectUrl());
                startActivity(intent);
            }
        });

        if (AppHolder.getInstance().getCommunity() == null || S.isNull(AppHolder.getInstance().getCommunity().getId())) {
            return;
        }
        Map<String, String> params = new HashMap<>();
        params.put(ConstantsData.METHOD, "pm.ppt.banner.list");//方法名称
        params.put("communityId", AppHolder.getInstance().getCommunity().getId());
        params.putAll(ConstantsData.getSystemParams());
        String sign = AopUtils.sign(params, ConstantsData.SECRET_VALUE);
        params.put("sign", sign);
        params.remove(ConstantsData.METHOD);
        LLog.d(params + "");
        RetrofitUtil
                .Api()
                .serviceBannerList(params)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseTSubscriber<BannerList>((BaseActivity) context) {
                    @Override
                    public void onResponseCallback(JSONObject response, String resultCode) {
                        LLog.d("onResponseCallback: " + response);
                    }

                    @Override
                    public void onNext(BannerList bannerList) {
                        LLog.d(bannerList.toString());
                        if (bannerList.getData().getBannerList() != null && bannerList.getData().getBannerList().size() > 0) {
                            // TODO: 填充数据
                            homeBannerAdapter.setNewData(bannerList.getData().getBannerList());

                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                    }
                });
    }

    /**
     * 获取大分类
     */
    private void initServiceKind() {

        mBannerRecycler2.setLayoutManager(new GridLayoutManager(mContext, 4));
        mBannerRecycler2.addItemDecoration(new DividerItemDecoration(mContext, DividerItemDecoration.VERTICAL));
        mBannerRecycler2.addItemDecoration(new DividerItemDecoration(mContext, DividerItemDecoration.HORIZONTAL));
        mBannerRecycler2.setNestedScrollingEnabled(false);
        final HomeListBottomAdapter homeBannerAdapter = new HomeListBottomAdapter(R.layout.item_gridview, null);
        mBannerRecycler2.setAdapter(homeBannerAdapter);

        homeBannerAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                Intent intent = new Intent();
                Bundle bundle = new Bundle();
                String title = homeBannerAdapter.getItem(position).getSortsName();
                String strId = homeBannerAdapter.getItem(position).getId();
                if ("便民服务".equals(title)) {
                    intent = new Intent(context, EasyServiceActivity.class);
                    startActivity(intent);
                } else {
                    intent = new Intent(context, SurroundingMerchantsActivity.class);
                    intent.putExtra("title", title);
                    intent.putExtra("serviceKindId", strId);
                    startActivity(intent);
                }
            }
        });

        if (AppHolder.getInstance().getCommunity() == null || S.isNull(AppHolder.getInstance().getCommunity().getId())) {
            return;
        }
        Map<String, String> params = new HashMap<>();
        params.put(ConstantsData.METHOD, Url.serviceListUrl);//方法名称
        params.put("communityId", AppHolder.getInstance().getCommunity().getId());
        params.putAll(ConstantsData.getSystemParams());
        String sign = AopUtils.sign(params, ConstantsData.SECRET_VALUE);
        params.put("sign", sign);
        params.remove(ConstantsData.METHOD);
        LLog.d(params + "");
        RetrofitUtil.Api().serviceKindList(params).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new BaseTSubscriber<ServiceList>((BaseActivity) context) {
            @Override
            public void onResponseCallback(JSONObject response, String resultCode) {
                LLog.d("onResponseCallback: " + response);
            }

            @Override
            public void onNext(ServiceList ad) {
                LLog.e(ad.toString());
                ServiceKind serviceKind = new ServiceKind();
                serviceKind.setSortsUrl("mall_logo");
                serviceKind.setId("all");
                serviceKind.setSortsName("便民服务");
                ad.getData().getServiceKindList().add(0, serviceKind);
                homeBannerAdapter.setNewData(ad.getData().getServiceKindList());
            }

            @Override
            public void onError(Throwable e) {
                super.onError(e);
            }
        });
    }

    private void initGridView() {
        gridView.setFocusable(false);
        String strByJson = getJson(context, fileName);

        //Json的解析类对象
        JsonParser parser = new JsonParser();
        //将JSON的String 转成一个JsonArray对象
        JsonArray jsonArray = parser.parse(strByJson).getAsJsonArray();
        Gson gson = new Gson();
        //加强for循环遍历JsonArray
        for (JsonElement indexArr : jsonArray) {
            //使用GSON，直接转成Bean对象
            MenuEntity menuEntity = gson.fromJson(indexArr, MenuEntity.class);
            indexDataAll.add(menuEntity);
        }

        String str = getJson(context, fileNameUser);

        //Json的解析类对象
        JsonParser parser1 = new JsonParser();
        //将JSON的String 转成一个JsonArray对象
        JsonArray jsonArray1 = parser1.parse(str).getAsJsonArray();
        Gson gson1 = new Gson();
        //加强for循环遍历JsonArray
        for (JsonElement indexArr : jsonArray1) {
            //使用GSON，直接转成Bean对象
            MenuEntity menuEntity = gson1.fromJson(indexArr, MenuEntity.class);
            indexDataUser.add(menuEntity);
        }

        //appContext.delFileData(AppConfig.KEY_All);

        String key = AppConfig.KEY_All;
        String keyUser = AppConfig.KEY_USER;
        appContext = (CustomApplication) context.getApplicationContext();
        appContext.saveObject((Serializable) indexDataAll, AppConfig.KEY_All);

        List<MenuEntity> indexDataUser = (List<MenuEntity>) appContext.readObject(AppConfig.KEY_USER);
        if (indexDataUser == null || indexDataUser.size() == 0) {
//            appContext.saveObject((Serializable) indexDataAll, AppConfig.KEY_USER);
            appContext.saveObject((Serializable) this.indexDataUser, AppConfig.KEY_USER);
        }
        indexDataList = (List<MenuEntity>) appContext.readObject(AppConfig.KEY_USER);

        MenuEntity allMenuEntity = new MenuEntity();
        allMenuEntity.setIco("ic_more");
        allMenuEntity.setId("all");
        allMenuEntity.setTitle("更多应用");
        indexDataList.add(allMenuEntity);
        adapter = new IndexDataAdapter(context, indexDataList);
        gridView.setAdapter(adapter);
        LLog.e(strByJson);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent();
                Bundle bundle = new Bundle();
                String title = indexDataList.get(position).getTitle();
                String strId = indexDataList.get(position).getId();
                LLog.i(title + strId);
                if (strId.equals("all")) {// 更多
                    intent.setClass(context, MenuManageActivity.class);
                    startActivity(intent);
                }
                item(title);
            }
        });
    }

    public static String getJson(Context context, String fileName) {
        StringBuilder stringBuilder = new StringBuilder();
        try {
            AssetManager assetManager = context.getAssets();
            BufferedReader bf = new BufferedReader(new InputStreamReader(
                    assetManager.open(fileName)));
            String line;
            while ((line = bf.readLine()) != null) {
                stringBuilder.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
            LLog.e(e.toString());
        }
        return stringBuilder.toString();
    }

    @Override
    public void onResume() {
        super.onResume();
        banner1.startAutoPlay();
        banner2.startAutoPlay();

        indexDataList.clear();
        indexDataList = (List<MenuEntity>) appContext.readObject(AppConfig.KEY_USER);
        MenuEntity allMenuEntity = new MenuEntity();
        allMenuEntity.setIco("ic_more");
        allMenuEntity.setId("all");
        allMenuEntity.setTitle("更多应用");
        indexDataList.add(allMenuEntity);
        adapter = new IndexDataAdapter(context, indexDataList);
        gridView.setAdapter(adapter);
    }

    @Override
    public void onPause() {
        super.onPause();
        banner1.stopAutoPlay();
        banner2.stopAutoPlay();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    private void confrim2(Class activity) {

        if (AppHolder.getInstance().getProprietor() == null || S.isNull(AppHolder.getInstance().getProprietor().getProprietorId())) {//未认证
            showProperDialog(context.getString(R.string.not_found_owner_information));
        } else {
            if (AppHolder.getInstance().getHouse().getProprietorId() == null) {
                Utils.toastError(context, context.getString(R.string.please_));
            } else {
                startActivity(new Intent(context, activity));
            }
        }
    }

    @OnClick({R.id.now_community_name, R.id.log_out, R.id.btn_unlocking, R.id.monitor, R.id.call_police, R.id.smart_home, R.id.call_property,
            R.id.dial_plate, R.id.personal, R.id.community_notice, R.id.easy_service, R.id.surrounding_merchants,
            R.id.property_service, R.id.express, R.id.retirement_care, R.id.second_hand_goods, R.id.car_rental_instead_of_driving,
            R.id.community_finance, R.id.community_group_buy, R.id.community_hospital, R.id.duty_lawyer_service, R.id.community_mall,
            R.id.complaint_repair, R.id.community_act, R.id.community_survey, R.id.property_notice, R.id.community_bbs, R.id.payment,
            R.id.car_inquiry})
    public void onClick(View view) {
        Intent intent;
        switch (view.getId()) {
            case R.id.now_community_name:
                break;
            case R.id.log_out://退出登录
                final HintMessageDialog hintDialog = new HintMessageDialog(this.getContext());
                hintDialog.showHintDialog("提示", "确定要退出吗？", new HintDialogListener() {
                    @Override
                    public void submitListener() {
                        SharePrefUtil.saveString(context, ConstantsData.USERNAME, "");
                        SharePrefUtil.saveString(context, ConstantsData.PASSWORD, "");
                        Intent intent1 = new Intent(context, LoginActivity.class);
                        intent1.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        intent1.putExtra("exit", 1);
                        startActivity(intent1);
                        HomeFragment.this.getActivity().finish();
                    }

                    @Override
                    public void cancelListener() {
                        hintDialog.dismiss();
                    }
                });
                break;
            case R.id.btn_unlocking://开锁
                Utils.toastError(context, StrConstant.NOT_YET_OPENED);
                break;
            case R.id.monitor://监控
                Utils.toastError(context, StrConstant.NOT_YET_OPENED);
                break;
            case R.id.call_police://安防报警
                Utils.toastError(context, StrConstant.NOT_YET_OPENED);
                break;
            case R.id.smart_home://智能家居
                Utils.toastError(context, StrConstant.NOT_YET_OPENED);
                break;
            case R.id.call_property://呼叫物业
                Utils.toastError(context, StrConstant.NOT_YET_OPENED);
                break;
            case R.id.dial_plate://社区电话
                Utils.toastError(context, StrConstant.NOT_YET_OPENED);
                break;
            case R.id.personal://个人中心
                Utils.toastError(context, StrConstant.NOT_YET_OPENED);
                break;
            case R.id.community_notice://社区通知
                Utils.toastError(context, StrConstant.NOT_YET_OPENED);
                break;
            case R.id.easy_service://便民服务
                intent = new Intent(this.getActivity(), EasyServiceActivity.class);
                startActivity(intent);
                break;
            case R.id.surrounding_merchants://周边商户
                intent = new Intent(this.getActivity(), SurroundingMerchantsActivity.class);
                startActivity(intent);
                break;
            case R.id.property_service://物业服务
                intent = new Intent(this.getActivity(), PropertyServiceActivity.class);
                startActivity(intent);
                break;
            case R.id.express://快递
                Utils.toastError(context, StrConstant.NOT_YET_OPENED);
                break;
            case R.id.retirement_care://养老医疗
                Utils.toastError(context, StrConstant.NOT_YET_OPENED);
                break;
            case R.id.second_hand_goods://二手物品
                Utils.toastError(context, StrConstant.NOT_YET_OPENED);
                break;
            case R.id.car_rental_instead_of_driving://租车代驾
                Utils.toastError(context, StrConstant.NOT_YET_OPENED);
                break;
            case R.id.community_finance://社区金融
                Utils.toastError(context, StrConstant.NOT_YET_OPENED);
                break;
            case R.id.community_group_buy://社区团购
                startActivity(new Intent(mContext, SheQuTuangouActivity.class));
                break;
            case R.id.car_inquiry:
//                Utils.toastError(context, StrConstant.NOT_YET_OPENED);
                intent = new Intent(this.getActivity(), CommunityFinanceActivity.class);
                intent.putExtra("url", "https://auto.news18a.com/m/price/index/index/yitongfuwu/");
                startActivity(intent);
                break;
            case R.id.community_hospital://社区医院
                Utils.toastError(context, StrConstant.NOT_YET_OPENED);
                break;
            case R.id.duty_lawyer_service://律师服务
                Utils.toastError(context, StrConstant.NOT_YET_OPENED);
                break;
            case R.id.community_mall://社区商城
                Utils.toastError(context, StrConstant.NOT_YET_OPENED);
                break;
            case R.id.complaint_repair:
                confrim2(FamillyHotLineListActivity.class);
                break;
            case R.id.community_act:
                confrim2(CommunityHomeActivity.class);
                break;
            case R.id.property_notice:
                confrim2(NoticeListActivity.class);
                break;
            case R.id.community_survey:
                confrim2(SurveyListActivity.class);
                break;
            case R.id.payment:
                Utils.toastError(context, StrConstant.NOT_YET_OPENED);
                break;
            case R.id.community_bbs://论坛
                confrim2(ForumActivity.class);
                break;
        }
    }

    private void item(String title) {
        Intent intent;
        switch (title) {
            case "开锁":
                Utils.toastError(context, StrConstant.NOT_YET_OPENED);
                break;
            case "监控":
                Utils.toastError(context, StrConstant.NOT_YET_OPENED);
                break;
            case "安防报警":
                Utils.toastError(context, StrConstant.NOT_YET_OPENED);
                break;
            case "智能家居":
                Utils.toastError(context, StrConstant.NOT_YET_OPENED);
                break;
            case "呼叫物业":
                Utils.toastError(context, StrConstant.NOT_YET_OPENED);
                break;
            case "社区电话":
                Utils.toastError(context, StrConstant.NOT_YET_OPENED);
                break;
            case "社区通知":
                Utils.toastError(context, StrConstant.NOT_YET_OPENED);
                break;
        }
    }

    //请求轮播图顶部
    private void initBanner(String location, final int index) {
        Map<String, String> params = new HashMap<>();
        params.put(ConstantsData.METHOD, "pm.ppt.advertisement.list");//方法名称
        params.put("location", location);
        params.putAll(ConstantsData.getSystemParams());
        String sign = AopUtils.sign(params, ConstantsData.SECRET_VALUE);
        params.put("sign", sign);
        params.remove(ConstantsData.METHOD);
        LLog.d(params + "");
        RetrofitUtil.Api().Banner(params).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new BaseTSubscriber<Ad>((BaseActivity) context) {
            @Override
            public void onResponseCallback(JSONObject response, String resultCode) {
                LLog.d("onResponseCallback: " + response);
            }

            @Override
            public void onNext(Ad ad) {
                if (ad.getCode().equals("000")) {
                    if (index == 1) {
                        LLog.d(index + "130");
                        String str = ad.getData().getAdvertisementList().get(0).getImgUrl().replace(" ", "");
                        imgesUrl = Arrays.asList(str.split(","));
                        String str2 = ad.getData().getAdvertisementList().get(0).getTargetUrl().replace(" ", "");//去掉所用空格;
                        targetUrl = Arrays.asList(str2.split(","));
                        LLog.d(imgesUrl + "126");
                        banner1.setData(imgesUrl, null);//第二个参数为提示文字资源集合
                        banner1.setmAdapter(new XBanner.XBannerAdapter() {
                            @Override
                            public void loadBanner(XBanner banner1, View view, int position) {
                                Glide.with(context).load(imgesUrl.get(position)).into((ImageView) view);
                            }
                        });
                    } else if (index == 2) {
                        LLog.d(index + "144");
                        String str = ad.getData().getAdvertisementList().get(0).getImgUrl().replace(" ", "");
                        imgesUrl2 = Arrays.asList(str.split(","));
                        String str2 = ad.getData().getAdvertisementList().get(0).getTargetUrl().replace(" ", "");//去掉所用空格;
                        targetUrl_bottom = Arrays.asList(str2.split(","));
                        banner2.setData(imgesUrl2, null);//第二个参数为提示文字资源集合
                        banner2.setmAdapter(new XBanner.XBannerAdapter() {
                            @Override
                            public void loadBanner(XBanner banner2, View view, int position) {
                                Glide.with(context).load(imgesUrl2.get(position)).into((ImageView) view);
                            }
                        });
                    } else if (index == 3) {
                        LLog.d(index + "144");
                        String str = ad.getData().getAdvertisementList().get(0).getImgUrl().replace(" ", "");
                        financeList = Arrays.asList(str.split(","));

                        if (financeList.size() > 0 && financeList.get(0) != null) {
                            AppHolder.getInstance().setFinanceUrl(financeList.get(0));
                            LLog.d(AppHolder.getInstance().getFinanceUrl());
                        }

                    }

                } else if (ad.getCode().equals("002")) {
                    if (index == 1) {
                        imgesUrl.add("http://sudasuta.com/wp-content/uploads/2013/10/10143181686_375e063f2c_z.jpg");
                        imgesUrl.add("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1505996071262&di=ad1a19ce3e4fafd85d3a5552abc1ee5b&imgtype=0&src=http%3A%2F%2Fimgsrc.baidu.com%2Fimage%2Fc0%253Dshijue1%252C0%252C0%252C294%252C40%2Fsign%3D0a0eda45c9cec3fd9f33af36bee1be4a%2Ffd039245d688d43f3488b67b771ed21b0ef43b11.jpg");
                        imgesUrl.add("https://ss1.bdstatic.com/70cFvXSh_Q1YnxGkpoWK1HF6hhy/it/u=3956805945,1536371408&fm=11&gp=0.jpg");

                        banner1.setData(imgesUrl, null);//第二个参数为提示文字资源集合
                        banner1.setmAdapter(new XBanner.XBannerAdapter() {
                            @Override
                            public void loadBanner(XBanner banner1, View view, int position) {
                                Glide.with(context).load(imgesUrl.get(position)).into((ImageView) view);
                            }
                        });
                    }

                }
            }

            @Override
            public void onError(Throwable e) {
                super.onError(e);
            }
        });

    }

    private void showProperDialog(String info) {
        final HintMessageDialog hintDialog = new HintMessageDialog(this.getContext());
        hintDialog.showHintDialog("提示", info, new HintDialogListener() {
            @Override
            public void submitListener() {
                hintDialog.dismiss();
                Intent intent = new Intent(getActivity(), HouseListActivity.class);
                startActivity(intent);
                // Toast.makeText(getActivity(), "稍后" , Toast.LENGTH_SHORT).show();
            }

            @Override
            public void cancelListener() {
                hintDialog.dismiss();
            }
        });
    }


    //动态设置轮播图高度
    private void setBannerHeight(XBanner xBanner, int arg1, int arg2) {
        if (xBanner == banner1) {
            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) xBanner.getLayoutParams();
            int width = Utils.getScreenWidth(this.getActivity());
            params.height = width * arg1 / arg2;
            xBanner.setLayoutParams(params);
        } else {
            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) xBanner.getLayoutParams();
            int width = Utils.getScreenWidth(this.getActivity());
            params.height = width * arg1 / arg2;
            xBanner.setLayoutParams(params);
        }
    }

    private int currentPage = 1;
    private int rowCountPerPage = 10;

    /**
     * 房屋列表
     */
    private void getHouseList(String memberId) {

        showProgress(true);
        Map<String, String> map = new HashMap();
        map.putAll(ConstantsData.getSystemParams());
        map.put(ConstantsData.METHOD, Url.houseListUrl);
        map.put("memberId", memberId);
        map.put("currentPage", currentPage + "");
        map.put("rowCountPerPage", rowCountPerPage + "");
        String sign = AopUtils.sign(map, ConstantsData.SECRET_VALUE);
        map.put("sign", sign);
        map.remove(ConstantsData.METHOD);
        RetrofitUtil.Api().houseList(map).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new BaseTSubscriber<HouseBean>((BaseActivity) getActivity()) {
            @Override
            public void onResponseCallback(JSONObject response, String resultCode) {
                showProgress(false);
            }

            @Override
            public void onNext(HouseBean houseBean) {
                super.onNext(houseBean);

                if (TextUtils.equals(houseBean.getCode(), "000")) {

                    listCommunity = houseBean.getData().getHousesList();
                    showCommunityListPop(listCommunity);
                }
                showProgress(false);
            }

            @Override
            public void onNetError(Throwable e) {
                super.onNetError(e);

                if (e.getMessage() == null) {

                } else if (e.getMessage().contains("Failed to connect") || e.getMessage().contains("failed to connect") || e.getMessage().contains("SocketTimeoutException")) {
                    Utils.toastError(getActivity(), "服务器连接超时,请检查网络");
                } else if ("Invalid index 0, size is 0".equals(e.getMessage())) {//连接超时

                } else if (e.getMessage().contains("Unable to resolve host")) {//网络访问错误
                    Utils.toastError(getActivity(), "网络请求错误");
                } else {
                    Utils.toastError(getActivity(), "网络请求错误");
                }
                showProgress(false);
            }
        });
    }

    private List<House> houseList = new ArrayList<>();

    //管理员获取小区列表信息
    private void getCommunityList() {
        showProgress(true);
        Map<String, String> map = new HashMap<>();
        map.putAll(ConstantsData.getSystemParams());
        map.put(ConstantsData.METHOD, Url.superCommunityListUrl);
        String sign = AopUtils.sign(map, ConstantsData.SECRET_VALUE);
        map.put("sign", sign);
        map.remove(ConstantsData.METHOD);
        RetrofitUtil.Api().CommunityList(map).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new BaseTSubscriber<SuperCommunity>((BaseActivity) context) {
            @Override
            public void onResponseCallback(JSONObject response, String resultCode) {
                LLog.d("onResponseCallback: " + response);
                showProgress(false);
            }

            @Override
            public void onNext(SuperCommunity superCommunity) {
                super.onNext(superCommunity);
                if (TextUtils.equals(superCommunity.getCode(), "000")) {

                    houseList = superCommunity.getData().getHousesList();
                    showCommunityListPop(houseList);

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
     * 显示小区列表的 popwindow
     */
    private void showCommunityListPop(final List<House> list) {

        communityAdapter = new ListCommunityAdapter(getActivity());
        communityAdapter.setList(list);

        mMenuView = LayoutInflater.from(getActivity()).inflate(R.layout.choose_list_pop2, null);
        ListView listView = (ListView) mMenuView.findViewById(R.id.listView);
        listView.setAdapter(communityAdapter);

        final PopupWindow pop = new PopupWindow();

        // 设置SelectPicPopupWindow的View
        pop.setContentView(mMenuView);
        // 设置SelectPicPopupWindow弹出窗体的宽
        pop.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        // 设置SelectPicPopupWindow弹出窗体的高
//        pop.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        pop.setHeight(500);
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

                House c = list.get(position);
                AppHolder.getInstance().setHouse(c);
                AppHolder.getInstance().getProprietor().setProprietorId(c.getProprietorId());
                AppHolder.getInstance().getHouse().setCommunityName(c.getCommunityName());
                if ("1".equals(AppHolder.getInstance().getMemberInfo().getSupers()) || "2".equals(AppHolder.getInstance().getMemberInfo().getSupers())) {
                    AppHolder.getInstance().getHouse().setCommunityId(c.getId());
                } else {
                    AppHolder.getInstance().getHouse().setCommunityId(c.getCommunityId());
                }
                nowCommunityName.setText(getResources().getString(R.string.current_plot) + ": " + S.getStr(AppHolder.getInstance().getHouse().getCommunityName()));
                pop.dismiss();
            }
        });

        //mMenuView添加OnTouchListener监听判断获取触屏位置如果在选择框外面则销毁弹出框
        mMenuView.setOnTouchListener(new View.OnTouchListener() {

            public boolean onTouch(View v, MotionEvent event) {

                int height = mMenuView.findViewById(R.id.pop_layout).getTop();
                int y = (int) event.getY();
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
        pop.showAtLocation(parentScrollView, Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
    }

    /**
     * 检查手机上是否安装了指定的软件
     *
     * @param packageName：应用包名
     * @return
     */
    private boolean isAvilible(String packageName) {
        //获取packagemanager
        final PackageManager packageManager = context.getPackageManager();
        //获取所有已安装程序的包信息
        List<PackageInfo> packageInfos = packageManager.getInstalledPackages(0);
        //用于存储所有已安装程序的包名
        List<String> packageNames = new ArrayList<String>();
        //从pinfo中将包名字逐一取出，压入pName list中
        if (packageInfos != null) {
            for (int i = 0; i < packageInfos.size(); i++) {
                String packName = packageInfos.get(i).packageName;
                packageNames.add(packName);
            }
        }
        //判断packageNames中是否有目标程序的包名，有TRUE，没有FALSE
        return packageNames.contains(packageName);
    }
}
