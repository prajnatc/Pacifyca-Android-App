package com.thinkpace.pacifyca.entity;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Krishna Upadhya on 11/17/2016.
 */

public class CPCGenerateOtpModel implements IPCDataModel {

    public String getToken() {
        return mToken;
    }

    public String getMobileNumber() {
        return mMobileNumber;
    }

    public int getOtp() {
        return mOtp;
    }

    @SerializedName("token")
    private String mToken;

    @SerializedName("mobile_number")
    private String mMobileNumber;

    @SerializedName("otp")
    private int mOtp;

}
