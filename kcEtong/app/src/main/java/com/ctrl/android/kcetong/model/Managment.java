package com.ctrl.android.kcetong.model;

import java.io.Serializable;
import java.util.List;

/**
 * e管家  问题
 * Created by Eric on 2015/10/29.
 */
public class Managment implements Serializable{

    private int           type;//类型
    private String        question;
    private List<Problem> listProblem;

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

    public List<Problem> getListProblem() {

        return listProblem;
    }

    public void setListProblem(List<Problem> listProblem) {
        this.listProblem = listProblem;
    }
}
