package com.ctrl.android.kcetong.model;

/**
 * Created by cxl on 2017/2/7.
 */

public class ApkVersionsBean {


    /**
     * method : pm.ppt.apkVersions.updateApkVersions
     * level : Info
     * code : 000
     * description : 获取版本信息成功！
     * data : {"apkVersions":{"id":"1","apkName":"诚信行业主端","apkVersion":"2.0.0","apkUrl":"http://115.28.243.3:8008/attached/apk/versions/app-release.apk","isNew":"1","fileSize":"10.52","adminId":"1","remark":"1","updateTime":1479693456000,"clientType":"1","osType":"0","versionCode":3}}
     */

    private String   method;
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
        /**
         * apkVersions : {"id":"1","apkName":"诚信行业主端","apkVersion":"2.0.0","apkUrl":"http://115.28.243.3:8008/attached/apk/versions/app-release.apk","isNew":"1","fileSize":"10.52","adminId":"1","remark":"1","updateTime":1479693456000,"clientType":"1","osType":"0","versionCode":3}
         */

        private ApkInfo apkVersions;

        public ApkInfo getApkVersions() {
            return apkVersions;
        }

        public void setApkVersions(ApkInfo apkVersions) {
            this.apkVersions = apkVersions;
        }

    }
}
