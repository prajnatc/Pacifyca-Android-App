package com.thinkpace.pacifyca.entity;

/**
 * Created by sapna on 15-03-2017.
 */

public class CPCAssignmentList implements IPCDataModel {

    private String title;

    private String assignedNo;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAssignedNo() {
        return assignedNo;
    }

    public void setAssignedNo(String assignedNo) {
        this.assignedNo = assignedNo;
    }

    public String getAssigneddate() {
        return assigneddate;
    }

    public void setAssigneddate(String assigneddate) {
        this.assigneddate = assigneddate;
    }

    public String getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(String expiryDate) {
        this.expiryDate = expiryDate;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAssigneeName() {
        return assigneeName;
    }

    public void setAssigneeName(String assigneeName) {
        this.assigneeName = assigneeName;
    }

    private String assigneddate;

    private String expiryDate;

    private String description;

    private String assigneeName;

    public String getSubName() {
        return subName;
    }

    public void setSubName(String subName) {
        this.subName = subName;
    }

    private String subName;

    //Prajna

    private  boolean isRead = false;

    public boolean isRead() {
        return isRead;
    }

    public void setRead(boolean read){
        isRead = read;
    }

}
