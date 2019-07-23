package com.ctrl.android.kcetong.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ctrl.android.kcetong.R;
import com.ctrl.android.kcetong.model.AppHolder;
import com.ctrl.android.kcetong.model.OrderBean;
import com.ctrl.android.kcetong.toolkit.Url.Url;
import com.ctrl.android.kcetong.toolkit.util.AopUtils;
import com.ctrl.android.kcetong.toolkit.util.BaseLinearLayoutManager;
import com.ctrl.android.kcetong.toolkit.util.ConstantsData;
import com.ctrl.android.kcetong.toolkit.util.LLog;
import com.ctrl.android.kcetong.toolkit.util.StrConstant;
import com.ctrl.android.kcetong.toolkit.util.Utils;
import com.ctrl.android.kcetong.toolkit.webutils.BaseTSubscriber;
import com.ctrl.android.kcetong.toolkit.webutils.RetrofitUtil;
import com.ctrl.android.kcetong.ui.activity.MyOrderDetail;
import com.ctrl.android.kcetong.ui.activity.OrderEvaluateRelease;
import com.ctrl.android.kcetong.ui.adapter.MyOrderAdapter;
import com.ctrl.android.kcetong.ui.base.BaseActivity;
import com.ctrl.android.kcetong.ui.base.BaseFragment;
import com.github.jdsjlzx.interfaces.OnItemClickListener;
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
import butterknife.Unbinder;
import okhttp3.ResponseBody;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by liu on 2018/3/7.
 */

public class MyOrderFragment extends BaseFragment implements MyOrderAdapter.OnClickButtonListener{
    @BindView(R.id.recyclerView)
    LRecyclerView recyclerView;
    private String         mType;
    private MyOrderAdapter adapter;
    private LRecyclerViewAdapter mLRecyclerViewAdapter;
    private int                                    currentPage           = 1;
    private int                                    rowCountPerPage       = 10;
    private int                                    parentRowCountPerPage = 0;
    private List<OrderBean.DataBean.OrderListBean> list                  = new ArrayList<>();
    private        boolean              isPrepared;
    Unbinder unbinder;

