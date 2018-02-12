package com.fifed.architecture.app.fragments.transiotions;

import android.os.Build;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Uriy Praizner on 02.12.17.
 */

public class FragmentViewTransitionsRequest {
    private List<View> requests = new ArrayList<>();

    public FragmentViewTransitionsRequest with(View view) {
        requests.add(view);
        return this;
    }

    public List<View> getRequests() {
        return requests;
    }

    public static boolean isLolipop() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP;
    }
}
