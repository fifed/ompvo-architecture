package com.fifed.architecture.app.activities.interfaces;

import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.Toolbar;
import android.view.ViewGroup;

/**
 * Created by Fedir on 24.07.2016.
 */
public interface ActivityContentInterface {
    Toolbar getToolbar();
    DrawerLayout getDrawer();
    ViewGroup getToolbarContainer();
}
