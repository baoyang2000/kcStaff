package com.ctrl.android.kcetong.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import com.ctrl.android.kcetong.R;
import com.ctrl.android.kcetong.model.AppHolder;
import com.ctrl.android.kcetong.model.Express;
import com.ctrl.android.kcetong.model.ExpressResult;
import com.ctrl.android.kcetong.toolkit.util.AopUtils;
import com.ctrl.android.kcetong.toolkit.util.BaseLinearLayoutManager;
import com.ctrl.android.kcetong.toolkit.util.ConstantsData;
import com.ctrl.android.kcetong.toolkit.util.LLog;
import com.ctrl.android.kcetong.toolkit.util.StrConstant;
import com.ctrl.android.kcetong.toolkit.util.Utils;
import com.ctrl.android.kcetong.toolkit.webutils.BaseTSubscriber;
import com.ctrl.android.kcetong.toolkit.webutils.RetrofitUtil;
import com.ctrl.android.kcetong.ui.adapter.ExpressAdapter;
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

public class ExpressActivity extends BaseActivity {

    @BindView(R.id.recyclerView_activity)
    LRecyclerView mLRecyclerView;
    private LRecyclerViewAdapter mLRecyclerViewAdapter;

    private String TITLE = StrConstant.EXPRESS_LIST_TITLE;
    private String proprietorId = AppHolder.getInstance().getProprietor().getProprietorId();
    private int currentPage = 1;
    private int rowCountPerPage = ConstantsData.PAGE_CAPACITY;
    private int parentRowCountPerPage = 0;
    /**
     * 快递列表
     * */
    private List<Express> expressList = new ArrayList<>();

    private ExpressAdapter adapter;

    @Override
    protected void initView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_express);
        ButterKnife.bind(this);
        toolbarBaseSetting(TITLE, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ExpressActivity.this.finish();
            }
        });
    }
    @Override
    protected void initData() {
        adapter = new ExpressAdapter(this);
        mLRecyclerViewAdapter = new LRecyclerViewAdapter(adapter);
        mLRecyclerView.setLayoutManager(new BaseLinearLayoutManager(this));
        mLRecyclerView.setRefreshProgressStyle(ProgressStyle.BallSpinFadeLoader);
        mLRecyclerView.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh() {
                currentPage = 1;
                requestExpressList(proprietorId, String.valueOf(currentPage), String.valueOf(rowCountPerPage));
            }
        });
        mLRecyclerView.setRefreshing(true);

        mLRecyclerView.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                currentPage++;
                LLog.d(currentPage+"");
                if(parentRowCountPerPage == ConstantsData.PAGE_CAPACITY){
                    RecyclerViewStateUtils.setFooterViewState(ExpressActivity.this, mLRecyclerView, 10, LoadingFooter.State.Loading, null);
                    requestExpressList(proprietorId, String.valueOf(currentPage), String.valueOf(rowCountPerPage));
                }else {
                    RecyclerViewStateUtils.setFooterViewState(ExpressActivity.this, mLRecyclerView, 15, LoadingFooter.State.Normal, null);
//                    requestExpressList(proprietorId, String.valueOf(currentPage), String.valueOf(rowCountPerPage));
                    Utils.toastError(ExpressActivity.this,"没有更多内容了");
                }
            }
        });
        adapter.setOnItemClickListener(new ExpressAdapter.OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(View view, String data) {
                Intent intent = new Intent(ExpressActivity.this, ExpressDetailActivity.class);
                intent.putExtra("expressId",data);
                startActivity(intent);
            }
        });
        mLRecyclerView.setAdapter(mLRecyclerViewAdapter);
    }
    /**
     * 获取业主快递信息列表
     * @param proprietorId 业主id
     * @param current_Page 当前页码
     * @param rowCountPerPage 每页条数
     */
    public void requestExpressList(String proprietorId, final String current_Page,String rowCountPerPage) {
        Map<String,String> map = new HashMap<String,String>();
        map.put(ConstantsData.METHOD,"pm.ppt.express.list");//方法名称
        map.putAll(ConstantsData.getSystemParams());

        map.put("proprietorId", proprietorId);
        map.put("currentPage", current_Page);
        map.put("rowCountPerPage", rowCountPerPage);

        String sign = AopUtils.sign(map, ConstantsData.SECRET_VALUE);
        map.put("sign",sign);
        LLog.d(map+"");
        map.remove(ConstantsData.METHOD);
        RetrofitUtil.Api().requestExpressList(map).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseTSubscriber<ExpressResult>(this) {
                    @Override
                    public void onResponseCallback(JSONObject response, String resultCode) {
                        LLog.d(response+"");
                        showProgress(false);
                    }

                    @Override
                    public void onNext(ExpressResult expressResult) {
                        super.onNext(expressResult);
                        if(TextUtils.equals("000",expressResult.getCode())){
                            if (currentPage == 1) {//清除原有数据
                                adapter.clear();
                            }
                            parentRowCountPerPage = expressResult.getData().getExpressList().size();
                            List<Express> list_result = expressResult.getData().getExpressList();
                            if(expressResult.getData().getExpressList() != null && list_result.size()>0){
                                if(currentPage > 1){//当前页大于1，将加载出来的数据添加到expressList后面
                                    expressList.addAll(list_result);
                                }else if(currentPage == 1){
                                    expressList = list_result;
                                }
                                adapter.setList(expressList);

                            }
                            mLRecyclerViewAdapter.notifyDataSetChanged();
                        }else if(TextUtils.equals("002",expressResult.getCode())){

                            Utils.showShortToast(ExpressActivity.this, "无数据");
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

}
