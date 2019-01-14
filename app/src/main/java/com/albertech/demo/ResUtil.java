package com.albertech.demo;

import android.content.Context;
import android.support.annotation.ColorInt;



public class ResUtil {

    public static @ColorInt int getColor(int colorRes) {
        return getContext().getResources().getColor(colorRes);
    }


    private static Context getContext() {
        return MyApplication.getContext();
    }
}
