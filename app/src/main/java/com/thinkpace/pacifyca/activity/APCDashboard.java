package com.thinkpace.pacifyca.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.firebase.iid.FirebaseInstanceId;
import com.thinkpace.pacifyca.Adapter.CPCUserMessagesAdapter;
import com.thinkpace.pacifyca.BuildConfig;
import com.thinkpace.pacifyca.R;
import com.thinkpace.pacifyca.app.CPCVolley;
import com.thinkpace.pacifyca.app.CTVNavigationDrawer;
import com.thinkpace.pacifyca.calander.APCInstituteCalendarActivity;
import com.thinkpace.pacifyca.entity.CPCAssignment;
import com.thinkpace.pacifyca.entity.CPCAssignmentData;
import com.thinkpace.pacifyca.entity.CPCAssignmentList;
import com.thinkpace.pacifyca.entity.CPCDeleteMessages;
import com.thinkpace.pacifyca.entity.CPCMessageDetails;
import com.thinkpace.pacifyca.entity.CPCNotice;
import com.thinkpace.pacifyca.entity.CPCNoticeData;
import com.thinkpace.pacifyca.entity.CPCNoticeList;
import com.thinkpace.pacifyca.entity.CPCNotificationTokenUpdateModel;
import com.thinkpace.pacifyca.entity.CPCParentDetailsModel;
import com.thinkpace.pacifyca.entity.CPCStudentDetails;
import com.thinkpace.pacifyca.entity.CPCStudents;
import com.thinkpace.pacifyca.entity.CPCUserMessages;
import com.thinkpace.pacifyca.entity.IPCDataModel;
import com.thinkpace.pacifyca.fragment.DrawerFragment;
import com.thinkpace.pacifyca.fragment.FPCStudentsDialogFragment;
import com.thinkpace.pacifyca.net.CPCGsonGetRequest;
import com.thinkpace.pacifyca.net.CPCGsonPostRequest;
import com.thinkpace.pacifyca.utils.CPCAppCommonUtility;
import com.thinkpace.pacifyca.utils.CPCAppConstants;
import com.thinkpace.pacifyca.utils.CPCLoginUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

public class APCDashboard extends APCBaseActivity implements Response.ErrorListener, FPCStudentsDialogFragment.OnStudentsDialogClickListener, Response.Listener<IPCDataModel>, DrawerFragment.IJRDrawerListener, View.OnClickListener {


    @BindView(R.id.menu_btn_about)
    Button aboutMenu;

    @BindView(R.id.menu_btn_news)
    Button newsMenu;

    @BindView(R.id.menu_btn_sms)
    Button smsMenu;

    @BindView(R.id.menu_btn_notice)
    Button noticeMenu;

    @BindView(R.id.menu_btn_assignment)
    Button assignmentMenu;

    @BindView(R.id.menu_btn_calendar)
    Button calendarMenu;

    @BindView(R.id.menu_btn_video)
    Button videoMenu;

    @BindView(R.id.menu_btn_exam)
    Button examMenu;

    @BindView(R.id.no_connection_layout)
    LinearLayout mNoConnectionLyt;

    @BindView(R.id.badge_notification_assignment)
    TextView badgeAssignment;

    @BindView(R.id.badge_notification_news)
    TextView badgeNews;

    @BindView(R.id.badge_notification_notice)
    TextView badgeNotice;

    @BindView(R.id.badge_notification_exam)
    TextView badgeExam;

    @BindView(R.id.badge_notification_sms)
    TextView badgeSMS;

    private CTVNavigationDrawer mNavigationDrawerBuilder;
    private boolean doubleBackToExitPressedOnce;
    private CPCUserMessagesAdapter mMessagesAdapter;

    private FPCStudentsDialogFragment mStudentsDialogFragment;
    private String TAG_AUTHOR_FRAGMENT = "FPCStudentsDialogFragment";
    private ArrayList<CPCMessageDetails> mStudentMessagesList;
    private EqualSpaceItemDecoration spaceItemDecoration;

    private ArrayList<String> mStudentList;
    private int unread_news_count=0;
    private int unread_notice_count=0;
    private int unread_exam_count=0;
    private int unread_assignment_count=0;
    private int unread_message_count=0;

