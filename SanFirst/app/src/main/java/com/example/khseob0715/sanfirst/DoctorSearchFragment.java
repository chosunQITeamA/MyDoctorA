package com.example.khseob0715.sanfirst;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import static android.R.layout.simple_spinner_dropdown_item;


/**
 * A simple {@link Fragment} subclass.
 */
public class DoctorSearchFragment extends Fragment {


    public DoctorSearchFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        ViewGroup rootView = (ViewGroup)inflater.inflate(R.layout.fragment_search_doctor, container, false);

        Spinner spinner = (Spinner)rootView.findViewById(R.id.GenderSpinner);

       // ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item);

        spinner = (Spinner)rootView.findViewById(R.id.GenderSpinner);
        ArrayAdapter genderAdapter = ArrayAdapter.createFromResource(getContext(), R.array.gender, android.R.layout.simple_spinner_item);
        genderAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(genderAdapter);

        return rootView;
    }

}
