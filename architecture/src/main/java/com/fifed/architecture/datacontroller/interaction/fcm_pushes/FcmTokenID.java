package com.fifed.architecture.datacontroller.interaction.fcm_pushes;

import com.fifed.architecture.datacontroller.interaction.fcm_pushes.core.FcmPush;

/**
 * Created by Fedir on 10.07.2016.
 */
public class FcmTokenID implements FcmPush {
    private String tokenID;

    public String getTokenID() {
        return tokenID;
    }

    public FcmTokenID setTokenID(String tokenID) {
        this.tokenID = tokenID;
        return this;
    }
}
