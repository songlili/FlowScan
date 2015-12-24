package com.fing.flowscan.model;

import android.database.Cursor;
import android.net.TrafficStats;

import java.util.Date;

/**
 * Created by fing on 2015/12/11.
 * Time 下午 09:13
 */
public class TrafficInfo {
    private int id;// 主键，自增id
    private long mobileReceive;// mobile接收流量
    private long mobileSend;// mobile发送流量
    private long mRx; // mobile瞬间接收流量
    private long mSx; // mobile瞬间发送流量
    private long wifiReceive;// wifi接收流量
    private long wifiSend;// wifi发送流量
    private long wRx; // wifi瞬间接收流量
    private long wSx; // wifi瞬间发送流量
    private int interval = 10000; // 时间间隔，毫秒计算,默认10秒
    private Date time; // 保存时间，格式yyyy-MM-dd hh:mm:ss
    private int state;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public long getMobileReceive() {
        return mobileReceive;
    }

    public void setMobileReceive(long mobileReceive) {
        this.mobileReceive = mobileReceive;
    }

    public long getMobileSend() {
        return mobileSend;
    }

    public void setMobileSend(long mobileSend) {
        this.mobileSend = mobileSend;
    }

    public long getmRx() {
        return mRx;
    }

    public void setmRx(long mRx) {
        this.mRx = mRx;
    }

    public long getmSx() {
        return mSx;
    }

    public void setmSx(long mSx) {
        this.mSx = mSx;
    }

    public long getWifiReceive() {
        return wifiReceive;
    }

    public void setWifiReceive(long wifiReceive) {
        this.wifiReceive = wifiReceive;
    }

    public long getWifiSend() {
        return wifiSend;
    }

    public void setWifiSend(long wifiSend) {
        this.wifiSend = wifiSend;
    }

    public long getwRx() {
        return wRx;
    }

    public void setwRx(long wRx) {
        this.wRx = wRx;
    }

    public long getwSx() {
        return wSx;
    }

    public void setwSx(long wSx) {
        this.wSx = wSx;
    }

    public int getInterval() {
        return interval;
    }

    public void setInterval(int interval) {
        this.interval = interval;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public static class TrafficInfoBuilder {
        private TrafficInfo info;

        public TrafficInfoBuilder() {
            info = new TrafficInfo();
            long oldSend = TrafficStats.getMobileTxBytes();
            long oldReceive = TrafficStats.getMobileRxBytes();
            long oldTotalSend = TrafficStats.getTotalTxBytes();
            long oldTotalReceive = TrafficStats.getTotalRxBytes();
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            long newSend = TrafficStats.getMobileTxBytes();
            long newReceive = TrafficStats.getMobileRxBytes();
            long newTotalSend = TrafficStats.getTotalTxBytes();
            long newTotalReceive = TrafficStats.getTotalRxBytes();
            long mSx = newSend - oldSend;
            long mRx = newReceive - oldReceive;
            long wSx = (newTotalSend - oldTotalSend - mSx) * 2;
            long wRx = (newTotalReceive - oldTotalReceive - mRx) * 2;
            mRx *= 2;
            mSx *= 2;

            long wifiSend = newTotalSend - newSend;
            long wifiReceive = newTotalReceive - newReceive;

            info.setMobileSend(newSend);
            info.setMobileReceive(newReceive);
            info.setmSx(mSx);
            info.setmRx(mRx);
            info.setWifiSend(wifiSend);
            info.setWifiReceive(wifiReceive);
            info.setwSx(wSx);
            info.setwRx(wRx);
            info.setTime(new Date());
        }

        public TrafficInfo builder() {
            return info;
        }
    }

}
