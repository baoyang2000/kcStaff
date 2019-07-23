package com.ctrl.android.kcetong.model;

/**
 * e管家  问题
 * Created by Eric on 2015/10/29.
 */
public class Problem {

    private String id;//id
    private String communityId;//
    private String problemName;//
    private String keyword;//
    private String problemTypes;//
    private String createTime;//
    private String disabled;//
    private String questionAnswer;//
    private String noneInfo;//
    private int    type;
    private String question;

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

    public String getProblemName() {
        return problemName;
    }

    public void setProblemName(String problemName) {
        this.problemName = problemName;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public String getProblemTypes() {
        return problemTypes;
    }

    public void setProblemTypes(String problemTypes) {
        this.problemTypes = problemTypes;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getDisabled() {
        return disabled;
    }

    public void setDisabled(String disabled) {
        this.disabled = disabled;
    }

    public String getQuestionAnswer() {
        return questionAnswer;
    }

    public void setQuestionAnswer(String questionAnswer) {
        this.questionAnswer = questionAnswer;
    }

    public String getNoneInfo() {
        return noneInfo;
    }

    public void setNoneInfo(String noneInfo) {
        this.noneInfo = noneInfo;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }
}
