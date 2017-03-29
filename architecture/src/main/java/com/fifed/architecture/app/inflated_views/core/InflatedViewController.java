package com.fifed.architecture.app.inflated_views.core;

import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.support.annotation.IdRes;
import android.support.annotation.LayoutRes;
import android.view.View;
import android.view.ViewGroup;

import com.fifed.architecture.app.activities.core.BaseActivity;
import com.fifed.architecture.app.observers.ObserverActivity;

import java.lang.ref.WeakReference;

/**
 * Created by Fedir on 12.02.2017.
 */

public abstract class InflatedViewController implements ObserverActivity, View.OnAttachStateChangeListener {
    private View inflatedView;
    private BaseActivity activity;
    private @IdRes int containerID;

    public InflatedViewController(BaseActivity activity) {
        this.activity = new WeakReference<>(activity).get();
        initView();
    }

    public InflatedViewController(BaseActivity activity, @IdRes int containerID) {
        this.activity = new WeakReference<>(activity).get();
        this.containerID = containerID;
        initView();
    }


    public View getView() {
        return inflatedView;
    }

    public BaseActivity getActivity(){
        return activity;
    }

    public void destroyView(){
        inflatedView = null;
    }

    private void setListeners(){
        inflatedView.addOnAttachStateChangeListener(this);
    }

    private void addViewToContainer(){
        inflatedView.post(new Runnable() {
            @Override
            public void run() {
                ViewGroup container = (ViewGroup) activity.findViewById(containerID);
                if(container != null) {
                    container.addView(inflatedView);
                }
            }
        });
    }

    private void initView(){
        try {
            ViewDataBinding vb = DataBindingUtil.inflate(activity.getLayoutInflater(), getLayoutResource(), null, false);
            onBindingFinish(vb);
            inflatedView  = vb.getRoot();
        } catch (NoClassDefFoundError e) {
            inflatedView = activity.getLayoutInflater().inflate(getLayoutResource(), null);
        }
        setListeners();
        onCreatedView(inflatedView);
        if (containerID != 0) {
            addViewToContainer();
        }
    }

    protected void onBindingFinish(ViewDataBinding vb){}

    protected  void onCreatedView(View v){}
    protected abstract @LayoutRes int getLayoutResource();


    @Override
    public void onViewAttachedToWindow(View v) {
        activity.registerObserver(this);
    }

    @Override
    public void onViewDetachedFromWindow(View v) {
        activity.unregisterObserver(this);
    }

    @Override
    public String getObserverTag() {
        return getClass().getSimpleName() + hashCode();
    }
}
