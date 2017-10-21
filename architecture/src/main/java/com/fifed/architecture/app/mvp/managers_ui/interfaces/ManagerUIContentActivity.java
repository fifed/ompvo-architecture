package com.fifed.architecture.app.mvp.managers_ui.interfaces;


import android.content.Intent;

import com.fifed.architecture.app.mvp.managers_ui.interfaces.core.ManagerUI;

/**
 * Created by Fedir on 04.07.2016.
 */
public interface ManagerUIContentActivity extends ManagerUI{
    void startAuthActivity(Intent intent);
}
