package com.thinkpace.pacifyca.entity;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by sapna on 30-11-2016.
 */

public class CPCUserMessages implements IPCDataModel {

    @SerializedName("data")
    private ArrayList<CPCMessageDetails> messagesList;

    public ArrayList<CPCMessageDetails> getMessagesList() {
        return messagesList;
    }

    public void setMessagesList(ArrayList<CPCMessageDetails> messagesList) {
        this.messagesList = messagesList;
    }
}
