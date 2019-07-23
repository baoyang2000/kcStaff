package com.ctrl.android.kcetong.ui.activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.ctrl.android.kcetong.R;
import com.ctrl.android.kcetong.model.AppHolder;
import com.ctrl.android.kcetong.model.House;
import com.ctrl.android.kcetong.model.HouseBean;
import com.ctrl.android.kcetong.toolkit.Url.Url;
import com.ctrl.android.kcetong.toolkit.util.AopUtils;
import com.ctrl.android.kcetong.toolkit.util.BaseLinearLayoutManager;
import com.ctrl.android.kcetong.toolkit.util.ConstantsData;
import com.ctrl.android.kcetong.toolkit.util.LLog;
import com.ctrl.android.kcetong.toolkit.util.StrConstant;
import com.ctrl.android.kcetong.toolkit.util.Utils;
import com.ctrl.android.kcetong.toolkit.webutils.BaseTSubscriber;
import com.ctrl.android.kcetong.toolkit.webutils.RetrofitUtil;
import com.ctrl.android.kcetong.ui.adapter.HouseListAdapter;
import com.ctrl.android.kcetong.ui.base.BaseActivity;
import com.ctrl.android.kcetong.ui.view.DividerDecoration;
import com.ctrl.third.common.library.utils.AnimUtil;
import com.github.jdsjlzx.interfaces.OnItemClickListener;
import com.github.jdsjlzx.interfaces.OnLoadMoreListener;
import com.github.jdsjlzx.interfaces.OnRefreshListener;
import com.github.jdsjlzx.recyclerview.LRecyclerView;
import com.github.jdsjlzx.recyclerview.LRecyclerViewAdapter;
import com.github.jdsjlzx.recyclerview.ProgressStyle;
import com.github.jdsjlzx.util.RecyclerViewStateUtils;
import com.github.jdsjlzx.view.LoadingFooter;

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

public class HouseListActivity extends BaseActivity {

    @BindView(R.id.listView)
    LRecyclerView listView;
    @BindView(R.id.add_house_btn)
    TextView      addHouseBtn;
    private int    currentPage     = 1;
    private int    rowCountPerPage = 10;
    private HouseListAdapter     adapter;
    private LRecyclerViewAdapter mLRecyclerViewAdapter;
    private List<House>          list;
    private int                  parentRowCountPerPage;
    private String               memberId;
    private static final int CHOOSE_ROOM_CODE = 3;
    private House house;

