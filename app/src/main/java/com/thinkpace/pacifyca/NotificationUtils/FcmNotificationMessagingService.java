package com.thinkpace.pacifyca.NotificationUtils;

import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;


public class FcmNotificationMessagingService extends FirebaseMessagingService {

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        Log.d("tag", "on message RECEIVED---" + remoteMessage.getData());
    }
}