    // IllegalStateException
//    private boolean mShowDialog = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        ButterKnife.bind(this);
        initToolBar();
        initUI();
        loadData();
//        getStudentsList();
    }

    @Override
    protected void onStart() {
        super.onStart();
        setBadgeNotificationCount();
    }

    private void setBadgeNotificationCount() {
        if(CPCAppConstants.KEY_UNREAD_ASSIGNMENT_COUNT>0){
            badgeAssignment.setText(""+CPCAppConstants.KEY_UNREAD_ASSIGNMENT_COUNT);
            badgeAssignment.setVisibility(View.VISIBLE);
        }else{
            badgeAssignment.setVisibility(View.GONE);
        }
        if(CPCAppConstants.KEY_UNREAD_NEWS_COUNT>0){
            badgeNews.setText(""+CPCAppConstants.KEY_UNREAD_NEWS_COUNT);
            badgeNews.setVisibility(View.VISIBLE);
        }else{
            badgeNews.setVisibility(View.GONE);
        }
        if(CPCAppConstants.KEY_UNREAD_NOTICES_COUNT>0){
            badgeNotice.setText(""+CPCAppConstants.KEY_UNREAD_NOTICES_COUNT);
            badgeNotice.setVisibility(View.VISIBLE);
        }else{
            badgeNotice.setVisibility(View.GONE);
        }
        if(CPCAppConstants.KEY_UNREAD_EXAM_COUNT>0){
            badgeExam.setText(""+CPCAppConstants.KEY_UNREAD_EXAM_COUNT);
            badgeExam.setVisibility(View.VISIBLE);
        }else{
            badgeExam.setVisibility(View.GONE);
        }
        if(CPCAppConstants.KEY_UNREAD_MESSAGE_COUNT>0){
            badgeSMS.setText(""+CPCAppConstants.KEY_UNREAD_MESSAGE_COUNT);
            badgeSMS.setVisibility(View.VISIBLE);
        }else{
            badgeSMS.setVisibility(View.GONE);
        }
    }

    private void loadData() {
        loadUserDetails();

        if (CPCAppCommonUtility.isNetworkAvailable(getApplicationContext())) {
            mNoConnectionLyt.setVisibility(View.GONE);
            loadCatalog();
        } else {
            mNoConnectionLyt.setVisibility(View.VISIBLE);
        }
    }

    private void loadUserDetails() {
        if (CPCAppCommonUtility.isNetworkAvailable(this)) {
            mNoConnectionLyt.setVisibility(View.GONE);
            String url = CPCAppConstants.KEY_GET_USER_DETAILS_URL;
            Map<String, String> headers = new HashMap<>();
            headers.put(CPCAppConstants.KEY_HEADER_API_KEY, BuildConfig.API_KEY);
            headers.put(CPCAppConstants.KEY_AUTHORIZATION, CPCAppCommonUtility.getAccessToken(this));
            showProgressbar(true);
            CPCVolley.getRequestQueue(getApplicationContext()).add(
                    new CPCGsonGetRequest(url, this, this,
                            new CPCParentDetailsModel(), headers));
        } else {
            mNoConnectionLyt.setVisibility(View.VISIBLE);
        }
    }


    private void initUI() {
        aboutMenu.setOnClickListener(this);
        newsMenu.setOnClickListener(this);
        smsMenu.setOnClickListener(this);
        noticeMenu.setOnClickListener(this);
        assignmentMenu.setOnClickListener(this);
        calendarMenu.setOnClickListener(this);
        videoMenu.setOnClickListener(this);
        examMenu.setOnClickListener(this);
    }


    private void showStudentsDialog() {
        //if (mShowDialog) {
//            mShowDialog = false;
            android.support.v4.app.FragmentManager manager = getSupportFragmentManager();
            mStudentsDialogFragment = new FPCStudentsDialogFragment();
            if (mStudentsDialogFragment != null) {
                mStudentsDialogFragment.setCancelable(false);
                mStudentsDialogFragment.setDialogListener(this);
                mStudentsDialogFragment.show(manager, TAG_AUTHOR_FRAGMENT);
            }
       // }
    }

