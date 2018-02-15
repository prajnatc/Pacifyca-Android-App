package com.thinkpace.pacifyca.entity;

/**
 * Created by Krishna on 3/19/2017.
 */
public class CPCInstituteData implements IPCDataModel {

    private String type;
    private String title;
    private String description;
    private String date;

    public String getBackgroundColor() {
        return backgroundColor;
    }

    public String getDate() {
        return date;
    }

    public String getDescription() {
        return description;
    }

    public String getTitle() {
        return title;
    }

    public String getType() {
        return type;
    }

    private String backgroundColor;

}
