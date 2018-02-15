package com.thinkpace.pacifyca.calander;

import com.google.gson.annotations.SerializedName;
import com.thinkpace.pacifyca.entity.IPCDataModel;

import java.io.Serializable;

/**
 * Created by Gadagool Krishna on 12/21/2016.
 */

public class CMonthData implements IPCDataModel {
    private MData data;

    public MData getData ()
    {
        return data;
    }

    public void setData (MData data)
    {
        this.data = data;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [data = "+data+"]";
    }

}
