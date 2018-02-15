package com.thinkpace.pacifyca.activity;

import android.app.ProgressDialog;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.thinkpace.pacifyca.Adapter.CPCTImeTablePagerAdapter;
import com.thinkpace.pacifyca.BuildConfig;
import com.thinkpace.pacifyca.R;
import com.thinkpace.pacifyca.app.CPCVolley;
import com.thinkpace.pacifyca.entity.IPCDataModel;
import com.thinkpace.pacifyca.entity.timetable.CPCTimeTableData;
import com.thinkpace.pacifyca.entity.timetable.CPCTimeTableModel;
import com.thinkpace.pacifyca.net.CPCGsonGetRequest;
import com.thinkpace.pacifyca.utils.CPCAppCommonUtility;
import com.thinkpace.pacifyca.utils.CPCAppConstants;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

public class APCTimeTable extends APCBaseActivity implements Response.ErrorListener, Response.Listener<IPCDataModel> {
    @BindView(R.id.viewpager)
    ViewPager mViewPager;

    @BindView(R.id.tabs)
    TabLayout mTabLayout;

    @BindView(R.id.time_table_layout)
    RelativeLayout mTimeTablelyt;

    @BindView(R.id.no_connection_layout)
    LinearLayout mNoConnectionLyt;

    @BindView(R.id.no_messages_lyt)
    LinearLayout mNoMessagesLyt;

    @BindView(R.id.retry_btn)
    Button mRetrybtn;

    private ProgressDialog mProgressDialog;
    private CPCTImeTablePagerAdapter mTimeTablePagerAdapter;
    private Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_time_table);
        ButterKnife.bind(this);
        initToolBar();
        initUI();
        loadTimeTable();
    }

    private void initToolBar() {
        mToolbar = (Toolbar) findViewById(R.id.toolbar_actionbar);
        ((TextView) mToolbar.findViewById(R.id.title)).setText(R.string.time_table);
        initialiseToolbar((Toolbar) findViewById(R.id.toolbar_actionbar));
        getSupportActionBar().setDisplayShowHomeEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        // getSupportActionBar().setTitle(R.string.time_table);
    }

    private void initUI() {
        mRetrybtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });
        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setMessage("Please wait...");
        mProgressDialog.setCancelable(false);
        mViewPager = (ViewPager) findViewById(R.id.viewpager);
        mTabLayout = (TabLayout) findViewById(R.id.tabs);
    }

    @Override
    public void onErrorResponse(VolleyError error) {
        CPCAppCommonUtility.log("response", "error" + error.getMessage());
        hideProgressDialog();
        if (error != null)
            CPCAppCommonUtility.handleErrorResponse(this, error.getAlertTitle(), error.getAlertMessage());
        else
            CPCAppCommonUtility.handleErrorResponse(this, null, null);
    }

    @Override
    public void onResponse(IPCDataModel response) {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            hideProgressDialog();
            if (response instanceof CPCTimeTableModel) {
                CPCTimeTableModel timeTableModel = (CPCTimeTableModel) response;
                if (timeTableModel != null && timeTableModel.getData() != null && timeTableModel.getData().size() > 0) {
                    mNoMessagesLyt.setVisibility(View.GONE);
                    mTimeTablelyt.setVisibility(View.VISIBLE);
                    setTimeTableAdapter(timeTableModel.getData());
                } else {
                    mNoMessagesLyt.setVisibility(View.VISIBLE);
                    mTimeTablelyt.setVisibility(View.GONE);
                }
            }
        }
    }

    private void setTimeTableAdapter(ArrayList<CPCTimeTableData> data) {
        mTimeTablePagerAdapter = new CPCTImeTablePagerAdapter(getSupportFragmentManager(), data);
        mViewPager.setAdapter(mTimeTablePagerAdapter);
        mViewPager.setOffscreenPageLimit(3);
        mTabLayout.setupWithViewPager(mViewPager);
    }


    //Load time table
    private void loadTimeTable() {
        if (CPCAppCommonUtility.isNetworkAvailable(this)) {
            mTimeTablelyt.setVisibility(View.VISIBLE);
            mNoConnectionLyt.setVisibility(View.GONE);
            String parentId = CPCAppCommonUtility.getUserId(this);
            String clientId = CPCAppCommonUtility.getClientId(this, CPCAppConstants.KEY_CLIENT_ID);
            String studentId = CPCAppCommonUtility.getStudentId(this, CPCAppConstants.KEY_STUDENT_ID);
            String url = CPCAppConstants.KEY_BASE_URL + "/parent/" + parentId + "/client/" + clientId + "/student/" + studentId + "/course-time-table";// CPCAppConstants.KEY_GET_ASSIGNMENTS_URL;
            Map<String, String> headers = new HashMap<>();
            headers.put(CPCAppConstants.KEY_HEADER_API_KEY, BuildConfig.API_KEY);
            headers.put(CPCAppConstants.KEY_AUTHORIZATION, CPCAppCommonUtility.getAccessToken(this));
            showProgressDialog();
            CPCVolley.getRequestQueue(getApplicationContext()).add(
                    new CPCGsonGetRequest(url, this, this,
                            new CPCTimeTableModel(), headers));
        } else {
            mTimeTablelyt.setVisibility(View.GONE);
            mNoConnectionLyt.setVisibility(View.VISIBLE);
        }
    }

    private void showProgressDialog() {
        if (!mProgressDialog.isShowing())
            mProgressDialog.show();
    }

    private void hideProgressDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing())
            mProgressDialog.dismiss();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }


}
