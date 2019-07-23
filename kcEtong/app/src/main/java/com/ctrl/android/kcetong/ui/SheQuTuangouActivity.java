package com.ctrl.android.kcetong.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

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
import com.ctrl.android.kcetong.ui.adapter.ChoiceAdapter;
import com.ctrl.android.kcetong.ui.base.BaseActivity;
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
 * 描述：社区团购
 *
 * @author zhangqin
 * @date 2018/4/16
 */
public class SheQuTuangouActivity extends BaseActivity {

    @BindView(R.id.recyclerView)
    LRecyclerView recyclerView;
    @BindView(R.id.tv_top_title)
    TextView mTvTopTitle;

    private LRecyclerViewAdapter mLRecyclerViewAdapter;
    private ChoiceAdapter adapter;
    private int currentPage = 1;
    private int rowCountPerPage = 10;
    private int parentRowCountPerPage = 0;
    private List<ChoiceGroup> list = new ArrayList<>();
    private boolean isPrepared;

    private Activity mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_she_qu_tuangou);
        ButterKnife.bind(this);
        mContext = this;

        mTvTopTitle.setText("社区团购");


        adapter = new ChoiceAdapter(mContext);
        adapter.setDataList(list);
        mLRecyclerViewAdapter = new LRecyclerViewAdapter(adapter);
        recyclerView.setLayoutManager(new BaseLinearLayoutManager(mContext));
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
                    RecyclerViewStateUtils.setFooterViewState(mContext, recyclerView, 10, LoadingFooter.State.Loading, null);
                    getData();

                } else {
                    RecyclerViewStateUtils.setFooterViewState(mContext, recyclerView, 10, LoadingFooter.State.Normal, null);
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
                i.setClass(mContext, ChoiceDetailActivity.class);
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

    @Override
    protected void initView(Bundle savedInstanceState) {

    }

    @Override
    protected void initData() {

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
        RetrofitUtil.Api().productList(map).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new BaseTSubscriber<ProductListBean>((BaseActivity) mContext) {
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
                    Utils.toastError(mContext, StrConstant.NODATA);
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
                    Utils.toastError(mContext, "服务器连接超时,请检查网络");
                } else if ("Invalid index 0, size is 0".equals(e.getMessage())) {//连接超时
                }

                if (currentPage > 0) {
                    currentPage--;
                }
                RecyclerViewStateUtils.setFooterViewState(mContext, recyclerView, 10, LoadingFooter.State.Normal, null);
                recyclerView.refreshComplete();
            }

        });
    }

}
