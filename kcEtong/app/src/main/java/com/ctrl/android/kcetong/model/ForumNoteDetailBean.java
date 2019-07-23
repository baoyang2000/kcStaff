package com.ctrl.android.kcetong.model;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/1/21.
 */

public class ForumNoteDetailBean implements Serializable{

    /**
     * code : 000
     * data : {"queryPostInfo":{"categoryId":"31116d8a8e944fcb83db73502e801220","content":"过年好礼貌","createTime":1484729145000,"currentPage":0,"forumPostId":"1478a5fe517f443f85de69d893076a0c","handleStatus":"0","index":0,"isPraise":"0","memberId":"d60a446c0c6640a5877035f666d3cfdd","memberName":"习大大","modifyTime":1484729145000,"praiseNum":1,"readNum":7,"replyList":[{"count":0,"createTime":1484729157000,"memberId":"d60a446c0c6640a5877035f666d3cfdd","memberName":"习大大","postId":"1478a5fe517f443f85de69d893076a0c","postReplyId":"b530a9b76951434f8ccda364a2760970","replyContent":"图图km"}],"replyNum":1,"rowCountPerPage":0,"title":"过年好","verifyStatus":"0"}}
     * level : Info
     * method : pm.ppt.forumPost.queryForumPostDetl
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
        /**
         * queryPostInfo : {"categoryId":"31116d8a8e944fcb83db73502e801220","content":"过年好礼貌","createTime":1484729145000,"currentPage":0,"forumPostId":"1478a5fe517f443f85de69d893076a0c","handleStatus":"0","index":0,"isPraise":"0","memberId":"d60a446c0c6640a5877035f666d3cfdd","memberName":"习大大","modifyTime":1484729145000,"praiseNum":1,"readNum":7,"replyList":[{"count":0,"createTime":1484729157000,"memberId":"d60a446c0c6640a5877035f666d3cfdd","memberName":"习大大","postId":"1478a5fe517f443f85de69d893076a0c","postReplyId":"b530a9b76951434f8ccda364a2760970","replyContent":"图图km"}],"replyNum":1,"rowCountPerPage":0,"title":"过年好","verifyStatus":"0"}
         */

        private ForumNoteDetail queryPostInfo;

        public ForumNoteDetail getQueryPostInfo() {
            return queryPostInfo;
        }

        public void setQueryPostInfo(ForumNoteDetail queryPostInfo) {
            this.queryPostInfo = queryPostInfo;
        }

    }
}
