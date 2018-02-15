package com.thinkpace.pacifyca.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
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
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.thinkpace.pacifyca.Adapter.CPCAssignmentsAdapter;
import com.thinkpace.pacifyca.Adapter.CPCUserMessagesAdapter;
import com.thinkpace.pacifyca.BuildConfig;
import com.thinkpace.pacifyca.R;
import com.thinkpace.pacifyca.app.CPCVolley;
import com.thinkpace.pacifyca.entity.CPCAssignment;
import com.thinkpace.pacifyca.entity.CPCAssignmentData;
import com.thinkpace.pacifyca.entity.CPCAssignmentList;
import com.thinkpace.pacifyca.entity.CPCMessageDetails;
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


public class APCAssignments extends APCBaseActivity implements CPCAssignmentsAdapter.OnAssignmentClickListener, Response.ErrorListener, Response.Listener<IPCDataModel>, AdapterView.OnItemSelectedListener {

    @BindView(R.id.assignments_recycler_view)
    RecyclerView mAssignmentsRecycler;

    @BindView(R.id.assignments_spinner)
    Spinner mSubjectSpinner;

    @BindView(R.id.assignments_layout)
    RelativeLayout mAssignmentsLayout;

    @BindView(R.id.no_connection_layout)
    LinearLayout mNoConnectionLyt;

    @BindView(R.id.no_messages_lyt)
    LinearLayout mNoMessagesLyt;

    @BindView(R.id.retry_btn)
    Button mRetrybtn;

    private ProgressDialog mProgressDialog;

    private ArrayList<String> mSubjectsList;
    private ArrayList<CPCAssignmentData> mAssignmentsList;
    private CPCAssignmentsAdapter mAssignmentsAdapter;
    private Toolbar mToolbar;

