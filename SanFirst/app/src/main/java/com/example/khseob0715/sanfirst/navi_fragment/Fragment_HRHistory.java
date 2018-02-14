package com.example.khseob0715.sanfirst.navi_fragment;


import android.Manifest;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import com.example.khseob0715.sanfirst.R;
import com.example.khseob0715.sanfirst.UserActivity.UserActivity;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.TileOverlay;
import com.google.android.gms.maps.model.TileOverlayOptions;
import com.google.android.gms.maps.model.TileProvider;
import com.google.android.gms.maps.model.UrlTileProvider;

import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import static android.location.LocationManager.GPS_PROVIDER;


public class Fragment_HRHistory extends Fragment implements View.OnClickListener{

    private MapView mapView = null;

    private Double HR_Lat = 32.882499;
    private Double HR_lon = -117.234644;

    GoogleMap map;


    private static final int TRANSPARENCY_MAX = 100;

    private static final String MOON_MAP_URL_FORMAT = "https://tiles.waqi.info/tiles/usepa-aqi/%d/%d/%d.png?token=9f64560eb24586831e1167b1c0e8ecddb1193014";
    //"https://tiles.breezometer.com/{z}/{x}/{y}.png?key=YOUR_API_KEY";

    private TileOverlay mMoonTiles;

    private ListView listView;
    private LinearLayout Start_date, End_date;
    private TextView Start_date_text, End_date_text;

    private DatePickerDialog datePickerDialog;

    private myAdapter adapter;

    // 서버랑 연결 되면 받을 값.
    private String[] items = {"ss", "das","ss", "das","ss", "das","ss", "das","ss", "das","ss", "das","ss", "das"};

    private LinearLayout startLayout, endLayout;

    ViewGroup rootView;

    UserActivity mainclass = new UserActivity(); // ?

    private LineChart mChart;
    private Thread thread;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = (ViewGroup) inflater.inflate(R.layout.fragment_heartrate_history, container, false);

        mapView = (MapView) rootView.findViewById(R.id.map);
       // mapView.getMapAsync((OnMapReadyCallback) this);

        listView = (ListView) rootView.findViewById(R.id.listView);
        adapter = new myAdapter();
        listView.setAdapter(adapter);

        startLayout = (LinearLayout)rootView.findViewById(R.id.start_date);
        startLayout.setOnClickListener(this);
        endLayout = (LinearLayout)rootView.findViewById(R.id.end_date);
        endLayout.setOnClickListener(this);

        Start_date_text = (TextView)rootView.findViewById(R.id.Start_date_text);
        End_date_text = (TextView)rootView.findViewById(R.id.End_date_text);

        long now = System.currentTimeMillis();

        Date date = new Date(now);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String getTime = sdf.format(date);

        Start_date_text.setText(getTime);
        End_date_text.setText(getTime);

        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        mChart = (LineChart) view.findViewById(R.id.HeartChart);

