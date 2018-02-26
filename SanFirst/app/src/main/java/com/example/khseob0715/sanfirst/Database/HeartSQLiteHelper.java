package com.example.khseob0715.sanfirst.Database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

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
}