//    private void setMessagesListAdapter(ArrayList<CPCMessageDetails> messagesList) {
//        List<String> messageIdsList = (List<String>) CPCAppCommonUtility.readObject(this, CPCAppConstants.KEY_FAVOURITE_MESSAGE_ID_LIST);
//        if (messageIdsList != null && messageIdsList.size() > 0) {
//            for (String messageId : messageIdsList) {
//                for (CPCMessageDetails messageDetail : messagesList) {
//                    if (!TextUtils.isEmpty(messageId) && messageDetail != null && !TextUtils.isEmpty(messageDetail.getId()) && messageDetail.getId().equals(messageId)) {
//                        messageDetail.setFavourite(true);
//                    }
//                }
//            }
//        }
//
//        List<String> readMessageIdsList = (List<String>) CPCAppCommonUtility.readObject(this, CPCAppConstants.KEY_READ_MESSAGE_ID_LIST);
//        if (readMessageIdsList != null && readMessageIdsList.size() > 0) {
//            for (String messageId : readMessageIdsList) {
//                for (CPCMessageDetails messageDetail : messagesList) {
//                    if (!TextUtils.isEmpty(messageId) && messageDetail != null && !TextUtils.isEmpty(messageDetail.getId()) && messageDetail.getId().equals(messageId)) {
//                        messageDetail.setRead(true);
//                    }
//                }
//            }
//        }
//        mMessagesAdapter = new CPCUserMessagesAdapter(this, messagesList);
//        mMessagesAdapter.setOnMessagesClickListener(this);
//        mStudentsRecycler.setAdapter(mMessagesAdapter);
//    }

    private void initToolBar() {
        initialiseToolbar((Toolbar) findViewById(R.id.toolbar_actionbar));
        getSupportActionBar().setDisplayShowHomeEnabled(true);
//        getSupportActionBar().setHomeAsUpIndicator(R.drawable.nav_icon);
        //specify all the flavors here
        if (BuildConfig.FLAVOR.equals("aac")){
            getSupportActionBar().setTitle("Anand Ashram School");
            getSupportActionBar().setDisplayShowTitleEnabled(true);
        } else if (BuildConfig.FLAVOR.equals("rotary")){
            getSupportActionBar().setTitle("Rotary Institutions Moodbidri");
            getSupportActionBar().setDisplayShowTitleEnabled(true);
        } else if (BuildConfig.FLAVOR.equals("joyland")){
            getSupportActionBar().setTitle("Joyland Play Home");
            getSupportActionBar().setDisplayShowTitleEnabled(true);
        }else if (BuildConfig.FLAVOR.equals("fmmc")){
            getSupportActionBar().setTitle("Fr. Muller Medical College");
            getSupportActionBar().setDisplayShowTitleEnabled(true);
        }else if (BuildConfig.FLAVOR.equals("demo")){
            getSupportActionBar().setTitle("");
            getSupportActionBar().setDisplayShowTitleEnabled(false);
//
//            ActionBar actionBar = getActionBar();
//            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//            actionBar.setHomeAsUpIndicator(R.drawable.ic_drawer);
        } else {
//            Pacifyca
            getSupportActionBar().setDisplayShowTitleEnabled(false);
            getSupportActionBar().setIcon(R.drawable.horizontal_logo);

        }

        mNavigationDrawerBuilder = new CTVNavigationDrawer
                .Builder()
                .setToolbar((Toolbar) findViewById(R.id.toolbar_actionbar))
                .setDrawerLayout((DrawerLayout) findViewById(R.id.drawer_layout))
                .setContainerResID(R.id.drawer_container)
                .build();
        mNavigationDrawerBuilder.initializeNavigationDrawer(this);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.nav_icon);
    }

    private void loadCatalog() {

    }

    @Override
    public void onErrorResponse(VolleyError error) {
        CPCAppCommonUtility.log("response", "error" + error.getMessage());
        showProgressbar(false);
        if (error != null)
            CPCAppCommonUtility.handleErrorResponse(this, error.getAlertTitle(), error.getAlertMessage());
        else
            CPCAppCommonUtility.handleErrorResponse(this, null, null);
    }

    @Override
    protected void onResume() {
        if (mNavigationDrawerBuilder != null) {
            mNavigationDrawerBuilder.setChildName();
        }
        super.onResume();
    }

