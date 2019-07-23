package com.ctrl.android.kcetong.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;
import com.ctrl.android.kcetong.R;
import com.ctrl.android.kcetong.model.AppHolder;
import com.ctrl.android.kcetong.model.Visit;
import com.ctrl.android.kcetong.model.VisitBean;
import com.ctrl.android.kcetong.toolkit.Url.Url;
import com.ctrl.android.kcetong.toolkit.util.AopUtils;
import com.ctrl.android.kcetong.toolkit.util.BaseLinearLayoutManager;
import com.ctrl.android.kcetong.toolkit.util.ConstantsData;
import com.ctrl.android.kcetong.toolkit.util.LLog;
import com.ctrl.android.kcetong.toolkit.util.Utils;
import com.ctrl.android.kcetong.toolkit.webutils.BaseTSubscriber;
import com.ctrl.android.kcetong.toolkit.webutils.RetrofitUtil;
import com.ctrl.android.kcetong.ui.adapter.MyVisitAdapter;
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

/**
 * @项目名称: 诚信行<br>
 * @类描述: 预约访客<br>
 * @创建人： whs <br>
 * @创建时间： 2017/1/12 15:17 <br>
 * @修改人： <br>
 * @修改时间: 2017/1/12 15:17 <br>
 */
public class MyVisitActivity extends BaseActivity {

    @BindView(R.id.toolbar_right_btn)
    TextView      Rightbtn;
    @BindView(R.id.lv_express)
    LRecyclerView lvExpress;
    private int currentPage           = 1;
    private int parentRowCountPerPage = 0;
    private List<Visit>          list;
    private MyVisitAdapter       adapter;
    private LRecyclerViewAdapter mLRecyclerViewAdapter;
    private String               proprietorId;

    @Override
    protected void initView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_my_visit);
        ButterKnife.bind(this);
        proprietorId = AppHolder.getInstance().getProprietor().getProprietorId();
    }

    @Override
    protected void initData() {

        toolbarBaseSetting("我的到访", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utils.hideKeyBoard(MyVisitActivity.this);
                MyVisitActivity.this.finish();
            }
        });
        Rightbtn.setVisibility(View.VISIBLE);
        Rightbtn.setText("添加");
        Rightbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if("0".equals(AppHolder.getInstance().getMemberInfo().getSupers())){
                    Intent intent = new Intent(MyVisitActivity.this, VisitAddActivity.class);
                    startActivityForResult(intent,7000);
                }else if("1".equals(AppHolder.getInstance().getMemberInfo().getSupers())
                         || "2".equals(AppHolder.getInstance().getMemberInfo().getSupers())){
                    Utils.showShortToast(MyVisitActivity.this, getString(R.string.manager_cannot));
                }
            }
        });
        getVisitList(proprietorId);
        list = new ArrayList<>();
        adapter = new MyVisitAdapter(MyVisitActivity.this);
        adapter.setListData(list);
        mLRecyclerViewAdapter = new LRecyclerViewAdapter(adapter);
        lvExpress.setLayoutManager(new BaseLinearLayoutManager(this));
        //设置分割线
        final DividerDecoration divider = new DividerDecoration.Builder(this).setHeight(R.dimen.between_element_margin).setColorResource(R.color.main_bg).build();
        lvExpress.addItemDecoration(divider);
        lvExpress.setRefreshProgressStyle(ProgressStyle.BallSpinFadeLoader);
        lvExpress.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh() {
                //下拉刷新
                currentPage = 1;
                getVisitList(proprietorId);
            }
        });
        lvExpress.setRefreshing(true);
        lvExpress.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                currentPage++;
                if (parentRowCountPerPage == 10) {
                    RecyclerViewStateUtils.setFooterViewState(MyVisitActivity.this, lvExpress, 5, LoadingFooter.State.Loading, null);
                    getVisitList(proprietorId);

                } else {
                    RecyclerViewStateUtils.setFooterViewState(MyVisitActivity.this, lvExpress, 10, LoadingFooter.State.Normal, null);
                }
            }
        });
        lvExpress.setAdapter(mLRecyclerViewAdapter);
        mLRecyclerViewAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {

                //到访类型（0：预约到访、1：突发到访）
                if(list.get(position).getType() == 0){
                    Intent intent = new Intent(MyVisitActivity.this,MyVisitDetail.class);
                    intent.putExtra("communityVisitId",list.get(position).getCommunityVisitId());
                    startActivityForResult(intent,7000);

                } else {
                    Intent intent = new Intent(MyVisitActivity.this,BurstVisitActivity.class);
                    intent.putExtra("communityVisitId",list.get(position).getCommunityVisitId());
                    startActivityForResult(intent,8000);

                }
            }

            @Override
            public void onItemLongClick(View view, int i) {

            }
        });
    }

    //获得访问列表
    private void getVisitList(String proprietorId) {
        Map<String, String> map = new HashMap<>();
        map.putAll(ConstantsData.getSystemParams());
        map.put(ConstantsData.METHOD, Url.VisitlistUrl);
        map.put("proprietorId", proprietorId);
        map.put("currentPage", currentPage + "");
        map.put("rowCountPerPage", String.valueOf(ConstantsData.ROW_COUNT_PER_PAGE));
        String sign = AopUtils.sign(map, ConstantsData.SECRET_VALUE);
        map.put("sign", sign);
        map.remove(ConstantsData.METHOD);
        RetrofitUtil.Api().VisitListdate(map).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new BaseTSubscriber<VisitBean>(this) {
            @Override
            public void onResponseCallback(JSONObject response, String resultCode) {
                LLog.d("onResponseCallback: " + response);
            }

            @Override
            public void onNext(VisitBean visitBean) {
                super.onNext(visitBean);
                if (TextUtils.equals("000", visitBean.getCode())) {
                    if (currentPage == 1) {
                        list.clear();
                    }
                    parentRowCountPerPage = visitBean.getData().getVisitList().size();
                    list.addAll(visitBean.getData().getVisitList());
                    adapter.notifyDataSetChanged();
                    mLRecyclerViewAdapter.notifyDataSetChanged();

                }
                lvExpress.refreshComplete();


            }

            @Override
            public void onNetError(Throwable e) {

                if (e.getMessage() == null) {

                } else if (e.getMessage().contains("Failed to connect") || e.getMessage().contains("failed to connect") || e.getMessage().contains("SocketTimeoutException")) {
                    Utils.toastError(MyVisitActivity.this, "服务器连接超时,请检查网络");
                } else if ("Invalid index 0, size is 0".equals(e.getMessage())) {//连接超时

                } else if (e.getMessage().contains("Unable to resolve host")) {//网络访问错误
                    Utils.toastError(MyVisitActivity.this, "网络请求错误");
                } else {
                    Utils.toastError(MyVisitActivity.this, "网络请求错误");
                }

                if (currentPage > 0) {
                    currentPage--;
                }
                lvExpress.refreshComplete();
            }

        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (7001 == resultCode && requestCode == 7000){
            lvExpress.setRefreshing(true);
        }
        if (8001 == resultCode && requestCode == 8000){
            lvExpress.setRefreshing(true);
        }
    }
}
