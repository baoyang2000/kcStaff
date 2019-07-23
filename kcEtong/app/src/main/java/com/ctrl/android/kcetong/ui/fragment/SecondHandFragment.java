package com.ctrl.android.kcetong.ui.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ctrl.android.kcetong.R;
import com.ctrl.android.kcetong.model.AppHolder;
import com.ctrl.android.kcetong.model.UsedGoods;
import com.ctrl.android.kcetong.model.UserGoodsResult;
import com.ctrl.android.kcetong.toolkit.util.AopUtils;
import com.ctrl.android.kcetong.toolkit.util.BaseLinearLayoutManager;
import com.ctrl.android.kcetong.toolkit.util.ConstantsData;
import com.ctrl.android.kcetong.toolkit.util.LLog;
import com.ctrl.android.kcetong.toolkit.util.StrConstant;
import com.ctrl.android.kcetong.toolkit.util.Utils;
import com.ctrl.android.kcetong.toolkit.webutils.BaseTSubscriber;
import com.ctrl.android.kcetong.toolkit.webutils.RetrofitUtil;
import com.ctrl.android.kcetong.ui.activity.BabyDetailActivity;
import com.ctrl.android.kcetong.ui.activity.DetailToBuyActivity;
import com.ctrl.android.kcetong.ui.adapter.SecondHandBuyAdapter;
import com.ctrl.android.kcetong.ui.adapter.SecondHandTransferAdapter;
import com.ctrl.android.kcetong.ui.base.BaseActivity;
import com.ctrl.android.kcetong.ui.base.BaseFragment;
import com.ctrl.android.kcetong.ui.view.DividerDecoration;
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

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by liu on 2017/1/21.
 * 二手交易 fragment
 */

public class SecondHandFragment extends BaseFragment {

    public LRecyclerView mLRecyclerView;
    private LRecyclerViewAdapter mLRecyclerViewAdapter;

    private String handType; //交易类型

    private String                    mKindId;
    private SecondHandTransferAdapter adapter1;
    private SecondHandBuyAdapter      adapter2;

    private int currentPage=1;
    private int currentTotalItem=1;
    private int rowCountPerPage=10;
    private List<UsedGoods> usedGoodsList;

