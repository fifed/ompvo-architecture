package com.fifed.architecture.app.activities.core;


import android.os.Bundle;
import android.support.annotation.ColorInt;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;

import com.fifed.architecture.app.activities.interfaces.ActivityActionInterface;
import com.fifed.architecture.app.activities.interfaces.ActivityContentInterface;
import com.fifed.architecture.app.activities.interfaces.ActivityStateInterface;
import com.fifed.architecture.app.activities.interfaces.feedback_interfaces.core.FragmentFeedBackInterface;
import com.fifed.architecture.app.constants.FragmentData;
import com.fifed.architecture.app.mvp.managers_ui.interfaces.core.ManagerUI;
import com.fifed.architecture.app.mvp.presenters.BaseViewPresenter;
import com.fifed.architecture.app.mvp.presenters.intefaces.Presenter;
import com.fifed.architecture.app.mvp.view_notification.ViewNotification;
import com.fifed.architecture.app.mvp.views.ActivityView;
import com.fifed.architecture.app.observers.ObservableActivity;
import com.fifed.architecture.app.observers.ObserverActivity;
import com.fifed.architecture.app.utils.ModelFilter;
import com.fifed.architecture.datacontroller.interaction.core.Action;
import com.fifed.architecture.datacontroller.interaction.core.ErrorData;
import com.fifed.architecture.datacontroller.interaction.core.Model;

import java.util.ArrayList;
import java.util.List;

import static com.fifed.architecture.app.utils.user_informer.UserSpecialInformer.DEF_COLOR;
import static com.fifed.architecture.app.utils.user_informer.UserSpecialInformer.showInfoErrorForUser;
import static com.fifed.architecture.app.utils.user_informer.UserSpecialInformer.showInformationForUser;

/**
 * Created by Fedir on 30.06.2016.
 */
