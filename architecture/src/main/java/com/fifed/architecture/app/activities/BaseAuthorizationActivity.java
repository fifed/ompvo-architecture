package com.fifed.architecture.app.activities;

import com.fifed.architecture.app.activities.core.BaseActivity;
import com.fifed.architecture.app.activities.interfaces.feedback_interfaces.AuthActivityFragmentsFeedbacks;
import com.fifed.architecture.app.mvp.managers_ui.interfaces.ManagerUIAuthActivity;


/**
 * Created by Fedir on 01.07.2016.
 */
public abstract class BaseAuthorizationActivity extends BaseActivity implements AuthActivityFragmentsFeedbacks {
    @Override
    public ManagerUIAuthActivity getManagerUI() {
        return (ManagerUIAuthActivity) managerUI;
    }
}