        // 차트의 아래 Axis
        XAxis xAxis = mChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM); // xAxis의 위치는 아래쪽
        xAxis.setTextSize(10f); // xAxis에 표출되는 텍스트의 크기는 10f
        xAxis.setDrawGridLines(false); // xAxis의 그리드 라인을 없앰

        // 차트의 왼쪽 Axis
        YAxis leftAxis = mChart.getAxisLeft();
        leftAxis.setDrawGridLines(false); // leftAxis의 그리드 라인을 없앰

        // 차트의 오른쪽 Axis
        YAxis rightAxis = mChart.getAxisRight();
        rightAxis.setEnabled(false); // rightAxis를 비활성화 함

        LineData data = new LineData();
        mChart.setData(data); // LineData를 셋팅함

        feedMultiple(); // 쓰레드를 활용하여 실시간으로 데이터
    }

    private void feedMultiple() {
        if(thread != null)
            thread.interrupt();        // 살아있는 쓰레드에 인터럽트를 검

        final Runnable runnable = new Runnable() {
            @Override
            public void run() {
                addEntry();            // addEntry를 실행하게 함
            }
        };

        thread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (true)
                {
                    mainclass.runOnUiThread(runnable);    // UI 쓰레드에서 위에서 생성한 runnable를 실행함
                    try
                    {
                        Thread.sleep(500);        // 0.5초간 쉼
                    }catch (InterruptedException ie)
                    {
                        ie.printStackTrace();
                    }
                }
            }
        });
        thread.start();
    }

    private void addEntry() {
        LineData data = mChart.getData();

        LineDataSet set0 = (LineDataSet) data.getDataSetByIndex(0);
        LineDataSet set1 = (LineDataSet) data.getDataSetByIndex(1);

        if (set0 == null || set1 == null) {
            // creation of null
            set0 = createSet(-65536,"Heart-Rate");      // createSet 한다.
            set1 = createSet(-16711681,"RR-Rate");

            data.addDataSet(set0);
            data.addDataSet(set1);
        }

        data.addEntry(new Entry(set0.getEntryCount(), (float) (Math.random() * 40) + 30f), 0);
        data.addEntry(new Entry(set1.getEntryCount(), (float) (Math.random() * 40) + 30f), 1);

        data.notifyDataChanged();                                      // data의 값 변동을 감지함

        mChart.notifyDataSetChanged();                                // chart의 값 변동을 감지함
        mChart.setVisibleXRangeMaximum(10);                           // chart에서 최대 X좌표기준으로 몇개의 데이터를 보여줄지 설정함
        mChart.moveViewToX(data.getEntryCount());                     // 가장 최근에 추가한 데이터의 위치로 chart를 이동함
    }

    private LineDataSet createSet(int setColor, String dataName) {
        LineDataSet set = new LineDataSet(null, dataName);           // 데이터셋의 이름을 "Dynamic Data"로 설정(기본 데이터는 null)
        set.setAxisDependency(YAxis.AxisDependency.LEFT);            // Axis를 YAxis의 LEFT를 기본으로 설정
        set.setColor(setColor);                                      // 데이터의 라인색을 HoloBlue로 설정
        set.setCircleColor(setColor);                                // 데이터의 점을 WHITE로 설정 // 65536
        set.setLineWidth(2f);                                        // 라인의 두께를 2f로 설정
        set.setCircleRadius(4f);                                     // 데이터 점의 반지름을 4f로 설정
        set.setFillAlpha(65);                                        // 투명도 채우기를 65로 설정
        set.setFillColor(ColorTemplate.getHoloBlue());               // 채우기 색을 HoloBlue로 설정
        set.setHighLightColor(Color.rgb(244, 117, 117));             // 하이라이트 컬러(선택시 색)을 rgb(244, 117, 117)로 설정
        set.setDrawValues(false);                                   // 각 데이터의 값을 텍스트로 나타내지 않게함(false)
        return set;                                                 // 이렇게 생성한 set을 반환
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
        if(thread != null){
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

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        //액티비티가 처음 생성될 때 실행되는 함수
        if (mapView != null) {
            mapView.onCreate(savedInstanceState);
        }
    }

    private void StartLocationService() {
        Log.e("startLocationService","startLocationService");
        LocationManager manager = (LocationManager)getActivity().getSystemService(Context.LOCATION_SERVICE);
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
            }
        } catch (SecurityException e) {
            e.printStackTrace();
        }
        Toast.makeText(getActivity(), "start Location tracker.", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.start_date:

                DatePickerDialog.OnDateSetListener callback = new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        Start_date_text.setText(year + "-" + (monthOfYear + 1) + "-" + dayOfMonth);
                    }
                };

                datePickerDialog = new DatePickerDialog(view.getContext(), android.R.style.Theme_Material_Light_Dialog_Alert, callback, 2018, 0, 19);
                datePickerDialog.show();

                break;

            case R.id.end_date:

                DatePickerDialog.OnDateSetListener endcallback = new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        End_date_text.setText(year + "-" + (monthOfYear + 1) + "-" + dayOfMonth);
                    }
                };

                datePickerDialog = new DatePickerDialog(view.getContext(), android.R.style.Theme_Material_Light_Dialog_Alert, endcallback, 2018, 0, 19);
                datePickerDialog.show();
                break;

        }
    }


    private class GPSListener implements LocationListener{

        @Override
        public void onLocationChanged(Location location) {
//            lat = location.getLatitude();
//            lon = location.getLongitude();
//            String msg = "Lat:" + lat + " / Lon:" + lon;
//            m_Adapter.add(msg);
        }

        @Override
        public void onStatusChanged(String s, int i, Bundle bundle) {
        }

        @Override
        public void onProviderEnabled(String s) {
            ShowMyLocation(HR_Lat, HR_lon, map);
        }

        @Override
        public void onProviderDisabled(String s) {
        }
    }

    private void ShowMyLocation(Double lat, Double lon, GoogleMap googleMap) {
        LatLng nowLocation = new LatLng(lat, lon);
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(nowLocation);
        markerOptions.title("now location");
        googleMap.addMarker(markerOptions);
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(nowLocation));
        googleMap.animateCamera(CameraUpdateFactory.zoomTo(20));
    }


    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        StartLocationService();

        TileProvider tileProvider = new UrlTileProvider(256, 256) {
            @Override
            public synchronized URL getTileUrl(int x, int y, int zoom) {
                // The moon tile coordinate system is reversed.  This is not normal.
                String s = String.format(Locale.US, MOON_MAP_URL_FORMAT, zoom, x, y);
                URL url = null;
                try {
                    url = new URL(s);
                } catch (MalformedURLException e) {
                    throw new AssertionError(e);
                }
                return url;
            }
        };
        mMoonTiles = map.addTileOverlay(new TileOverlayOptions().tileProvider(tileProvider));
    }


    class myAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return items.length;
        }

        @Override
        public Object getItem(int position) {
            return items[position];
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            HistoricalHeart view = new HistoricalHeart(rootView.getContext());
            //view.setText(items[position]);
            //view.setTextSize(40.0f);
            return view;
        }
    }

    class HistoricalHeart extends LinearLayout {
        TextView Test;

        public HistoricalHeart(Context context) {
            super(context);
            init(context);
        }

        public HistoricalHeart(Context context, @Nullable AttributeSet attrs) {
            super(context, attrs);
            init(context);
        }

        private void init(Context context) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            inflater.inflate(R.layout.historical_heart_list, this);
        }

    }
}