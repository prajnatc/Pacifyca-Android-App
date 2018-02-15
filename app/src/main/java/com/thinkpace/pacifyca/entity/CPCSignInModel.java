package com.thinkpace.pacifyca.entity;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Krishna on 11/27/2016.
 */
public class CPCSignInModel implements IPCDataModel {

    @SerializedName("token")
    private String mAuthToken;

    @SerializedName("token_type")
    private String mTokenType;

    @SerializedName("name")
    private String mUserName;

    @SerializedName("activated_on")
    private String mActivatedDate;

    public String getAuthToken() {
        return mAuthToken;
    }

    public String getTokenType() {
        return mTokenType;
    }

    public String getUserName() {
        return mUserName;
    }

    public String getActivatedDate() {
        return mActivatedDate;
    }
}
