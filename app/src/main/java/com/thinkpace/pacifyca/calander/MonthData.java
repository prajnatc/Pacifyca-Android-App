package com.thinkpace.pacifyca.calander;

import java.io.Serializable;

/**
 * Created by Gadagool Krishna on 12/21/2016.
 */
public class MonthData implements Serializable {
    private String message;

    private String session;

    private boolean absent;

    private String date;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getSession() {
        return session;
    }

    public void setSession(String session) {
        this.session = session;
    }

    public boolean getAbsent() {
        return absent;
    }

    public void setAbsent(boolean absent) {
        this.absent = absent;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    @Override
    public String toString() {
        return "ClassPojo [message = " + message + ", session = " + session + ", absent = " + absent + ", date = " + date + "]";
    }
}
