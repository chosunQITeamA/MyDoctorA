package com.example.khseob0715.sanfirst.navi_fragment;


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
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.khseob0715.sanfirst.R;


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



        return rootView;



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
