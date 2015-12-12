package com.fing.flowscan.utils;

import android.view.View;

/**
 * Created by fing on 2015/12/12.
 */
public class ViewUtil {
    @SuppressWarnings("unchecked")
    public static <T extends View> T $(View view, int resId){
        return (T) view.findViewById(resId);
    };
}
