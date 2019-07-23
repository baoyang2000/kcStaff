package com.ctrl.android.kcetong.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.ctrl.android.kcetong.R;
import com.ctrl.android.kcetong.model.AppHolder;
import com.ctrl.android.kcetong.model.ServiceKind;
import com.ctrl.android.kcetong.model.ServiceListResult;
import com.ctrl.android.kcetong.toolkit.util.AopUtils;
import com.ctrl.android.kcetong.toolkit.util.ConstantsData;
import com.ctrl.android.kcetong.toolkit.util.LLog;
import com.ctrl.android.kcetong.toolkit.util.StrConstant;
import com.ctrl.android.kcetong.toolkit.util.Utils;
import com.ctrl.android.kcetong.toolkit.webutils.BaseTSubscriber;
import com.ctrl.android.kcetong.toolkit.webutils.RetrofitUtil;
import com.ctrl.android.kcetong.ui.adapter.EngagedServiceAdapter;
import com.ctrl.android.kcetong.ui.base.BaseActivity;
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
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * 特约服务
 */
public class EngagedServiceActivity extends BaseActivity {
    @BindView(R.id.toolbar_right_btn)
    TextView toolbar_right_btn;
    @BindView(R.id.recyclerView_activity)
    LRecyclerView mLRecyclerView;
    private LRecyclerViewAdapter mLRecyclerViewAdapter;
    private String TITLE = StrConstant.ENGAGED_SERVICE;

    private int mPage=1;
    private int parentRowCountPerPage = 0;
    private EngagedServiceAdapter adapter;

    private List<ServiceKind> serviceKindList = new ArrayList<>();

    @Override
    protected void initView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_engaged_service);
        ButterKnife.bind(this);
        toolbarBaseSetting(TITLE, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EngagedServiceActivity.this.finish();
            }
        });
        toolbar_right_btn.setVisibility(View.VISIBLE);
        toolbar_right_btn.setText(StrConstant.MY_VISIT);
        toolbar_right_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if("1".equals(AppHolder.getInstance().getMemberInfo().getSupers())
                   || "2".equals(AppHolder.getInstance().getMemberInfo().getSupers())){
                    Utils.showShortToast(EngagedServiceActivity.this,getString(R.string.manager_cannot));

                }else {
                    Intent in =new Intent(EngagedServiceActivity.this,MyOrderServiceActivity.class);
                    startActivity(in);
                }

            }
        });
    }

    @Override
    protected void initData() {
        adapter = new EngagedServiceAdapter(EngagedServiceActivity.this);
        mLRecyclerViewAdapter = new LRecyclerViewAdapter(adapter);
        mLRecyclerView.setLayoutManager(new GridLayoutManager(this,2));
        mLRecyclerView.setRefreshProgressStyle(ProgressStyle.BallSpinFadeLoader);
        mLRecyclerView.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh() {
                mPage = 1;
                requestServiceKindList(AppHolder.getInstance().getHouse().getCommunityId(), mPage + "");
            }
        });
        mLRecyclerView.setRefreshing(true);
        mLRecyclerView.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                mPage++;
                LLog.d(mPage+"");
                if(parentRowCountPerPage == ConstantsData.ROW_COUNT_PER_PAGE){
                    RecyclerViewStateUtils.setFooterViewState(EngagedServiceActivity.this, mLRecyclerView, 10, LoadingFooter.State.Normal, null);
                    requestServiceKindList(AppHolder.getInstance().getHouse().getCommunityId(), mPage + "");
                }else {
                    RecyclerViewStateUtils.setFooterViewState(EngagedServiceActivity.this, mLRecyclerView, 15, LoadingFooter.State.Normal, null);
                    Utils.toastError(EngagedServiceActivity.this,"没有更多内容了");

                    mPage--;
                }
                mLRecyclerView.refreshComplete();
            }
        });
        adapter.setOnItemClickListener(new EngagedServiceAdapter.OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(View view, String data) {
                Intent i = new Intent(EngagedServiceActivity.this, EngagedServiceListActivity.class);
                i.putExtra("id",data);
                startActivity(i);
            }
        });
        mLRecyclerView.setAdapter(mLRecyclerViewAdapter);
    }
    /**
     * 获取特约服务类别列表
     * @param communityId 社区id
     * @param currentPage 当前页码
     * */
    public void requestServiceKindList(String communityId, String currentPage){
        Map<String,String> map = new HashMap<String,String>();
        map.put(ConstantsData.METHOD,"pm.ppt.serviceKind.list");//方法名称
        map.putAll(ConstantsData.getSystemParams());

        map.put("communityId",communityId);
        map.put("currentPage",currentPage);
        map.put("rowCountPerPage",ConstantsData.ROW_COUNT_PER_PAGE+"");
        String sign = AopUtils.sign(map, ConstantsData.SECRET_VALUE);
        map.put("sign",sign);
        LLog.d(map+"");
        map.remove(ConstantsData.METHOD);

        RetrofitUtil.Api().requestServiceKindList(map).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseTSubscriber<ServiceListResult>(this) {
                    @Override
                    public void onResponseCallback(JSONObject response, String resultCode) {
                        LLog.d(response+"");
                    }

                    @Override
                    public void onNext(ServiceListResult serviceListResult) {
                        super.onNext(serviceListResult);
                        if(TextUtils.equals("000",serviceListResult.getCode())){
                            if (mPage == 1) {//清除原有数据
                                adapter.clear();
                            }
                            parentRowCountPerPage = serviceListResult.getData().getServiceKindList().size();
                            List<ServiceKind> list_result = serviceListResult.getData().getServiceKindList();
                            if(serviceListResult.getData().getServiceKindList() != null && list_result.size()>0){
                                if(mPage > 1){//当前页大于1，将加载出来的数据添加到expressList后面
                                    serviceKindList.addAll(list_result);
                                }else if(mPage == 1){
                                    serviceKindList = list_result;
                                }
                                adapter.setList(serviceKindList);

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
