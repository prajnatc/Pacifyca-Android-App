package com.thinkpace.pacifyca.entity;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by sapna on 10-11-2016.
 */

public class CPCCatalog implements IPCDataModel {
    @SerializedName("servers")
    private ArrayList<CPCServer> mTabMenuList;

    public ArrayList<CPCServer> getmTabMenuList() {
        return mTabMenuList;
    }

    public void setmTabMenuList(ArrayList<CPCServer> mTabMenuList) {
        this.mTabMenuList = mTabMenuList;
    }

}
