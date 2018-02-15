package com.thinkpace.pacifyca.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
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
import com.thinkpace.pacifyca.Adapter.CPCNoticeAdapter;
import com.thinkpace.pacifyca.Adapter.CPCUserMessagesAdapter;
import com.thinkpace.pacifyca.BuildConfig;
import com.thinkpace.pacifyca.R;
import com.thinkpace.pacifyca.app.CPCVolley;
import com.thinkpace.pacifyca.app.CTVNavigationDrawer;
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
import com.thinkpace.pacifyca.utils.CustomDialog;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.leolin.shortcutbadger.ShortcutBadger;

public class APCHomeScreen extends APCBaseActivity implements Response.ErrorListener, CPCUserMessagesAdapter.OnMessagesClickListener, FPCStudentsDialogFragment.OnStudentsDialogClickListener, Response.Listener<IPCDataModel>, DrawerFragment.IJRDrawerListener {

    private ArrayList<String> mSubjectsList;

    @BindView(R.id.students_recycler_view)
    RecyclerView mStudentsRecycler;

    @BindView(R.id.message_indicator)
    ProgressBar progressBar;

    @BindView(R.id.no_connection_layout)
    LinearLayout mNoConnectionLyt;

    @BindView(R.id.no_messages_lyt)
    LinearLayout mNoMessagesLyt;

    @BindView(R.id.retry_btn)
    Button mRetrybtn;

    @BindView(R.id.swipe_refresh)
    SwipeRefreshLayout mSwipeRefreshLayout;

    private CTVNavigationDrawer mNavigationDrawerBuilder;
    private boolean doubleBackToExitPressedOnce;
    private CPCUserMessagesAdapter mMessagesAdapter;

    private FPCStudentsDialogFragment mStudentsDialogFragment;
    private String TAG_AUTHOR_FRAGMENT = "FPCStudentsDialogFragment";
    private ArrayList<CPCMessageDetails> mStudentMessagesList;
    private EqualSpaceItemDecoration spaceItemDecoration;

    private ArrayList<String> mStudentList;
    private int unread_notice_count=0;
    private int unread_assignment_count=0;
    private int unread_message_count=0;
    private Toolbar mToolbar;

