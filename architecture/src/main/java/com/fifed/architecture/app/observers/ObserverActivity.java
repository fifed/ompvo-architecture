package com.fifed.architecture.app.observers;


import com.fifed.architecture.datacontroller.interaction.core.ErrorData;
import com.fifed.architecture.datacontroller.interaction.core.Model;

/**
 * Created by Fedir on 30.06.2016.
 */
public interface ObserverActivity {

    boolean onBackPressed();

    void onUpdateData(Model model);

    void onError(ErrorData errorData);

    void onPassiveObserveUpdateData(Model model);

    void onPassiveObserveError(ErrorData errorData);

    String getObserverTag();
}
