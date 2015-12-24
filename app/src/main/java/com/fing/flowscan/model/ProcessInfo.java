package com.fing.flowscan.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by fing on 2015/12/22.
 * Time 下午 03:10
 */
public class ProcessInfo{
    private int id;
    private String packageName;
    private long send;
    private long receive;
    private Date time=new Date();



    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public long getSend() {
        return send;
    }

    public void setSend(long send) {
        this.send = send;
    }

    public long getReceive() {
        return receive;
    }

    public void setReceive(long receive) {
        this.receive = receive;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

}
