package com.thinkpace.pacifyca.entity;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Krishna on 12/25/2016.
 */
public class CPCStudentInfoModel implements IPCDataModel {
    @SerializedName("data")
    private CPCStudentInfoData cpcStudentInfoData;

    public CPCStudentInfoData getCpcStudentInfoData() {
        return cpcStudentInfoData;
    }
}
