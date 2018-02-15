package com.thinkpace.pacifyca.entity;

import com.google.gson.annotations.SerializedName;

/**
 * Created by sapna on 30-11-2016.
 */

public class CPCMessageDetails implements IPCDataModel {

    private String student_name;
    private String message_content;


    @SerializedName("institution_name")
    private String mInstitutionName;

    @SerializedName("id")
    private String mId;
    @SerializedName("client_id")
    private String mClientId;

    public String getMessageTime() {
        return mMessageTime;
    }

    @SerializedName("sent_at")
    private String mMessageTime;

    @SerializedName("course_name")
    private String mCourseName;

    @SerializedName("academic_year")
    private String mAcademicYear;


    private boolean isFavourite = false;

    public boolean isFavourite() {
        return isFavourite;
    }

    public void setFavourite(boolean favourite) {
        isFavourite = favourite;
    }

    //Prajna
    private  boolean isRead = false;

    public boolean isRead() {
        return isRead;
    }

    public void setRead(boolean read){
        isRead = read;
    }

    public String getStudentName() {
        return student_name;
    }

    public void setStudentName(String studentName) {
        this.student_name = studentName;
    }

    public String getMessage_content() {
        return message_content;
    }

    public void setMessage_content(String message_content) {
        this.message_content = message_content;
    }

    public String getInstitutionName() {
        return mInstitutionName;
    }

    public String getId() {
        return mId;
    }


    public String getmClientId() {
        return mClientId;
    }

    public String getmAcademicYear() {
        return mAcademicYear;
    }

    public void setmAcademicYear(String mAcademicYear) {
        this.mAcademicYear = mAcademicYear;
    }

    public String getmCourseName() {
        return mCourseName;
    }

    public void setmCourseName(String mCourseName) {
        this.mCourseName = mCourseName;
    }
}
