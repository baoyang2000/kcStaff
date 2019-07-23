package com.ctrl.android.kcetong.ui.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import com.ctrl.android.kcetong.R;
import com.ctrl.android.kcetong.model.CommentBean;
import com.ctrl.android.kcetong.toolkit.Url.Url;
import com.ctrl.android.kcetong.toolkit.util.AopUtils;
import com.ctrl.android.kcetong.toolkit.util.BaseLinearLayoutManager;
import com.ctrl.android.kcetong.toolkit.util.ConstantsData;
import com.ctrl.android.kcetong.toolkit.util.LLog;
import com.ctrl.android.kcetong.toolkit.util.StrConstant;
import com.ctrl.android.kcetong.toolkit.util.Utils;
import com.ctrl.android.kcetong.toolkit.webutils.BaseTSubscriber;
import com.ctrl.android.kcetong.toolkit.webutils.RetrofitUtil;
import com.ctrl.android.kcetong.ui.adapter.ChoiceCommentAdapter;
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

public class CommentActivity extends BaseActivity {

    @BindView(R.id.recyclerView)
    LRecyclerView recyclerView;
    private LRecyclerViewAdapter mLRecyclerViewAdapter;
    private ChoiceCommentAdapter adapter;
    private int                                           currentPage           = 1;
    private int                                           rowCountPerPage       = 10;
    private int                                           parentRowCountPerPage = 0;
    private List<CommentBean.DataBean.EvaluationListBean> list                  = new ArrayList<>();

    @Override
    protected void initView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_comment);
        ButterKnife.bind(this);
        toolbarBaseSetting("评论", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CommentActivity.this.finish();
            }
        });
    }

    @Override
    protected void initData() {
        adapter = new ChoiceCommentAdapter(CommentActivity.this);
        adapter.setDataList(list);
        mLRecyclerViewAdapter = new LRecyclerViewAdapter(adapter);
        recyclerView.setLayoutManager(new BaseLinearLayoutManager(CommentActivity.this));
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
                    RecyclerViewStateUtils.setFooterViewState(CommentActivity.this, recyclerView, 10, LoadingFooter.State.Loading, null);
                    getData();

                } else {
                    RecyclerViewStateUtils.setFooterViewState(CommentActivity.this, recyclerView, 10, LoadingFooter.State.Normal, null);
                }
            }
        });
        recyclerView.setAdapter(mLRecyclerViewAdapter);
        LLog.w("<init()>");
        recyclerView.setRefreshing(true);
    }

    private void getData() {
        Map<String, String> map = new HashMap();
        map.putAll(ConstantsData.getSystemParams());
        map.put(ConstantsData.METHOD, Url.commentListUrl);
//        map.put("memberId", AppHolder.getInstance().getMemberInfo().getMemberId());
        map.put("productId", getIntent().getStringExtra("productId"));
        map.put("currentPage", currentPage + "");
        map.put("rowCountPerPage", rowCountPerPage + "");
        String sign = AopUtils.sign(map, ConstantsData.SECRET_VALUE);
        map.put("sign", sign);
        map.remove(ConstantsData.METHOD);
        LLog.d(map +"");
        RetrofitUtil.Api().commentList(map).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new BaseTSubscriber<CommentBean>(this) {
            @Override
            public void onResponseCallback(JSONObject response, String resultCode) {
                LLog.d("onResponseCallback: " + response);
            }

            @Override
            public void onNext(CommentBean bean) {
                super.onNext(bean);
                if (TextUtils.equals("000", bean.getCode())) {
                    if (currentPage == 1) {
                        list.clear();
                    }
                    list.addAll(bean.getData().getEvaluationList());
                    adapter.setDataList(list);
                }else if(TextUtils.equals("002", bean.getCode())){
                    Utils.toastError(CommentActivity.this, StrConstant.NODATA);
                }
                if (bean.getData().getEvaluationList() == null) {
                    parentRowCountPerPage = 0;
                } else {
                    parentRowCountPerPage = bean.getData().getEvaluationList().size();
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
                    Utils.toastError(CommentActivity.this, "服务器连接超时,请检查网络");
                } else if ("Invalid index 0, size is 0".equals(e.getMessage())) {//连接超时
                }

                if (currentPage > 0) {
                    currentPage--;
                }
                RecyclerViewStateUtils.setFooterViewState(CommentActivity.this, recyclerView, 10, LoadingFooter.State.Normal, null);
                recyclerView.refreshComplete();
            }

        });
    }
}
