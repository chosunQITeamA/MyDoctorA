package com.example.khseob0715.sanfirst.navi_fragment;


import android.app.DatePickerDialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.example.khseob0715.sanfirst.R;
import com.example.khseob0715.sanfirst.UserActivity.UserMainActivity;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;



public class Fragment_HRHistory extends Fragment {

    private ListView listView;
    private LinearLayout Start_date, End_date;
    private TextView Start_date_text, End_date_text;

    private DatePickerDialog datePickerDialog;

    myAdapter adapter;

    // 서버랑 연결 되면 받을 값.
    String[] items = {"ss", "das"};


    ViewGroup rootView;

    UserMainActivity mainclass = new UserMainActivity(); // ?

    private LineChart chart;
    private Thread thread;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = (ViewGroup) inflater.inflate(R.layout.fragment_heartrate_history, container, false);

        listView = (ListView) rootView.findViewById(R.id.listView);
        adapter = new myAdapter();
        listView.setAdapter(adapter);

        Start_date_text = (TextView)rootView.findViewById(R.id.Start_date_Pick_text);
        End_date_text = (TextView)rootView.findViewById(R.id.End_date_Pick_text);

        Start_date = (LinearLayout) rootView.findViewById(R.id.Start_date_Pick_Layout);
        Start_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog.OnDateSetListener callback = new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        Start_date_text.setText(year + "/" + (monthOfYear+1) + "/" + dayOfMonth);
                    }
                };

                datePickerDialog = new DatePickerDialog(view.getContext(), android.R.style.Theme_Material_Light_Dialog_Alert, callback, 2018, 0, 19);
                datePickerDialog.show();
            }
        });

        End_date  = (LinearLayout) rootView.findViewById(R.id.End_date_Pick_Layout);
        End_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog.OnDateSetListener callback = new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        End_date_text.setText(year + "/" + (monthOfYear+1) + "/" + dayOfMonth);
                    }
                };

                datePickerDialog = new DatePickerDialog(view.getContext(), android.R.style.Theme_Material_Light_Dialog_Alert, callback, 2018, 0, 19);
                datePickerDialog.show();
            }
        });

        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        chart = (LineChart)view.findViewById(R.id.chart);

        // 차트의 아래 Axis
        XAxis xAxis = chart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);    // xAxis의 위치는 아래쪽
        xAxis.setTextSize(10f);                            // xAxis에 표출되는 텍스트의 크기는 10f
        xAxis.setDrawGridLines(false);                    // xAxis의 그리드 라인을 없앰

        // 차트의 왼쪽 Axis
        YAxis leftAxis = chart.getAxisLeft();
        leftAxis.setDrawGridLines(false);                // leftAxis의 그리드 라인을 없앰

        // 차트의 오른쪽 Axis
        YAxis rightAxis = chart.getAxisRight();
        rightAxis.setEnabled(false);                    // rightAxis를 비활성화 함

        LineData data = new LineData();
        chart.setData(data);                            // LineData를 셋팅함

        feedMultiple();                                    // 쓰레드를 활용하여 실시간으로 데이터
    }

    private void feedMultiple()
    {
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
                        Thread.sleep(100);        // 0.1초간 쉼
                    }catch (InterruptedException ie)
                    {
                        ie.printStackTrace();
                    }
                }
            }
        });
        thread.start();
    }

    private void addEntry()
    {
        LineData data = chart.getData();    // onCreate에서 생성한 LineData를 가져옴
        if(data != null)                    // 데이터가 비어있지 않으면
        {
            ILineDataSet set = data.getDataSetByIndex(0);    // 0번째 위치의 데이터셋을 가져옴

            if(set == null)                    // 0번에 위치한 값이 없으면
            {
                set = createSet();            // createSet을 함
                data.addDataSet(set);        // createSet을 한 set을 데이터셋에 추가함
            }

            // set의 맨 마지막에 랜덤값(30~69.99999)을 Entry로 data에 추가함
            data.addEntry(new Entry(set.getEntryCount(), (float)(Math.random() * 40) + 30f), 0);
            data.notifyDataChanged();        // data의 값 변동을 감지함

            chart.notifyDataSetChanged();                // chart의 값 변동을 감지함
            chart.setVisibleXRangeMaximum(10);            // chart에서 최대 X좌표기준으로 몇개의 데이터를 보여줄지 설정함
            chart.moveViewToX(data.getEntryCount());    // 가장 최근에 추가한 데이터의 위치로 chart를 이동함
        }
    }

    private LineDataSet createSet() {

        LineDataSet set = new LineDataSet(null, "Dynamic Data");    // 데이터셋의 이름을 "Dynamic Data"로 설정(기본 데이터는 null)
        set.setAxisDependency(YAxis.AxisDependency.LEFT);            // Axis를 YAxis의 LEFT를 기본으로 설정
        set.setColor(ColorTemplate.getHoloBlue());                    // 데이터의 라인색을 HoloBlue로 설정
        set.setCircleColor(Color.WHITE);                            // 데이터의 점을 WHITE로 설정
        set.setLineWidth(2f);                                        // 라인의 두께를 2f로 설정
        set.setCircleRadius(4f);                                    // 데이터 점의 반지름을 4f로 설정
        set.setFillAlpha(65);                                        // 투명도 채우기를 65로 설정
        set.setFillColor(ColorTemplate.getHoloBlue());                // 채우기 색을 HoloBlue로 설정
        set.setHighLightColor(Color.rgb(244, 117, 117));            // 하이라이트 컬러(선택시 색)을 rgb(244, 117, 117)로 설정
        set.setDrawValues(false);                                    // 각 데이터의 값을 텍스트로 나타내지 않게함(false)
        return set;                                                    // 이렇게 생성한 set을 반환
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