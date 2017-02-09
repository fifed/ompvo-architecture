package com.fifed.architecture.app.activities.interfaces.feedback_interfaces;

import com.fifed.architecture.app.activities.interfaces.feedback_interfaces.core.FragmentFeedBackInterface;

/**
 * Created by Fedir on 04.07.2016.
 */
public interface AuthActivityFragmentsFeedbacks extends FragmentFeedBackInterface {
    void startContentActivity(int userID, String key, Class<?> cls);
}
