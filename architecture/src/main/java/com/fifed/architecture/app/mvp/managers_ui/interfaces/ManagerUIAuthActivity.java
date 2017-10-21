package com.fifed.architecture.app.mvp.managers_ui.interfaces;

import android.content.Intent;

import com.fifed.architecture.app.mvp.managers_ui.interfaces.core.ManagerUI;


/**
 * Created by Fedir on 01.07.2016.
 */
public interface ManagerUIAuthActivity extends ManagerUI{
    void startContentActivity(Intent intent);
}
