package com.example.khseob0715.sanfirst.navi_fragment;


import android.Manifest;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.res.ResourcesCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import com.example.khseob0715.sanfirst.R;
import com.example.khseob0715.sanfirst.ServerConn.ReceiveAQI;
import com.example.khseob0715.sanfirst.ServerConn.ReceiveAQI_ChartData;
import com.example.khseob0715.sanfirst.UserActivity.UserActivity;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.w3c.dom.Text;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static android.location.LocationManager.GPS_PROVIDER;


/**
 * A simple {@link Fragment} subclass.
 */
public class Fragment_AQIHistory extends Fragment implements View.OnClickListener, OnMapReadyCallback {

    private ListView listView;
    private ViewGroup rootView;
    private ProgressBar PM25_Bar,CO_Bar,NO2_Bar,SO2_Bar,O3_Bar;

    private myAdapter AQIadapter;

    private Handler handler;

    // 서버랑 연결 되면 받을 값.
    public static String[] AQI_date_items = new String[50];

    public static int[] PM_Avg = new int[50];
    public static int[] CO_Avg = new int[50];
    public static int[] NO_Avg = new int[50];
    public static int[] SO_Avg = new int[50];
    public static int[] O3_Avg = new int[50];

    public static int[] TP_Avg = new int[50];

    public static LineChart pmChart;
    public static LineChart coChart;
    public static LineChart o3Chart;
    public static LineChart noChart;
    public static LineChart soChart;

    public static LineDataSet AQI_set1 = null;
    public static LineDataSet AQI_set2 = null;
    public static LineDataSet AQI_set3 = null;
    public static LineDataSet AQI_set4 = null;
    public static LineDataSet AQI_set5 = null;

    public static LineData AQI_data1;
    public static LineData AQI_data2;
    public static LineData AQI_data3;
    public static LineData AQI_data4;
    public static LineData AQI_data5;

    private Thread thread;

    private LinearLayout Start_date_layout, End_date_layout;
    private TextView Start_date_text, End_date_text;

    private DatePickerDialog datePickerDialog;

    private Button SearchBtn;

    private ReceiveAQI receiveAQI = new ReceiveAQI();
    private ReceiveAQI_ChartData receiveAQI_chartData = new ReceiveAQI_ChartData();
    public static int Air_response_count = 0;

    private String chart_date_text = "";

    private String pre_dp = "1";

    private TextView pm25_data, co_data, so_data, no_data, o3_data, tp_data;

    private int usn;

    public static int pmValue = 0, coValue = 0, soValue = 0, noValue = 0, o3Value = 0, tpValue = 0;

    GoogleMap map;
    MapView mapView = null;

    //32.882499 / -117.234644
    public static Double lat = 0.00;
    public static Double lon = 0.00;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        rootView = (ViewGroup) inflater.inflate(R.layout.fragment_aqi_history, container, false);

        listView = (ListView) rootView.findViewById(R.id.airlistview);
        AQIadapter = new myAdapter();
        listView.setAdapter(AQIadapter);

        pm25_data = (TextView)rootView.findViewById(R.id.pm25_data);

        co_data = (TextView)rootView.findViewById(R.id.co_data);
        no_data = (TextView)rootView.findViewById(R.id.no2_data);
        so_data = (TextView)rootView.findViewById(R.id.so2_data);
        o3_data = (TextView)rootView.findViewById(R.id.o3_data);
        tp_data = (TextView)rootView.findViewById(R.id.Temp_data);

        usn = UserActivity.getUSN();


        PM25_Bar = (ProgressBar)rootView.findViewById(R.id.PM25_progress);
        PM25_Bar.getProgressDrawable().setColorFilter(Color.GREEN, android.graphics.PorterDuff.Mode.SRC_IN);
        PM25_Bar.setProgress(0);
        PM25_Bar.setMax(300);

        CO_Bar = (ProgressBar)rootView.findViewById(R.id.CO_progress);
        CO_Bar.getProgressDrawable().setColorFilter(Color.YELLOW, android.graphics.PorterDuff.Mode.SRC_IN);
        CO_Bar.setProgress(0);
        CO_Bar.setMax(300);

