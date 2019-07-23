package com.ctrl.android.kcetong.model;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2017/2/4.
 */

public class ShopCategoryBean implements Serializable {


    /**
     * code : 000
     * description : 查询社区推荐商家列表成功！
     * level : Info
     * method : pm.ppt.companysAround.get
     */

    private String code;
    private DataBean data;
    private String   description;
    private String   level;
    private String   method;

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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public static class DataBean {
        private List<CompanyAroundBean> companyAround;

        public List<CompanyAroundBean> getCompanyAround() {
            return companyAround;
        }

        public void setCompanyAround(List<CompanyAroundBean> companyAround) {
            this.companyAround = companyAround;
        }

        public static class CompanyAroundBean {
            /**
             * categoryName : 超市
             * companyAroundlist : [{"address":"北京","businessTime":"9：00-12：00","categoryName":"超市","communityName":"邮电新村","companyImg":"http://og3hq2x84.bkt.clouddn.com/d5efcaf1b48d4636b9b24833004ca182jpg","introduction":"测试商家","mobile":"15686582652","name":"阿萨德"}]
             */

            private String categoryName;
            private List<CompanyAroundlistBean> companyAroundlist;

            public String getCategoryName() {
                return categoryName;
            }

            public void setCategoryName(String categoryName) {
                this.categoryName = categoryName;
            }

            public List<CompanyAroundlistBean> getCompanyAroundlist() {
                return companyAroundlist;
            }

            public void setCompanyAroundlist(List<CompanyAroundlistBean> companyAroundlist) {
                this.companyAroundlist = companyAroundlist;
            }

            public static class CompanyAroundlistBean {
                /**
                 * address : 北京
                 * businessTime : 9：00-12：00
                 * categoryName : 超市
                 * communityName : 邮电新村
                 * companyImg : http://og3hq2x84.bkt.clouddn.com/d5efcaf1b48d4636b9b24833004ca182jpg
                 * introduction : 测试商家
                 * mobile : 15686582652
                 * name : 阿萨德
                 */

                private String address;
                private String businessTime;
                private String categoryName;
                private String communityName;
                private String companyImg;
                private String introduction;
                private String mobile;
                private String name;

                public String getAddress() {
                    return address;
                }

                public void setAddress(String address) {
                    this.address = address;
                }

                public String getBusinessTime() {
                    return businessTime;
                }

                public void setBusinessTime(String businessTime) {
                    this.businessTime = businessTime;
                }

                public String getCategoryName() {
                    return categoryName;
                }

                public void setCategoryName(String categoryName) {
                    this.categoryName = categoryName;
                }

                public String getCommunityName() {
                    return communityName;
                }

                public void setCommunityName(String communityName) {
                    this.communityName = communityName;
                }

                public String getCompanyImg() {
                    return companyImg;
                }

                public void setCompanyImg(String companyImg) {
                    this.companyImg = companyImg;
                }

                public String getIntroduction() {
                    return introduction;
                }

                public void setIntroduction(String introduction) {
                    this.introduction = introduction;
                }

                public String getMobile() {
                    return mobile;
                }

                public void setMobile(String mobile) {
                    this.mobile = mobile;
                }

                public String getName() {
                    return name;
                }

                public void setName(String name) {
                    this.name = name;
                }
            }
        }
    }
}
