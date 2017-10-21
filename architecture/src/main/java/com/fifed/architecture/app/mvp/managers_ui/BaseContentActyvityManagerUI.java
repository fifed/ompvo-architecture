package com.fifed.architecture.app.mvp.managers_ui;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;

import com.fifed.architecture.R;
import com.fifed.architecture.app.mvp.managers_ui.interfaces.ManagerUIContentActivity;
import com.fifed.architecture.app.mvp.managers_ui.core.BaseManagerUI;


/**
 * Created by Fedir on 01.07.2016.
 */
public abstract class BaseContentActyvityManagerUI extends BaseManagerUI implements ManagerUIContentActivity {

    public BaseContentActyvityManagerUI(AppCompatActivity activity) {
        super(activity);
    }

    @Override
    public void startAuthActivity(Intent intent) {
        getActivity().startActivity(intent);
        getActivity().finish();
        getActivity().overridePendingTransition(R.anim.fragment_animation_pop_enter, R.anim.fragment_animation_pop_exit);
    }
}
