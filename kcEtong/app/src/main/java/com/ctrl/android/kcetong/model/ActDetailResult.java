package com.ctrl.android.kcetong.model;

import java.io.Serializable;
import java.util.List;

/**
 * Created by liu on 2017/1/13.
 */

public class ActDetailResult implements Serializable{
    private String method;
    private DataBean data;
    private String code;
    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    @Override
    public String toString() {
        return "ActDetailResult{" +
               "method='" + method + '\'' +
               ", data=" + data +
               ", code='" + code + '\'' +
               '}';
    }

    public static class DataBean{
        private List<Img> actionPictureList;
        private List<Participant> participantList;
        private ActDetail actionInfo;

        @Override
        public String toString() {
            return "DataBean{" +
                   "actionPictureList=" + actionPictureList +
                   ", participantList=" + participantList +
                   ", actionInfo=" + actionInfo +
                   '}';
        }

        public List<Img> getActionPictureList() {
            return actionPictureList;
        }

        public void setActionPictureList(List<Img> actionPictureList) {
            this.actionPictureList = actionPictureList;
        }

        public List<Participant> getParticipantList() {
            return participantList;
        }

        public void setParticipantList(List<Participant> participantList) {
            this.participantList = participantList;
        }

        public ActDetail getActionInfo() {
            return actionInfo;
        }

        public void setActionInfo(ActDetail actionInfo) {
            this.actionInfo = actionInfo;
        }
    }
}
