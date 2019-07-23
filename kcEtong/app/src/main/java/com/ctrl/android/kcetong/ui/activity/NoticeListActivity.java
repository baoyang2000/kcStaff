package com.ctrl.android.kcetong.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;

import com.ctrl.android.kcetong.R;
import com.ctrl.android.kcetong.model.AppHolder;
import com.ctrl.android.kcetong.model.Notice;
import com.ctrl.android.kcetong.toolkit.Url.Url;
import com.ctrl.android.kcetong.toolkit.util.AopUtils;
import com.ctrl.android.kcetong.toolkit.util.BaseLinearLayoutManager;
import com.ctrl.android.kcetong.toolkit.util.ConstantsData;
import com.ctrl.android.kcetong.toolkit.util.LLog;
import com.ctrl.android.kcetong.toolkit.util.StrConstant;
import com.ctrl.android.kcetong.toolkit.util.Utils;
import com.ctrl.android.kcetong.toolkit.webutils.BaseTSubscriber;
import com.ctrl.android.kcetong.toolkit.webutils.RetrofitUtil;
import com.ctrl.android.kcetong.ui.adapter.NoticeListAdapter;
import com.ctrl.android.kcetong.ui.base.BaseActivity;
import com.ctrl.android.kcetong.ui.view.DividerDecoration;
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

public class NoticeListActivity extends BaseActivity {
    @BindView(R.id.recyclerView_notice_data)
    LRecyclerView recyclerViewNoticeData;
    @BindView(R.id.activity_notice_list)
    LinearLayout  activityNoticeList;
    private int currentPage           = 1;
    private int parentRowCountPerPage = 0;
    private NoticeListAdapter                            adapter;
    private LRecyclerViewAdapter                         mLRecyclerViewAdapter;
    private List<Notice.DataBean.PropertyNoticeListBean> list;
    private String communityId = AppHolder.getInstance().getHouse().getCommunityId();
    private String activityId  ;

    @Override
    protected void initView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_notice_list);
        ButterKnife.bind(this);
    }

    @Override
    protected void initData() {
        toolbarBaseSetting("温馨提示", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utils.hideKeyBoard(NoticeListActivity.this);
                NoticeListActivity.this.finish();
            }
        });

        if("1".equals(AppHolder.getInstance().getMemberInfo().getSupers())
           || "2".equals(AppHolder.getInstance().getMemberInfo().getSupers())){
            activityId = "";
        }else if("0".equals(AppHolder.getInstance().getMemberInfo().getSupers())){
            activityId = AppHolder.getInstance().getProprietor().getProprietorId();
        }
        list = new ArrayList<>();
        adapter = new NoticeListAdapter(NoticeListActivity.this);
        adapter.setListData(list);
        mLRecyclerViewAdapter = new LRecyclerViewAdapter(adapter);
        recyclerViewNoticeData.setLayoutManager(new BaseLinearLayoutManager(this));
        //设置分割线
        final DividerDecoration divider = new DividerDecoration.Builder(this)
                .setHeight(R.dimen.default_divider_height)
                .setColorResource(R.color.main_bg)
                .build();
        recyclerViewNoticeData.addItemDecoration(divider);
        recyclerViewNoticeData.setRefreshProgressStyle(ProgressStyle.BallSpinFadeLoader);
        recyclerViewNoticeData.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh() {
                //下拉刷新
                currentPage = 1;
                requestNoticeList(communityId, activityId);
            }
        });
        recyclerViewNoticeData.setRefreshing(true);
        recyclerViewNoticeData.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                currentPage++;
                if (parentRowCountPerPage == 10) {
                    RecyclerViewStateUtils.setFooterViewState(NoticeListActivity.this, recyclerViewNoticeData, 5, LoadingFooter.State.Loading, null);
                    requestNoticeList(communityId, activityId);

                } else {
                    RecyclerViewStateUtils.setFooterViewState(NoticeListActivity.this, recyclerViewNoticeData, 10, LoadingFooter.State.Normal, null);
                }
            }
        });
        recyclerViewNoticeData.setAdapter(mLRecyclerViewAdapter);
        //Lrecyclerview的item点击
        mLRecyclerViewAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Intent intent = new Intent(NoticeListActivity.this, NoticeDetailActivity.class);
                intent.putExtra("title",list.get(position).getNoticeTitle());
                intent.putExtra("propertNoticeId",list.get(position).getPropertyNoticeId());
                startActivityForResult(intent,6000);
            }

            @Override
            public void onItemLongClick(View view, int i) {

            }
        });

    }
    //小区公告列表
    public void requestNoticeList(String communityId, String activityId) {
        showProgress(true);
        Map<String, String> map = new HashMap();
        map.putAll(ConstantsData.getSystemParams());
        map.put(ConstantsData.METHOD, Url.noticeUrl);
        map.put("communityId", communityId);
        map.put("activityId", activityId);
        map.put("currentPage", currentPage + "");
        map.put("rowCountPerPage", String.valueOf(ConstantsData.ROW_COUNT_PER_PAGE));
        String sign = AopUtils.sign(map, ConstantsData.SECRET_VALUE);
        map.put("sign", sign);
        map.remove(ConstantsData.METHOD);
        LLog.d(map + "");
        RetrofitUtil.Api().NoticrLiseDate(map).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new BaseTSubscriber<Notice>(this) {
            @Override
            public void onResponseCallback(JSONObject response, String resultCode) {
                LLog.d("onResponseCallback: " + response);
                showProgress(false);
            }

            @Override
            public void onNext(Notice notice) {
                super.onNext(notice);
                if (TextUtils.equals("000", notice.getCode())) {
                    if (currentPage == 1) {
                        list.clear();
                    }
                    parentRowCountPerPage = notice.getData().getPropertyNoticeList().size();
                    list.addAll(notice.getData().getPropertyNoticeList());
                    adapter.notifyDataSetChanged();
                    mLRecyclerViewAdapter.notifyDataSetChanged();
                }else if(TextUtils.equals("002", notice.getCode())){
                    Utils.toastError(NoticeListActivity.this, StrConstant.NODATA);
                }
                recyclerViewNoticeData.refreshComplete();
                showProgress(false);
            }

            @Override
            public void onNetError(Throwable e) {
                if (currentPage > 0) {
                    currentPage--;
                }
                recyclerViewNoticeData.refreshComplete();
                showProgress(false);
            }

        });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (6001 == resultCode && requestCode == 6000){
            recyclerViewNoticeData.setRefreshing(true);
        }
    }
}
