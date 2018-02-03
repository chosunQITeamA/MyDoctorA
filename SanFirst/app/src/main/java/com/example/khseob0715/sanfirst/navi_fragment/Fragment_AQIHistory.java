package com.example.khseob0715.sanfirst.navi_fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.khseob0715.sanfirst.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class Fragment_AQIHistory extends Fragment {


    public Fragment_AQIHistory() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_aqi_history, container, false);
    }

}
