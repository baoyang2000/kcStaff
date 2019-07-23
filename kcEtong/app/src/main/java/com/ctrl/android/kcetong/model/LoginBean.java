package com.ctrl.android.kcetong.model;

import java.util.List;

/**
 * Created by Administrator on 2017/1/4.
 */

public class LoginBean {


    /**
     * method : pm.ppt.members.login
     * level : Info
     * code : 000
     * description : 登录成功！
     * data : {"receiveAddList":[],"memberInfo":{"imgUrl":"http://58.56.38.194:8082/attached/headImg/img/2016/12/16/EE097DF9-DD1A-45B1-86C6-A75DB18B5D21.png","point":0,"hasPayPwd":"0","nickName":"动感星空","gender":"0","userName":"18660771597","memberId":"7091ebfbd949490eb7a078e8d2f197ac"}}
     */

    private String   code;
    /**
     * receiveAddList : []
     * memberInfo : {"imgUrl":"http://58.56.38.194:8082/attached/headImg/img/2016/12/16/EE097DF9-DD1A-45B1-86C6-A75DB18B5D21.png","point":0,"hasPayPwd":"0","nickName":"动感星空","gender":"0","userName":"18660771597","memberId":"7091ebfbd949490eb7a078e8d2f197ac"}
     */

    private DataBean data;

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
        /**
         * imgUrl : http://58.56.38.194:8082/attached/headImg/img/2016/12/16/EE097DF9-DD1A-45B1-86C6-A75DB18B5D21.png
         * point : 0
         * hasPayPwd : 0
         * nickName : 动感星空
         * gender : 0
         * userName : 18660771597
         * memberId : 7091ebfbd949490eb7a078e8d2f197ac
         */

        private MemberInfo memberInfo;
        private List<ReceiveAddress> receiveAddList;

        public MemberInfo getMemberInfo() {
            return memberInfo;
        }

        public void setMemberInfo(MemberInfo memberInfo) {
            this.memberInfo = memberInfo;
        }

        public List<ReceiveAddress> getReceiveAddList() {
            return receiveAddList;
        }

        public void setReceiveAddList(List<ReceiveAddress> receiveAddList) {
            this.receiveAddList = receiveAddList;
        }
    }
}
