package com.ctrl.android.kcetong.ui.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ctrl.android.kcetong.R;
import com.ctrl.android.kcetong.model.Act;
import com.ctrl.android.kcetong.model.ActResult;
import com.ctrl.android.kcetong.model.AppHolder;
import com.ctrl.android.kcetong.toolkit.Url.Url;
import com.ctrl.android.kcetong.toolkit.util.AopUtils;
import com.ctrl.android.kcetong.toolkit.util.BaseLinearLayoutManager;
import com.ctrl.android.kcetong.toolkit.util.ConstantsData;
import com.ctrl.android.kcetong.toolkit.util.LLog;
import com.ctrl.android.kcetong.toolkit.util.StrConstant;
import com.ctrl.android.kcetong.toolkit.util.Utils;
import com.ctrl.android.kcetong.toolkit.webutils.BaseTSubscriber;
import com.ctrl.android.kcetong.toolkit.webutils.RetrofitUtil;
import com.ctrl.android.kcetong.ui.activity.ActDetailActivity;
import com.ctrl.android.kcetong.ui.adapter.ActivityListAdapter;
import com.ctrl.android.kcetong.ui.base.BaseActivity;
import com.ctrl.android.kcetong.ui.base.BaseFragment;
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

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by liu on 2017/1/10.
 * 活动列表
 */

public class ActListFragment extends BaseFragment {

    private String               actListType;//活动列表的类型

    private LRecyclerView        mLRecyclerView;
    private LRecyclerViewAdapter mLRecyclerViewAdapter;
    private Context              context;
    private List<Act> actList = new ArrayList<>();
    private int currentPage = 1;
    private int parentRowCountPerPage = 0;
    private String obtainType = "";//活动类型(0:社区活动,1:我参与的,2:我发起的)
    private int dataListSize ;
    private boolean isRefresh = false;
    /**
     * 活动适配器
     */
    private ActivityListAdapter adapter;

