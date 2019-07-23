package com.ctrl.android.kcetong.model;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2017/1/19.
 */

public class ManagmentBean implements Serializable {
    private String                 code;
    private ManagmentBean.DataBean data;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public ManagmentBean.DataBean getData() {
        return data;
    }

    public void setData(ManagmentBean.DataBean data) {
        this.data = data;
    }

    public static class DataBean {

        public List<Problem> getProblemList() {
            return problemList;
        }

        public void setProblemList(List<Problem> problemList) {
            this.problemList = problemList;
        }

        private List<Problem> problemList;

    }
}
