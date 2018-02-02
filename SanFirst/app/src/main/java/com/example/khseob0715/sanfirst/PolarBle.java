package com.example.khseob0715.sanfirst;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.util.Log;

import java.util.StringTokenizer;

// 서비스 클래스를 구현하려면, Service 를 상속받는다
public class PolarBLE extends Service {

    private SharedPreferences prefs;

    PolarBleService mPolarBleService;
    String mpolarBleDeviceAddress="00:22:D0:A4:9D:83";  // 우리꺼
    int batteryLevel=0;
    public static int polarhrvalue = 0;

    //------------------------------
    String mDefaultDeviceAddress;

    @Override
    public IBinder onBind(Intent intent) {
        // Service 객체와 (화면단 Activity 사이에서)
        // 통신(데이터를 주고받을) 때 사용하는 메서드
        // 데이터를 전달할 필요가 없으면 return null;
        return null;
    }
    @Override
    public void onCreate() {
        super.onCreate();
        // 서비스에서 가장 먼저 호출됨(최초에 한번만)
        prefs = getSharedPreferences(HConstants.DEVICE_CONFIG, Context.MODE_MULTI_PROCESS);
        mDefaultDeviceAddress = prefs.getString(HConstants.CONFIG_DEFAULT_DEVICE_ADDRESS, null);

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // 서비스가 호출될 때마다 실행
        activatePolar();
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        // 서비스가 종료될 때 실행
        deactivatePolar();
    }

    protected void activatePolar() {
        Log.w(this.getClass().getName(), "** activatePolar()");

        Intent gattactivatePolarServiceIntent = new Intent(this, PolarBleService.class);
        bindService(gattactivatePolarServiceIntent, mPolarBleServiceConnection, BIND_AUTO_CREATE);
        registerReceiver(mPolarBleUpdateReceiver, makePolarGattUpdateIntentFilter());
    }

    protected void deactivatePolar() {
        Log.w(this.getClass().getName(), "deactivatePolar()");
        try{
            if(mPolarBleService!=null){
                Log.w(this.getClass().getName(), "**** unbindService()");
                unbindService(mPolarBleServiceConnection);
                Log.w(this.getClass().getName(), "bindService()");
            }
        }catch(Exception e){
            Log.e(this.getClass().getName(), e.toString());
        }

        try{
            unregisterReceiver(mPolarBleUpdateReceiver);
        }catch(Exception e){
            Log.e(this.getClass().getName(), e.toString());
        }
    }

    public int getPolarhrvalue() { // 임의 랜덤값을 리턴하는 메서드
        Log.d("BoundService","getRan()");
        return polarhrvalue;
    }

    private final BroadcastReceiver mPolarBleUpdateReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context ctx, Intent intent) {
            //Log.w(TAG, "####BroadcastReceiver Polar BLE Service ");

            final String action = intent.getAction();
            if (PolarBleService.ACTION_GATT_CONNECTED.equals(action)) {
            } else if (PolarBleService.ACTION_GATT_DISCONNECTED.equals(action)) {
                Log.w(this.getClass().getName(), "mPolarBleUpdateReceiver received ACTION_GATT_DISCONNECTED");
            } else if (PolarBleService.ACTION_HR_DATA_AVAILABLE.equals(action)) {

                //heartRate+";"+pnnPercentage+";"+pnnCount+";"+rrThreshold+";"+bioHarnessSessionData.totalNN
                //String data = intent.getStringExtra(BluetoothLeService.EXTRA_DATA);
                String data = intent.getStringExtra(PolarBleService.EXTRA_DATA);
                StringTokenizer tokens = new StringTokenizer(data, ";");
                int hr = Integer.parseInt(tokens.nextToken());
                int prrPercenteage = Integer.parseInt(tokens.nextToken());
                int prrCount = Integer.parseInt(tokens.nextToken());
                int rrThreshold = Integer.parseInt(tokens.nextToken());	//50%, 30%, etc.
                int rrTotal = Integer.parseInt(tokens.nextToken());
                int rrValue = Integer.parseInt(tokens.nextToken());
                long sid = Long.parseLong(tokens.nextToken());

                Log.e(this.getClass().getName(), "MD: Heart rate is " + hr);
                polarhrvalue = hr;

            }else if (PolarBleService.ACTION_BATTERY_DATA_AVAILABLE.equals(action)) {
                //String data = intent.getStringExtra(BluetoothLeService.EXTRA_DATA);
                String data = intent.getStringExtra(PolarBleService.EXTRA_DATA);
                batteryLevel = Integer.parseInt(data);
            }else if (PolarBleService.ACTION_GATT_SERVICES_DISCOVERED.equals(action)) {
                String data = intent.getStringExtra(PolarBleService.EXTRA_DATA);
                //String data = intent.getStringExtra(BluetoothLeService.EXTRA_DATA);
                StringTokenizer tokens = new StringTokenizer(data, ";");
                int totalNN = Integer.parseInt(tokens.nextToken());
                long lSessionId = Long.parseLong(tokens.nextToken());


            }
        }
    };

    private static IntentFilter makePolarGattUpdateIntentFilter() {
        final IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(PolarBleService.ACTION_GATT_CONNECTED);
        intentFilter.addAction(PolarBleService.ACTION_GATT_DISCONNECTED);
        intentFilter.addAction(PolarBleService.ACTION_GATT_SERVICES_DISCOVERED);
        intentFilter.addAction(PolarBleService.ACTION_HR_DATA_AVAILABLE);
        intentFilter.addAction(PolarBleService.ACTION_BATTERY_DATA_AVAILABLE);
        return intentFilter;
    }

    private final ServiceConnection mPolarBleServiceConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName componentName, IBinder service) {
            mPolarBleService = ((PolarBleService.LocalBinder) service).getService();
            if (!mPolarBleService.initialize()) {
                Log.e(this.getClass().getName(), "Unable to initialize Bluetooth");
                try {
                    PolarBLE.this.finalize();
                } catch (Throwable throwable) {
                    throwable.printStackTrace();
                }
            }
            // Automatically connects to the device upon successful start-up initialization.
            //mPolarBleService.connect(app.polarBleDeviceAddress, false);
            mPolarBleService.connect(mpolarBleDeviceAddress, false);
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {

            mPolarBleService = null;
        }
    };
}
