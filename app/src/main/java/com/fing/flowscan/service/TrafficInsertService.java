package com.fing.flowscan.service;

import android.app.Service;
import android.content.Intent;
import android.net.TrafficStats;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.fing.flowscan.dao.TrafficDAO;
import com.fing.flowscan.model.TrafficInfo;

import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by fing on 2015/12/12.
 * Time 下午 09:14
 */
public class TrafficInsertService extends Service {
    TrafficDAO dao;
    Timer timer;
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        dao = TrafficDAO.getInstance(TrafficInsertService.this);
        timer = new Timer();
        timer.schedule(timerTask,500,500);
    }
    TimerTask timerTask = new TimerTask() {
        @Override
        public void run() {
            TrafficInfo info = new TrafficInfo.TrafficInfoBuilder().builder();
            dao.insert(info);
        }
    };

    @Override
    public void onDestroy() {
        super.onDestroy();
        timer.cancel();
    }
}