    @Override
    protected void initView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_house_list);
        ButterKnife.bind(this);
        toolbarBaseSetting(StrConstant.HOUSES_TITLE, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HouseListActivity.this.finish();
            }
        });
    }

    @Override
    protected void initData() {

        house = new House();
        memberId = AppHolder.getInstance().getMemberInfo().getMemberId();
        LLog.w("memberId =" + memberId);
        list = new ArrayList<>();
        adapter = new HouseListAdapter(this, new HouseListAdapter.HouseBindListener() {
            @Override
            public void removeHouseBind(House listBean, int position) {
                impRemoveHouseBind(listBean.getId(), position);
            }
        });

        adapter.setDataList(list);
        mLRecyclerViewAdapter = new LRecyclerViewAdapter(adapter);
        listView.setLayoutManager(new BaseLinearLayoutManager(this));
        final DividerDecoration divider = new DividerDecoration.Builder(this).setHeight(R.dimen.default_divider_height).setColorResource(R.color.light_gray).build();
        listView.addItemDecoration(divider);
        listView.setRefreshProgressStyle(ProgressStyle.BallSpinFadeLoader);
        listView.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh() {
                currentPage = 1;
                houseList();
            }
        });
        listView.setRefreshing(true);
        listView.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                currentPage++;
                if (parentRowCountPerPage == 5) {
                    RecyclerViewStateUtils.setFooterViewState(HouseListActivity.this, listView, 5, LoadingFooter.State.Loading, null);
                    houseList();
                } else {
                    RecyclerViewStateUtils.setFooterViewState(HouseListActivity.this, listView, 10, LoadingFooter.State.Normal, null);
                }
            }
        });
        listView.setAdapter(mLRecyclerViewAdapter);
        mLRecyclerViewAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int i) {
                setHouseBind(list.get(i).getId(), i);
            }

            @Override
            public void onItemLongClick(View view, int i) {

            }
        });
    }

    /**
     * 房屋列表
     */
    private void houseList() {

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
        RetrofitUtil.Api().houseList(map).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new BaseTSubscriber<HouseBean>(this) {
            @Override
            public void onResponseCallback(JSONObject response, String resultCode) {

            }

            @Override
            public void onNext(HouseBean houseBean) {
                super.onNext(houseBean);

                if (TextUtils.equals(houseBean.getCode(), "000")) {
                    if (currentPage == 1) {
                        list.clear();
                    }
                    parentRowCountPerPage = houseBean.getData().getHousesList().size();
                    list.addAll(houseBean.getData().getHousesList());
                    adapter.setDataList(list);
                    adapter.notifyDataSetChanged();
                    mLRecyclerViewAdapter.notifyDataSetChanged();
                    if (list.size() > 0) {
                        for (House house : list) {
                            if (house.getIsDefault() == 1) {
                                AppHolder.getInstance().setHouse(house);
                                AppHolder.getInstance().getProprietor().setProprietorId(house.getProprietorId());
                                AppHolder.getInstance().getHouse().setCommunityName(house.getCommunityName());
                                AppHolder.getInstance().getHouse().setCommunityId(house.getCommunityId());
                            }
                        }
                    }
                }

                listView.refreshComplete();
                showProgress(false);
            }

            @Override
            public void onNetError(Throwable e) {
                super.onNetError(e);

                if (e.getMessage() == null) {

                } else if (e.getMessage().contains("Failed to connect") || e.getMessage().contains("failed to connect") || e.getMessage().contains("SocketTimeoutException")) {
                    Utils.toastError(HouseListActivity.this, "服务器连接超时,请检查网络");
                } else if ("Invalid index 0, size is 0".equals(e.getMessage())) {//连接超时

                } else if (e.getMessage().contains("Unable to resolve host")) {//网络访问错误
                    Utils.toastError(HouseListActivity.this, "网络请求错误");
                } else {
                    Utils.toastError(HouseListActivity.this, "网络请求错误");
                }
                listView.refreshComplete();
                showProgress(false);
            }
        });
    }

    /**
     * 解除房屋绑定
     */
    private void impRemoveHouseBind(String housesBindId, final int position) {

        showProgress(true);
        Map<String, String> map = new HashMap();
        map.putAll(ConstantsData.getSystemParams());
        map.put(ConstantsData.METHOD, Url.removeHouseBind);
        map.put("memberId", memberId);
        map.put("housesBindId", housesBindId);
        String sign = AopUtils.sign(map, ConstantsData.SECRET_VALUE);
        map.put("sign", sign);
        map.remove(ConstantsData.METHOD);

        RetrofitUtil.Api().removeHouseBind(map).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new BaseTSubscriber<ResponseBody>(this) {
            @Override
            public void onResponseCallback(JSONObject response, String resultCode) {

                if (TextUtils.equals(ConstantsData.success, resultCode)) {
                    Utils.showShortToast(HouseListActivity.this, StrConstant.deleteSuccess);
                    list.remove(position);
                    adapter.notifyDataSetChanged();
                    mLRecyclerViewAdapter.notifyDataSetChanged();

                    if (list.size() > 0) {
                        for (int i = 0; i < list.size(); i++) {
                            if (list.get(i).getIsDefault() == 1) {
                                AppHolder.getInstance().setHouse(list.get(i));
                                AppHolder.getInstance().getProprietor().setProprietorId(list.get(i).getProprietorId());
                                AppHolder.getInstance().getHouse().setCommunityName(list.get(i).getCommunityName());
                                AppHolder.getInstance().getHouse().setCommunityId(list.get(i).getCommunityId());
                            }
                        }
                    } else {
                        AppHolder.getInstance().setHouse(new House());
                        AppHolder.getInstance().getProprietor().setProprietorId("");
                    }
                } else {
                    Utils.toastError(HouseListActivity.this, StrConstant.deleteFailed);
                }
                showProgress(false);
            }
        });
    }

    /**
     * 设置默认房屋
     */
    private void setHouseBind(String housesBindId, final int position) {

        showProgress(true);
        Map<String, String> map = new HashMap();
        map.putAll(ConstantsData.getSystemParams());
        map.put(ConstantsData.METHOD, Url.setDefaultHouse);
        map.put("memberId", memberId);
        map.put("housesBindId", housesBindId);
        String sign = AopUtils.sign(map, ConstantsData.SECRET_VALUE);
        map.put("sign", sign);
        map.remove(ConstantsData.METHOD);

        RetrofitUtil.Api().setHouseBind(map).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new BaseTSubscriber<ResponseBody>(this) {
            @Override
            public void onResponseCallback(JSONObject response, String resultCode) {
                showProgress(false);
                if (TextUtils.equals(resultCode, ConstantsData.success)) {
                    Utils.showShortToast(HouseListActivity.this, StrConstant.HouseBindSuccess);
                    for (int i = 0; i < list.size(); i++) {
                        if (i == position) {
                            list.get(i).setIsDefault(1);
                            AppHolder.getInstance().setHouse(list.get(i));
                            house = list.get(i);
                            AppHolder.getInstance().getProprietor().setProprietorId(list.get(i).getProprietorId());
                        } else {
                            list.get(i).setIsDefault(0);
                        }
                    }
                    adapter.notifyDataSetChanged();
                    mLRecyclerViewAdapter.notifyDataSetChanged();
                    Intent intent = new Intent();
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("house", house);
                    intent.putExtras(bundle);
                    setResult(2001, intent);
                } else {
                    try {
                        Utils.showShortToast(HouseListActivity.this, response.getString("description"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    @OnClick(R.id.add_house_btn)
    public void onClick() {
        Intent intent = new Intent(HouseListActivity.this, HouseConfirmActivity.class);
        startActivityForResult(intent, 1000);
        AnimUtil.intentSlidIn(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == 1001) {
            listView.setRefreshing(true);
        }
    }
}