public abstract class BaseActivity extends AppCompatActivity implements ObservableActivity, ActivityView, ActivityActionInterface, ActivityStateInterface, ActivityContentInterface, FragmentFeedBackInterface {
    private ArrayList<ObserverActivity> observerList = new ArrayList<>();
    private List<ObserverActivity> passiveObserverList = new ArrayList<>();
    protected ManagerUI managerUI;
    private Presenter presenter;
    private volatile boolean isClickedBackPressed;
    private String lastError = "";
    private View rootView;
    private final String WILL_BE_ROTATED = "willBeRotated";
    private boolean isRotated;
    private boolean afterSaveInstanceState;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(savedInstanceState != null){
            isRotated = savedInstanceState.getBoolean(WILL_BE_ROTATED, false);
        }
        rootView = findViewById(android.R.id.content);
        presenter = getViewPresenter();
        managerUI = getManagerUIToInit();
        onActivityInited();
    }


    protected void onActivityInited(){}

    public boolean isActivityRotated(){
        return isRotated;
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        afterSaveInstanceState = true;
        outState.putBoolean(WILL_BE_ROTATED, true);
        super.onSaveInstanceState(outState);
    }

    @Override
    public boolean isAfterSaveInstanceState() {
        return afterSaveInstanceState;
    }

    @Override
    protected void onResume() {
        super.onResume();
        afterSaveInstanceState = false;
    }

    protected abstract ManagerUI getManagerUIToInit();

    public abstract BaseViewPresenter getViewPresenter();

    @Override
    protected void onDestroy() {
        if(presenter !=  null) {
            presenter.onPresenterDestroy();
        }
        managerUI.onDestroyActivity();
        super.onDestroy();
    }


    public View getRootView() {
        return rootView;
    }


    @Override
    public void onBackPressed() {
        if (getDrawer() != null && getDrawer().isDrawerVisible(GravityCompat.START)) {
            getDrawer().closeDrawers();
        } else if (!notifyOnBackPressed()) {
            doubleBackPressed();
        }
    }

    protected void doubleBackPressed() {
        if(getExitWithDoubleClickText() == 0){
            super.onBackPressed();
        } else if (!isClickedBackPressed && getSupportFragmentManager().getBackStackEntryCount() == 0) {
            showInformationForUser(rootView, getString(getExitWithDoubleClickText()), getSnakbarTextColor(), getSnakbarBackgroundColor());
            isClickedBackPressed = true;
            rootView.postDelayed(new Runnable() {
                @Override
                public void run() {
                    isClickedBackPressed = false;
                }
            }, 2000);
        } else super.onBackPressed();
    }
    @StringRes
    public  int getExitWithDoubleClickText(){
        return 0;
    }

    protected @ColorInt int getSnakbarBackgroundColor() {
        return DEF_COLOR;
    }

    protected
    @ColorInt
    int getSnakbarTextColor() {
        return DEF_COLOR;
    }


    @Override
    public void registerObserver(ObserverActivity obsever) {
        observerList.add(obsever);
    }

    @Override
    public void unregisterObserver(ObserverActivity observer) {
        observerList.remove(observer);
    }

    @Override
    public boolean notifyOnBackPressed() {
        boolean fragmentGotInvoke = false;
        for (int i = 0; i < observerList.size(); i++) {
            ObserverActivity observer = observerList.get(i);
            if (fragmentGotInvoke = observer.handleOnBackPressed()) break;
        }
        return fragmentGotInvoke;
    }

    @Override
    public void onUpdateData(Model model) {
        notifyObserversOnUpdateData(model);
    }

    @Override
    public void onError(ErrorData errorData) {
        notifyObserversOnError(errorData);
    }

    @Override
    public void onPreloadFinished(Action action) {
        notifyObserversOnPreloadFinshed(action);
    }

    @Override
    public void notifyObserversOnPreloadFinshed(Action action) {
        for (int i = 0; i < observerList.size(); i++) {
            ObserverActivity observer = observerList.get(i);
            if (observer.getObserverTag().equals(action.getObserverTag())){
                observer.onPreloadFinish(action);
                break;
            }
        }
    }

    @Override
    public void notifyObserversOnUpdateData(Model model) {
        if(handleModelInActivity(model)){
            return;
        }
        if(model.getAction().isSingleResponse()){
            for (int i = 0; i < observerList.size(); i++) {
                ObserverActivity observer = observerList.get(i);
                if (observer.getObserverTag().equals(model.getAction().getObserverTag())) {
                    observer.onUpdateData(model);
                    break;
                }
            }
            for (int i = 0; i < passiveObserverList.size(); i++) {
                ObserverActivity observer = passiveObserverList.get(i);
                if(!observerList.contains(observer)) {
                    if (observer.getObserverTag().equals(model.getAction().getObserverTag())) {
                        observer.onPassiveObserveUpdateData(model);
                        break;
                    }
                }
            }

        } else {
            for (int i = 0; i < observerList.size(); i++) {
                ObserverActivity observer = observerList.get(i);
                if (ModelFilter.isObserverWorkingWithModel(observer, model)) {
                    observer.onUpdateData(model);
                }
            }
            for (int i = 0; i < passiveObserverList.size(); i++) {
                ObserverActivity observer = passiveObserverList.get(i);
                if(!observerList.contains(observer)) {
                    if (ModelFilter.isObserverWorkingWithModel(observer, model)) {
                        observer.onPassiveObserveUpdateData(model);
                    }
                }
            }
        }
    }

    protected boolean handleModelInActivity(Model model){
        return false;
    }

    @Override
    public void userMadeAction(Action action) {
        presenter.onUserMadeAction(action);
    }

    @Override
    public void preloadForAction(Action action) {
        presenter.onPreloadAction(action);
    }

    @Override
    public void notifyObserversOnError(ErrorData errorData) {
        if (errorData.getGlobalErrorMessage() != null) {
            handleGlobalError(errorData);
        }
        if(handleErrorInActivity(errorData)){
            return;
        }
        for (int i = 0; i < observerList.size(); i++) {
            ObserverActivity observer = observerList.get(i);
            if (observer.getObserverTag().equals(errorData.getAction().getObserverTag())){
                observer.onError(errorData);
                break;
            }
        }

        for (int i = 0; i < passiveObserverList.size(); i++) {
            ObserverActivity observer = passiveObserverList.get(i);
            if (!observerList.contains(observer) && observer.getObserverTag().equals(errorData.getAction().getObserverTag())){
                observer.onPassiveObserveError(errorData);
                break;
            }
        }
    }

    protected boolean handleErrorInActivity(ErrorData errorData){
        return false;
    }

    @Override
    public void addAsPassiveObservers(ObserverActivity observer) {
        passiveObserverList.add(observer);
    }

    @Override
    public void removePassiveObservers(ObserverActivity observer) {
        passiveObserverList.remove(observer);
        presenter.notifyObserverIsDestroyed(observer.getObserverTag());
    }

    @Override
    public void onInternetConnectionStateChanged(boolean isConnected) {
        for (int i = 0; i < observerList.size(); i++) {
            observerList.get(i).onInternetConnectionStateChanged(isConnected);
        }
    }

    @Override
    public void changeFragmentTo(FragmentData data) {
        managerUI.changeFragmentTo(data);
    }

    protected void handleGlobalError(ErrorData errorData) {
        if(!errorData.getGlobalErrorMessage().equals(lastError)) {
            showInfoErrorForUser(rootView, errorData.getGlobalErrorMessage(),
                    DEF_COLOR, DEF_COLOR);
            lastError = errorData.getGlobalErrorMessage();
            rootView.postDelayed(new Runnable() {
                @Override
                public void run() {
                    lastError = "";
                }
            }, 2000);
        }
    }

    @Override
    public Toolbar getToolbar() {
        return managerUI.getToolbar();
    }

    @Override
    public ViewGroup getToolbarContainer() {
        return managerUI.getToolbarContainer();
    }

    @Override
    public void initBackPressed() {
        onBackPressed();
    }


    public ManagerUI getManagerUI() {
        return managerUI;
    }

    @Override
    public DrawerLayout getDrawer() {
        return managerUI.getDrawer();
    }

    @Override
    public void sendNotificationToManager(ViewNotification notification) {
        managerUI.onReceiveNotification(notification);
    }

    public Presenter getPresenter() {
        return presenter;
    }

    public boolean isClickedBackPressed() {
        return isClickedBackPressed;
    }
}
