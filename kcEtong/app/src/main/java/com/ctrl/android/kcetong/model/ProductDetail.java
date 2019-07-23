package com.ctrl.android.kcetong.model;

import java.util.List;

/**
 * Created by liu on 2018/3/5.
 */

public class ProductDetail {
    /**
     * method : pm.ppt.product.get
     * level : Info
     * code : 000
     * description : 查询成功！
     * data : {"productInfo":{}}
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
        private ChoiceGroup productInfo;
        private List<Img>   productPicList;

        public ChoiceGroup getProductInfo() {
            return productInfo;
        }

        public void setProductInfo(ChoiceGroup productInfo) {
            this.productInfo = productInfo;
        }

        public List<Img> getProductPicList() {
            return productPicList;
        }

        public void setProductPicList(List<Img> productPicList) {
            this.productPicList = productPicList;
        }
    }
}
