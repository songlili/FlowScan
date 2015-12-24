package com.fing.flowscan.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.fing.flowscan.model.TrafficInfo;
import com.fing.flowscan.utils.DBHelp;
import com.fing.flowscan.utils.LogUtil;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Created by fing on 2015/12/11.
 * Time 下午 09:14
 */
public class TrafficDAO {
    public static final String DB_NAME = "traffic.db";
    public static final String TABLE_NAME = "traffic_db";
    public static final int VERSION = 1;
    private SQLiteDatabase db;
    private static TrafficDAO instance;

    private TrafficDAO(Context context) {
        DBHelp dbHelp = new DBHelp(context, DB_NAME, null, VERSION);
        db = dbHelp.getWritableDatabase();
    }

    public static TrafficDAO getInstance(Context context) {
        if (instance == null) {
            synchronized (TrafficDAO.class) {
                if (instance == null) {
                    instance = new TrafficDAO(context);
                }
            }
        }
        return instance;
    }

    public void insert(TrafficInfo info) {
        try {
            db.beginTransaction();
            ContentValues values = new ContentValues();
            values.put("mobileReceive", info.getMobileReceive());
            values.put("mobileSend", info.getMobileSend());
            values.put("mRx", info.getmRx());
            values.put("mSx", info.getmSx());
            values.put("wifiReceive", info.getWifiReceive());
            values.put("wifiSend", info.getWifiSend());
            values.put("wRx", info.getwRx());
            values.put("wSx", info.getwSx());
            values.put("interval", info.getInterval());
            DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CANADA);
            values.put("time", format.format(info.getTime()));
            values.put("state", info.getState());
            db.insert(TABLE_NAME, null, values);
            db.setTransactionSuccessful();
        } catch (Exception e) {
            LogUtil.e("insert", e.getMessage());
        } finally {
            db.endTransaction();
        }
    }

    public void delete(int id) {
        try {
            db.beginTransaction();
            db.delete(TABLE_NAME, "id=" + id, null);
            db.setTransactionSuccessful();
        } catch (Exception e) {
            LogUtil.e("delete", e.getMessage());
        } finally {
            db.endTransaction();
        }
    }
    public TrafficInfo query(String section, String[] sectionArgs, String groupBy, String having, String
            orderBy, String limit) {
        List<TrafficInfo> list = queryList(section,sectionArgs,groupBy,having,orderBy,limit);
        if(list.size()>0)
            return list.get(0);
        return null;
    }
    public List<TrafficInfo> queryList(String section, String[] sectionArgs, String groupBy, String having, String
            orderBy, String limit) {
        List<TrafficInfo> infoList = new ArrayList<>();
        try {
            db.beginTransaction();
            Cursor cursor = db.query(TABLE_NAME, null, section, sectionArgs, groupBy, having, orderBy, limit);
            while (cursor.moveToNext()) {
                TrafficInfo info = new TrafficInfo();
                info.setId(cursor.getInt(cursor.getColumnIndex("id")));
                info.setMobileReceive(cursor.getLong(cursor.getColumnIndex("mobileReceive")));
                info.setMobileSend(cursor.getLong(cursor.getColumnIndex("mobileSend")));
                info.setmRx(cursor.getLong(cursor.getColumnIndex("mRx")));
                info.setmSx(cursor.getLong(cursor.getColumnIndex("mSx")));
                info.setWifiReceive(cursor.getLong(cursor.getColumnIndex("wifiReceive")));
                info.setWifiSend(cursor.getLong(cursor.getColumnIndex("wifiSend")));
                info.setwRx(cursor.getLong(cursor.getColumnIndex("wRx")));
                info.setwSx(cursor.getLong(cursor.getColumnIndex("wSx")));
                info.setInterval(cursor.getInt(cursor.getColumnIndex("interval")));
                info.setState(cursor.getInt(cursor.getColumnIndex("state")));
                DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CANADA);
                info.setTime(format.parse(cursor.getString(cursor.getColumnIndex("time"))));
                infoList.add(info);
            }
            db.setTransactionSuccessful();
        } catch (Exception e) {
            LogUtil.e("delete", e.getMessage());
        } finally {
            db.endTransaction();
        }
        return infoList;
    }

    /**
     * 提供按照时间格式来生成Info的方法
     *
     * @param timeFormat
     * @return
     */
    public TrafficInfo queryTrafficInfo(String timeFormat) {
        Cursor cursor = db.query(TABLE_NAME,  new String[]{"sum(mRx)", "sum(mSx)", "sum(wRx)", "sum(wSx)"},"time like '" + timeFormat + "%'", null, null, null, null, null);
        if (cursor.moveToNext()) {
            TrafficInfo info = new TrafficInfo();
            info.setMobileReceive(cursor.getLong(0));
            info.setMobileSend(cursor.getLong(1));
            info.setWifiReceive(cursor.getLong(2));
            info.setWifiSend(cursor.getLong(3));
            return info;
        }
        cursor.close();
        return null;
    }
    /**
     * 提供按照时间格式来生成Info的方法
     *
     * @return
     */
    public TrafficInfo queryBetweenTime(String startTime, String endTime) {
        Cursor cursor = db.query(TABLE_NAME, new String[]{"sum(mRx)", "sum(mSx)", "sum(wRx)", "sum(wSx)"},"time >= ? and time <= ?", new String[]{startTime,endTime}, null, null, null, null);
        if (cursor.moveToNext()) {
            TrafficInfo info = new TrafficInfo();
            info.setMobileReceive(cursor.getLong(0));
            info.setMobileSend(cursor.getLong(1));
            info.setWifiReceive(cursor.getLong(2));
            info.setWifiSend(cursor.getLong(3));
            return info;
        }
        cursor.close();
        return null;
    }
}
