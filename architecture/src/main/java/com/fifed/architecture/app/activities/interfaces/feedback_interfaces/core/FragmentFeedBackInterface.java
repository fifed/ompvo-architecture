package com.fifed.architecture.app.activities.interfaces.feedback_interfaces.core;

import com.fifed.architecture.app.constants.FragmentData;
import com.fifed.architecture.app.mvp.view_notification.ViewNotification;


/**
 * Created by Fedir on 06.07.2016.
 */
public interface FragmentFeedBackInterface {
    void initBackPressed();
    void changeFragmentTo(FragmentData data);
    void sendNotificationToManager(ViewNotification notification);
}
