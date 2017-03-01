package com.fifed.architecture.datacontroller.interaction.core;


/**
 * Created by Fedir on 05.07.2016.
 */
public class ErrorData {
    private String TAG;
    private String globalErrorMessage;

    public ErrorData(String TAG){
        this.TAG = TAG;
    }

    public ErrorData(String TAG, String globalErrorMessage) {
        this.TAG = TAG;
        this.globalErrorMessage = globalErrorMessage;
    }


    public String getTAG() {
        return TAG;
    }

    public String getGlobalErrorMessage() {
        return globalErrorMessage;
    }

}