    public static ActListFragment newInstance(String actListType){
        ActListFragment fragment = new ActListFragment();
        fragment.actListType = actListType;
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater,  ViewGroup container, Bundle savedInstanceState) {
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.fragment_activity_list, container, false);
        }
        context = this.getActivity();
        initView(rootView);
        return rootView;
    }
    private void initView(View rootView){
        mLRecyclerView = (LRecyclerView) rootView.findViewById(R.id.recyclerView_activity);
        adapter = new ActivityListAdapter(actListType,context);

        mLRecyclerViewAdapter  = new LRecyclerViewAdapter(adapter);

        mLRecyclerView.setLayoutManager(new BaseLinearLayoutManager(context));
        mLRecyclerView.setRefreshProgressStyle(ProgressStyle.BallSpinFadeLoader);
        mLRecyclerView.setOnRefreshListener(new OnRefreshListener() {
            //0表示待评价 1 表示已评价
            @Override
            public void onRefresh() {
                currentPage = 1;
                if (ConstantsData.ACT_ALL.equals(actListType)){//全部活动
                    obtainType = "0";
                } else if (ConstantsData.ACT_I_TAKE_IN.equals(actListType)){//我参与的
                    obtainType = "1";
                }else if(ConstantsData.ACT_I_START_UP.equals(actListType)){//我发起的
                    obtainType = "2";
                }
                initData(obtainType);
            }
        });

        mLRecyclerView.setRefreshing(true);

        mLRecyclerView.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                currentPage++;
                LLog.d(currentPage+"");
                if (ConstantsData.ACT_ALL.equals(actListType)){//全部活动
                    obtainType = "0";
                } else if (ConstantsData.ACT_I_TAKE_IN.equals(actListType)){//我参与的
                    obtainType = "1";
                }else if(ConstantsData.ACT_I_START_UP.equals(actListType)){//我发起的
                    obtainType = "2";
                }
                if(parentRowCountPerPage == ConstantsData.PAGE_CAPACITY){
                    RecyclerViewStateUtils.setFooterViewState((Activity) context, mLRecyclerView, 10, LoadingFooter.State.Loading, null);
                    initData(obtainType);
                }else {
                    RecyclerViewStateUtils.setFooterViewState((Activity) context, mLRecyclerView, 15, LoadingFooter.State.Normal, null);
                    currentPage--;
                }
//                initData(obtainType);
            }
        });

        adapter.setOnItemClickListener(new ActivityListAdapter.OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(View view, String data) {
                Intent intent = new Intent(getActivity(), ActDetailActivity.class);
                intent.putExtra("actionId",data);
                startActivity(intent);
            }
        });
        mLRecyclerView.setAdapter(mLRecyclerViewAdapter);
    }
    public void initData(final String type){
        String communityId = AppHolder.getInstance().getHouse().getCommunityId();
        String memberId = AppHolder.getInstance().getMemberInfo().getMemberId();
        String proprietorId = AppHolder.getInstance().getProprietor().getProprietorId();
        Map<String ,String> map = new HashMap<>();
        map.put(ConstantsData.METHOD, Url.queryActionList);
        map.put("communityId", communityId);
        map.put("memberId",memberId);
        if("0".equals(AppHolder.getInstance().getMemberInfo().getSupers())){
            map.put("proprietorId",proprietorId);
        }

        map.put("obtainType",type);
        map.put("currentPage",String.valueOf(currentPage));
        map.put("rowCountPerPage",String.valueOf(ConstantsData.ROW_COUNT_PER_PAGE));
//        map.put("rowCountPerPage","1");

        map.putAll(ConstantsData.getSystemParams());
        String sign = AopUtils.sign(map,ConstantsData.SECRET_VALUE);
        map.put("sign",sign);
        LLog.d(map+"");
        map.remove(ConstantsData.METHOD);
        RetrofitUtil.Api().getActList(map).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new BaseTSubscriber<ActResult>((BaseActivity)this.getActivity()) {
            @Override
            public void onResponseCallback(JSONObject response, String resultCode) {
                LLog.d(response+"");
            }
            @Override
            public void onNext(ActResult actResult) {
                super.onNext(actResult);
                if(TextUtils.equals("000",actResult.getCode())){
                    List<Act> list_result = actResult.getData().getActionList();
                    if (currentPage == 1){//清除原有数据
                        if("0".equals(type)){
                            adapter.clearActAll();
                        }else if("1".equals(type)){
                            adapter.clearActTakeIn();
                        }else if("2".equals(type)){
                            adapter.clearActLaunch();
                        }
                    }
                    dataListSize = actResult.getData().getActionList().size();
                    parentRowCountPerPage =dataListSize;
                    if (actResult.getData().getActionList() != null &&dataListSize >0){
                        if(currentPage >1){//如果是加载更多，将数据添加到list后面
                            actList.addAll(list_result);

                        }else if(currentPage == 1){
                            actList = list_result;
                        }
                        if("0".equals(type)){
                            adapter.addActAllList(actList);
                        }else if("1".equals(type)){
                            adapter.addTakeInList(actList);
                            adapter.notifyDataSetChanged();
                        }else if("2".equals(type)){
                            adapter.addLaunchList(actList);
                        }
                    }
                    mLRecyclerViewAdapter.notifyDataSetChanged();

                }else if(TextUtils.equals("002", actResult.getCode())){
                    Utils.toastError(context, StrConstant.NODATA);
                }
                mLRecyclerView.refreshComplete();
            }
            @Override
            public void onNetError(Throwable e) {
                super.onNetError(e);
                if (currentPage > 1) {
                    currentPage--;
                }
                mLRecyclerView.refreshComplete();
            }
        });
    }
    @Override
    public void onPause() {
        super.onPause();
        isRefresh = true;
    }
    @Override
    public void onResume() {
        super.onResume();
        if (Utils.getOrderListRefreshState(context)) {
            if (ConstantsData.ACT_ALL.equals(actListType)){//全部活动
                obtainType = "0";
            } else if (ConstantsData.ACT_I_TAKE_IN.equals(actListType)){//我参与的
                obtainType = "1";
            }else if(ConstantsData.ACT_I_START_UP.equals(actListType)){//我发起的
                obtainType = "2";
            }
            initData(obtainType);
            isRefresh = false;
            Utils.saveOrderListRefreshState(context, false);
        }
    }
}
