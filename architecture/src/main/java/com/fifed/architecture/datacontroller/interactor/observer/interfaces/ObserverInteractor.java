package com.fifed.architecture.datacontroller.interactor.observer.interfaces;

import com.fifed.architecture.datacontroller.interaction.core.Action;
import com.fifed.architecture.datacontroller.interaction.core.ErrorData;
import com.fifed.architecture.datacontroller.interaction.core.Model;

/**
 * Created by Fedir on 05.07.2016.
 */
public interface ObserverInteractor {
    void onUpdateData(Model model);

    void onError(ErrorData errorData);

    void onPreloadFinished(Action action);
}
