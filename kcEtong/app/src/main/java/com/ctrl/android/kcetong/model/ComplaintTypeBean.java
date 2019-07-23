package com.ctrl.android.kcetong.model;

import java.util.List;

/**
 * Created by cxl on 2017/1/16.
 */

public class ComplaintTypeBean {


    /**
     * method : pm.common.kind.list
     * level : Info
     * code : 000
     * description : 查询成功！
     * data : {"kindList":[{"id":"45909a36a743484cb075d57c930b1550","kindKey":"CPT","kindName":"环境卫生","sortNum":1,"createTime":1446862251000,"remark":"环境卫生类","disabled":"0"},{"id":"163a4a7a97b44cb08efaf2f16ba92a2d","kindKey":"CPT","kindName":"公共安全","sortNum":2,"createTime":1446862228000,"remark":"公共安全类","disabled":"0"},{"id":"2","kindKey":"CPT","kindName":"车辆管理","sortNum":3,"createTime":1445398572000,"remark":"车辆管理","disabled":"0"},{"id":"1","kindKey":"CPT","kindName":"外来人员","sortNum":4,"createTime":1445398527000,"remark":"外来人员","disabled":"0"},{"id":"90a0ebd7a31e41669a9b7500cc7737b1","kindKey":"CPT","kindName":"物业员工","sortNum":5,"createTime":1451282497000,"remark":"针对物业员工的投诉","disabled":"0"},{"id":"68498ec57c214229b8ac3559c41fd86f","kindKey":"CPT","kindName":"其他业主","sortNum":6,"createTime":1451282544000,"remark":"针对其他业主的投诉","disabled":"0"}]}
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
        private List<KindListBean> kindList;

        public List<KindListBean> getKindList() {
            return kindList;
        }

        public void setKindList(List<KindListBean> kindList) {
            this.kindList = kindList;
        }

        public static class KindListBean {
            /**
             * id : 45909a36a743484cb075d57c930b1550
             * kindKey : CPT
             * kindName : 环境卫生
             * sortNum : 1
             * createTime : 1446862251000
             * remark : 环境卫生类
             * disabled : 0
             */

            private String id;
            private String kindKey;
            private String kindName;
            private int    sortNum;
            private long   createTime;
            private String remark;
            private String disabled;

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public String getKindKey() {
                return kindKey;
            }

            public void setKindKey(String kindKey) {
                this.kindKey = kindKey;
            }

            public String getKindName() {
                return kindName;
            }

            public void setKindName(String kindName) {
                this.kindName = kindName;
            }

            public int getSortNum() {
                return sortNum;
            }

            public void setSortNum(int sortNum) {
                this.sortNum = sortNum;
            }

            public long getCreateTime() {
                return createTime;
            }

            public void setCreateTime(long createTime) {
                this.createTime = createTime;
            }

            public String getRemark() {
                return remark;
            }

            public void setRemark(String remark) {
                this.remark = remark;
            }

            public String getDisabled() {
                return disabled;
            }

            public void setDisabled(String disabled) {
                this.disabled = disabled;
            }
        }
    }
}
