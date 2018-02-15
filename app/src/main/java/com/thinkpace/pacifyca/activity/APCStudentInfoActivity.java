package com.thinkpace.pacifyca.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.squareup.picasso.Picasso;
import com.thinkpace.pacifyca.BuildConfig;
import com.thinkpace.pacifyca.R;
import com.thinkpace.pacifyca.app.CPCVolley;
import com.thinkpace.pacifyca.entity.CPCStudentDetails;
import com.thinkpace.pacifyca.entity.CPCStudentInfoModel;
import com.thinkpace.pacifyca.entity.IPCDataModel;
import com.thinkpace.pacifyca.net.CPCGsonGetRequest;
import com.thinkpace.pacifyca.utils.CPCAppCommonUtility;
import com.thinkpace.pacifyca.utils.CPCAppConstants;
import com.thinkpace.pacifyca.widget.RoboTextView;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Krishna on 12/4/2016.
 */
public class APCStudentInfoActivity extends APCBaseActivity implements Response.ErrorListener, Response.Listener<IPCDataModel> {
    @BindView(R.id.admission_date)
    RoboTextView mAdmissionDate;
    @BindView(R.id.student_reg_no)
    RoboTextView mStudentRegNo;
    @BindView(R.id.dob)
    RoboTextView mDob;
    @BindView(R.id.student_name)
    RoboTextView mStudentName;
    @BindView(R.id.school_name)
    RoboTextView mSchoolName;
    @BindView(R.id.course_name)
    RoboTextView mCourseName;
    @BindView(R.id.academic_year)
    RoboTextView mAcademicYear;
    private CPCStudentDetails mStudentData;
    @BindView(R.id.progress_lyt)
    RelativeLayout mProgressLyt;
    private String mStudentId;
    @BindView(R.id.profile_img)
    ImageView mProfileImage;
    private String mClientId;
    private Toolbar mToolbar;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.student_profile_activity);
        mToolbar = (Toolbar) findViewById(R.id.toolbar_actionbar);
        ((TextView) mToolbar.findViewById(R.id.title)).setText("Student information");
        initialiseToolbar(mToolbar);

        getSupportActionBar().setDisplayShowHomeEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        getSupportActionBar().setTitle("Student information");
        ButterKnife.bind(this);
        getIntentData();
        initStudentInfoCall();
    }

    private void initStudentInfoCall() {
        if (CPCAppCommonUtility.isNetworkAvailable(this) && mStudentId != null) {
            String url = CPCAppConstants.KEY_GET_STUDENT_INFO_URL;
            Map<String, String> headers = new HashMap<>();
            headers.put(CPCAppConstants.KEY_HEADER_API_KEY, BuildConfig.API_KEY);
            headers.put(CPCAppConstants.KEY_AUTHORIZATION, CPCAppCommonUtility.getAccessToken(this));
            progressLytVisibility(true);
            String parentId = CPCAppCommonUtility.getUserId(this);
            url = url + parentId + "/client/" + mClientId + "/student/" + mStudentId + "/student-info";
            CPCVolley.getRequestQueue(getApplicationContext()).add(
                    new CPCGsonGetRequest(url, this, this,
                            new CPCStudentInfoModel(), headers));
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


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    public void getIntentData() {
        Intent intent = getIntent();
        if (intent.hasExtra("info")) {
            try {
                Bundle bundle = intent.getBundleExtra("info");
                mStudentData = (CPCStudentDetails) bundle.getSerializable("student_info");
                if (mStudentData != null && mStudentData.getStudent_id() != null) {
                    mStudentId = mStudentData.getStudent_id();
                }
                mClientId = mStudentData.getClient_id();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        else{
            mStudentId = intent.getStringExtra("student_id");
            mClientId = intent.getStringExtra("client_id");
        }
    }

    @Override
    public void onErrorResponse(VolleyError error) {

    }

    @Override
    public void onResponse(IPCDataModel response) {
        progressLytVisibility(false);
        if (response instanceof CPCStudentInfoModel) {
            CPCStudentInfoModel cpcStudentInfoModel = (CPCStudentInfoModel) response;
            if (cpcStudentInfoModel != null && cpcStudentInfoModel.getCpcStudentInfoData() != null) {
                initStudentData(cpcStudentInfoModel);
            }
        }
    }

    private void initStudentData(CPCStudentInfoModel cpcStudentInfoModel) {
        mStudentName.setText(cpcStudentInfoModel.getCpcStudentInfoData().getStudentName());
        mAdmissionDate.setText(cpcStudentInfoModel.getCpcStudentInfoData().getAdmissionDate());
        mStudentRegNo.setText(cpcStudentInfoModel.getCpcStudentInfoData().getRegisterNumber());
        mDob.setText(cpcStudentInfoModel.getCpcStudentInfoData().getDateOfBirth());
        mSchoolName.setText(cpcStudentInfoModel.getCpcStudentInfoData().getStudentInstitute());
        mCourseName.setText(cpcStudentInfoModel.getCpcStudentInfoData().getCourseName());
        mAcademicYear.setText(cpcStudentInfoModel.getCpcStudentInfoData().getAcademicYear());
        String imagePath = cpcStudentInfoModel.getCpcStudentInfoData().getProfilePhoto();
        if (!TextUtils.isEmpty(imagePath)) {
            Picasso.with(APCStudentInfoActivity.this)
                    .load(imagePath)
                    .placeholder(R.drawable.profile_icon)
                    .error(R.drawable.profile_icon)
                    .into(mProfileImage);
        }
    }
}
