package com.fifed.architecture.app.mvp.presenters;

import com.fifed.architecture.app.application.core.BaseApp;
import com.fifed.architecture.app.mvp.presenters.intefaces.Presenter;
import com.fifed.architecture.app.mvp.views.ActivityView;
import com.fifed.architecture.datacontroller.interaction.core.Action;
import com.fifed.architecture.datacontroller.interaction.core.ErrorData;
import com.fifed.architecture.datacontroller.interaction.core.Model;
import com.fifed.architecture.datacontroller.interactor.core.interfaces.InteractorActionInterface;
import com.fifed.architecture.datacontroller.interactor.observer.interfaces.ObservableInteractor;
import com.fifed.architecture.datacontroller.interactor.observer.interfaces.ObserverInteractor;

import java.lang.ref.WeakReference;


/**
 * Created by Fedir on 04.07.2016.
 */
public abstract class BaseViewPresenter implements Presenter, ObserverInteractor {
    private ActivityView activityView;

    public BaseViewPresenter(ActivityView activityView) {
        this.activityView = new WeakReference<>(activityView).get();
        registerAsObserver();
    }

    private void registerAsObserver(){
        getObservable().registerObserver(this);
    }

    @Override
    public void notifyObserverIsDestroyed(String observerTag) {
        getActionInterface().onObserverIsDestroyed(observerTag);
    }

    @Override
    public void onUpdateData(Model model) {
        activityView.onUpdateData(model);
    }

    @Override
    public void onError(ErrorData errorData) {
        activityView.onError(errorData);
    }

    @Override
    public void onPreloadFinished(Action action) {
        activityView.onPreloadFinished(action);
    }

    @Override
    public   void onUserMadeAction(Action action){
        getActionInterface().sendUserAction(action);
    }

    @Override
    public void onPreloadAction(Action action) {
        getActionInterface().sendPreloadAction(action);
    }

    @Override
    public void onInternetConnectionStateChanged(boolean isConnected) {
        activityView.onInternetConnectionStateChanged(isConnected);
    }

    @Override
    public  void onPresenterDestroy(){
        getObservable().unregisterObserver(this);
    }


    private   InteractorActionInterface getActionInterface(){
        return BaseApp.getActionInterface();
    }
    private ObservableInteractor getObservable(){
        return BaseApp.getObservable();
    }

}
