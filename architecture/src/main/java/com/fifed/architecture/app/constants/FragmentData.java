package com.fifed.architecture.app.constants;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.fifed.architecture.app.fragments.transiotions.FragmentViewTransitionsRequest;

/**
 * Created by Fedir on 21.07.2016.
 */
public abstract class FragmentData {
    private Bundle bundle;
    private FragmentViewTransitionsRequest request;

    public void setBundle(Bundle bundle) {
        this.bundle = bundle;
    }

    public Bundle getBundle() {
        return bundle;
    }

    public void setViewTransitionsRequest(FragmentViewTransitionsRequest request) {
        this.request = request;
    }

    @Nullable
    public FragmentViewTransitionsRequest getRequest() {
        return request;
    }

    public abstract Enum getFragmentID();
}
