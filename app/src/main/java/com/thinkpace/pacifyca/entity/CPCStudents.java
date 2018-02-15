package com.thinkpace.pacifyca.entity;

import java.util.ArrayList;

/**
 * Created by Krishna Upadhya on 12/1/2016.
 */

public class CPCStudents implements IPCDataModel {
    private ArrayList<CPCStudentDetails> data;

    public ArrayList<CPCStudentDetails> getData ()
    {
        return data;
    }

    public void setData (ArrayList<CPCStudentDetails> data)
    {
        this.data = data;
    }

}
