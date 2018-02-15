package com.thinkpace.pacifyca.entity.timetable;

import com.thinkpace.pacifyca.entity.IPCDataModel;

import java.util.ArrayList;

/**
 * Created by sapna on 19-03-2017.
 */

public class CPCTimeTableData implements IPCDataModel {
    private String week_name;

    private ArrayList<CPCTimeTableItem> item;

    public String getWeek_name ()
    {
        return week_name;
    }

    public void setWeek_name (String week_name)
    {
        this.week_name = week_name;
    }

    public ArrayList<CPCTimeTableItem>  getItem ()
    {
        return item;
    }

    public void setItem ( ArrayList<CPCTimeTableItem> item)
    {
        this.item = item;
    }
}
