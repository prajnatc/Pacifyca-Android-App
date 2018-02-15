package com.thinkpace.pacifyca.entity;

/**
 * Created by prajna on 25-10-2017.
 */

public class CPCNoticeList implements IPCDataModel {

    private String title;

    private String noticedNo;

    private String type;

    public String getTitle() {
        return title;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getNoticedNo() {
        return noticedNo;
    }

    public void setNoticedNo(String noticedNo) {
        this.noticedNo = noticedNo;
    }

    public String geNoticeddate() {
        return noticeddate;
    }

    public void setNoticeddate(String noticeddate) {
        this.noticeddate = noticeddate;
    }

    public String getExpiryDate() {
        return expiry_date;
    }

    public String getStartdate() {
        return start_date;
    }

    public void setStartDate(String start_date) {
        this.start_date = start_date;
    }

    public void setExpiryDate(String expiryDate) {
        this.expiry_date = expiryDate;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

//    public String getAssigneeName() {
//        return assigneeName;
//    }

//    public void setAssigneeName(String assigneeName) {
//        this.assigneeName = assigneeName;
//    }

    private String noticeddate;

    private String start_date;

    private String expiry_date;

    private String description;

//    private String assigneeName;

//    public String getSubName() {
//        return subName;
//    }

//    public void setSubName(String subName) {
//        this.subName = subName;
//    }

//    private String subName;

    //Prajna

    private  boolean isRead = false;

    public boolean isRead() {
        return isRead;
    }

    public void setRead(boolean read){
        isRead = read;
    }

}
