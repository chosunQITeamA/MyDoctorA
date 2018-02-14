package com.example.khseob0715.sanfirst.navi_fragment;


import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.res.ResourcesCompat;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TabHost;
import android.widget.TextView;

import com.example.khseob0715.sanfirst.R;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;


/**
 * A simple {@link Fragment} subclass.
 */
public class Fragment_AQIHistory extends Fragment {

    private ListView listView;
    private ViewGroup rootView;
    private ProgressBar PM25_Bar,CO_Bar,NO2_Bar,SO2_Bar,O3_Bar;

    private myAdapter adapter2;

    // 서버랑 연결 되면 받을 값.
    private String[] items = {"ss", "das","s","s","s","s","s","f","s","s","s","s","f"};

    private LineChart pmChart;
    private LineChart coChart;
    private LineChart o3Chart;
    private LineChart noChart;
    private LineChart soChart;

    private Thread thread;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        rootView = (ViewGroup) inflater.inflate(R.layout.fragment_aqi_history, container, false);

        listView = (ListView) rootView.findViewById(R.id.airlistview);
        adapter2 = new myAdapter();
        listView.setAdapter(adapter2);

        PM25_Bar = (ProgressBar)rootView.findViewById(R.id.PM25_progress);
        PM25_Bar.getProgressDrawable().setColorFilter(Color.GREEN, android.graphics.PorterDuff.Mode.SRC_IN);
        PM25_Bar.setProgress(50);

        CO_Bar = (ProgressBar)rootView.findViewById(R.id.CO_progress);
        CO_Bar.getProgressDrawable().setColorFilter(Color.YELLOW, android.graphics.PorterDuff.Mode.SRC_IN);
        CO_Bar.setProgress(50);

        NO2_Bar = (ProgressBar)rootView.findViewById(R.id.NO2_progress);
        NO2_Bar.getProgressDrawable().setColorFilter(Color.CYAN, android.graphics.PorterDuff.Mode.SRC_IN);
        NO2_Bar.setProgress(50);

        SO2_Bar = (ProgressBar)rootView.findViewById(R.id.SO2_progress);
        SO2_Bar.getProgressDrawable().setColorFilter(Color.RED, android.graphics.PorterDuff.Mode.SRC_IN);
        SO2_Bar.setProgress(50);

        O3_Bar = (ProgressBar)rootView.findViewById(R.id.O3_progress);
        O3_Bar.getProgressDrawable().setColorFilter(Color.RED, android.graphics.PorterDuff.Mode.SRC_IN);
        O3_Bar.setProgress(50);

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
        //spec.setIndicator(null, ResourcesCompat.getDrawable(getResources(), R.drawable.bigcloud_so, null));
        spec.setIndicator("SO2");
        spec.setContent(R.id.tab6);
        host.addTab(spec);


        for(int i=0;i<host.getTabWidget().getChildCount();i++)
        {
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

        feedMultiple(); // 쓰레드를 활용하여 실시간으로 데이터

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
                    getActivity().runOnUiThread(runnable); // UI 쓰레드에서 위에서 생성한 runnable를 실행함
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
        LineData data = pmChart.getData();
        LineData data2 = coChart.getData();
        LineData data3 = o3Chart.getData();
        LineData data4 = noChart.getData();
        LineData data5 = soChart.getData();


        LineDataSet set1 = (LineDataSet) data.getDataSetByIndex(0);
        LineDataSet set2 = (LineDataSet) data.getDataSetByIndex(0);
        LineDataSet set3 = (LineDataSet) data.getDataSetByIndex(0);
        LineDataSet set4 = (LineDataSet) data.getDataSetByIndex(0);
        LineDataSet set5 = (LineDataSet) data.getDataSetByIndex(0);


        if (set1 == null) {
            // creation of null
            set1 = createSet(Color.parseColor("#FFFF7A87"), "PM2.5");
            data.addDataSet(set1);
        }

        if (set2 == null) {
            set2 = createSet(Color.parseColor("#FFFF7A87"), "CO");
            data2.addDataSet(set2);
        }

        if (set3 == null) {
            set3 = createSet(Color.parseColor("#FFFF7A87"), "NO2");
            data3.addDataSet(set3);
        }

        if (set4 == null) {
            set4 = createSet(Color.parseColor("#FFFF7A87"), "O3");
            data4.addDataSet(set4);
        }

        if (set5 == null) {
            set5 = createSet(Color.parseColor("#FFFF7A87"), "SO2");
            data5.addDataSet(set5);
        }

        data.addEntry(new Entry(set1.getEntryCount(), 0), 0);
        data2.addEntry(new Entry(set2.getEntryCount(), 0), 0);
        data3.addEntry(new Entry(set3.getEntryCount(), 0), 0);
        data4.addEntry(new Entry(set4.getEntryCount(), 0), 0);
        data5.addEntry(new Entry(set5.getEntryCount(), 0), 0);

        data.notifyDataChanged();                                      // data의 값 변동을 감지함
        data2.notifyDataChanged();
        data3.notifyDataChanged();                                      // data의 값 변동을 감지함
        data4.notifyDataChanged();
        data5.notifyDataChanged();                                      // data의 값 변동을 감지함

        pmChart.notifyDataSetChanged();                                // chart의 값 변동을 감지함
        pmChart.setVisibleXRangeMaximum(10);                           // chart에서 최대 X좌표기준으로 몇개의 데이터를 보여줄지 설정함
        pmChart.moveViewToX(data.getEntryCount());                     // 가장 최근에 추가한 데이터의 위치로 chart를 이동함

        coChart.notifyDataSetChanged();                                // chart의 값 변동을 감지함
        coChart.setVisibleXRangeMaximum(10);                           // chart에서 최대 X좌표기준으로 몇개의 데이터를 보여줄지 설정함
        coChart.moveViewToX(data.getEntryCount());                     // 가장 최근에 추가한 데이터의 위치로 chart를 이동함

        o3Chart.notifyDataSetChanged();                                // chart의 값 변동을 감지함
        o3Chart.setVisibleXRangeMaximum(10);                           // chart에서 최대 X좌표기준으로 몇개의 데이터를 보여줄지 설정함
        o3Chart.moveViewToX(data.getEntryCount());                     // 가장 최근에 추가한 데이터의 위치로 chart를 이동함

        noChart.notifyDataSetChanged();                                // chart의 값 변동을 감지함
        noChart.setVisibleXRangeMaximum(10);                           // chart에서 최대 X좌표기준으로 몇개의 데이터를 보여줄지 설정함
        noChart.moveViewToX(data.getEntryCount());                     // 가장 최근에 추가한 데이터의 위치로 chart를 이동함

        soChart.notifyDataSetChanged();                                // chart의 값 변동을 감지함
        soChart.setVisibleXRangeMaximum(10);                           // chart에서 최대 X좌표기준으로 몇개의 데이터를 보여줄지 설정함
        soChart.moveViewToX(data.getEntryCount());                     // 가장 최근에 추가한 데이터의 위치로 chart를 이동함
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
            HistoricalAir view = new HistoricalAir(rootView.getContext());
            //view.setText(items[position]);
            //view.setTextSize(40.0f);
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
}
