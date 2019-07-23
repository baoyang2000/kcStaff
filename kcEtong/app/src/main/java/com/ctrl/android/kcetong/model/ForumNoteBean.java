package com.ctrl.android.kcetong.model;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2017/1/20.
 */

public class ForumNoteBean implements Serializable {

    /**
     * code : 000
     * data : {"forumPostList":[{"categoryId":"31116d8a8e944fcb83db73502e801220","content":"123456","createTime":1484796469000,"currentPage":0,"forumPostId":"959a0df30b0b43e7929dc7c0c89e5396","handleStatus":"0","index":0,"memberId":"d60a446c0c6640a5877035f666d3cfdd","memberName":"习大大","modifyTime":1484796469000,"praiseNum":0,"readNum":0,"replyNum":0,"rowCountPerPage":0,"title":"帖子","verifyStatus":"0"}]}
     * level : Info
     * method : pm.ppt.forumPost.queryForumPostList
     */

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
        private List<ForumNote> forumPostList;

        public List<ForumNote> getForumPostList() {
            return forumPostList;
        }

        public void setForumPostList(List<ForumNote> forumPostList) {
            this.forumPostList = forumPostList;
        }


    }
}
