package com.fifed.architecture.datacontroller.interactor.core;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import com.fifed.architecture.app.mvp.presenters.intefaces.Presenter;
import com.fifed.architecture.datacontroller.interaction.core.Action;
import com.fifed.architecture.datacontroller.interaction.core.ErrorData;
import com.fifed.architecture.datacontroller.interaction.core.Model;
import com.fifed.architecture.datacontroller.interactor.core.interfaces.InteractorActionInterface;
import com.fifed.architecture.datacontroller.interactor.observer.interfaces.ObservableInteractor;
import com.fifed.architecture.datacontroller.interactor.observer.interfaces.ObserverInteractor;
import com.fifed.architecture.datacontroller.interactor.utils.InternetConnectionObserver;

import java.util.ArrayList;
import java.util.List;

import static com.fifed.architecture.datacontroller.interactor.utils.InternetConnectionObserver.*;


/**
 * Created by Fedir on 05.07.2016.
 */
public abstract class BaseInteractor implements ObservableInteractor, InteractorActionInterface {
    private long MAX_PRELOAD_WAII_TIME = 500;
    private Handler handler;
    private ArrayList<ObserverInteractor> observerList = new ArrayList<>();
    private List<Model> modelBuffer = new ArrayList<>();
    private List<ErrorData> errorBuffer = new ArrayList<>();
    private List<Action> preloadActionQueue = new ArrayList<>();
    private List<Action> offlineActionQueue = new ArrayList<>();
    private List<Model> preloadedModels = new ArrayList<>();
    private List<Model> staticCache = new ArrayList<>();
    private Context context;

    public abstract void onUserAction(Action action);
    protected void onInternetConnectionStateChanged(ConnectionState state){}
    protected void onInternetConnectionStateChanged(boolean isConnected){}

    protected BaseInteractor(Context context) {
        this.context = context;
        handler = new Handler(Looper.getMainLooper());
        initInternetConnectionObserver();
    }

