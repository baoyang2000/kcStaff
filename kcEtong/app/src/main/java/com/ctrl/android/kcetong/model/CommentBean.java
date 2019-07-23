package com.ctrl.android.kcetong.model;

import java.util.List;

/**
 * Created by liu on 2018/3/7.
 */

public class CommentBean {

    /**
     * method : pm.ppt.orderEvaluation.list
     * level : Info
     * code : 000
     * description : 查询成功！
     * data : {"evaluationList":[{"id":"07448e058d7845aaa702b8888a812faf","nickName":" ","level":"1","content":"哈哈哈哈","createTime":1520326003000,"index":0,"rowCountPerPage":0},{"id":"9907e2ac66c346128a105135175325b1","nickName":" ","level":"1","content":"国产剧剧剧ｖｈ的屠夫公交车共产国际头发屠夫公交车共产国际","createTime":1520236048000,"index":0,"rowCountPerPage":0}],"evaluatRate":"100"}
     */

    private String method;
    private String   level;
    private String   code;
    private String   description;
    private DataBean data;

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * evaluationList : [{"id":"07448e058d7845aaa702b8888a812faf","nickName":" ","level":"1","content":"哈哈哈哈","createTime":1520326003000,"index":0,"rowCountPerPage":0},{"id":"9907e2ac66c346128a105135175325b1","nickName":" ","level":"1","content":"国产剧剧剧ｖｈ的屠夫公交车共产国际头发屠夫公交车共产国际","createTime":1520236048000,"index":0,"rowCountPerPage":0}]
         * evaluatRate : 100
         */

        private String evaluatRate;
        private List<EvaluationListBean> evaluationList;

        public String getEvaluatRate() {
            return evaluatRate;
        }

        public void setEvaluatRate(String evaluatRate) {
            this.evaluatRate = evaluatRate;
        }

        public List<EvaluationListBean> getEvaluationList() {
            return evaluationList;
        }

        public void setEvaluationList(List<EvaluationListBean> evaluationList) {
            this.evaluationList = evaluationList;
        }

        public static class EvaluationListBean {
            /**
             * id : 07448e058d7845aaa702b8888a812faf
             * nickName :
             * level : 1
             * content : 哈哈哈哈
             * createTime : 1520326003000
             * index : 0
             * rowCountPerPage : 0
             */

            private String id;
            private String nickName;
            private String level;
            private String content;
            private long   createTime;
            private int    index;
            private int    rowCountPerPage;

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public String getNickName() {
                return nickName;
            }

            public void setNickName(String nickName) {
                this.nickName = nickName;
            }

            public String getLevel() {
                return level;
            }

            public void setLevel(String level) {
                this.level = level;
            }

            public String getContent() {
                return content;
            }

            public void setContent(String content) {
                this.content = content;
            }

            public long getCreateTime() {
                return createTime;
            }

            public void setCreateTime(long createTime) {
                this.createTime = createTime;
            }

            public int getIndex() {
                return index;
            }

            public void setIndex(int index) {
                this.index = index;
            }

            public int getRowCountPerPage() {
                return rowCountPerPage;
            }

            public void setRowCountPerPage(int rowCountPerPage) {
                this.rowCountPerPage = rowCountPerPage;
            }
        }
    }
}
