package com.fifed.architecture.app.mvp.managerui_fabrics;

import android.app.Activity;
import android.content.res.Configuration;
import android.support.v7.app.AppCompatActivity;

import com.fifed.architecture.app.mvp.managers_ui.interfaces.core.ManagerUI;


/**
 * Created by Fedir on 01.07.2016.
 */
public abstract class ManagerUIActivityFabric {

    public abstract ManagerUI getAuthManagerUI(AppCompatActivity activity);
    public abstract ManagerUI getContentManagerUI(AppCompatActivity activity);

    protected  boolean isTablet(Activity activity) {
        boolean xlarge = ((activity.getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) ==
                Configuration.SCREENLAYOUT_SIZE_XLARGE);
        boolean large = ((activity.getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) ==
                Configuration.SCREENLAYOUT_SIZE_LARGE);
        return (xlarge || large);
    }

    protected boolean isPortraitOrientation (Activity activity){
       return activity.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT;
    }

    protected boolean isLandscapeOrientation (Activity activity){
        return activity.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE;
    }

}
