package com.ctrl.android.kcetong.model;

import java.io.Serializable;
import java.util.List;

/**
 * Created by liu on 2017/1/13.
 */

public class HouseResult implements Serializable {
    private String code;
    private String description;
    private DataBean data;

    @Override
    public String toString() {
        return "HouseResult{" +
               "code='" + code + '\'' +
               ", description='" + description + '\'' +
               ", data=" + data +
               '}';
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

    public static class DataBean{
        private List<House> housesList;

        @Override
        public String toString() {
            return "DataBean{" +
                   "housesList=" + housesList +
                   '}';
        }

        public List<House> getHousesList() {
            return housesList;
        }

        public void setHousesList(List<House> housesList) {
            this.housesList = housesList;
        }
    }
}
