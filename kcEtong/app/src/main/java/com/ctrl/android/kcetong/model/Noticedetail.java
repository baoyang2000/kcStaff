package com.ctrl.android.kcetong.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/1/12.
 */

public class Noticedetail implements Serializable {

    /**
     * method : pm.ppt.propertyNotice.queryPropertyNoticeInfo
     * level : Info
     * code : 000
     * data : {"propertyNoticeImage":{"originalImg":"http://localhost:8080/"},"propertyNoticeInfo":{"propertyNoticeId":"11","createTime":1445841881000,"content":"a","currentPage":0,"rowCountPerPage":0,"index":0}}
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
        /**
         * propertyNoticeImage : {"originalImg":"http://localhost:8080/"}
         * propertyNoticeInfo : {"propertyNoticeId":"11","createTime":1445841881000,"content":"a","currentPage":0,"rowCountPerPage":0,"index":0}
         */
        private List<Img> propertyNoticeImage = new ArrayList<>();
        private PropertyNoticeInfoBean propertyNoticeInfo;


        public List<Img> getListImg() {
            return propertyNoticeImage;
        }
        public PropertyNoticeInfoBean getPropertyNoticeInfo() {
            return propertyNoticeInfo;
        }

        public void setPropertyNoticeInfo(PropertyNoticeInfoBean propertyNoticeInfo) {
            this.propertyNoticeInfo = propertyNoticeInfo;
        }

        public static class PropertyNoticeInfoBean {
            private String noticeTitle;//公告标题

            //公告列表使用
            private String level;//公告级别（0：重要、1：一般）
            private String status;//状态（0：未读、1：已读）
            //公告详情使用
            private String content;//公告内容
            private String createTime;//公告发布时间
            private String userName;//用户名
            //private String originalImg;//公告图片
            private String propertyNoticeId;//通知公告id



            public String getPropertyNoticeId(){return propertyNoticeId;}
            public void setPropertyNoticeId(String propertyNoticeId){this.propertyNoticeId=propertyNoticeId;}

            public String getNoticeTitle() {
                return noticeTitle;
            }

            public void setNoticeTitle(String noticeTitle) {
                this.noticeTitle = noticeTitle;
            }

            public String getLevel() {
                return level;
            }

            public void setLevel(String level) {
                this.level = level;
            }

            public String getStatus() {
                return status;
            }

            public void setStatus(String status) {
                this.status = status;
            }

            public String getContent() {
                return content;
            }

            public void setContent(String content) {
                this.content = content;
            }

            public String getCreateTime() {
                return createTime;
            }

            public void setCreateTime(String createTime) {
                this.createTime = createTime;
            }

            public String getUserName() {
                return userName;
            }

            public void setUserName(String userName) {
                this.userName = userName;
            }
        }
    }
}