    // IllegalStateException
//    private boolean mShowDialog = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        initToolBar();
        initUI();
        loadData();
        mRetrybtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadData();
            }
        });
        getStudentsList();
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
        mStudentsRecycler.setLayoutManager(new LinearLayoutManager(this));
        mStudentsRecycler.setHasFixedSize(true);
        spaceItemDecoration = new EqualSpaceItemDecoration(6);
        mStudentsRecycler.addItemDecoration(spaceItemDecoration);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mSwipeRefreshLayout.setRefreshing(false);
                loadData();
            }
        });
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

    private void setMessagesListAdapter(ArrayList<CPCMessageDetails> messagesList) {
        List<String> messageIdsList = (List<String>) CPCAppCommonUtility.readObject(this, CPCAppConstants.KEY_FAVOURITE_MESSAGE_ID_LIST);
        if (messageIdsList != null && messageIdsList.size() > 0) {
            for (String messageId : messageIdsList) {
                for (CPCMessageDetails messageDetail : messagesList) {
                    if (!TextUtils.isEmpty(messageId) && messageDetail != null && !TextUtils.isEmpty(messageDetail.getId()) && messageDetail.getId().equals(messageId)) {
                        messageDetail.setFavourite(true);
                    }
                }
            }
        }

        List<String> readMessageIdsList = (List<String>) CPCAppCommonUtility.readObject(this, CPCAppConstants.KEY_READ_MESSAGE_ID_LIST);
        if (readMessageIdsList != null && readMessageIdsList.size() > 0) {
            for (String messageId : readMessageIdsList) {
                for (CPCMessageDetails messageDetail : messagesList) {
                    if (!TextUtils.isEmpty(messageId) && messageDetail != null && !TextUtils.isEmpty(messageDetail.getId()) && messageDetail.getId().equals(messageId)) {
                        messageDetail.setRead(true);
                    }
                }
            }
        }
        mMessagesAdapter = new CPCUserMessagesAdapter(this, messagesList);
        mMessagesAdapter.setOnMessagesClickListener(this);
        mStudentsRecycler.setAdapter(mMessagesAdapter);
    }

    private void initToolBar() {
        mToolbar = (Toolbar) findViewById(R.id.toolbar_actionbar);
        ((TextView) mToolbar.findViewById(R.id.title)).setText(R.string.assignments);
        initialiseToolbar(mToolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        getSupportActionBar().setTitle(R.string.assignments);
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
                storeUserId(cpcParentDetailsModel);
                CPCStudentDetails student = (CPCStudentDetails) CPCAppCommonUtility.readObject(this, CPCAppConstants.KEY_SELECTED_STUDENT);
                if (student == null)
                    showStudentsDialog();
//                    mShowDialog = true;
                loadUserMessages();
                updateNotificationToken();
            }
        } else if (response instanceof CPCUserMessages) {
            showProgressbar(false);
            CPCUserMessages userMessages = (CPCUserMessages) response;
            if (userMessages != null && userMessages.getMessagesList() != null && userMessages.getMessagesList().size() > 0) {
                mNoMessagesLyt.setVisibility(View.GONE);
                mSwipeRefreshLayout.setVisibility(View.VISIBLE);
                mStudentMessagesList = userMessages.getMessagesList();
                setMessagesListAdapter(userMessages.getMessagesList());
                setMessageListAdapterGetUnreadCount(userMessages.getMessagesList());
            } else {
                mNoMessagesLyt.setVisibility(View.VISIBLE);
                mSwipeRefreshLayout.setVisibility(View.GONE);
            }
        } else if (response instanceof CPCDeleteMessages) {
            showProgressbar(false);
            CPCDeleteMessages deleteMessages = (CPCDeleteMessages) response;
            if (deleteMessages != null && !TextUtils.isEmpty(deleteMessages.getMessage()))
                Toast.makeText(this, deleteMessages.getMessage(), Toast.LENGTH_SHORT).show();
            else
                Toast.makeText(this, "Message deleted successfully", Toast.LENGTH_SHORT).show();
            loadUserMessages();
        } else if (response instanceof CPCNotificationTokenUpdateModel) {
//            Toast.makeText(APCHomeScreen.this, "Notification token updated successfully", Toast.LENGTH_SHORT).show();
        } else if (response instanceof CPCStudents) {
            CPCStudents students = (CPCStudents) response;
//            mStudentList.clear();
            if (students != null && students.getData() != null && students.getData().size() > 0) {
                for (int index = 0; index < students.getData().size(); index++) {
//                    mStudentList.add(students.getData().get(index).getStudent_id());
                    getNoticeList(students.getData().get(index).getStudent_id());
                    getAssignmentList(students.getData().get(index).getStudent_id());
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
                    if(exist==false)
                    ++unread_notice_count;
                    //CPCAppConstants.KEY_UNREAD_NOTICE_COUNT = unread_notice_count;
                }
                CPCAppCommonUtility.setShortcutBadger(this);
        }
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
            CPCAppCommonUtility.setShortcutBadger(this);
        }
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
            CPCAppCommonUtility.setShortcutBadger(this);
        }
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

    private void loadUserMessages() {
        String parentId = CPCAppCommonUtility.getUserId(this);
        String clientId = CPCAppCommonUtility.getClientId(this, CPCAppConstants.KEY_CLIENT_ID);
        if (CPCAppCommonUtility.isNetworkAvailable(this) && !TextUtils.isEmpty(parentId)) {
            mNoConnectionLyt.setVisibility(View.GONE);
            String url = CPCAppConstants.KEY_BASE_URL+"/parent/" + parentId + "/messages";
            Map<String, String> headers = new HashMap<>();
            headers.put(CPCAppConstants.KEY_HEADER_API_KEY, BuildConfig.API_KEY);
            //prajna
            headers.put(CPCAppConstants.KEY_CLIENT_ID,clientId);
            CPCAppCommonUtility.log(CPCAppConstants.KEY_HEADER_API_KEY, BuildConfig.API_KEY);
            headers.put(CPCAppConstants.KEY_AUTHORIZATION, CPCAppCommonUtility.getAccessToken(this));
            CPCAppCommonUtility.log(CPCAppConstants.KEY_AUTHORIZATION, CPCAppCommonUtility.getAccessToken(this));
            showProgressbar(true);
            CPCVolley.getRequestQueue(getApplicationContext()).add(
                    new CPCGsonGetRequest(url, this, this,
                            new CPCUserMessages(), headers));
        } else {
            mNoConnectionLyt.setVisibility(View.VISIBLE);
        }
    }


