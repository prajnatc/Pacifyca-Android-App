package com.thinkpace.pacifyca.entity.timetable;

import com.thinkpace.pacifyca.entity.IPCDataModel;

import java.util.ArrayList;

/**
 * Created by sapna on 19-03-2017.
 */

public class CPCTimeTableModel implements IPCDataModel{

    private ArrayList<CPCTimeTableData> data;

    public ArrayList<CPCTimeTableData> getData ()
    {
        return data;
    }

    public void setData (ArrayList<CPCTimeTableData> data)
    {
        this.data = data;
    }
}
