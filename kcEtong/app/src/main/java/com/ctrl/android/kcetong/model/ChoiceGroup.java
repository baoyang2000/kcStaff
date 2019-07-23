package com.ctrl.android.kcetong.model;

/**
 * 团购实体
 * Created by Administrator on 2015/11/24.
 */
public class ChoiceGroup {
    private String id;//商品id
    private String productName;//商品名称
    private String originalPrice;//原价
    private String sellingPrice;//售价
    private String listPicUrl;//列表图片url
    private String requiredPoint;//所需积分
    private String sellType;//消费类型（0：资金消费、1：积分消费）
    private String salesVolume;//销量
    private String startTime;//团购开始时间
    private String endTime;//团购结束时间
    private String overTime;//团购剩余时间（格式：1天2小时30分钟）
    private String sellState;//团购状态（0：未开始、1：已开始、2：已结束）

    private String companyAddress;//商家地址
    private String companyMobile;//商家联系方式
    private String infomation;//商品详情（html格式）
    private String collectStatue;//收藏状态：0未收藏、1已收藏

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
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

    public String getListPicUrl() {
        return listPicUrl;
    }

    public void setListPicUrl(String listPicUrl) {
        this.listPicUrl = listPicUrl;
    }

    public String getRequiredPoint() {
        return requiredPoint;
    }

    public void setRequiredPoint(String requiredPoint) {
        this.requiredPoint = requiredPoint;
    }

    public String getSellType() {
        return sellType;
    }

    public void setSellType(String sellType) {
        this.sellType = sellType;
    }

    public String getSalesVolume() {
        return salesVolume;
    }

    public void setSalesVolume(String salesVolume) {
        this.salesVolume = salesVolume;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getCompanyAddress() {
        return companyAddress;
    }

    public void setCompanyAddress(String companyAddress) {
        this.companyAddress = companyAddress;
    }

    public String getCompanyMobile() {
        return companyMobile;
    }

    public void setCompanyMobile(String companyMobile) {
        this.companyMobile = companyMobile;
    }

    public String getInfomation() {
        return infomation;
    }

    public void setInfomation(String infomation) {
        this.infomation = infomation;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getOverTime() {
        return overTime;
    }

    public void setOverTime(String overTime) {
        this.overTime = overTime;
    }

    public String getSellState() {
        return sellState;
    }

    public void setSellState(String sellState) {
        this.sellState = sellState;
    }

    public String getCollectStatue() {
        return collectStatue;
    }

    public void setCollectStatue(String collectStatue) {
        this.collectStatue = collectStatue;
    }
}
