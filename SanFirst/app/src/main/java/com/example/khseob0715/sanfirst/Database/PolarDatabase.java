package com.example.khseob0715.sanfirst.Database;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class PolarDatabase extends Service {

    public int PolarDB_heartratevalue = 0;

    private DBHelper dbHelper;

    public PolarDatabase() {

    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");

    }

    @Override
    public void onCreate() {
        super.onCreate();
        // 초반 디비 생성
        // 1분 핸들러   -> 디비 보내고 드랍 후 생성




    }
}