package com.thinkpace.pacifyca.entity;

import java.util.ArrayList;

/**
 * Created by sapna on 15-03-2017.
 */

public class CPCAssignmentData implements IPCDataModel {
    public ArrayList<CPCAssignmentList> getAssignmentList() {
        if (assignmentList == null)
            assignmentList = new ArrayList<>();
        return assignmentList;
    }

    public void setAssignmentList(ArrayList<CPCAssignmentList> assignmentList) {
        this.assignmentList = assignmentList;
    }

    public String getSubname() {
        return subname;
    }

    public void setSubname(String subname) {
        this.subname = subname;
    }

    private ArrayList<CPCAssignmentList> assignmentList;

    private String subname;


}
