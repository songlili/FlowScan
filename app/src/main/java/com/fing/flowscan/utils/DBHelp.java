package com.fing.flowscan.utils;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by fing on 2015/12/11.
 */
public class DBHelp extends SQLiteOpenHelper {
    private static String CREATE_TRAFFIC_DB = "create table traffic_db(" +
            "id integer primary key autoincrement," +
            "mobileReceive integer," +
            "mobileSend integer," +
            "mRx integer," +
            "mSx integer," +
            "wifiReceive integer," +
            "wifiSend integer," +
            "wRx integer," +
            "wSx integer," +
            "interval integer," +
            "time varchar(20)," +
            "state integer" +
            ");";
    private static String CREATE_PROCESS_DB="create table process_db(" +
            "id integer primary key autoincrement," +
            "receive integer," +
            "send integer," +
            "time varchar(20)," +
            "package_name varchar(20)" +
            ");";

    public DBHelp(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TRAFFIC_DB);
        db.execSQL(CREATE_PROCESS_DB);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

}
