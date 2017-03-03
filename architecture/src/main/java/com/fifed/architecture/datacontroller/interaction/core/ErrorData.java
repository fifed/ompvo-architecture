package com.fifed.architecture.datacontroller.interaction.core;


/**
 * Created by Fedir on 05.07.2016.
 */
public class ErrorData {
    private String TAG;
    private String globalErrorMessage;
    private Throwable error;

    public ErrorData(String TAG, String globalErrorMessage, Throwable error){
        this.TAG = TAG;
        this.globalErrorMessage = globalErrorMessage;
        this.error = error;
    }

    public ErrorData(String TAG, Throwable error) {
        this.TAG = TAG;
        this.error = error;
    }

    public String getTAG() {
        return TAG;
    }

    public String getGlobalErrorMessage() {
        return globalErrorMessage;
    }

    public Throwable getError() {
        return error;
    }
}
