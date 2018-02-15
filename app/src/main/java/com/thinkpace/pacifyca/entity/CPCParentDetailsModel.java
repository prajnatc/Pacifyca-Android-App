package com.thinkpace.pacifyca.entity;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Krishna on 11/27/2016.
 */
public class CPCParentDetailsModel implements IPCDataModel {

    @SerializedName("parent")
    private CPCParentData mParentData;

    public CPCParentData getParentData() {
        return mParentData;
    }
}
