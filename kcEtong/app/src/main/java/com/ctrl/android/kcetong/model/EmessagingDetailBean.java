package com.ctrl.android.kcetong.model;

/**
 * Created by cxl on 2017/2/6.
 */

public class EmessagingDetailBean {

    private String                        code;
    private EmessagingDetailBean.DataBean data;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public EmessagingDetailBean.DataBean getData() {
        return data;
    }

    public void setData(EmessagingDetailBean.DataBean data) {
        this.data = data;
    }

    public static class DataBean {

        private Problem questionAnswer;

        @Override
        public String toString() {
            return "DataBean{" +
                   "questionAnswer=" + questionAnswer +
                   '}';
        }

        public Problem getQuestionAnswer() {
            return questionAnswer;
        }

        public void setQuestionAnswer(Problem questionAnswer) {
            this.questionAnswer = questionAnswer;
        }
    }
}
