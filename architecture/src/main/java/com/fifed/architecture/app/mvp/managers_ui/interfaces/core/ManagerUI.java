package com.fifed.architecture.app.mvp.managers_ui.interfaces.core;

import android.support.annotation.LayoutRes;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.Toolbar;
import android.view.ViewGroup;

import com.fifed.architecture.app.constants.BaseFragmentIdentifier;
import com.fifed.architecture.app.mvp.view_data_pack.core.DataPack;
import com.fifed.architecture.app.mvp.view_notification.ViewNotification;

/**
 * Created by Fedir on 24.07.2016.
 */
public interface ManagerUI {
    void changeFragmentTo(BaseFragmentIdentifier fragmentsID, DataPack pack);
    Toolbar getToolbar();
    void initToolbar();
    @LayoutRes int getActivityRootLayout();
    DrawerLayout getDrawer();
    ViewGroup getToolbarContainer();
    void onDestroyActivity();
    void onReceiveNotification(ViewNotification notification);
}
