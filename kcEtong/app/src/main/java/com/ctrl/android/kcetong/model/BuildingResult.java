package com.ctrl.android.kcetong.model;

import java.io.Serializable;
import java.util.List;

/**
 * Created by liu on 2017/1/13.
 */

public class BuildingResult implements Serializable {
    private String code;
    private DataBean data;

    @Override
    public String toString() {
        return "BuildingResult{" +
               "code='" + code + '\'' +
               ", data=" + data +
               '}';
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

    public static class DataBean{
        private List<String> buildList;
        private List<String> unitList;
        private List<String> roomList;

        @Override
        public String toString() {
            return "DataBean{" +
                   "buildList=" + buildList +
                   ", unitList=" + unitList +
                   ", roomList=" + roomList +
                   '}';
        }

        public List<String> getBuildList() {
            return buildList;
        }

        public void setBuildList(List<String> buildList) {
            this.buildList = buildList;
        }

        public List<String> getUnitList() {
            return unitList;
        }

        public void setUnitList(List<String> unitList) {
            this.unitList = unitList;
        }

        public List<String> getRoomList() {
            return roomList;
        }

        public void setRoomList(List<String> roomList) {
            this.roomList = roomList;
        }
    }
}
