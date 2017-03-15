package com.fifed.architecture.app.utils.user_informer;

import android.graphics.Color;
import android.support.annotation.ColorInt;

public class UserSpecialInformerConfig {
    @ColorInt
    private int errorTextColor = Color.WHITE;
    @ColorInt
    private int defaultTextColor = Color.WHITE;
    @ColorInt
    private int errorBackgroundColor = Color.parseColor("#f44336");
    @ColorInt
    private int defaultBackgroundColor = Color.BLACK;

    private float textSize = 18;

    public UserSpecialInformerConfig(int errorTextColor, int defaultTextColor, int errorBackgroundColor, int defaultBackgroundColor, float textSize) {
        this.errorTextColor = errorTextColor;
        this.defaultTextColor = defaultTextColor;
        this.errorBackgroundColor = errorBackgroundColor;
        this.defaultBackgroundColor = defaultBackgroundColor;
        this.textSize = textSize;
    }

    public UserSpecialInformerConfig() {
    }
    @ColorInt
    public int getErrorTextColor() {
        return errorTextColor;
    }

    public void setErrorTextColor(@ColorInt int errorTextColor) {
        this.errorTextColor = errorTextColor;
    }
    @ColorInt
    public int getDefaultTextColor() {
        return defaultTextColor;
    }

    public void setDefaultTextColor(@ColorInt int defaultTextColor) {
        this.defaultTextColor = defaultTextColor;
    }
    @ColorInt
    public int getErrorBackgroundColor() {
        return errorBackgroundColor;
    }

    public void setErrorBackgroundColor(@ColorInt int errorBackgroundColor) {
        this.errorBackgroundColor = errorBackgroundColor;
    }
    @ColorInt
    public int getDefaultBackgroundColor() {
        return defaultBackgroundColor;
    }

    public void setDefaultBackgroundColor(@ColorInt int defaultBackgroundColor) {
        this.defaultBackgroundColor = defaultBackgroundColor;
    }

    public float getTextSize() {
        return textSize;
    }

    public void setTextSize(float textSize) {
        this.textSize = textSize;
    }
}
