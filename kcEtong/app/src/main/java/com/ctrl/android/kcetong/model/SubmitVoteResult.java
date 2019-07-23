package com.ctrl.android.kcetong.model;

import java.io.Serializable;

/**
 * Created by liu on 2017/1/21.
 */

public class SubmitVoteResult implements Serializable {
    private String code;
    private DataBean data;

    @Override
    public String toString() {
        return "SubmitVoteResult{" +
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
        private String optionASum;
        private String optionBSum;
        private String optionCSum;
        private String optionA;
        private String optionB;
        private String optionC;

        @Override
        public String toString() {
            return "DataBean{" +
                   "optionASum='" + optionASum + '\'' +
                   ", optionBSum='" + optionBSum + '\'' +
                   ", optionCSum='" + optionCSum + '\'' +
                   ", optionA='" + optionA + '\'' +
                   ", optionB='" + optionB + '\'' +
                   ", optionC='" + optionC + '\'' +
                   '}';
        }

        public String getOptionASum() {
            return optionASum;
        }

        public void setOptionASum(String optionASum) {
            this.optionASum = optionASum;
        }

        public String getOptionBSum() {
            return optionBSum;
        }

        public void setOptionBSum(String optionBSum) {
            this.optionBSum = optionBSum;
        }

        public String getOptionCSum() {
            return optionCSum;
        }

        public void setOptionCSum(String optionCSum) {
            this.optionCSum = optionCSum;
        }

        public String getOptionA() {
            return optionA;
        }

        public void setOptionA(String optionA) {
            this.optionA = optionA;
        }

        public String getOptionB() {
            return optionB;
        }

        public void setOptionB(String optionB) {
            this.optionB = optionB;
        }

        public String getOptionC() {
            return optionC;
        }

        public void setOptionC(String optionC) {
            this.optionC = optionC;
        }
    }
}
