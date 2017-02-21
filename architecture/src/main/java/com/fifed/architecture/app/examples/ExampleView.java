package com.fifed.architecture.app.examples;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

import com.fifed.architecture.app.activities.interfaces.ActivityActionInterface;
import com.fifed.architecture.app.observers.ObservebleActivity;
import com.fifed.architecture.app.observers.ObserverActivity;
import com.fifed.architecture.datacontroller.interaction.core.Action;
import com.fifed.architecture.datacontroller.interaction.core.ErrorData;
import com.fifed.architecture.datacontroller.interaction.core.Model;

public class ExampleView extends View implements ObserverActivity {
    public ExampleView(Context context) {
        super(context);
    }

    public ExampleView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ExampleView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    @Override
    public boolean onBackPressed() {
        return false;
    }

    @Override
    public void onUpdateData(Model model) {

    }

    @Override
    public void onError(ErrorData errorData) {

    }

    @Override
    public void onPassiveObserveUpdateData(Model model) {

    }

    @Override
    public void onPassiveObserveError(ErrorData errorData) {

    }

    @Override
    public String getObserverTag() {
        return getClass().getSimpleName() + hashCode();
    }

    private void onUserMadeAction(Action action){
        ((ActivityActionInterface) getContext()).userMadeAction(action);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        ((ObservebleActivity) getContext()).registerObserver(this);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        ((ObservebleActivity) getContext()).unregisterObserver(this);
    }
}
