package com.thinkpace.pacifyca.entity;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Krishna on 11/27/2016.
 */
public class CPCParentData implements IPCDataModel {
    @SerializedName("id")
    private int mId;

    @SerializedName("user_id")
    private String mUserId;

    public int getId() {
        return mId;
    }

    public String getUserId() {
        return mUserId;
    }
}
