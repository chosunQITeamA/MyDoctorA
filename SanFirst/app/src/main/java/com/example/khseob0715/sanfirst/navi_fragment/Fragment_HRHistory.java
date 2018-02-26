package com.example.khseob0715.sanfirst.navi_fragment;


import android.app.DatePickerDialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import com.example.khseob0715.sanfirst.R;
import com.example.khseob0715.sanfirst.ServerConn.ReceiveHR;
import com.example.khseob0715.sanfirst.ServerConn.ReceiveHR_ChartData;
import com.example.khseob0715.sanfirst.UserActivity.UserActivity;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


public class Fragment_HRHistory extends Fragment implements View.OnClickListener, OnMapReadyCallback, OnChartValueSelectedListener {

    GoogleMap map;
    MapView mapView = null;

    //32.882499 / -117.234644
    public static Double lat = 32.882499;
    public static Double lon = -117.234644;

    private static final int TRANSPARENCY_MAX = 100;

    private ListView listView;
    private LinearLayout Start_date, End_date;
    private TextView Start_date_text, End_date_text, pick_date;
    private Button HRsearchBtn;

    private DatePickerDialog datePickerDialog;

    private myAdapter Heart_adapter;

    // 서버랑 연결 되면 받을 값.
    public static String[] items = new String[50];
    public static int[] HeartAvg = new int[50];
    public static int[] RRAvg = new int[50];
    public static double[] Historylat = new double[5000];
    public static double[] Historylon = new double[5000];

    public static int response_count = 0;

    private LinearLayout startLayout, endLayout;

    ViewGroup rootView;

    UserActivity mainclass = new UserActivity(); // ?
    ReceiveHR receiveHR = new ReceiveHR();
    ReceiveHR_ChartData receiveHR_chartData = new ReceiveHR_ChartData(); // chart data receive

    public static LineChart HRChart;
    public static LineChart RRChart;
    private Thread thread;

    private Handler handler;

    private String chart_date_text = "";

    private int usn;

    public static LineData data, data1;
    public static LineDataSet set0 = null , set1 = null;

    private String pre_dp = "1";

    public static int flag = 0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = (ViewGroup) inflater.inflate(R.layout.fragment_heartrate_history, container, false);

        HRChart = (LineChart) rootView.findViewById(R.id.HeartChart);
        RRChart = (LineChart) rootView.findViewById(R.id.RRChart);

        listView = (ListView) rootView.findViewById(R.id.listView);

        Heart_adapter = new myAdapter();

        listView.setAdapter(Heart_adapter);

        usn = UserActivity.getUSN();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                chart_date_text = "" + listView.getItemAtPosition(position);

