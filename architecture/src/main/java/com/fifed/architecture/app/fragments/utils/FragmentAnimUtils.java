package com.fifed.architecture.app.fragments.utils;

public class FragmentAnimUtils {
    private static boolean isRevertAnim;
    public static void revertAnim(){
        isRevertAnim = true;
    }
    public static void restoreAnim(){
        isRevertAnim = false;
    }

    public static boolean isRevertAnim() {
        return isRevertAnim;
    }
}