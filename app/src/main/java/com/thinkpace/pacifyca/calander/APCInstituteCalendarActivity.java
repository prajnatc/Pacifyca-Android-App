package com.thinkpace.pacifyca.calander;

import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.squareup.timessquare.CalendarCellDecorator;
import com.squareup.timessquare.CalendarPickerView;
import com.squareup.timessquare.CalendarPickerView.SelectionMode;
import com.squareup.timessquare.DefaultDayViewAdapter;
import com.thinkpace.pacifyca.Adapter.CPCHolidayListAdapter;
import com.thinkpace.pacifyca.BuildConfig;
import com.thinkpace.pacifyca.R;
import com.thinkpace.pacifyca.activity.APCBaseActivity;
import com.thinkpace.pacifyca.app.CPCVolley;
import com.thinkpace.pacifyca.entity.CPCInstituteCalenderModel;
import com.thinkpace.pacifyca.entity.IPCDataModel;
import com.thinkpace.pacifyca.net.CPCGsonGetRequest;
import com.thinkpace.pacifyca.utils.CPCAppCommonUtility;
import com.thinkpace.pacifyca.utils.CPCAppConstants;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

public class APCInstituteCalendarActivity extends APCBaseActivity implements Response.ErrorListener, Response.Listener<IPCDataModel> {
    private CalendarPickerView calendar;
    private CPCInstituteCalenderModel mMonthData;
    private String mSubjectId;
    private String mMonthYear;
    private int mYear;
    private int mMonth;
    private int mDaysInMonth;
    private Boolean mIsDailyTwice;
    private Toolbar mToolbar;
    @BindView(R.id.holiday_list)
    RecyclerView mHolidayList;

