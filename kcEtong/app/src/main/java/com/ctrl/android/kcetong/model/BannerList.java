package com.ctrl.android.kcetong.model;

import java.util.List;

/**
 * 描述：
 *
 * @author zhangqin
 * @date 2018/3/29
 */
public class BannerList {

    /**
     * method : pm.ppt.banner.list
     * level : Info
     * code : 000
     * description : 查询banner列表成功！
     * data : {"bannerList":[{"id":1,"communityId":"eb33c8eb31104a63995642983230d01a","title":"2","titleColor":"2","subTitle":"2","subTitleColor":"2","createTime":1521787454000,"sortNum":0,"picUrl":"2","redirectUrl":"2","disabled":"0"},{"id":2,"communityId":"eb33c8eb31104a63995642983230d01a","title":"大标题1","titleColor":"1","subTitle":"小标题1","subTitleColor":"1","createTime":1521797382000,"sortNum":1,"picUrl":"http://og3hq2x84.bkt.clouddn.com/p1c992v7qs19p41dl36g31gr01fpe1.jpg","redirectUrl":"http://www.baidu.com","disabled":"0"}]}
     */

    private String method;
    private String level;
    private String code;
    private String description;
    private DataBean data;

    @Override
    public String toString() {
        return "BannerList{" +
                "method='" + method + '\'' +
                ", level='" + level + '\'' +
                ", code='" + code + '\'' +
                ", description='" + description + '\'' +
                ", data=" + data +
                '}';
    }

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
        private List<BannerListBean> bannerList;

        public List<BannerListBean> getBannerList() {
            return bannerList;
        }

        public void setBannerList(List<BannerListBean> bannerList) {
            this.bannerList = bannerList;
        }

        public static class BannerListBean {
            /**
             * id : 1
             * communityId : eb33c8eb31104a63995642983230d01a
             * title : 2
             * titleColor : 2
             * subTitle : 2
             * subTitleColor : 2
             * createTime : 1521787454000
             * sortNum : 0
             * picUrl : 2
             * redirectUrl : 2
             * disabled : 0
             */

            private int id;
            private String communityId;
            private String title;
            private String titleColor;
            private String subTitle;
            private String subTitleColor;
            private long createTime;
            private int sortNum;
            private String picUrl;
            private String redirectUrl;
            private String disabled;

            public int getId() {
                return id;
            }

            public void setId(int id) {
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

            public String getTitleColor() {
                return titleColor;
            }

            public void setTitleColor(String titleColor) {
                this.titleColor = titleColor;
            }

            public String getSubTitle() {
                return subTitle;
            }

            public void setSubTitle(String subTitle) {
                this.subTitle = subTitle;
            }

            public String getSubTitleColor() {
                return subTitleColor;
            }

            public void setSubTitleColor(String subTitleColor) {
                this.subTitleColor = subTitleColor;
            }

            public long getCreateTime() {
                return createTime;
            }

            public void setCreateTime(long createTime) {
                this.createTime = createTime;
            }

            public int getSortNum() {
                return sortNum;
            }

            public void setSortNum(int sortNum) {
                this.sortNum = sortNum;
            }

            public String getPicUrl() {
                return picUrl;
            }

            public void setPicUrl(String picUrl) {
                this.picUrl = picUrl;
            }

            public String getRedirectUrl() {
                return redirectUrl;
            }

            public void setRedirectUrl(String redirectUrl) {
                this.redirectUrl = redirectUrl;
            }

            public String getDisabled() {
                return disabled;
            }

            public void setDisabled(String disabled) {
                this.disabled = disabled;
            }
        }
    }
}
