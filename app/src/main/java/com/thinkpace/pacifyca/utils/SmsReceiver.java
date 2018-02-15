package com.thinkpace.pacifyca.utils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;

import com.thinkpace.pacifyca.activity.ISmsListener;


public class SmsReceiver extends BroadcastReceiver {

    private ISmsListener mSmsListener;

    public void setSmsListener(ISmsListener smsListener) {
        mSmsListener = smsListener;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        try {
            if (intent.getAction().equals("android.provider.Telephony.SMS_RECEIVED")) {
                Bundle bundle = intent.getExtras();
                SmsMessage[] msgs = null;
                String msgFrom = "";
                String msgBody = "";
                if (bundle != null) {
                    Object[] pdus = (Object[]) bundle.get("pdus");
                    msgs = new SmsMessage[pdus.length];
                    for (int i = 0; i < msgs.length; i++) {
                        msgs[i] = SmsMessage.createFromPdu((byte[]) pdus[i]);
                        msgFrom = msgs[i].getOriginatingAddress();
                        msgBody += msgs[i].getMessageBody();
                    }

                    if (mSmsListener != null) {
                        mSmsListener.onSmsReceived(msgFrom, msgBody);
                    }
                }
            }
        } catch (Exception e) {
           e.printStackTrace();
        }
    }
}
