package com.ctrl.android.kcetong.model;

import java.util.List;

/**
 * Created by cxl on 2017/1/22.
 */

public class RepairListBean {

    /**
     * method : pm.ppt.repairDemand.list
     * level : Info
     * code : 000
     * description : 查询成功！
     * data : {"repairList":[{"id":"1","communityId":"1","handleStatus":"0","repairNum":"201511131711641395","createTime":1447408431000,"index":0,"rowCountPerPage":0,"repairKindName":"水管报修","building":"1","unit":"1","room":"1"}]}
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
        private List<Repair> repairList;

        public List<Repair> getRepairList() {
            return repairList;
        }

        public void setRepairList(List<Repair> repairList) {
            this.repairList = repairList;
        }

    }
}
