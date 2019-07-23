package com.ctrl.android.kcetong.model;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2017/1/20.
 */

public class ForumCategoryBean implements Serializable {



    private String code;
    private DataBean data;
    private String   level;
    private String   method;

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

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public static class DataBean {
        private List<ForumCategory> forumCategoryList;

        public List<ForumCategory> getForumCategoryList() {
            return forumCategoryList;
        }

        public void setForumCategoryList(List<ForumCategory> forumCategoryList) {
            this.forumCategoryList = forumCategoryList;
        }


    }
}
