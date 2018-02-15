package com.thinkpace.pacifyca.entity;

import com.google.gson.annotations.SerializedName;
import com.thinkpace.pacifyca.entity.CPCUserTypeData;
import com.thinkpace.pacifyca.entity.IPCDataModel;

import java.util.ArrayList;

/**
 * Created by Krishna on 11/26/2016.
 */
public class CPCUserTypesModel implements IPCDataModel {


    @SerializedName("userType")
    public ArrayList<CPCUserTypeData> mUserTypeData;

    public ArrayList<CPCUserTypeData> getUserTypeData() {
        return mUserTypeData;
    }
}
