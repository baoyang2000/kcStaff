package com.ctrl.android.kcetong.model;

import java.util.List;

/**
 * Created by cxl on 2017/1/20.
 */

public class ComplaintDeatilBean {

    /**
     * method : pm.ppt.complaint.get
     * level : Info
     * code : 000
     * description : 查询成功！
     * data : {"complaintResultPicList":null,"complaintPicList":null,"complaintInfo":{"communityId":"1","evaluateLevel":"1","evaluateContent":"很好，处理得当","result":"完成","createTime":1445319744000,"content":"3发的啥办法","index":0,"rowCountPerPage":0,"complaintKindName":"水暖维修","hasEvaluate":"1","building":"1","unit":"1","room":"1"}}
     */

    private String   method;
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
         * complaintResultPicList : null
         * complaintPicList : null
         * complaintInfo : {"communityId":"1","evaluateLevel":"1","evaluateContent":"很好，处理得当","result":"完成","createTime":1445319744000,"content":"3发的啥办法","index":0,"rowCountPerPage":0,"complaintKindName":"水暖维修","hasEvaluate":"1","building":"1","unit":"1","room":"1"}
         */

        private List<Img>       complaintResultPicList;
        private List<GoodPic>   complaintPicList;
        private ComplaintDeatil complaintInfo;

        public List<Img> getComplaintResultPicList() {
            return complaintResultPicList;
        }

        public void setComplaintResultPicList(List<Img> complaintResultPicList) {
            this.complaintResultPicList = complaintResultPicList;
        }

        public List<GoodPic> getComplaintPicList() {
            return complaintPicList;
        }

        public void setComplaintPicList(List<GoodPic> complaintPicList) {
            this.complaintPicList = complaintPicList;
        }

        public ComplaintDeatil getComplaintInfo() {
            return complaintInfo;
        }

        public void setComplaintInfo(ComplaintDeatil complaintInfo) {
            this.complaintInfo = complaintInfo;
        }
    }
}
