package com.ctrl.android.kcetong.ui.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ctrl.android.kcetong.R;
import com.ctrl.android.kcetong.model.AppHolder;
import com.ctrl.android.kcetong.model.Survey;
import com.ctrl.android.kcetong.model.SurveyListResult;
import com.ctrl.android.kcetong.toolkit.util.AopUtils;
import com.ctrl.android.kcetong.toolkit.util.BaseLinearLayoutManager;
import com.ctrl.android.kcetong.toolkit.util.ConstantsData;
import com.ctrl.android.kcetong.toolkit.util.LLog;
import com.ctrl.android.kcetong.toolkit.util.MessageUtils;
import com.ctrl.android.kcetong.toolkit.util.StrConstant;
import com.ctrl.android.kcetong.toolkit.util.Utils;
import com.ctrl.android.kcetong.toolkit.webutils.BaseTSubscriber;
import com.ctrl.android.kcetong.toolkit.webutils.RetrofitUtil;
import com.ctrl.android.kcetong.ui.activity.SurveyDetailActivity;
import com.ctrl.android.kcetong.ui.activity.VoteDetailActivity;
import com.ctrl.android.kcetong.ui.adapter.SurveyListAdapter;
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
 * Created by liu on 2017/1/20.
 */

public class SurveyListFragment extends BaseFragment {
    private String surveyType;//调查列表的类型

    private String proprietorId = "1";
    public LRecyclerView mLRecyclerView;
    private LRecyclerViewAdapter mLRecyclerViewAdapter;

    private SurveyListAdapter adapter;
    private int currentPage = 1;
    private int rowCountPerPage = ConstantsData.PAGE_CAPACITY;
    private int parentRowCountPerPage = 0;

    private List<Survey> surveyList;

    public static SurveyListFragment newInstance(String surveyType){
        SurveyListFragment fragment = new SurveyListFragment();
        fragment.surveyType = surveyType;
        return fragment;
    }

