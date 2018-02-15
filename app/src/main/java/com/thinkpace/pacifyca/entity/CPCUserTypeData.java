package com.thinkpace.pacifyca.entity;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Krishna on 11/26/2016.
 */
public class CPCUserTypeData implements IPCDataModel {

    @SerializedName("type")
    public String mType;

    public String getType() {
        return mType;
    }
}