    public static SecondHandFragment newInstance(String type,String mKindId){
        SecondHandFragment fragment=new SecondHandFragment();
        fragment.handType=type;
        fragment.mKindId=mKindId;
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.fragment_second_hand, container, false);
        }
        usedGoodsList = new ArrayList<>();
        context = this.getActivity();
        initView(rootView);
        return rootView;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 1002:
                if(resultCode == 1003){
                    int position = data.getIntExtra("position", 0);
                    usedGoodsList.remove(position - 1);
                    adapter2.notifyDataSetChanged();
                }
                break;
            case 1008:
                if(resultCode == 1009){
                    int position = data.getIntExtra("position", -1);
                    Log.i("Tag", "Tag121313" + position);
                    usedGoodsList.remove(position - 1);
                    adapter1.notifyDataSetChanged();
                }
        }
    }

    private void initView(View rootView) {

        mLRecyclerView = (LRecyclerView) rootView.findViewById(R.id.recyclerView_second_hand);

        if(StrConstant.SECOND_HAND_TRANSFER.equals(handType)) {//转让
            adapter1 = new SecondHandTransferAdapter(handType, context);
            mLRecyclerViewAdapter = new LRecyclerViewAdapter(adapter1);
        }else if(StrConstant.SECOND_HAND_BUY.equals(handType)){
            //求购
            adapter2 = new SecondHandBuyAdapter(context);
            mLRecyclerViewAdapter = new LRecyclerViewAdapter(adapter2);
        }

        mLRecyclerView.setLayoutManager(new BaseLinearLayoutManager(context));
        //设置分割线
        final DividerDecoration divider = new DividerDecoration.Builder(context).setHeight(R.dimen.between_element_margin).setColorResource(R.color.main_bg).build();
        mLRecyclerView.addItemDecoration(divider);

        mLRecyclerView.setRefreshProgressStyle(ProgressStyle.BallSpinFadeLoader);
        mLRecyclerView.setOnRefreshListener(new OnRefreshListener() {
            //0表示待评价 1 表示已评价
            @Override
            public void onRefresh() {
                currentPage = 1;
                //我的分类信息
                if(getActivity().getIntent().getFlags()==StrConstant.GET_OWNER_LIST) {
                    if(StrConstant.SECOND_HAND_TRANSFER.equals(handType)) {//转让

                        if(mKindId == null ||mKindId.equals("")) {
                            requestGoodsList(handType, "", AppHolder.getInstance().getProprietor().getProprietorId(), String.valueOf(currentPage));
                        }else {
                            requestGoodsList(handType,mKindId,AppHolder.getInstance().getProprietor().getProprietorId(),String.valueOf(currentPage));
                        }

                    }else if(StrConstant.SECOND_HAND_BUY.equals(handType)){//求购

                        if(mKindId == null ||mKindId.equals("")) {
                            requestGoodsList(handType, "", AppHolder.getInstance().getProprietor().getProprietorId(), String.valueOf(currentPage));
                        }else {
                            requestGoodsList(handType,mKindId,AppHolder.getInstance().getProprietor().getProprietorId(),String.valueOf(currentPage));
                        }
                    }
                }else {
                    //全部信息
                    if(StrConstant.SECOND_HAND_TRANSFER.equals(handType)) {//转让

                        if(mKindId == null ||mKindId.equals("")) {
                            requestGoodsList(handType, "", "", String.valueOf(currentPage));
                        }else {
                            requestGoodsList(handType,mKindId,"",String.valueOf(currentPage));
                        }

                    }else if(StrConstant.SECOND_HAND_BUY.equals(handType)){//求购

                        if(mKindId == null ||mKindId.equals("")) {
                            requestGoodsList(handType, "", "", String.valueOf(currentPage));
                        }else {
                            requestGoodsList(handType,mKindId,"",String.valueOf(currentPage));
                        }
                    }
                }
            }
        });

        mLRecyclerView.setRefreshing(true);

        mLRecyclerView.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                currentPage++;

                if(currentTotalItem == 10){
                    RecyclerViewStateUtils.setFooterViewState((Activity) context, mLRecyclerView, 10, LoadingFooter.State.Loading, null);

                    //我的分类信息
                    if(getActivity().getIntent().getFlags()==StrConstant.GET_OWNER_LIST) {
                        if(StrConstant.SECOND_HAND_TRANSFER.equals(handType)) {//转让

                            if(mKindId == null ||mKindId.equals("")) {
                                requestGoodsList(handType, "", AppHolder.getInstance().getProprietor().getProprietorId(), String.valueOf(currentPage));
                            }else {
                                requestGoodsList(handType,mKindId,AppHolder.getInstance().getProprietor().getProprietorId(),String.valueOf(currentPage));
                            }

                        }else if(StrConstant.SECOND_HAND_BUY.equals(handType)){//求购

                            if(mKindId == null ||mKindId.equals("")) {
                                requestGoodsList(handType, "", AppHolder.getInstance().getProprietor().getProprietorId(), String.valueOf(currentPage));
                            }else {
                                requestGoodsList(handType,mKindId,AppHolder.getInstance().getProprietor().getProprietorId(),String.valueOf(currentPage));
                            }
                        }
                    }else {
                        //全部信息
                        if(StrConstant.SECOND_HAND_TRANSFER.equals(handType)) {//转让

                            if(mKindId == null ||mKindId.equals("")) {
                                requestGoodsList(handType, "", "", String.valueOf(currentPage));
                            }else {
                                requestGoodsList(handType,mKindId,"",String.valueOf(currentPage));
                            }

                        }else if(StrConstant.SECOND_HAND_BUY.equals(handType)){//求购

                            if(mKindId == null ||mKindId.equals("")) {
                                requestGoodsList(handType, "", "", String.valueOf(currentPage));
                            }else {
                                requestGoodsList(handType,mKindId,"",String.valueOf(currentPage));
                            }
                        }
                    }
                }else {
                    RecyclerViewStateUtils.setFooterViewState((Activity) context, mLRecyclerView, 15, LoadingFooter.State.Normal, null);
//                            if(mKindId==null ||mKindId.equals("")) {
//                                requestGoodsList(handType, "", AppHolder.getInstance().getProprietor().getProprietorId(), String.valueOf(currentPage));
//                            }else {
//                                requestGoodsList(handType,mKindId,AppHolder.getInstance().getProprietor().getProprietorId(),String.valueOf(currentPage));
//                            }
                    Utils.toastError(context,StrConstant.NO_MORE_CONTENT);
                    currentPage--;
                }
            }
        });

        if(getActivity().getIntent().getFlags() == StrConstant.GET_OWNER_LIST){//我的
            if(StrConstant.SECOND_HAND_TRANSFER.equals(handType)){//转让

                adapter1.setOnItemClickListener(new SecondHandTransferAdapter.OnRecyclerViewItemClickListener() {
                    @Override
                    public void onItemClick(View view, Map<String, String> data) {

                        Intent intent = new Intent(context, BabyDetailActivity.class);
                        intent.putExtra("usedGoodsId", data.get("usedGoodsId"));
                        intent.putExtra("position", Integer.parseInt(data.get("position")));
                        intent.addFlags(StrConstant.MY_BABY_DETAIL);
                        intent.putExtra("releaseTime", data.get("releaseTime"));

                        Log.i("usedGoodsId", "jason" + data.get("usedGoodsId"));
                        Log.i("releaseTime", "jason" + data.get("releaseTime"));
                        startActivityForResult(intent, 1008);
                    }
                });
            }else if(StrConstant.SECOND_HAND_BUY.equals(handType)){//求购

                adapter2.setOnItemClickListener(new SecondHandBuyAdapter.OnRecyclerViewItemClickListener() {
                    @Override
                    public void onItemClick(View view, Map<String, String> data) {
                        Intent intent = new Intent(context, DetailToBuyActivity.class);
                        intent.putExtra("usedGoodsId", data.get("usedGoodsId"));
                        intent.putExtra("position",Integer.parseInt(data.get("position")));
                        intent.addFlags(StrConstant.MY_BABY_BUY_DETAIL);
                        intent.putExtra("releaseTime", data.get("releaseTime"));

                        Log.i("usedGoodsId", "jason" + data.get("usedGoodsId"));
                        Log.i("releaseTime", "jason" + data.get("releaseTime"));
                        startActivityForResult(intent, 1002);
                    }
                });
            }
        }else {//全部信息
            if(StrConstant.SECOND_HAND_TRANSFER.equals(handType)){//转让

                adapter1.setOnItemClickListener(new SecondHandTransferAdapter.OnRecyclerViewItemClickListener() {
                    @Override
                    public void onItemClick(View view, Map<String, String> data) {

                        Intent intent = new Intent(context, BabyDetailActivity.class);
                        intent.putExtra("usedGoodsId", data.get("usedGoodsId"));
                        intent.putExtra("releaseTime", data.get("releaseTime"));

                        Log.i("usedGoodsId", "jason" + data.get("usedGoodsId"));
                        Log.i("releaseTime", "jason" + data.get("releaseTime"));
                        startActivity(intent);
                    }
                });
            }else if(StrConstant.SECOND_HAND_BUY.equals(handType)){//求购

                adapter2.setOnItemClickListener(new SecondHandBuyAdapter.OnRecyclerViewItemClickListener() {
                    @Override
                    public void onItemClick(View view, Map<String, String> data) {
                        Intent intent = new Intent(context, DetailToBuyActivity.class);
                        intent.putExtra("usedGoodsId", data.get("usedGoodsId"));
                        intent.putExtra("releaseTime", data.get("releaseTime"));

                        Log.i("usedGoodsId", "jason" + data.get("usedGoodsId"));
                        Log.i("releaseTime", "jason" + data.get("releaseTime"));
                        startActivity(intent);
                    }
                });
            }
        }
        mLRecyclerView.setAdapter(mLRecyclerViewAdapter);
    }

    /**
     * 获取二手交易列表
     * @param transactionType 交易类型（0：转让、1：求购）
     * @param goodKindId 二手物品分类id
     * @param proprietorId  业主id
     * @param current_Page 当前页码
     */
    public void requestGoodsList(String transactionType,String goodKindId,String proprietorId,String current_Page) {
        Map<String,String> params = new HashMap<>();
        params.put(ConstantsData.METHOD,"pm.ppt.usedGoods.list");
        params.putAll(ConstantsData.getSystemParams());

        params.put("transactionType", transactionType);
        params.put("goodKindId", goodKindId);
        if("1".equals(AppHolder.getInstance().getMemberInfo().getSupers())
           || "2".equals(AppHolder.getInstance().getMemberInfo().getSupers())){
            params.put("proprietorId", proprietorId);
        }

        params.put("currentPage", current_Page);
        params.put("rowCountPerPage", "10");

        String sign = AopUtils.sign(params, ConstantsData.SECRET_VALUE);
        params.put("sign",sign);
        LLog.d(params+"");
        params.remove(ConstantsData.METHOD);
        RetrofitUtil.Api().requestGoodsList(params).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseTSubscriber<UserGoodsResult>((BaseActivity) this.getActivity()) {
                    @Override
                    public void onResponseCallback(JSONObject response, String resultCode) {
                        LLog.d(response+"");
                    }
                    @Override
                    public void onNext(UserGoodsResult userGoodsResult) {
                        super.onNext(userGoodsResult);
                        if(TextUtils.equals("000",userGoodsResult.getCode())){
                            List<UsedGoods> list_result = userGoodsResult.getData().getUsedGoodList();
                            if(currentPage == 1){
                                if(StrConstant.SECOND_HAND_TRANSFER.equals(handType)){//转让
                                    adapter1.clear();
                                }else if(StrConstant.SECOND_HAND_BUY.equals(handType)){
                                    adapter2.clear();
                                }
                            }
                            int dataListSize = userGoodsResult.getData().getUsedGoodList().size();
                            currentTotalItem =dataListSize;
                            if (userGoodsResult.getData().getUsedGoodList() != null && dataListSize >0){
                                if(currentPage >1){//如果是加载更多，将数据添加到list后面
                                    usedGoodsList.addAll(list_result);

                                }else if(currentPage == 1){
                                    usedGoodsList = list_result;
                                }
                                if(StrConstant.SECOND_HAND_TRANSFER.equals(handType)){
                                    adapter1.addGoodsList(usedGoodsList);
                                }else if(StrConstant.SECOND_HAND_BUY.equals(handType)){
                                    adapter2.addGoodsList(usedGoodsList);
                                }
                            }
                            mLRecyclerViewAdapter.notifyDataSetChanged();
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
                        showProgress(false);
                    }
                });
    }

}
