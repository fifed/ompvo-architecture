package com.fifed.architecture.datacontroller.interactor.core;

import android.content.Context;
import android.util.Log;

import com.fifed.architecture.app.mvp.presenters.intefaces.Presenter;
import com.fifed.architecture.datacontroller.interaction.core.Action;
import com.fifed.architecture.datacontroller.interaction.core.ErrorData;
import com.fifed.architecture.datacontroller.interaction.core.Model;
import com.fifed.architecture.datacontroller.interactor.core.interfaces.InteractorActionInterface;
import com.fifed.architecture.datacontroller.interactor.observer.interfaces.ObservableInteractor;
import com.fifed.architecture.datacontroller.interactor.observer.interfaces.ObserverInteractor;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by Fedir on 05.07.2016.
 */
public abstract class BaseInteractor implements ObservableInteractor, InteractorActionInterface {
    private ArrayList<ObserverInteractor> observerList = new ArrayList<>();
    private List<Model> modelBuffer = new ArrayList<>();
    private List<ErrorData> errorBuffer = new ArrayList<>();
    private List<Action> offlineActionQueue = new ArrayList<>();
    private List<Model> preloadedModels = new ArrayList<>();
    private Context context;

    public abstract void onUserAction(Action action);
    protected BaseInteractor(Context context) {
        this.context = context;
    }

    @Override
    public void registerObserver(ObserverInteractor observer) {
        observerList.add(observer);
        if(observer instanceof Presenter) {
            pushBuffers();
        }
    }

    @Override
    public void unregisterObserver(ObserverInteractor observer) {
        observerList.remove(observer);
    }

    @Override
    public void sendUserAction(Action action) {
        for (int i = 0; i < preloadedModels.size(); i++) {
            if(preloadedModels.get(i) != null && action.getClass().getSimpleName().equals(preloadedModels.get(i).getAction().getClass().getSimpleName())){
                notifyObserversOnUpdateData(preloadedModels.get(i));
                preloadedModels.remove(i);
                return;
            }
        }
        onUserAction(action);
    }

    @Override
    public void notifyObserversOnUpdateData(Model model) {
        boolean containsActiveActivity = false;
        for (int i = 0; i < observerList.size(); i++) {
            if(observerList.get(i) instanceof Presenter){
                if(((Presenter)(observerList.get(i))).getObserverState() == Presenter.ObserverState.ACTIVE)
                containsActiveActivity = true;
                break;
            }
        }
        if(observerList.size() == 0 || !containsActiveActivity){
            writeBufferModel(model);
        } else {
            for (int i = 0; i < observerList.size(); i++) {
                ObserverInteractor observer = observerList.get(i);
                observer.onUpdateData(model);
            }
        }
    }

    @Override
    public void notifyObserversOnError(ErrorData errorData) {
        if(errorData.getError() != null) {
            if(errorData.getError().getMessage() != null && errorData.getError().getMessage().length() > 1){
                Log.e("ErrorDataLog", errorData.getError().getClass().getSimpleName() +" : " +
                        errorData.getError().getMessage());
            }
            if(errorData.getError().getCause() != null) {
                Log.e("ErrorDataLog", errorData.getError().getCause().getClass().getSimpleName() +" : " +
                        errorData.getError().getCause().toString());
            }
            if(errorData.getError().getStackTrace() != null) {
                StringBuilder builder = new StringBuilder();
                StackTraceElement[] stack = errorData.getError().getStackTrace();
                for (int i = 0; i < stack.length; i++) {
                    builder.append(stack[i].toString());
                    builder.append("\n");
                }
                Log.e("ErrorDataLog",  builder.toString());
            }
        }

        boolean containsActiveActivity = false;
        for (int i = 0; i < observerList.size(); i++) {
            if(observerList.get(i) instanceof Presenter){
                if(((Presenter)(observerList.get(i))).getObserverState() == Presenter.ObserverState.ACTIVE)
                    containsActiveActivity = true;
                break;
            }
        }
        if(observerList.size() == 0 || !containsActiveActivity){
            writeBufferError(errorData);
        } else {
            for (int i = 0; i < observerList.size(); i++) {
                ObserverInteractor observer = observerList.get(i);
                observer.onError(errorData);
            }
        }
    }

    @Override
    public void notifyObserversOnPreloadFinished(final Model model) {
        preloadedModels.add(model);
        for (int i = 0; i < observerList.size(); i++) {
            observerList.get(i).onPreloadFinished(model.getAction());
        }
    }

    public Context getContext() {
        return context;
    }

    private void writeBufferError(ErrorData error){
        errorBuffer.add(error);
    }

    private void writeBufferModel(Model model){
        modelBuffer.add(model);
    }

    private void pushBuffers(){
        if(modelBuffer.size() > 0) {
            for (int i = 0; i < observerList.size(); i++) {
                ObserverInteractor observer = observerList.get(i);
                for (int j = 0; j < modelBuffer.size(); j++) {
                    observer.onUpdateData(modelBuffer.get(j));
                }
            }
            modelBuffer.clear();
        }
        if(errorBuffer.size() > 0) {
            for (int i = 0; i < observerList.size(); i++) {
                ObserverInteractor observer = observerList.get(i);
                for (int j = 0; j < modelBuffer.size(); j++) {
                    observer.onError(errorBuffer.get(j));
                }
            }
            errorBuffer.clear();
        }
    }

    public void addActionToOfflineQueue(Action action){
        offlineActionQueue.add(action);
    }

    public void runOfflineQueue(){
        for (int i = 0; i < offlineActionQueue.size(); i++) {
            onUserAction(offlineActionQueue.get(i));
        }
        offlineActionQueue.clear();
    }

    public void clearPreloadedData(){
        preloadedModels.clear();
    }
}