        NO2_Bar = (ProgressBar)rootView.findViewById(R.id.NO2_progress);
        NO2_Bar.getProgressDrawable().setColorFilter(Color.CYAN, android.graphics.PorterDuff.Mode.SRC_IN);
        NO2_Bar.setProgress(0);
        NO2_Bar.setMax(300);

        SO2_Bar = (ProgressBar)rootView.findViewById(R.id.SO2_progress);
        SO2_Bar.getProgressDrawable().setColorFilter(Color.RED, android.graphics.PorterDuff.Mode.SRC_IN);
        SO2_Bar.setProgress(0);
        SO2_Bar.setMax(300);

        O3_Bar = (ProgressBar)rootView.findViewById(R.id.O3_progress);
        O3_Bar.getProgressDrawable().setColorFilter(Color.RED, android.graphics.PorterDuff.Mode.SRC_IN);
        O3_Bar.setProgress(0);
        O3_Bar.setMax(300);

        Start_date_layout = (LinearLayout)rootView.findViewById(R.id.Start_date_layout);
        Start_date_layout.setOnClickListener(this);
        End_date_layout = (LinearLayout)rootView.findViewById(R.id.End_date_layout);
        End_date_layout.setOnClickListener(this);

        Start_date_text = (TextView)rootView.findViewById(R.id.Start_date);
        End_date_text = (TextView)rootView.findViewById(R.id.End_date);

        SearchBtn = (Button)rootView.findViewById(R.id.SearchBtn);
        SearchBtn.setOnClickListener(this);


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                // Cursor mycursor = (Cursor)listView.getItemAtPosition(position);

                chart_date_text = "" + listView.getItemAtPosition(position);

