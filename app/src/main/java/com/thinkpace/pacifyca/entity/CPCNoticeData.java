package com.thinkpace.pacifyca.entity;

import java.util.ArrayList;

/**
 * Created by prajna on 25-10-2017.
 */

public class CPCNoticeData implements IPCDataModel {
    public ArrayList<CPCNoticeList> getNoticeList() {
        if (noticeList == null)
           noticeList = new ArrayList<>();
        return noticeList;
    }

    public void setNoticeList(ArrayList<CPCNoticeList> noticeList) {
        this.noticeList = noticeList;
    }

//    public String getSubname() {
//        return subname;
//    }
//
//    public void setSubname(String subname) {
//        this.subname = subname;
//    }

    private ArrayList<CPCNoticeList> noticeList;

//    private String subname;


}
