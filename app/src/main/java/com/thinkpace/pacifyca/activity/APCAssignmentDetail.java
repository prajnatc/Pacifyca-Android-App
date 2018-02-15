package com.thinkpace.pacifyca.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.thinkpace.pacifyca.R;
import com.thinkpace.pacifyca.entity.CPCAssignmentList;
import com.thinkpace.pacifyca.entity.CPCMessageDetails;
import com.thinkpace.pacifyca.utils.CPCAppConstants;
import com.thinkpace.pacifyca.widget.RoboTextView;

import butterknife.BindView;
import butterknife.ButterKnife;

public class APCAssignmentDetail extends APCBaseActivity {

    @BindView(R.id.assignment_detail_layout)
    LinearLayout mAssignmentDetailLyt;
    @BindView(R.id.no_messages_lyt)
    LinearLayout mNoMsgLyt;
    @BindView(R.id.assignee_name_text_view)
    RoboTextView mAssigneeName;
    @BindView(R.id.expiry_date)
    RoboTextView mAssignmentExpiryDate;
    @BindView(R.id.assignment_title)
    RoboTextView mAssignmentTitle;
    @BindView(R.id.assignment_description)
    WebView mAssignmentContent;
    @BindView(R.id.assigned_date_text_view)
    RoboTextView mAssignmentDate;
    private CPCAssignmentList assignmentDetail;
    private Toolbar mToolbar;

    @BindView(R.id.subject_name)
    RoboTextView mSubjectName;

    private Intent notificationIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_apcassignment_detail);
        mToolbar = (Toolbar) findViewById(R.id.assignment_toolbar);
        ((TextView) mToolbar.findViewById(R.id.title)).setText(R.string.assignment_details);
        initialiseToolbar(mToolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ButterKnife.bind(this);
        initToolBar();
        initData();
    }

    private void initToolBar() {
//        getSupportActionBar().setTitle(R.string.assignment_details);
    }

    private void initData() {
        if (getIntent() != null && getIntent().hasExtra(CPCAppConstants.ASSIGNMENT_DETAILS)) {
            assignmentDetail = (CPCAssignmentList) getIntent().getSerializableExtra(CPCAppConstants.ASSIGNMENT_DETAILS);
            if (assignmentDetail != null) {
                mAssigneeName.setText(assignmentDetail.getAssigneeName());
                String assignmentDetails = "<![CDATA[".concat(assignmentDetail.getDescription());
                mAssignmentContent.loadData(assignmentDetails, "text/html", null);
                mAssignmentContent.getSettings().setBuiltInZoomControls(true);
                mAssignmentDate.setText(assignmentDetail.getAssigneddate());
                mAssignmentExpiryDate.setText("Due : " + assignmentDetail.getExpiryDate());
                mAssignmentTitle.setText(assignmentDetail.getTitle());
                mSubjectName.setText(assignmentDetail.getSubName());
            }
        }else if(getIntent() != null){
            notificationIntent = getIntent();
            Bundle extras = getIntent().getExtras();
            Log.d("tag","NOTICEBOARD - " + extras);
            if (notificationIntent != null) {
                mAssigneeName.setText(notificationIntent.getStringExtra("assignee_name"));
                String assignmentDetails = "<![CDATA[".concat(notificationIntent.getStringExtra("notification_body"));
                mAssignmentContent.loadData(assignmentDetails, "text/html", null);
                mAssignmentContent.getSettings().setBuiltInZoomControls(true);
                mAssignmentDate.setText(notificationIntent.getStringExtra("assignment_date"));
                mAssignmentExpiryDate.setText("Due : " + notificationIntent.getStringExtra("display_date"));
                mAssignmentTitle.setText(notificationIntent.getStringExtra("notification_title"));
                mSubjectName.setText(notificationIntent.getStringExtra("subject_name"));
//                idOffer = notificationIntent.getStringExtra("title"); // Retrieve the id
            }
        }
        if (assignmentDetail != null || notificationIntent != null)  {
            mAssignmentDetailLyt.setVisibility(View.VISIBLE);
            mNoMsgLyt.setVisibility(View.GONE);
        } else {
            mNoMsgLyt.setVisibility(View.VISIBLE);
            mAssignmentDetailLyt.setVisibility(View.GONE);
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
}
