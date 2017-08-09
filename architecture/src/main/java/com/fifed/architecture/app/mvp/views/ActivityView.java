package com.fifed.architecture.app.mvp.views;


import com.fifed.architecture.datacontroller.interaction.core.Action;
import com.fifed.architecture.datacontroller.interaction.core.ErrorData;
import com.fifed.architecture.datacontroller.interaction.core.Model;

/**
 * Created by Fedir on 30.06.2016.
 */
public interface ActivityView {
    void onError(ErrorData errorData);
    void onUpdateData(Model model);
    void onPreloadFinished(Action action);
    void onInternetConnectionStateChanged(boolean isConnected);
}
