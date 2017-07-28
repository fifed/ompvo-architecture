package com.fifed.architecture.datacontroller.interaction.core;


/**
 * Created by Fedir on 05.07.2016.
 */
public abstract class Model {
    private Action action;

    public Model(Action action) {
        this.action = action;
    }

    public Action getAction() {
        return action;
    }

    public void setAction(Action action) {
        this.action = action;
    }
}
