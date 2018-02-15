package com.thinkpace.pacifyca.activity;

/**
 * Created by Krishna Upadhya on 11/14/2016.
 */

public interface ISmsListener {
    public void onSmsReceived(String from, String msgBody);
}