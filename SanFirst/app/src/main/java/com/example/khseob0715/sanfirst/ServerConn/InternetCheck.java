package com.example.khseob0715.sanfirst.ServerConn;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.IBinder;
import android.support.annotation.Nullable;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Kim Jin Hyuk on 2018-02-17.
 */

public class InternetCheck extends Service {
    // Method to manually check connection status

    private TimerTask m_Task;
    private Timer m_Timer;

    @Override
    public void onCreate() {
        super.onCreate();

        m_Task = new TimerTask() {
            @Override
            public void run() {//실제 기능 구현
                isNetworkConnected();
            }
        };
        m_Timer = new Timer();
        m_Timer.schedule(m_Task, 3000, 1000);   // 원래 period 5000 임
    }

    public void isNetworkConnected() {
        ConnectivityManager manager = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo mobile = manager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        NetworkInfo wifi = manager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);

        if (mobile.isConnected() || wifi.isConnected()) {
        } else  {
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
