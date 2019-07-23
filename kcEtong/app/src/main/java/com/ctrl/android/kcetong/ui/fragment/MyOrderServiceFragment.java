package com.ctrl.android.kcetong.ui.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ctrl.android.kcetong.R;
import com.ctrl.android.kcetong.model.AppHolder;
import com.ctrl.android.kcetong.model.Repair;
import com.ctrl.android.kcetong.model.ServiceOrder;
import com.ctrl.android.kcetong.model.ServiceOrderListResult;
import com.ctrl.android.kcetong.toolkit.util.AopUtils;
import com.ctrl.android.kcetong.toolkit.util.BaseLinearLayoutManager;
import com.ctrl.android.kcetong.toolkit.util.ConstantsData;
import com.ctrl.android.kcetong.toolkit.util.LLog;
import com.ctrl.android.kcetong.toolkit.webutils.BaseTSubscriber;
import com.ctrl.android.kcetong.toolkit.webutils.RetrofitUtil;
import com.ctrl.android.kcetong.ui.activity.MyOrderServiceActivity;
import com.ctrl.android.kcetong.ui.activity.MyOrderServiceAftertreatmentActivity;
import com.ctrl.android.kcetong.ui.activity.MyOrderServicePretreatmentActivity;
import com.ctrl.android.kcetong.ui.adapter.MyOrderServiceAdapter;
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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.ButterKnife;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static android.app.Activity.RESULT_OK;

/**
 * Created by jason on 2015/10/26.
 */
public class MyOrderServiceFragment extends BaseFragment{
    private LRecyclerView mLRecyclerView;//可刷新的列表

    private LRecyclerViewAdapter mLRecyclerViewAdapter;

    private MyOrderServiceAdapter adapter;
    private String                progressState; //处理状态
    private List<Repair>          list_repair;
    private List<ServiceOrder> serviceOrderList;
    private int currentPage=1;
    private int currentTotalItem=1;
    private int rowCountPerPage=20;
    private int parentRowCountPerPage = 0;
    private Context context;

    private int dataListSize = 0;

