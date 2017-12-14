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

    private static IUserSpecialInformer instance;

    static {
        config = new UserSpecialInformerConfig();
    }

    public static void setConfig(UserSpecialInformerConfig informerConfig) {
        config = informerConfig;
    }

    public static void setDefInstance(IUserSpecialInformer instance) {
        UserSpecialInformer.instance = instance;
    }

    public static void showInformationForUser(View view, String text) {
        showInformationForUser(view, text, DEF_COLOR, DEF_COLOR);
    }

    public static void showInformationForUser(View view, String text, @ColorInt int textColor, @ColorInt int backgroundColor) {
        if (instance != null) {
            instance.showInformationForUser(text);
        } else {
            try {
                Snackbar snackbar = Snackbar.make(view, text, Snackbar.LENGTH_SHORT);
                View rootView = snackbar.getView();
                TextView tv = (TextView) rootView.findViewById(R.id.snackbar_text);
                tv.setTextSize(config.getTextSize());
                if (textColor != DEF_COLOR) {
                    tv.setTextColor(textColor);
                } else tv.setTextColor(config.getDefaultTextColor());
                if (backgroundColor != DEF_COLOR) {
                    rootView.setBackgroundColor(backgroundColor);
                } else rootView.setBackgroundColor(config.getDefaultBackgroundColor());
                snackbar.show();
            } catch (NullPointerException e) {
                e.printStackTrace();
            }
        }
    }

    public static void showInfoErrorForUser(View view, String text) {
        showInfoErrorForUser(view, text, DEF_COLOR, DEF_COLOR);
    }

    public static void showInfoErrorForUser(View view, String text, @ColorInt int textColor, @ColorInt int backgroundColor) {
        if (instance != null) {
            instance.showInfoErrorForUser(text);
        } else {
            try {
                Snackbar snackbar = Snackbar.make(view, text, Snackbar.LENGTH_SHORT);
                View rootView = snackbar.getView();
                TextView tv = (TextView) rootView.findViewById(R.id.snackbar_text);
                tv.setTextSize(config.getTextSize());
                if (textColor != DEF_COLOR) {
                    tv.setTextColor(textColor);
                } else tv.setTextColor(config.getErrorTextColor());
                if (backgroundColor != DEF_COLOR) {
                    rootView.setBackgroundColor(backgroundColor);
                } else rootView.setBackgroundColor(config.getErrorBackgroundColor());
                snackbar.show();
            } catch (NullPointerException e) {
                e.printStackTrace();
            }
        }
    }


    public interface IUserSpecialInformer {

        void showInformationForUser(String text);

        void showInfoErrorForUser(String text);
    }
}
