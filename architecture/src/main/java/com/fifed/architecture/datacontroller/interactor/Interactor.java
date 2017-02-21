package com.fifed.architecture.datacontroller.interactor;

import android.content.Context;
import android.support.annotation.CallSuper;
import android.util.Log;

import com.fifed.architecture.datacontroller.interaction.core.Action;
import com.fifed.architecture.datacontroller.interaction.fcm_pushes.FcmPushData;
import com.fifed.architecture.datacontroller.interaction.fcm_pushes.FcmTokenID;
import com.fifed.architecture.datacontroller.interaction.fcm_pushes.core.FcmPush;
import com.fifed.architecture.datacontroller.interactor.core.BaseInteractor;
import com.fifed.architecture.datacontroller.interactor.observer.interfaces.ObservableInteractor;

/**
 * Created by Fedir on 05.07.2016.
 */
public class Interactor extends BaseInteractor {
    private static Interactor interactor;
    public String fcmToken;

    public Interactor(Context context) {
        super(context);
    }

    public static ObservableInteractor getInteractor(Context context) {
        return (interactor == null) ? interactor = new Interactor(context) : interactor;
    }


    public void onPushEvent(FcmPush push) {
        if (push instanceof FcmTokenID) {
            fcmToken = ((FcmTokenID) push).getTokenID();
        } else if (push instanceof FcmPushData) {
            Log.v("Push_LOG", ((FcmPushData) (push)).getData().toString());
        }
    }


    @Override
    @CallSuper
    public void onUserAction(Action action) {



    }

    @Override
    public void onObserverIsDestroyed(String observerTag) {

    }

}
