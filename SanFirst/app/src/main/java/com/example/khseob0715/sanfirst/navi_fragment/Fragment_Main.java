/*
 * Copyright (C) 2014 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.khseob0715.sanfirst.navi_fragment;

import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
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
import android.widget.ImageView;
import android.widget.TextView;

import com.example.khseob0715.sanfirst.Activity.UserMainActivity;
import com.example.khseob0715.sanfirst.R;
import com.example.khseob0715.sanfirst.udoo_btchat.DeviceListActivity;
import com.lylc.widget.circularprogressbar.CircularProgressBar;

import static com.example.khseob0715.sanfirst.R.id;
import static com.example.khseob0715.sanfirst.R.layout.fragment_main;

/**
 * This fragment controls Bluetooth to communicate with other devices.
 */
public class Fragment_Main extends Fragment {
    private static final String TAG = "Fragment_Main";

    //AQI
    ImageView aqicon;
    public CircularProgressBar aqiseekbar;

    CircularProgressBar coseekbar;
    CircularProgressBar so2seekbar;
    CircularProgressBar o3seekbar;
    CircularProgressBar no2seekbar;
    CircularProgressBar pm25seekbar;

    int coval;
    int so2val;
    int o3val;
    int no2val;
    int pm25val;

    public int[] airlist = new int[]{coval, so2val, o3val, no2val, pm25val};
    CircularProgressBar[] airseekbar = new CircularProgressBar[]{coseekbar, so2seekbar, o3seekbar, no2seekbar, pm25seekbar};

    //Heartrate
    public CircularProgressBar hrseekbar;
    int hrseekstartval = 0;
    int hrseekendval = 0;
    public TextView heartval;
    UserMainActivity mainclass = new UserMainActivity();

    //Temp
    TextView temperval;

    private BluetoothAdapter mBluetoothAdapter = null;
    // Intent request codes
    private static final int REQUEST_CONNECT_DEVICE_SECURE = 1;
    private static final int REQUEST_CONNECT_DEVICE_INSECURE = 2;
    private static final int REQUEST_ENABLE_BT = 3;

    public Fragment_Main() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(fragment_main, container, false);    // fragment 맞춘 레이아웃 설정
    }

    @SuppressLint("ResourceType")
    @Override
    public void onViewCreated(final View view, @Nullable Bundle savedInstanceState) {
        // setContentView 이전에 find를 할 시 NullPointerException이 발생함. 이에 따라, View가 Created 된 이후에 Find

        hrseekbar = (CircularProgressBar) view.findViewById(id.hrseekbar);
        aqiseekbar = (CircularProgressBar) view.findViewById(id.aqiseekbar);

        temperval = (TextView) view.findViewById(id.temperval);

        coseekbar = (CircularProgressBar) view.findViewById(id.coseekbar);    // 각 AQI별 값 (12345까지 필요)
        so2seekbar = (CircularProgressBar) view.findViewById(id.so2seekbar);
        o3seekbar = (CircularProgressBar) view.findViewById(id.o3seekbar);
        no2seekbar = (CircularProgressBar) view.findViewById(id.no2seekbar);
        pm25seekbar = (CircularProgressBar) view.findViewById(id.pm25seekbar);

        heartval = (TextView) view.findViewById(id.receiveheartvalue);
        aqicon = (ImageView) view.findViewById(id.aqi_icon);

        // Handler method (Heartval을 위해서)
        startSubThread();
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
            /*
            case R.id.insecure_connect_scan: {
                // Launch the DeviceListActivity to see devices and do scan
                Intent serverIntent = new Intent(getActivity(), DeviceListActivity.class);
                startActivityForResult(serverIntent, REQUEST_CONNECT_DEVICE_INSECURE);
                return true;
            }
            */
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

    // Heartrate Seekbar animation (startval, endval)
    public void heartseekani(int startval, int endval) {

        hrseekbar.animateProgressTo(startval, endval, new CircularProgressBar.ProgressAnimationListener() {
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

        hrseekstartval = endval;
    }


    // AQI seekbar
    public void aqiseekani(int indexlevel) {
        // aqi val에 따라 얼굴 변화 및 색변화
        if (indexlevel >= 0 && indexlevel <= 50) {
            aqicon.setImageResource(R.drawable.aqi_1);
        } else if (indexlevel > 50 && indexlevel <= 100) {
            aqicon.setImageResource(R.drawable.aqi_2);
        } else if (indexlevel > 100 && indexlevel <= 150) {
            aqicon.setImageResource(R.drawable.aqi_3);
        } else if (indexlevel > 150 && indexlevel <= 200) {
            aqicon.setImageResource(R.drawable.aqi_4);
        } else if (indexlevel > 200 && indexlevel <= 300) {
            aqicon.setImageResource(R.drawable.aqi_5);
        } else if (indexlevel > 300 && indexlevel <= 500) {
            aqicon.setImageResource(R.drawable.aqi_6);
        } else {
        }
        aqiseekani(0, indexlevel);  // start = 0, end = indexlevel
    }

    // aqi seekbar
    public void aqiseekani(int startval, int endval) {
        aqiseekbar.animateProgressTo(startval, endval, new CircularProgressBar.ProgressAnimationListener() {
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
    }
/*
    // each air quality value
    public void aireachval(int index, int value) {
//        airseekbar[index].setTitle(String.valueOf(value));
        airseekbar[index].animateProgressTo(0, value, new CircularProgressBar.ProgressAnimationListener() {
            @Override
            public void onAnimationStart() {
            }

            @Override
            public void onAnimationProgress(int progress) {
            }

            @Override
            public void onAnimationFinish() {
//                heartseek.setSubTitle("done");
            }
        });
    }
*/
    //---------------------------------------------------------------------- heartrate threat
    public void startSubThread() {
        //작업스레드 생성(매듭 묶는과정)
        MyRunnable myRunnable = new MyRunnable();
        Thread heartThread = new Thread(myRunnable);
        heartThread.setDaemon(true);
        heartThread.start();
    }

    android.os.Handler receivehearthandler = new android.os.Handler() {
        public void handleMessage(Message msg) {
            if (msg.what == 0) {
                hrseekstartval = hrseekendval;
                hrseekendval = mainclass.getHeartratevalue();
                heartseekani(hrseekstartval, hrseekendval);
                heartval.setText(String.valueOf(mainclass.getHeartratevalue()));
 //               for (int i = 0; i <= 4; i++) {
 //                   aireachval(i, airlist[i]);
 //               }
            }
        }
    };


    public class MyRunnable implements Runnable {
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