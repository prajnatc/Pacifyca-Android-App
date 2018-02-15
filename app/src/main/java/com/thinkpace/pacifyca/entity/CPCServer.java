package com.thinkpace.pacifyca.entity;

import com.google.gson.annotations.SerializedName;

/**
 * Created by sapna on 10-11-2016.
 */

public class CPCServer implements IPCDataModel {
    @SerializedName("getcatalogurl")
    private String catalogurl;

    public String getCatalogurl() {
        return catalogurl;
    }

    public void setCatalogurl(String catalogurl) {
        this.catalogurl = catalogurl;
    }
}