                Toast.makeText(getContext(), "chart : " + chart_date_text, Toast.LENGTH_SHORT).show();
                if(!pre_dp.equals(chart_date_text)) {
                    pick_date.setText(chart_date_text);
                    if (set0 != null) {
                        set0.clear();
                    }

                    if (set1 != null) {
                        set1.clear();
                    }
                    receiveHR_chartData.ReceiveHR_ChartData_Asycn(usn, chart_date_text, chart_date_text);
                    // handler.postDelayed(new enableBtn(),2000);
                }
                pre_dp = "" + chart_date_text;
            }
        });

        startLayout = (LinearLayout) rootView.findViewById(R.id.start_date);
        startLayout.setOnClickListener(this);

        endLayout = (LinearLayout) rootView.findViewById(R.id.end_date);
        endLayout.setOnClickListener(this);

        HRsearchBtn = (Button) rootView.findViewById(R.id.HRDataSearchBtn);
        HRsearchBtn.setOnClickListener(this);

        Start_date_text = (TextView) rootView.findViewById(R.id.Start_date_text);
        End_date_text = (TextView) rootView.findViewById(R.id.End_date_text);

        pick_date = (TextView)rootView.findViewById(R.id.PickDate);

        TabHost_setting();

        chart_setting();

        Day_setting();

        handler = new Handler();

        receiveHR.ReceiveHR_Asycn(usn, Start_date_text.getText().toString(), End_date_text.getText().toString());
        handler.postDelayed(new Update_list(),1200);
        handler.postDelayed(new Update_list_chart(), 1200);

        mapView = (MapView) rootView.findViewById(R.id.mapView);
        mapView.getMapAsync(this);

        if (mapView != null) {
            mapView.onCreate(savedInstanceState);
        }

        HRChart.setOnChartValueSelectedListener(this);
        RRChart.setOnChartValueSelectedListener(this);

        return rootView;
    }

    private void Day_setting(){
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_MONTH, -7);

        Date previous_date = calendar.getTime();

        long now = System.currentTimeMillis();
        Date date = new Date(now);

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        String preTime = sdf.format(previous_date);
        String curTime = sdf.format(date);

        Start_date_text.setText(preTime);
        End_date_text.setText(curTime);

    }

    private void TabHost_setting(){
        TabHost host = (TabHost)rootView.findViewById(R.id.HeartTabhost);
        host.setup();

        TabHost.TabSpec spec = host.newTabSpec("tab1");
        spec.setIndicator("Heart-Rate");
        spec.setContent(R.id.tab1);
        host.addTab(spec);

        spec = host.newTabSpec("RR-Interval");
        spec.setIndicator("RR-Interval");
        //spec.setIndicator(null, ResourcesCompat.getDrawable(getResources(), R.drawable.bigcloud_pm, null));
        spec.setContent(R.id.tab2);
        host.addTab(spec);

        for(int i=0;i<host.getTabWidget().getChildCount();i++){
            TextView tv = (TextView) host.getTabWidget().getChildAt(i).findViewById(android.R.id.title);
            tv.setTextColor(Color.parseColor("#000000"));
        }
    }

    private void chart_setting(){
        // 차트의 아래 Axis
        XAxis xAxis = HRChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM); // xAxis의 위치는 아래쪽
        xAxis.setTextSize(10f); // xAxis에 표출되는 텍스트의 크기는 10f
        xAxis.setDrawGridLines(false); // xAxis의 그리드 라인을 없앰

        // 차트의 왼쪽 Axis
        YAxis leftAxis = HRChart.getAxisLeft();
        leftAxis.setDrawGridLines(false); // leftAxis의 그리드 라인을 없앰

        // 차트의 오른쪽 Axis
        YAxis rightAxis = HRChart.getAxisRight();
        rightAxis.setEnabled(false); // rightAxis를 비활성화 함

        // 차트의 아래 Axis
        XAxis RRxAxis = RRChart.getXAxis();
        RRxAxis.setPosition(XAxis.XAxisPosition.BOTTOM); // xAxis의 위치는 아래쪽
        RRxAxis.setTextSize(10f); // xAxis에 표출되는 텍스트의 크기는 10f
        RRxAxis.setDrawGridLines(false); // xAxis의 그리드 라인을 없앰

        // 차트의 왼쪽 Axis
        YAxis RRleftAxis = RRChart.getAxisLeft();
        RRleftAxis.setDrawGridLines(false); // leftAxis의 그리드 라인을 없앰

        // 차트의 오른쪽 Axis
        YAxis RRrightAxis = RRChart.getAxisRight();
        RRrightAxis.setEnabled(false); // rightAxis를 비활성화 함

        LineData data = new LineData();
        HRChart.setData(data); // LineData를 셋팅함

        LineData data1 = new LineData();
        RRChart.setData(data1);
    }

    @Override
    public void onValueSelected(Entry e, Highlight h) {
        Toast.makeText(getContext(),""+e.getX(),Toast.LENGTH_LONG).show();
        int selectPick = (int)e.getX();
        ShowMyLocaion(Historylat[selectPick], Historylon[selectPick], map);
    }

    @Override
    public void onNothingSelected() {

    }


    public static void addEntry(double HR, double RR) {
        data = HRChart.getData();
        data1 = RRChart.getData();

        set0 = (LineDataSet) data.getDataSetByIndex(0);
        set1 = (LineDataSet) data1.getDataSetByIndex(0);

        if(set0 == null){
            set0 = createSet(Color.parseColor("#FFFF7A87"), "Heart-Rate");      // createSet 한다.
            data.addDataSet(set0);
        }

        if (set1 == null) {
            // creation of null
            set1 = createSet(Color.parseColor("#FFFF7A87"), "RR-Interval");
            data1.addDataSet(set1);
        }

        data.addEntry(new Entry(set0.getEntryCount(), (float)HR), 0);
        data1.addEntry(new Entry(set1.getEntryCount(), (float)RR), 0);

        data.notifyDataChanged();                                      // data의 값 변동을 감지함
        data1.notifyDataChanged();

        HRChart.notifyDataSetChanged();                                // chart의 값 변동을 감지함
        HRChart.setVisibleXRangeMaximum(10);                           // chart에서 최대 X좌표기준으로 몇개의 데이터를 보여줄지 설정함
        HRChart.moveViewToX(0);

        RRChart.notifyDataSetChanged();;
        RRChart.setVisibleXRangeMaximum(10);
        RRChart.moveViewToX(0);
    }

    public static LineDataSet createSet(int setColor, String dataName) {
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
        ShowMyLocaion(lat,lon,map);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.start_date:

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

                datePickerDialog = new DatePickerDialog(view.getContext(), android.R.style.Theme_Material_Light_Dialog_Alert, callback, 2018, 1, 11);
                datePickerDialog.show();

                break;

            case R.id.end_date:
                DatePickerDialog.OnDateSetListener endcallback = new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        String data = year + "-" + (monthOfYear + 1) + "-" + dayOfMonth;
                        SimpleDateFormat enddata = new SimpleDateFormat("yyyy-MM-dd");

                        Date date = null ;
                        try {
                            date = enddata.parse(data);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }

                        End_date_text.setText(enddata.format(date));
                    }
                };

                datePickerDialog = new DatePickerDialog(view.getContext(), android.R.style.Theme_Material_Light_Dialog_Alert, endcallback, 2018, 1, 1);
                datePickerDialog.show();
                break;

            case R.id.HRDataSearchBtn:
                int usn = UserActivity.getUSN();
                receiveHR.ReceiveHR_Asycn(usn, Start_date_text.getText().toString(), End_date_text.getText().toString());
                handler.postDelayed(new Update_list(),1200);
                break;
        }
    }

    private void ShowMyLocaion(Double lat, Double lon, GoogleMap googleMap) {
        LatLng nowLocation = new LatLng(lat, lon);
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(nowLocation);
        markerOptions.title("now location");

        BitmapDescriptor icon = BitmapDescriptorFactory.fromResource(R.drawable.heart_icon);
        markerOptions.icon(icon);

        googleMap.clear();
        googleMap.addMarker(markerOptions);
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(nowLocation));
        googleMap.animateCamera(CameraUpdateFactory.zoomTo(15));
    }

    class myAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return response_count;
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

            TextView date = (TextView)view.findViewById(R.id.HistoryHeartDate);
            date.setText(items[position]);

            TextView Heart_avg = (TextView)view.findViewById(R.id.AvgHeart_text);
            Heart_avg.setText(String.valueOf(HeartAvg[position]));

            TextView RR_avg = (TextView)view.findViewById(R.id.AvgRR_text);
            RR_avg.setText(String.valueOf(RRAvg[position]));

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
            View view = inflater.inflate(R.layout.historical_heart_list, this);
        }
    }

    private class Update_list implements Runnable{

        @Override
        public void run() {
            Heart_adapter.notifyDataSetChanged();
        }
    }
    private class Update_list_chart implements Runnable{
        @Override
        public void run() {

            String init_chart = "" + listView.getItemAtPosition(0);
            receiveHR_chartData.ReceiveHR_ChartData_Asycn(usn, init_chart, init_chart);
            pick_date.setText(init_chart);
        }
    }


}