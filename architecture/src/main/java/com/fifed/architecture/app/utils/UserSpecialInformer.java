package com.fifed.architecture.app.utils;

import android.graphics.Color;
import android.support.annotation.ColorInt;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.widget.TextView;

import com.fifed.architecture.R;

/**
 * Created by Fedir on 30.07.2016.
 */
public class UserSpecialInformer {
    public static final int DEFAULT_TEXT_COLOR = 0;
    public static final int DEFAULT_BACKGROUND_COLOR = 0;
    public static void showInformationForUser(View view, String text, @ColorInt int textColor, @ColorInt int backgroundColor){
        try {
            Snackbar snackbar = Snackbar.make(view, text, Snackbar.LENGTH_SHORT);
            View rootView = snackbar.getView();
            TextView tv = (TextView) rootView.findViewById(R.id.snackbar_text);
            tv.setTextSize(18);
            if(textColor != DEFAULT_TEXT_COLOR){
                tv.setTextColor(textColor);
            } else tv.setTextColor(Color.WHITE);
            if(backgroundColor != DEFAULT_BACKGROUND_COLOR){
                rootView.setBackgroundColor(backgroundColor);
            } else rootView.setBackgroundColor(Color.BLACK);
            snackbar.show();
        } catch (NullPointerException e){
            e.printStackTrace();
        }
    }
}