//    @Override
//    protected void onResumeFragments() {
//        super.onResumeFragments();
//    }

    @Override
    public void onResponse(IPCDataModel response) {
        if (response instanceof CPCParentDetailsModel) {
            CPCParentDetailsModel cpcParentDetailsModel = (CPCParentDetailsModel) response;
            if (cpcParentDetailsModel != null) {
                cpcParentDetailsModel.getParentData().getUserId();
                storeUserId(cpcParentDetailsModel);
                CPCStudentDetails student = (CPCStudentDetails) CPCAppCommonUtility.readObject(this, CPCAppConstants.KEY_SELECTED_STUDENT);
                if (student == null)
                    showStudentsDialog();
//                    mShowDialog = true;
//                loadUserMessages();
                updateNotificationToken();
                getStudentsList();
            }
        } else if (response instanceof CPCUserMessages) {
            showProgressbar(false);
            CPCUserMessages userMessages = (CPCUserMessages) response;
            if (userMessages != null && userMessages.getMessagesList() != null && userMessages.getMessagesList().size() > 0) {
//                mNoMessagesLyt.setVisibility(View.GONE);
//                mSwipeRefreshLayout.setVisibility(View.VISIBLE);
                mStudentMessagesList = userMessages.getMessagesList();
//                setMessagesListAdapter(userMessages.getMessagesList());
                setMessageListAdapterGetUnreadCount(userMessages.getMessagesList());
            } else {
//                mNoMessagesLyt.setVisibility(View.VISIBLE);
//                mSwipeRefreshLayout.setVisibility(View.GONE);
            }
        } else if (response instanceof CPCDeleteMessages) {
            showProgressbar(false);
            CPCDeleteMessages deleteMessages = (CPCDeleteMessages) response;
            if (deleteMessages != null && !TextUtils.isEmpty(deleteMessages.getMessage()))
                Toast.makeText(this, deleteMessages.getMessage(), Toast.LENGTH_SHORT).show();
            else
                Toast.makeText(this, "Message deleted successfully", Toast.LENGTH_SHORT).show();
//            loadUserMessages();
        } else if (response instanceof CPCNotificationTokenUpdateModel) {
//            Toast.makeText(APCHomeScreen.this, "Notification token updated successfully", Toast.LENGTH_SHORT).show();
        } else if (response instanceof CPCStudents) {
            CPCStudents students = (CPCStudents) response;
//            mStudentList.clear();
            if (students != null && students.getData() != null && students.getData().size() > 0) {
                for (int index = 0; index < students.getData().size(); index++) {
//                    mStudentList.add(students.getData().get(index).getStudent_id());
                    getNoticeList(students.getData().get(index).getStudent_id(), students.getData().get(index).getClient_id());
                    getAssignmentList(students.getData().get(index).getStudent_id(), students.getData().get(index).getClient_id());
                }
            }
        } else if (response instanceof CPCNotice) {
            CPCNotice cpcNoticeModel = (CPCNotice) response;
            if (cpcNoticeModel != null && cpcNoticeModel.getData() != null && cpcNoticeModel.getData().size() > 0) {
                CPCNoticeData noticeData = new CPCNoticeData();
                ArrayList<CPCNoticeList> tempList = new ArrayList<>();
                        for (CPCNoticeData data : cpcNoticeModel.getData()) {
                            for (CPCNoticeList noticeDetail : data.getNoticeList()) {
                                tempList.add(noticeDetail);
                            }
                        }
                    setNoticeListAdapter(tempList);
            }
        } else if(response instanceof CPCAssignment){
            CPCAssignment cpcAssignmentModel = (CPCAssignment) response;
            if (cpcAssignmentModel != null && cpcAssignmentModel.getData() != null && cpcAssignmentModel.getData().size() > 0) {
                CPCAssignmentData assignmentData = new CPCAssignmentData();
                ArrayList<CPCAssignmentList> asignmentTempList = new ArrayList<>();
                for (CPCAssignmentData data : cpcAssignmentModel.getData()) {
                    for (CPCAssignmentList assignmentDetail : data.getAssignmentList()) {
                        asignmentTempList.add(assignmentDetail);
                    }
                }
                setAssignmentListAdapter(asignmentTempList);
            }
        }
    }

    private void setNoticeListAdapter(ArrayList<CPCNoticeList> noticeList) {
        List<String> noticeIdsList = (List<String>) CPCAppCommonUtility.readObject(this, CPCAppConstants.KEY_READ_NOTICE_ID_LIST);
        if (noticeIdsList != null && noticeIdsList.size() > 0) {
//            for (String noticeId : noticeIdsList) {
                for (CPCNoticeList noticeDetail : noticeList) {
                    String noticeExistId = noticeDetail.getNoticedNo();
                    Boolean exist = false;
                    for (String noticeId : noticeIdsList) {
                        if (!TextUtils.isEmpty(noticeId) && noticeExistId != null && noticeExistId.equals(noticeId)) {
                            exist=true;
                        }
                    }
                    if(exist==false){
                        if(noticeDetail.getType().equals(CPCAppConstants.KEY_NEWS_TYPE)){
                            ++unread_news_count;
                        }else if(noticeDetail.getType().equals(CPCAppConstants.KEY_CIRCULAR_TYPE)){
                            ++unread_notice_count;
                        }else if(noticeDetail.getType().equals(CPCAppConstants.KEY_EXAM_TYPE)){
                            ++unread_exam_count;
                        }
                    }
                    CPCAppConstants.KEY_UNREAD_NOTICES_COUNT = unread_notice_count;
                    CPCAppConstants.KEY_UNREAD_NEWS_COUNT = unread_news_count;
                    CPCAppConstants.KEY_UNREAD_EXAM_COUNT = unread_exam_count;
                }
        }else{
            for (CPCNoticeList noticeDetail : noticeList) {
                if(noticeDetail.getType().equals(CPCAppConstants.KEY_NEWS_TYPE)){
                    ++unread_news_count;
                }else if(noticeDetail.getType().equals(CPCAppConstants.KEY_CIRCULAR_TYPE)){
                    ++unread_notice_count;
                }else if(noticeDetail.getType().equals(CPCAppConstants.KEY_EXAM_TYPE)){
                    ++unread_exam_count;
                }
            }
            CPCAppConstants.KEY_UNREAD_NOTICES_COUNT = unread_notice_count;
            CPCAppConstants.KEY_UNREAD_NEWS_COUNT = unread_news_count;
            CPCAppConstants.KEY_UNREAD_EXAM_COUNT = unread_exam_count;
        }
        setBadgeNotificationCount();
        CPCAppCommonUtility.setShortcutBadger(this);
    }

    private void setAssignmentListAdapter(ArrayList<CPCAssignmentList> assignmentLists) {
        List<String> assignmentIdsList = (List<String>) CPCAppCommonUtility.readObject(this, CPCAppConstants.KEY_READ_ASSIGNMENT_ID_LIST);
        if (assignmentIdsList != null && assignmentIdsList.size() > 0) {
            for (CPCAssignmentList assignmentDetail : assignmentLists) {
                String assignmentExistId = assignmentDetail.getAssignedNo();
                Boolean exist = false;
                for (String assignmentId : assignmentIdsList) {
                    if (!TextUtils.isEmpty(assignmentId) && assignmentExistId != null && assignmentExistId.equals(assignmentId)) {
                        exist=true;
                    }
                }
                if(exist==false)
                    ++unread_assignment_count;
                CPCAppConstants.KEY_UNREAD_ASSIGNMENT_COUNT = unread_assignment_count;
            }
        }else{
            CPCAppConstants.KEY_UNREAD_ASSIGNMENT_COUNT = assignmentLists.size();
        }
        setBadgeNotificationCount();
        CPCAppCommonUtility.setShortcutBadger(this);
    }

    private void setMessageListAdapterGetUnreadCount(ArrayList<CPCMessageDetails> messagesList) {

        List<String> readMessageIdsList = (List<String>) CPCAppCommonUtility.readObject(this, CPCAppConstants.KEY_READ_MESSAGE_ID_LIST);
        if (readMessageIdsList != null && readMessageIdsList.size() > 0) {
            for (CPCMessageDetails messageDetail : messagesList) {
                String messageExistId = messageDetail.getId();
                Boolean exist = false;
                for (String messageId : readMessageIdsList) {
                    if (!TextUtils.isEmpty(messageId) && messageExistId != null && messageExistId.equals(messageId)) {
                        exist=true;
                    }
                }
                if(exist==false)
                    ++unread_message_count;
                CPCAppConstants.KEY_UNREAD_MESSAGE_COUNT = unread_message_count;
            }
        }else{
            CPCAppConstants.KEY_UNREAD_MESSAGE_COUNT = messagesList.size();
        }
        setBadgeNotificationCount();
        CPCAppCommonUtility.setShortcutBadger(this);
    }

    private void updateNotificationToken() {
        if (CPCAppCommonUtility.isNetworkAvailable(this)) {
            mNoConnectionLyt.setVisibility(View.GONE);
            String url = CPCAppConstants.KEY_BASE_URL + "/user-token";
            JSONObject jsonObject = new JSONObject();
            String deviceToken = FirebaseInstanceId.getInstance().getToken();
            Map<String, String> headers = new HashMap<>();
            headers.put(CPCAppConstants.KEY_HEADER_API_KEY, BuildConfig.API_KEY);
            headers.put(CPCAppConstants.KEY_AUTHORIZATION, CPCAppCommonUtility.getAccessToken(this));
            showProgressbar(true);

            try {
                jsonObject.put(CPCAppConstants.KEY_USER_TOKEN, deviceToken);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            CPCVolley.getRequestQueue(getApplicationContext()).add(
                    new CPCGsonPostRequest(url, this, this,
                            new CPCNotificationTokenUpdateModel(), null, headers, jsonObject.toString(), Request.Method.POST));
        } else {
            mNoConnectionLyt.setVisibility(View.VISIBLE);
        }
    }

    private void storeUserId(CPCParentDetailsModel loginModel) {
        try {
            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
            SharedPreferences.Editor editor = preferences.edit();
            editor.putString(CPCAppConstants.KEY_USER_ID, String.valueOf(loginModel.getParentData().getId()));
            editor.apply();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

//    private void loadUserMessages() {
//        String parentId = CPCAppCommonUtility.getUserId(this);
//        String clientId = CPCAppCommonUtility.getClientId(this, CPCAppConstants.KEY_CLIENT_ID);
//        if (CPCAppCommonUtility.isNetworkAvailable(this) && !TextUtils.isEmpty(parentId)) {
//            mNoConnectionLyt.setVisibility(View.GONE);
//            String url = CPCAppConstants.KEY_BASE_URL+"/parent/" + parentId + "/messages";
//            Map<String, String> headers = new HashMap<>();
//            headers.put(CPCAppConstants.KEY_HEADER_API_KEY, BuildConfig.API_KEY);
//            //prajna
//            headers.put(CPCAppConstants.KEY_CLIENT_ID,clientId);
//            CPCAppCommonUtility.log(CPCAppConstants.KEY_HEADER_API_KEY, BuildConfig.API_KEY);
//            headers.put(CPCAppConstants.KEY_AUTHORIZATION, CPCAppCommonUtility.getAccessToken(this));
//            CPCAppCommonUtility.log(CPCAppConstants.KEY_AUTHORIZATION, CPCAppCommonUtility.getAccessToken(this));
//            showProgressbar(true);
//            CPCVolley.getRequestQueue(getApplicationContext()).add(
//                    new CPCGsonGetRequest(url, this, this,
//                            new CPCUserMessages(), headers));
//        } else {
//            mNoConnectionLyt.setVisibility(View.VISIBLE);
//        }
//    }


    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }
        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Press back again to close",
                Toast.LENGTH_SHORT).show();
        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce = false;
            }
        }, 10000);
    }

    @Override
    public void onDrawerClose() {
        mNavigationDrawerBuilder.closeDrawers();
    }

    @Override
    public void onLogoutClick() {
        CPCLoginUtils.clearPreference(this);
        CPCAppCommonUtility.deleteObject(this, CPCAppConstants.KEY_SELECTED_STUDENT);
        startLoginScreen();
    }

    private void startLoginScreen() {
        Intent intent = new Intent(APCDashboard.this, APCLoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    @Override
    public void onStudentSelected(CPCStudentDetails item) {
        if (mNavigationDrawerBuilder != null) {
            mNavigationDrawerBuilder.setChildName();
//            loadUserMessages();
        }
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
//            mShowDialog = true;
            showStudentsDialog();
        }
        return super.onOptionsItemSelected(item);
    }

    private void showProgressbar(boolean visibility) {
//        if (progressBar != null) {
//            if (visibility)
//                progressBar.setVisibility(View.VISIBLE);
//            else
//                progressBar.setVisibility(View.GONE);
//        }
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id){
            case R.id.menu_btn_about:
                handleAboutClick();
                break;
            case R.id.menu_btn_news:
                handleNewsClick();
                break;
            case R.id.menu_btn_sms:
                handleSMSClick();
                break;
            case R.id.menu_btn_notice:
                handleNoticeClick();
                break;
            case R.id.menu_btn_assignment:
                handleAssignmentClick();
                break;
            case R.id.menu_btn_calendar:
                handleACalendarClick();
                break;
            case R.id.menu_btn_video:
                handleVideoClick();
                break;
            case R.id.menu_btn_exam:
                handleExamClick();
                break;
        }

    }

    private void handleExamClick() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (this == null) {
                    return;
                }
                Intent noticeIntent = new Intent(APCDashboard.this, APCNotice.class);
                noticeIntent.putExtra(CPCAppConstants.KEY_NOTICE_TYPE, CPCAppConstants.KEY_EXAM_TYPE); //Optional parameters
                APCDashboard.this.startActivity(noticeIntent);
            }
        }, 400);
    }

    private void handleVideoClick() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (this == null) {
                    return;
                }
                startActivity(new Intent(APCDashboard.this, APCVideos.class));
            }
        }, 400);
    }

    private void handleACalendarClick() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (this == null) {
                    return;
                }
                startActivity(new Intent(APCDashboard.this, APCInstituteCalendarActivity.class));
            }
        }, 400);
    }

    private void handleAssignmentClick() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (this == null) {
                    return;
                }
                startActivity(new Intent(APCDashboard.this, APCAssignments.class));
            }
        }, 400);
    }

    private void handleNoticeClick() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (this == null) {
                    return;
                }
                Intent noticeIntent = new Intent(APCDashboard.this, APCNotice.class);
                noticeIntent.putExtra(CPCAppConstants.KEY_NOTICE_TYPE, CPCAppConstants.KEY_CIRCULAR_TYPE); //Optional parameters
                APCDashboard.this.startActivity(noticeIntent);
            }
        }, 400);
    }

    private void handleSMSClick() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (this == null) {
                    return;
                }
                startActivity(new Intent(APCDashboard.this, APCHomeScreen.class));
            }
        }, 400);
    }

    private void handleNewsClick() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (this == null) {
                    return;
                }

                Intent noticeIntent = new Intent(APCDashboard.this, APCNotice.class);
                noticeIntent.putExtra(CPCAppConstants.KEY_NOTICE_TYPE, CPCAppConstants.KEY_NEWS_TYPE); //Optional parameters
                startActivity(noticeIntent);
            }
        }, 400);
    }

    private void handleAboutClick() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (this == null) {
                    return;
                }
                Intent intent = new Intent(APCDashboard.this, APCWebViewActivity.class);
                intent.putExtra("WEB_URL", getString(R.string.web_view_url));
                intent.putExtra("PAGE_TITLE", "About us");
                startActivity(intent);
            }
        }, 250);
    }

