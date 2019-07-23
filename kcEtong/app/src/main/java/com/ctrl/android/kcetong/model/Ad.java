package com.ctrl.android.kcetong.model;

import java.io.Serializable;
import java.util.List;

/**
 * 广告
 * Created by Eric on 2015/11/11.
 */
public class Ad implements Serializable {


    /**
     * method : pm.ppt.advertisement.list
     * level : Info
     * code : 000
     * data : {"advertisementList":[{"id":"79ff93cc50ca439c9dc5b7e045541fff","location":"PPT_HOME_BOTTOM","imgUrl":"http://og3hq2x84.bkt.clouddn.com/p1b3p67pe3p4mfvn8ee1r0squ11.jpg,http://og3hq2x84.bkt.clouddn.com/p1b3p68b5c64t18qp138215hq13tm5.jpg,http://og3hq2x84.bkt.clouddn.com/p1b3p68elelkr1v6d32nn7fj0n8.png","targetUrl":"http://www.cxhpm.com/m/,http://www.cxhpm.com/m/,http://www.cxhpm.com/m/","sortNum":1,"adminId":"506cf194e92848e597bf64c377223682","validTime":1577871373000,"createTime":1481535369000}]}
     */

    private String method;
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
        private List<AdvertisementListBean> advertisementList;

        public List<AdvertisementListBean> getAdvertisementList() {
            return advertisementList;
        }

        public void setAdvertisementList(List<AdvertisementListBean> advertisementList) {
            this.advertisementList = advertisementList;
        }

        public static class AdvertisementListBean {
            /**
             * id : 79ff93cc50ca439c9dc5b7e045541fff
             * location : PPT_HOME_BOTTOM
             * imgUrl : http://og3hq2x84.bkt.clouddn.com/p1b3p67pe3p4mfvn8ee1r0squ11.jpg,http://og3hq2x84.bkt.clouddn.com/p1b3p68b5c64t18qp138215hq13tm5.jpg,http://og3hq2x84.bkt.clouddn.com/p1b3p68elelkr1v6d32nn7fj0n8.png
             * targetUrl : http://www.cxhpm.com/m/,http://www.cxhpm.com/m/,http://www.cxhpm.com/m/
             * sortNum : 1
             * adminId : 506cf194e92848e597bf64c377223682
             * validTime : 1577871373000
             * createTime : 1481535369000
             */

            private String id;
            private String location;
            private String imgUrl;
            private String targetUrl;
            private int    sortNum;
            private String adminId;
            private long   validTime;
            private long   createTime;

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public String getLocation() {
                return location;
            }

            public void setLocation(String location) {
                this.location = location;
            }

            public String getImgUrl() {
                return imgUrl;
            }

            public void setImgUrl(String imgUrl) {
                this.imgUrl = imgUrl;
            }

            public String getTargetUrl() {
                return targetUrl;
            }

            public void setTargetUrl(String targetUrl) {
                this.targetUrl = targetUrl;
            }

            public int getSortNum() {
                return sortNum;
            }

            public void setSortNum(int sortNum) {
                this.sortNum = sortNum;
            }

            public String getAdminId() {
                return adminId;
            }

            public void setAdminId(String adminId) {
                this.adminId = adminId;
            }

            public long getValidTime() {
                return validTime;
            }

            public void setValidTime(long validTime) {
                this.validTime = validTime;
            }

            public long getCreateTime() {
                return createTime;
            }

            public void setCreateTime(long createTime) {
                this.createTime = createTime;
            }
        }
    }
}
