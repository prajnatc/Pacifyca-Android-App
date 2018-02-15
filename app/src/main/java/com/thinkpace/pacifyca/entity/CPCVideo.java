package com.thinkpace.pacifyca.entity;

import com.thinkpace.pacifyca.entity.CPCAssignmentData;
import com.thinkpace.pacifyca.entity.IPCDataModel;

import java.util.ArrayList;

/**
 * Created by sapna on 15-03-2017.
 */

public class CPCVideo implements IPCDataModel {
    private ArrayList<CPCVideoData> data;

    public ArrayList<CPCVideoData> getData() {
        return data;
    }

    public void setData(ArrayList<CPCVideoData> data) {
        this.data = data;
    }
}
