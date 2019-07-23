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
import com.ctrl.android.kcetong.model.Complaint;
import com.ctrl.android.kcetong.toolkit.Url.Url;
import com.ctrl.android.kcetong.toolkit.util.AopUtils;
import com.ctrl.android.kcetong.toolkit.util.BaseLinearLayoutManager;
import com.ctrl.android.kcetong.toolkit.util.ConstantsData;
import com.ctrl.android.kcetong.toolkit.util.LLog;
import com.ctrl.android.kcetong.toolkit.util.StrConstant;
import com.ctrl.android.kcetong.toolkit.util.Utils;
import com.ctrl.android.kcetong.toolkit.webutils.BaseTSubscriber;
import com.ctrl.android.kcetong.toolkit.webutils.RetrofitUtil;
import com.ctrl.android.kcetong.ui.activity.MyComplaintAftertreatmentActivity;
import com.ctrl.android.kcetong.ui.activity.MyComplaintPretreatmentActivity;
import com.ctrl.android.kcetong.ui.adapter.MyComplaintAdapter;
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

import static com.ctrl.android.kcetong.R.id.pull_to_refresh_listView001;

/**
 * Created by cxl on 2017/1/12.
 */

public class MyComplaintFragment extends BaseFragment {

    @BindView(pull_to_refresh_listView001)
    public LRecyclerView pullToRefreshListView001;
    private View view = null;
    private String progressState;
    private int                                        currentPage           = 1;
    private int                                        rowCountPerPage       = 10;
    private List<Complaint.DataBean.ComplaintListBean> list                  = new ArrayList<>();
    private int                                        parentRowCountPerPage = 0;
    private        MyComplaintAdapter   adapter;
    private        LRecyclerViewAdapter mLRecyclerViewAdapter;
    private        boolean              isPrepared;
    private static MyComplaintFragment  fragment;

    public static MyComplaintFragment newInstance(String state) {
        fragment = new MyComplaintFragment();
        Bundle bundle = new Bundle();
        bundle.putString("state", state);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        progressState = bundle.getString("state");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_my_complaint, null);

        ButterKnife.bind(this, view);
        isPrepared = true;
        lazyLoad();
        return view;
    }

    @Override
    protected void lazyLoad() {

        if (!isPrepared || !isVisible) {
            return;
        }
        LLog.w("<lazyLoad()>");
        LLog.w(getActivity() + "");
        LLog.w("progressState =" + progressState);
        adapter = new MyComplaintAdapter(getActivity());
        adapter.setDataList(list);
        mLRecyclerViewAdapter = new LRecyclerViewAdapter(adapter);
        pullToRefreshListView001.setLayoutManager(new BaseLinearLayoutManager(getActivity()));
//        final DividerDecoration divider = new DividerDecoration.Builder(getActivity()).setHeight(R.dimen.between_element_margin).setColorResource(R.color.main_bg).build();
//        pullToRefreshListView001.addItemDecoration(divider);
        pullToRefreshListView001.setRefreshProgressStyle(ProgressStyle.BallSpinFadeLoader);
        pullToRefreshListView001.setOnRefreshListener(new OnRefreshListener() {
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
        pullToRefreshListView001.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore() {

                currentPage++;
                if (parentRowCountPerPage == 10) {
                    RecyclerViewStateUtils.setFooterViewState(getActivity(), pullToRefreshListView001, 10, LoadingFooter.State.Loading, null);
                    getComlpaintData();

                } else {
                    RecyclerViewStateUtils.setFooterViewState(getActivity(), pullToRefreshListView001, 10, LoadingFooter.State.Normal, null);
                }
            }
        });
        pullToRefreshListView001.setAdapter(mLRecyclerViewAdapter);
        LLog.w("<init()>");
        pullToRefreshListView001.setRefreshing(true);
        mLRecyclerViewAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int i) {
                Bundle bundle = new Bundle();
                if (TextUtils.equals("3", list.get(i).getHandleStatus())) {
                    bundle.putString("complaintId", list.get(i).getId());
                    Utils.startActivity(bundle, getActivity(), MyComplaintAftertreatmentActivity.class);
                } else {
                    bundle.putString("complaintId", list.get(i).getId());
                    bundle.putString("progressState", list.get(i).getHandleStatus());
                    bundle.putInt("position", i);
                    Intent intent = new Intent(getActivity(), MyComplaintPretreatmentActivity.class);
                    intent.putExtras(bundle);
                    startActivityForResult(intent, 2500);
                }
            }

            @Override
            public void onItemLongClick(View view, int i) {

            }
        });
    }

    /**
     * 获取投诉列表数据
     */
    private void getComlpaintData() {

        isPrepared = false;
        showProgress(true);
        String              proprietorId = AppHolder.getInstance().getProprietor().getProprietorId();
        Map<String, String> map          = new HashMap();
        map.putAll(ConstantsData.getSystemParams());
        map.put(ConstantsData.METHOD, Url.complaintListUrl);
        map.put("proprietorId", proprietorId);
        map.put("handleStatus", progressState);
        LLog.w(progressState);
        map.put("currentPage", currentPage + "");
        map.put("rowCountPerPage", rowCountPerPage + "");
        String sign = AopUtils.sign(map, ConstantsData.SECRET_VALUE);
        map.put("sign", sign);
        map.remove(ConstantsData.METHOD);

        RetrofitUtil.Api().ComplaintList(map).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new BaseTSubscriber<Complaint>((BaseActivity) getActivity()) {

            @Override
            public void onNext(Complaint complaint) {
                super.onNext(complaint);
                if (TextUtils.equals("000", complaint.getCode())) {
                    if (currentPage == 1) {
                        list.clear();
                    }
                    parentRowCountPerPage = complaint.getData().getComplaintList().size();
                    list.addAll(complaint.getData().getComplaintList());
                    LLog.w("notifyDataSetChanged");
                    adapter.setDataList(list);
                    adapter.notifyDataSetChanged();
                    mLRecyclerViewAdapter.notifyDataSetChanged();
                }else if(TextUtils.equals("002", complaint.getCode())){
                    Utils.toastError(context, StrConstant.NODATA);
                }
                LLog.w("11111111111111111111111111111111");
                adapter.notifyDataSetChanged();
                mLRecyclerViewAdapter.notifyDataSetChanged();
                pullToRefreshListView001.refreshComplete();
                showProgress(false);
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
                RecyclerViewStateUtils.setFooterViewState(getActivity(), pullToRefreshListView001, 10, LoadingFooter.State.Normal, null);
                showProgress(false);
                pullToRefreshListView001.refreshComplete();
            }

            @Override
            public void onResponseCallback(JSONObject response, String resultCode) {

            }

        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == 2501 && requestCode == 2500) {
            int position = data.getIntExtra("position", -1);
            if (position != -1) {
                list.remove(position);
                adapter.setDataList(list);
                adapter.notifyDataSetChanged();
                mLRecyclerViewAdapter.notifyDataSetChanged();
            }
        }
    }
}
