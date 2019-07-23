package com.ctrl.android.kcetong.model;

import java.util.List;

/**
 * Created by cxl on 2017/1/16.
 */

public class HouseBean {


    /**
     * method : pm.ppt.memberBind.list
     * level : Info
     * code : 000
     * data : {"housesList":[{"id":"203e542d7855411ebf8b52476bea673e","communityId":"8588212e1ea248c18723b8b7e12244a1","communityName":"邮电新村","memberId":"7091ebfbd949490eb7a078e8d2f197ac","proprietorId":"4b3e30948a194c23859e54626e9a8014","addressId":"aa93f8a6b5794621ac8bec59ac545a58","building":"1","unit":"1","room":"1907","isDefault":"1","createTime":1450679818000,"proprietorName":"胡志军","index":0,"rowCountPerPage":0,"isPayment":"1"}]}
     */

    private String   method;
    private String   level;
    private String   code;
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

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        private List<House> housesList;
        private List<House> communityList;

        public List<House> getHousesList() {
            return housesList;
        }

        public void setHousesList(List<House> housesList) {
            this.housesList = housesList;
        }

        public List<House> getCommunityList() {
            return communityList;
        }

        public void setCommunityList(List<House> communityList) {
            this.communityList = communityList;
        }
//        public static class HousesListBean {
//            /**
//             * id : 203e542d7855411ebf8b52476bea673e
//             * communityId : 8588212e1ea248c18723b8b7e12244a1
//             * communityName : 邮电新村
//             * memberId : 7091ebfbd949490eb7a078e8d2f197ac
//             * proprietorId : 4b3e30948a194c23859e54626e9a8014
//             * addressId : aa93f8a6b5794621ac8bec59ac545a58
//             * building : 1
//             * unit : 1
//             * room : 1907
//             * isDefault : 1
//             * createTime : 1450679818000
//             * proprietorName : 胡志军
//             * index : 0
//             * rowCountPerPage : 0
//             * isPayment : 1
//             */
//
//            private String id;
//            private String communityId;
//            private String communityName;
//            private String memberId;
//            private String proprietorId;
//            private String addressId;
//            private String building;
//            private String unit;
//            private String room;
//            private String isDefault;
//            private long   createTime;
//            private String proprietorName;
//            private int    index;
//            private int    rowCountPerPage;
//            private String isPayment;
//
//            public String getId() {
//                return id;
//            }
//
//            public void setId(String id) {
//                this.id = id;
//            }
//
//            public String getCommunityId() {
//                return communityId;
//            }
//
//            public void setCommunityId(String communityId) {
//                this.communityId = communityId;
//            }
//
//            public String getCommunityName() {
//                return communityName;
//            }
//
//            public void setCommunityName(String communityName) {
//                this.communityName = communityName;
//            }
//
//            public String getMemberId() {
//                return memberId;
//            }
//
//            public void setMemberId(String memberId) {
//                this.memberId = memberId;
//            }
//
//            public String getProprietorId() {
//                return proprietorId;
//            }
//
//            public void setProprietorId(String proprietorId) {
//                this.proprietorId = proprietorId;
//            }
//
//            public String getAddressId() {
//                return addressId;
//            }
//
//            public void setAddressId(String addressId) {
//                this.addressId = addressId;
//            }
//
//            public String getBuilding() {
//                return building;
//            }
//
//            public void setBuilding(String building) {
//                this.building = building;
//            }
//
//            public String getUnit() {
//                return unit;
//            }
//
//            public void setUnit(String unit) {
//                this.unit = unit;
//            }
//
//            public String getRoom() {
//                return room;
//            }
//
//            public void setRoom(String room) {
//                this.room = room;
//            }
//
//            public String getIsDefault() {
//                return isDefault;
//            }
//
//            public void setIsDefault(String isDefault) {
//                this.isDefault = isDefault;
//            }
//
//            public long getCreateTime() {
//                return createTime;
//            }
//
//            public void setCreateTime(long createTime) {
//                this.createTime = createTime;
//            }
//
//            public String getProprietorName() {
//                return proprietorName;
//            }
//
//            public void setProprietorName(String proprietorName) {
//                this.proprietorName = proprietorName;
//            }
//
//            public int getIndex() {
//                return index;
//            }
//
//            public void setIndex(int index) {
//                this.index = index;
//            }
//
//            public int getRowCountPerPage() {
//                return rowCountPerPage;
//            }
//
//            public void setRowCountPerPage(int rowCountPerPage) {
//                this.rowCountPerPage = rowCountPerPage;
//            }
//
//            public String getIsPayment() {
//                return isPayment;
//            }
//
//            public void setIsPayment(String isPayment) {
//                this.isPayment = isPayment;
//            }
//        }
    }
}
