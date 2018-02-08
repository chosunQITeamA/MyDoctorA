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
import android.widget.ImageView;
import android.widget.TextView;

import com.example.khseob0715.sanfirst.UserActivity.UserMainActivity;
import com.example.khseob0715.sanfirst.R;
import com.example.khseob0715.sanfirst.udoo_btchat.DeviceListActivity;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.lylc.widget.circularprogressbar.CircularProgressBar;

import java.util.ArrayList;

import static com.example.khseob0715.sanfirst.R.id;
import static com.example.khseob0715.sanfirst.R.layout.fragment_main;

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
    private int coval, so2val, o3val, no2val, pm25val;
    public int[] airlist = new int[]{coval, so2val, o3val, no2val, pm25val};
    private CircularProgressBar[] airseekbar = new CircularProgressBar[]{coseekbar, so2seekbar, o3seekbar, no2seekbar, pm25seekbar};
    private UserMainActivity mainclass = new UserMainActivity();
    private BluetoothAdapter mBluetoothAdapter = null; /* Intent request codes*/
    private static final int REQUEST_CONNECT_DEVICE_SECURE = 1;
    private static final int REQUEST_CONNECT_DEVICE_INSECURE = 2;
    private static final int REQUEST_ENABLE_BT = 3;


    private LineChart chart;
    private Thread thread;

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
        return inflater.inflate(fragment_main, container, false); // fragment 맞춘 레이아웃 설정
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

        chart = (LineChart) view.findViewById(R.id.chart);

// 차트의 아래 Axis
        XAxis xAxis = chart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM); // xAxis의 위치는 아래쪽
        xAxis.setTextSize(10f); // xAxis에 표출되는 텍스트의 크기는 10f
        xAxis.setDrawGridLines(false); // xAxis의 그리드 라인을 없앰

// 차트의 왼쪽 Axis
        YAxis leftAxis = chart.getAxisLeft();
        leftAxis.setDrawGridLines(false); // leftAxis의 그리드 라인을 없앰

