package com.example.khseob0715.sanfirst.navi_fragment;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.res.ResourcesCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TabHost;
import android.widget.TextView;

import com.example.khseob0715.sanfirst.R;
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
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.lylc.widget.circularprogressbar.CircularProgressBar;

import static android.location.LocationManager.GPS_PROVIDER;

public class Fragment_TabMain extends Fragment implements View.OnClickListener, OnMapReadyCallback {

    private String cloudimage = "aqicloud1";

    private CircularProgressBar co_seekbar, so2_seekbar, o3_seekbar, no2_seekbar, pm25_seekbar;
    private CircularProgressBar heart_seekbar;
    public static int heart_rate_value = 50, rr_rate_value = 00;
    private int ConcenVal_Start[] = {20, 10, 40, 30, 20};
    public static int ConcenVal[] = {30, 20, 50, 10, 50, 20};
    private int heart_start = 0;

    private LineChart mChart;
    private LineChart mChart2;
    private Thread thread;

    private ViewGroup rootView;

    private LinearLayout PMLayout, COLayout, O3Layout, NO2Layout, SO2Layout;
    private ImageView PM_Cloud, CO_Cloud, O3_Cloud, NO2_Cloud, SO2_Cloud, Heart;
    private Button so2Btn, no2Btn, o3Btn, pmBtn, coBtn;
    private Drawable alphaPM, alphaCO, alphaO3, alphaNO2, alphaSO2;

    private TextView HeartRateText, tempText;

    UserActivity mainclass = new UserActivity();

    GoogleMap map;
    MapView mapView = null;

    //32.882499 / -117.234644
    public static Double lat = 32.882499;
    public static Double lon = -117.234644;

    private TextView CC_Value_SO2, CC_Value_NO2, CC_Value_O3, CC_Value_PM, CC_Value_CO;
    private TextView SeekValue_SO2,SeekValue_NO2, SeekValue_O3, SeekValue_PM, SeekValue_CO;


    private BluetoothAdapter mBluetoothAdapter = null; /* Intent request codes*/

    private static final int REQUEST_CONNECT_DEVICE_SECURE = 1;
    private static final int REQUEST_CONNECT_DEVICE_INSECURE = 2;
    private static final int REQUEST_ENABLE_BT = 3;

    public static Fragment_TabMain TabMainContext;


    private Thread aqiThread;
    public Fragment_TabMain() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = (ViewGroup) inflater.inflate(R.layout.activity_tab_main, container, false);
        TabHost host = (TabHost) rootView.findViewById(R.id.host);
        host.setup();

        TabHost.TabSpec spec = host.newTabSpec("tab1");
        spec.setIndicator(null, ResourcesCompat.getDrawable(getResources(), R.drawable.airtext, null));
        spec.setContent(R.id.tab_content1);
        host.addTab(spec);

        spec = host.newTabSpec("tab2");
        spec.setIndicator(null, ResourcesCompat.getDrawable(getResources(), R.drawable.hearttext, null));
        spec.setContent(R.id.tab_content2);
        host.addTab(spec);

        setHasOptionsMenu(true);
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        TabMainContext = this;

