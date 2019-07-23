package com.ctrl.android.kcetong.model;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2017/1/10.
 */

public class Notice implements Serializable{

    /**
     * code : 000
     * data : {"propertyNoticeList":[{"createTime":1484004986000,"currentPage":0,"index":0,"level":"0","noticeTitle":"移动一号楼收费通知","propertyNoticeId":"b1248ae1aebf4376b09e8a9d9b087c5b","rowCountPerPage":0,"status":"0"},{"createTime":1483834573000,"currentPage":0,"index":0,"level":"0","noticeTitle":"1号楼收费通知","propertyNoticeId":"e0fe7d0adef84063879ebc8aa424dea9","rowCountPerPage":0,"status":"0"}]}
     * level : Info
     * method : pm.ppt.propertyNotice.queryPropertyNoticeList
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
        private List<PropertyNoticeListBean> propertyNoticeList;

        public List<PropertyNoticeListBean> getPropertyNoticeList() {
            return propertyNoticeList;
        }

        public void setPropertyNoticeList(List<PropertyNoticeListBean> propertyNoticeList) {
            this.propertyNoticeList = propertyNoticeList;
        }

        public static class PropertyNoticeListBean {
            /**
             * createTime : 1484004986000
             * currentPage : 0
             * index : 0
             * level : 0
             * noticeTitle : 移动一号楼收费通知
             * propertyNoticeId : b1248ae1aebf4376b09e8a9d9b087c5b
             * rowCountPerPage : 0
             * status : 0
             */

            private long createTime;
            private int    currentPage;
            private int    index;
            private String level;
            private String noticeTitle;
            private String propertyNoticeId;
            private int    rowCountPerPage;
            private String status;

            public long getCreateTime() {
                return createTime;
            }

            public void setCreateTime(long createTime) {
                this.createTime = createTime;
            }

            public int getCurrentPage() {
                return currentPage;
            }

            public void setCurrentPage(int currentPage) {
                this.currentPage = currentPage;
            }

            public int getIndex() {
                return index;
            }

            public void setIndex(int index) {
                this.index = index;
            }

            public String getLevel() {
                return level;
            }

            public void setLevel(String level) {
                this.level = level;
            }

            public String getNoticeTitle() {
                return noticeTitle;
            }

            public void setNoticeTitle(String noticeTitle) {
                this.noticeTitle = noticeTitle;
            }

            public String getPropertyNoticeId() {
                return propertyNoticeId;
            }

            public void setPropertyNoticeId(String propertyNoticeId) {
                this.propertyNoticeId = propertyNoticeId;
            }

            public int getRowCountPerPage() {
                return rowCountPerPage;
            }

            public void setRowCountPerPage(int rowCountPerPage) {
                this.rowCountPerPage = rowCountPerPage;
            }

            public String getStatus() {
                return status;
            }

            public void setStatus(String status) {
                this.status = status;
            }
        }
    }
}
