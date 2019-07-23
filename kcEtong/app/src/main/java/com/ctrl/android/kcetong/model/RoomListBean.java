package com.ctrl.android.kcetong.model;

import java.util.List;

/**
 * Created by cxl on 2017/1/17.
 */

public class RoomListBean {

    /**
     * method : pm.ppt.communityAddress.list
     * level : Info
     * code : 000
     * data : {"roomList":["0301","0302","0303","0304","0401","0402","0403","0404","0501","0502","0503","0504","0601","0602","0603","0604","0701","0702","0703","0704","0801","0802","0803","0804","0901","0902","0903","0904","1001","1002","1003","1004","1101","1102","1103","1104","1201","1202","1203","1204","1301","1302","1303","1304","1401","1402","1403","1404","1501","1502","1503","1504","1601","1602","1603","1604","1701","1702","1703","1704","1801","1802","1803","1804","1901","1902","1903","1904","2001","2002","2003","2004","2101","2102","2103","2104","2201","2202","2203","2204","2301","2302","2303","2304","2401","2402","2403","2404","2501","2502","2503","2504","2601","2602","2603","2604","2701","2702","2703","2704"],"unitList":null,"buildList":null}
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
        /**
         * roomList : ["0301","0302","0303","0304","0401","0402","0403","0404","0501","0502","0503","0504","0601","0602","0603","0604","0701","0702","0703","0704","0801","0802","0803","0804","0901","0902","0903","0904","1001","1002","1003","1004","1101","1102","1103","1104","1201","1202","1203","1204","1301","1302","1303","1304","1401","1402","1403","1404","1501","1502","1503","1504","1601","1602","1603","1604","1701","1702","1703","1704","1801","1802","1803","1804","1901","1902","1903","1904","2001","2002","2003","2004","2101","2102","2103","2104","2201","2202","2203","2204","2301","2302","2303","2304","2401","2402","2403","2404","2501","2502","2503","2504","2601","2602","2603","2604","2701","2702","2703","2704"]
         * unitList : null
         * buildList : null
         */

        private Object       unitList;
        private Object       buildList;
        private List<String> roomList;

        public Object getUnitList() {
            return unitList;
        }

        public void setUnitList(Object unitList) {
            this.unitList = unitList;
        }

        public Object getBuildList() {
            return buildList;
        }

        public void setBuildList(Object buildList) {
            this.buildList = buildList;
        }

        public List<String> getRoomList() {
            return roomList;
        }

        public void setRoomList(List<String> roomList) {
            this.roomList = roomList;
        }
    }
}