// 차트의 오른쪽 Axis
        YAxis rightAxis = chart.getAxisRight();
        rightAxis.setEnabled(false); // rightAxis를 비활성화 함

        ArrayList<ILineDataSet> lineDataSets = new ArrayList<>();


        //LineData data = new LineData();
        //chart.setData(data); // LineData를 셋팅함
        chart.setData(new LineData(lineDataSets));

        feedMultiple(); // 쓰레드를 활용하여 실시간으로 데이터

        startSubThread();
    }

    private void feedMultiple() {
        if (thread != null)
            thread.interrupt(); // 살아있는 쓰레드에 인터럽트를 검

        final Runnable runnable = new Runnable() {
            @Override
            public void run() {
                addEntry(); // addEntry를 실행하게 함
            }
        };

        thread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    mainclass.runOnUiThread(runnable); // UI 쓰레드에서 위에서 생성한 runnable를 실행함
                    try {
                        Thread.sleep(100); // 0.1초간 쉼
                    } catch (InterruptedException ie) {
                        ie.printStackTrace();
                    }
                }
            }
        });
        thread.start();
    }

    private void addEntry() {
        LineData data = chart.getData(); // onCreate에서 생성한 LineData를 가져온다.

        if (data != null) { // 데이터가 비어있지 않는다.
            ILineDataSet set = data.getDataSetByIndex(0); // 0번째 위치의 데이터셋을 가져온다.

            if (set == null) { // 0번에 위치한 값이 없을 경우.
                set = createSet(); // createSet 한다.
                data.addDataSet(set); // createSet 을 한 set을 데이터셋에 추가함
            }

            // set의 맨 마지막에 랜덤값(30~69.99999)을 Entry로 data에 추가함
            data.addEntry(new Entry(set.getEntryCount(), (float) (Math.random() * 40) + 30f), 0);
            data.notifyDataChanged(); // data의 값 변동을 감지함

            chart.notifyDataSetChanged(); // chart의 값 변동을 감지함
            chart.setVisibleXRangeMaximum(10); // chart에서 최대 X좌표기준으로 몇개의 데이터를 보여줄지 설정함
            chart.moveViewToX(data.getEntryCount()); // 가장 최근에 추가한 데이터의 위치로 chart를 이동함
        }
    }

    private LineDataSet createSet() {
        LineDataSet set = new LineDataSet(null, "Heart-Rate"); // 데이터셋의 이름을 "Dynamic Data"로 설정(기본 데이터는 null)
        set.setAxisDependency(YAxis.AxisDependency.LEFT); // Axis를 YAxis의 LEFT를 기본으로 설정
        set.setColor(ColorTemplate.getHoloBlue()); // 데이터의 라인색을 HoloBlue로 설정
        set.setCircleColor(Color.WHITE); // 데이터의 점을 WHITE로 설정
        set.setLineWidth(2f); // 라인의 두께를 2f로 설정
        set.setCircleRadius(4f); // 데이터 점의 반지름을 4f로 설정
        set.setFillAlpha(65); // 투명도 채우기를 65로 설정
        set.setFillColor(ColorTemplate.getHoloBlue()); // 채우기 색을 HoloBlue로 설정
        set.setHighLightColor(Color.rgb(244, 117, 117)); // 하이라이트 컬러(선택시 색)을 rgb(244, 117, 117)로 설정
        set.setDrawValues(false); // 각 데이터의 값을 텍스트로 나타내지 않게함(false)
        return set; // 이렇게 생성한 set을 반환
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

    /*
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

//    */
//// AQI seekbar
//    public void aqiseekani(int indexlevel) {
//// aqi val에 따라 얼굴 변화 및 색변화
//        if (indexlevel >= 0 && indexlevel <= 50) {
//            aqicon.setImageResource(R.drawable.aqi_1);
//        } else if (indexlevel > 50 && indexlevel <= 100) {
//            aqicon.setImageResource(R.drawable.aqi_2);
//        } else if (indexlevel > 100 && indexlevel <= 150) {
//            aqicon.setImageResource(R.drawable.aqi_3);
//        } else if (indexlevel > 150 && indexlevel <= 200) {
//            aqicon.setImageResource(R.drawable.aqi_4);
//        } else if (indexlevel > 200 && indexlevel <= 300) {
//            aqicon.setImageResource(R.drawable.aqi_5);
//        } else if (indexlevel > 300 && indexlevel <= 500) {
//            aqicon.setImageResource(R.drawable.aqi_6);
//        } else {
//        }
//        aqiseekani(0, indexlevel); // start = 0, end = indexlevel
//    }
//
//    // aqi seekbar
//    public void aqiseekani(int startval, int endval) {
//        aqiseekbar.animateProgressTo(startval, endval, new CircularProgressBar.ProgressAnimationListener() {
//            @Override
//            public void onAnimationStart() {
//            }
//
//            @Override
//            public void onAnimationProgress(int progress) {
//            }
//
//            @Override
//            public void onAnimationFinish() {
//            }
//        });
//    }

    /*
    // each air quality value
    public void aireachval(int index, int value) {
    airseekbar[index].setTitle(String.valueOf(value));
    airseekbar[index].animateProgressTo(0, value, new CircularProgressBar.ProgressAnimationListener() {
    @Override
    public void onAnimationStart() {
    }

    @Override
    public void onAnimationProgress(int progress) {
    }

    @Override
    public void onAnimationFinish() {
    // heartseek.setSubTitle("done");
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
/*
android.os.Handler receivehearthandler = new android.os.Handler() {
public void handleMessage(Message msg) {
if (msg.what == 0) {
hrseekstartval = hrseekendval;
hrseekendval = mainclass.getHeartratevalue();
heartseekani(hrseekstartval, hrseekendval);
heartval.setText(String.valueOf(mainclass.getHeartratevalue()));
/*
for (int i = 0; i <= 4; i++) {
aireachval(i, airlist[i]);
}
*/
/*
}
}
};
*/

    public class MyRunnable implements Runnable {
        @Override
        public void run() {
            while (true) {
                Message msg = Message.obtain();
                msg.what = 0;
// receivehearthandler.sendMessage(msg);
                try {
                    Thread.sleep(1000); // 갱신주기 1초
                } catch (Exception e) {
                }
            }
        }
    }
}

