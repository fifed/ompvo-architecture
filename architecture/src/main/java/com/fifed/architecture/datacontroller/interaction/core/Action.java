package com.fifed.architecture.datacontroller.interaction.core;

/**
 * Created by Fedir on 05.07.2016.
 */
public abstract class Action {
    protected String userID;
    private String TAG;
    private boolean singleResponse;
    protected boolean hasOfflineSync;

    public Action(String TAG) {
        this.TAG = TAG;
    }

    public Action(String TAG, String userID) {
        this.TAG = TAG;
        this.userID = userID;
    }
    public Action (String TAG, String userID, boolean singleResponse){
        this.TAG = TAG;
        this.userID = userID;
        this.singleResponse = singleResponse;
    }

    public boolean isSingleResponse() {
        return singleResponse;
    }

    public String getTAG() {
        return TAG;
    }

    public String getUserID() {
        return userID;
    }

    public boolean hasOfflineSync() {
        return hasOfflineSync;
    }
}
