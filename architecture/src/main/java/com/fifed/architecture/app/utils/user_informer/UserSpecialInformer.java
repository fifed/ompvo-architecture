package com.fifed.architecture.app.utils.user_informer;

import android.support.annotation.ColorInt;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.widget.TextView;

import com.fifed.architecture.R;

/**
 * Created by Fedir on 30.07.2016.
 */
public class UserSpecialInformer {
    private static UserSpecialInformerConfig config;
    public static final int DEF_COLOR = 0;

    static {
        config = new UserSpecialInformerConfig();
    }

    public static void setConfig(UserSpecialInformerConfig informerConfig){
        config = informerConfig;
    }


    public static void showInformationForUser(View view, String text, @ColorInt int textColor, @ColorInt int backgroundColor){
        try {
            Snackbar snackbar = Snackbar.make(view, text, Snackbar.LENGTH_SHORT);
            View rootView = snackbar.getView();
            TextView tv = (TextView) rootView.findViewById(R.id.snackbar_text);
            tv.setTextSize(config.getTextSize());
            if(textColor != DEF_COLOR){
                tv.setTextColor(textColor);
            } else tv.setTextColor(config.getDefaultTextColor());
            if(backgroundColor != DEF_COLOR){
                rootView.setBackgroundColor(backgroundColor);
            } else rootView.setBackgroundColor(config.getDefaultBackgroundColor());
            snackbar.show();
        } catch (NullPointerException e){
            e.printStackTrace();
        }
    }

    public static void showInfoErrorForUser(View view, String text, @ColorInt int textColor, @ColorInt int backgroundColor){
        try {
            Snackbar snackbar = Snackbar.make(view, text, Snackbar.LENGTH_SHORT);
            View rootView = snackbar.getView();
            TextView tv = (TextView) rootView.findViewById(R.id.snackbar_text);
            tv.setTextSize(config.getTextSize());
            if(textColor != DEF_COLOR){
                tv.setTextColor(textColor);
            } else tv.setTextColor(config.getErrorTextColor());
            if(backgroundColor != DEF_COLOR){
                rootView.setBackgroundColor(backgroundColor);
            } else rootView.setBackgroundColor(config.getErrorBackgroundColor());
            snackbar.show();
        } catch (NullPointerException e){
            e.printStackTrace();
        }
    }
}
