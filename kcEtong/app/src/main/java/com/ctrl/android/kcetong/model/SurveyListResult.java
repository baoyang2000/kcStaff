package com.ctrl.android.kcetong.model;

import java.io.Serializable;
import java.util.List;

/**
 * Created by liu on 2017/1/20.
 */

public class SurveyListResult implements Serializable {
    private String code;
    private DataBean data;

    @Override
    public String toString() {
        return "SurveyListResult{" +
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
        private List<Survey> questionnaireList;

        @Override
        public String toString() {
            return "DataBean{" +
                   "questionnaireList=" + questionnaireList +
                   '}';
        }

        public List<Survey> getQuestionnaireList() {
            return questionnaireList;
        }

        public void setQuestionnaireList(List<Survey> questionnaireList) {
            this.questionnaireList = questionnaireList;
        }
    }
}
