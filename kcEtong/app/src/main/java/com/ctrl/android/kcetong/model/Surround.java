package com.ctrl.android.kcetong.model;

import java.io.Serializable;
import java.util.List;

/**
 * Created by liu on 2018/2/9.
 */

public class Surround implements Serializable{

    /**
     * method : pm.ppt.service.list
     * level : Info
     * code : 000
     * description : 查询成功！
     * data : {"serviceList":[{"id":"1","communityId":"1","serviceName":"服务名称11","contentPicture":"http://114.215.95.182:8088/attached/propertyImg/img/2016/5/12/1A890D9F-C7DE-4427-9347-CF5D43F76B4E.png","listPicture":"http://114.215.95.182:8088/attached/propertyImg/img/2016/9/4/03B81034-DDBD-4810-8C7D-1F1C3F828380.png","company":"公司名称","phone":"电话","address":"地址","urlAddress":"网址","content":"内容","businessList":[{"id":"2","title":"22","picture":"http://114.215.95.182:8088/attached/propertyImg/img/2016/9/4/03B81034-DDBD-4810-8C7D-1F1C3F828380.png","serviceId":"1"}],"bannerList":[{"id":"1","title":"aa","picture":"http://114.215.95.182:8088/attached/propertyImg/img/2016/9/4/03B81034-DDBD-4810-8C7D-1F1C3F828380.png","serviceId":"1"}],"index":0,"rowCountPerPage":0},{"id":"6","communityId":"1","serviceName":"服务名称1166","contentPicture":"http://114.215.95.182:8088/attached/propertyImg/img/2016/9/4/03B81034-DDBD-4810-8C7D-1F1C3F828380.png","listPicture":"http://114.215.95.182:8088/attached/propertyImg/img/2016/9/4/03B81034-DDBD-4810-8C7D-1F1C3F828380.png","company":"公司名称","phone":"电话","address":"地址","urlAddress":"网址","content":"eer","index":0,"rowCountPerPage":0},{"id":"5","communityId":"1","serviceName":"服务名称1155","contentPicture":"http://114.215.95.182:8088/attached/propertyImg/img/2016/10/21/F138BE17-E0FA-4D82-805A-2C277B69B837.png","listPicture":"http://114.215.95.182:8088/attached/propertyImg/img/2016/9/4/03B81034-DDBD-4810-8C7D-1F1C3F828380.png","company":"公司名称","phone":"电话","address":"地址","urlAddress":"网址","content":"36699","index":0,"rowCountPerPage":0},{"id":"4","communityId":"1","serviceName":"服务名称1144","contentPicture":"http://114.215.95.182:8088/attached/propertyImg/img/2016/10/21/F138BE17-E0FA-4D82-805A-2C277B69B837.png","listPicture":"http://114.215.95.182:8088/attached/propertyImg/img/2016/9/4/03B81034-DDBD-4810-8C7D-1F1C3F828380.png","company":"公司名称","phone":"电话","address":"地址","urlAddress":"网址","content":"wed3rrg","index":0,"rowCountPerPage":0},{"id":"3","communityId":"1","serviceName":"服务名称1133","contentPicture":"http://114.215.95.182:8088/attached/propertyImg/img/2016/5/12/1A890D9F-C7DE-4427-9347-CF5D43F76B4E.png","listPicture":"http://114.215.95.182:8088/attached/propertyImg/img/2016/9/4/03B81034-DDBD-4810-8C7D-1F1C3F828380.png","company":"公司名称","phone":"电话","address":"地址","urlAddress":"网址","content":"w w w","index":0,"rowCountPerPage":0}]}
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

    public static class DataBean  implements Serializable{
        private List<ServiceListBean> serviceList;

        public List<ServiceListBean> getServiceList() {
            return serviceList;
        }

        public void setServiceList(List<ServiceListBean> serviceList) {
            this.serviceList = serviceList;
        }

        public static class ServiceListBean implements Serializable {
            /**
             * id : 1
             * communityId : 1
             * serviceName : 服务名称11
             * contentPicture : http://114.215.95.182:8088/attached/propertyImg/img/2016/5/12/1A890D9F-C7DE-4427-9347-CF5D43F76B4E.png
             * listPicture : http://114.215.95.182:8088/attached/propertyImg/img/2016/9/4/03B81034-DDBD-4810-8C7D-1F1C3F828380.png
             * company : 公司名称
             * phone : 电话
             * address : 地址
             * urlAddress : 网址
             * content : 内容
             * businessList : [{"id":"2","title":"22","picture":"http://114.215.95.182:8088/attached/propertyImg/img/2016/9/4/03B81034-DDBD-4810-8C7D-1F1C3F828380.png","serviceId":"1"}]
             * bannerList : [{"id":"1","title":"aa","picture":"http://114.215.95.182:8088/attached/propertyImg/img/2016/9/4/03B81034-DDBD-4810-8C7D-1F1C3F828380.png","serviceId":"1"}]
             * index : 0
             * rowCountPerPage : 0
             */

