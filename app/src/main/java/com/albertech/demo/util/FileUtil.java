package com.albertech.demo.util;

import com.albertech.demo.MyApplication;

public class FileUtil {

    public static String getExternalCacheDirPath() {
        return MyApplication.getContext().getExternalCacheDir().getAbsolutePath();
    }
}
