package com.thinkpace.pacifyca.entity;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by sapna on 29-12-2016.
 */

public class CPCChartData implements IPCDataModel {

    public ArrayList<CPCPieChartItems> getmAttendenceList() {
        return mAttendenceList;
    }

    @SerializedName("data")
    private ArrayList<CPCPieChartItems> mAttendenceList;

}