    public static SurveyListFragment newInstance(String surveyType,String proprietorId){
        SurveyListFragment fragment = new SurveyListFragment();
        fragment.surveyType = surveyType;
        fragment.proprietorId = proprietorId;
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.survey_list_fragment, container, false);
        }
        surveyList = new ArrayList<>();
        context = this.getActivity();
        initView(rootView);
        return rootView;
    }

    private void initView(View rootView){

        mLRecyclerView = (LRecyclerView) rootView.findViewById(R.id.recyclerView_survey_list);
        adapter = new SurveyListAdapter(surveyType,context);

        mLRecyclerViewAdapter  = new LRecyclerViewAdapter(adapter);

        mLRecyclerView.setLayoutManager(new BaseLinearLayoutManager(context));
        //设置分割线
        final DividerDecoration divider = new DividerDecoration.Builder(context)
                .setHeight(R.dimen.default_divider_height)
                .setColorResource(R.color.main_bg)
                .build();
        mLRecyclerView.addItemDecoration(divider);

        mLRecyclerView.setRefreshProgressStyle(ProgressStyle.BallSpinFadeLoader);
        mLRecyclerView.setOnRefreshListener(new OnRefreshListener() {
            //0表示待评价 1 表示已评价
            @Override
            public void onRefresh() {
                currentPage = 1;

                String communityId = AppHolder.getInstance().getHouse().getCommunityId();
                //问卷类型（0：调查问卷、1：投票）
                String questionnaireType = "";
                if(surveyType.equals(StrConstant.COMMINITY_SURVEY)){
                    questionnaireType = "0";
                } else if(surveyType.equals(StrConstant.COMMINITY_VOTE)){
                    questionnaireType = "1";
                }
                showProgress(true);
                requestSurveyList(communityId,questionnaireType,proprietorId,String.valueOf(currentPage),String.valueOf(rowCountPerPage));

            }
        });

        mLRecyclerView.setRefreshing(true);

        mLRecyclerView.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                currentPage++;
//                LLog.d(currentPage + "");
                String communityId = AppHolder.getInstance().getHouse().getCommunityId();
                //问卷类型（0：调查问卷、1：投票）
                String questionnaireType = "";
                if(surveyType.equals(StrConstant.COMMINITY_SURVEY)){
                    questionnaireType = "0";
                } else if(surveyType.equals(StrConstant.COMMINITY_VOTE)){
                    questionnaireType = "1";
                }
                if(parentRowCountPerPage == ConstantsData.PAGE_CAPACITY){
                    RecyclerViewStateUtils.setFooterViewState((Activity) context, mLRecyclerView, 10, LoadingFooter.State.Loading, null);
                    showProgress(true);
                    requestSurveyList(communityId,questionnaireType,proprietorId,String.valueOf(currentPage),String.valueOf(rowCountPerPage));
                }else {
                    RecyclerViewStateUtils.setFooterViewState((Activity) context, mLRecyclerView, 15, LoadingFooter.State.Normal, null);
                    Utils.toastError(context,StrConstant.NO_MORE_CONTENT);
                    currentPage--;
                }
            }
        });

        adapter.setOnItemClickListener(new SurveyListAdapter.OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(View view, Map<String,String> map) {
                LLog.d(map+"-----");
                String data = map.get("id");
                String title = map.get("title");
                int status = Integer.parseInt(map.get("status"));
                String position = map.get("position");

                if(surveyType.equals(StrConstant.COMMINITY_SURVEY)){

                    if(status == 1){
                        MessageUtils.showShortToast(getActivity(),"已参与该社区调查");
                        Intent intent = new Intent(getActivity(), SurveyDetailActivity.class);
                        intent.putExtra("id",data);
                        //是否参与（0：未参与、1：已参与）
                        intent.putExtra("partInFlg","" + status);
                        intent.putExtra("title",title);
                        startActivity(intent);
                    } else {
                        Intent intent = new Intent(getActivity(), SurveyDetailActivity.class);
                        intent.putExtra("id",data);
                        //是否参与（0：未参与、1：已参与）
                        intent.putExtra("partInFlg","" + status);
                        intent.putExtra("title",title);
                        startActivityForResult(intent,200);
                    }

                } else if(surveyType.equals(StrConstant.COMMINITY_VOTE)){

                    if(status == 1){
                        MessageUtils.showShortToast(getActivity(),"已参与该投票");
                        Intent intent = new Intent(getActivity(), VoteDetailActivity.class);
                        intent.putExtra("id",data);
                        //是否参与（0：未参与、1：已参与）
                        intent.putExtra("partInFlg","" + status);
                        intent.putExtra("title",title);

                        startActivity(intent);
                    } else {
                        Intent intent = new Intent(getActivity(), VoteDetailActivity.class);
                        intent.putExtra("id",data);
                        //是否参与（0：未参与、1：已参与）
                        intent.putExtra("partInFlg","" + status);
                        intent.putExtra("title",title);
                        intent.putExtra("position",position);
                        startActivityForResult(intent, 200);
                    }
                }
            }
        });
        mLRecyclerView.setAdapter(mLRecyclerViewAdapter);
    }

    /**
     * 调查列表
     * @param communityId 社区ID
     * @param questionnaireType 问卷类型（0：调查问卷、1：投票）
     * @param proprietorId 业主ID
     * @param current_Page 当前页码
     * @param rowCountPerPage 每页条数
     * */
    public void requestSurveyList(String communityId,String questionnaireType,
            String proprietorId,String current_Page,String rowCountPerPage){
        Map<String,String> map = new HashMap<>();
        map.put(ConstantsData.METHOD,"pm.ppt.questionnaire.list");//方法名称
        map.putAll(ConstantsData.getSystemParams());

        map.put("communityId",communityId);
        map.put("questionnaireType",questionnaireType);
        if("0".equals(AppHolder.getInstance().getMemberInfo().getSupers())){
            map.put("proprietorId",proprietorId);
        }

        map.put("currentPage",current_Page);
        map.put("rowCountPerPage",rowCountPerPage);

        String sign = AopUtils.sign(map, ConstantsData.SECRET_VALUE);
        map.put("sign",sign);
        LLog.d(map+"");
        map.remove(ConstantsData.METHOD);

        RetrofitUtil.Api().requestSurveyList(map).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseTSubscriber<SurveyListResult>((BaseActivity) this.getActivity()) {
                    @Override
                    public void onResponseCallback(JSONObject response, String resultCode) {
                        LLog.d(response+"");
                        showProgress(false);
                    }

                    @Override
                    public void onNext(SurveyListResult surveyListResult) {
                        super.onNext(surveyListResult);
                        if(TextUtils.equals("000",surveyListResult.getCode())){
                            List<Survey> list_result = surveyListResult.getData().getQuestionnaireList();
                            LLog.d(list_result+"");
                            if (currentPage == 1){//清除原有数据
                                if(StrConstant.COMMINITY_SURVEY.equals(surveyType)){
                                    adapter.clearSurvey();
                                }else if(StrConstant.COMMINITY_VOTE.equals(surveyType)){
                                    adapter.clearVote();
                                }
                            }
                            int dataListSize = surveyListResult.getData().getQuestionnaireList().size();
                            parentRowCountPerPage =dataListSize;
                            if (surveyListResult.getData().getQuestionnaireList() != null &&dataListSize >0){
                                if(currentPage >1){//如果是加载更多，将数据添加到list后面
                                    surveyList.addAll(list_result);

                                }else if(currentPage == 1){
                                    surveyList = list_result;
                                }
                                if(StrConstant.COMMINITY_SURVEY.equals(surveyType)){
                                    adapter.addSurveyList(surveyList);
                                }else if(StrConstant.COMMINITY_VOTE.equals(surveyType)){
                                    adapter.addVoteList(surveyList);
                                }
                            }
                            mLRecyclerViewAdapter.notifyDataSetChanged();
                        }else if(TextUtils.equals("002", surveyListResult.getCode())){
                            Utils.toastError(context, StrConstant.NODATA);
                        }
                        mLRecyclerView.refreshComplete();
                        showProgress(false);
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
