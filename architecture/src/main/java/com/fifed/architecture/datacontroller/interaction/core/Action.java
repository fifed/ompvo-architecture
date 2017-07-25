package com.fifed.architecture.datacontroller.interaction.core;

/**
 * Created by Fedir on 05.07.2016.
 */
public abstract class Action {
    private String TAG;
    private boolean singleResponse;
    private boolean needStaticRAMCache;


    public Action(String TAG) {
        this.TAG = TAG;
    }

    public Action (String TAG, boolean singleResponse){
        this.TAG = TAG;
        this.singleResponse = singleResponse;
    }

    public Action(String TAG, boolean singleResponse, boolean needStaticRAMCache) {
        this.TAG = TAG;
        this.singleResponse = singleResponse;
        this.needStaticRAMCache = needStaticRAMCache;
    }

    public boolean isSingleResponse() {
        return singleResponse;
    }

    public String getTAG() {
        return TAG;
    }

    public boolean isNeedStaticRAMCache() {
        return needStaticRAMCache;
    }
}
