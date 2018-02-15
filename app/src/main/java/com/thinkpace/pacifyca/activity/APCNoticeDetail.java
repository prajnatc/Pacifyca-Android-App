package com.thinkpace.pacifyca.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.thinkpace.pacifyca.R;
import com.thinkpace.pacifyca.entity.CPCNoticeList;
import com.thinkpace.pacifyca.utils.CPCAppConstants;
import com.thinkpace.pacifyca.widget.RoboTextView;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by prajna on 25-10-2017.
 */

public class APCNoticeDetail extends APCBaseActivity {

    @BindView(R.id.notice_detail_layout)
    LinearLayout mNoticeDetailLyt;
    @BindView(R.id.no_messages_lyt)
    LinearLayout mNoMsgLyt;
//    @BindView(R.id.assignee_name_text_view)
//    RoboTextView mAssigneeName;
    @BindView(R.id.expiry_date)
    RoboTextView mNoticeExpiryDate;
    @BindView(R.id.notice_title)
    RoboTextView mNoticeTitle;
    @BindView(R.id.notice_description)
    WebView mNoticeContent;
//    @BindView(R.id.assigned_date_text_view)
//    RoboTextView mNoticeDate;
    @BindView(R.id.tv_start_date)
    RoboTextView mStartDate;
    @BindView(R.id.tv_end_date)
    RoboTextView mEndDate;
    private CPCNoticeList noticeDetail;
    private Toolbar mToolbar;

   private Intent notificationIntent;

//    @BindView(R.id.subject_name)
    RoboTextView tvDetailTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_apcnotice_detail);
        tvDetailTitle = (RoboTextView) findViewById(R.id.notice_details);
        mToolbar = (Toolbar) findViewById(R.id.notice_toolbar);
        ((TextView) mToolbar.findViewById(R.id.title)).setText(R.string.notice_details);
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
        mStartDate.setVisibility(View.GONE);
        mEndDate.setVisibility(View.GONE);
        if (getIntent() != null && getIntent().hasExtra(CPCAppConstants.NOTICE_DETAILS)) {
            noticeDetail = (CPCNoticeList) getIntent().getSerializableExtra(CPCAppConstants.NOTICE_DETAILS);
            if (noticeDetail != null) {
//                mAssigneeName.setText("xxx");//noticeDetail.getAssigneeName());
                String noticeDetails = "<![CDATA[".concat(noticeDetail.getDescription());
                mNoticeContent.loadData(noticeDetails, "text/html", null);
                mNoticeContent.getSettings().setBuiltInZoomControls(true);
//                mNoticeDate.setText(noticeDetail.geNoticeddate());
                mNoticeExpiryDate.setText(noticeDetail.geNoticeddate());
                mNoticeTitle.setText(noticeDetail.getTitle());
                Intent titleIntent = getIntent();
                if(titleIntent.hasExtra(CPCAppConstants.KEY_NOTICE_TYPE)){
                    if(titleIntent.getStringExtra(CPCAppConstants.KEY_NOTICE_TYPE).equals(CPCAppConstants.KEY_NEWS_TYPE)){
                        ((TextView) mToolbar.findViewById(R.id.title)).setText(R.string.news);
                    }else if(titleIntent.getStringExtra(CPCAppConstants.KEY_NOTICE_TYPE).equals(CPCAppConstants.KEY_EXAM_TYPE)){
                        ((TextView) mToolbar.findViewById(R.id.title)).setText(R.string.exam_schedule);
//                        mStartDate.setText("Start date - ".concat(noticeDetail.getStartdate()));
//                        mEndDate.setText("End date - ".concat(noticeDetail.getExpiryDate()));
//                        mStartDate.setVisibility(View.VISIBLE);
//                        mEndDate.setVisibility(View.VISIBLE);
                    }else{
                        ((TextView) mToolbar.findViewById(R.id.title)).setText(R.string.notice);
                    }
                }
//                mSubjectName.setText("xxx");//noticeDetail.getSubName());
            }
        }else if(getIntent() != null){
            notificationIntent = getIntent();
            Bundle extras = getIntent().getExtras();
            Log.d("tag","NOTICEBOARD - " + extras);
            if (notificationIntent != null) {
                mNoticeTitle.setText(notificationIntent.getStringExtra("notification_title"));
                String noticeDetails = "<![CDATA[".concat(notificationIntent.getStringExtra("notification_body"));
                mNoticeContent.loadData(noticeDetails, "text/html", null);
                mNoticeContent.getSettings().setBuiltInZoomControls(true);
                mNoticeExpiryDate.setText(notificationIntent.getStringExtra("display_date"));
                if(notificationIntent.getStringExtra("notice_title").equals(CPCAppConstants.KEY_NEWS_TYPE)){
                    ((TextView) mToolbar.findViewById(R.id.title)).setText(R.string.news);
                }else if(notificationIntent.getStringExtra("notice_title").equals(CPCAppConstants.KEY_EXAM_TYPE)){
                    ((TextView) mToolbar.findViewById(R.id.title)).setText(R.string.exam_schedule);
                }else{
                    ((TextView) mToolbar.findViewById(R.id.title)).setText(R.string.notice);
                }
//                idOffer = notificationIntent.getStringExtra("title"); // Retrieve the id
            }
        }
        if (noticeDetail != null || notificationIntent != null) {
            mNoticeDetailLyt.setVisibility(View.VISIBLE);
            mNoMsgLyt.setVisibility(View.GONE);
        } else {
            mNoMsgLyt.setVisibility(View.VISIBLE);
            mNoticeDetailLyt.setVisibility(View.GONE);
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
