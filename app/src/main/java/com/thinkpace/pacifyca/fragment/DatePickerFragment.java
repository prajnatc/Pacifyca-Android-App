package com.thinkpace.pacifyca.fragment;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.MenuItem;
import android.widget.DatePicker;

import com.thinkpace.pacifyca.R;
import com.thinkpace.pacifyca.listener.IPCDateSelectionListener;
import com.thinkpace.pacifyca.utils.CPCAppConstants;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import static com.thinkpace.pacifyca.R.style.DateDialogTheme;

public class DatePickerFragment extends DialogFragment
        implements DatePickerDialog.OnDateSetListener {
    IPCDateSelectionListener mListener;
    Date mMaxDate = null;
    Date mMinDate = null;
    private String dateFormat;

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        onAttach(getParentFragment());
        String defaultDate = getArguments().getString(CPCAppConstants.KEY_DEFAULT_DOB);
        SimpleDateFormat df = null;
        dateFormat = null;
        String minDate = null;
        String maxDate = null;
        Bundle bundle = getArguments();
        if (bundle != null) {
            if (bundle.containsKey(CPCAppConstants.MAX_DATE)) {
                maxDate = (String) bundle.get(CPCAppConstants.MAX_DATE);
            }
            if (bundle.containsKey(CPCAppConstants.MIN_DATE)) {
                minDate = (String) bundle.get(CPCAppConstants.MIN_DATE);
            }
            if (bundle.containsKey(CPCAppConstants.DATE_FORMAT)) {
                dateFormat = (String) bundle.get(CPCAppConstants.DATE_FORMAT);
            }
        }
        if (dateFormat != null) {
            df = new SimpleDateFormat(dateFormat);
        } else {
            df = new SimpleDateFormat("yyyy-MM-dd");
        }
        try {
            if (maxDate != null)
                mMaxDate = df.parse(maxDate);
            if (minDate != null)
                mMinDate = df.parse(minDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Calendar cal = Calendar.getInstance();
        try {
            cal.setTime(df.parse(defaultDate));
        } catch (Exception e) {
            e.printStackTrace();
        }
        SimpleDateFormat df1 = new SimpleDateFormat("MM");
        int year = cal.get(Calendar.YEAR);
        int month = Integer.parseInt(df1.format(cal.getTime()));
        if (month == 0) {
            month = 0;
        } else {
            month = month - 1;
        }
        int day = cal.get(Calendar.DAY_OF_MONTH);
        // Create a new instance of DatePickerDialog and return it
        DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(), DateDialogTheme, this, year, month, day);
        // request a window without the title
        Calendar calendar = Calendar.getInstance();
        if (mMaxDate != null) {
            datePickerDialog.getDatePicker().setMaxDate(mMaxDate.getTime());
        } else {
            datePickerDialog.getDatePicker().setMaxDate(calendar.getTimeInMillis());
        }
        if (mMinDate != null) {
            datePickerDialog.getDatePicker().setMinDate(mMinDate.getTime());
        }
        return datePickerDialog;
    }

    public void onAttach(Fragment fragment) {
        try {
            if (fragment instanceof IPCDateSelectionListener) {
                mListener = (IPCDateSelectionListener) fragment;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setListener(IPCDateSelectionListener listener) {
        mListener = listener;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            if (activity instanceof IPCDateSelectionListener) {
                mListener = (IPCDateSelectionListener) activity;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int day) {
        if (view.isShown()) {
            if (mListener != null) {
                if (dateFormat != null) {
                    String dateOldString = year + "-" + (month + 1) + "-" + day;
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                    try {
                        Date oldDate = sdf.parse(dateOldString);
                        SimpleDateFormat newSdf = new SimpleDateFormat(dateFormat);
                        String newDateString = newSdf.format(oldDate);
                        mListener.onDateSelected(newDateString);
                    } catch (ParseException e) {
                        mListener.onDateSelected(year + "-" + (month + 1) + "-" + day);
                    }
                } else {
                    mListener.onDateSelected(year + "-" + (month + 1) + "-" + day);
                }
            }
        }
    }
}