        return rootView;
    }

    public void onViewCreated(final View view, @Nullable Bundle savedInstanceState) {

        co_seekbar = (CircularProgressBar) view.findViewById(R.id.coseekbar); // 각 AQI별 값 (12345까지 필요)
        so2_seekbar = (CircularProgressBar) view.findViewById(R.id.so2seekbar);
        o3_seekbar = (CircularProgressBar) view.findViewById(R.id.o3seekbar);
        no2_seekbar = (CircularProgressBar) view.findViewById(R.id.no2seekbar);
        pm25_seekbar = (CircularProgressBar) view.findViewById(R.id.pm25seekbar);
        heart_seekbar = (CircularProgressBar) view.findViewById(R.id.heartseekbar);

        PMLayout = (LinearLayout) view.findViewById(R.id.PMLayout);
        COLayout = (LinearLayout) view.findViewById(R.id.COLayout);
        O3Layout = (LinearLayout) view.findViewById(R.id.O3Layout);
        SO2Layout = (LinearLayout) view.findViewById(R.id.SO2Layout);
        NO2Layout = (LinearLayout) view.findViewById(R.id.NO2Layout);

        pmBtn = (Button) view.findViewById(R.id.PMBtn);
        coBtn = (Button) view.findViewById(R.id.COBtn);
        o3Btn = (Button) view.findViewById(R.id.O3Btn);
        so2Btn = (Button) view.findViewById(R.id.SO2Btn);
        no2Btn = (Button) view.findViewById(R.id.NO2Btn);

        visible_layout(View.VISIBLE, View.GONE, View.GONE, View.GONE, View.GONE);

        PM_Cloud = (ImageView) view.findViewById(R.id.PM_Cloud);
        PM_Cloud.setOnClickListener(this);
        CO_Cloud = (ImageView) view.findViewById(R.id.CO_Cloud);
        CO_Cloud.setOnClickListener(this);
        O3_Cloud = (ImageView) view.findViewById(R.id.O_Cloud);
        O3_Cloud.setOnClickListener(this);
        NO2_Cloud = (ImageView) view.findViewById(R.id.NO_Cloud);
        NO2_Cloud.setOnClickListener(this);
        SO2_Cloud = (ImageView) view.findViewById(R.id.SO_Cloud);
        SO2_Cloud.setOnClickListener(this);

        alphaPM = ((ImageView) view.findViewById(R.id.PM_Cloud)).getDrawable();

        alphaCO = ((ImageView) view.findViewById(R.id.CO_Cloud)).getDrawable();
        alphaCO.setAlpha(50);

        alphaNO2 = ((ImageView) view.findViewById(R.id.NO_Cloud)).getDrawable();
        alphaNO2.setAlpha(50);

        alphaO3 = ((ImageView) view.findViewById(R.id.O_Cloud)).getDrawable();
        alphaO3.setAlpha(50);

        alphaSO2 = ((ImageView) view.findViewById(R.id.SO_Cloud)).getDrawable();
        alphaSO2.setAlpha(50);

        mChart = (LineChart) view.findViewById(R.id.chart);
        mChart2 = (LineChart) view.findViewById(R.id.chart2);

        Heart = (ImageView) view.findViewById(R.id.smallheart);
        tempText = (TextView) view.findViewById(R.id.tabMain_temp);
        HeartRateText = (TextView) view.findViewById(R.id.DataValueHeart);

        CC_Value_CO = (TextView) view.findViewById(R.id.concentrationValueCO);
        CC_Value_NO2 = (TextView) view.findViewById(R.id.concentrationValueNO2);
        CC_Value_SO2 = (TextView) view.findViewById(R.id.concentrationValueSO2);
        CC_Value_O3 = (TextView) view.findViewById(R.id.concentrationValueO3);
        CC_Value_PM = (TextView) view.findViewById(R.id.concentrationValuePM);

        SeekValue_CO = (TextView)view.findViewById(R.id.DataValueCO);
        SeekValue_NO2 = (TextView)view.findViewById(R.id.DataValueNO2);
        SeekValue_SO2 = (TextView)view.findViewById(R.id.DataValueSO2);
        SeekValue_O3 = (TextView)view.findViewById(R.id.DataValueO3);
        SeekValue_PM = (TextView)view.findViewById(R.id.DataValuePM);

        Animation animation = AnimationUtils.loadAnimation(getContext(), R.anim.wave);
        animation.setRepeatCount(Animation.INFINITE);
        Heart.startAnimation(animation);

        chart_setting();

        LineData data = new LineData();
        mChart.setData(data); // LineData를 셋팅함

        LineData data2 = new LineData();
        mChart2.setData(data2);

        feedMultiple(); // 쓰레드를 활용하여 실시간으로 데이터

        mapView = (MapView) view.findViewById(R.id.mapView);
        mapView.getMapAsync(this);

        startSubThread();

        if (mapView != null) {
            mapView.onCreate(savedInstanceState);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.PM_Cloud:
                ConcenVal_Start[4] = 0;
                alpha_setting(255, 50, 50, 50, 50);
                visible_layout(View.VISIBLE, View.GONE, View.GONE, View.GONE, View.GONE);
                cloubChange(Integer.valueOf(CC_Value_PM.getText().toString()));
                break;
            case R.id.CO_Cloud:
                ConcenVal_Start[2] = 0;
                alpha_setting(50, 255, 50, 50, 50);
                visible_layout(View.GONE, View.VISIBLE, View.GONE, View.GONE, View.GONE);
                cloubChange(Integer.valueOf(CC_Value_CO.getText().toString()));
                break;
            case R.id.NO_Cloud:
                ConcenVal_Start[1] = 0;
                alpha_setting(50, 50, 255, 50, 50);
                visible_layout(View.GONE, View.GONE, View.VISIBLE, View.GONE, View.GONE);
                cloubChange(Integer.valueOf(CC_Value_NO2.getText().toString()));
                break;
            case R.id.O_Cloud:
                ConcenVal_Start[0] = 0;
                alpha_setting(50, 50, 50, 255, 50);
                visible_layout(View.GONE, View.GONE, View.GONE, View.VISIBLE, View.GONE);
                cloubChange(Integer.valueOf(CC_Value_O3.getText().toString()));
                break;
            case R.id.SO_Cloud:
                ConcenVal_Start[3] = 0;
                alpha_setting(50, 50, 50, 50, 255);
                visible_layout(View.GONE, View.GONE, View.GONE, View.GONE, View.VISIBLE);
                cloubChange(Integer.valueOf(CC_Value_SO2.getText().toString()));
                break;
        }
        ShowMyLocaion(lat, lon, map);
    }

    private void cloubChange(int cloudvalue) {
        if(cloudvalue >= 300)   {
            cloudimage = "aqicloud6";
        }   else if (cloudvalue >= 200)    {
            cloudimage = "aqicloud5";
        }   else if (cloudvalue >= 150) {
            cloudimage = "aqicloud4";
        }   else if (cloudvalue >= 100) {
            cloudimage = "aqicloud3";
        }   else if (cloudvalue >= 50)  {
            cloudimage = "aqicloud2";
        }   else    {
            cloudimage = "aqicloud1";
        }
    }

    private void AQI_Button_Set(int i, int value)  {
        Button setButton = null;
        switch (i) {
            case 0:
                setButton = o3Btn;
                break;
            case 1:
                setButton = no2Btn;
                break;
            case 2:
                setButton = coBtn;
                break;
            case 3:
                setButton = so2Btn;
                break;
            case 4:
                setButton = pmBtn;
                break;
            default:
                setButton = pmBtn;
                break;
        }
        
        String Texture = null;
        String TextColor = null;
        String BColor = null;

        if(value >= 300)    {
            Texture = "Hazardous";
            TextColor = "#FFFFFF";
            //BColor = "#7E0023";
            //setButton.setBackground(getResources().getDrawable(R.drawable.rounded_corner_button6));
            setButton.setBackgroundResource(R.drawable.rounded_corner_button6);
        }   else if ( value >=200)  {
            Texture = "Very Unhealthy";
            TextColor = "#FFFFFF";
            //BColor = "#8F3F97";
            //setButton.setBackground(getResources().getDrawable(R.drawable.rounded_corner_button5));
            setButton.setBackgroundResource(R.drawable.rounded_corner_button5);
        }   else if (value >= 150)  {
            Texture = "Unhealthy";
            TextColor = "#FFFFFF";
            //BColor = "#FF0000";
            //setButton.setBackground(getResources().getDrawable(R.drawable.rounded_corner_button4));
            setButton.setBackgroundResource(R.drawable.rounded_corner_button4);
        }   else if (value >= 100)  {
            Texture = "Sensitive Unhealthy";
            TextColor = "#FFFFFF";
            //BColor = "#FF7E00";
            //setButton.setBackground(getResources().getDrawable(R.drawable.rounded_corner_button3));
            setButton.setBackgroundResource(R.drawable.rounded_corner_button3);
        }   else if (value >= 50)   {
            Texture = "Moderate";
            TextColor = "#000000";
            //BColor = "#FFFF00";
            //setButton.setBackground(getResources().getDrawable(R.drawable.rounded_corner_button2));
            setButton.setBackgroundResource(R.drawable.rounded_corner_button2);
        }   else    {
            Texture = "Good";
            TextColor = "#FFFFFF";
            //BColor = "#00E400";
            //setButton.setBackground(getResources().getDrawable(R.drawable.rounded_corner_button));
            setButton.setBackgroundResource(R.drawable.rounded_corner_button);

            BColor = "#00E400";
        }
        /*
        if(value>=0 && value<=50)    {
        }   else if (value>50 && value<=100)    {
        }   else if (value>100 && value<=150)   {
        }   else if (value>150 && value<=200)   {
        }   else if (value>200 && value<=300)   {
        }   else    {
        }
        */
        setButton.setText(Texture);
        setButton.setTextColor(Color.parseColor(TextColor));
        //setButton.setBackgroundColor(Color.parseColor(BColor));

    }

    private void chart_setting() {
        // 차트의 아래 Axis
        XAxis xAxis = mChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM); // xAxis의 위치는 아래쪽
        // xAxis.setTextSize(10f); // xAxis에 표출되는 텍스트의 크기는 10f
        xAxis.setDrawGridLines(false); // xAxis의 그리드 라인을 없앰

        // 차트의 왼쪽 Axis
        YAxis leftAxis = mChart.getAxisLeft();
        leftAxis.setDrawGridLines(false); // leftAxis의 그리드 라인을 없앰

        // 차트의 오른쪽 Axis
        YAxis rightAxis = mChart.getAxisRight();
        rightAxis.setEnabled(false); // rightAxis를 비활성화 함

        // 차트의 아래 Axis
        XAxis xAxis2 = mChart2.getXAxis();
        xAxis2.setPosition(XAxis.XAxisPosition.BOTTOM); // xAxis의 위치는 아래쪽
        // xAxis.setTextSize(10f); // xAxis에 표출되는 텍스트의 크기는 10f
        xAxis2.setDrawGridLines(false); // xAxis의 그리드 라인을 없앰

        // 차트의 왼쪽 Axis
        YAxis leftAxis2 = mChart2.getAxisLeft();
        leftAxis2.setDrawGridLines(false); // leftAxis의 그리드 라인을 없앰

        // 차트의 오른쪽 Axis
        YAxis rightAxis2 = mChart2.getAxisRight();
        rightAxis2.setEnabled(false); // rightAxis를 비활성화 함
    }

    private void alpha_setting(int a, int b, int c, int d, int e) {
        alphaPM.setAlpha(a);
        alphaCO.setAlpha(b);
        alphaNO2.setAlpha(c);
        alphaO3.setAlpha(d);
        alphaSO2.setAlpha(e);
    }

    private void visible_layout(int a, int b, int c, int d, int e) {
        PMLayout.setVisibility(a);
        COLayout.setVisibility(b);
        NO2Layout.setVisibility(c);
        O3Layout.setVisibility(d);
        SO2Layout.setVisibility(e);
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
                        Thread.sleep(500); // 0.5초간 쉼
                    } catch (InterruptedException ie) {
                        ie.printStackTrace();
                    }
                }
            }
        });
        thread.start();
    }

    private void addEntry() {
        LineData data = mChart.getData();
        LineData data2 = mChart2.getData();


        LineDataSet set1 = (LineDataSet) data.getDataSetByIndex(0);
        LineDataSet set2 = (LineDataSet) data.getDataSetByIndex(0);


        if (set1 == null) {
            // creation of null
            set1 = createSet(Color.parseColor("#FFFF7A87"), "Heart-Rate");
            data.addDataSet(set1);
        }

        if (set2 == null) {
            set2 = createSet(Color.parseColor("#FFFF7A87"), "RR-Interval");
            data2.addDataSet(set2);
        }

        data.addEntry(new Entry(set1.getEntryCount(), heart_rate_value), 0);
        data2.addEntry(new Entry(set1.getEntryCount(), rr_rate_value), 0);

        data.notifyDataChanged();                                      // data의 값 변동을 감지함
        data2.notifyDataChanged();

        mChart.notifyDataSetChanged();                                // chart의 값 변동을 감지함
        mChart.setVisibleXRangeMaximum(10);                           // chart에서 최대 X좌표기준으로 몇개의 데이터를 보여줄지 설정함
        mChart.moveViewToX(data.getEntryCount());                     // 가장 최근에 추가한 데이터의 위치로 chart를 이동함

        mChart2.notifyDataSetChanged();                                // chart의 값 변동을 감지함
        mChart2.setVisibleXRangeMaximum(10);                           // chart에서 최대 X좌표기준으로 몇개의 데이터를 보여줄지 설정함
        mChart2.moveViewToX(data.getEntryCount());                     // 가장 최근에 추가한 데이터의 위치로 chart를 이동함

    }

    private LineDataSet createSet(int setColor, String dataName) {
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
        mapView.onPause();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mapView.onLowMemory();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;

        BitmapDescriptor icon = BitmapDescriptorFactory.fromResource(R.drawable.aqicloud1);
        ShowMyLocaion(lat,lon,map);
        StartLocationService();
    }

    private void StartLocationService() {
        Log.e("startLocationService", "startLocationService");
        LocationManager manager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        GPSListener gpsListener = new GPSListener();
        long minTime = 10000;
        float minDistance = 0;
        try {   //GPS 위치 요청
            if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            manager.requestLocationUpdates(GPS_PROVIDER, minTime, minDistance, (LocationListener) gpsListener);

            // location request with network
            manager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, minTime, minDistance, (LocationListener) gpsListener);

            Location lastLocation = manager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

            if (lastLocation != null) {
                Double latitude = lastLocation.getLatitude();
                Double longitude = lastLocation.getLongitude();

               // Toast.makeText(getActivity(), "Lat:" + latitude + " / Lon:" + longitude, Toast.LENGTH_SHORT).show();
            }
        } catch (SecurityException e) {
            e.printStackTrace();
        }
        //Toast.makeText(getActivity(), "start Location tracker.", Toast.LENGTH_SHORT).show();
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

        BitmapDescriptor icon = BitmapDescriptorFactory.fromResource(getResources().getIdentifier(cloudimage,"drawable","com.example.khseob0715.sanfirst"));

        markerOptions.icon(icon);

        CircleOptions circleOptions = new CircleOptions()
                .center(new LatLng(lat, lon))
                .strokeColor(Color.WHITE)
                .fillColor(0xb8ffffff)
                .radius(100);
        // In meters

        googleMap.clear();

