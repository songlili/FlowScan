package com.fing.flowscan;

import android.app.Application;
import android.content.Context;
import android.content.Intent;

import com.fing.flowscan.dao.TrafficDAO;
import com.fing.flowscan.model.TrafficInfo;
import com.fing.flowscan.utils.DBHelp;
import com.fing.flowscan.utils.LogUtil;

/**
 * Created by fing on 2015/12/12.
 * Time 下午 09:13
 */
public class App extends Application {
    private static Context context;
    @Override
    public void onCreate() {
        super.onCreate();
        context = this.getApplicationContext();
        LogUtil.LOG_PRINT = false;
    }
    public static Context getContext(){
        return context;
    }
}
