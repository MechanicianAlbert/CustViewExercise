package com.albertech.demo;

import android.content.Context;
import android.support.annotation.ColorInt;
import android.support.annotation.StringRes;


public class ResUtil {

    public static @ColorInt int getColor(int colorRes) {
        return getContext().getResources().getColor(colorRes);
    }

    public static String getString(@StringRes int stringRes) {
        return getContext().getResources().getString(stringRes);
    }


    private static Context getContext() {
        return MyApplication.getContext();
    }

}