    public static MyOrderFragment newInstance(String type) {
        MyOrderFragment fragment = new MyOrderFragment();
        fragment.mType = type;
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.fragment_my_order, container, false);
        }
        unbinder = ButterKnife.bind(this, rootView);
        isPrepared = true;
        lazyLoad();
        return rootView;
    }
    @Override
    protected void lazyLoad() {
        if (!isPrepared || !isVisible) {
            return;
        }
        adapter = new MyOrderAdapter(getActivity(), mType);
        adapter.setDataList(list);
        adapter.setListener(this);
        mLRecyclerViewAdapter = new LRecyclerViewAdapter(adapter);
        recyclerView.setLayoutManager(new BaseLinearLayoutManager(getActivity()));
        recyclerView.setRefreshProgressStyle(ProgressStyle.BallSpinFadeLoader);
        recyclerView.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh() {
                currentPage = 1;
                list.clear();
                getData();
            }
        });
        recyclerView.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore() {

                currentPage++;
                LLog.w("parentRowCountPerPage2 =" + parentRowCountPerPage);
                if (parentRowCountPerPage == 10) {
                    RecyclerViewStateUtils.setFooterViewState(getActivity(), recyclerView, 10, LoadingFooter.State.Loading, null);
                    getData();

                } else {
                    RecyclerViewStateUtils.setFooterViewState(getActivity(), recyclerView, 10, LoadingFooter.State.Normal, null);
                }
            }
        });
        recyclerView.setAdapter(mLRecyclerViewAdapter);
        LLog.w("<init()>");
        recyclerView.setRefreshing(true);
        mLRecyclerViewAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int in) {
                Intent intent = new Intent();
                intent.setClass(context ,MyOrderDetail.class);
                intent.putExtra("type", mType);
                intent.putExtra("order",list.get(in));
                startActivity(intent);
            }

            @Override
            public void onItemLongClick(View view, int i) {

            }
        });
    }

    private void getData() {
        isPrepared = false;
        Map<String, String> map = new HashMap();
        map.putAll(ConstantsData.getSystemParams());
        map.put(ConstantsData.METHOD, Url.orderListUrl);
        map.put("memberId", AppHolder.getInstance().getMemberInfo().getMemberId());
        map.put("obtainType", mType);
        map.put("currentPage", currentPage + "");
        map.put("rowCountPerPage", rowCountPerPage + "");
        String sign = AopUtils.sign(map, ConstantsData.SECRET_VALUE);
        map.put("sign", sign);
        map.remove(ConstantsData.METHOD);
        LLog.d(map +"");
        RetrofitUtil.Api().orderList(map).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new BaseTSubscriber<OrderBean>((BaseActivity) getActivity()) {
            @Override
            public void onResponseCallback(JSONObject response, String resultCode) {
                LLog.d("onResponseCallback: " + response);
            }

            @Override
            public void onNext(OrderBean bean) {
                super.onNext(bean);
                if (TextUtils.equals("000", bean.getCode())) {
                    if (currentPage == 1) {
                        list.clear();
                    }
                    list.addAll(bean.getData().getOrderList());
                    adapter.setDataList(list);
                }else if(TextUtils.equals("002", bean.getCode())){
                    Utils.toastError(context, StrConstant.NODATA);
                }
                if (bean.getData().getOrderList() == null) {
                    parentRowCountPerPage = 0;
                } else {
                    parentRowCountPerPage = bean.getData().getOrderList().size();
                }
                LLog.w("parentRowCountPerPage1 =" + parentRowCountPerPage);
                adapter.notifyDataSetChanged();
                mLRecyclerViewAdapter.notifyDataSetChanged();
                recyclerView.refreshComplete();
            }

            @Override
            public void onNetError(Throwable e) {

                if (e.getMessage() == null) {

                } else if (e.getMessage().contains("Failed to connect") || e.getMessage().contains("failed to connect") || e.getMessage().contains("SocketTimeoutException")) {
                    Utils.toastError(getActivity(), "服务器连接超时,请检查网络");
                } else if ("Invalid index 0, size is 0".equals(e.getMessage())) {//连接超时
                }

                if (currentPage > 0) {
                    currentPage--;
                }
                RecyclerViewStateUtils.setFooterViewState(getActivity(), recyclerView, 10, LoadingFooter.State.Normal, null);
                recyclerView.refreshComplete();
            }

        });
    }

    @Override
    public void click(int position, String orderId, OrderBean.DataBean.OrderListBean order) {
        if("1".equals(mType)){
            orderReceive(orderId);
        }else if("2".equals(mType)){
            Intent i = new Intent();
            i.setClass(context, OrderEvaluateRelease.class);
            i.putExtra("order",order);
            startActivity(i);
        }else if("3".equals(mType)){
            orderDelete(orderId);
        }
    }

    private void orderDelete(String orderId) {
        showProgress(true);
        Map<String, String> map = new HashMap();
        map.putAll(ConstantsData.getSystemParams());
        map.put(ConstantsData.METHOD, Url.orderDeleteUrl);
        map.put("orderId", orderId);
        String sign = AopUtils.sign(map, ConstantsData.SECRET_VALUE);
        map.put("sign", sign);
        map.remove(ConstantsData.METHOD);
        LLog.d(map + "");
        RetrofitUtil.Api().orderDelete(map).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new BaseTSubscriber<ResponseBody>((BaseActivity) context) {
            @Override
            public void onResponseCallback(JSONObject response, String resultCode) {
                showProgress(false);
                if (TextUtils.equals(resultCode, ConstantsData.success)) {
                    Utils.toastError(context, "删除订单成功");
                    recyclerView.setRefreshing(true);
                }
            }

            @Override
            public void onNetError(Throwable e) {
            }

        });
    }

    private void orderReceive(String orderId) {
        showProgress(true);
        Map<String, String> map = new HashMap();
        map.putAll(ConstantsData.getSystemParams());
        map.put(ConstantsData.METHOD, Url.orderReceiveUrl);
        map.put("orderId", orderId);
        String sign = AopUtils.sign(map, ConstantsData.SECRET_VALUE);
        map.put("sign", sign);
        map.remove(ConstantsData.METHOD);
        LLog.d(map + "");
        RetrofitUtil.Api().orderReceive(map).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new BaseTSubscriber<ResponseBody>((BaseActivity) context) {
            @Override
            public void onResponseCallback(JSONObject response, String resultCode) {
                showProgress(false);
                if (TextUtils.equals(resultCode, ConstantsData.success)) {
                    Utils.toastError(context, "确认收货成功");
                    recyclerView.setRefreshing(true);
                }
            }

            @Override
            public void onNetError(Throwable e) {
            }
        });
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
