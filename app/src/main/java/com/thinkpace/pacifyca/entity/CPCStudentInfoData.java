package com.thinkpace.pacifyca.entity;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Krishna on 12/25/2016.
 */
public class CPCStudentInfoData implements IPCDataModel {
    @SerializedName("student_id")
    private String mStudentId;
    @SerializedName("Institute")
    private String mStudentInstitute;
    @SerializedName("FullName")
    private String mStudentName;
    @SerializedName("CourseName")
    private String mCourseName;
    @SerializedName("AcademicYear")
    private String mAcademicYear;
    @SerializedName("AdmissionDate")
    private String mAdmissionDate;
    @SerializedName("RegisterNumber")
    private String mRegisterNumber;
    @SerializedName("ProfilePhoto")
    private String mProfilePhoto;
    @SerializedName("DateOfBirth")
    private String mDateOfBirth;

    public String getStudentId() {
        return mStudentId;
    }

    public String getStudentInstitute() {
        return mStudentInstitute;
    }

    public String getStudentName() {
        return mStudentName;
    }

    public String getCourseName() {
        return mCourseName;
    }

    public String getAcademicYear() {
        return mAcademicYear;
    }

    public String getAdmissionDate() {
        return mAdmissionDate;
    }

    public String getRegisterNumber() {
        return mRegisterNumber;
    }

    public String getProfilePhoto() {
        return mProfilePhoto;
    }

    public String getDateOfBirth() {
        return mDateOfBirth;
    }
}