//    @Override
//    public void onMessageItemClick(int position, CPCMessageDetails messageDetails) {
//
//        Intent intent = new Intent(this, APCMessageDetails.class);
//        intent.putExtra(CPCAppConstants.MESSAGE_DETAILS, messageDetails);
//        readUserMessage(position,messageDetails);
//        startActivity(intent);
//
//    }

    //Prajna
//    private void readUserMessage(int position, CPCMessageDetails messageDetails){
//        List<String> messageIdsLists = (List<String>) CPCAppCommonUtility.readObject(this, CPCAppConstants.KEY_READ_MESSAGE_ID_LIST);
//        String readMessageId = null;
//        if (messageIdsLists != null && messageIdsLists.size() > 0 && messageDetails != null && !TextUtils.isEmpty(messageDetails.getId())) {
//            for (String messageId : messageIdsLists) {
//                if (messageId.equals(messageDetails.getId())) {
//                    readMessageId = messageId;
//                    break;
//                }
//            }
//            if (TextUtils.isEmpty(readMessageId)) {
//                if(CPCAppConstants.KEY_UNREAD_MESSAGE_COUNT > 0){
//                    CPCAppConstants.KEY_UNREAD_MESSAGE_COUNT = CPCAppConstants.KEY_UNREAD_MESSAGE_COUNT - 1;
//                    CPCAppCommonUtility.setShortcutBadger(this);
//                }
//                messageDetails.setRead(true);
//                messageIdsLists.add(messageDetails.getId());
//                updateReadMessageIdsList(messageIdsLists);
//            }else{
//                messageDetails.setRead(true);
//                messageIdsLists.add(messageDetails.getId());
//                updateReadMessageIdsList(messageIdsLists);
//            }
//        }else{
//            messageIdsLists = new ArrayList<>();
//            messageIdsLists.add(messageDetails.getId());
//            messageDetails.setRead(true);
//            updateReadMessageIdsList(messageIdsLists);
//        }
//        mMessagesAdapter.updateAdapter(mStudentMessagesList);
//    }

