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
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.thinkpace.pacifyca.Adapter.CPCNoticeAdapter;
import com.thinkpace.pacifyca.Adapter.CPCUserMessagesAdapter;
import com.thinkpace.pacifyca.BuildConfig;
import com.thinkpace.pacifyca.R;
import com.thinkpace.pacifyca.app.CPCVolley;
import com.thinkpace.pacifyca.entity.CPCNotice;
import com.thinkpace.pacifyca.entity.CPCNoticeData;
import com.thinkpace.pacifyca.entity.CPCNoticeList;
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
import me.leolin.shortcutbadger.ShortcutBadger;

/**
 * Created by prajna on 25-10-2017.
 */


public class APCNotice extends APCBaseActivity implements CPCNoticeAdapter.OnNoticeClickListener, Response.ErrorListener, Response.Listener<IPCDataModel>, AdapterView.OnItemSelectedListener {

    @BindView(R.id.notice_recycler_view)
    RecyclerView mNoticeRecycler;

    @BindView(R.id.notice_spinner)
    Spinner mSubjectSpinner;

    @BindView(R.id.notice_layout)
    RelativeLayout mNoticeLayout;

    @BindView(R.id.no_connection_layout)
    LinearLayout mNoConnectionLyt;

    @BindView(R.id.no_messages_lyt)
    LinearLayout mNoMessagesLyt;

    @BindView(R.id.retry_btn)
    Button mRetrybtn;

    @BindView(R.id.swipe_notice_refresh)
    SwipeRefreshLayout mSwipeRefreshLayout;

    private ProgressDialog mProgressDialog;
    private  String noticeType;

//    private ArrayList<String> mSubjectsList;
    private ArrayList<CPCNoticeData> mNoticeList;
    private CPCNoticeAdapter mNoticeAdapter;
    private Toolbar mToolbar;

