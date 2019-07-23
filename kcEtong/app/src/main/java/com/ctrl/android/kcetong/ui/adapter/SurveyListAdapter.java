package com.ctrl.android.kcetong.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ctrl.android.kcetong.R;
import com.ctrl.android.kcetong.model.Survey;
import com.ctrl.android.kcetong.toolkit.util.ConstantsData;
import com.ctrl.android.kcetong.toolkit.util.D;
import com.ctrl.android.kcetong.toolkit.util.S;
import com.ctrl.android.kcetong.toolkit.util.StrConstant;
import com.github.jdsjlzx.recyclerview.LRecyclerView;
import com.github.jdsjlzx.recyclerview.LRecyclerViewAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by liu on 2017/1/20.
 */

public class SurveyListAdapter extends LRecyclerView.Adapter<SurveyListAdapter.SurveyListViewHolder> implements View.OnClickListener{

    private Context mContext;

    private List<Survey> surveyList ;
    private List<Survey> voteList;
    private String surveyType;

    private SurveyListAdapter.OnRecyclerViewItemClickListener mOnItemClickListener = null;

    public static interface OnRecyclerViewItemClickListener{
        void onItemClick(View view,Map<String,String> data);
    }

    public SurveyListAdapter(String surveyType,Context mContext) {
        this.mContext = mContext;
        this.surveyType = surveyType;
        surveyList = new ArrayList<>();
        voteList = new ArrayList<>();
    }

    /**
     * 添加问卷调查
     *
     * @param allList
     */
    public void addSurveyList(List<Survey> allList) {
        this.surveyList = allList;
    }
    /**
     * 添加投票
     *
     * @param takeInList
     */
    public void addVoteList(List<Survey> takeInList) {
        this.voteList = takeInList;
    }

    /**
     * 清除
     */
    public void clearSurvey() {
        this.surveyList.clear();
    }

    public void clearVote() {
        this.voteList.clear();
    }
    @Override
    public SurveyListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View rootView = LayoutInflater.from(mContext).inflate(R.layout.survey_list_item, parent, false);
        rootView.setOnClickListener(this);
        return new SurveyListViewHolder(rootView);
    }

    @Override
    public void onBindViewHolder(SurveyListViewHolder holder, int position) {
        SurveyListViewHolder surveyListViewHolder = holder;
        Survey survey;
        if(surveyType.equals(StrConstant.COMMINITY_SURVEY)){
            survey = surveyList.get(position);
            initData(survey,surveyListViewHolder,position);
        } else if(surveyType.equals(StrConstant.COMMINITY_VOTE)){
            survey = voteList.get(position);
            initData(survey,surveyListViewHolder,position);
        }
    }

    private void initData(final Survey survey,SurveyListViewHolder holder, int position){
        holder.survey_title.setText(S.getStr(survey.getTitle()));
        holder.community_name_and_time.setText(S.getStr(survey.getCommunityName()) + "    " + D.getDateStrFromStamp(ConstantsData.YYYY_MM_DD,survey.getCreateTime()));
        //是否参与（0：未参与、1：已参与）
        if(survey.getHasParticipate() == 1){
            holder.survey_status_flg.setText("已参与");
            holder.survey_status_flg.setBackgroundResource(R.drawable.gray_bg_shap);
        } else {
            holder.survey_status_flg.setText("未参与");
            holder.survey_status_flg.setBackgroundResource(R.drawable.orange_bg_shap);
        }

        Map<String,String> map = new HashMap<>();
        map.put("id",survey.getId());
        map.put("status",survey.getHasParticipate()+"");
        map.put("title",survey.getTitle());
        map.put("position",position+"");
        holder.itemView.setTag(map);
//        holder.itemView.setTag(survey.getHasParticipate());
    }

    @Override
    public int getItemCount() {
        if(surveyType.equals(StrConstant.COMMINITY_SURVEY)){
            return this.surveyList.size();
        } else if(surveyType.equals(StrConstant.COMMINITY_VOTE)){
            return this.voteList.size();
        }
        return 0;
    }
    @Override
    public void onClick(View v) {
        if(mOnItemClickListener  != null){
            mOnItemClickListener .onItemClick(v,(Map<String,String>)v.getTag());
        }
    }
    public void setOnItemClickListener(SurveyListAdapter.OnRecyclerViewItemClickListener listener){
        this.mOnItemClickListener = listener;
    }
    class SurveyListViewHolder extends LRecyclerViewAdapter.ViewHolder{
        @BindView(R.id.survey_title)//调查主题
                TextView survey_title;
        @BindView(R.id.community_name_and_time)//小区名称和时间
                TextView community_name_and_time;
        @BindView(R.id.survey_status_flg)//调查状态
                TextView survey_status_flg;
        public SurveyListViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }
}
