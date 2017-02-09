package com.fifed.architecture.datacontroller.interactor.core;

import android.content.Context;

import com.fifed.architecture.datacontroller.interaction.core.ErrorData;
import com.fifed.architecture.datacontroller.interaction.fcm_pushes.core.FcmPush;
import com.fifed.architecture.datacontroller.interaction.core.Model;
import com.fifed.architecture.datacontroller.interactor.core.interfaces.InteractorActionInterface;
import com.fifed.architecture.datacontroller.interactor.observer.interfaces.ObservableInteractor;
import com.fifed.architecture.datacontroller.interactor.observer.interfaces.ObserverInteractor;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;


/**
 * Created by Fedir on 05.07.2016.
 */
public abstract class BaseInteractor implements ObservableInteractor, InteractorActionInterface {
    private ArrayList<ObserverInteractor> observerList = new ArrayList<>();
    protected Context context;


    protected BaseInteractor(Context context) {
        this.context = context;
        EventBus.getDefault().register(this);
    }

    @Override
    public void registerObserver(ObserverInteractor observer) {
        observerList.add(observer);
    }

    @Override
    public void unregisterObserver(ObserverInteractor observer) {
        observerList.remove(observer);
    }

    @Override
    public void notifyObserversOnUpdateData(Model model) {
        for (int i = 0; i < observerList.size(); i++) {
            ObserverInteractor observer = observerList.get(i);
            observer.onUpdateData(model);
        }
    }

    @Override
    public void notifyObserversOnError(ErrorData errorData) {
        for (int i = 0; i < observerList.size(); i++) {
            ObserverInteractor observer = observerList.get(i);
            observer.onError(errorData);
        }
    }

    @Subscribe
    public void onPushEvent(FcmPush push) {

    }

    public Context getContext() {
        return context;
    }

}

