package com.ctrl.android.kcetong.model;

import java.util.ArrayList;
import java.util.List;

/**
 * 描述：
 *
 * @author zhangqin
 * @date 2018/3/29
 */
public class ServiceListBottom {

    /**
     * method : pm.ppt.service.list
     * level : Info
     * code : 000
     * description : 查询成功！
     * data : {"serviceList":[{"id":"a757941b58ed449392e244e290102386","communityId":"eb33c8eb31104a63995642983230d01a","serviceName":"家政服务","contentPicture":"http://og3hq2x84.bkt.clouddn.com/p1c983973098jl6ij5oclofdc1.jpg","listPicture":"http://og3hq2x84.bkt.clouddn.com/p1c9839tm51ej5ocl1f3u1ottb4a1.jpg","company":"山东凯程家园","phone":"400","address":"威海经区富城国际1405","urlAddress":"www.sdgyt.top","content":"（1）一般家务：制作家庭便餐、家居保洁、衣物洗涤、园艺等等，以器物的服务为主；\r\n（2）看护婴幼儿：对婴幼儿的照料、看管；\r\n（3）护理老年人：照料、陪护老年人；\r\n（4）照顾病人：在家庭或医院照料、看护病人；\r\n（5）护理产妇与新生儿：护理产妇与新生儿，亦称月嫂；\r\n（6）家庭教育：主要是对幼儿或小学生的品德和良好习惯的培养、智力开发和学业辅导；\r\n（7）家庭理财：对客户日常生活开支的管理，包括采购、记账等；\r\n（8）家庭秘书：从事家庭或家庭企业的文书档案处理、电脑操作、公关事务、汽车驾驶等；\r\n（9）家庭安全员：一类是负责家庭器物的维修与安全，如负责水电、电器、电脑、住宅的维修服务与安全等，又称家庭技术员；另一类是负责家人的安全，又称家庭保安员；\r\n（10）陪伴：陪同购物或聊天，主要满足客户舒缓情绪，排遣孤独感，或通过其他陪伴方式满足客户的心理需求；\r\n（11）管家：对客户的家庭事务进行全面安排，具体安排其他家庭服务员的日常工作。能针对客户家人的不同特点，调配膳食，美化家庭环境，科学体育锻炼，安排家庭休闲娱乐、旅游，迎送宾客，指导合理的作息等等；\r\n（12）居家服务的家政从业人员（保姆）其服务项目一般是全面多项的，而钟点工、计件工则有单项或多项的，其具体服务项目应以客户与家政企业或家政从业人员所签订的合同内容为准。\r\n（13）单位、社区的服务项目，如清洁托管、单位保洁等，因其后勤服务比较专业化，其项目一般以单项或几项为主。\r\n(14)家庭电器的维修、维护、清洗（包括：厨房电器，电视、电脑、空调等家用电器）。","index":0,"rowCountPerPage":0}]}
     */

    private String method;
    private String level;
    private String code;
    private String description;
    private DataBean data = new DataBean();

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
        private List<ServiceListBean> serviceList = new ArrayList<>();

        public List<ServiceListBean> getServiceList() {
            return serviceList;
        }

        public void setServiceList(List<ServiceListBean> serviceList) {
            this.serviceList = serviceList;
        }

        public static class ServiceListBean {
            /**
             * id : a757941b58ed449392e244e290102386
             * communityId : eb33c8eb31104a63995642983230d01a
             * serviceName : 家政服务
             * contentPicture : http://og3hq2x84.bkt.clouddn.com/p1c983973098jl6ij5oclofdc1.jpg
             * listPicture : http://og3hq2x84.bkt.clouddn.com/p1c9839tm51ej5ocl1f3u1ottb4a1.jpg
             * company : 山东凯程家园
             * phone : 400
             * address : 威海经区富城国际1405
             * urlAddress : www.sdgyt.top
             * content : （1）一般家务：制作家庭便餐、家居保洁、衣物洗涤、园艺等等，以器物的服务为主；
             （2）看护婴幼儿：对婴幼儿的照料、看管；
             （3）护理老年人：照料、陪护老年人；
             （4）照顾病人：在家庭或医院照料、看护病人；
             （5）护理产妇与新生儿：护理产妇与新生儿，亦称月嫂；
             （6）家庭教育：主要是对幼儿或小学生的品德和良好习惯的培养、智力开发和学业辅导；
             （7）家庭理财：对客户日常生活开支的管理，包括采购、记账等；
             （8）家庭秘书：从事家庭或家庭企业的文书档案处理、电脑操作、公关事务、汽车驾驶等；
             （9）家庭安全员：一类是负责家庭器物的维修与安全，如负责水电、电器、电脑、住宅的维修服务与安全等，又称家庭技术员；另一类是负责家人的安全，又称家庭保安员；
             （10）陪伴：陪同购物或聊天，主要满足客户舒缓情绪，排遣孤独感，或通过其他陪伴方式满足客户的心理需求；
             （11）管家：对客户的家庭事务进行全面安排，具体安排其他家庭服务员的日常工作。能针对客户家人的不同特点，调配膳食，美化家庭环境，科学体育锻炼，安排家庭休闲娱乐、旅游，迎送宾客，指导合理的作息等等；
             （12）居家服务的家政从业人员（保姆）其服务项目一般是全面多项的，而钟点工、计件工则有单项或多项的，其具体服务项目应以客户与家政企业或家政从业人员所签订的合同内容为准。
             （13）单位、社区的服务项目，如清洁托管、单位保洁等，因其后勤服务比较专业化，其项目一般以单项或几项为主。
             (14)家庭电器的维修、维护、清洗（包括：厨房电器，电视、电脑、空调等家用电器）。
             * index : 0
             * rowCountPerPage : 0
             */

            private String id;
            private String communityId;
            private String serviceName;
            private String contentPicture;
            private String listPicture;
            private String company;
            private String phone;
            private String address;
            private String urlAddress;
            private String content;
            private int index;
            private int rowCountPerPage;

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
        }
    }
}
