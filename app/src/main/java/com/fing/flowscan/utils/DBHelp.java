package com.fing.flowscan.utils;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by fing on 2015/12/11.
 */
public class DBHelp extends SQLiteOpenHelper {
    private static String CREATE_DB = "create table traffic_db(" +
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

    public DBHelp(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_DB);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

}
