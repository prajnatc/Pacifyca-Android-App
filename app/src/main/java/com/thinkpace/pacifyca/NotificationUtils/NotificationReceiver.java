package com.thinkpace.pacifyca.NotificationUtils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.google.firebase.messaging.RemoteMessage;
import com.thinkpace.pacifyca.utils.CPCAppCommonUtility;
import com.thinkpace.pacifyca.utils.CPCAppConstants;


/**
 * Created by Supriya A on 9/30/2016.
 */
public class NotificationReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        RemoteMessage message = intent.getParcelableExtra(CPCAppConstants.NOTIFICATION_MSG_EXTRA);
        CPCAppCommonUtility.log("tag","on message recieved"+message.getData());
    }

}
