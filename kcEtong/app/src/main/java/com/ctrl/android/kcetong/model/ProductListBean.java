package com.ctrl.android.kcetong.model;

import java.util.List;

/**
 * Created by liu on 2018/3/5.
 */

public class ProductListBean {
    /**
     * method : pm.ppt.product.list
     * level : Info
     * code : 000
     * description : 查询成功！
     * data : {"productsList":[{"id":"8211d666c9e34c06a6553f9d30597625","productName":"星火网球俱乐部体验券","originalPrice":0,"sellingPrice":0,"requiredPoint":100,"listPicUrl":"http://114.215.95.182:8080/attached/goods/img/a320e2effe6147a4980827dbcfb43887.jpg","sellType":"1","salesVolume":0,"startTime":1504851900000,"endTime":1506096000000,"overTime":"已结束","sellState":"2","index":0,"rowCountPerPage":0,"readCount":0,"communityId":"fce1bf272fbc4e1ea5bf42e0dc2248c8"}]}
     */

    private String method;
    private String   level;
    private String   code;
    private String   description;
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

    public static class DataBean {
        private List<ChoiceGroup> productsList;

        public List<ChoiceGroup> getProductsList() {
            return productsList;
        }

        public void setProductsList(List<ChoiceGroup> productsList) {
            this.productsList = productsList;
        }
    }
}