    private CPCHolidayListAdapter cpcHolidayListAdapter;
    private EqualSpaceItemDecoration spaceItemDecoration;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.year_calander_lyt);

        mToolbar = (Toolbar) findViewById(R.id.toolbar_actionbar);
        ((TextView) mToolbar.findViewById(R.id.title)).setText("Institute Calendar");
        initialiseToolbar((Toolbar) findViewById(R.id.toolbar_actionbar));
        getSupportActionBar().setDisplayShowHomeEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        getSupportActionBar().setTitle("Monthly attendance");
        ButterKnife.bind(this);
        getIntentData();
        initCalendarDataFetchApiCall();
        initCalendar();
        mHolidayList.setLayoutManager(new LinearLayoutManager(this));
        mHolidayList.setHasFixedSize(true);

    }

    private void getIntentData() {
        Intent intent = getIntent();
        if (intent.hasExtra(CPCAppConstants.SUBJECT_ID)) {
            mSubjectId = intent.getStringExtra(CPCAppConstants.SUBJECT_ID);
        }
//        if (intent.hasExtra(CPCAppConstants.MONTH_YEAR)) {
        mMonthYear = /*intent.getStringExtra(CPCAppConstants.MONTH_YEAR);*/monthYearGet();
//        }
        if (intent.hasExtra(CPCAppConstants.IS_DAILY_TWICE)) {
            mIsDailyTwice = intent.getBooleanExtra(CPCAppConstants.IS_DAILY_TWICE, false);
        }
       /* if (mIsDailyTwice) {
            mAbsentLyt.setVisibility(View.VISIBLE);
        } else {
            mAbsentLyt.setVisibility(View.GONE);
        }*/

    }


    private void initCalendarDataFetchApiCall() {
        if (CPCAppCommonUtility.isNetworkAvailable(this)) {
            String url = CPCAppConstants.KEY_GET_ATTENDANCE_DETAILS_URL;
            Map<String, String> headers = new HashMap<>();
            headers.put(CPCAppConstants.KEY_HEADER_API_KEY, BuildConfig.API_KEY);
            headers.put(CPCAppConstants.KEY_AUTHORIZATION, CPCAppCommonUtility.getAccessToken(this));
            String parentId = CPCAppCommonUtility.getUserId(this);
            String clientId = CPCAppCommonUtility.getClientId(this, CPCAppConstants.KEY_CLIENT_ID);
            String studentId = CPCAppCommonUtility.getStudentId(this, CPCAppConstants.KEY_STUDENT_ID);
            url = url + parentId + "/client/" + clientId + "/student/" + studentId + "/institution-calender-details?from_month_year=" + mMonthYear;
            /*if (!TextUtils.isEmpty(mSubjectId)) {
                url = url + "&subject_id=" + mSubjectId;
            }*/
            CPCVolley.getRequestQueue(getApplicationContext()).add(
                    new CPCGsonGetRequest(url, this, this,
                            new CPCInstituteCalenderModel(), headers));
        } else {
            Toast.makeText(this, R.string.no_network_msg, Toast.LENGTH_SHORT).show();
        }
    }


    public String monthYearGet() {
        String finalDate = null;
        Calendar c = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy");
        String date = sdf.format(c.getTime());
        try {
            Date oldDate = sdf.parse(date);
            SimpleDateFormat newSdf = new SimpleDateFormat("MM-yyyy");
            finalDate = newSdf.format(oldDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return finalDate;
    }

    private void getMonthYear(String monthYear) {
        try {
            SimpleDateFormat parseFormat = new SimpleDateFormat("MM-yyyy");
            SimpleDateFormat printYearFormat = new SimpleDateFormat("yyyy");
            SimpleDateFormat printDateFormat = new SimpleDateFormat("MM");
            Date date = parseFormat.parse(monthYear);
            mYear = Integer.parseInt(printYearFormat.format(date));
            mMonth = Integer.parseInt(printDateFormat.format(date));
            if (mMonth == 4 || mMonth == 6 || mMonth == 9 || mMonth == 11) {
                mDaysInMonth = 30;
            } else if (mMonth == 2) {
//                mDaysInMonth = (leapYear) ? 29 : 28;
                mDaysInMonth = 28;
            } else {
                mDaysInMonth = 31;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initCalendar() {
        getMonthYear(mMonthYear);
        String date_s = mYear + "-" + 1 + "-01 00:00:00.0"; //edit mMonth
        String date_e = mYear + "-" + mMonth + "-" + mDaysInMonth + " 00:00:00.0";
        SimpleDateFormat dt = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

        Calendar c = Calendar.getInstance();
        try {
            c.setTime(dt.parse(date_e));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        c.add(Calendar.MONTH, 12);  // number of days to add
        String finalDate = dt.format(c.getTime());

        try {

            Calendar firstYear = Calendar.getInstance();
            Calendar lastYear = Calendar.getInstance();
            firstYear.add(Calendar.YEAR, -1);
            lastYear.add(Calendar.YEAR, 1);

            Date date = dt.parse(date_s);
            Date date2 = dt.parse(finalDate);
            Log.d("DATE-3", " " + date);
            Log.d("DATE-4", " " + finalDate);
            calendar = (CalendarPickerView) findViewById(R.id.calendar_view);
            calendar.setCustomDayView(new DefaultDayViewAdapter());
            calendar.setDecorators(Arrays.<CalendarCellDecorator>asList(new InstituteCalendarDecorator(APCInstituteCalendarActivity.this, mMonthData)));
            calendar.init(firstYear.getTime(), lastYear.getTime()) //
                    .inMode(SelectionMode.SINGLE)
                    .withSelectedDate(new Date())
                    .displayOnly();

        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onErrorResponse(VolleyError error) {
        Toast.makeText(APCInstituteCalendarActivity.this, "Great!! student is 100% present this month", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onResponse(IPCDataModel response) {
        if (response instanceof CPCInstituteCalenderModel) {
            CPCInstituteCalenderModel mInstituteData = (CPCInstituteCalenderModel) response;
            if (mInstituteData != null && mInstituteData.getInstituteData() != null && mInstituteData.getInstituteData().size() > 0) {
                mMonthData = mInstituteData;
                initCalendar();
                initHolidayList(mMonthData);
            } else {
//                Toast.makeText(APCAttendanceCalendarActivity.this, "No Attendance data available for this month", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void initHolidayList(CPCInstituteCalenderModel mMonthData) {
        cpcHolidayListAdapter = new CPCHolidayListAdapter(this, mMonthData);
        mHolidayList.setAdapter(cpcHolidayListAdapter);
        spaceItemDecoration = new EqualSpaceItemDecoration(2);
        mHolidayList.addItemDecoration(spaceItemDecoration);
    }

    public class EqualSpaceItemDecoration extends RecyclerView.ItemDecoration {

        private final int mSpaceHeight;

        public EqualSpaceItemDecoration(int mSpaceHeight) {
            this.mSpaceHeight = mSpaceHeight;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent,
                                   RecyclerView.State state) {
            outRect.bottom = mSpaceHeight;
            outRect.top = mSpaceHeight;
            outRect.left = mSpaceHeight;
            outRect.right = mSpaceHeight;
        }
    }
}
