package com.example.khseob0715.sanfirst.Database;

import android.app.Service;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

/**
 * Created by Kim Jin Hyuk on 2018-02-14.
 */

public class DBHelper extends Service {
    SQLiteDatabase db;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    public void createDatabase(String name) {
        db = openOrCreateDatabase(name, MODE_WORLD_WRITEABLE, null);
    }

    public void createTable(String name)    {
        try {
            db.execSQL("create Table "
                    + name
                    + "("
                    + "USN integer, "
                    + "TS text, "
                    + "LAT double, "
                    + "LNG double, "
                    + "Heart_rate double, "
                    + "RR_rate double "
                    + ");");
        }   catch (Exception e) {
            Log.e("createTable", "Exception in createTable", e);
        }
    }

    public void insertData(int USN, String TS, Double LAT, Double LNG, Double Heart_rate, Double RR_rate)  {
        try {
            String data = "insert into "
                    + getPackageName()
                    + "(USN, TS, LAT, LNG, Heart_rate, RR_rate) values ("
                    + USN
                    + ","
                    + "'" + TS + "', "
                    + LAT + ","
                    + LNG + ","
                    + Heart_rate + ","
                    + RR_rate + ");";
            db.execSQL(data);
        }   catch (Exception e) {
            Log.e("insertData", "Exception in insertData", e);
        }
    }

    public void dropTable(String name)  {
        try {
            String drop = "drop table if exists " + name;
            db.execSQL(drop);
        }   catch (Exception e) {
            Log.e("dropTable", "Exception in dropTable", e);
        }
    }
}