    //Prajna
    private ArrayList<CPCNoticeList> oNoticeList = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_apcnotice);
        ButterKnife.bind(this);
        initToolBar();
        initUI();
        loadNotice();
    }

    private void initToolBar() {
        Intent intent = getIntent();
        noticeType = intent.getStringExtra("type");
        mToolbar = (Toolbar) findViewById(R.id.toolbar_actionbar);
        if(noticeType.equals(CPCAppConstants.KEY_NEWS_TYPE)) {
            ((TextView) mToolbar.findViewById(R.id.title)).setText(R.string.news);
        }else if(noticeType.equals(CPCAppConstants.KEY_CIRCULAR_TYPE)) {
            ((TextView) mToolbar.findViewById(R.id.title)).setText(R.string.notice);
        }
        else{
            ((TextView) mToolbar.findViewById(R.id.title)).setText(R.string.exam_schedule);
        }
        initialiseToolbar(mToolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        getSupportActionBar().setTitle(R.string.assignments);
    }

    private void initUI() {
        mNoticeRecycler.setLayoutManager(new LinearLayoutManager(this));
        mNoticeRecycler.setHasFixedSize(true);
        mSubjectSpinner.setOnItemSelectedListener(this);
        mRetrybtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadNotice();
            }
        });
        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setMessage("Please wait...");
        mProgressDialog.setCancelable(false);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mSwipeRefreshLayout.setRefreshing(false);
                loadData();
            }
        });
    }

    private void loadData() {
        loadNotice();

        if (CPCAppCommonUtility.isNetworkAvailable(getApplicationContext())) {
            mNoConnectionLyt.setVisibility(View.GONE);
//            loadCatalog();
        } else {
            mNoConnectionLyt.setVisibility(View.VISIBLE);
        }
    }

    //Load user types
    private void loadNotice() {
        if (CPCAppCommonUtility.isNetworkAvailable(this)) {
            mNoticeLayout.setVisibility(View.VISIBLE);
            mNoConnectionLyt.setVisibility(View.GONE);
            String parentId = CPCAppCommonUtility.getUserId(this);
            String clientId = CPCAppCommonUtility.getClientId(this, CPCAppConstants.KEY_CLIENT_ID);
            String studentId = CPCAppCommonUtility.getStudentId(this, CPCAppConstants.KEY_STUDENT_ID);
            String url = CPCAppConstants.KEY_BASE_URL + "/parent/" + parentId + "/client/" + clientId + "/student/" + studentId + "/notice-board-details";// CPCAppConstants.KEY_GET_ASSIGNMENTS_URL;
            Map<String, String> headers = new HashMap<>();
            headers.put(CPCAppConstants.KEY_HEADER_API_KEY, BuildConfig.API_KEY);
            headers.put(CPCAppConstants.KEY_AUTHORIZATION, CPCAppCommonUtility.getAccessToken(this));
            headers.put(CPCAppConstants.KEY_NOTICE_TYPE, noticeType);
            showProgressDialog();
            CPCVolley.getRequestQueue(getApplicationContext()).add(
                    new CPCGsonGetRequest(url, this, this,
                            new CPCNotice(), headers));
        } else {
            mNoticeLayout.setVisibility(View.GONE);
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
        if (response instanceof CPCNotice) {
            CPCNotice cpcNoticeModel = (CPCNotice) response;
            if (cpcNoticeModel != null && cpcNoticeModel.getData() != null && cpcNoticeModel.getData().size() > 0) {
//                mSubjectsList = new ArrayList<>();
                mNoticeList = new ArrayList<>();
                CPCNoticeData noticeData = new CPCNoticeData();
                ArrayList<CPCNoticeList> tempList = new ArrayList<>();
                for (CPCNoticeData data : cpcNoticeModel.getData()) {
//                    mSubjectsList.add(data.getSubname());
                    for (CPCNoticeList listItems : data.getNoticeList()) {
//                        listItems.setSubName(data.getSubname());
                        tempList.add(listItems);
                        oNoticeList.add(listItems);
                    }
                }
//                mSubjectsList.add(0, getString(R.string.all_subjects));
//                noticeData.setSubname(getString(R.string.all_subjects));
                noticeData.setNoticeList(tempList);
                mNoticeList = cpcNoticeModel.getData();
                mSwipeRefreshLayout.setVisibility(View.VISIBLE);
                //Prajna
                setNoticeListAdapter(tempList);
                //
                mNoticeList.add(0, noticeData);

//                setSubjectSpinnerAdapter();
                setNoticeAdapter(0);
            } else {
                mNoMessagesLyt.setVisibility(View.VISIBLE);
                mNoticeLayout.setVisibility(View.GONE);
                mSwipeRefreshLayout.setVisibility(View.GONE);
            }
        }
    }

//    private void setSubjectSpinnerAdapter() {
//        mSubjectSpinner.setVisibility(View.VISIBLE);
//        ArrayAdapter<String> locationsAdapter = new ArrayAdapter<String>(this, R.layout.spinner_text_layout, mSubjectsList);
//        locationsAdapter.setDropDownViewResource(R.layout.dropdown_item);
//        mSubjectSpinner.setAdapter(locationsAdapter);
//    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {


        if (mNoticeList != null && mNoticeList.size() > 0) {
            CPCNoticeData data = mNoticeList.get(position);
            if (mNoticeAdapter != null && data != null && data.getNoticeList() != null && data.getNoticeList().size() > 0)
                mNoticeAdapter.updateList(data.getNoticeList());
        }
    }

    private void setNoticeAdapter(int position) {

        if (mNoticeList != null && mNoticeList.size() > 0) {
            CPCNoticeData data = mNoticeList.get(position);
            if (data != null) {
                mNoMessagesLyt.setVisibility(View.GONE);
                mNoticeLayout.setVisibility(View.VISIBLE);
                mNoticeAdapter = new CPCNoticeAdapter(this, data.getNoticeList());
                mNoticeAdapter.setOnNoticeClickListener(this);
                mNoticeRecycler.setAdapter(mNoticeAdapter);
            }
        } else {
            mNoMessagesLyt.setVisibility(View.VISIBLE);
            mNoticeLayout.setVisibility(View.GONE);
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
    public void onNoticeItemClick(int position, CPCNoticeList noticeDetail) {
        Intent intent = new Intent(this, APCNoticeDetail.class);
        intent.putExtra(CPCAppConstants.NOTICE_DETAILS, noticeDetail);
        intent.putExtra(CPCAppConstants.KEY_NOTICE_TYPE, noticeType);
        readUserNotice(position,noticeDetail);
        startActivity(intent);
    }

    //Prajna
    private void setNoticeListAdapter(ArrayList<CPCNoticeList> noticeList) {
        List<String> noticeIdsList = (List<String>) CPCAppCommonUtility.readObject(this, CPCAppConstants.KEY_READ_NOTICE_ID_LIST);
        if (noticeIdsList != null && noticeIdsList.size() > 0) {
            for (String noticeId : noticeIdsList) {
                for (CPCNoticeList noticeDetail : noticeList) {
                    if (!TextUtils.isEmpty(noticeId) && noticeDetail != null && !TextUtils.isEmpty(noticeDetail.getNoticedNo()) && noticeDetail.getNoticedNo().equals(noticeId)) {
                        noticeDetail.setRead(true);
                    }
                }
            }
        }


        mNoticeAdapter = new CPCNoticeAdapter(this, noticeList);
        mNoticeAdapter.setOnNoticeClickListener(this);
        mNoticeRecycler.setAdapter(mNoticeAdapter);
    }

    //Prajna
    private void readUserNotice(int position, CPCNoticeList noticeDetail){
        List<String> noticeIdsLists = (List<String>) CPCAppCommonUtility.readObject(this, CPCAppConstants.KEY_READ_NOTICE_ID_LIST);
        String readNoticeId = null;
        if (noticeIdsLists != null && noticeIdsLists.size() > 0 && noticeDetail != null && !TextUtils.isEmpty(noticeDetail.getNoticedNo())) {
            for (String noticeId : noticeIdsLists) {
                if (noticeId.equals(noticeDetail.getNoticedNo())) {
                    readNoticeId = noticeId;
                    break;
                }
            }
            if (TextUtils.isEmpty(readNoticeId)) {
                if(noticeDetail.getType().equals(CPCAppConstants.KEY_NEWS_TYPE)){
                    CPCAppConstants.KEY_UNREAD_NEWS_COUNT = CPCAppConstants.KEY_UNREAD_NEWS_COUNT - 1;
                }else if(noticeDetail.getType().equals(CPCAppConstants.KEY_CIRCULAR_TYPE)){
                    CPCAppConstants.KEY_UNREAD_NOTICES_COUNT = CPCAppConstants.KEY_UNREAD_NOTICES_COUNT - 1;
                }else if(noticeDetail.getType().equals(CPCAppConstants.KEY_EXAM_TYPE)){
                    CPCAppConstants.KEY_UNREAD_EXAM_COUNT = CPCAppConstants.KEY_UNREAD_EXAM_COUNT - 1;
                }
                CPCAppCommonUtility.setShortcutBadger(this);
                noticeDetail.setRead(true);
                noticeIdsLists.add(noticeDetail.getNoticedNo());
                updateReadNoticeIdsList(noticeIdsLists);
            }else{
                noticeDetail.setRead(true);
               noticeIdsLists.add(noticeDetail.getNoticedNo());
                updateReadNoticeIdsList(noticeIdsLists);
            }
        }else{
            noticeIdsLists = new ArrayList<>();
            noticeIdsLists.add(noticeDetail.getNoticedNo());
            if(noticeDetail.getType().equals(CPCAppConstants.KEY_NEWS_TYPE)){
                CPCAppConstants.KEY_UNREAD_NEWS_COUNT = CPCAppConstants.KEY_UNREAD_NEWS_COUNT - 1;
            }else if(noticeDetail.getType().equals(CPCAppConstants.KEY_CIRCULAR_TYPE)){
                CPCAppConstants.KEY_UNREAD_NOTICES_COUNT = CPCAppConstants.KEY_UNREAD_NOTICES_COUNT - 1;
            }else if(noticeDetail.getType().equals(CPCAppConstants.KEY_EXAM_TYPE)){
                CPCAppConstants.KEY_UNREAD_EXAM_COUNT = CPCAppConstants.KEY_UNREAD_EXAM_COUNT - 1;
            }
            CPCAppCommonUtility.setShortcutBadger(this);
            noticeDetail.setRead(true);
            updateReadNoticeIdsList(noticeIdsLists);
        }
        mNoticeAdapter.updateList(oNoticeList);

    }

    private void updateReadNoticeIdsList(List<String> noticeIdsList){
        CPCAppCommonUtility.deleteObject(this, CPCAppConstants.KEY_READ_NOTICE_ID_LIST);
        CPCAppCommonUtility.writeObject(this, CPCAppConstants.KEY_READ_NOTICE_ID_LIST, noticeIdsList);
    }

}
