package com.fifed.architecture.app.activities.interfaces.feedback_interfaces.core;

import android.support.annotation.Nullable;

import com.fifed.architecture.app.constants.BaseFragmentIdentifier;
import com.fifed.architecture.app.mvp.view_data_pack.core.DataPack;
import com.fifed.architecture.app.mvp.view_notification.ViewNotification;


/**
 * Created by Fedir on 06.07.2016.
 */
public interface FragmentFeedBackInterface {
    void initBackPressed();
    void changeFragmentTo(BaseFragmentIdentifier fragmentsID, @Nullable DataPack pack);
    void sendNotificationToManager(ViewNotification notification);
}
