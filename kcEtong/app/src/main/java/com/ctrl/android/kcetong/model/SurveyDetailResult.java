package com.ctrl.android.kcetong.model;

import java.io.Serializable;
import java.util.List;

/**
 * Created by liu on 2017/1/20.
 */

public class SurveyDetailResult implements Serializable {
    private String code;
    private DataBean data;

    @Override
    public String toString() {
        return "SurveyDetailResult{" +
               "code='" + code + '\'' +
               ", data=" + data +
               '}';
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean{
        private List<SurveyDetail> questionnaireMessageList;
        private String advice;

        @Override
        public String toString() {
            return "DataBean{" +
                   "questionnaireMessageList=" + questionnaireMessageList +
                   ", advice='" + advice + '\'' +
                   '}';
        }

        public List<SurveyDetail> getQuestionnaireMessageList() {
            return questionnaireMessageList;
        }

        public void setQuestionnaireMessageList(List<SurveyDetail> questionnaireMessageList) {
            this.questionnaireMessageList = questionnaireMessageList;
        }

        public String getAdvice() {
            return advice;
        }

        public void setAdvice(String advice) {
            this.advice = advice;
        }
    }
}
