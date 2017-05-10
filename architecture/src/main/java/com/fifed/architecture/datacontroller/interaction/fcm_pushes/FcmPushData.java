package com.fifed.architecture.datacontroller.interaction.fcm_pushes;

import android.os.Bundle;

import com.fifed.architecture.datacontroller.interaction.fcm_pushes.core.FcmPush;


/**
 * Created by Fedir on 10.07.2016.
 */
public class FcmPushData implements FcmPush {
    private Bundle data;

    public Bundle getData() {
        return data;
    }

    public FcmPushData setData(Bundle data) {
        this.data = data;
        return this;
    }
}
