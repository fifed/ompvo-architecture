package com.fifed.architecture.datacontroller.interactor.observer.interfaces;


import com.fifed.architecture.datacontroller.interaction.core.ErrorData;
import com.fifed.architecture.datacontroller.interaction.core.Model;

/**
 * Created by Fedir on 05.07.2016.
 */
public interface ObservableInteractor {
    void registerObserver(ObserverInteractor observer);
    void unregisterObserver(ObserverInteractor observer);
    void notifyObserversOnUpdateData(Model model);
    void notifyObserversOnError(ErrorData errorData);
    void notifyObserversOnPreloadFinished(Model model);
}