    public static MyOrderServiceFragment newInstance(String state){
        MyOrderServiceFragment fragment=new MyOrderServiceFragment();
//        处理状态（0：待处理、1：处理中、2：已处理、3：已结束 4.全部）
        fragment.progressState=state;
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.fragment_my_repair, container, false);
        }
        context = this.getActivity();
        initView(rootView);
        Log.e("fragment-----","1");
        return rootView;

    }

    private void initView(View rootView){
        mLRecyclerView = (LRecyclerView) rootView.findViewById(R.id.recyclerView_reparis);

        adapter = new MyOrderServiceAdapter(progressState,context);
        mLRecyclerViewAdapter  = new LRecyclerViewAdapter(adapter);

        mLRecyclerView.setLayoutManager(new BaseLinearLayoutManager(context));
        mLRecyclerView.setRefreshProgressStyle(ProgressStyle.BallSpinFadeLoader);
        mLRecyclerView.setOnRefreshListener(new OnRefreshListener() {
            //0表示待评价 1 表示已评价
            @Override
            public void onRefresh() {
                currentPage = 1;
                if(AppHolder.getInstance().getProprietor().getProprietorId() == null || AppHolder.getInstance().getProprietor().getProprietorId().equals("")) {
                }else{
                    requestServiceOrderList(AppHolder.getInstance().getProprietor().getProprietorId(), progressState, String.valueOf(currentPage));
                }
            }
        });

        mLRecyclerView.setRefreshing(true);

        mLRecyclerView.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                currentPage++;
                LLog.d(currentPage+"");

                if(parentRowCountPerPage == ConstantsData.ROW_COUNT_PER_PAGE){
                    RecyclerViewStateUtils.setFooterViewState((Activity) context, mLRecyclerView, 10, LoadingFooter.State.Loading, null);
                    if(AppHolder.getInstance().getProprietor().getProprietorId() == null || AppHolder.getInstance().getProprietor().getProprietorId().equals("")) {

                    }else{
                        requestServiceOrderList(AppHolder.getInstance().getProprietor().getProprietorId(), progressState, String.valueOf(currentPage));
                    }
                }else {
                    RecyclerViewStateUtils.setFooterViewState((Activity) context, mLRecyclerView, 15, LoadingFooter.State.Normal, null);
                    currentPage--;
                }
            }
        });

        adapter.setOnItemClickListener(new MyOrderServiceAdapter.OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(View view, Map<String,String> data) {
//
                if ("0".equals(progressState)) {
                    Intent intent = new Intent(getActivity(), MyOrderServicePretreatmentActivity.class);
                    intent.putExtra("progressState", data.get("handleStatus"));
                    intent.putExtra("repairDemandId", data.get("id"));
                    startActivityForResult(intent,2000);
                }
                if ("1".equals(progressState)) {
                    Intent intent = new Intent(getActivity(), MyOrderServicePretreatmentActivity.class);
                    intent.putExtra("progressState", data.get("handleStatus"));
                    intent.putExtra("repairDemandId", data.get("id"));
                    startActivity(intent);
                }
                if ("2".equals(progressState)) {
                    Intent intent = new Intent(getActivity(), MyOrderServicePretreatmentActivity.class);
                    intent.putExtra("progressState", data.get("handleStatus"));
                    intent.putExtra("repairDemandId", data.get("id"));
                    startActivityForResult(intent, 2000);
                }
                if ("3".equals(progressState)) {
                    Intent intent = new Intent(getActivity(), MyOrderServiceAftertreatmentActivity.class);
                    intent.putExtra("repairDemandId", data.get("id"));
                    startActivityForResult(intent,2000);
                }
                if ("4".equals(progressState)){//全部
                    if("3".equals(data.get("handleStatus"))){
                        Intent intent = new Intent(getActivity(), MyOrderServiceAftertreatmentActivity.class);
                        intent.putExtra("repairDemandId", data.get("id"));
                        startActivityForResult(intent ,2000);
                    }
                    else {
                        Intent intent = new Intent(getActivity(), MyOrderServicePretreatmentActivity.class);
                        intent.putExtra("progressState", data.get("handleStatus"));
                        intent.putExtra("repairDemandId", data.get("id"));
                        startActivityForResult(intent, 2000);
                    }
                }
            }
        });
        mLRecyclerView.setAdapter(mLRecyclerViewAdapter);
    }

    /**
     * 获取我的预约服务列表
     * @param proprietorId 业主ID
     * @param handleStatus 处理状态（0：待处理、1：处理中、2：已处理、3：已结束）
     * @param current_Page 当前页码
     * */
    public void requestServiceOrderList(String proprietorId,String handleStatus,String current_Page){
        Map<String,String> map = new HashMap<>();
        map.put(ConstantsData.METHOD,"pm.ppt.serviceOrder.list");//方法名称
        map.putAll(ConstantsData.getSystemParams());

        map.put("proprietorId",proprietorId);
        map.put("handleStatus",handleStatus);
        map.put("currentPage",current_Page);
        map.put("rowCountPerPage",ConstantsData.ROW_COUNT_PER_PAGE+"");

        String sign = AopUtils.sign(map, ConstantsData.SECRET_VALUE);
        map.put("sign",sign);
        LLog.d(map+"");
        map.remove(ConstantsData.METHOD);
        RetrofitUtil.Api().requestServiceOrderList(map).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseTSubscriber<ServiceOrderListResult>((BaseActivity) this.getActivity()) {
                    @Override
                    public void onResponseCallback(JSONObject response, String resultCode) {
                        LLog.d(response+"");
                    }

                    @Override
                    public void onNext(ServiceOrderListResult serviceOrderListResult) {
                        super.onNext(serviceOrderListResult);
                        if(TextUtils.equals("000",serviceOrderListResult.getCode())){
                            List<ServiceOrder> list_result = serviceOrderListResult.getData().getServiceOrderList();
                            if (currentPage == 1){//清除原有数据
                                if("0".equals(progressState)){
                                    adapter.clearPending();
                                }else if("1".equals(progressState)){
                                    adapter.clearProgressing();
                                }else if("2".equals(progressState)){
                                    adapter.clearProgressed();
                                }else if("3".equals(progressState)){
                                    adapter.clearEnd();
                                }else if("4".equals(progressState)){
                                    adapter.clear();
                                }
                            }
                            dataListSize = serviceOrderListResult.getData().getServiceOrderList().size();
                            parentRowCountPerPage =dataListSize;
                            if (serviceOrderListResult.getData().getServiceOrderList() != null &&dataListSize >0){
                                if(currentPage >1){//如果是加载更多，将数据添加到list后面
                                    serviceOrderList.addAll(list_result);

                                }else if(currentPage == 1){
                                    serviceOrderList = list_result;
                                }
                                if("0".equals(progressState)){
                                    adapter.addPendingList(serviceOrderList);
                                }else if("1".equals(progressState)){
                                    adapter.addProgressingList(serviceOrderList);
                                }else if("2".equals(progressState)){
                                    adapter.addProgressedList(serviceOrderList);
                                }else if("3".equals(progressState)){
                                    adapter.addEndList(serviceOrderList);
                                }else if("4".equals(progressState)){
                                    adapter.setList(serviceOrderList);
                                }
                            }
                            mLRecyclerViewAdapter.notifyDataSetChanged();

                        }
                        mLRecyclerView.refreshComplete();
                    }

                    @Override
                    public void onNetError(Throwable e) {
                        super.onNetError(e);
                        if(currentPage > 1){
                            currentPage--;
                        }
                        mLRecyclerView.refreshComplete();
                    }
                });
    }
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);

    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case 2000:
                if(2001 == resultCode){
                    MyOrderServiceActivity myOrderServiceActivity = (MyOrderServiceActivity) getActivity();
                    myOrderServiceActivity.getAdapter().reLoad();
                }
                if(resultCode == RESULT_OK){
                    MyOrderServiceActivity myOrderServiceActivity = (MyOrderServiceActivity) getActivity();
                    myOrderServiceActivity.getAdapter().reLoad();
                }
                break;
        }
    }
}
