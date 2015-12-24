package com.fing.flowscan.service;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.TrafficStats;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.fing.flowscan.dao.ProcessDao;
import com.fing.flowscan.fragement.ProcessFragment;
import com.fing.flowscan.lisenter.BinderCallback;
import com.fing.flowscan.model.ProcessInfo;
import com.fing.flowscan.utils.LogUtil;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by fing on 2015/12/22.
 * Time 下午 03:47
 */
public class ProcessQueryService extends Service {
    ProcessDao pDao;
    Timer timer;
    String processStyle = "all";
    ProcessQueryBinder binder;
    BinderCallback callback;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        pDao = ProcessDao.getInstance(this);
        binder = new ProcessQueryBinder();
        PackageManager pm = getPackageManager();
        final List<PackageInfo> pList = pm.getInstalledPackages(PackageManager.GET_UNINSTALLED_PACKAGES);
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                String startTime = "";
                String endTime = null;
                Date date = new Date();
                DateFormat format;
                switch (processStyle) {

                    case "all":
                        List<ProcessInfo> pInfos = new ArrayList<>();
                        for (PackageInfo info : pList) {
                            if ((info.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) == 0) {
                                ProcessInfo pInfo = new ProcessInfo();
                                pInfo.setSend(TrafficStats.getUidTxBytes(info.applicationInfo.uid));
                                pInfo.setReceive(TrafficStats.getUidRxBytes(info.applicationInfo.uid));
                                pInfo.setPackageName(info.packageName);
                                pInfos.add(pInfo);
                            }
                        }
                        binder.setProcessInfos(pInfos);
                        if (callback != null)
                            callback.call();
                        return;
                    case "hour":
                        format = new SimpleDateFormat("yyyy-MM-dd HH");
                        startTime = format.format(date);
                        break;
                    case "month":
                        format = new SimpleDateFormat("yyyy-MM");
                        startTime = format.format(date);
                        break;
                    case "today":
                        format = new SimpleDateFormat("yyyy-MM-dd");
                        startTime = format.format(date);
                        break;
                }

                List<ProcessInfo> pInfos = new ArrayList<>();
                for (PackageInfo info : pList) {
                    if ((info.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) == 0) {
                        ProcessInfo pInfo = pDao.query(startTime, null, info.packageName);
                        pInfo.setSend(TrafficStats.getUidTxBytes(info.applicationInfo.uid));
                        pInfo.setReceive(TrafficStats.getUidRxBytes(info.applicationInfo.uid));
                        pInfo.setPackageName(info.packageName);
                        pInfos.add(pInfo);
                    }
                }
                binder.setProcessInfos(pInfos);
                if (callback != null)
                    callback.call();
            }

        }, 0, 1000);
        return binder;
    }

    public void setCallback(BinderCallback callback) {
        this.callback = callback;
    }

    public class ProcessQueryBinder extends Binder {
        List<ProcessInfo> pInfos;

        public ProcessQueryService getService() {
            return ProcessQueryService.this;
        }

        public void setProcessStyle(String processStyle) {
            ProcessQueryService.this.processStyle = processStyle;
        }

        public void setProcessInfos(List<ProcessInfo> infos) {
            this.pInfos = infos;
        }

        public List<ProcessInfo> getProcessInfos() {
            return this.pInfos;
        }
    }

}