// Get back the mutable Circle
        googleMap.addCircle(circleOptions);

        googleMap.addMarker(markerOptions);
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(nowLocation));
        googleMap.animateCamera(CameraUpdateFactory.zoomTo(17));
    }


    // aqi seekbar
    public void seekani(CircularProgressBar item, int startval, int endval) {
        item.animateProgressTo(startval, endval, new CircularProgressBar.ProgressAnimationListener() {
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

    public void startSubThread() {
        //작업스레드 생성(매듭 묶는과정)
        heartHandler aqiRunnable = new heartHandler();
        aqiThread = new Thread(aqiRunnable);
        aqiThread.setDaemon(true);
        aqiThread.start();
    }

    android.os.Handler receivehearthandler = new android.os.Handler() {
        public void handleMessage(Message msg) {

            seekani(heart_seekbar, heart_start, heart_rate_value );

            seekani(o3_seekbar, Integer.valueOf(ConcenVal_Start[0]/3), Integer.valueOf(ConcenVal[0]/3));
            seekani(no2_seekbar,Integer.valueOf(ConcenVal_Start[1]/3), Integer.valueOf(ConcenVal[1]/3));
            seekani(co_seekbar, Integer.valueOf(ConcenVal_Start[2]/3), Integer.valueOf(ConcenVal[2]/3));
            seekani(so2_seekbar, Integer.valueOf(ConcenVal_Start[3]/3), Integer.valueOf(ConcenVal[3]/3));
            seekani(pm25_seekbar, Integer.valueOf(ConcenVal_Start[4]/3), Integer.valueOf(ConcenVal[4]/3));

            heart_start = heart_rate_value;

            for(int i = 0 ; i < 5; i++){
                ConcenVal_Start[i] = ConcenVal[i];
            }

            CC_Value_O3.setText(String.valueOf(ConcenVal[0]));
            CC_Value_NO2.setText(String.valueOf(ConcenVal[1]));
            CC_Value_CO.setText(String.valueOf(ConcenVal[2]));
            CC_Value_SO2.setText(String.valueOf(ConcenVal[3]));
            CC_Value_PM.setText(String.valueOf(ConcenVal[4]));

            SeekValue_O3.setText(String.valueOf(ConcenVal[0]));
            SeekValue_NO2.setText(String.valueOf(ConcenVal[1]));
            SeekValue_CO.setText(String.valueOf(ConcenVal[2]));
            SeekValue_SO2.setText(String.valueOf(ConcenVal[3]));
            SeekValue_PM.setText(String.valueOf(ConcenVal[4]));

            tempText.setText(String.valueOf(ConcenVal[5]) +"℉");

            for(int i=0; i<5; i++)  {
                AQI_Button_Set(i, ConcenVal[i]);
            }

            HeartRateText.setText(String.valueOf(heart_rate_value));
        }
    };

    public class heartHandler implements Runnable {
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