//    @Override
//    public void onFavouritesClick(int position, CPCMessageDetails messageDetails) {
//        List<String> messageIdsList = (List<String>) CPCAppCommonUtility.readObject(this, CPCAppConstants.KEY_FAVOURITE_MESSAGE_ID_LIST);
//        String favMessageId = null;
//        if (messageIdsList != null && messageIdsList.size() > 0 && messageDetails != null && !TextUtils.isEmpty(messageDetails.getId())) {
//            for (String messageId : messageIdsList) {
//                if (messageId.equals(messageDetails.getId())) {
//                    favMessageId = messageId;
//                    break;
//                }
//            }
//            if (TextUtils.isEmpty(favMessageId)) {
//                messageDetails.setFavourite(true);
//                messageIdsList.add(messageDetails.getId());
//
//            } else {
//                messageDetails.setFavourite(false);
//                messageIdsList.remove(favMessageId);
//            }
//            updateMessageIdsList(messageIdsList);
//        } else {
//            messageIdsList = new ArrayList<>();
//            messageIdsList.add(messageDetails.getId());
//            messageDetails.setFavourite(true);
//            updateMessageIdsList(messageIdsList);
//        }
//
//        mMessagesAdapter.updateAdapter(mStudentMessagesList);
//    }

//    private void updateMessageIdsList(List<String> messageIdsList) {
//        CPCAppCommonUtility.deleteObject(this, CPCAppConstants.KEY_FAVOURITE_MESSAGE_ID_LIST);
//        CPCAppCommonUtility.writeObject(this, CPCAppConstants.KEY_FAVOURITE_MESSAGE_ID_LIST, messageIdsList);
//    }
//
//    //Prajna
//    private void updateReadMessageIdsList(List<String> messageIdsList){
//        CPCAppCommonUtility.deleteObject(this, CPCAppConstants.KEY_READ_MESSAGE_ID_LIST);
//        CPCAppCommonUtility.writeObject(this, CPCAppConstants.KEY_READ_MESSAGE_ID_LIST, messageIdsList);
//    }

