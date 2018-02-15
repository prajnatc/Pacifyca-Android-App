package com.thinkpace.pacifyca.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.thinkpace.pacifyca.Adapter.CPCAssignmentsAdapter;
import com.thinkpace.pacifyca.Adapter.CPCVideosAdapter;
import com.thinkpace.pacifyca.BuildConfig;
import com.thinkpace.pacifyca.R;
import com.thinkpace.pacifyca.app.CPCVolley;
import com.thinkpace.pacifyca.entity.CPCAssignment;
import com.thinkpace.pacifyca.entity.CPCAssignmentData;
import com.thinkpace.pacifyca.entity.CPCAssignmentList;
import com.thinkpace.pacifyca.entity.CPCVideo;
import com.thinkpace.pacifyca.entity.CPCVideoData;
import com.thinkpace.pacifyca.entity.CPCVideoList;
import com.thinkpace.pacifyca.entity.IPCDataModel;
import com.thinkpace.pacifyca.net.CPCGsonGetRequest;
import com.thinkpace.pacifyca.utils.CPCAppCommonUtility;
import com.thinkpace.pacifyca.utils.CPCAppConstants;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;


public class APCVideos extends APCBaseActivity implements Response.ErrorListener, Response.Listener<IPCDataModel> {

    @BindView(R.id.video_recycler_view)
    RecyclerView mVideosRecycler;

    @BindView(R.id.video_layout)
    RelativeLayout mVideosLayout;

    @BindView(R.id.no_connection_layout)
    LinearLayout mNoConnectionLyt;

    @BindView(R.id.no_messages_lyt)
    LinearLayout mNoMessagesLyt;

    @BindView(R.id.retry_btn)
    Button mRetrybtn;

//    @BindView(R.id.swipe_notice_refresh)
//    SwipeRefreshLayout mSwipeRefreshLayout;

    private ProgressDialog mProgressDialog;

    private ArrayList<CPCVideoData> mVideoList;
    private CPCVideosAdapter mVideosAdapter;
    private Toolbar mToolbar;

    //Prajna
    private ArrayList<CPCVideoList> oVideoList = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_apcvideo);
        ButterKnife.bind(this);
        initToolBar();
        initUI();
        loadVideo();
    }

    private void initToolBar() {
        mToolbar = (Toolbar) findViewById(R.id.toolbar_actionbar);
        ((TextView) mToolbar.findViewById(R.id.title)).setText(R.string.videos);
        initialiseToolbar(mToolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void initUI() {
        mVideosRecycler.setLayoutManager(new LinearLayoutManager(this));
        mVideosRecycler.setHasFixedSize(true);
        mRetrybtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadVideo();
            }
        });
        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setMessage("Please wait...");
        mProgressDialog.setCancelable(false);
//        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
//            @Override
//            public void onRefresh() {
//                mSwipeRefreshLayout.setRefreshing(false);
//                loadVideo();
//            }
//        });
    }

    //Load user types
    private void loadVideo() {
        mNoMessagesLyt.setVisibility(View.GONE);
        if (CPCAppCommonUtility.isNetworkAvailable(this)) {
            mVideosLayout.setVisibility(View.VISIBLE);
            mNoConnectionLyt.setVisibility(View.GONE);
            String parentId = CPCAppCommonUtility.getUserId(this);
            String clientId = CPCAppCommonUtility.getClientId(this, CPCAppConstants.KEY_CLIENT_ID);
//            String studentId = CPCAppCommonUtility.getStudentId(this, CPCAppConstants.KEY_STUDENT_ID);
            String url = CPCAppConstants.KEY_BASE_URL + "/parent/" + parentId + "/client/" + clientId + "/videos-list";// CPCAppConstants.KEY_GET_ASSIGNMENTS_URL;
            Map<String, String> headers = new HashMap<>();
            headers.put(CPCAppConstants.KEY_HEADER_API_KEY, BuildConfig.API_KEY);
            headers.put(CPCAppConstants.KEY_AUTHORIZATION, CPCAppCommonUtility.getAccessToken(this));
            showProgressDialog();
            CPCVolley.getRequestQueue(getApplicationContext()).add(
                    new CPCGsonGetRequest(url, this, this,
                            new CPCVideo(), headers));
        } else {
            mVideosLayout.setVisibility(View.GONE);
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
        }
        if (response instanceof CPCVideo) {
            CPCVideo cpcVideoModel = (CPCVideo) response;
            if (cpcVideoModel != null && cpcVideoModel.getData() != null && cpcVideoModel.getData().size() > 0) {
                mVideoList = new ArrayList<>();
                CPCVideoData videoData = new CPCVideoData();
                ArrayList<CPCVideoList> tempList = new ArrayList<>();
                for (CPCVideoData data : cpcVideoModel.getData()) {
                    for (CPCVideoList listItems : data.getVideoLists()) {
                        tempList.add(listItems);
                        oVideoList.add(listItems);
                    }
                }
                videoData.setVideoList(tempList);
                mVideoList = cpcVideoModel.getData();
//                mSwipeRefreshLayout.setVisibility(View.VISIBLE);
                //Prajna
                setVideoListAdapter(tempList);
                //
                mVideoList.add(0, videoData);
//                setVideoAdapter(0);
            } else {
                mNoMessagesLyt.setVisibility(View.VISIBLE);
                mVideosLayout.setVisibility(View.GONE);
            }
        }
    }

//    @Override
//    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//
//
//        if (mAssignmentsList != null && mAssignmentsList.size() > 0) {
//            CPCAssignmentData data = mAssignmentsList.get(position);
//            if (mAssignmentsAdapter != null && data != null && data.getAssignmentList() != null && data.getAssignmentList().size() > 0)
//                mAssignmentsAdapter.updateList(data.getAssignmentList());
//        }
//    }

