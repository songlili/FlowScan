package com.fing.flowscan.service;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.fing.flowscan.lisenter.BinderCallback;
import com.fing.flowscan.dao.TrafficDAO;
import com.fing.flowscan.model.TrafficInfo;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by fing on 2015/12/12.
 * Time 上午 10:50
 */
public class TrafficQueryService extends Service {
    TrafficBinder binder;
    TrafficDAO dao;
    Timer timer;
    BinderCallback callback;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        binder = new TrafficBinder();
        return binder;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        dao = TrafficDAO.getInstance(TrafficQueryService.this);
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                TrafficInfo nowInfo = dao.query(null, null, null, null, "id desc", "1");
                binder.setNowInfo(nowInfo);
                Date date = new Date();
                DateFormat hourFormat = new SimpleDateFormat("yyyy-MM-dd HH", Locale.CANADA);
                TrafficInfo hourInfo = dao.queryTrafficInfo(hourFormat.format(date));
                binder.setHourInfo(hourInfo);
                DateFormat dayFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.CANADA);
                TrafficInfo dayInfo = dao.queryTrafficInfo(dayFormat.format(date));
                binder.setDayInfo(dayInfo);
                if (callback != null)
                    callback.call();
            }
        }, 1000, 1000);

    }

    public BinderCallback getCallback() {
        return callback;
    }

    public void setCallback(BinderCallback callback) {
        this.callback = callback;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        timer.cancel();
    }

    public class TrafficBinder extends Binder {
        TrafficInfo nowInfo, hourInfo, dayInfo;

        public TrafficQueryService getService() {
            return TrafficQueryService.this;
        }

        public TrafficInfo getNowInfo() {
            return nowInfo;
        }

        public void setNowInfo(TrafficInfo nowInfo) {
            this.nowInfo = nowInfo;
        }

        public TrafficInfo getHourInfo() {
            return hourInfo;
        }

        public void setHourInfo(TrafficInfo hourInfo) {
            this.hourInfo = hourInfo;
        }

        public TrafficInfo getDayInfo() {
            return dayInfo;
        }

        public void setDayInfo(TrafficInfo dayInfo) {
            this.dayInfo = dayInfo;
        }
    }
}
