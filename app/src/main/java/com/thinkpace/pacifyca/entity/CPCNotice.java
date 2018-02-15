package com.thinkpace.pacifyca.entity;

import java.util.ArrayList;

/**
 * Created by prajna on 25-10-2017.
 */

public class CPCNotice implements IPCDataModel {
    private ArrayList<CPCNoticeData> data;

    public ArrayList<CPCNoticeData> getData() {
        return data;
    }

    public void setData(ArrayList<CPCNoticeData> data) {
        this.data = data;
    }
}