            private String id;
            private String                 communityId;
            private String                 serviceName;
            private String                 contentPicture;
            private String                 supervisionPhone;
            private String                 listPicture;
            private String                 company;
            private String                 phone;
            private String                 address;
            private String                 urlAddress;
            private String                 content;
            private int                    index;
            private int                    rowCountPerPage;
            private List<BusinessListBean> businessList;
            private List<BannerListBean>   bannerList;

            public String getSupervisionPhone() {
                return supervisionPhone;
            }

            public void setSupervisionPhone(String supervisionPhone) {
                this.supervisionPhone = supervisionPhone;
            }

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public String getCommunityId() {
                return communityId;
            }

            public void setCommunityId(String communityId) {
                this.communityId = communityId;
            }

            public String getServiceName() {
                return serviceName;
            }

            public void setServiceName(String serviceName) {
                this.serviceName = serviceName;
            }

            public String getContentPicture() {
                return contentPicture;
            }

            public void setContentPicture(String contentPicture) {
                this.contentPicture = contentPicture;
            }

            public String getListPicture() {
                return listPicture;
            }

            public void setListPicture(String listPicture) {
                this.listPicture = listPicture;
            }

            public String getCompany() {
                return company;
            }

            public void setCompany(String company) {
                this.company = company;
            }

            public String getPhone() {
                return phone;
            }

            public void setPhone(String phone) {
                this.phone = phone;
            }

            public String getAddress() {
                return address;
            }

            public void setAddress(String address) {
                this.address = address;
            }

            public String getUrlAddress() {
                return urlAddress;
            }

            public void setUrlAddress(String urlAddress) {
                this.urlAddress = urlAddress;
            }

            public String getContent() {
                return content;
            }

            public void setContent(String content) {
                this.content = content;
            }

            public int getIndex() {
                return index;
            }

            public void setIndex(int index) {
                this.index = index;
            }

            public int getRowCountPerPage() {
                return rowCountPerPage;
            }

            public void setRowCountPerPage(int rowCountPerPage) {
                this.rowCountPerPage = rowCountPerPage;
            }

            public List<BusinessListBean> getBusinessList() {
                return businessList;
            }

            public void setBusinessList(List<BusinessListBean> businessList) {
                this.businessList = businessList;
            }

            public List<BannerListBean> getBannerList() {
                return bannerList;
            }

            public void setBannerList(List<BannerListBean> bannerList) {
                this.bannerList = bannerList;
            }

            public static class BusinessListBean implements Serializable {
                /**
                 * id : 2
                 * title : 22
                 * picture : http://114.215.95.182:8088/attached/propertyImg/img/2016/9/4/03B81034-DDBD-4810-8C7D-1F1C3F828380.png
                 * serviceId : 1
                 */

                private String id;
                private String title;
                private String picture;
                private String serviceId;

                public String getId() {
                    return id;
                }

                public void setId(String id) {
                    this.id = id;
                }

                public String getTitle() {
                    return title;
                }

                public void setTitle(String title) {
                    this.title = title;
                }

                public String getPicture() {
                    return picture;
                }

                public void setPicture(String picture) {
                    this.picture = picture;
                }

                public String getServiceId() {
                    return serviceId;
                }

                public void setServiceId(String serviceId) {
                    this.serviceId = serviceId;
                }
            }

            public static class BannerListBean implements Serializable {
                /**
                 * id : 1
                 * title : aa
                 * picture : http://114.215.95.182:8088/attached/propertyImg/img/2016/9/4/03B81034-DDBD-4810-8C7D-1F1C3F828380.png
                 * serviceId : 1
                 */

                private String id;
                private String title;
                private String picture;
                private String serviceId;

                public String getId() {
                    return id;
                }

                public void setId(String id) {
                    this.id = id;
                }

                public String getTitle() {
                    return title;
                }

                public void setTitle(String title) {
                    this.title = title;
                }

                public String getPicture() {
                    return picture;
                }

                public void setPicture(String picture) {
                    this.picture = picture;
                }

                public String getServiceId() {
                    return serviceId;
                }

                public void setServiceId(String serviceId) {
                    this.serviceId = serviceId;
                }
            }
        }
    }
}
