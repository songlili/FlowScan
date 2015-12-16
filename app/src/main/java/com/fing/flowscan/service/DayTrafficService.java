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
        LogUtil.e("----------","onCreate");

        return binder;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        final TrafficDAO dao = TrafficDAO.getInstance(this);
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {

                if (day != null) {
                    List<Map<String, Long>> list = new ArrayList<>();
                    for (int i = 0; i <= 22; i += 2) {
                        Map<String, Long> map = new HashMap<>();
                        map.put("time",(long)i);

                        String str1 = i < 10 ? " 0" + i : " " + i;
                        String str2 = (i+1) < 10 ? " 0"+(i+1):" "+(i+1);
                        TrafficInfo info1 = dao.query("time like '" + day+str1 + "%'", null, null, null, null, "1");
                        TrafficInfo info2 = dao.query("time like '" + day+str2 + "%'", null, null, null, "id desc", "1");
                        if(info1 == null && info2 == null){
                            map.put("mobileSend", 0L);
                            map.put("mobileReceive", 0L);
                            map.put("wifiSend", 0L);
                            map.put("wifiReceive", 0L);
                            LogUtil.e("time-----------","runing -----------------1"+"time like '" + day+str1 + "%'"+"=========="+"time like '" + day+str2 + "%'");
                            list.add(map);
                        }else if(info1 == null){
                            LogUtil.e("time-----------","runing -----------------2");
                            info1 = dao.query("time like '" + day+str2 + "%'", null, null, null, null, "1");
                            map.put("mobileSend", info2.getMobileSend()-info1.getMobileSend());
                            map.put("mobileReceive", info2.getMobileReceive()-info1.getMobileReceive());
                            map.put("wifiSend", info2.getWifiSend()-info1.getWifiSend());
                            map.put("wifiReceive", info2.getWifiReceive()-info1.getWifiReceive());
                            list.add(map);
                        }else{
                            LogUtil.e("time-----------","runing -----------------3");
                            info2 = dao.query("time like '" + day+str1 + "%'", null, null, null, "id desc", "1");
                            map.put("mobileSend", info2.getMobileSend()-info1.getMobileSend());
                            map.put("mobileReceive", info2.getMobileReceive()-info1.getMobileReceive());
                            map.put("wifiSend", info2.getWifiSend()-info1.getWifiSend());
                            map.put("wifiReceive", info2.getWifiReceive()-info1.getWifiReceive());
                            list.add(map);
                        }
                    }
                    binder.setList(list);
                    callback.call();
                }
            }
        }, 1000, 1000);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        LogUtil.e("--------","onDestroy");
        timer.cancel();
    }

    public void setDay(String day) {
        this.day = day;
    }


    public class DayTrafficBinder extends Binder {
        List<Map<String, Long>> list;

        public DayTrafficService getService() {
            return DayTrafficService.this;
        }

        public void setList(List<Map<String, Long>> list) {
            this.list = list;
        }

        public List<Map<String, Long>> getList() {
            return list;
        }
    }

    public void setCallback(BinderCallback callback) {
        this.callback = callback;
    }
}
