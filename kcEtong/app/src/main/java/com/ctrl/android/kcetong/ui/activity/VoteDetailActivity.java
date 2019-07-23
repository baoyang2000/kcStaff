package com.ctrl.android.kcetong.ui.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.ctrl.android.kcetong.R;
import com.ctrl.android.kcetong.model.AppHolder;
import com.ctrl.android.kcetong.model.SubmitVoteResult;
import com.ctrl.android.kcetong.model.SurveyDetail;
import com.ctrl.android.kcetong.model.SurveyDetailResult;
import com.ctrl.android.kcetong.toolkit.util.AopUtils;
import com.ctrl.android.kcetong.toolkit.util.ConstantsData;
import com.ctrl.android.kcetong.toolkit.util.LLog;
import com.ctrl.android.kcetong.toolkit.util.MessageUtils;
import com.ctrl.android.kcetong.toolkit.util.S;
import com.ctrl.android.kcetong.toolkit.util.StrConstant;
import com.ctrl.android.kcetong.toolkit.util.Utils;
import com.ctrl.android.kcetong.toolkit.webutils.BaseTSubscriber;
import com.ctrl.android.kcetong.toolkit.webutils.RetrofitUtil;
import com.ctrl.android.kcetong.ui.adapter.VoteDetailAdapter;
import com.ctrl.android.kcetong.ui.base.BaseActivity;
import com.ctrl.android.kcetong.ui.dialog.AlertChartDialog;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class VoteDetailActivity extends BaseActivity {
    @BindView(R.id.scroll_view)
    ScrollView scroll_view;
    @BindView(R.id.listView)
    ListView   listView;
    @BindView(R.id.survey_advice_content)//意见建议内容
    EditText   survey_advice_content;
    @BindView(R.id.vote_tv_title)
    TextView vote_tv_title;
    @BindView(R.id.submit_layout)
    LinearLayout submit_layout;
    @BindView(R.id.submit_btn)//提交按钮
    TextView     submit_btn;
    @BindView(R.id.tv_survey_content)
    TextView tv_survey_content;

    /**
     * 选项A 数量
     */
    private String choice_num_a   = "56";
    /**
     * 选项A 名称
     */
    private String choice_title_a = "选项A";
    /**
     * 选项B 数量
     */
    private String choice_num_b   = "89";

    /**
     * 选项B 名称
     */
    private String choice_title_b = "选项B";
    /**
     * 选项C 数量
     */
    private String choice_num_c   = "33";
    /**
     * 选项C 名称
     */
    private String choice_title_c = "选项C";

    private String TITLE = "投票";

    private String questionnaireId;
    private String partInFlg;//是否参与（0：未参与、1：已参与）
    private String position;

    private List<SurveyDetail> surveyDetailList;
    private String             advice;
    private VoteDetailAdapter  voteDetailAdapter;

    @Override
    protected void initView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_vote_detail);
        ButterKnife.bind(this);
        toolbarBaseSetting(TITLE, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                VoteDetailActivity.this.finish();
            }
        });
        scroll_view.smoothScrollTo(0, 20);//设置scrollview的起始位置在顶部
    }

    @Override
    protected void initData() {
        questionnaireId = getIntent().getStringExtra("id");
        partInFlg = getIntent().getStringExtra("partInFlg");
        vote_tv_title.setText(getIntent().getStringExtra("title"));
        position = getIntent().getStringExtra("position");
        String proprietorId = AppHolder.getInstance().getProprietor().getProprietorId();

        surveyDetailList = new ArrayList<>();
        showProgress(true);
        requestSurveyDetail(questionnaireId, proprietorId);
    }

    @OnClick(R.id.submit_btn)
    public void onClick() {
        if("0".equals(AppHolder.getInstance().getMemberInfo().getSupers())){
            Log.d("demo", "调查结果: " + S.getJsonStr(getAnsList(surveyDetailList)));

            boolean flg = true;

            if(surveyDetailList != null && surveyDetailList.size() > 0){
                for(int i = 0 ; i < surveyDetailList.size() ; i ++){
                    SurveyDetail s = surveyDetailList.get(i);
                    if(s.getAnswerA() == 1 || s.getAnswerB() == 1 || s.getAnswerC() == 1){
                        flg = true;
                    } else {
                        flg = false;
                        break;
                    }
                }
            }

            if(flg == false){
                MessageUtils.showShortToast(this,"有问题尚未选择");
            } else {
                String communityId = AppHolder.getInstance().getHouse().getCommunityId();
                //String questionnaireId =
                //问卷类型（0：调查问卷、1：投票）
                String questionnaireType = "1";
                String proprietorId = AppHolder.getInstance().getProprietor().getProprietorId();
                String answerJson = S.getJsonStr(getAnsList(surveyDetailList));
                showProgress(true);
                requestSubmitVote(communityId,questionnaireId,questionnaireType,proprietorId,answerJson);
            }
        }else if("1".equals(AppHolder.getInstance().getMemberInfo().getSupers())
                 || "2".equals(AppHolder.getInstance().getMemberInfo().getSupers())){
            Utils.showShortToast(VoteDetailActivity.this, getString(R.string.manager_cannot));
        }

    }

    /**
     * 提交投票
     * @param communityId 社区ID
     * @param questionnaireId 调查问卷或者投票ID
     * @param questionnaireType 问卷类型（0：调查问卷、1：投票）
     * @param proprietorId 业主ID
     * @param answerJson 回答内容JSON串（注：格式参照实例）
     * */
    public void requestSubmitVote(String communityId,String questionnaireId,String questionnaireType,
            String proprietorId,String answerJson){
        Map<String,String> map = new HashMap<String,String>();
        map.put(ConstantsData.METHOD,"pm.ppt.questionnaireMessageProprietor.reply");//方法名称
        map.putAll(ConstantsData.getSystemParams());

        map.put("communityId",communityId);
        map.put("questionnaireId",questionnaireId);
        map.put("questionnaireType",questionnaireType);
        map.put("proprietorId",proprietorId);
        map.put("answerJson",answerJson);
        map.put("advice",survey_advice_content.getText().toString());

        String sign = AopUtils.sign(map, ConstantsData.SECRET_VALUE);
        map.put("sign",sign);
        LLog.d(map+"");
        map.remove(ConstantsData.METHOD);

        RetrofitUtil.Api().requestSubmitVote(map).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseTSubscriber<SubmitVoteResult>(this) {
                    @Override
                    public void onResponseCallback(JSONObject response, String resultCode) {
                        LLog.d(response+"");
                        showProgress(false);
                    }
                    @Override
                    public void onNext(SubmitVoteResult submitVoteResult) {
                        super.onNext(submitVoteResult);
                        showProgress(false);
                        if(TextUtils.equals("000",submitVoteResult.getCode())){
                            choice_num_a = submitVoteResult.getData().getOptionASum();
                            choice_title_a = submitVoteResult.getData().getOptionA();
                            if(S.isNull(choice_num_a)){
                                choice_num_a = "0";
                            }
                            choice_num_b = submitVoteResult.getData().getOptionBSum();
                            choice_title_b = submitVoteResult.getData().getOptionB();
                            if(S.isNull(choice_num_b)){
                                choice_num_b = "0";
                            }
                            choice_num_c = submitVoteResult.getData().getOptionCSum();
                            choice_title_c = submitVoteResult.getData().getOptionC();
                            if(S.isNull(choice_num_c)){
                                choice_num_c = "0";
                            }
                            showAlertChartDialog();
                        }
                    }

                    @Override
                    public void onNetError(Throwable e) {
                        super.onNetError(e);
                        showProgress(false);
                    }
                });
    }

    /**
     * 弹出自定义弹出框
     * */
    public void showAlertChartDialog() {

        AlertChartDialog.Builder builder = new AlertChartDialog.Builder(this);
        builder.setCancleFlg(false);

        builder.setWidth((Utils.getDisplayWidth(this)));
        builder.setHeight((Utils.getDisplayHeight(this)));

        builder.setChoice_num_a(choice_num_a);
        builder.setChoice_title_a(choice_title_a);
        builder.setChoice_num_b(choice_num_b);
        builder.setChoice_title_b(choice_title_b);
        builder.setChoice_num_c(choice_num_c);
        builder.setChoice_title_c(choice_title_c);

        builder.setMessage("感谢您的投票");
        builder.setReturnButton(getResources().getString(R.string.app_back),new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                Intent intent = new Intent();
                intent.putExtra("position", position);
                setResult(RESULT_OK,intent);
                finish();
            }
        });
        builder.create().show();
    }
    /**
     * 调查详细列表
     * @param questionnaireId 调查问卷或者投票ID
     * @param proprietorId 业主ID
     * */
    public void requestSurveyDetail(String questionnaireId,String proprietorId){
        Map<String,String> map = new HashMap<>();
        map.put(ConstantsData.METHOD,"pm.ppt.questionnaire.get");//方法名称
        map.putAll(ConstantsData.getSystemParams());

        map.put("questionnaireId",questionnaireId);
        if("0".equals(holder.getMemberInfo().getSupers())){
            map.put("proprietorId",proprietorId);
        }

        String sign = AopUtils.sign(map, ConstantsData.SECRET_VALUE);
        map.put("sign",sign);
        LLog.d(map + "");
        map.remove(ConstantsData.METHOD);
        RetrofitUtil.Api().requestSurveyDetail(map).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new BaseTSubscriber<SurveyDetailResult>(this) {
                        @Override
                        public void onResponseCallback(JSONObject response, String resultCode) {
                            LLog.d(response+"");
                            showProgress(false);
                        }

                        @Override
                        public void onNext(SurveyDetailResult surveyDetailResult) {
                            super.onNext(surveyDetailResult);

                            if(TextUtils.equals("000",surveyDetailResult.getCode())){
                                survey_advice_content.setVisibility(View.VISIBLE);
                                submit_layout.setVisibility(View.VISIBLE);

                                surveyDetailList.add(surveyDetailResult.getData().getQuestionnaireMessageList().get(0));
                                advice = surveyDetailResult.getData().getAdvice();

                                voteDetailAdapter = new VoteDetailAdapter(VoteDetailActivity.this);
                                voteDetailAdapter.setPartInFlg(partInFlg);
                                voteDetailAdapter.setList(surveyDetailList);
                                listView.setAdapter(voteDetailAdapter);

                                //是否参与（0：未参与、1：已参与）
                                if(partInFlg.equals("0")){
                                    //
                                    survey_advice_content.setVisibility(View.VISIBLE);
                                    tv_survey_content.setVisibility(View.GONE);
                                } else {
                                    survey_advice_content.setVisibility(View.GONE);
                                    tv_survey_content.setVisibility(View.VISIBLE);
                                    survey_advice_content.setEnabled(false);
                                    tv_survey_content.setText(surveyDetailResult.getData().getAdvice());
                                    submit_btn.setEnabled(false);
                                    submit_btn.setClickable(false);
                                    submit_btn.setBackgroundResource(R.drawable.gray_bg_shap);
                                }
                            }else if(TextUtils.equals("002", surveyDetailResult.getCode())){
                                Utils.toastError(VoteDetailActivity.this, "暂无数据");
                            }
                            showProgress(false);
                        }

                        @Override
                        public void onNetError(Throwable e) {
                            super.onNetError(e);
                            showProgress(false);
                        }
                    });

    }

    /**
     * 根据问题生成答案列表
     * */
    private List<Map<String,String>> getAnsList(List<SurveyDetail> listSurveyDetail){
        List<Map<String,String>> list= new ArrayList<>();
        for(int i = 0 ; i < listSurveyDetail.size() ; i ++){

            //0：未选中、1：选中
            String stra;
            if(listSurveyDetail.get(i).getAnswerA() == 1){
                stra = "1";
            } else {
                stra = "0";
            }

            String strb;
            if(listSurveyDetail.get(i).getAnswerB() == 1){
                strb = "1";
            } else {
                strb = "0";
            }
            String strc;
            if(listSurveyDetail.get(i).getAnswerC() == 1){
                strc = "1";
            } else {
                strc = "0";
            }

            Map<String,String> map = new HashMap<>();
            map.put(StrConstant.MESSAGEID,listSurveyDetail.get(i).getId());//问题编号
            map.put(StrConstant.OPTIONNUM,(stra + strb + strc));//答案
            list.add(map);
        }
        return list;
    }
}
