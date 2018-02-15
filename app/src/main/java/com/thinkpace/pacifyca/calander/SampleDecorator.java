package com.thinkpace.pacifyca.calander;

import android.content.Context;
import android.support.v4.content.ContextCompat;

import com.squareup.timessquare.CalendarCellDecorator;
import com.squareup.timessquare.CalendarCellView;
import com.thinkpace.pacifyca.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class SampleDecorator implements CalendarCellDecorator {
    Context mContext;
    CMonthData cMonthData;

    public SampleDecorator(Context context, CMonthData monthData) {
        mContext = context;
        cMonthData = monthData;
    }

    @Override
    public void decorate(CalendarCellView cellView, Date date) {
        String dateString = Integer.toString(date.getDate());
        SimpleDateFormat dt1 = new SimpleDateFormat("dd/MM/yyyy");
        String finalString;
        String endDate = dt1.format(date);
        finalString = dateString;
        String mPreviousDate = null;
        if (cMonthData != null && cMonthData.getData() != null) {
            for (MonthData c : cMonthData.getData().getMonthData()) {
                if (CheckDates(c.getDate(), endDate)) {
                    if (c.getAbsent() && !c.getSession().equalsIgnoreCase("GROUP_DAILY_TWICE") || (mPreviousDate != null && mPreviousDate.equalsIgnoreCase(c.getDate()))) {
                        cellView.setBackgroundColor(ContextCompat.getColor(mContext, R.color.red));
                        cellView.setSelected(true);
                        finalString = dateString;
                    } else {
                        String session = null;
                        mPreviousDate = c.getDate();
                        if (c.getMessage().equalsIgnoreCase("First session")) {
                            session = "AM";
                        } else {
                            session = "PM";
                        }
                        finalString = finalString + "\n" + session;
                        cellView.setBackgroundColor(ContextCompat.getColor(mContext, R.color.yellow));
                        cellView.setSelected(true);
                    }
                }
            }
        }
        cellView.getDayOfMonthTextView().setText(finalString);
    }

    public static boolean CheckDates(String startDate, String endDate) {

        SimpleDateFormat dfDate = new SimpleDateFormat("dd/MM/yyyy");

        boolean b = false;

        try {
            if (dfDate.parse(startDate).before(dfDate.parse(endDate))) {
                b = false;  // If start date is before end date.
            } else if (dfDate.parse(startDate).equals(dfDate.parse(endDate))) {
                b = true;  // If two dates are equal.
            } else {
                b = false; // If start date is after the end date.
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return b;
    }
}
