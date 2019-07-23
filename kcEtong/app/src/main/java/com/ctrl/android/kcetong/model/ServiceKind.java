package com.ctrl.android.kcetong.model;

/**
 * 特约服务类别
 * Created by Administrator on 2015/10/29.
 */
public class ServiceKind {
    private String id;//id
    private String communityId;//社区id
    private String sortsName;//服务类别名称
    private String sortsUrl;//类别图片url
    private String sortNum;//顺序号
    private String remark;//备注
    private String createTime;//创建时间

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

    public String getSortsName() {
        return sortsName;
    }

    public void setSortsName(String sortsName) {
        this.sortsName = sortsName;
    }

    public String getSortsUrl() {
        return sortsUrl;
    }

    public void setSortsUrl(String sortsUrl) {
        this.sortsUrl = sortsUrl;
    }

    public String getSortNum() {
        return sortNum;
    }

    public void setSortNum(String sortNum) {
        this.sortNum = sortNum;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }
}
