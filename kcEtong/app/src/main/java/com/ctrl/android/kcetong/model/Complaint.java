package com.ctrl.android.kcetong.model;

import java.util.List;

/**
 * Created by cxl on 2017/1/13.
 */

public class Complaint {


    /**
     * method : pm.ppt.complaint.list
     * level : Info
     * code : 000
     * description : 查询成功！
     * data : {"complaintList":[{"id":"3","communityId":"1","handleStatus":"0","complaintNum":"1","createTime":1445421695000,"index":0,"rowCountPerPage":0,"complaintKindName":"水暖维修","building":"3","unit":"3","room":"3"},{"id":"7","handleStatus":"0","index":0,"rowCountPerPage":0},{"id":"2","handleStatus":"0","index":0,"rowCountPerPage":0},{"id":"9ab230f9fb8d4047bac28952f83c44b5","handleStatus":"0","index":0,"rowCountPerPage":0},{"id":"6af8586dbedb49f7bf490e35c7ed658f","handleStatus":"0","index":0,"rowCountPerPage":0}]}
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
        private List<ComplaintListBean> complaintList;

        public List<ComplaintListBean> getComplaintList() {
            return complaintList;
        }

        public void setComplaintList(List<ComplaintListBean> complaintList) {
            this.complaintList = complaintList;
        }

        public static class ComplaintListBean {
            /**
             * id : 3
             * communityId : 1
             * handleStatus : 0
             * complaintNum : 1
             * createTime : 1445421695000
             * index : 0
             * rowCountPerPage : 0
             * complaintKindName : 水暖维修
             * building : 3
             * unit : 3
             * room : 3
             */

            private String id;
            private String communityId;
            private String handleStatus;
            private String complaintNum;
            private String   createTime;
            private int    index;
            private int    rowCountPerPage;
            private String complaintKindName;
            private String building;
            private String unit;
            private String room;
            private String communityName;

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public String getCommunityId() {
                return communityId;
            }

            public void setCommunityId(String communityId) {
                this.communityId = communityId;
            }

            public String getHandleStatus() {
                return handleStatus;
            }

            public void setHandleStatus(String handleStatus) {
                this.handleStatus = handleStatus;
            }

            public String getComplaintNum() {
                return complaintNum;
            }

            public void setComplaintNum(String complaintNum) {
                this.complaintNum = complaintNum;
            }

            public String getCreateTime() {
                return createTime;
            }

            public void setCreateTime(String createTime) {
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

            public String getComplaintKindName() {
                return complaintKindName;
            }

            public void setComplaintKindName(String complaintKindName) {
                this.complaintKindName = complaintKindName;
            }

            public String getBuilding() {
                return building;
            }

            public void setBuilding(String building) {
                this.building = building;
            }

            public String getUnit() {
                return unit;
            }

            public void setUnit(String unit) {
                this.unit = unit;
            }

            public String getRoom() {
                return room;
            }

            public void setRoom(String room) {
                this.room = room;
            }

            public String getCommunityName() {
                return communityName;
            }

            public void setCommunityName(String communityName) {
                this.communityName = communityName;
            }
        }
    }
}
