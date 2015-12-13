package com.fing.flowscan.utils;

import android.app.Activity;
import android.content.Context;
import android.view.View;

import com.fing.flowscan.model.TrafficInfo;

/**
 * Created by fing on 2015/12/12.
 */
public class FUtil {
    @SuppressWarnings("unchecked")
    public static <T extends View> T $(View view, int resId){
        return (T) view.findViewById(resId);
    };
    public static String formatByte(long byteNum){
        if(byteNum > 1024*1024*1024){
            return String.format("%.2fGB",(double)byteNum/(1024*1024*1024));
        }
        if(byteNum > 1024 * 1024){
            return String.format("%.2fMB",(double)byteNum/(1024*1024));
        }
        if(byteNum > 1024){
            return String.format("%.2fKB",(double)byteNum/1024);
        }
        return String.format("%.2fB",(double)byteNum);
    }
}
