package com.ctrl.android.kcetong.model;

/**
 * 区
 * Created by Eric on 2015/10/29.
 */
public class Area {

    private String id;//id
    private String ptree;//
    private String name;//
    private String grade;//
    private String pid;//
    private String disabled;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPtree() {
        return ptree;
    }

    public void setPtree(String ptree) {
        this.ptree = ptree;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public String getDisabled() {
        return disabled;
    }

    public void setDisabled(String disabled) {
        this.disabled = disabled;
    }
}
