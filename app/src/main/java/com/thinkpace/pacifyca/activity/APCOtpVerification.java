package com.thinkpace.pacifyca.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.iid.FirebaseInstanceId;
import com.thinkpace.pacifyca.BuildConfig;
import com.thinkpace.pacifyca.R;
import com.thinkpace.pacifyca.app.CPCVolley;
import com.thinkpace.pacifyca.entity.CPCGenerateOtpModel;
import com.thinkpace.pacifyca.entity.CPCSignInModel;
import com.thinkpace.pacifyca.entity.IPCDataModel;
import com.thinkpace.pacifyca.net.CPCGsonPostRequest;
import com.thinkpace.pacifyca.utils.CPCAppCommonUtility;
import com.thinkpace.pacifyca.utils.CPCAppConstants;
import com.thinkpace.pacifyca.utils.SmsReceiver;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

public class APCOtpVerification extends AppCompatActivity implements ISmsListener, Response.ErrorListener, Response.Listener<IPCDataModel> {
    private SmsReceiver mSmsReceiver;
    @BindView(R.id.input_OTP)
    EditText mOtpInputEditTxt;

    @BindView(R.id.edit_layout)
    LinearLayout mEditMobileNumberLyt;

    @BindView(R.id.submit_btn)
    Button mSubmitBtn;

    @BindView(R.id.otp_mobile_num)
    TextView mMobileNumberTxt;

    private String mMobileNumber = null;
    private String mToken = null;
    private int mOtp;
    private ProgressDialog mProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp_verification);
        ButterKnife.bind(this);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.setStatusBarColor(getResources().getColor(R.color.status_bar_green));
        }
        initUI();
        getIntentDate();
        registerSmsReceiver();
    }


    public void getIntentDate() {
        Intent intent = getIntent();
        mMobileNumber = intent.getStringExtra(CPCAppConstants.KEY_MOBILE_NUMBER);
        mToken = intent.getStringExtra(CPCAppConstants.KEY_TOKEN);
        if (intent.hasExtra("otp"))
            mOtp = intent.getIntExtra("otp", 0);
        if (mOtp != 0) {
            mOtpInputEditTxt.setText("" + mOtp);
        }
        if (mMobileNumber != null)
            mMobileNumberTxt.setText(mMobileNumber);
    }

    private void initUI() {
        if (getIntent() != null && getIntent().hasExtra("MOBILE_NUMBER")) {
            String mobileNum = getIntent().getStringExtra("MOBILE_NUMBER");
            mMobileNumberTxt.setText(mobileNum);
        }
        mEditMobileNumberLyt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        mSubmitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validateOtp();
            }
        });
        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setMessage("Please wait...");
        mProgressDialog.setCancelable(false);
    }

    //Send Request to the API to validate OTP
    private void validateOtp() {
        if (CPCAppCommonUtility.isNetworkAvailable(this)) {

            String url = CPCAppConstants.KEY_SIGN_IN_URL;
            JSONObject jsonObject = null;
            Map<String, String> headers = new HashMap<>();
            headers.put(CPCAppConstants.KEY_HEADER_API_KEY, BuildConfig.API_KEY);
            showProgressDialog();
            try {
                jsonObject = new JSONObject();
                jsonObject.put(CPCAppConstants.KEY_PASSWORD, mOtpInputEditTxt.getText());
                jsonObject.put(CPCAppConstants.KEY_TOKEN, mToken);
                jsonObject.put(CPCAppConstants.KEY_MOBILE_NUMBER, mMobileNumber);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            CPCVolley.getRequestQueue(getApplicationContext()).add(
                    new CPCGsonPostRequest(url, this, this,
                            new CPCSignInModel(), null, headers, jsonObject.toString(), Request.Method.POST));
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


    private void registerSmsReceiver() {
        try {
            if (isFinishing()) {
                return;
            }
            /*if (PermissionUtil.isVersionMarshmallowAndAbove() && !PermissionUtil.checkReadSmsPermission(this)) {
                //permission is not given in marshmallow and above
                return;
            }*/
            if (mSmsReceiver == null) {
                mSmsReceiver = new SmsReceiver();
                mSmsReceiver.setSmsListener(this);
                IntentFilter filter = new IntentFilter();
                filter.addAction("android.provider.Telephony.SMS_RECEIVED");
                filter.setPriority(IntentFilter.SYSTEM_HIGH_PRIORITY);
                registerReceiver(mSmsReceiver, filter);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void unregisterSmsReceiver() {
        try {
            if (mSmsReceiver != null) {
                unregisterReceiver(mSmsReceiver);
                mSmsReceiver.setSmsListener(null);
                mSmsReceiver = null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onSmsReceived(String from, String msgBody) {
        if (isFinishing()) {
            return;
        }
        //Check if the received SMS has any OTP
        String receivedOtp = CPCAppCommonUtility.getOtpFromSms(from, msgBody);
        unregisterSmsReceiver();
        if (!TextUtils.isEmpty(receivedOtp))
            mMobileNumberTxt.setText(receivedOtp);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterSmsReceiver();
    }

    //On Error Response from the API
    @Override
    public void onErrorResponse(VolleyError error) {
       Log.i("API Response", ""+error);
        if (error != null && error.getMessage() != null && error.getMessage().equalsIgnoreCase("400")) {
            Toast.makeText(APCOtpVerification.this, "Invalid OTP", Toast.LENGTH_SHORT).show();
        }
        hideProgressDialog();
    }

    //On Response from the API
    @Override
    public void onResponse(IPCDataModel response) {

        Log.i("API Response", ""+response);

        hideProgressDialog();
        if (response instanceof CPCSignInModel) {
            CPCSignInModel cpcSignInModel = (CPCSignInModel) response;
            if (cpcSignInModel != null && cpcSignInModel.getAuthToken() != null) {
                storeLoginInfo(cpcSignInModel);
                startHomeScreen();
            }
        }
    }

    //Store Logged in User Info
    private void storeLoginInfo(CPCSignInModel loginModel) {
        try {
            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
            SharedPreferences.Editor editor = preferences.edit();
            editor.putString(CPCAppConstants.ACCESS_TOKEN, loginModel.getTokenType() + " " + loginModel.getAuthToken());
            editor.putString(CPCAppConstants.USER_NAME, loginModel.getUserName());
            editor.apply();
            CPCAppCommonUtility.log("KR", "Success");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //Load Home Screen
    private void startHomeScreen() {
        Intent intent = new Intent(APCOtpVerification.this, APCDashboard.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

}

