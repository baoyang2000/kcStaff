package com.ctrl.android.kcetong.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.ctrl.android.kcetong.R;
import com.ctrl.android.kcetong.model.AppHolder;
import com.ctrl.android.kcetong.model.ChoiceGroup;
import com.ctrl.android.kcetong.model.ProductListBean;
import com.ctrl.android.kcetong.toolkit.Url.Url;
import com.ctrl.android.kcetong.toolkit.util.AopUtils;
import com.ctrl.android.kcetong.toolkit.util.BaseLinearLayoutManager;
import com.ctrl.android.kcetong.toolkit.util.ConstantsData;
import com.ctrl.android.kcetong.toolkit.util.LLog;
import com.ctrl.android.kcetong.toolkit.util.StrConstant;
import com.ctrl.android.kcetong.toolkit.util.Utils;
import com.ctrl.android.kcetong.toolkit.webutils.BaseTSubscriber;
import com.ctrl.android.kcetong.toolkit.webutils.RetrofitUtil;
import com.ctrl.android.kcetong.ui.activity.ChoiceDetailActivity;
import com.ctrl.android.kcetong.ui.activity.MyOrderActivity;
import com.ctrl.android.kcetong.ui.activity.OrderAddressActivity;
import com.ctrl.android.kcetong.ui.adapter.ChoiceAdapter;
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
import butterknife.OnClick;
import butterknife.Unbinder;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * 团购
 */
public class ZhidongFragment extends BaseFragment {
    @BindView(R.id.rl_receive_address)
    RelativeLayout rlReceiveAddress;
    @BindView(R.id.rl_order)
    RelativeLayout rlOrder;
    @BindView(R.id.recyclerView)
    LRecyclerView recyclerView;
    Unbinder unbinder;

    private LRecyclerViewAdapter mLRecyclerViewAdapter;
    private ChoiceAdapter adapter;
    private int currentPage = 1;
    private int rowCountPerPage = 10;
    private int parentRowCountPerPage = 0;
    private List<ChoiceGroup> list = new ArrayList<>();
    private boolean isPrepared;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.fragment_zhidong, container, false);
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
        adapter = new ChoiceAdapter(getActivity());
        adapter.setDataList(list);
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
                Intent i = new Intent();
                i.setClass(context, ChoiceDetailActivity.class);
                i.putExtra("id", list.get(in).getId());
                i.putExtra("sellState", list.get(in).getSellState());
                i.putExtra("overTime", list.get(in).getOverTime());
                startActivity(i);
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
        map.put(ConstantsData.METHOD, Url.productListUrl);
        map.put("memberId", AppHolder.getInstance().getMemberInfo().getMemberId());
        map.put("currentPage", currentPage + "");
        map.put("rowCountPerPage", rowCountPerPage + "");
        String sign = AopUtils.sign(map, ConstantsData.SECRET_VALUE);
        map.put("sign", sign);
        map.remove(ConstantsData.METHOD);
        LLog.d(map + "");
        RetrofitUtil.Api().productList(map).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new BaseTSubscriber<ProductListBean>((BaseActivity) getActivity()) {
            @Override
            public void onResponseCallback(JSONObject response, String resultCode) {
                LLog.d("onResponseCallback: " + response);
            }

            @Override
            public void onNext(ProductListBean bean) {
                super.onNext(bean);
                if (TextUtils.equals("000", bean.getCode())) {
                    if (currentPage == 1) {
                        list.clear();
                    }
                    list.addAll(bean.getData().getProductsList());
                    adapter.setDataList(list);
                } else if (TextUtils.equals("002", bean.getCode())) {
                    Utils.toastError(context, StrConstant.NODATA);
                }
                if (bean.getData().getProductsList() == null) {
                    parentRowCountPerPage = 0;
                } else {
                    parentRowCountPerPage = bean.getData().getProductsList().size();
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
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick({R.id.rl_receive_address, R.id.rl_order})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.rl_receive_address:
                Intent intent = new Intent(context, OrderAddressActivity.class);
                context.startActivity(intent);
                break;
            case R.id.rl_order:
                Intent intent1 = new Intent(context, MyOrderActivity.class);
                context.startActivity(intent1);
                break;
        }
    }
}
