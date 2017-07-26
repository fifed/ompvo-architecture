package com.fifed.architecture.app.windows.core;

import android.app.Activity;
import android.content.Context;
import android.graphics.PixelFormat;
import android.os.Build;
import android.support.annotation.ColorRes;
import android.support.annotation.LayoutRes;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;

import com.fifed.architecture.app.observers.ObservebleActivity;
import com.fifed.architecture.app.observers.ObserverActivity;

import java.lang.ref.WeakReference;

/**
 * Created by Fedir on 27.01.2017.
 */

public abstract class BaseWindow implements ObserverActivity {

    private boolean windowShowing;

    private Activity activity;

    private WindowManager windowManager;

    private WindowManager.LayoutParams windowParams;

    private FrameLayout.LayoutParams rootVeiwParams, decorViewParams, contentViewParams;

    private View rootView, decorView, contentView, statusBarView;

    private int statusBarHeight;

    private @ColorRes int statusBarColorRes;

    public BaseWindow(Activity activity, View contentView, @ColorRes int statusBarColor) {
        this.activity = new WeakReference<>(activity).get();
        this.contentView = contentView;
        this.statusBarColorRes = statusBarColor;
        initContent();
    }

    public BaseWindow(Activity activity, @LayoutRes int contentViewRes, @ColorRes int statusBarColorRes){
        this.activity = new WeakReference<>(activity).get();
        this.contentView = ((LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(contentViewRes, null);
        this.statusBarColorRes = statusBarColorRes;
        initContent();
    }

    public BaseWindow(Activity activity){
        this.activity = activity;
        initContent();
    }

    public BaseWindow showWindow() {
        show();
        return this;
    }

    public void closeWindow() {
        close();
    }

    private void initContent(){
        onCreate();
        initWindowManagerWithParams();
        calculateStatusBarHeight();
        initRootView();
        initStatusBarView();
        initDecorView();
    }

    private void calculateStatusBarHeight(){
        int result = 0;
        int resourceId = activity.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = activity.getResources().getDimensionPixelSize(resourceId);
        }
        statusBarHeight = result;
    }

    private void initWindowManagerWithParams(){
        windowManager = (WindowManager) activity.getSystemService(Context.WINDOW_SERVICE);
        windowParams = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.TYPE_APPLICATION,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSLUCENT
        );
    }

    private void initStatusBarView(){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            windowParams.flags = windowParams.flags | WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS;
            statusBarView = new View(activity);
            if(statusBarColorRes != 0) {
                statusBarView.setBackgroundResource(statusBarColorRes);
            }
            FrameLayout.LayoutParams statusBarParams = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, statusBarHeight);
            ((FrameLayout)rootView).addView(statusBarView, statusBarParams);
        }
    }

    private void initDecorView(){
        decorView = new FrameLayout(activity);
        decorViewParams = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            decorViewParams.topMargin = statusBarHeight;
        }
        addViewToWindow();
        ((FrameLayout)rootView).addView(decorView, decorViewParams);
    }

    private void initRootView(){
        rootView = new FrameLayout(activity);
        rootView.setLayoutParams(rootVeiwParams = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
    }

    private void show(){
        if(!windowShowing) {
            ((ObservebleActivity)activity).registerObserver(this);
            windowManager.addView(rootView, windowParams);
            windowShowing = true;
            onShow();
        }
    }

    private void close(){
        if(windowShowing) {
            ((ObservebleActivity)activity).unregisterObserver(this);
            onHide();
            windowShowing = false;
            windowManager.removeViewImmediate(rootView);
            onDestroy();
        }
    }

    private void addViewToWindow(){
        ((FrameLayout) decorView).addView(contentView);
        contentViewParams = (FrameLayout.LayoutParams) contentView.getLayoutParams();
    }

    protected void setContentViewRes(@LayoutRes int contentViewRes){
        contentView = activity.getLayoutInflater().inflate(contentViewRes, null);
    }

    public View getContentView() {
        return contentView;
    }

    public void setContentView(View contentView) {
        this.contentView = contentView;
    }

    public int getStatusBarColorRes() {
        return statusBarColorRes;
    }

    public void setStatusBarColorRes(int statusBarColorRes) {
        this.statusBarColorRes = statusBarColorRes;
    }

    @Override
    public boolean handleOnBackPressed(){
        if(windowShowing) {
            close();
            return true;
        } else return false;
    }

    @Override
    public String getObserverTag() {
        return getClass().getSimpleName() + hashCode();
    }

    public boolean isWindowShowing() {
        return windowShowing;
    }

    public BaseWindow withWrapContentWindowMode(int gravity, boolean isWidthWrapContent, boolean isHeightWrapContent){
        rootVeiwParams.height = isHeightWrapContent ? ViewGroup.LayoutParams.WRAP_CONTENT : ViewGroup.LayoutParams.MATCH_PARENT;
        rootVeiwParams.width =  isWidthWrapContent ? ViewGroup.LayoutParams.WRAP_CONTENT : ViewGroup.LayoutParams.MATCH_PARENT;
        rootView.setLayoutParams(rootVeiwParams);

        decorViewParams.height = isHeightWrapContent ? ViewGroup.LayoutParams.WRAP_CONTENT : ViewGroup.LayoutParams.MATCH_PARENT;
        decorViewParams.width = isWidthWrapContent ? ViewGroup.LayoutParams.WRAP_CONTENT : ViewGroup.LayoutParams.MATCH_PARENT;
        decorView.setLayoutParams(decorViewParams);

        if(contentView != null){
            contentViewParams.height = isHeightWrapContent ? ViewGroup.LayoutParams.WRAP_CONTENT : ViewGroup.LayoutParams.MATCH_PARENT;
            contentViewParams.width = isWidthWrapContent ? ViewGroup.LayoutParams.WRAP_CONTENT : ViewGroup.LayoutParams.MATCH_PARENT;
            contentView.setLayoutParams(contentViewParams);
        }

        windowParams.height = isHeightWrapContent ? WindowManager.LayoutParams.WRAP_CONTENT : WindowManager.LayoutParams.MATCH_PARENT;
        windowParams.width = isWidthWrapContent ? WindowManager.LayoutParams.WRAP_CONTENT : WindowManager.LayoutParams.MATCH_PARENT;
        windowParams.gravity = gravity;

        if(statusBarView != null){
            statusBarView.setVisibility(View.GONE);
        }
        if(isWindowShowing()){
            windowManager.updateViewLayout(rootView, windowParams);
        }
        return this;
    }


    protected void onCreate(){

    }

    protected void onShow(){

    }

    protected void onHide(){

    }

    protected void onDestroy(){

    }
}
