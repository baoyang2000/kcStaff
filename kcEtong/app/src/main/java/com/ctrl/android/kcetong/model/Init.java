package com.ctrl.android.kcetong.model;

import java.io.Serializable;

/**
 * Created by liu on 2017/1/4.
 */

public class Init implements Serializable{
    /*"communityInfo": {
        "id": "8588212e1ea248c18723b8b7e12244a1",
                "unit": "1",
                "building": "1",
                "proprietorId": "4b3e30948a194c23859e54626e9a8014",
                "communityName": "邮电新村",
                "addressId": "aa93f8a6b5794621ac8bec59ac545a58",
                "proprietorName": "胡志军",
                "housesBindId": "203e542d7855411ebf8b52476bea673e",
                "room": "1907"
    }*/
    private Community communityInfo;

    @Override
    public String toString() {
        return "Init{" +
               "communityInfo=" + communityInfo +
               '}';
    }

    public Community getCommunityInfo() {
        return communityInfo;
    }

    public void setCommunityInfo(Community communityInfo) {
        this.communityInfo = communityInfo;
    }
}
