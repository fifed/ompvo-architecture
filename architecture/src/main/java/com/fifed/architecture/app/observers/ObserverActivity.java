package com.fifed.architecture.app.observers;


import com.fifed.architecture.datacontroller.interaction.core.Action;
import com.fifed.architecture.datacontroller.interaction.core.ErrorData;
import com.fifed.architecture.datacontroller.interaction.core.Model;

/**
 * Created by Fedir on 30.06.2016.
 */
public interface ObserverActivity {

    boolean handleOnBackPressed();

    void onUpdateData(Model model);

    void onError(ErrorData errorData);

    void onPassiveObserveUpdateData(Model model);

    void onPassiveObserveError(ErrorData errorData);

    void onPreloadFinish(Action action);

    String getObserverTag();
}
