package com.ctrl.android.kcetong.model;

/**
 * 特约服务详情
 * Created by Administrator on 2015/10/29.
 */
public class ServiceProduct {
    private String id;//服务id
    private String serviceKindId;//类别id
    private String serviceName;//服务名称
    private String serviceUrl;//服务图片
    private String originalPrice;//原价
    private String sellingPrice;//卖价
    private String specification;//规格单位
    private String stock;//库存
    private String infomation;//服务介绍详情
    private String isAdded;//上架/下架（0：下架、1：上架）
    private String sortNum;//顺序号
    private String salesVolume;//销量
    private String disabled;
    private String createTime;
    private String modifyTime;

    private String serviceTime;//服务时间

    @Override
    public String toString() {
        return "ServiceProduct{" +
               "id='" + id + '\'' +
               ", serviceKindId='" + serviceKindId + '\'' +
               ", serviceName='" + serviceName + '\'' +
               ", serviceUrl='" + serviceUrl + '\'' +
               ", originalPrice='" + originalPrice + '\'' +
               ", sellingPrice='" + sellingPrice + '\'' +
               ", specification='" + specification + '\'' +
               ", stock='" + stock + '\'' +
               ", infomation='" + infomation + '\'' +
               ", isAdded='" + isAdded + '\'' +
               ", sortNum='" + sortNum + '\'' +
               ", salesVolume='" + salesVolume + '\'' +
               ", disabled='" + disabled + '\'' +
               ", createTime='" + createTime + '\'' +
               ", modifyTime='" + modifyTime + '\'' +
               ", serviceTime='" + serviceTime + '\'' +
               '}';
    }

    public String getDisabled() {
        return disabled;
    }

    public void setDisabled(String disabled) {
        this.disabled = disabled;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getModifyTime() {
        return modifyTime;
    }

    public void setModifyTime(String modifyTime) {
        this.modifyTime = modifyTime;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getServiceKindId() {
        return serviceKindId;
    }

    public void setServiceKindId(String serviceKindId) {
        this.serviceKindId = serviceKindId;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public String getServiceUrl() {
        return serviceUrl;
    }

    public void setServiceUrl(String serviceUrl) {
        this.serviceUrl = serviceUrl;
    }

    public String getOriginalPrice() {
        return originalPrice;
    }

    public void setOriginalPrice(String originalPrice) {
        this.originalPrice = originalPrice;
    }

    public String getSellingPrice() {
        return sellingPrice;
    }

    public void setSellingPrice(String sellingPrice) {
        this.sellingPrice = sellingPrice;
    }

    public String getSpecification() {
        return specification;
    }

    public void setSpecification(String specification) {
        this.specification = specification;
    }

    public String getStock() {
        return stock;
    }

    public void setStock(String stock) {
        this.stock = stock;
    }

    public String getInfomation() {
        return infomation;
    }

    public void setInfomation(String infomation) {
        this.infomation = infomation;
    }

    public String getIsAdded() {
        return isAdded;
    }

    public void setIsAdded(String isAdded) {
        this.isAdded = isAdded;
    }

    public String getSortNum() {
        return sortNum;
    }

    public void setSortNum(String sortNum) {
        this.sortNum = sortNum;
    }

    public String getSalesVolume() {
        return salesVolume;
    }

    public void setSalesVolume(String salesVolume) {
        this.salesVolume = salesVolume;
    }

    public String getServiceTime() {
        return serviceTime;
    }

    public void setServiceTime(String serviceTime) {
        this.serviceTime = serviceTime;
    }
}
