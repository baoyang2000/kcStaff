package com.ctrl.android.kcetong.model;

/**
 * 我的预约服务
 * Created by Administrator on 2015/10/29.
 */
public class ServiceOrder {
    private String id;//订单id
    private String appointmentTime;//预约时间
    private String handleStatus;//处理状态（0：待处理、1：处理中、2：已处理、3：已结束）
    private String repairNum;//服务编号
    private String building;//楼号
    private String unit;//单元号
    private String room;//房间号
    private String repairKindName;//服务类别名称
    private String productName;//服务名称
    private String content;//备注
    private String sellingPrice;//服务费用
    private String evaluateLevel;//评价等级（0：非常满意、1：基本满意、2：不满意、3：未评价）
    private String evaluateContent;//评价内容
    private String result;//物业处理结果
    private String payMode;//付款方式1.线上支付 2.线下支付
    private String payStatus;

    @Override
    public String toString() {
        return "ServiceOrder{" +
               "id='" + id + '\'' +
               ", appointmentTime='" + appointmentTime + '\'' +
               ", handleStatus='" + handleStatus + '\'' +
               ", repairNum='" + repairNum + '\'' +
               ", building='" + building + '\'' +
               ", unit='" + unit + '\'' +
               ", room='" + room + '\'' +
               ", repairKindName='" + repairKindName + '\'' +
               ", productName='" + productName + '\'' +
               ", content='" + content + '\'' +
               ", sellingPrice='" + sellingPrice + '\'' +
               ", evaluateLevel='" + evaluateLevel + '\'' +
               ", evaluateContent='" + evaluateContent + '\'' +
               ", result='" + result + '\'' +
               ", payMode='" + payMode + '\'' +
               ", payStatus='" + payStatus + '\'' +
               '}';
    }

    public String getPayMode() {
        return payMode;
    }

    public void setPayMode(String payMode) {
        this.payMode = payMode;
    }

    public String getPayStatus() {
        return payStatus;
    }

    public void setPayStatus(String payStatus) {
        this.payStatus = payStatus;
    }

    public String getEvaluateLevel() {
        return evaluateLevel;
    }

    public void setEvaluateLevel(String evaluateLevel) {
        this.evaluateLevel = evaluateLevel;
    }

    public String getEvaluateContent() {
        return evaluateContent;
    }

    public void setEvaluateContent(String evaluateContent) {
        this.evaluateContent = evaluateContent;
    }
    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getSellingPrice() {
        return sellingPrice;
    }

    public void setSellingPrice(String sellingPrice) {
        this.sellingPrice = sellingPrice;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAppointmentTime() {
        return appointmentTime;
    }

    public void setAppointmentTime(String appointmentTime) {
        this.appointmentTime = appointmentTime;
    }

    public String getHandleStatus() {
        return handleStatus;
    }

    public void setHandleStatus(String handleStatus) {
        this.handleStatus = handleStatus;
    }

    public String getRepairNum() {
        return repairNum;
    }

    public void setRepairNum(String repairNum) {
        this.repairNum = repairNum;
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

    public String getRepairKindName() {
        return repairKindName;
    }

    public void setRepairKindName(String repairKindName) {
        this.repairKindName = repairKindName;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }
}