//    private void setVideoAdapter(int position) {
//
//        if (mAssignmentsList != null && mAssignmentsList.size() > 0) {
//            CPCAssignmentData data = mAssignmentsList.get(position);
//            if (data != null) {
//                mNoMessagesLyt.setVisibility(View.GONE);
//                mAssignmentsLayout.setVisibility(View.VISIBLE);
//                mAssignmentsAdapter = new CPCAssignmentsAdapter(this, data.getAssignmentList());
//                mAssignmentsAdapter.setOnAssignmentClickListener(this);
//                mAssignmentsRecycler.setAdapter(mAssignmentsAdapter);
//            }
//        } else {
//            mNoMessagesLyt.setVisibility(View.VISIBLE);
//            mAssignmentsLayout.setVisibility(View.GONE);
//        }
//    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

//    @Override
//    public void onNothingSelected(AdapterView<?> parent) {
//
//    }

//    @Override
//    public void onAssignmentItemClick(int position, CPCAssignmentList assignmentDetail) {
//        Intent intent = new Intent(this, APCAssignmentDetail.class);
//        intent.putExtra(CPCAppConstants.ASSIGNMENT_DETAILS, assignmentDetail);
//        readUserAssignment(position,assignmentDetail);
//        startActivity(intent);
//    }

    //Prajna
    private void setVideoListAdapter(ArrayList<CPCVideoList> videoList) {
//        List<String> assignmentIdsList = (List<String>) CPCAppCommonUtility.readObject(this, CPCAppConstants.KEY_READ_ASSIGNMENT_ID_LIST);
//        if (assignmentIdsList != null && assignmentIdsList.size() > 0) {
//            for (String assignmentId : assignmentIdsList) {
//                for (CPCAssignmentList assignmentDetail : assignmentList) {
//                    if (!TextUtils.isEmpty(assignmentId) && assignmentDetail != null && !TextUtils.isEmpty(assignmentDetail.getAssignedNo()) && assignmentDetail.getAssignedNo().equals(assignmentId)) {
//                        assignmentDetail.setRead(true);
//                    }
//                }
//            }
//        }
//
        mVideosAdapter = new CPCVideosAdapter(this, videoList);
//        mVideosAdapter.setOnAssignmentClickListener(this);
        mVideosRecycler.setAdapter(mVideosAdapter);
    }

//    //Prajna
//    private void readUserAssignment(int position, CPCAssignmentList assignmentDetail){
//        List<String> assignmentIdsLists = (List<String>) CPCAppCommonUtility.readObject(this, CPCAppConstants.KEY_READ_ASSIGNMENT_ID_LIST);
//        String readAssignmentId = null;
//        if (assignmentIdsLists != null && assignmentIdsLists.size() > 0 && assignmentDetail != null && !TextUtils.isEmpty(assignmentDetail.getAssignedNo())) {
//            for (String assignmentId : assignmentIdsLists) {
//                if (assignmentId.equals(assignmentDetail.getAssignedNo())) {
//                    readAssignmentId = assignmentId;
//                    break;
//                }
//            }
//            if (TextUtils.isEmpty(readAssignmentId)) {
//                if(CPCAppConstants.KEY_UNREAD_ASSIGNMENT_COUNT > 1){
//                    CPCAppConstants.KEY_UNREAD_ASSIGNMENT_COUNT = CPCAppConstants.KEY_UNREAD_ASSIGNMENT_COUNT - 2;
//                    CPCAppCommonUtility.setShortcutBadger(this);
//                }
//                assignmentDetail.setRead(true);
//                assignmentIdsLists.add(assignmentDetail.getAssignedNo());
//                updateReadAssignmentIdsList(assignmentIdsLists);
//            }else{
//                assignmentDetail.setRead(true);
//                assignmentIdsLists.add(assignmentDetail.getAssignedNo());
//                updateReadAssignmentIdsList(assignmentIdsLists);
//            }
//        }else{
//            assignmentIdsLists = new ArrayList<>();
//            assignmentIdsLists.add(assignmentDetail.getAssignedNo());
//            assignmentDetail.setRead(true);
//            updateReadAssignmentIdsList(assignmentIdsLists);
//        }
//
////        if (mAssignmentsList != null && mAssignmentsList.size() > 0) {
////            CPCAssignmentData data = mAssignmentsList.get(position);
////            if (mAssignmentsAdapter != null && data != null && data.getAssignmentList() != null && data.getAssignmentList().size() > 0)
////                mAssignmentsAdapter.updateList(data.getAssignmentList());
////        }
//
//        mAssignmentsAdapter.updateList(oAssigmentList);
//
////        mAssignmentsAdapter = new CPCAssignmentsAdapter(this, assignmentList);
////        mAssignmentsAdapter.setOnAssignmentClickListener(this);
////        mAssignmentsRecycler.setAdapter(mAssignmentsAdapter);
//
////        CPCAssignmentData data = mAssignmentsList.get(position);
////        mAssignmentsAdapter.updateList(data.getAssignmentList());
//    }
//
//    private void updateReadAssignmentIdsList(List<String> assignmentIdsList){
//        CPCAppCommonUtility.deleteObject(this, CPCAppConstants.KEY_READ_ASSIGNMENT_ID_LIST);
//        CPCAppCommonUtility.writeObject(this, CPCAppConstants.KEY_READ_ASSIGNMENT_ID_LIST, assignmentIdsList);
//    }

}
