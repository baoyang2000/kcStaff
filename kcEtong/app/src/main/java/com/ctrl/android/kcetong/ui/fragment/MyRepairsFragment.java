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
import com.ctrl.android.kcetong.model.Repair;
import com.ctrl.android.kcetong.model.RepairListBean;
import com.ctrl.android.kcetong.toolkit.Url.Url;
import com.ctrl.android.kcetong.toolkit.util.AopUtils;
import com.ctrl.android.kcetong.toolkit.util.BaseLinearLayoutManager;
import com.ctrl.android.kcetong.toolkit.util.ConstantsData;
import com.ctrl.android.kcetong.toolkit.util.LLog;
import com.ctrl.android.kcetong.toolkit.util.StrConstant;
import com.ctrl.android.kcetong.toolkit.util.Utils;
import com.ctrl.android.kcetong.toolkit.webutils.BaseTSubscriber;
import com.ctrl.android.kcetong.toolkit.webutils.RetrofitUtil;
import com.ctrl.android.kcetong.ui.activity.MyRepairsAftertreatmentActivity;
import com.ctrl.android.kcetong.ui.activity.MyRepairsPretreatmentActivity;
import com.ctrl.android.kcetong.ui.adapter.MyRepairsAdapter;
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
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by cxl on 2017/1/22.
 */

public class MyRepairsFragment extends BaseFragment {

