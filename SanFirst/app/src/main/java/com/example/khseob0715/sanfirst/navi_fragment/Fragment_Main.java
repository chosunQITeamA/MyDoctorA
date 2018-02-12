/* Copyright (C) 2014 The Android Open Source Project Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0 Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License. */
package com.example.khseob0715.sanfirst.navi_fragment;

import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.example.khseob0715.sanfirst.R;
import com.example.khseob0715.sanfirst.UserActivity.UserActivity;
import com.example.khseob0715.sanfirst.udoo_btchat.DeviceListActivity;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.lylc.widget.circularprogressbar.CircularProgressBar;

import static com.example.khseob0715.sanfirst.R.id;

/**
 * This fragment controls Bluetooth to communicate with other devices.
 */
public class Fragment_Main extends Fragment {
    private static final String TAG = "Fragment_Main"; /*AQI*/
    private CircularProgressBar coseekbar;
    private CircularProgressBar so2seekbar;
    private CircularProgressBar o3seekbar;
    private CircularProgressBar no2seekbar;
    private CircularProgressBar pm25seekbar;

    private int Scoval, Sso2val, So3val, Sno2val, Spm25val;
    public int[] aqistart = {0,0,0,0,0};
    public int[] aqiend = {0,0,0,0,0};
    private CircularProgressBar[] airseekbar = new CircularProgressBar[]{coseekbar, so2seekbar, o3seekbar, no2seekbar, pm25seekbar};

    UserActivity mainclass = new UserActivity();

    private BluetoothAdapter mBluetoothAdapter = null; /* Intent request codes*/

    private static final int REQUEST_CONNECT_DEVICE_SECURE = 1;
    private static final int REQUEST_CONNECT_DEVICE_INSECURE = 2;
    private static final int REQUEST_ENABLE_BT = 3;


    private ViewGroup rootView;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        rootView = (ViewGroup) inflater.inflate(R.layout.fragment_main, container, false);

        return rootView;
    }

    @SuppressLint("ResourceType")
    @Override
    public void onViewCreated(final View view, @Nullable Bundle savedInstanceState) {
        // setContentView 이전에 find를 할 시 NullPointerException이 발생함. 이에 따라, View가 Created 된 이후에 Find

        coseekbar = (CircularProgressBar) view.findViewById(id.coseekbar); // 각 AQI별 값 (12345까지 필요)
        so2seekbar = (CircularProgressBar) view.findViewById(id.so2seekbar);
        o3seekbar = (CircularProgressBar) view.findViewById(id.o3seekbar);
        no2seekbar = (CircularProgressBar) view.findViewById(id.no2seekbar);
        pm25seekbar = (CircularProgressBar) view.findViewById(id.pm25seekbar);

    }

    // aqi seekbar
    public void seekani(int i, int startval, int endval) {
        airseekbar[i].animateProgressTo(startval, endval, new CircularProgressBar.ProgressAnimationListener() {
            @Override
            public void onAnimationStart() {
            }

            @Override
            public void onAnimationProgress(int progress) {
            }

            @Override
            public void onAnimationFinish() {
            }
        });
        aqistart[i] = aqiend[i];
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.bluetooth_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.secure_connect_scan: {
                // Launch the DeviceListActivity to see devices and do scan
                Intent serverIntent = new Intent(getActivity(), DeviceListActivity.class);
                startActivityForResult(serverIntent, REQUEST_CONNECT_DEVICE_SECURE);
                return true;
            }
            case R.id.discoverable: {
                // Ensure this device is discoverable by others
                ensureDiscoverable();
                return true;
            }
        }
        return false;
    }

    private void ensureDiscoverable() {
        if (mBluetoothAdapter.getScanMode() !=
                BluetoothAdapter.SCAN_MODE_CONNECTABLE_DISCOVERABLE) {
            Intent discoverableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
            discoverableIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 300);
            startActivity(discoverableIntent);
        }
    }

    public class MyRunnable implements Runnable {
        @Override
        public void run() {
            while (true) {
                Message msg = Message.obtain();
                msg.what = 0;
                try {
                    Thread.sleep(500); // 갱신주기 1초
                } catch (Exception e) {
                }
            }
        }
    }


    //heartrate threat
    public void startSubThread() {
        //작업스레드 생성(매듭 묶는과정)
        MyRunnable2 aqiRunnable = new MyRunnable2();
        Thread aqiThread = new Thread(aqiRunnable);
        aqiThread.setDaemon(true);
        aqiThread.start();
    }


    android.os.Handler receivehearthandler = new android.os.Handler() {
        public void handleMessage(Message msg) {
            if (msg.what == 0) {
                for(int i=0; i<=4; i++) {
                    aqiend[i] = mainclass.getAQIvalue(i);
                    seekani(i, 0, aqiend[i]);
                }
            }
        }
    };


    public class MyRunnable2 implements Runnable {
        @Override
        public void run() {
            while (true) {
                Message msg = Message.obtain();
                msg.what = 0;
                receivehearthandler.sendMessage(msg);
                try {
                    Thread.sleep(1000); // 갱신주기 1초
                } catch (Exception e) {
                }
            }
        }
    }
}

