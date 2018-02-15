package com.thinkpace.pacifyca.activity;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;

import com.thinkpace.pacifyca.utils.PermissionUtil;

import java.util.ArrayList;

/**
 * Created by Krishna Upadhya on 11/15/2016.
 */

public class LoginUtils {

    private Activity mActivity;
    private Context mContext;
    private String TAG = LoginUtils.class.getSimpleName();


    public LoginUtils(Activity activity) {
        mActivity = activity;
        mContext = mActivity.getApplicationContext();
    }

    public void init() {
       // load();
    }

    private void load() {
       /* if (!PermissionUtil.isVersionMarshmallowAndAbove() || PermissionUtil
                .checkPhoneStatePermission(mActivity)) {
            //sendUAirShipTag();
        }*/
        requestPermission();
    }

    public void requestPermission() {
        if (PermissionUtil.isVersionMarshmallowAndAbove()) {
            ArrayList<String> permissions = new ArrayList<>(0);

            if (!(mActivity.checkSelfPermission(Manifest.permission.READ_PHONE_STATE) ==
                    PackageManager.PERMISSION_GRANTED)) {
                permissions.add(Manifest.permission.READ_PHONE_STATE);
            }

           /* if (!(mActivity.checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) ==
                    PackageManager.PERMISSION_GRANTED)) {
                permissions.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
            }

            if (!(mActivity.checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) ==
                    PackageManager.PERMISSION_GRANTED)) {
                permissions.add(Manifest.permission.READ_EXTERNAL_STORAGE);
            }

            if (!(mActivity.checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) ==
                    PackageManager.PERMISSION_GRANTED)) {
                permissions.add(Manifest.permission.ACCESS_COARSE_LOCATION);
            }*/

            if (!(mActivity.checkSelfPermission(Manifest.permission.READ_SMS) ==
                    PackageManager.PERMISSION_GRANTED)) {
                permissions.add(Manifest.permission.READ_SMS);
            }

            if (!(mActivity.checkSelfPermission(Manifest.permission.RECEIVE_SMS) ==
                    PackageManager.PERMISSION_GRANTED)) {
                permissions.add(Manifest.permission.RECEIVE_SMS);
            }

           /* if (!(mActivity.checkSelfPermission(Manifest.permission.READ_CALENDAR) == PackageManager
                    .PERMISSION_GRANTED)) {
                permissions.add(Manifest.permission.READ_CALENDAR);
            }*/
            if (permissions.size() > 0) {
                String[] permission = new String[permissions.size()];
                permission = permissions.toArray(permission);
                mActivity.requestPermissions(permission, 1);
            }
        }
    }
}
