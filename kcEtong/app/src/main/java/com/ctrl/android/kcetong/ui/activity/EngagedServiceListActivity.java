package com.ctrl.android.kcetong.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import com.ctrl.android.kcetong.R;
import com.ctrl.android.kcetong.model.AppHolder;
import com.ctrl.android.kcetong.model.ServiceProduct;
import com.ctrl.android.kcetong.model.ServiceProductResult;
import com.ctrl.android.kcetong.toolkit.util.AopUtils;
import com.ctrl.android.kcetong.toolkit.util.BaseLinearLayoutManager;
import com.ctrl.android.kcetong.toolkit.util.ConstantsData;
import com.ctrl.android.kcetong.toolkit.util.LLog;
import com.ctrl.android.kcetong.toolkit.util.StrConstant;
import com.ctrl.android.kcetong.toolkit.util.Utils;
import com.ctrl.android.kcetong.toolkit.webutils.BaseTSubscriber;
import com.ctrl.android.kcetong.toolkit.webutils.RetrofitUtil;
import com.ctrl.android.kcetong.ui.adapter.EngagedServiceListAdapter;
import com.ctrl.android.kcetong.ui.base.BaseActivity;
import com.github.jdsjlzx.interfaces.OnLoadMoreListener;
import com.github.jdsjlzx.interfaces.OnRefreshListener;
import com.github.jdsjlzx.recyclerview.LRecyclerView;
import com.github.jdsjlzx.recyclerview.LRecyclerViewAdapter;
import com.github.jdsjlzx.recyclerview.ProgressStyle;
import com.github.jdsjlzx.util.RecyclerViewStateUtils;
import com.github.jdsjlzx.view.LoadingFooter;

import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class EngagedServiceListActivity extends BaseActivity {

    @BindView(R.id.recyclerView_engaged)
    LRecyclerView mLRecyclerView;

    private LRecyclerViewAdapter mLRecyclerViewAdapter;

    private EngagedServiceListAdapter adapter;
    private String TITLE = StrConstant.ENGAGED_SERVICE;
    private int    mPage = 1;
    private int parentRowCountPerPage = 0;

    private List<ServiceProduct> serviceProductList = new ArrayList<>();
    @Override
    protected void initView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_engaged_service_list);
        ButterKnife.bind(this);
        toolbarBaseSetting(TITLE, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EngagedServiceListActivity.this.finish();
            }
        });
    }
    @Override
    protected void initData() {

        adapter = new EngagedServiceListAdapter(EngagedServiceListActivity.this);
        mLRecyclerViewAdapter = new LRecyclerViewAdapter(adapter);
        mLRecyclerView.setLayoutManager(new BaseLinearLayoutManager(this));
        mLRecyclerView.setRefreshProgressStyle(ProgressStyle.BallSpinFadeLoader);
        mLRecyclerView.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh() {
                mPage = 1;
                requestServiceList(AppHolder.getInstance().getHouse().getCommunityId(),getIntent().getStringExtra("id"),mPage+"");
            }
        });
        mLRecyclerView.setRefreshing(true);
        mLRecyclerView.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                mPage++;
                LLog.d(mPage + "");
                if(parentRowCountPerPage == ConstantsData.ROW_COUNT_PER_PAGE){
                    RecyclerViewStateUtils.setFooterViewState(EngagedServiceListActivity.this, mLRecyclerView, 10, LoadingFooter.State.Loading, null);
                    requestServiceList(AppHolder.getInstance().getHouse().getCommunityId(),getIntent().getStringExtra("id"),mPage+"");
                }else {
                    RecyclerViewStateUtils.setFooterViewState(EngagedServiceListActivity.this, mLRecyclerView, 15, LoadingFooter.State.Normal, null);
                    Utils.toastError(EngagedServiceListActivity.this,"没有更多内容了");

                    mPage--;
                }
                mLRecyclerView.refreshComplete();
            }
        });
        adapter.setOnItemClickListener(new EngagedServiceListAdapter.OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(View view, Map<String,String> data) {
                Intent i = new Intent(EngagedServiceListActivity.this, EngagedServiceListDetailActivity.class);
                i.putExtra("id",data.get("id"));
                i.putExtra("serviceKindId", data.get("serviceKindId"));
                i.putExtra("data", (Serializable) data);
                startActivity(i);
            }
        });
        mLRecyclerView.setAdapter(mLRecyclerViewAdapter);
    }
    /**
     * 获取特约服务列表及详情
     * @param communityId 社区id
     * @param serviceKindId 当前选中的服务类别Id
     * @param currentPage 当前页码
     * */
    public void requestServiceList(String communityId,String serviceKindId,String currentPage){
        Map<String,String> map = new HashMap<>();
        map.put(ConstantsData.METHOD,"pm.ppt.serviceProduct.list");//方法名称
        map.putAll(ConstantsData.getSystemParams());

        map.put("communityId",communityId);
        map.put("serviceKindId",serviceKindId);
        map.put("currentPage",currentPage);
        map.put("rowCountPerPage",ConstantsData.ROW_COUNT_PER_PAGE+"");

        String sign = AopUtils.sign(map, ConstantsData.SECRET_VALUE);
        map.put("sign",sign);
        LLog.d(map+"");
        map.remove(ConstantsData.METHOD);
        RetrofitUtil.Api().requestServiceList(map).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseTSubscriber<ServiceProductResult>(this) {
                    @Override
                    public void onResponseCallback(JSONObject response, String resultCode) {
                        LLog.d(response+"");
                    }

                    @Override
                    public void onNext(ServiceProductResult serviceProductResult) {
                        super.onNext(serviceProductResult);
                        if(TextUtils.equals("000",serviceProductResult.getCode())){
                            if (mPage == 1) {//清除原有数据
                                adapter.clear();
                            }
                            parentRowCountPerPage = serviceProductResult.getData().getServiceProductList().size();
                            List<ServiceProduct> list_result = serviceProductResult.getData().getServiceProductList();
                            if(serviceProductResult.getData().getServiceProductList() != null && list_result.size()>0){
                                if(mPage > 1){//当前页大于1，将加载出来的数据添加到expressList后面
                                    serviceProductList.addAll(list_result);
                                }else if(mPage == 1){
                                    serviceProductList = list_result;
                                }
                                adapter.setList(serviceProductList);

                            }
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
                    }
                });
    }
}