    @BindView(R.id.pull_to_refresh_listView_repairs)
    public         LRecyclerView        pull_to_refresh_listView_repairs;//可刷新的列表
    private static MyRepairsFragment    fragment;
    private        LRecyclerViewAdapter mLRecyclerViewAdapter;
    private        boolean              isPrepared;
    private        String               progressState;
    private int          currentPage           = 1;
    private int          rowCountPerPage       = 10;
    private int          parentRowCountPerPage = 0;
    private List<Repair> list                  = new ArrayList<>();
    private MyRepairsAdapter adapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        progressState = bundle.getString("state");
    }

    @Override
    protected void lazyLoad() {
        if (!isPrepared || !isVisible) {
            return;
        }
        LLog.w("<lazyLoad()>");
        LLog.w(getActivity() + "");
        LLog.w("progressState =" + progressState);
        adapter = new MyRepairsAdapter(getActivity());
        adapter.setDataList(list);
        mLRecyclerViewAdapter = new LRecyclerViewAdapter(adapter);
        pull_to_refresh_listView_repairs.setLayoutManager(new BaseLinearLayoutManager(getActivity()));
//        final DividerDecoration divider = new DividerDecoration.Builder(getActivity()).setHeight(R.dimen.between_element_margin).setColorResource(R.color.main_bg).build();
//        pull_to_refresh_listView_repairs.addItemDecoration(divider);
        pull_to_refresh_listView_repairs.setRefreshProgressStyle(ProgressStyle.BallSpinFadeLoader);
        pull_to_refresh_listView_repairs.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh() {
                currentPage = 1;
                list.clear();
                adapter.setDataList(list);
                adapter.notifyDataSetChanged();
                mLRecyclerViewAdapter.notifyDataSetChanged();
                getComlpaintData();
            }
        });
        pull_to_refresh_listView_repairs.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore() {

                currentPage++;
                LLog.w("parentRowCountPerPage2 =" + parentRowCountPerPage);
                if (parentRowCountPerPage == 10) {
                    RecyclerViewStateUtils.setFooterViewState(getActivity(), pull_to_refresh_listView_repairs, 10, LoadingFooter.State.Loading, null);
                    getComlpaintData();

                } else {
                    RecyclerViewStateUtils.setFooterViewState(getActivity(), pull_to_refresh_listView_repairs, 10, LoadingFooter.State.Normal, null);
                }
            }
        });
        pull_to_refresh_listView_repairs.setAdapter(mLRecyclerViewAdapter);
        LLog.w("<init()>");
        pull_to_refresh_listView_repairs.setRefreshing(true);
        mLRecyclerViewAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int i) {
                Bundle bundle = new Bundle();
                if (TextUtils.equals("3", list.get(i).getHandleStatus())) {
                    bundle.putString("complaintId", list.get(i).getId());
                    Utils.startActivity(bundle, getActivity(), MyRepairsAftertreatmentActivity.class);
                } else {
                    bundle.putString("complaintId", list.get(i).getId());
                    bundle.putString("progressState", list.get(i).getHandleStatus());
                    bundle.putInt("position", i);
                    Intent intent = new Intent(getActivity(), MyRepairsPretreatmentActivity.class);
                    intent.putExtras(bundle);
                    startActivityForResult(intent, 2500);
                }
            }

            @Override
            public void onItemLongClick(View view, int i) {

            }
        });
    }

    public static MyRepairsFragment newInstance(String state) {
        fragment = new MyRepairsFragment();
        Bundle bundle = new Bundle();
        bundle.putString("state", state);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_my_repairs, null);
        ButterKnife.bind(this, view);
        isPrepared = true;
        lazyLoad();
        return view;
    }

    /**
     * 获取报修列表数据
     */
    private void getComlpaintData() {

        isPrepared = false;
        String              proprietorId = AppHolder.getInstance().getProprietor().getProprietorId();
        Map<String, String> map          = new HashMap();
        map.putAll(ConstantsData.getSystemParams());
        map.put(ConstantsData.METHOD, Url.repairList);
        map.put("proprietorId", proprietorId);
        map.put("handleStatus", progressState);
        map.put("currentPage", currentPage + "");
        LLog.w("currentPage =" + currentPage);
        map.put("rowCountPerPage", rowCountPerPage + "");
        String sign = AopUtils.sign(map, ConstantsData.SECRET_VALUE);
        map.put("sign", sign);
        map.remove(ConstantsData.METHOD);

        RetrofitUtil.Api().repairList(map).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new BaseTSubscriber<RepairListBean>((BaseActivity) getActivity()) {
            @Override
            public void onResponseCallback(JSONObject response, String resultCode) {
                LLog.d("onResponseCallback: " + response);
            }

            @Override
            public void onNext(RepairListBean repairListBean) {
                super.onNext(repairListBean);
                if (TextUtils.equals("000", repairListBean.getCode())) {
                    if (currentPage == 1) {
                        list.clear();
                    }
                    list.addAll(repairListBean.getData().getRepairList());
                    LLog.w("notifyDataSetChanged");
                    adapter.setDataList(list);
                    adapter.notifyDataSetChanged();
                    mLRecyclerViewAdapter.notifyDataSetChanged();
                }else if(TextUtils.equals("002", repairListBean.getCode())){
                   Utils.toastError(context, StrConstant.NODATA);
                }
                if (repairListBean.getData().getRepairList() == null) {
                    parentRowCountPerPage = 0;
                } else {
                    parentRowCountPerPage = repairListBean.getData().getRepairList().size();
                }
                LLog.w("parentRowCountPerPage1 =" + parentRowCountPerPage);
                adapter.notifyDataSetChanged();
                mLRecyclerViewAdapter.notifyDataSetChanged();
                pull_to_refresh_listView_repairs.refreshComplete();
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
                RecyclerViewStateUtils.setFooterViewState(getActivity(), pull_to_refresh_listView_repairs, 10, LoadingFooter.State.Normal, null);
                pull_to_refresh_listView_repairs.refreshComplete();
            }

        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == 2501 && requestCode == 2500) {
            int position = data.getIntExtra("position", -1);
            LLog.w("position =" + position);
            if (position != -1) {
                list.remove(position);
                LLog.w("list.size() = " + list.size());
                adapter.setDataList(list);
                adapter.notifyDataSetChanged();
                mLRecyclerViewAdapter.notifyDataSetChanged();
            }
        }
    }
}
