package com.fifed.architecture.datacontroller.interaction.fcm_pushes;

import com.fifed.architecture.datacontroller.interaction.fcm_pushes.core.FcmPush;

import java.util.Map;


/**
 * Created by Fedir on 10.07.2016.
 */
public class FcmPushData implements FcmPush {
    private Map<String, String> data;

    public Map<String, String> getData() {
        return data;
    }

    public FcmPushData setData(Map<String, String> data) {
        this.data = data;
        return this;
    }
}
