package com.example.khseob0715.sanfirst.Database;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class SQLiteHelper extends SQLiteOpenHelper {

    public static final String DB_NAME = "TempData";
    public static final int DB_VERSION = 1;
    private static final String TAG = "SQLiteHelper";

    private static SQLiteHelper INSTANCE;
    private static SQLiteDatabase mDb;
    public String aqiCT, hrCT, aqiDT, hrDT;

    public SQLiteHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    public static SQLiteHelper getInstance(Context context) {
        if (INSTANCE == null) {
            INSTANCE = new SQLiteHelper(context.getApplicationContext());
            mDb = INSTANCE.getWritableDatabase();
        }

        return INSTANCE;
    }

    public void open() {
        if (mDb.isOpen() == false) {
            INSTANCE.onOpen(mDb);
        }
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        createTable(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }

    public void createTable(SQLiteDatabase db) {
        aqiCT = "create table aqi_data(no2 FLOAT, so2 FLOAT, o3 FLOAT, co FLOAT, pm FLOAT, temp FLOAT, time DATETIME);";
        hrCT = "create table hr_data(heartRate INTEGER, rr_interval INTEGER, time DATETIME);";
        try {
            db.execSQL(aqiCT);
            //Log.i(TAG, "create AQI table");
        } catch (SQLException sqex) {
        }
        try {
            db.execSQL(hrCT);
            //Log.i(TAG, "create HR table");
        } catch (SQLException sqex) {
        }
    }

    public void dropTable(SQLiteDatabase db) {
        aqiDT = "drop table aqi_data;";
        hrDT = "drop table hr_data;";

        try {
            db.execSQL(aqiDT);
            //Log.i(TAG, "drop AQI table");
        } catch (SQLException sqex) {
        }
        try {
            db.execSQL(hrDT);
            //Log.i(TAG, "drop HR table");
        } catch (SQLException sqex) {
        }
    }
}