//    @Override
//    public void onBackPressed() {
//        if (doubleBackToExitPressedOnce) {
//            super.onBackPressed();
//            return;
//        }
//        this.doubleBackToExitPressedOnce = true;
//        Toast.makeText(this, "Press back again to close",
//                Toast.LENGTH_SHORT).show();
//        new Handler().postDelayed(new Runnable() {
//
//            @Override
//            public void run() {
//                doubleBackToExitPressedOnce = false;
//            }
//        }, 10000);
//    }

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
        Intent intent = new Intent(APCHomeScreen.this, APCLoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    @Override
    public void onStudentSelected(CPCStudentDetails item) {
        if (mNavigationDrawerBuilder != null) {
            mNavigationDrawerBuilder.setChildName();
            loadUserMessages();
        }
    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        MenuInflater inflater = getMenuInflater();
//        inflater.inflate(R.menu.menu_main, menu);
//        return true;
//    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    private void showProgressbar(boolean visibility) {
        if (progressBar != null) {
            if (visibility)
                progressBar.setVisibility(View.VISIBLE);
            else
                progressBar.setVisibility(View.GONE);
        }
    }

    @Override
    public void onMessageItemClick(int position, CPCMessageDetails messageDetails) {

        Intent intent = new Intent(this, APCMessageDetails.class);
        intent.putExtra(CPCAppConstants.MESSAGE_DETAILS, messageDetails);
        readUserMessage(position,messageDetails);
        startActivity(intent);

    }

    //Prajna
    private void readUserMessage(int position, CPCMessageDetails messageDetails){
        List<String> messageIdsLists = (List<String>) CPCAppCommonUtility.readObject(this, CPCAppConstants.KEY_READ_MESSAGE_ID_LIST);
        String readMessageId = null;
        if (messageIdsLists != null && messageIdsLists.size() > 0 && messageDetails != null && !TextUtils.isEmpty(messageDetails.getId())) {
            for (String messageId : messageIdsLists) {
                if (messageId.equals(messageDetails.getId())) {
                    readMessageId = messageId;
                    break;
                }
            }
            if (TextUtils.isEmpty(readMessageId)) {
                if(CPCAppConstants.KEY_UNREAD_MESSAGE_COUNT > 0){
                    CPCAppConstants.KEY_UNREAD_MESSAGE_COUNT = CPCAppConstants.KEY_UNREAD_MESSAGE_COUNT - 1;
                    CPCAppCommonUtility.setShortcutBadger(this);
                }
                messageDetails.setRead(true);
                messageIdsLists.add(messageDetails.getId());
                updateReadMessageIdsList(messageIdsLists);
            }else{
                messageDetails.setRead(true);
                messageIdsLists.add(messageDetails.getId());
                updateReadMessageIdsList(messageIdsLists);
            }
        }else{
            messageIdsLists = new ArrayList<>();
            messageIdsLists.add(messageDetails.getId());
            if(CPCAppConstants.KEY_UNREAD_MESSAGE_COUNT > 0){
                CPCAppConstants.KEY_UNREAD_MESSAGE_COUNT = CPCAppConstants.KEY_UNREAD_MESSAGE_COUNT - 1;
                CPCAppCommonUtility.setShortcutBadger(this);
            }
            messageDetails.setRead(true);
            updateReadMessageIdsList(messageIdsLists);
        }
        mMessagesAdapter.updateAdapter(mStudentMessagesList);
    }

    @Override
    public void onFavouritesClick(int position, CPCMessageDetails messageDetails) {
        List<String> messageIdsList = (List<String>) CPCAppCommonUtility.readObject(this, CPCAppConstants.KEY_FAVOURITE_MESSAGE_ID_LIST);
        String favMessageId = null;
        if (messageIdsList != null && messageIdsList.size() > 0 && messageDetails != null && !TextUtils.isEmpty(messageDetails.getId())) {
            for (String messageId : messageIdsList) {
                if (messageId.equals(messageDetails.getId())) {
                    favMessageId = messageId;
                    break;
                }
            }
            if (TextUtils.isEmpty(favMessageId)) {
                messageDetails.setFavourite(true);
                messageIdsList.add(messageDetails.getId());

            } else {
                messageDetails.setFavourite(false);
                messageIdsList.remove(favMessageId);
            }
            updateMessageIdsList(messageIdsList);
        } else {
            messageIdsList = new ArrayList<>();
            messageIdsList.add(messageDetails.getId());
            messageDetails.setFavourite(true);
            updateMessageIdsList(messageIdsList);
        }

        mMessagesAdapter.updateAdapter(mStudentMessagesList);
    }

    private void updateMessageIdsList(List<String> messageIdsList) {
        CPCAppCommonUtility.deleteObject(this, CPCAppConstants.KEY_FAVOURITE_MESSAGE_ID_LIST);
        CPCAppCommonUtility.writeObject(this, CPCAppConstants.KEY_FAVOURITE_MESSAGE_ID_LIST, messageIdsList);
    }

    //Prajna
    private void updateReadMessageIdsList(List<String> messageIdsList){
        CPCAppCommonUtility.deleteObject(this, CPCAppConstants.KEY_READ_MESSAGE_ID_LIST);
        CPCAppCommonUtility.writeObject(this, CPCAppConstants.KEY_READ_MESSAGE_ID_LIST, messageIdsList);
    }

    @Override
    public void onDeleteMessageClick(int position, final CPCMessageDetails messageDetails) {
        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case DialogInterface.BUTTON_POSITIVE:

                        DeleteUserMessage(messageDetails);
                        break;

                    case DialogInterface.BUTTON_NEGATIVE:
                        dialog.dismiss();
                        break;
                }
            }
        };

        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Are you sure you want to delete the message?").setPositiveButton("Yes", dialogClickListener)
                .setNegativeButton("No", dialogClickListener).show();
    }

    private void DeleteUserMessage(CPCMessageDetails messageDetails) {
        String parentId = CPCAppCommonUtility.getUserId(this);
        if (CPCAppCommonUtility.isNetworkAvailable(this) && !TextUtils.isEmpty(parentId)) {
            mNoConnectionLyt.setVisibility(View.GONE);
            String url = CPCAppConstants.KEY_BASE_URL + "/parent/" + parentId + "/client/" + messageDetails.getmClientId() + "/messages/" + messageDetails.getId() + "/archive";
            Map<String, String> headers = new HashMap<>();
            headers.put(CPCAppConstants.KEY_HEADER_API_KEY, BuildConfig.API_KEY);
            headers.put(CPCAppConstants.KEY_AUTHORIZATION, CPCAppCommonUtility.getAccessToken(this));
            showProgressbar(true);
            CPCVolley.getRequestQueue(getApplicationContext()).add(
                    new CPCGsonPostRequest(url, this, this,
                            new CPCDeleteMessages(), null, headers, null, Request.Method.DELETE));
        } else {
            mNoConnectionLyt.setVisibility(View.VISIBLE);
        }
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

    private void getNoticeList(String stId) {
        if (CPCAppCommonUtility.isNetworkAvailable(this)) {
            mNoConnectionLyt.setVisibility(View.GONE);
            String parentId = CPCAppCommonUtility.getUserId(this);
            String clientId = CPCAppCommonUtility.getClientId(this, CPCAppConstants.KEY_CLIENT_ID);
//            String studentId = CPCAppCommonUtility.getStudentId(this, sttId);
            String url = CPCAppConstants.KEY_BASE_URL + "/parent/" + parentId + "/client/" + clientId + "/student/" + stId + "/notice-board-details";// CPCAppConstants.KEY_GET_ASSIGNMENTS_URL;
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

    private void getAssignmentList(String stId){
        if (CPCAppCommonUtility.isNetworkAvailable(this)) {
            mNoConnectionLyt.setVisibility(View.GONE);
            String parentId = CPCAppCommonUtility.getUserId(this);
            String clientId = CPCAppCommonUtility.getClientId(this, CPCAppConstants.KEY_CLIENT_ID);
//            String studentId = CPCAppCommonUtility.getStudentId(this, CPCAppConstants.KEY_STUDENT_ID);
            String url = CPCAppConstants.KEY_BASE_URL + "/parent/" + parentId + "/client/" + clientId + "/student/" + stId + "/assignments-details";// CPCAppConstants.KEY_GET_ASSIGNMENTS_URL;
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