package com.fifed.architecture.app.fragments.core;

import android.app.Service;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.CallSuper;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;

import com.fifed.architecture.R;
import com.fifed.architecture.app.activities.interfaces.ActivityActionInterface;
import com.fifed.architecture.app.activities.interfaces.ActivityContentInterface;
import com.fifed.architecture.app.activities.interfaces.ActivityStateInterface;
import com.fifed.architecture.app.activities.interfaces.feedback_interfaces.core.FragmentFeedBackInterface;
import com.fifed.architecture.app.constants.BaseFragmentIdentifier;
import com.fifed.architecture.app.fragments.utils.FragmentAnimUtils;
import com.fifed.architecture.app.observers.ObservebleActivity;
import com.fifed.architecture.app.observers.ObserverActivity;
import com.fifed.architecture.datacontroller.interaction.core.Action;
import com.fifed.architecture.datacontroller.interaction.core.ErrorData;
import com.fifed.architecture.datacontroller.interaction.core.Model;


/**
 * Created by Fedir on 30.06.2016.
 */
public abstract class BaseFragment extends Fragment implements ObserverActivity, View.OnClickListener{
    private static boolean isEnabledRestoreAnim;
    private String TAG;
    private boolean fromBackStack;
    private boolean reloadedAsNewFragment;
    private static Class<? extends Fragment> lastFragment;
    private final String OUT_STATE_DATA = "outStateData";
    private Bundle outStateData;
    private boolean isExecuteAnim;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root;
        try {
            ViewDataBinding vb = DataBindingUtil.inflate(inflater, getLayoutResource(), null, false);
            onBindingFinish(vb);
            root = vb.getRoot();
        } catch (NoClassDefFoundError | NullPointerException e) {
            root = inflater.inflate(getLayoutResource(), null);

        }
        initUI(root);
        setListeners();
        return root;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putBundle(OUT_STATE_DATA, outStateData);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(savedInstanceState != null) {
            outStateData = savedInstanceState.getBundle(OUT_STATE_DATA);
        }
        TAG = getClass().getSimpleName() + String.valueOf(hashCode());
        setRetainInstance(true);
        ((ObservebleActivity) getActivity()).addAsPassiveObservers(this);
    }

    @Override
    public void setArguments(Bundle args) {
        outStateData = args;
        super.setArguments(args);
    }

    @Override
    public String getObserverTag() {
        return getCustomTAG();
    }

    @LayoutRes
    protected abstract int getLayoutResource();

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ((ObservebleActivity) getActivity()).registerObserver(this);
        onFragmentRegisteredAsObserver();
    }

    @Override
    public void onDestroyView() {
        ((ObservebleActivity) getActivity()).unregisterObserver(this);
        onFragmentUnregisteredAsObserver();
        fromBackStack = true;
        lastFragment = getClass();
        super.onDestroyView();
        hideKeyboard();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        ((ObservebleActivity) getActivity()).removePassiveObservers(this);
    }

    @CallSuper
    @Override
    public Animation onCreateAnimation(int transit, boolean enter, int nextAnim) {
        Animation animation;
        if(FragmentAnimUtils.isRevertAnim()) {
            if (enter) {
                animation = AnimationUtils.loadAnimation(getActivity(), R.anim.fragment_animation_enter);
            } else {
                animation = AnimationUtils.loadAnimation(getActivity(), R.anim.fragment_animation_exit);
            }
            if(!isEnabledRestoreAnim) {
                isEnabledRestoreAnim = true;
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        FragmentAnimUtils.restoreAnim();
                        isEnabledRestoreAnim = false;
                    }
                }, animation.getDuration());
            }
        } else if(nextAnim != 0){
            animation = AnimationUtils.loadAnimation(getActivity(), nextAnim);
        } else {
            animation = super.onCreateAnimation(transit, enter, nextAnim);
        }
        if(animation != null){
            animation.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {
                    isExecuteAnim = true;
                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    isExecuteAnim = false;
                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });
        }
        return animation;
    }

    @Override
    public final boolean handleOnBackPressed() {
        return isExecuteAnim || onBackPressed();
    }

    public boolean onBackPressed(){
        return false;
    }

    protected void hideKeyboard(){
        ((InputMethodManager)getContext().getSystemService(Service.INPUT_METHOD_SERVICE)).
                hideSoftInputFromWindow(getActivity().findViewById(android.R.id.content).getWindowToken(), 0);
    }

    protected boolean isKeyboardVisible(){
        return ((InputMethodManager)getContext().getSystemService(Service.INPUT_METHOD_SERVICE)).isActive();
    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public void onPassiveObserveUpdateData(Model model) {

    }

    @Override
    public void onPassiveObserveError(ErrorData errorData) {

    }

    @Override
    public void onPreloadFinish(Action action) {

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
    protected void initUI(View v){}
    protected void onBindingFinish(ViewDataBinding vb){}
    protected  void setListeners(){}
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

    public boolean isRestoredFromBackStack() {
        return fromBackStack;
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

    public BaseFragment reloadedAsNewFragment(boolean reloadedAsNewFragment){
        this.reloadedAsNewFragment = reloadedAsNewFragment;
        return this;
    }
    protected boolean isReloadedAsNewFragment(){
        return reloadedAsNewFragment;
    }
    @CallSuper
    public void onReloadFragmentDataWithOutChangeState(@Nullable Bundle bundle){
        outStateData = bundle;
    }
    @CallSuper
    public void onReloadFromPassiveState(Bundle bundle) {
        outStateData = bundle;
    }

    @Nullable
    protected Bundle getFragmentData(){
        return outStateData;
    }
    public static Class<? extends Fragment> getLastVisibleFragment() {
        return lastFragment;
    }
}

