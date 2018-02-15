package com.thinkpace.pacifyca.calander;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.squareup.timessquare.CalendarCellDecorator;
import com.squareup.timessquare.CalendarCellView;
import com.thinkpace.pacifyca.R;
import com.thinkpace.pacifyca.entity.CPCInstituteCalenderModel;
import com.thinkpace.pacifyca.entity.CPCInstituteData;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class InstituteCalendarDecorator implements CalendarCellDecorator {
    Context mContext;
    CPCInstituteCalenderModel cMonthData;

    public InstituteCalendarDecorator(Context context, CPCInstituteCalenderModel monthData) {
        mContext = context;
        cMonthData = monthData;
    }

    @Override
    public void decorate(CalendarCellView cellView, Date date) {
        String dateString = Integer.toString(date.getDate());
        SimpleDateFormat dt1 = new SimpleDateFormat("dd-MM-yyyy");
        cellView.setBackgroundColor(ContextCompat.getColor(mContext, R.color.white));
        cellView.setSelected(false);
        String finalString;
        String endDate = dt1.format(date);
        finalString = dateString;
        String mPreviousDate = null;
        if (cMonthData != null && cMonthData.getInstituteData() != null) {
            for (final CPCInstituteData c : cMonthData.getInstituteData()) {
                if (CheckDates(c.getDate(), endDate)) {
//                    if (c.getAbsent() && !c.getSession().equalsIgnoreCase("GROUP_DAILY_TWICE") || (mPreviousDate != null && mPreviousDate.equalsIgnoreCase(c.getDate()))) {
                    cellView.setBackgroundColor(Color.parseColor(c.getBackgroundColor()));
                    cellView.setSelected(true);
                    cellView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Log.e("Tag","Calendar Clicked, Title :"+c.getTitle()+", Description :"+c.getDescription());

                            AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                            builder.setMessage(c.getDescription())
                                    .setTitle(c.getTitle())
                                    .setCancelable(false)
                                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                            //do things
                                        }
                                    });
                            AlertDialog alert = builder.create();
                            alert.show();

                        }
                    });
                    finalString = dateString;
                  /*  } else {
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
                    }*/
                }
            }
        }
        cellView.getDayOfMonthTextView().setText(finalString);
    }

    public static boolean CheckDates(String startDate, String endDate) {

        SimpleDateFormat dfDate = new SimpleDateFormat("dd-MM-yyyy");

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
