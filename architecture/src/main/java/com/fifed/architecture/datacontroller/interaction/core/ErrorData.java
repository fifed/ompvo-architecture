package com.fifed.architecture.datacontroller.interaction.core;


/**
 * Created by Fedir on 05.07.2016.
 */
public class ErrorData {
    private Action action;
    private String globalErrorMessage;
    private Throwable error;

    public ErrorData(Action action, String globalErrorMessage, Throwable error){
        this.action = action;
        this.globalErrorMessage = globalErrorMessage;
        this.error = error;
    }

    public ErrorData(Action action, Throwable error) {
        this.action = action;
        this.error = error;
    }

    public Action getAction() {
        return action;
    }

    public String getGlobalErrorMessage() {
        return globalErrorMessage;
    }

    public Throwable getError() {
        return error;
    }
}
