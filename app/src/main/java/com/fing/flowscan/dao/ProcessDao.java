package com.fing.flowscan.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.fing.flowscan.model.ProcessInfo;
import com.fing.flowscan.model.TrafficInfo;
import com.fing.flowscan.utils.DBHelp;
import com.fing.flowscan.utils.LogUtil;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;

/**
 * Created by fing on 2015/12/22.
 * Time 下午 03:10
 */
public class ProcessDao {
    public static final String DB_NAME = "traffic.db";
    public static final String TABLE_NAME = "process_db";
    public static final int VERSION = 1;
    private SQLiteDatabase db;
    private static ProcessDao instance;

    private ProcessDao(Context context) {
        DBHelp dbHelp = new DBHelp(context, DB_NAME, null, VERSION);
        db = dbHelp.getWritableDatabase();
    }

    public static ProcessDao getInstance(Context context) {
        if (instance == null) {
            synchronized (TrafficDAO.class) {
                if (instance == null) {
                    instance = new ProcessDao(context);
                }
            }
        }
        return instance;
    }

    public void insert(ProcessInfo info){
        try {
            db.beginTransaction();
            ContentValues values = new ContentValues();
            values.put("receive", info.getReceive());
            values.put("send", info.getSend());
            DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CANADA);
            values.put("time", format.format(info.getTime()));
            values.put("package_name", info.getPackageName());
            db.insert(TABLE_NAME, null, values);
            db.setTransactionSuccessful();
        } catch (Exception e) {
            LogUtil.e("insert", e.getMessage());
        } finally {
            db.endTransaction();
        }
    }
    public ProcessInfo query(String startTime,String endTime,String packageName){
        Cursor cursor;
        if(endTime == null){
            cursor = db.query(TABLE_NAME, new String[]{"send", "receive", "package_name"}, "time >= ? and package_name = ?", new String[]{startTime, packageName}, null, null, null, null);
        }else {
           cursor = db.query(TABLE_NAME, new String[]{"send", "receive", "package_name"}, "time >= ? and time <= ? and package_name = ?", new String[]{startTime, endTime, packageName}, null, null, null, null);
        }
        if(cursor.getCount()>0){
            cursor.moveToFirst();
            ProcessInfo firstInfo = new ProcessInfo();
            firstInfo.setSend(cursor.getLong(0));
            firstInfo.setReceive(cursor.getLong(1));
            cursor.moveToLast();
            ProcessInfo lastInfo = new ProcessInfo();
            lastInfo.setSend(cursor.getLong(0));
            lastInfo.setReceive(cursor.getLong(1));
            ProcessInfo info = new ProcessInfo();
            info.setSend(lastInfo.getSend()-firstInfo.getSend());
            info.setReceive(lastInfo.getReceive()-firstInfo.getReceive());
            info.setPackageName(cursor.getString(2));
            return info;
        }
        cursor.close();
        return null;
    }
}
