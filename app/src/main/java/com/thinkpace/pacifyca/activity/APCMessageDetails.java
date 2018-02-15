package com.thinkpace.pacifyca.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.thinkpace.pacifyca.R;
import com.thinkpace.pacifyca.entity.CPCMessageDetails;
import com.thinkpace.pacifyca.utils.CPCAppCommonUtility;
import com.thinkpace.pacifyca.utils.CPCAppConstants;
import com.thinkpace.pacifyca.widget.RoboTextView;

import butterknife.BindView;
import butterknife.ButterKnife;

public class APCMessageDetails extends APCBaseActivity {
    @BindView(R.id.message_detail_layout)
    LinearLayout mMsgDetailLyt;
    @BindView(R.id.no_messages_lyt)
    LinearLayout mNoMsgLyt;
    @BindView(R.id.student_name_text_view)
    RoboTextView mStudentName;
    @BindView(R.id.institution_name_text_view)
    RoboTextView mInstitutionName;
    @BindView(R.id.message_content_text_view)
    RoboTextView mMessageContent;
    @BindView(R.id.course_name_text_view)
    RoboTextView mCourseName;
    @BindView(R.id.academic_year_text_view)
    RoboTextView mAcademicYear;
    @BindView(R.id.date_text_view)
    RoboTextView mDateTextView;
    private CPCMessageDetails messageDetail;
    private String mFormattedDate;
    private Toolbar mToolbar;
    private Intent notificationIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_apcmessage_details);
        ButterKnife.bind(this);
        mToolbar = (Toolbar) findViewById(R.id.toolbar_actionbar);
        ((TextView) mToolbar.findViewById(R.id.title)).setText("Message Details");
        initialiseToolbar((Toolbar) findViewById(R.id.toolbar_actionbar));
        getSupportActionBar().setDisplayShowHomeEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        getSupportActionBar().setTitle(R.string.message_details);
        initData();
    }

    private void initData() {
        if(getIntent()!=null && getIntent().hasExtra(CPCAppConstants.MESSAGE_DETAILS)){
            messageDetail= (CPCMessageDetails) getIntent().getSerializableExtra(CPCAppConstants.MESSAGE_DETAILS);
            if(messageDetail!=null){
                mStudentName.setText(messageDetail.getStudentName());
                mInstitutionName.setText(messageDetail.getInstitutionName());
                mMessageContent.setText(messageDetail.getMessage_content());
                mCourseName.setText(messageDetail.getmCourseName());
                mAcademicYear.setText(messageDetail.getmAcademicYear());
                if(!TextUtils.isEmpty(messageDetail.getMessageTime())){
                    CPCAppCommonUtility.log("date",selectedDate(messageDetail.getMessageTime()));
                    mDateTextView.setText(selectedDate(messageDetail.getMessageTime()));
                }
            }
        }else if(getIntent() != null){
            notificationIntent = getIntent();
            Bundle extras = getIntent().getExtras();
            Log.d("tag","NOTICEBOARD - " + extras);
            if (notificationIntent != null) {
                mStudentName.setText(notificationIntent.getStringExtra("assignee_name"));
                mInstitutionName.setText(notificationIntent.getStringExtra("notification_title"));
                mCourseName.setText(notificationIntent.getStringExtra("subject_name"));
                mMessageContent.setText(notificationIntent.getStringExtra("notification_body"));
                mAcademicYear.setText(notificationIntent.getStringExtra("assignment_date"));
//                if(!TextUtils.isEmpty(notificationIntent.getStringExtra("display_date"))){
//                    CPCAppCommonUtility.log("displaydate",selectedDate(notificationIntent.getStringExtra("display_date")));
                    mDateTextView.setText(notificationIntent.getStringExtra("display_date"));
//                }
            }
        }
        if(messageDetail!=null || notificationIntent != null){
            mMsgDetailLyt.setVisibility(View.VISIBLE);
            mNoMsgLyt.setVisibility(View.GONE);
        }
        else {
            mNoMsgLyt.setVisibility(View.VISIBLE);
            mMsgDetailLyt.setVisibility(View.GONE);
        }
    }
    private String selectedDate(String date) {
        String inputFormat = "yyyy-MM-dd HH:mm:ss";//"E MMM dd HH:mm:ss Z yyyy";
        return CPCAppCommonUtility.formatDate(this, date.toString(), inputFormat, "yyyy-MM-dd HH:mm");//yyyy-MM-dd
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
