package com.thinkpace.pacifyca.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.thinkpace.pacifyca.Adapter.CPCAttendanceChartAdapter;
import com.thinkpace.pacifyca.BuildConfig;
import com.thinkpace.pacifyca.R;
import com.thinkpace.pacifyca.app.CPCVolley;
import com.thinkpace.pacifyca.calander.APCAttendanceCalendarActivity;
import com.thinkpace.pacifyca.entity.CPCAttendanceDetailsModel;
import com.thinkpace.pacifyca.entity.CPCChartData;
import com.thinkpace.pacifyca.entity.CPCMessageDetails;
import com.thinkpace.pacifyca.entity.CPCPieChartItems;
import com.thinkpace.pacifyca.entity.CPCStudentDetails;
import com.thinkpace.pacifyca.entity.IPCDataModel;
import com.thinkpace.pacifyca.fragment.DatePickerFragment;
import com.thinkpace.pacifyca.fragment.FPCStudentsDialogFragment;
import com.thinkpace.pacifyca.listener.IPCDateSelectionListener;
import com.thinkpace.pacifyca.net.CPCGsonGetRequest;
import com.thinkpace.pacifyca.utils.CPCAppCommonUtility;
import com.thinkpace.pacifyca.utils.CPCAppConstants;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Krishna on 12/2/2016.
 */
public class APCAttendanceActivity extends APCBaseActivity implements CPCAttendanceChartAdapter.OnChartClickListener, FPCStudentsDialogFragment.OnStudentsDialogClickListener, IPCDateSelectionListener, View.OnClickListener, Response.ErrorListener, Response.Listener<IPCDataModel> {
    public static final int[] COLORFUL_COLORS = {
            Color.rgb(69, 194, 142), Color.rgb(217, 18, 9), Color.rgb(254, 244, 125),
            Color.rgb(106, 150, 31), Color.rgb(179, 100, 53)
    };
    CPCAttendanceChartAdapter mAttendanceChartAdapter;
    ArrayList<CPCPieChartItems> pieChartDataArrayList;
    @BindView(R.id.date_picker)
    TextView mDatePicker;
    @BindView(R.id.student_name)
    TextView mStudentName;
    @BindView(R.id.course_name)
    TextView mCourseName;
    @BindView(R.id.submit_btn)
    Button mSubmitBtn;
    @BindView(R.id.progress_lyt)
    RelativeLayout mProgressLyt;
    @BindView(R.id.no_attendance_lyt)
    RelativeLayout mNoAttendanceLyt;
    @BindView(R.id.pie_chart_recycler_view)
    RecyclerView mAttendanceRecyclerView;

