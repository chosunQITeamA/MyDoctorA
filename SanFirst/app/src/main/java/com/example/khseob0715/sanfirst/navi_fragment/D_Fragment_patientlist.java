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
public class D_Fragment_patientlist extends Fragment {


    public D_Fragment_patientlist() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.d__fragment_patient__list, container, false);
    }

}