//    @Override
//    public void onDeleteMessageClick(int position, final CPCMessageDetails messageDetails) {
//        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                switch (which) {
//                    case DialogInterface.BUTTON_POSITIVE:
//
//                        DeleteUserMessage(messageDetails);
//                        break;
//
//                    case DialogInterface.BUTTON_NEGATIVE:
//                        dialog.dismiss();
//                        break;
//                }
//            }
//        };
//
//        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
//        builder.setMessage("Are you sure you want to delete the message?").setPositiveButton("Yes", dialogClickListener)
//                .setNegativeButton("No", dialogClickListener).show();
//    }

//    private void DeleteUserMessage(CPCMessageDetails messageDetails) {
//        String parentId = CPCAppCommonUtility.getUserId(this);
//        if (CPCAppCommonUtility.isNetworkAvailable(this) && !TextUtils.isEmpty(parentId)) {
//            mNoConnectionLyt.setVisibility(View.GONE);
//            String url = CPCAppConstants.KEY_BASE_URL + "/parent/" + parentId + "/client/" + messageDetails.getmClientId() + "/messages/" + messageDetails.getId() + "/archive";
//            Map<String, String> headers = new HashMap<>();
//            headers.put(CPCAppConstants.KEY_HEADER_API_KEY, BuildConfig.API_KEY);
//            headers.put(CPCAppConstants.KEY_AUTHORIZATION, CPCAppCommonUtility.getAccessToken(this));
//            showProgressbar(true);
//            CPCVolley.getRequestQueue(getApplicationContext()).add(
//                    new CPCGsonPostRequest(url, this, this,
//                            new CPCDeleteMessages(), null, headers, null, Request.Method.DELETE));
//        } else {
//            mNoConnectionLyt.setVisibility(View.VISIBLE);
//        }
//    }

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

    private void getStudentsList() {
            if (CPCAppCommonUtility.isNetworkAvailable(this)) {
                showProgressbar(true);
                String parentId = CPCAppCommonUtility.getUserId(this);
                Map<String, String> headers = new HashMap<>();
                headers.put(CPCAppConstants.KEY_HEADER_API_KEY, BuildConfig.API_KEY);
                headers.put(CPCAppConstants.KEY_AUTHORIZATION, CPCAppCommonUtility.getAccessToken(this));
                headers.put("client_unique_key", "");
                //String getStudentsAPI;// = "https://dl.dropboxusercontent.com/s/cwtqdsveazyarbw/students.json?dl=0";
                String getStudentsAPI = CPCAppConstants.KEY_BASE_URL + "/parent/" + parentId + "/students";
                CPCVolley.getRequestQueue(getApplicationContext()).add(
                        new CPCGsonGetRequest(getStudentsAPI, this, this,
                                new CPCStudents(), headers));

            } else {
                mNoConnectionLyt.setVisibility(View.VISIBLE);
            }
    }

    private void getNoticeList(String stId, String ClntId) {
        if (CPCAppCommonUtility.isNetworkAvailable(this)) {
            mNoConnectionLyt.setVisibility(View.GONE);
            String parentId = CPCAppCommonUtility.getUserId(this);
//            String clientId = CPCAppCommonUtility.getClientId(this, CPCAppConstants.KEY_CLIENT_ID);
//            String studentId = CPCAppCommonUtility.getStudentId(this, sttId);
            String url = CPCAppConstants.KEY_BASE_URL + "/parent/" + parentId + "/client/" + ClntId + "/student/" + stId + "/notice-board-details";// CPCAppConstants.KEY_GET_ASSIGNMENTS_URL;
            Map<String, String> headers = new HashMap<>();
            headers.put(CPCAppConstants.KEY_HEADER_API_KEY, BuildConfig.API_KEY);
            headers.put(CPCAppConstants.KEY_AUTHORIZATION, CPCAppCommonUtility.getAccessToken(this));
//            headers.put(CPCAppConstants.KEY_NOTICE_TYPE, noticeType);
            showProgressbar(true);
            CPCVolley.getRequestQueue(getApplicationContext()).add(
                    new CPCGsonGetRequest(url, this, this,
                            new CPCNotice(), headers));
        } else {
//            mNoticeLayout.setVisibility(View.GONE);
            mNoConnectionLyt.setVisibility(View.VISIBLE);
        }
    }

    private void getAssignmentList(String stId, String ClntId){
        if (CPCAppCommonUtility.isNetworkAvailable(this)) {
            mNoConnectionLyt.setVisibility(View.GONE);
            String parentId = CPCAppCommonUtility.getUserId(this);
//            String clientId = CPCAppCommonUtility.getClientId(this, CPCAppConstants.KEY_CLIENT_ID);
//            String studentId = CPCAppCommonUtility.getStudentId(this, CPCAppConstants.KEY_STUDENT_ID);
            String url = CPCAppConstants.KEY_BASE_URL + "/parent/" + parentId + "/client/" + ClntId + "/student/" + stId + "/assignments-details";// CPCAppConstants.KEY_GET_ASSIGNMENTS_URL;
            Map<String, String> headers = new HashMap<>();
            headers.put(CPCAppConstants.KEY_HEADER_API_KEY, BuildConfig.API_KEY);
            headers.put(CPCAppConstants.KEY_AUTHORIZATION, CPCAppCommonUtility.getAccessToken(this));
            showProgressbar(true);
            CPCVolley.getRequestQueue(getApplicationContext()).add(
                    new CPCGsonGetRequest(url, this, this,
                            new CPCAssignment(), headers));
        } else {
//            mAssignmentsLayout.setVisibility(View.GONE);
            mNoConnectionLyt.setVisibility(View.VISIBLE);
        }
    }


}