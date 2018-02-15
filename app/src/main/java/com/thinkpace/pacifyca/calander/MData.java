package com.thinkpace.pacifyca.calander;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Gadagool Krishna on 12/21/2016.
 */
public class MData implements Serializable {
    public ArrayList<MonthData> getMonthData() {
        return monthData;
    }

    private ArrayList<MonthData> monthData;
}
