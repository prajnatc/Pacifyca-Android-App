package com.thinkpace.pacifyca.entity;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by Krishna on 12/3/2016.
 */
public class CPCAttendanceDetailsModel implements IPCDataModel {
    @SerializedName("data")
    private ArrayList<CPCAttendanceData> mAttendanceData;

    @SerializedName("message")
    private String mMessage;

    public ArrayList<CPCAttendanceData> getAttendanceData() {
        return mAttendanceData;
    }

    public String getMessage() {
        return mMessage;
    }

    private String subject_id;
    private String subject;
}