                Toast.makeText(getContext(), "chart : " + chart_date_text, Toast.LENGTH_SHORT).show();
                //Toast.makeText(getContext(), "chart : " + mycursor.getString(3), Toast.LENGTH_SHORT).show();
                if(!pre_dp.equals(chart_date_text)) {
                    // text data setting
                    pm25_data.setText(String.valueOf(PM_Avg[position]));
                    co_data.setText(String.valueOf((CO_Avg[position])));
                    so_data.setText(String.valueOf((SO_Avg[position])));
                    no_data.setText(String.valueOf((NO_Avg[position])));
                    o3_data.setText(String.valueOf((O3_Avg[position])));
                    tp_data.setText(String.valueOf((TP_Avg[position])));

                    // progressbar data setting
                    PM25_Bar.setProgress((PM_Avg[position]));
                    CO_Bar.setProgress((CO_Avg[position]));
                    O3_Bar.setProgress((O3_Avg[position]));
                    NO2_Bar.setProgress((NO_Avg[position]));
                    SO2_Bar.setProgress((SO_Avg[position]));

                    if(AQI_set1 != null){
                        AQI_set1.clear();
                    }

                    if(AQI_set2 != null){
                        AQI_set2.clear();
                    }

                    if(AQI_set3 != null){
                        AQI_set3.clear();
                    }

                    if(AQI_set4 != null){
                        AQI_set4.clear();
                    }


                    if(AQI_set5 != null){
                        AQI_set5.clear();
                    }

                    receiveAQI_chartData.ReceiveAQI_ChartData_Asycn(usn, chart_date_text, chart_date_text);
                }
                pre_dp = "" + chart_date_text;
            }
        });


        TabHost host = (TabHost)rootView.findViewById(R.id.AirTabhost);
        host.setup();

        TabHost.TabSpec spec = host.newTabSpec("tab1");
        spec.setIndicator("ALL");
        spec.setContent(R.id.tab1);
        host.addTab(spec);

        spec = host.newTabSpec("tab2");
        spec.setIndicator("PM");
        //spec.setIndicator(null, ResourcesCompat.getDrawable(getResources(), R.drawable.bigcloud_pm, null));
        spec.setContent(R.id.tab2);
        host.addTab(spec);

        spec = host.newTabSpec("tab3");
        spec.setIndicator("CO");
        //spec.setIndicator(null, ResourcesCompat.getDrawable(getResources(), R.drawable.bigcloud_co, null));
        spec.setContent(R.id.tab3);
        host.addTab(spec);

        spec = host.newTabSpec("tab4");
        spec.setIndicator("NO2");
        //spec.setIndicator(null, ResourcesCompat.getDrawable(getResources(), R.drawable.bigcloud_no, null));
        spec.setContent(R.id.tab4);
        host.addTab(spec);

        spec = host.newTabSpec("tab5");
        //spec.setIndicator(null, ResourcesCompat.getDrawable(getResources(), R.drawable.bigcloud_o, null));
        spec.setIndicator("O3");
        spec.setContent(R.id.tab5);
        host.addTab(spec);

        spec = host.newTabSpec("tab6");
        spec.setIndicator("SO2");
        spec.setContent(R.id.tab6);
        host.addTab(spec);

        for(int i=0;i<host.getTabWidget().getChildCount();i++){
            TextView tv = (TextView) host.getTabWidget().getChildAt(i).findViewById(android.R.id.title);
            tv.setTextColor(Color.parseColor("#000000"));
        }

        pmChart = (LineChart)rootView.findViewById(R.id.pmChart);
        coChart = (LineChart)rootView.findViewById(R.id.coChart);
        o3Chart = (LineChart)rootView.findViewById(R.id.o3Chart);
        noChart = (LineChart)rootView.findViewById(R.id.noChart);
        soChart = (LineChart)rootView.findViewById(R.id.soChart);

        chart_setting();

        LineData data = new LineData();
        pmChart.setData(data);

        LineData data2 = new LineData();
        coChart.setData(data2);

        LineData data3 = new LineData();
        o3Chart.setData(data3);

        LineData data4 = new LineData();
        noChart.setData(data4);

        LineData data5 = new LineData();
        soChart.setData(data5);

        long now = System.currentTimeMillis();

        Date date = new Date(now);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String getTime = sdf.format(date);

        Start_date_text.setText(getTime);
        End_date_text.setText(getTime);


        handler = new Handler();

        mapView = (MapView) rootView.findViewById(R.id.mapView);
        mapView.getMapAsync(this);

        if (mapView != null) {
            mapView.onCreate(savedInstanceState);
        }

        // feedMultiple(); // 쓰레드를 활용하여 실시간으로 데이터
        // addEntry();
        return rootView;
    }

    private void chart_setting() {
        // 차트의 아래 Axis
        XAxis xAxis = pmChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM); // xAxis의 위치는 아래쪽
        // xAxis.setTextSize(10f); // xAxis에 표출되는 텍스트의 크기는 10f
        xAxis.setDrawGridLines(false); // xAxis의 그리드 라인을 없앰

        // 차트의 왼쪽 Axis
        YAxis leftAxis = pmChart.getAxisLeft();
        leftAxis.setDrawGridLines(false); // leftAxis의 그리드 라인을 없앰

        // 차트의 오른쪽 Axis
        YAxis rightAxis = pmChart.getAxisRight();
        rightAxis.setEnabled(false); // rightAxis를 비활성화 함

        // 차트의 아래 Axis
        XAxis xAxis2 = coChart.getXAxis();
        xAxis2.setPosition(XAxis.XAxisPosition.BOTTOM); // xAxis의 위치는 아래쪽
        // xAxis.setTextSize(10f); // xAxis에 표출되는 텍스트의 크기는 10f
        xAxis2.setDrawGridLines(false); // xAxis의 그리드 라인을 없앰

        // 차트의 왼쪽 Axis
        YAxis leftAxis2 = coChart.getAxisLeft();
        leftAxis2.setDrawGridLines(false); // leftAxis의 그리드 라인을 없앰

        // 차트의 오른쪽 Axis
        YAxis rightAxis2 = coChart.getAxisRight();
        rightAxis2.setEnabled(false); // rightAxis를 비활성화 함

        // 차트의 아래 Axis
        XAxis xAxis3 = o3Chart.getXAxis();
        xAxis3.setPosition(XAxis.XAxisPosition.BOTTOM); // xAxis의 위치는 아래쪽
        // xAxis.setTextSize(10f); // xAxis에 표출되는 텍스트의 크기는 10f
        xAxis3.setDrawGridLines(false); // xAxis의 그리드 라인을 없앰

        // 차트의 왼쪽 Axis
        YAxis leftAxis3 = o3Chart.getAxisLeft();
        leftAxis3.setDrawGridLines(false); // leftAxis의 그리드 라인을 없앰

        // 차트의 오른쪽 Axis
        YAxis rightAxis3 = o3Chart.getAxisRight();
        rightAxis3.setEnabled(false); // rightAxis를 비활성화 함

        // 차트의 아래 Axis
        XAxis xAxis4 = noChart.getXAxis();
        xAxis4.setPosition(XAxis.XAxisPosition.BOTTOM); // xAxis의 위치는 아래쪽
        // xAxis.setTextSize(10f); // xAxis에 표출되는 텍스트의 크기는 10f
        xAxis4.setDrawGridLines(false); // xAxis의 그리드 라인을 없앰

        // 차트의 왼쪽 Axis
        YAxis leftAxis4 = noChart.getAxisLeft();
        leftAxis4.setDrawGridLines(false); // leftAxis의 그리드 라인을 없앰

        // 차트의 오른쪽 Axis
        YAxis rightAxis4 = noChart.getAxisRight();
        rightAxis4.setEnabled(false); // rightAxis를 비활성화 함


        // 차트의 아래 Axis
        XAxis xAxis5 = soChart.getXAxis();
        xAxis5.setPosition(XAxis.XAxisPosition.BOTTOM); // xAxis의 위치는 아래쪽
        // xAxis.setTextSize(10f); // xAxis에 표출되는 텍스트의 크기는 10f
        xAxis5.setDrawGridLines(false); // xAxis의 그리드 라인을 없앰

        // 차트의 왼쪽 Axis
        YAxis leftAxis5 = soChart.getAxisLeft();
        leftAxis5.setDrawGridLines(false); // leftAxis의 그리드 라인을 없앰

        // 차트의 오른쪽 Axis
        YAxis rightAxis5 = soChart.getAxisRight();
        rightAxis5.setEnabled(false); // rightAxis를 비활성화 함
    }

    public static void Airdata_addEntry(int pm, int co, int o3, int no, int so) {
        AQI_data1 = pmChart.getData();
        AQI_data2 = coChart.getData();
        AQI_data3 = o3Chart.getData();
        AQI_data4 = noChart.getData();
        AQI_data5 = soChart.getData();

        AQI_set1 = (LineDataSet) AQI_data1.getDataSetByIndex(0);
        AQI_set2 = (LineDataSet) AQI_data2.getDataSetByIndex(0);
        AQI_set3 = (LineDataSet) AQI_data3.getDataSetByIndex(0);
        AQI_set4 = (LineDataSet) AQI_data4.getDataSetByIndex(0);
        AQI_set5 = (LineDataSet) AQI_data5.getDataSetByIndex(0);

        if (AQI_set1 == null) {
            // creation of null
            AQI_set1 = air_createSet(Color.parseColor("#FFFF7A87"), "PM2.5");
            AQI_data1.addDataSet(AQI_set1);
        }

        if (AQI_set2 == null) {
            AQI_set2 = air_createSet(Color.parseColor("#FFFF7A87"), "CO");
            AQI_data2.addDataSet(AQI_set2);
        }

        if (AQI_set3 == null) {
            AQI_set3 = air_createSet(Color.parseColor("#FFFF7A87"), "NO2");
            AQI_data3.addDataSet(AQI_set3);
        }

        if (AQI_set4 == null) {
            AQI_set4 = air_createSet(Color.parseColor("#FFFF7A87"), "O3");
            AQI_data4.addDataSet(AQI_set4);
        }

        if (AQI_set5 == null) {
            AQI_set5 = air_createSet(Color.parseColor("#FFFF7A87"), "SO2");
            AQI_data5.addDataSet(AQI_set5);
        }

        AQI_data1.addEntry(new Entry(AQI_set1.getEntryCount(), pm), 0);
        AQI_data2.addEntry(new Entry(AQI_set2.getEntryCount(), co), 0);
        AQI_data3.addEntry(new Entry(AQI_set3.getEntryCount(), o3), 0);
        AQI_data4.addEntry(new Entry(AQI_set4.getEntryCount(), no), 0);
        AQI_data5.addEntry(new Entry(AQI_set5.getEntryCount(), so), 0);

        AQI_data1.notifyDataChanged();                                      // data의 값 변동을 감지함
        AQI_data2.notifyDataChanged();
        AQI_data3.notifyDataChanged();                                      // data의 값 변동을 감지함
        AQI_data4.notifyDataChanged();
        AQI_data5.notifyDataChanged();                                      // data의 값 변동을 감지함

        pmChart.notifyDataSetChanged();                                // chart의 값 변동을 감지함
        pmChart.setVisibleXRangeMaximum(10);                           // chart에서 최대 X좌표기준으로 몇개의 데이터를 보여줄지 설정함
        pmChart.moveViewToX(0);                     // 가장 최근에 추가한 데이터의 위치로 chart를 이동함

        coChart.notifyDataSetChanged();                                // chart의 값 변동을 감지함
        coChart.setVisibleXRangeMaximum(10);                           // chart에서 최대 X좌표기준으로 몇개의 데이터를 보여줄지 설정함
        coChart.moveViewToX(0);                     // 가장 최근에 추가한 데이터의 위치로 chart를 이동함

        o3Chart.notifyDataSetChanged();                                // chart의 값 변동을 감지함
        o3Chart.setVisibleXRangeMaximum(10);                           // chart에서 최대 X좌표기준으로 몇개의 데이터를 보여줄지 설정함
        o3Chart.moveViewToX(0);                     // 가장 최근에 추가한 데이터의 위치로 chart를 이동함

        noChart.notifyDataSetChanged();                                // chart의 값 변동을 감지함
        noChart.setVisibleXRangeMaximum(10);                           // chart에서 최대 X좌표기준으로 몇개의 데이터를 보여줄지 설정함
        noChart.moveViewToX(0);                     // 가장 최근에 추가한 데이터의 위치로 chart를 이동함

        soChart.notifyDataSetChanged();                                // chart의 값 변동을 감지함
        soChart.setVisibleXRangeMaximum(10);                           // chart에서 최대 X좌표기준으로 몇개의 데이터를 보여줄지 설정함
        soChart.moveViewToX(0);                     // 가장 최근에 추가한 데이터의 위치로 chart를 이동함
    }

    public static LineDataSet air_createSet(int setColor, String dataName) {
        LineDataSet set = new LineDataSet(null, dataName);            // 데이터셋의 이름을 "Dynamic Data"로 설정(기본 데이터는 null)
        set.setAxisDependency(YAxis.AxisDependency.LEFT);              // Axis를 YAxis의 LEFT를 기본으로 설정
        set.setColor(setColor);                                        // 데이터의 라인색을 HoloBlue로 설정
        set.setCircleColor(setColor);                                  // 데이터의 점을 WHITE로 설정 // 65536
        set.setLineWidth(2f);                                          // 라인의 두께를 2f로 설정
        set.setCircleRadius(4f);                                       // 데이터 점의 반지름을 4f로 설정
        set.setFillAlpha(65);                                          // 투명도 채우기를 65로 설정
        set.setFillColor(ColorTemplate.getHoloBlue());                 // 채우기 색을 HoloBlue로 설정
        set.setHighLightColor(Color.rgb(244, 117, 117));               // 하이라이트 컬러(선택시 색)을 rgb(244, 117, 117)로 설정
        set.setDrawValues(false);                                     // 각 데이터의 값을 텍스트로 나타내지 않게함(false)
        return set;                                                   // 이렇게 생성한 set을 반환
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.Start_date_layout:
                DatePickerDialog.OnDateSetListener callback = new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        String data = year + "-" + (monthOfYear + 1) + "-" + dayOfMonth;
                        SimpleDateFormat startdata = new SimpleDateFormat("yyyy-MM-dd");

                        Date date = null ;
                        try {
                            date = startdata.parse(data);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        Start_date_text.setText(startdata.format(date));
                    }
                };

                datePickerDialog = new DatePickerDialog(view.getContext(), android.R.style.Theme_Material_Light_Dialog_Alert, callback, 2018, 1, 1);
                datePickerDialog.show();
                break;

            case R.id.End_date_layout:
                DatePickerDialog.OnDateSetListener callback2 = new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        String data = year + "-" + (monthOfYear + 1) + "-" + dayOfMonth;
                        SimpleDateFormat startdata = new SimpleDateFormat("yyyy-MM-dd");

                        Date date = null ;
                        try {
                            date = startdata.parse(data);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        End_date_text.setText(startdata.format(date));
                    }
                };

                datePickerDialog = new DatePickerDialog(view.getContext(), android.R.style.Theme_Material_Light_Dialog_Alert, callback2, 2018, 1, 1);
                datePickerDialog.show();
                break;

            case R.id.SearchBtn:
                int usn = UserActivity.getUSN();
                receiveAQI.ReceiveAQI_Asycn(usn,Start_date_text.getText().toString(), End_date_text.getText().toString());
                handler.postDelayed(new Update_list(),1200);
                break;
        }
    }

    private class GPSListener implements LocationListener {

        @Override
        public void onLocationChanged(Location location) {
            lat = location.getLatitude();
            lon = location.getLongitude();
            String msg = "Lat:" + lat + " / Lon:" + lon;
            ShowMyLocaion(lat, lon, map);
        }

        @Override
        public void onStatusChanged(String s, int i, Bundle bundle) {
        }

        @Override
        public void onProviderEnabled(String s) {
            ShowMyLocaion(lat, lon, map);
        }

        @Override
        public void onProviderDisabled(String s) {
        }
    }

    private void ShowMyLocaion(Double lat, Double lon, GoogleMap googleMap) {
        LatLng nowLocation = new LatLng(lat, lon);
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(nowLocation);
        markerOptions.title("now location");
        googleMap.addMarker(markerOptions);
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(nowLocation));
        googleMap.animateCamera(CameraUpdateFactory.zoomTo(15));
    }

    @Override
    public void onStart() {
        super.onStart();
        mapView.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
        mapView.onStop();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        if (thread != null) {
            thread.interrupt();
        }
        mapView.onPause();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mapView.onLowMemory();
    }

    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        StartLocationService();
    }

    private void StartLocationService() {
        Log.e("startLocationService", "startLocationService");
        LocationManager manager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        GPSListener gpsListener = new GPSListener();
        long minTime = 10000;
        float minDistance = 0;
        try {   //GPS 위치 요청
            if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                    && ActivityCompat.checkSelfPermission(getActivity(),
                    Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            manager.requestLocationUpdates(GPS_PROVIDER, minTime, minDistance, (LocationListener) gpsListener);

            // location request with network
            manager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, minTime, minDistance, (LocationListener) gpsListener);

            Location lastLocation = manager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

            if (lastLocation != null) {
                Double latitude = lastLocation.getLatitude();
                Double longitude = lastLocation.getLongitude();

                Toast.makeText(getActivity(), "HR-" + "Lat:" + latitude + " / Lon:" + longitude, Toast.LENGTH_SHORT).show();
            }
        } catch (SecurityException e) {
            e.printStackTrace();
        }
        Toast.makeText(getActivity(), "start Location tracker.", Toast.LENGTH_SHORT).show();
    }

    class myAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return Air_response_count;
        }

        @Override
        public Object getItem(int position) {
            return AQI_date_items[position];
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            HistoricalAir view = new HistoricalAir(rootView.getContext());

            TextView date = (TextView)view.findViewById(R.id.Historical_AQI_date);
            date.setText(AQI_date_items[position]);

            TextView PM = (TextView)view.findViewById(R.id.PM_avg);
            PM.setText(String.valueOf(PM_Avg[position]));
            TextView CO = (TextView)view.findViewById(R.id.CO_avg);
            CO.setText(String.valueOf(CO_Avg[position]));
            TextView NO = (TextView)view.findViewById(R.id.NO2_avg);
            NO.setText(String.valueOf(NO_Avg[position]));
            TextView SO = (TextView)view.findViewById(R.id.SO2_avg);
            SO.setText(String.valueOf(SO_Avg[position]));
            TextView O3 = (TextView)view.findViewById(R.id.O3_avg);
            O3.setText(String.valueOf(O3_Avg[position]));
            TextView TP = (TextView)view.findViewById(R.id.F_avg);
            TP.setText(String.valueOf(TP_Avg[position]));

            return view;
        }
    }

    class HistoricalAir extends LinearLayout {
        TextView Test;

        public HistoricalAir(Context context) {
            super(context);
            init(context);
        }

        public HistoricalAir(Context context, @Nullable AttributeSet attrs) {
            super(context, attrs);
            init(context);
        }

        private void init(Context context) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            inflater.inflate(R.layout.historical_air_list, this);
        }

    }


    private class Update_list implements Runnable{

        @Override
        public void run() {
            AQIadapter.notifyDataSetChanged();
        }
    }



}
