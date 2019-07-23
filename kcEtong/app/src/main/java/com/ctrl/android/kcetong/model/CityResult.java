package com.ctrl.android.kcetong.model;

import java.io.Serializable;
import java.util.List;

/**
 * Created by liu on 2017/2/7.
 */

public class CityResult implements Serializable {
    private String code;
    private DataBean data;

    @Override
    public String toString() {
        return "CityResult{" +
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

    public static class DataBean {
        private List<City> citiesList;

        @Override
        public String toString() {
            return "DataBean{" +
                   "citiesList=" + citiesList +
                   '}';
        }

        public List<City> getCitiesList() {
            return citiesList;
        }

        public void setCitiesList(List<City> citiesList) {
            this.citiesList = citiesList;
        }
    }
}
