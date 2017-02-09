package com.fifed.architecture.app.activities;

import com.fifed.architecture.app.activities.core.BaseActivity;
import com.fifed.architecture.app.activities.interfaces.feedback_interfaces.ContentActivityFragmentsFeedbacks;
import com.fifed.architecture.app.mvp.managers_ui.interfaces.ManagerUIContentActivity;


/**
 * Created by Fedir on 01.07.2016.
 */
public abstract class BaseContentActivity extends BaseActivity implements ContentActivityFragmentsFeedbacks {

    @Override
    public ManagerUIContentActivity getManagerUI() {
        return (ManagerUIContentActivity) managerUI;
    }
}