    private FPCStudentsDialogFragment mStudentsDialogFragment;
    private String TAG_AUTHOR_FRAGMENT = "FPCStudentsDialogFragment";
    private String mCalDate = null;
    private Toolbar mToolbar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.attendance_activity);
        mToolbar = (Toolbar) findViewById(R.id.toolbar_actionbar);
        ((TextView) mToolbar.findViewById(R.id.title)).setText("Attendance");
        initialiseToolbar(mToolbar);

        getSupportActionBar().setDisplayShowHomeEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        getSupportActionBar().setTitle("Attendance");
        ButterKnife.bind(this);
        initUi();
    }

    private void initUi() {
        mDatePicker.setOnClickListener(this);
        mSubmitBtn.setOnClickListener(this);
        mAttendanceRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        Calendar calender = Calendar.getInstance();
        selectedDate(new Date(calender.getTimeInMillis()));
        bindStudentInfo();
    }

    private void bindStudentInfo() {
        mStudentName.setText(CPCAppCommonUtility.getStudentName(this, CPCAppConstants.KEY_STUDENT_NAME));
    }


    private void selectedDate(Date date) {
        String selectedDate;
        String inputFormat = "E MMM dd HH:mm:ss Z yyyy";
        selectedDate = formatDate(this, date.toString(), inputFormat, "dd-MMM-yyyy");//yyyy-MM-dd
        mCalDate = formatDate(this, date.toString(), inputFormat, "yyyy-MM-dd");//yyyy-MM-dd

        mDatePicker.setText(selectedDate);
    }

    public static String formatDate(Activity mContext, String inputDate, String inputFormat, String outputFormat) {
        try {
            Locale appLocale = Locale.ENGLISH;
            DateFormat originalFormat = new SimpleDateFormat(inputFormat, appLocale);
            DateFormat targetFormat = new SimpleDateFormat(outputFormat, appLocale);
            Date dateObject = originalFormat.parse(inputDate);
            String formattedDate = targetFormat.format(dateObject);
            return formattedDate;
        } catch (ParseException e) {
            e.printStackTrace();
            return "";
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    public void noAttendanceLytVisibility(boolean isVisible) {
        if (isVisible) {
            mNoAttendanceLyt.setVisibility(View.VISIBLE);
        } else {
            mNoAttendanceLyt.setVisibility(View.GONE);
        }
    }


    @Override
    public void onDateSelected(String date) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date oldDate = sdf.parse(date);
            SimpleDateFormat newSdf = new SimpleDateFormat("dd-MMM-yyyy");
            String newDateString = newSdf.format(oldDate);
            mCalDate = new SimpleDateFormat("yyyy-MM-dd").format(oldDate);
            mDatePicker.setText(newDateString);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id) {
           /* case R.id.today:
                Calendar calender = Calendar.getInstance();
                selectedDate(new Date(calender.getTimeInMillis()));
                break;
            case R.id.yesterday:
                Calendar mYesterdayCalendar = Calendar.getInstance();
                mYesterdayCalendar.add(Calendar.DATE, -1);
                selectedDate(new Date(mYesterdayCalendar.getTimeInMillis()));
                break;*/
            case R.id.submit_btn:
                initAttendanceStatusApiCall();
                //startActivity(new Intent(APCAttendanceActivity.this, APCAttendanceCalendarActivity.class));
                break;
            case R.id.date_picker:
                initCalendarDialog();
                break;
        }
    }

    public String getMonthYear(String date) {
        String finalDate = null;
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy");
        try {
            Date oldDate = sdf.parse(date);
            SimpleDateFormat newSdf = new SimpleDateFormat("MM-yyyy");
            finalDate = newSdf.format(oldDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return finalDate;
    }

    private void initAttendanceStatusApiCall() {
        if (TextUtils.isEmpty(mDatePicker.getText())) {
            Toast.makeText(APCAttendanceActivity.this, "Please choose date to proceed", Toast.LENGTH_SHORT).show();
            return;
        }
        if (CPCAppCommonUtility.isNetworkAvailable(this)) {
            String url = CPCAppConstants.KEY_GET_ATTENDANCE_DETAILS_URL;
            Map<String, String> headers = new HashMap<>();
            headers.put(CPCAppConstants.KEY_HEADER_API_KEY, BuildConfig.API_KEY);
            headers.put(CPCAppConstants.KEY_AUTHORIZATION, CPCAppCommonUtility.getAccessToken(this));
            noAttendanceLytVisibility(false);
            progressLytVisibility(true);
            String parentId = CPCAppCommonUtility.getUserId(this);
            String clientId = CPCAppCommonUtility.getClientId(this, CPCAppConstants.KEY_CLIENT_ID);
            String studentId = CPCAppCommonUtility.getStudentId(this, CPCAppConstants.KEY_STUDENT_ID);
            url = url + parentId + "/client/" + clientId + "/student/" + studentId + "/attendance-chart-details?from_month_year=" + getMonthYear(mDatePicker.getText().toString());
            CPCVolley.getRequestQueue(getApplicationContext()).add(
                    new CPCGsonGetRequest(url, this, this,
                            new CPCChartData(), headers));
        } else {
            Toast.makeText(this, R.string.no_network_msg, Toast.LENGTH_SHORT).show();
        }
    }

    public void progressLytVisibility(boolean isVisible) {
        if (isVisible) {
            mProgressLyt.setVisibility(View.VISIBLE);
        } else {
            mProgressLyt.setVisibility(View.GONE);
        }

    }

    private String getCurrentDate() {
        Calendar c = Calendar.getInstance();
        System.out.println("Current time => " + c.getTime());
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        String formattedDate = df.format(c.getTime());
        return formattedDate;
    }

    private void initCalendarDialog() {
        DialogFragment newFragment = new DatePickerFragment();
        Bundle arg = new Bundle();
        String currentDate = null;
        if (!TextUtils.isEmpty(mCalDate)) {
            currentDate = mCalDate;
        } else {
            currentDate = getCurrentDate();
        }
        arg.putString(CPCAppConstants.KEY_DEFAULT_DOB, currentDate);
        newFragment.setArguments(arg);
        newFragment.show(getSupportFragmentManager(), "datePicker");
    }

    @Override
    public void onErrorResponse(VolleyError error) {
        CPCAppCommonUtility.log("response", "error" + error.getMessage());
        progressLytVisibility(false);
        if (error != null)
            CPCAppCommonUtility.handleErrorResponse(this, error.getAlertTitle(), error.getAlertMessage());
        else
            CPCAppCommonUtility.handleErrorResponse(this, null, null);
    }


    @Override
    public void onResponse(IPCDataModel response) {
        progressLytVisibility(false);
        if (response instanceof CPCAttendanceDetailsModel) {
            CPCAttendanceDetailsModel cpcAttendanceDetailsModel = (CPCAttendanceDetailsModel) response;
            if (cpcAttendanceDetailsModel != null && cpcAttendanceDetailsModel.getMessage() != null) {
                Toast.makeText(APCAttendanceActivity.this, "" + cpcAttendanceDetailsModel.getMessage(), Toast.LENGTH_SHORT).show();
            } else {
                noAttendanceLytVisibility(true);
            }
        } else if (response instanceof CPCChartData) {
            CPCChartData chartData = (CPCChartData) response;
            setChartData(chartData);
        }
    }

    private void setChartData(CPCChartData chartData) {
        pieChartDataArrayList = new ArrayList<>();

        if (chartData != null && chartData.getmAttendenceList() != null && chartData.getmAttendenceList().size() > 0) {
            noAttendanceLytVisibility(false);
            mAttendanceRecyclerView.setVisibility(View.VISIBLE);

            if (chartData.getmAttendenceList().get(0) != null && !TextUtils.isEmpty(chartData.getmAttendenceList().get(0).getType())
                    && chartData.getmAttendenceList().get(0).getType().equals(CPCAppConstants.KEY_GROUP_DAILY_TWICE)) {

                CPCPieChartItems items = chartData.getmAttendenceList().get(0);
                items.setmPresentAMCount(chartData.getmAttendenceList().get(0).getmPresentCount());
                items.setmPresentPMCount(chartData.getmAttendenceList().get(1).getmPresentCount());
                items.setmAbsentAMCount(chartData.getmAttendenceList().get(0).getmAbsentCount());
                items.setmAbsentPMCount(chartData.getmAttendenceList().get(1).getmAbsentCount());
                items.setmAbsentAmPmCount(chartData.getmAttendenceList().get(0).getmAbsentCount() + chartData.getmAttendenceList().get(1).getmAbsentCount());
                items.setmTotalAmPmCount(chartData.getmAttendenceList().get(0).getTotalClasses() + chartData.getmAttendenceList().get(1).getTotalClasses());
                pieChartDataArrayList.add(items);

            } else if (chartData.getmAttendenceList().get(0) != null && !TextUtils.isEmpty(chartData.getmAttendenceList().get(0).getType())
                    && chartData.getmAttendenceList().get(0).getType().equals(CPCAppConstants.KEY_GROUP_DAILY_ONCE)) {
                pieChartDataArrayList = chartData.getmAttendenceList();
            } else if (chartData.getmAttendenceList().get(0) != null && !TextUtils.isEmpty(chartData.getmAttendenceList().get(0).getType())
                    && chartData.getmAttendenceList().get(0).getType().equals(CPCAppConstants.KEY_GROUP_SUBJECT)) {
                pieChartDataArrayList = chartData.getmAttendenceList();
            }
            mAttendanceChartAdapter = new CPCAttendanceChartAdapter(this, pieChartDataArrayList, this);
            mAttendanceRecyclerView.setAdapter(mAttendanceChartAdapter);
        } else

        {
            noAttendanceLytVisibility(true);
            mAttendanceRecyclerView.setVisibility(View.GONE);
        }

    }

    @Override
    public void onChartItemClick(CPCPieChartItems item, int position, Boolean isDailyTwice) {
        Intent intent = new Intent(this, APCAttendanceCalendarActivity.class);
        if (item != null && !TextUtils.isEmpty(item.getSubjectId()))
            intent.putExtra(CPCAppConstants.SUBJECT_ID, item.getSubjectId());
        intent.putExtra(CPCAppConstants.MONTH_YEAR, getMonthYear(mDatePicker.getText().toString()));
        intent.putExtra(CPCAppConstants.IS_DAILY_TWICE, isDailyTwice);
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.change_student) {
            showStudentsDialog();
        } else if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    private void showStudentsDialog() {

        android.support.v4.app.FragmentManager manager = getSupportFragmentManager();
        mStudentsDialogFragment = new FPCStudentsDialogFragment();
        if (mStudentsDialogFragment != null) {
            mStudentsDialogFragment.setCancelable(false);
            mStudentsDialogFragment.setDialogListener(this);
            mStudentsDialogFragment.show(manager, TAG_AUTHOR_FRAGMENT);
        }
    }

    @Override
    public void onStudentSelected(CPCStudentDetails mSelectedStudentItem) {
        mAttendanceRecyclerView.setVisibility(View.GONE);
        bindStudentInfo();
        Calendar calender = Calendar.getInstance();
        selectedDate(new Date(calender.getTimeInMillis()));
        noAttendanceLytVisibility(false);
    }
}
