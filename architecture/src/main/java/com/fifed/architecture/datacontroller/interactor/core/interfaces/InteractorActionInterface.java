package com.fifed.architecture.datacontroller.interactor.core.interfaces;


import com.fifed.architecture.datacontroller.interaction.core.Action;

/**
 * Created by Fedir on 05.07.2016.
 */
public interface InteractorActionInterface {
    void sendUserAction(Action action);
    void sendPreloadAction(Action action);
    void onObserverDestroyed(String observerTag);
}