    private void initInternetConnectionObserver(){
        new InternetConnectionObserver(context, new ConnectionListener() {
            @Override
            public void onConnectionStateChanged(ConnectionState state) {
                onInternetConnectionStateChanged(state);
                boolean isConnected = false;
                switch (state){
                    case WIFI_CONNECTED:
                        isConnected = true;
                        break;
                    case MOBILE_NETWORK_CONNECTED:
                        isConnected = true;
                        break;
                    case DISCONNECTED:
                        isConnected = false;
                        break;
                }
                onInternetConnectionStateChanged(isConnected);
                for (int i = 0; i < observerList.size(); i++) {
                    observerList.get(i).onInternetConnectionStateChanged(isConnected);
                }
            }
        });
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
                preloadedModels.get(i).setAction(action);
                for (int j = 0; j < observerList.size(); j++) {
                    observerList.get(j).onUpdateData(preloadedModels.get(i));
                }
                preloadedModels.remove(i);
                return;
            }
        }
        for (int i = 0; i < staticCache.size(); i++) {
            if(action.getClass().getSimpleName().equals(staticCache.get(i).getClass().getSimpleName())){
                notifyObserversOnUpdateData(staticCache.get(i));
                return;
            }
        }

        boolean containsAction = false;
        for (int i = 0; i < preloadActionQueue.size(); i++) {
            if(preloadActionQueue.get(i) != null && action.getClass().getSimpleName().equals(preloadActionQueue.get(i).getClass().getSimpleName())){
                containsAction = true;
                break;
            }
        }
        if(!containsAction){
            onUserAction(action);
        }

    }

    @Override
    public void sendPreloadAction(final Action action) {
        boolean containsAction = false;
        for (int i = 0; i < preloadActionQueue.size(); i++) {
            if(preloadActionQueue.get(i) != null && action.getClass().getSimpleName().equals(preloadActionQueue.get(i).getClass().getSimpleName())){
               containsAction = true;
                break;
            }
        }
        if(!containsAction){
            preloadActionQueue.add(action);
            onUserAction(action);
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    if(preloadActionQueue != null && preloadActionQueue.contains(action)) {
                        for (int i = 0; i < observerList.size(); i++) {
                            observerList.get(i).onPreloadFinished(action);
                        }
                    }
                }
            }, MAX_PRELOAD_WAII_TIME);
        }
    }

    @Override
    public void notifyObserversOnUpdateData(final Model model) {
        boolean isInPreloadQueue = false;
        for (int i = 0; i < preloadActionQueue.size(); i++) {
                if(preloadActionQueue != null && preloadActionQueue.contains(model.getAction())){
                    preloadActionQueue.remove(model.getAction());
                    isInPreloadQueue = true;
                }
        }
        if(isInPreloadQueue){
            for (int i = 0; i < preloadedModels.size(); i++) {
                if(model.getClass().getSimpleName().equals(preloadedModels.get(i).getClass().getSimpleName())){
                    preloadedModels.remove(i);
                }
            }
            preloadedModels.add(model);
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    if(preloadedModels != null && preloadedModels.contains(model)){
                        preloadedModels.remove(model);
                    }
                }
            }, 1000);

            for (int i = 0; i < observerList.size(); i++) {
                observerList.get(i).onPreloadFinished(model.getAction());
            }

        } else {
            if (model.getAction().isNeedStaticRAMCache()) {
                for (int i = 0; i < staticCache.size(); i++) {
                    if (model.getClass().getSimpleName().equals(staticCache.get(i).getClass().getSimpleName())) {
                        staticCache.remove(i);
                    }
                }
                staticCache.add(model);
            }
            boolean containsActiveActivity = false;
            for (int i = 0; i < observerList.size(); i++) {
                if (observerList.get(i) instanceof Presenter) {
                    if (((Presenter) (observerList.get(i))).getObserverState() == Presenter.ObserverState.ACTIVE)
                        containsActiveActivity = true;
                    break;
                }
            }
            if (observerList.size() == 0 || !containsActiveActivity) {
                writeBufferModel(model);
            } else {
                for (int i = 0; i < observerList.size(); i++) {
                    ObserverInteractor observer = observerList.get(i);
                    observer.onUpdateData(model);
                }
            }
        }
    }

    @Override
    public void notifyObserversOnError(final ErrorData errorData) {
        Log.e("ErrorDataLog",  errorData.getClass().getSimpleName());
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

        for (int i = 0; i < preloadActionQueue.size(); i++) {
            if(preloadActionQueue.get(i) != null && errorData.getAction().getClass().getSimpleName().equals(preloadActionQueue.get(i).getClass().getSimpleName())){
                if(preloadActionQueue != null && preloadActionQueue.contains(errorData.getAction())){
                    preloadActionQueue.remove(errorData.getAction());
                }
            }
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

    public void removePreloadedDataWhereActions(Class<? extends Action> ... actionCls){
        for (Class c : actionCls) {
            for (int i = 0; i < preloadedModels.size(); i++) {
                if(c.getSimpleName().equals(preloadedModels.get(i).getAction().getClass().getSimpleName())){
                    preloadedModels.remove(i);
                }
            }
        }
    }

    public void removePreloadedDataWhereModels(Class<? extends Model> ... modelCls){
        for (Class c : modelCls) {
            for (int i = 0; i < preloadedModels.size(); i++) {
                if(c.getSimpleName().equals(preloadedModels.get(i).getClass().getSimpleName())){
                    preloadedModels.remove(i);
                }
            }
        }
    }

    public void clearStaticCashe(){
        staticCache.clear();
    }


    public void removeStaticCasheWhereActions(Class<? extends Action> ... actionCls){
        for (Class c : actionCls) {
            for (int i = 0; i < staticCache.size(); i++) {
                if(c.getSimpleName().equals(staticCache.get(i).getAction().getClass().getSimpleName())){
                    staticCache.remove(i);
                }
            }
        }
    }

    public void removeStaticCasheWhereModels(Class<? extends Model> ... modelCls){
        for (Class c : modelCls) {
            for (int i = 0; i < staticCache.size(); i++) {
                if(c.getSimpleName().equals(staticCache.get(i).getClass().getSimpleName())){
                    staticCache.remove(i);
                }
            }
        }
    }

    public void setMaxPreloadWaitTime(long maxPreloadWaitTime) {
        this.MAX_PRELOAD_WAII_TIME = maxPreloadWaitTime;
    }
}

