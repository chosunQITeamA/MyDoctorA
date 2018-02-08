package com.example.khseob0715.sanfirst.navi_fragment;


import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.example.khseob0715.sanfirst.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class Fragment_AQIHistory extends Fragment {

    private ViewGroup rootView;
    private ProgressBar PM25_Bar,CO_Bar,NO2_Bar,SO2_Bar,O3_Bar;

    public Fragment_AQIHistory() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        rootView = (ViewGroup) inflater.inflate(R.layout.fragment_aqi_history, container, false);

        PM25_Bar = (ProgressBar)rootView.findViewById(R.id.PM25_progress);
        PM25_Bar.getProgressDrawable().setColorFilter(Color.RED, android.graphics.PorterDuff.Mode.SRC_IN);
        PM25_Bar.setProgress(50);

        CO_Bar = (ProgressBar)rootView.findViewById(R.id.CO_progress);
        CO_Bar.getProgressDrawable().setColorFilter(Color.RED, android.graphics.PorterDuff.Mode.SRC_IN);
        CO_Bar.setProgress(50);

        NO2_Bar = (ProgressBar)rootView.findViewById(R.id.NO2_progress);
        NO2_Bar.getProgressDrawable().setColorFilter(Color.RED, android.graphics.PorterDuff.Mode.SRC_IN);
        NO2_Bar.setProgress(50);

        SO2_Bar = (ProgressBar)rootView.findViewById(R.id.SO2_progress);
        SO2_Bar.getProgressDrawable().setColorFilter(Color.RED, android.graphics.PorterDuff.Mode.SRC_IN);
        SO2_Bar.setProgress(50);

        O3_Bar = (ProgressBar)rootView.findViewById(R.id.O3_progress);
        O3_Bar.getProgressDrawable().setColorFilter(Color.RED, android.graphics.PorterDuff.Mode.SRC_IN);
        O3_Bar.setProgress(50);



        return rootView;



    }

}
