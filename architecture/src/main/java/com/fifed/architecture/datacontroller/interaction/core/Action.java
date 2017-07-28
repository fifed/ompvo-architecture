package com.fifed.architecture.datacontroller.interaction.core;

/**
 * Created by Fedir on 05.07.2016.
 */
public abstract class Action {
    private String observerTag;
    private boolean singleResponse;
    private boolean needStaticRAMCache;


    public Action(String observerTag) {
        this.observerTag = observerTag;
    }

    public Action (String observerTag, boolean singleResponse){
        this.observerTag = observerTag;
        this.singleResponse = singleResponse;
    }

    public Action(String observerTag, boolean singleResponse, boolean needStaticRAMCache) {
        this.observerTag = observerTag;
        this.singleResponse = singleResponse;
        this.needStaticRAMCache = needStaticRAMCache;
    }

    public boolean isSingleResponse() {
        return singleResponse;
    }

    public String getObserverTag() {
        return observerTag;
    }

    public boolean isNeedStaticRAMCache() {
        return needStaticRAMCache;
    }
}
