package com.example.khseob0715.sanfirst.Database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class HeartSQLiteHelper {

    public int dbMode = Context.MODE_PRIVATE;
    public boolean Heartexist = false;

    // Database 생성 및 열기
     //public SQLiteDatabase db;

    // Table 생성
    public  void createTable(SQLiteDatabase db){
        Heartexist = true;
        String Heart_CT = "create table if not exists HEART_HISTORY(usn INTEGER, ts TEXT, lat DOUBLE, lng DOUBLE, heart_rate DOUBLE, rr_rate DOUBLE) ";
        db.execSQL(Heart_CT);
    }

    // Table 삭제
    public  void dropTable(SQLiteDatabase db){
        Heartexist = false;
        String Heart_DT = "drop table if exists HEART_HISTORY;";
        db.execSQL(Heart_DT);
    }

    // Data 추가
    public  void insertData(SQLiteDatabase db, int usn, String ts, double lat, double lng, double heart_rate, double rr_rate){
        if(Heartexist)  {
            String Heart_ID = "insert into HEART_HISTORY values(" + usn +", '" + ts + "',"+lat+","+lng+","+heart_rate+","+rr_rate+");";
            db.execSQL(Heart_ID);
        }
    }

    //--------------------------insert 확인
    public  void selectAll(SQLiteDatabase db){
        String sql = "select * from HEART_HISTORY ;";
        Cursor results = db.rawQuery(sql, null);

        results.moveToFirst();

        while(!results.isAfterLast()){
            int usn = results.getInt(0);
            String ts = results.getString(1);
            Double lat = results.getDouble(2);
            Double lng = results.getDouble(3);
            Double heart_rate = results.getDouble(4);
            Double rr_rate = results.getDouble(5);
            results.moveToNext();
        }
        results.close();
    }

/*
    public  final String DB_NAME = "MyDoctorA";
    public  final int DB_VERSION = 1;
    private  final String TAG = "HeartSQLiteHelper";

    private  HeartSQLiteHelper INSTANCE;
    private  SQLiteDatabase mDb;
    public String Heart_CT, Heart_DT;

    public HeartSQLiteHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    public  HeartSQLiteHelper getInstance(Context context) {
        if (INSTANCE == null) {
            INSTANCE = new HeartSQLiteHelper(context.getApplicationContext());
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
        Heart_CT = "create table HEART_HISTORY(Heart_sn INTEGER, usn INTEGER, ts TEXT, Lat DOUBLE, Lng DOUBLE, Heart_rate DOUBLE, RR_rate DOUBLE);";
        try {
            db.execSQL(Heart_CT);
        } catch (SQLException sqex) {
        }
    }

    public void dropTable(SQLiteDatabase db) {
        Heart_DT = "drop table HEART_HISTORY;";
        try {
            db.execSQL(Heart_DT);
            createTable(db);
        } catch (SQLException sqex) {
        }
    }
    */
}