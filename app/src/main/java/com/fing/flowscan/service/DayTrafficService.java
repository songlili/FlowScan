package com.fing.flowscan.service;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.fing.flowscan.dao.TrafficDAO;
import com.fing.flowscan.lisenter.BinderCallback;
import com.fing.flowscan.model.TrafficInfo;
import com.fing.flowscan.utils.FUtil;
import com.fing.flowscan.utils.LogUtil;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Future;

/**
 * Created by fing on 2015/12/13.
 * Time 下午 07:57
 */
public class DayTrafficService extends Service {
    DayTrafficBinder binder;
    String day;
    BinderCallback callback;
    Timer timer;
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        binder = new DayTrafficBinder();
        final TrafficDAO dao = TrafficDAO.getInstance(this);
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                if (day != null) {
                    List<Map<String, String>> list = new ArrayList<>();
                    for (int i = 0; i <= 22; i += 2) {
                        Map<String, String> map = new HashMap<>();
                        map.put("time", i + "时");
                        String str = i < 10 ? " 0" + i : " " + i;
                        TrafficInfo info = dao.queryTrafficInfo(day + str);
                        map.put("mobileSend", FUtil.formatByte(info.getMobileSend()));
                        map.put("mobileReceive", FUtil.formatByte(info.getMobileReceive()));
                        map.put("wifiSend", FUtil.formatByte(info.getWifiSend()));
                        map.put("wifiReceive", FUtil.formatByte(info.getWifiReceive()));
                        list.add(map);
                    }
                    binder.setList(list);
                    callback.call();
                }
            }
        }, 1000, 1000);
        return binder;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        timer.cancel();
    }

    public void setDay(String day) {
        this.day = day;
    }

    public class DayTrafficBinder extends Binder {
        List<Map<String, String>> list;

        public DayTrafficService getService() {
            return DayTrafficService.this;
        }

        public void setList(List<Map<String, String>> list) {
            this.list = list;
        }

        public List<Map<String, String>> getList() {
            return list;
        }
    }

    public void setCallback(BinderCallback callback) {
        this.callback = callback;
    }
}
