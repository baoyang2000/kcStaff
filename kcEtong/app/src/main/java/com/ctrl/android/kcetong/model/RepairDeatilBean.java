package com.ctrl.android.kcetong.model;

import java.util.List;

/**
 * Created by cxl on 2017/1/22.
 */

public class RepairDeatilBean {

    /**
     * method : pm.ppt.RepairDemand.RepairDemandInformation
     * level : Info
     * code : 000
     * description : 查询成功！
     * data : {"repairDemandPicList":null,"repairDemandInfo":{"communityId":"1","handleStatus":"0","evaluateLevel":"3","createTime":1447408431000,"content":"聊","index":0,"rowCountPerPage":0,"repairKindName":"水管报修","building":"1","unit":"1","room":"1"},"repairDemandResultPicList":null}
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
         * repairDemandPicList : null
         * repairDemandInfo : {"communityId":"1","handleStatus":"0","evaluateLevel":"3","createTime":1447408431000,"content":"聊","index":0,"rowCountPerPage":0,"repairKindName":"水管报修","building":"1","unit":"1","room":"1"}
         * repairDemandResultPicList : null
         */

        private List<GoodPic> repairDemandPicList;
        private Repair        repairDemandInfo;
        private List<GoodPic> repairDemandResultPicList;

        public List<GoodPic> getRepairDemandPicList() {
            return repairDemandPicList;
        }

        public void setRepairDemandPicList(List<GoodPic> repairDemandPicList) {
            this.repairDemandPicList = repairDemandPicList;
        }

        public Repair getRepairDemandInfo() {
            return repairDemandInfo;
        }

        public void setRepairDemandInfo(Repair repairDemandInfo) {
            this.repairDemandInfo = repairDemandInfo;
        }

        public List<GoodPic> getRepairDemandResultPicList() {
            return repairDemandResultPicList;
        }

        public void setRepairDemandResultPicList(List<GoodPic> repairDemandResultPicList) {
            this.repairDemandResultPicList = repairDemandResultPicList;
        }
    }
}
