package com.fifed.architecture.app.dialogs.core;

import android.app.Dialog;
import android.app.Service;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.DrawableRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StyleRes;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;

import com.fifed.architecture.app.activities.interfaces.ActivityActionInterface;
import com.fifed.architecture.app.activities.interfaces.ActivityContentInterface;
import com.fifed.architecture.app.activities.interfaces.ActivityStateInterface;
import com.fifed.architecture.app.activities.interfaces.feedback_interfaces.core.FragmentFeedBackInterface;
import com.fifed.architecture.app.constants.BaseFragmentIdentifier;
import com.fifed.architecture.app.observers.ObservebleActivity;
import com.fifed.architecture.app.observers.ObserverActivity;
import com.fifed.architecture.app.utils.ResourceHelper;

/**
 * Created by Fedir on 10.12.2016.
 */

public abstract class BaseDialogFragment extends DialogFragment implements ObserverActivity, View.OnClickListener {

    private String TAG;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        setBackgroundWindow();
        View rootView = inflater.inflate(getLayoutResource(), null);
        initUI(rootView);
        setListeners();
        return rootView;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(isFullScreen()) {
            setStyle(STYLE_NO_FRAME, android.R.style.Theme);
        }
        TAG = getClass().getSimpleName() + String.valueOf(hashCode());
        setRetainInstance(true);
    }

    @Override
    public String getObserverTag() {
        return getCustomTAG();
    }

    @LayoutRes
    protected abstract int getLayoutResource();

    protected abstract boolean isFullScreen();

    protected abstract @DrawableRes int getDialogWindowBackground();

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ((ObservebleActivity) getActivity()).registerObserver(this);
        onFragmentRegisteredAsObserver();

    }

    private void setBackgroundWindow(){
        if(getDialogWindowBackground() != 0) {
            Window window = getDialog().getWindow();
            if(window != null) {
                window.setBackgroundDrawable((ResourceHelper.getResources(getDialogWindowBackground(), getActivity())));
            }
        }
    }

    private void setWindowAnim(Dialog dialog){
        Window window = dialog.getWindow();
        if(window != null) {
            window.setWindowAnimations(getWindowAnimStyle());
        }
    }


    @Override
    public void onPause() {
        super.onPause();
        hideKeyboard();
    }

    @Override
    public void onDestroyView() {
        ((ObservebleActivity) getActivity()).unregisterObserver(this);
        onFragmentUnregisteredAsObserver();
        super.onDestroyView();
    }


    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = new Dialog(getActivity(), android.R.style.Theme){
            @Override
            public void onBackPressed() {
                if(!BaseDialogFragment.this.onBackPressed()){
                    dismiss();
                }
            }
        };
        setWindowAnim(dialog);
        return dialog;
    }


    protected void hideKeyboard(){
        ((InputMethodManager)getContext().getSystemService(Service.INPUT_METHOD_SERVICE)).
                hideSoftInputFromWindow(getActivity().findViewById(android.R.id.content).getWindowToken(), 0);
    }


    public String getCustomTAG() {
        return TAG;
    }

    public FragmentFeedBackInterface getFragmentFeedBackInterface() {
        return (FragmentFeedBackInterface) getActivity();
    }

    protected void changeFragmentTo(final BaseFragmentIdentifier fragmentsID, @Nullable final Bundle bundle){
        if(getFragmentFeedBackInterface() != null && !isAfterSaveInstanteState()){
            getFragmentFeedBackInterface().changeFragmentTo(fragmentsID, bundle);
        } else if(getFragmentFeedBackInterface() != null){
            new Handler(getActivity().getMainLooper()).postDelayed(new Runnable() {
                @Override
                public void run() {
                    changeFragmentTo(fragmentsID, bundle);
                }
            }, 500);
        }
    }

    public ActivityActionInterface getActionInterface() {
        return (ActivityActionInterface) getActivity();
    }
    protected abstract View initUI(View v);
    protected abstract void setListeners();
    protected abstract @StyleRes int getWindowAnimStyle();
    protected  void onFragmentRegisteredAsObserver(){

    }
    protected void onFragmentUnregisteredAsObserver(){

    }

    protected ActivityContentInterface getContentInteface(){
        return (ActivityContentInterface)getActivity();
    }
    protected Toolbar getToolbar(){
        return getContentInteface().getToolbar();
    }
    protected void initBackPressed(){
        getFragmentFeedBackInterface().initBackPressed();
    }
    protected void showBackArrowOnToolbar(){
        getToolbar().setNavigationIcon(0);
        getToolbar().setLogo(0);
    }
    protected void hideBackArrowOnToolbar(){
        getToolbar().setNavigationIcon(null);
        getToolbar().setLogo(null);
    }
    protected DrawerLayout getDrawer(){
        return getContentInteface().getDrawer();
    }
    protected void showMenuIconOnToolbar(){
        getToolbar().setNavigationIcon(0);
    }
    protected void hideMenuIconOnToolbar(){
        getToolbar().setNavigationIcon(null);
    }
    protected void clearAllFragmentsBackStack(){
        FragmentManager fm = getActivity().getSupportFragmentManager();
        for(int i = 0; i < fm.getBackStackEntryCount(); ++i) {
            fm.popBackStack();
        }
    }
    protected ViewGroup getToolbarContainer(){
        return ((ActivityContentInterface) getActivity()).getToolbarContainer();
    }

    protected SharedPreferences getSharedPreferences(){
        return getActivity().getPreferences(Context.MODE_PRIVATE);
    }
    protected SharedPreferences.Editor getPreferencesEditor(){
        return getActivity().getPreferences(Context.MODE_PRIVATE).edit();
    }
    protected boolean isPortraitMode(){
        return getActivity().getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT;
    }
    protected  boolean isTablet() {
        boolean xlarge = ((getActivity().getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) ==
                Configuration.SCREENLAYOUT_SIZE_XLARGE);
        boolean large = ((getActivity().getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) ==
                Configuration.SCREENLAYOUT_SIZE_LARGE);
        return (xlarge || large);
    }
    protected boolean isAfterSaveInstanteState(){
        return ((ActivityStateInterface)getActivity()).isAfterSaveInstanceState();
    }
}


