package com.thinkpace.pacifyca.entity;

import android.text.TextUtils;

import com.google.gson.annotations.SerializedName;
import com.thinkpace.pacifyca.utils.CPCAppCommonUtility;

/**
 * Created by Krishna Upadhya on 12/27/2016.
 */

public class CPCPieChartItems implements IPCDataModel {


    private String session;

    @SerializedName("present")
    private String present;
    @SerializedName("absent")
    private String absent;
    @SerializedName("total")
    private String totalClasses;
    @SerializedName("AttendanceType")
    private String type;

    public String getSubjectId() {
        return subjectId;
    }

    public void setSubjectId(String subjectId) {
        this.subjectId = subjectId;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    @SerializedName("subject_id")
    private String subjectId;
    @SerializedName("subject")
    private String subject;


    private int mPresentCount;

    private int mAbsentCount;

    public int getmAbsentAmPmCount() {
        return mAbsentAmPmCount;
    }

    public void setmAbsentAmPmCount(int mAbsentAmPmCount) {
        this.mAbsentAmPmCount = mAbsentAmPmCount;
    }

    public int getmTotalAmPmCount() {
        return mTotalAmPmCount;
    }

    public void setmTotalAmPmCount(int mTotalAmPmCount) {
        this.mTotalAmPmCount = mTotalAmPmCount;
    }

    private int mAbsentAmPmCount;

    private int mTotalAmPmCount;

    public int getmPresentAMCount() {
        return mPresentAMCount;
    }

    public void setmPresentAMCount(int mPresentAMCount) {
        this.mPresentAMCount = mPresentAMCount;
    }

    public int getmPresentPMCount() {
        return mPresentPMCount;
    }

    public void setmPresentPMCount(int mPresentPMCount) {
        this.mPresentPMCount = mPresentPMCount;
    }

    private int mPresentAMCount;
    private int mPresentPMCount;

    public int getmAbsentAMCount() {
        return mAbsentAMCount;
    }

    public void setmAbsentAMCount(int mAbsentAMCount) {
        this.mAbsentAMCount = mAbsentAMCount;
    }

    public int getmAbsentPMCount() {
        return mAbsentPMCount;
    }

    public void setmAbsentPMCount(int mAbsentPMCount) {
        this.mAbsentPMCount = mAbsentPMCount;
    }

    private int mAbsentAMCount;
    private int mAbsentPMCount;

    public String getAbsent() {
        return absent;
    }

    public void setAbsent(String absent) {
        this.absent = absent;
    }

    public String getPresent() {
        return present;
    }

    public void setPresent(String present) {
        this.present = present;
    }

    public void setmPresentCount(int mPresentCount) {
        this.mPresentCount = mPresentCount;
    }

    public void setmAbsentCount(int mAbsentCount) {
        this.mAbsentCount = mAbsentCount;
    }

    public void setType(String type) {
        this.type = type;
    }


    public String getSession() {
        return session;
    }

    public void setSession(String session) {
        this.session = session;
    }


    public int getmPresentCount() {
        return ConvertToInt(getPresent());
    }

    public int getmAbsentCount() {
        return ConvertToInt(getAbsent());
    }

    public int getTotalClasses() {
        return ConvertToInt(totalClasses);
    }

    public String getType() {
        return type;
    }

    public int ConvertToInt(String value) {
        if (TextUtils.isEmpty(value)) return 0;
        int newVal = 0;
        try {
            double tempValue = Double.parseDouble(value);
            newVal = (int) tempValue;
            return newVal;
        } catch (NumberFormatException nfe) {
            CPCAppCommonUtility.log("Error", nfe.getMessage());
        } catch (Exception ex) {
            CPCAppCommonUtility.log("Error", ex.getMessage());
        }

        return newVal;
    }
}