    //Prajna
    private ArrayList<CPCAssignmentList> oAssigmentList = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_apcassignments);
        ButterKnife.bind(this);
        initToolBar();
        initUI();
        loadAssignments();
    }

    private void initToolBar() {
        mToolbar = (Toolbar) findViewById(R.id.toolbar_actionbar);
        ((TextView) mToolbar.findViewById(R.id.title)).setText(R.string.assignments);
        initialiseToolbar(mToolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        getSupportActionBar().setTitle(R.string.assignments);
    }

    private void initUI() {
        mAssignmentsRecycler.setLayoutManager(new LinearLayoutManager(this));
        mAssignmentsRecycler.setHasFixedSize(true);
        mSubjectSpinner.setOnItemSelectedListener(this);
        mRetrybtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadAssignments();
            }
        });
        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setMessage("Please wait...");
        mProgressDialog.setCancelable(false);
    }

    //Load user types
    private void loadAssignments() {
        if (CPCAppCommonUtility.isNetworkAvailable(this)) {
            mAssignmentsLayout.setVisibility(View.VISIBLE);
            mNoConnectionLyt.setVisibility(View.GONE);
            String parentId = CPCAppCommonUtility.getUserId(this);
            String clientId = CPCAppCommonUtility.getClientId(this, CPCAppConstants.KEY_CLIENT_ID);
            String studentId = CPCAppCommonUtility.getStudentId(this, CPCAppConstants.KEY_STUDENT_ID);
            String url = CPCAppConstants.KEY_BASE_URL + "/parent/" + parentId + "/client/" + clientId + "/student/" + studentId + "/assignments-details";// CPCAppConstants.KEY_GET_ASSIGNMENTS_URL;
            Map<String, String> headers = new HashMap<>();
            headers.put(CPCAppConstants.KEY_HEADER_API_KEY, BuildConfig.API_KEY);
            headers.put(CPCAppConstants.KEY_AUTHORIZATION, CPCAppCommonUtility.getAccessToken(this));
            showProgressDialog();
            CPCVolley.getRequestQueue(getApplicationContext()).add(
                    new CPCGsonGetRequest(url, this, this,
                            new CPCAssignment(), headers));
        } else {
            mAssignmentsLayout.setVisibility(View.GONE);
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
        if (response instanceof CPCAssignment) {
            CPCAssignment cpcAssignmentsModel = (CPCAssignment) response;
            if (cpcAssignmentsModel != null && cpcAssignmentsModel.getData() != null && cpcAssignmentsModel.getData().size() > 0) {
                mSubjectsList = new ArrayList<>();
                mAssignmentsList = new ArrayList<>();
                CPCAssignmentData assignmentData = new CPCAssignmentData();
                ArrayList<CPCAssignmentList> tempList = new ArrayList<>();
                for (CPCAssignmentData data : cpcAssignmentsModel.getData()) {
                    mSubjectsList.add(data.getSubname());
                    for (CPCAssignmentList listItems : data.getAssignmentList()) {
                        listItems.setSubName(data.getSubname());
                        tempList.add(listItems);
                        oAssigmentList.add(listItems);
                    }
                }
                mSubjectsList.add(0, getString(R.string.all_subjects));
                assignmentData.setSubname(getString(R.string.all_subjects));
                assignmentData.setAssignmentList(tempList);
                mAssignmentsList = cpcAssignmentsModel.getData();
                //Prajna
                setAssignmentListAdapter(tempList);
                //
                mAssignmentsList.add(0, assignmentData);

                setSubjectSpinnerAdapter();
                setAssignmentAdapter(0);
            } else {
                mNoMessagesLyt.setVisibility(View.VISIBLE);
                mAssignmentsLayout.setVisibility(View.GONE);
            }
        }
    }

    private void setSubjectSpinnerAdapter() {
        mSubjectSpinner.setVisibility(View.VISIBLE);
        ArrayAdapter<String> locationsAdapter = new ArrayAdapter<String>(this, R.layout.spinner_text_layout, mSubjectsList);
        locationsAdapter.setDropDownViewResource(R.layout.dropdown_item);
        mSubjectSpinner.setAdapter(locationsAdapter);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {


        if (mAssignmentsList != null && mAssignmentsList.size() > 0) {
            CPCAssignmentData data = mAssignmentsList.get(position);
            if (mAssignmentsAdapter != null && data != null && data.getAssignmentList() != null && data.getAssignmentList().size() > 0)
                mAssignmentsAdapter.updateList(data.getAssignmentList());
        }
    }

    private void setAssignmentAdapter(int position) {

        if (mAssignmentsList != null && mAssignmentsList.size() > 0) {
            CPCAssignmentData data = mAssignmentsList.get(position);
            if (data != null) {
                mNoMessagesLyt.setVisibility(View.GONE);
                mAssignmentsLayout.setVisibility(View.VISIBLE);
                mAssignmentsAdapter = new CPCAssignmentsAdapter(this, data.getAssignmentList());
                mAssignmentsAdapter.setOnAssignmentClickListener(this);
                mAssignmentsRecycler.setAdapter(mAssignmentsAdapter);
            }
        } else {
            mNoMessagesLyt.setVisibility(View.VISIBLE);
            mAssignmentsLayout.setVisibility(View.GONE);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public void onAssignmentItemClick(int position, CPCAssignmentList assignmentDetail) {
        Intent intent = new Intent(this, APCAssignmentDetail.class);
        intent.putExtra(CPCAppConstants.ASSIGNMENT_DETAILS, assignmentDetail);
        readUserAssignment(position,assignmentDetail);
        startActivity(intent);
    }

    //Prajna
    private void setAssignmentListAdapter(ArrayList<CPCAssignmentList> assignmentList) {
        List<String> assignmentIdsList = (List<String>) CPCAppCommonUtility.readObject(this, CPCAppConstants.KEY_READ_ASSIGNMENT_ID_LIST);
        if (assignmentIdsList != null && assignmentIdsList.size() > 0) {
            for (String assignmentId : assignmentIdsList) {
                for (CPCAssignmentList assignmentDetail : assignmentList) {
                    if (!TextUtils.isEmpty(assignmentId) && assignmentDetail != null && !TextUtils.isEmpty(assignmentDetail.getAssignedNo()) && assignmentDetail.getAssignedNo().equals(assignmentId)) {
                        assignmentDetail.setRead(true);
                    }
                }
            }
        }


        mAssignmentsAdapter = new CPCAssignmentsAdapter(this, assignmentList);
        mAssignmentsAdapter.setOnAssignmentClickListener(this);
        mAssignmentsRecycler.setAdapter(mAssignmentsAdapter);
    }

    //Prajna
    private void readUserAssignment(int position, CPCAssignmentList assignmentDetail){
        List<String> assignmentIdsLists = (List<String>) CPCAppCommonUtility.readObject(this, CPCAppConstants.KEY_READ_ASSIGNMENT_ID_LIST);
        String readAssignmentId = null;
        if (assignmentIdsLists != null && assignmentIdsLists.size() > 0 && assignmentDetail != null && !TextUtils.isEmpty(assignmentDetail.getAssignedNo())) {
            for (String assignmentId : assignmentIdsLists) {
                if (assignmentId.equals(assignmentDetail.getAssignedNo())) {
                    readAssignmentId = assignmentId;
                    break;
                }
            }
            if (TextUtils.isEmpty(readAssignmentId)) {
                if(CPCAppConstants.KEY_UNREAD_ASSIGNMENT_COUNT > 1){
                    CPCAppConstants.KEY_UNREAD_ASSIGNMENT_COUNT = CPCAppConstants.KEY_UNREAD_ASSIGNMENT_COUNT - 1;
                    CPCAppCommonUtility.setShortcutBadger(this);
                }
                assignmentDetail.setRead(true);
                assignmentIdsLists.add(assignmentDetail.getAssignedNo());
                updateReadAssignmentIdsList(assignmentIdsLists);
            }else{
                assignmentDetail.setRead(true);
                assignmentIdsLists.add(assignmentDetail.getAssignedNo());
                updateReadAssignmentIdsList(assignmentIdsLists);
            }
        }else{
            assignmentIdsLists = new ArrayList<>();
            assignmentIdsLists.add(assignmentDetail.getAssignedNo());
            if(CPCAppConstants.KEY_UNREAD_ASSIGNMENT_COUNT > 1){
                CPCAppConstants.KEY_UNREAD_ASSIGNMENT_COUNT = CPCAppConstants.KEY_UNREAD_ASSIGNMENT_COUNT - 1;
                CPCAppCommonUtility.setShortcutBadger(this);
            }
            assignmentDetail.setRead(true);
            updateReadAssignmentIdsList(assignmentIdsLists);
        }

//        if (mAssignmentsList != null && mAssignmentsList.size() > 0) {
//            CPCAssignmentData data = mAssignmentsList.get(position);
//            if (mAssignmentsAdapter != null && data != null && data.getAssignmentList() != null && data.getAssignmentList().size() > 0)
//                mAssignmentsAdapter.updateList(data.getAssignmentList());
//        }

        mAssignmentsAdapter.updateList(oAssigmentList);

//        mAssignmentsAdapter = new CPCAssignmentsAdapter(this, assignmentList);
//        mAssignmentsAdapter.setOnAssignmentClickListener(this);
//        mAssignmentsRecycler.setAdapter(mAssignmentsAdapter);

//        CPCAssignmentData data = mAssignmentsList.get(position);
//        mAssignmentsAdapter.updateList(data.getAssignmentList());
    }

    private void updateReadAssignmentIdsList(List<String> assignmentIdsList){
        CPCAppCommonUtility.deleteObject(this, CPCAppConstants.KEY_READ_ASSIGNMENT_ID_LIST);
        CPCAppCommonUtility.writeObject(this, CPCAppConstants.KEY_READ_ASSIGNMENT_ID_LIST, assignmentIdsList);
    }

}
