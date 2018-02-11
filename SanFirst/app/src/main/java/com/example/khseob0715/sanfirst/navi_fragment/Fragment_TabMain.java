package com.example.khseob0715.sanfirst.navi_fragment;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.graphics.Color;
import android.graphics.ColorSpace;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.res.ResourcesCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
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
import com.lylc.widget.circularprogressbar.CircularProgressBar;

public class Fragment_TabMain extends Fragment implements  View.OnClickListener {

    private CircularProgressBar co_seekbar;
    private CircularProgressBar so2_seekbar;
    private CircularProgressBar o3_seekbar;
    private CircularProgressBar no2_seekbar;
    private CircularProgressBar pm25_seekbar;

    private LineChart mChart;
    private Thread thread;

    private ViewGroup rootView;

    private LinearLayout PMLayout, COLayout, O3Layout, NO2Layout, SO2Layout;
    private ImageView PM_Cloud, CO_Cloud, O3_Cloud, NO2_Cloud, SO2_Cloud, Heart;

    private Drawable alphaPM, alphaCO, alphaO3, alphaNO2, alphaSO2;

    UserActivity mainclass = new UserActivity();

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


        return rootView;
    }

    public void onViewCreated(final View view, @Nullable Bundle savedInstanceState) {

        co_seekbar = (CircularProgressBar) view.findViewById(R.id.coseekbar); // 각 AQI별 값 (12345까지 필요)
        so2_seekbar = (CircularProgressBar) view.findViewById(R.id.so2seekbar);
        o3_seekbar = (CircularProgressBar) view.findViewById(R.id.o3seekbar);
        no2_seekbar = (CircularProgressBar) view.findViewById(R.id.no2seekbar);
        pm25_seekbar = (CircularProgressBar) view.findViewById(R.id.pm25seekbar);

        PMLayout = (LinearLayout) view.findViewById(R.id.PMLayout);
        COLayout = (LinearLayout) view.findViewById(R.id.COLayout);
        O3Layout = (LinearLayout) view.findViewById(R.id.O3Layout);
        SO2Layout = (LinearLayout) view.findViewById(R.id.SO2Layout);
        NO2Layout = (LinearLayout) view.findViewById(R.id.NO2Layout);

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

        Heart = (ImageView)view.findViewById(R.id.smallheart);

        Animation animation = AnimationUtils.loadAnimation(getContext(),R.anim.wave);
        animation.setRepeatCount(Animation.INFINITE);
        Heart.startAnimation(animation);

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

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.PM_Cloud:
                alpha_setting(255,50,50,50,50);
                visible_layout(View.VISIBLE, View.GONE, View.GONE, View.GONE, View.GONE);
                break;
            case R.id.CO_Cloud:
                alpha_setting(50,255,50,50,50);
                visible_layout(View.GONE, View.VISIBLE, View.GONE, View.GONE, View.GONE);
                break;
            case R.id.NO_Cloud:
                alpha_setting(50,50,255,50,50);
                visible_layout(View.GONE, View.GONE, View.VISIBLE, View.GONE, View.GONE);
                break;
            case R.id.O_Cloud:
                alpha_setting(50,50,50,255,50);
                visible_layout(View.GONE, View.GONE, View.GONE, View.VISIBLE, View.GONE);
                break;
            case R.id.SO_Cloud:
                alpha_setting(50,50,50,50,255);
                visible_layout(View.GONE, View.GONE, View.GONE, View.GONE, View.VISIBLE);
                break;
        }
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
        O3Layout.setVisibility(c);
        SO2Layout.setVisibility(d);
        NO2Layout.setVisibility(e);
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

        LineDataSet set1 = (LineDataSet) data.getDataSetByIndex(0);

        if (set1 == null) {
            // creation of null
            set1 = createSet(Color.parseColor("#FFFF7A87"),"RR-Rate");
            data.addDataSet(set1);
        }

        data.addEntry(new Entry(set1.getEntryCount(), (float) (Math.random() * 40) + 30f), 0);

        data.notifyDataChanged();                                      // data의 값 변동을 감지함

        mChart.notifyDataSetChanged();                                // chart의 값 변동을 감지함
        mChart.setVisibleXRangeMaximum(10);                           // chart에서 최대 X좌표기준으로 몇개의 데이터를 보여줄지 설정함
        mChart.moveViewToX(data.getEntryCount());                     // 가장 최근에 추가한 데이터의 위치로 chart를 이동함
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


}
