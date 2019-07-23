package com.ctrl.android.kcetong.ui.activity;

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
import com.ctrl.android.kcetong.listener.HintDialogListener;
import com.ctrl.android.kcetong.model.AppHolder;
import com.ctrl.android.kcetong.model.SurveyDetail;
import com.ctrl.android.kcetong.model.SurveyDetailResult;
import com.ctrl.android.kcetong.toolkit.util.AopUtils;
import com.ctrl.android.kcetong.toolkit.util.ConstantsData;
import com.ctrl.android.kcetong.toolkit.util.LLog;
import com.ctrl.android.kcetong.toolkit.util.S;
import com.ctrl.android.kcetong.toolkit.util.StrConstant;
import com.ctrl.android.kcetong.toolkit.util.Utils;
import com.ctrl.android.kcetong.toolkit.webutils.BaseTSubscriber;
import com.ctrl.android.kcetong.toolkit.webutils.RetrofitUtil;
import com.ctrl.android.kcetong.ui.adapter.SurveyDetailAdapter;
import com.ctrl.android.kcetong.ui.base.BaseActivity;
import com.ctrl.android.kcetong.ui.dialog.HintMessageDialog;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.ResponseBody;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class SurveyDetailActivity extends BaseActivity {

    @BindView(R.id.scroll_view)
    ScrollView scroll_view;
    @BindView(R.id.listView)
    ListView   listView;
    @BindView(R.id.survey_advice_content)//意见建议内容
    EditText   survey_advice_content;
    @BindView(R.id.tv_survey_content)
    TextView tv_survey_content;

    @BindView(R.id.submit_layout)
    LinearLayout submit_layout;
    @BindView(R.id.submit_btn)//提交按钮
    TextView     submit_btn;
    @BindView(R.id.survey_tv_title)
    TextView survey_tv_title;
    private String TITLE = "问卷调查";

    private String questionnaireId;
    private String partInFlg;//是否参与（0：未参与、1：已参与）

    private List<SurveyDetail> surveyDetailList;
    private String advice;
    private SurveyDetailAdapter surveyDetailAdapter;

    @Override
    protected void initView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_survey_detail);
        ButterKnife.bind(this);
        toolbarBaseSetting(TITLE, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SurveyDetailActivity.this.finish();
            }
        });
        scroll_view.smoothScrollTo(0, 20);//设置scrollview的起始位置在顶部
    }

    @Override
    protected void initData() {
        questionnaireId = getIntent().getStringExtra("id");
        partInFlg = getIntent().getStringExtra("partInFlg");
        survey_tv_title.setText(getIntent().getStringExtra("title"));

        surveyDetailList = new ArrayList<>();
        String proprietorId = AppHolder.getInstance().getProprietor().getProprietorId();
        showProgress(true);
        requestSurveyDetail(questionnaireId, proprietorId);
    }

    @OnClick(R.id.submit_btn)
    public void onClick() {
        if("0".equals(AppHolder.getInstance().getMemberInfo().getSupers())){
            Log.d("demo", "调查结果: " + S.getJsonStr(getAnsList(surveyDetailList)));
            String communityId = AppHolder.getInstance().getHouse().getCommunityId();
            //问卷类型（0：调查问卷、1：投票）
            String questionnaireType = "0";
            String proprietorId = AppHolder.getInstance().getProprietor().getProprietorId();
            String answerJson = S.getJsonStr(getAnsList(surveyDetailList));

            showProgress(true);
            requestSubmitSurvey(communityId, questionnaireId, questionnaireType ,proprietorId, answerJson);
        }else if("1".equals(AppHolder.getInstance().getMemberInfo().getSupers())
                 || "2".equals(AppHolder.getInstance().getMemberInfo().getSupers())){
            Utils.showShortToast(SurveyDetailActivity.this, getString(R.string.manager_cannot));
        }

    }

    /**
     * 提交调查
     * @param communityId 社区ID
     * @param questionnaireId 调查问卷或者投票ID
     * @param questionnaireType 问卷类型（0：调查问卷、1：投票）
     * @param proprietorId 业主ID
     * @param answerJson 回答内容JSON串（注：格式参照实例）
     * */
    public void requestSubmitSurvey(String communityId,String questionnaireId,String questionnaireType,
            String proprietorId,String answerJson){
        Map<String,String> map = new HashMap<String,String>();
        map.put(ConstantsData.METHOD,"pm.ppt.questionnaireMessageProprietor.reply");//方法名称
        map.putAll(ConstantsData.getSystemParams());

        map.put("communityId",communityId);
        map.put("questionnaireId",questionnaireId);
        map.put("questionnaireType",questionnaireType);
        if("0".equals(AppHolder.getInstance().getMemberInfo().getSupers())){
            map.put("proprietorId",proprietorId);
        }

        map.put("answerJson",answerJson);

        String sign = AopUtils.sign(map, ConstantsData.SECRET_VALUE);
        map.put("sign",sign);
        LLog.d(map+"");
        map.remove(ConstantsData.METHOD);
        RetrofitUtil.Api().requestSubmitSurvey(map).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseTSubscriber<ResponseBody>(this) {
                    @Override
                    public void onResponseCallback(JSONObject response, String resultCode) {
                        LLog.d(response+"");
                        showProgress(false);
                        try {
                            if(TextUtils.equals("000",response.getString("code"))){
                                final HintMessageDialog hintDialog = new HintMessageDialog(SurveyDetailActivity.this);
                                hintDialog.showDefaultDialogOneButton("提示", "感谢您的参与! ", new HintDialogListener() {
                                    @Override
                                    public void submitListener() {
                                        hintDialog.dismiss();
                                        Intent intent = new Intent();
                                        setResult(RESULT_OK,intent);
                                        SurveyDetailActivity.this.finish();
                                    }
                                    @Override
                                    public void cancelListener() {
                                        hintDialog.dismiss();
                                        Intent intent = new Intent();
                                        setResult(RESULT_OK,intent);
                                        SurveyDetailActivity.this.finish();
                                    }
                                },getResources().getString(R.string.app_back),false);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
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
     * 调查详细列表
     * @param questionnaireId 调查问卷或者投票ID
     * @param proprietorId 业主ID
     * */
    public void requestSurveyDetail(String questionnaireId,String proprietorId){
        Map<String,String> map = new HashMap<String,String>();
        map.put(ConstantsData.METHOD,"pm.ppt.questionnaire.get");//方法名称
        map.putAll(ConstantsData.getSystemParams());

        map.put("questionnaireId",questionnaireId);
        if("0".equals(AppHolder.getInstance().getMemberInfo().getSupers())){
            map.put("proprietorId",proprietorId);
        }

        String sign = AopUtils.sign(map, ConstantsData.SECRET_VALUE);
        map.put("sign",sign);
        LLog.d(map+"");
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

                            for(SurveyDetail surveyDetail : surveyDetailResult.getData().getQuestionnaireMessageList()){
                                surveyDetailList.add(surveyDetail);
                            }
//                            surveyDetailList.add(surveyDetailResult.getData().getQuestionnaireMessageList().get(0));
                            advice = surveyDetailResult.getData().getAdvice();

                            surveyDetailAdapter = new SurveyDetailAdapter(SurveyDetailActivity.this);
                            surveyDetailAdapter.setPartInFlg(partInFlg);
                            surveyDetailAdapter.setList(surveyDetailList);
                            listView.setAdapter(surveyDetailAdapter);

                            if(partInFlg.equals("0")){
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
                            Utils.toastError(SurveyDetailActivity.this, "暂无数据");
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
                stra = "";
            }

            String strb;
            if(listSurveyDetail.get(i).getAnswerB() == 1){
                strb = "2";
            } else {
                strb = "";
            }

            String strc;
            if(listSurveyDetail.get(i).getAnswerC() == 1){
                strc = "3";
            } else {
                strc = "";
            }

            Map<String,String> map = new HashMap<>();
            map.put(StrConstant.MESSAGEID,listSurveyDetail.get(i).getId());//问题编号
            map.put(StrConstant.OPTIONNUM,(stra + strb + strc));//答案
            list.add(map);
        }
        return list;
    }

}
