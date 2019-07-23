package com.ctrl.android.kcetong.model;

/**
 * 房产信息  实体
 * Created by Eric on 2015/11/3.
 */
public class HouseEstateInfo {

    private String id;//房产信息的id
    private String communityId;//房产信息所在的社区id
    private String communityName;//房产信息所在的社区id
    private String title;//房产的标题
    private String roomOwner;//房产的拥有人
    private String phone;//房产拥有人的电话
    private String building;//楼号
    private String unit;//单元号
    private String room;//房间号
    private String sittingRoom;//房屋室
    private String livingRoom;//房屋厅
    private String bathRoom;//卫生间
    private String orientation;//房屋朝向
    private String floor;//楼层介绍
    private String places;//详细位置介绍
    private String roomType;//住宅类型(1：普通住宅、2：商业住宅）
    private String sellType;//房屋状态（0：已售 1：在售）
    private String roomAge;//房龄
    private String propertyRight;//房屋产权
    private String renovation;//装修情况
    private String roomAcreage;//房屋面积
    private String singlePrice;//单价
    private String totalPrice;//总价
    private String housingProfile;//房屋简介
    private String createTime;//发布时间
    private String originalImg;//发布时间originalImg
   /*private String[] originalImg;//发布时间originalImg

    public String[] getOriginalImg() {
        return originalImg;
    }

    public void setOriginalImg(String[] originalImg) {
        this.originalImg = originalImg;
    }*/

    public String getCommunityName() {
        return communityName;
    }

    public void setCommunityName(String communityName) {
        this.communityName = communityName;
    }

    public String getOriginalImg() {
        return originalImg;
    }

    public void setOriginalImg(String originalImg) {
        this.originalImg = originalImg;
    }

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

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getRoomOwner() {
        return roomOwner;
    }

    public void setRoomOwner(String roomOwner) {
        this.roomOwner = roomOwner;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
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

    public String getSittingRoom() {
        return sittingRoom;
    }

    public void setSittingRoom(String sittingRoom) {
        this.sittingRoom = sittingRoom;
    }

    public String getLivingRoom() {
        return livingRoom;
    }

    public void setLivingRoom(String livingRoom) {
        this.livingRoom = livingRoom;
    }

    public String getBathRoom() {
        return bathRoom;
    }

    public void setBathRoom(String bathRoom) {
        this.bathRoom = bathRoom;
    }

    public String getOrientation() {
        return orientation;
    }

    public void setOrientation(String orientation) {
        this.orientation = orientation;
    }

    public String getFloor() {
        return floor;
    }

    public void setFloor(String floor) {
        this.floor = floor;
    }

    public String getPlaces() {
        return places;
    }

    public void setPlaces(String places) {
        this.places = places;
    }

    public String getRoomType() {
        return roomType;
    }

    public void setRoomType(String roomType) {
        this.roomType = roomType;
    }

    public String getSellType() {
        return sellType;
    }

    public void setSellType(String sellType) {
        this.sellType = sellType;
    }

    public String getRoomAge() {
        return roomAge;
    }

    public void setRoomAge(String roomAge) {
        this.roomAge = roomAge;
    }

    public String getPropertyRight() {
        return propertyRight;
    }

    public void setPropertyRight(String propertyRight) {
        this.propertyRight = propertyRight;
    }

    public String getRenovation() {
        return renovation;
    }

    public void setRenovation(String renovation) {
        this.renovation = renovation;
    }

    public String getRoomAcreage() {
        return roomAcreage;
    }

    public void setRoomAcreage(String roomAcreage) {
        this.roomAcreage = roomAcreage;
    }

    public String getSinglePrice() {
        return singlePrice;
    }

    public void setSinglePrice(String singlePrice) {
        this.singlePrice = singlePrice;
    }

    public String getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(String totalPrice) {
        this.totalPrice = totalPrice;
    }

    public String getHousingProfile() {
        return housingProfile;
    }

    public void setHousingProfile(String housingProfile) {
        this.housingProfile = housingProfile;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }
}
