package com.fifed.architecture.app.application.core;

import android.app.Application;
import android.content.Context;


import com.fifed.architecture.datacontroller.interactor.core.interfaces.InteractorActionInterface;
import com.fifed.architecture.datacontroller.interactor.observer.interfaces.ObservableInteractor;


/**
 * Created by Fedir on 05.07.2016.
 */
public abstract class BaseApp extends Application {
    protected static Context context;
    protected static ObservableInteractor interactor;

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
        interactor = getInteractor();
    }

    public abstract ObservableInteractor getInteractor();

    public static Context getContext() {
        return context;
    }

    public static ObservableInteractor getObservable() {
        return interactor;
    }

    public static InteractorActionInterface getActionInterface() {
        return (InteractorActionInterface) interactor;
    }
}
