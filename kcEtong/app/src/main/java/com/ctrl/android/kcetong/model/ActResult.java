package com.ctrl.android.kcetong.model;

import java.util.List;

/**
 * Created by liu on 2017/1/10.
 */

public class ActResult {
    /*{
        "method": "pm.ppt.action.queryActionList",
            "level": "Info",
            "code": "000",
            "description": "查询社区活动成功！",
            "data": {
        "actionList": [{
            "id": "7a053ba46e8c4f2e9aae57d2ea349af5",
                    "title": "圣诞大游行",
                    "startTime": 1451009160000,
                    "endTime": 1451034360000,
                    "actionType": "1",
                    "visibleType": "1",
                    "checkStatus": "1",
                    "disabled": "0",
                    "participateStatus": "0",
                    "index": 0,
                    "rowCountPerPage": 0},
                    {
            "id": "e5988f2a437c4ca684dfe503429c3f9b",
                "title": "圣诞派对",
                "startTime": 1450954800000,
                "endTime": 1450965600000,
                "actionType": "1",
                "visibleType": "1",
                "checkStatus": "1",
                "disabled": "0",
                "participateStatus": "0",
                "index": 0,
                "rowCountPerPage": 0
        }]
    }
    }*/
    private String code;
    private DataBeanAct data;
    private String description ;

    @Override
    public String toString() {
        return "ActResult{" +
               "code='" + code + '\'' +
               ", data=" + data +
               ", description='" + description + '\'' +
               '}';
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public DataBeanAct getData() {
        return data;
    }

    public void setData(DataBeanAct data) {
        this.data = data;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public static class DataBeanAct{
        private List<Act> actionList;

        @Override
        public String toString() {
            return "DataBeanAct{" +
                   "actionList=" + actionList +
                   '}';
        }

        public List<Act> getActionList() {
            return actionList;
        }

        public void setActionList(List<Act> actionList) {
            this.actionList = actionList;
        }
    }
}
