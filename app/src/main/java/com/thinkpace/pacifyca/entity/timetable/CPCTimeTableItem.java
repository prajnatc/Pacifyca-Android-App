package com.thinkpace.pacifyca.entity.timetable;

import com.thinkpace.pacifyca.entity.IPCDataModel;

/**
 * Created by sapna on 19-03-2017.
 */

public class CPCTimeTableItem implements IPCDataModel {
    private String subName;

    private String end_time;

    private String from_time;

    public String getSubName ()
    {
        return subName;
    }

    public void setSubName (String subName)
    {
        this.subName = subName;
    }

    public String getEnd_time ()
    {
        return end_time;
    }

    public void setEnd_time (String end_time)
    {
        this.end_time = end_time;
    }

    public String getFrom_time ()
    {
        return from_time;
    }

    public void setFrom_time (String from_time)
    {
        this.from_time = from_time;
    }

}
