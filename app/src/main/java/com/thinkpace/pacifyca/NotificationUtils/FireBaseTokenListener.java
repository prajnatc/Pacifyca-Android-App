package com.thinkpace.pacifyca.NotificationUtils;

import android.content.SharedPreferences;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;
import com.thinkpace.pacifyca.BuildConfig;
import com.thinkpace.pacifyca.R;
import com.thinkpace.pacifyca.app.CPCVolley;
import com.thinkpace.pacifyca.entity.CPCGenerateOtpModel;
import com.thinkpace.pacifyca.net.CPCGsonPostRequest;
import com.thinkpace.pacifyca.utils.CPCAppCommonUtility;
import com.thinkpace.pacifyca.utils.CPCAppConstants;
import com.thinkpace.pacifyca.utils.SecureSharedPreferences;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


/**
 * Created by Supriya A on 9/27/2016.
 */
public class FireBaseTokenListener extends FirebaseInstanceIdService {

    /**
     * Called if InstanceID token is updated. This may occur if the security of
     * the previous token had been compromised. Note that this is also called
     * when the InstanceID token is initially generated, so this is where
     * you retrieve the token.
     */
    @Override
    public void onTokenRefresh() {
        // Get updated InstanceID token.
        try {
            String refreshedToken = FirebaseInstanceId.getInstance().getToken(CPCAppConstants.PROJECT_NUMBER, "FCM");
            Log.d("TAG", "Refreshed token: " + refreshedToken);
            if (refreshedToken != null && TextUtils.isEmpty(CPCAppCommonUtility.getRegistrationId(this))) {
                saveToken(refreshedToken);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void saveToken(String refreshedToken) {
        SecureSharedPreferences prefs = new SecureSharedPreferences(this);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(CPCAppConstants.KEY_GCM_REGISTRATION_ID, refreshedToken);
        editor.commit();

        //update fcm token

    }


}
