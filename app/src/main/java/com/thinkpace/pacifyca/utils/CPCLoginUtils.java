package com.thinkpace.pacifyca.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Created by Krishna on 11/27/2016.
 */
public class CPCLoginUtils {
    public static void clearPreference(Context context) {
        try {
            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
            SharedPreferences.Editor editor = preferences.edit();
            editor.putString(CPCAppConstants.ACCESS_TOKEN, null);
            editor.putString(CPCAppConstants.USER_NAME, null);
            editor.putString(CPCAppConstants.KEY_USER_ID, null);
            editor.putString(CPCAppConstants.KEY_CLIENT_ID, null);
            editor.putString(CPCAppConstants.KEY_STUDENT_ID, null);
            editor.apply();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}