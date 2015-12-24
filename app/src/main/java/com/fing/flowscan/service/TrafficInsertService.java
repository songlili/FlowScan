package com.fing.flowscan.service;

import android.app.Service;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.TrafficStats;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.fing.flowscan.dao.ProcessDao;
import com.fing.flowscan.dao.TrafficDAO;
import com.fing.flowscan.model.ProcessInfo;
import com.fing.flowscan.model.TrafficInfo;

import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by fing on 2015/12/12.
 * Time 下午 09:14
 */
public class TrafficInsertService extends Service {
    TrafficDAO tDao;
    ProcessDao pDao;
    Timer timer;
    PackageManager pm;
    List<PackageInfo> infoList;
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        tDao = TrafficDAO.getInstance(TrafficInsertService.this);
        pDao = ProcessDao.getInstance(TrafficInsertService.this);
        timer = new Timer();
        timer.schedule(timerTask,500,500);
        pm = getPackageManager();
        infoList = pm.getInstalledPackages(PackageManager.GET_UNINSTALLED_PACKAGES);
    }
    TimerTask timerTask = new TimerTask() {
        @Override
        public void run() {
            TrafficInfo info = new TrafficInfo.TrafficInfoBuilder().builder();
            tDao.insert(info);
            for(PackageInfo pInfo : infoList){
                if((pInfo.applicationInfo.flags& ApplicationInfo.FLAG_SYSTEM)==0)
                {
                    int uId = pInfo.applicationInfo.uid;
                    ProcessInfo processInfo = new ProcessInfo();
                    processInfo.setPackageName(pInfo.packageName);
                    long pSend = TrafficStats.getUidTxBytes(uId);
                    long pReceive = TrafficStats.getUidRxBytes(uId);
                    processInfo.setReceive(pReceive);
                    processInfo.setSend(pSend);
                    processInfo.setTime(new Date());
                    pDao.insert(processInfo);
                }
            }
        }
    };

    @Override
    public void onDestroy() {
        super.onDestroy();
        timer.cancel();
    }
}

