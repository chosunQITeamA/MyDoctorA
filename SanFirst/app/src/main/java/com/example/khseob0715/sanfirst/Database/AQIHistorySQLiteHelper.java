package com.example.khseob0715.sanfirst.Database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class AQIHistorySQLiteHelper {

    public int dbMode = Context.MODE_PRIVATE;
    public boolean AQIHstatus = false;

    // Database 생성 및 열기
    //public SQLiteDatabase db;

    // Table 생성
    public  void AQIHcreateTable(SQLiteDatabase db){
        AQIHstatus = true;
        String AQIH_CT = "create table if not exists AQIH_HISTORY(usn INTEGER, ts TEXT, lat DOUBLE, lng DOUBLE, co DOUBLE, so2 DOUBLE, no2 DOUBLE, o3  DOUBLE, pm25 DOUBLE, tem DOUBLE);";
        db.execSQL(AQIH_CT);
        Log.e("SQLite-AQIH_CreateTable", AQIH_CT);
    }

    // Table 삭제
    public  void AQIHdropTable(SQLiteDatabase db){
        AQIHstatus = false;
        String AQIH_DT = "drop table if exists AQIH_HISTORY;";
        db.execSQL(AQIH_DT);
        Log.e("SQLite-AQIH_removeTable", AQIH_DT);
    }

    // Data 추가
    public  void AQIHinsertData(SQLiteDatabase db, int usn, String ts, double lat, double lng, double co, double so2, double no2, double o3, double pm25, double temp){
        String AQIH_ID = "insert into AQIH_HISTORY values(" + usn +", '" + ts + "',"+lat+","+lng+","+co+","+so2+","+no2+","+o3+","+pm25+","+temp+");";
        db.execSQL(AQIH_ID);
        Log.e("SQLite", AQIH_ID);
    }

    //--------------------------insert 확인
    public  void AQIHselectAll(SQLiteDatabase db){
        String sql = "select * from AQIH_HISTORY ;";
        Cursor results = db.rawQuery(sql, null);

        results.moveToFirst();

        while(!results.isAfterLast()){
            int usn = results.getInt(0);
            String ts = results.getString(1);
            Double lat = results.getDouble(2);
            Double lng = results.getDouble(3);
            Double co = results.getDouble(4);
            Double so2 = results.getDouble(5);
            Double no2 = results.getDouble(6);
            Double o3 = results.getDouble(7);
            Double pm25 = results.getDouble(8);
            Double temp = results.getDouble(9);
            Log.e("Select All = ", usn +"/"+ ts +"/"+ lat +"/"+ lng +"/"+ co +"/"+so2 +"/"+no2 +"/"+o3+"/"+pm25+"/"+temp);
            results.moveToNext();
        }
        results.close();
    }
}