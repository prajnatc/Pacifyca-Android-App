package com.thinkpace.pacifyca.entity;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Krishna on 12/3/2016.
 */
public class CPCAttendanceData implements IPCDataModel {
    @SerializedName("faculty_name")
    private String mFacultyName;
    @SerializedName("absent_message")
    private String mAbsentMessage;

    public String getFacultyName() {
        return mFacultyName;
    }

    public String getAbsentMessage() {
        return mAbsentMessage;
    }
}
