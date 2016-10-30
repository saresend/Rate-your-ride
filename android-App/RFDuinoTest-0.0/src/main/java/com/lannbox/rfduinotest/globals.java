package com.lannbox.rfduinotest;

import android.app.Application;

/**
 * Created by SamResendez on 10/30/16.
 */

public class globals {

    private static globals instance = new globals();

    public static globals getInstance() {
        return instance;

    }
    private String sessionID;
    private String bumpinessString;

    public String getSessionID() {
        return sessionID;
    }
    public void setSessionID(String sessionID) {
        this.sessionID = sessionID;
    }
    public String getBumpinessString() { return bumpinessString;}
    public void setBumpinessString(String bumpString) { this.bumpinessString = bumpString;}



}
