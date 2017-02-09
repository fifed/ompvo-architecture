package com.fifed.architecture.app.utils;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.ColorInt;
import android.support.annotation.ColorRes;
import android.support.annotation.DrawableRes;
import android.support.annotation.RawRes;
import android.support.v4.content.res.ResourcesCompat;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by Fedir on 13.08.2016.
 */
public class ResourceHelper {
    public static @ColorInt int getColor(@ColorRes int colorID, Context context) {
        return ResourcesCompat.getColor(context.getResources(), colorID, null);
    }
    public static Drawable getResources(@DrawableRes int resID, Context context){
       return ResourcesCompat.getDrawable(context.getResources(), resID, null);
    }

    public static String readRawText(@RawRes int rawTextFileResId, Context context) {
        InputStream inputStream = context.getResources().openRawResource(rawTextFileResId);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        int i;
        try {
            i = inputStream.read();
            while (i != -1) {
                byteArrayOutputStream.write(i);
                i = inputStream.read();
            }
            inputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return byteArrayOutputStream.toString();
    }

}
