package com.thinkpace.pacifyca.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.thinkpace.pacifyca.BuildConfig;
import com.thinkpace.pacifyca.R;
import com.thinkpace.pacifyca.app.CPCVolley;
import com.thinkpace.pacifyca.entity.CPCUserTypeData;
import com.thinkpace.pacifyca.entity.CPCUserTypesModel;
import com.thinkpace.pacifyca.entity.CPCGenerateOtpModel;
import com.thinkpace.pacifyca.entity.IPCDataModel;
import com.thinkpace.pacifyca.net.CPCGsonGetRequest;
import com.thinkpace.pacifyca.net.CPCGsonPostRequest;
import com.thinkpace.pacifyca.utils.CPCAppCommonUtility;
import com.thinkpace.pacifyca.utils.CPCAppConstants;
import com.thinkpace.pacifyca.utils.PermissionUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

public class APCLoginActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener, Response.ErrorListener, Response.Listener<IPCDataModel> {

    @BindView(R.id.spinner)
    Spinner mUserTypeSpinner;

    @BindView(R.id.get_password_btn)
    Button mLoginBtn;

    @BindView(R.id.input_mobile)
    EditText mMobileNumberEditTxt;

    @BindView(R.id.number_input_layout)
    TextInputLayout mMobileNumberLyt;

    private ProgressDialog mProgressDialog;
    LoginUtils loginUtils;

    private ArrayList<String> mUserTypeList = new ArrayList<>();
    private String mSpinnerItemSelected;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.setStatusBarColor(getResources().getColor(R.color.status_bar_green));
        }
        loginUtils = new LoginUtils(this);
        loginUtils.init();
        initView();
        loadUserTypes();

    }

    private void initView() {
        mUserTypeSpinner.setOnItemSelectedListener(this);
        mLoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (CPCAppCommonUtility.isValidMobile(mMobileNumberEditTxt.getText().toString()))
                    requestGenerateOtp();
                else {
                    mMobileNumberLyt.setError(getString(R.string.invalid_number_msg));
                }
            }
        });
        mMobileNumberEditTxt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                mMobileNumberLyt.setError(null);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        String storedMobileNumber = CPCAppCommonUtility.getStringValue(this, CPCAppConstants.KEY_MOBILE_NUMBER);
        if (!TextUtils.isEmpty(storedMobileNumber)) {
            mMobileNumberEditTxt.setText(storedMobileNumber);
            mMobileNumberEditTxt.setSelection(mMobileNumberEditTxt.getText().length());

        }
        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setMessage("Please wait...");
        mProgressDialog.setCancelable(false);
    }

    //Load user types
    private void loadUserTypes() {
        if (CPCAppCommonUtility.isNetworkAvailable(this)) {
            Map<String, String> headers = new HashMap<>();
            headers.put(CPCAppConstants.KEY_HEADER_API_KEY, BuildConfig.API_KEY);
            showProgressDialog();
            CPCVolley.getRequestQueue(getApplicationContext()).add(
                    new CPCGsonGetRequest(CPCAppConstants.KEY_BASE_URL +"/login-available-types", this, this,
                            new CPCUserTypesModel(), headers));
        } else {
            Toast.makeText(this, R.string.no_network_msg, Toast.LENGTH_SHORT).show();
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

    //Request to the API to generate OTP
    private void requestGenerateOtp() {
        if (CPCAppCommonUtility.isNetworkAvailable(this)) {
            String url = CPCAppConstants.KEY_GENERATE_OTP_URL;
            JSONObject jsonObject = null;
            Map<String, String> headers = new HashMap<>();
            headers.put(CPCAppConstants.KEY_HEADER_API_KEY, BuildConfig.API_KEY);
            showProgressDialog();
            try {
                jsonObject = new JSONObject();
                jsonObject.put(CPCAppConstants.KEY_USER_TYPE, mSpinnerItemSelected);
                jsonObject.put(CPCAppConstants.KEY_MOBILE_NUMBER, mMobileNumberEditTxt.getText().toString());
            } catch (JSONException e) {
                e.printStackTrace();
            }
            CPCVolley.getRequestQueue(getApplicationContext()).add(
                    new CPCGsonPostRequest(url, this, this,
                            new CPCGenerateOtpModel(), null, headers, jsonObject.toString(), Request.Method.POST));
        } else {
            Toast.makeText(this, R.string.no_network_msg, Toast.LENGTH_SHORT).show();
        }
    }

    private void setUserTypeAdapter() {
        mUserTypeSpinner.setVisibility(View.VISIBLE);
        ArrayAdapter<String> locationsAdapter = new ArrayAdapter<String>(this, R.layout.spinner_text_layout, mUserTypeList);
        locationsAdapter.setDropDownViewResource(R.layout.dropdown_item);
        mUserTypeSpinner.setAdapter(locationsAdapter);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        mSpinnerItemSelected = mUserTypeSpinner.getSelectedItem().toString();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    //On API Error Response
    @Override
    public void onErrorResponse(VolleyError error) {
        if (error != null && error.getMessage() != null && error.getMessage().equalsIgnoreCase("400")) {
            Toast.makeText(APCLoginActivity.this, "Please enter a valid mobile number", Toast.LENGTH_SHORT).show();
        }
        hideProgressDialog();
    }

    //On API Response
    @Override
    public void onResponse(IPCDataModel response) {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
        if (response instanceof CPCUserTypesModel) {
            CPCUserTypesModel cpcUserTypesModel = (CPCUserTypesModel) response;
            if (cpcUserTypesModel != null && cpcUserTypesModel.getUserTypeData().size() > 0) {
                for (CPCUserTypeData userType : cpcUserTypesModel.getUserTypeData()) {
                    mUserTypeList.add(userType.getType());
                }
                setUserTypeAdapter();
            } else {
                Toast.makeText(APCLoginActivity.this, "No User Type available please try again", Toast.LENGTH_SHORT).show();
            }
        } else if (response instanceof CPCGenerateOtpModel) {
            CPCGenerateOtpModel cpcGenerateOtpModel = (CPCGenerateOtpModel) response;

            if (cpcGenerateOtpModel != null && cpcGenerateOtpModel.getToken() != null && cpcGenerateOtpModel.getToken().trim().length() > 0) {
                if (!TextUtils.isEmpty(cpcGenerateOtpModel.getMobileNumber())) {
                    CPCAppCommonUtility.storeStringValue(this, CPCAppConstants.KEY_MOBILE_NUMBER, cpcGenerateOtpModel.getMobileNumber());
                }
                Intent intent = new Intent(this, APCOtpVerification.class);
                intent.putExtra(CPCAppConstants.KEY_MOBILE_NUMBER, cpcGenerateOtpModel.getMobileNumber());
                intent.putExtra(CPCAppConstants.KEY_TOKEN, cpcGenerateOtpModel.getToken());
                intent.putExtra("otp", cpcGenerateOtpModel.getOtp());
                startActivity(intent);
            }
        }
    }

    //User Permission
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode == PermissionUtil.REQUEST_CODE_READ_SMS){

            //If permission is granted
            if(grantResults.length >0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){

                //Displaying a toast
                Toast.makeText(this,"Permission granted now you can read the storage",Toast.LENGTH_LONG).show();
            }else{
                //Displaying another toast if permission is not granted
                Toast.makeText(this,"Oops you just denied the permission",Toast.LENGTH_LONG).show();
            }
        }
    }
}
