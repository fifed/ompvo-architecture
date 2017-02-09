package com.fifed.architecture.app.utils;

import android.graphics.Rect;
import android.view.View;
import android.view.ViewTreeObserver;

import java.util.HashMap;

public class KeyboardUtils implements ViewTreeObserver.OnGlobalLayoutListener {

    @Override
    public void onGlobalLayout() {
            Rect r = new Rect();
            mRootView.getWindowVisibleDisplayFrame(r);
            int heightDiff = mRootView.getRootView().getHeight() - (r.bottom - r.top);
            float dp = heightDiff / mScreenDensity;
            if (mCallback != null)
                mCallback.onToggleSoftKeyboard(dp > 200);
    }

    public interface SoftKeyboardToggleListener {
        void onToggleSoftKeyboard(boolean isVisible);
    }

    private SoftKeyboardToggleListener mCallback;
    private View mRootView;
    private float mScreenDensity = 1;
    private static HashMap<SoftKeyboardToggleListener, KeyboardUtils> sListenerMap = new HashMap<>();



    public static void addKeyboardToggleListener(View v, SoftKeyboardToggleListener listener) {
        removeKeyboardToggleListener(listener);
        sListenerMap.put(listener, new KeyboardUtils(v, listener));

    }

    public static void removeKeyboardToggleListener(SoftKeyboardToggleListener listener) {
        if(sListenerMap.containsKey(listener)) {
            KeyboardUtils k = sListenerMap.get(listener);
            k.removeListener();
            sListenerMap.remove(listener);
        }
    }

    public static void removeAllKeyboardToggleListeners() {
        for(SoftKeyboardToggleListener l : sListenerMap.keySet())
            sListenerMap.get(l).removeListener();

        sListenerMap.clear();
    }

    private void removeListener() {
        mCallback = null;
        mRootView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
    }

    private KeyboardUtils(View view, SoftKeyboardToggleListener listener) {
        mCallback = listener;
        mRootView =  view.getRootView();
        mRootView.getViewTreeObserver().addOnGlobalLayoutListener(this);
        mScreenDensity = view.getResources().getDisplayMetrics().density;
    }
}