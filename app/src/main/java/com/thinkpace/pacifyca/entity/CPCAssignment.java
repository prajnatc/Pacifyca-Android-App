package com.thinkpace.pacifyca.entity;

import java.util.ArrayList;

/**
 * Created by sapna on 15-03-2017.
 */

public class CPCAssignment implements IPCDataModel {
    private ArrayList<CPCAssignmentData> data;

    public ArrayList<CPCAssignmentData> getData() {
        return data;
    }

    public void setData(ArrayList<CPCAssignmentData> data) {
        this.data = data;
    }
}
