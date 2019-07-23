package com.ctrl.android.kcetong.model;

import java.util.List;

/**
 * Created by cxl on 2017/1/17.
 */

public class BuildingListBean {

    /**
     * method : pm.ppt.communityAddress.list
     * level : Info
     * code : 000
     * data : {"roomList":["0301","0302","0401","0402","0501","0502","0601","0602","0603","0701","0702","0703","0801","0802","0901","0902","1001","1002","1101","1102","1201","1202","1301","1302","1304","1401","1402","1404","1501","1502","1601","1602","1701","1702","1801","1802","1901","1902","1903","1904","2001","2002","2101","2102","2103","2201","2202","2204","2301","2302","2401","2402","2403","2501","2502","2601","2602","2701","2702","2703","2801","2802","2901","2902","3001","3002","3101","3102","3201","3202"],"unitList":["1","2","3"],"buildList":["01","02","03","04","二期01","二期02","二期03","商铺"]}
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
        private List<String> roomList;
        private List<String> unitList;
        private List<String> buildList;

        public List<String> getRoomList() {
            return roomList;
        }

        public void setRoomList(List<String> roomList) {
            this.roomList = roomList;
        }

        public List<String> getUnitList() {
            return unitList;
        }

        public void setUnitList(List<String> unitList) {
            this.unitList = unitList;
        }

        public List<String> getBuildList() {
            return buildList;
        }

        public void setBuildList(List<String> buildList) {
            this.buildList = buildList;
        }
    }
}
