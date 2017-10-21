package com.fifed.architecture.app.mvp.managers_ui;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;

import com.fifed.architecture.R;
import com.fifed.architecture.app.mvp.managers_ui.interfaces.ManagerUIAuthActivity;
import com.fifed.architecture.app.mvp.managers_ui.core.BaseManagerUI;


/**
 * Created by Fedir on 01.07.2016.
 */
public  abstract class BaseAuthorizotionActivityManagerUI extends BaseManagerUI implements ManagerUIAuthActivity {

    public BaseAuthorizotionActivityManagerUI(AppCompatActivity activity) {
        super(activity);
    }

    @Override
    public void startContentActivity(Intent intent) {
        getActivity().startActivity(intent);
        getActivity().finish();
        getActivity().overridePendingTransition(R.anim.fragment_animation_enter, R.anim.fragment_animation_exit);
    }

    @Override
    protected Class<?> getFirstInStackFragmentClass() {
        return null;
    }
}
