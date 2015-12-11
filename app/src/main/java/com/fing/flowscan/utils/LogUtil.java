package com.fing.flowscan.utils;

import android.util.Log;

/**
 * Created by fingthinking on 15/10/1.
 */
public class LogUtil {
    public static boolean LOG_PRINT = true;

    public static <T> void e(String tag, T msg) {
        if (LOG_PRINT)
            Log.e(tag, "" + msg);
    }

    public static <T> void d(String tag, T msg) {
        if (LOG_PRINT)
            Log.d(tag, "" + msg);
    }

    public static <T> void v(String tag, T msg) {
        if (LOG_PRINT)
            Log.v(tag, "" + msg);
    }

    public static <T> void i(String tag, T msg) {
        if (LOG_PRINT)
            Log.i(tag, "" + msg);
    }

    public static <T> void w(String tag, T msg) {
        if (LOG_PRINT)
            Log.w(tag, "" + msg);
    }
}
