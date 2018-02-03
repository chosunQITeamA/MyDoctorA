package com.example.khseob0715.sanfirst.navi_fragment;


import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.RadioButton;

import com.example.khseob0715.sanfirst.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class Fragment_Profile extends Fragment {

    Button BirthSelectBtn;
    DatePickerDialog datePickerDialog;

    RadioButton maleRadio, femaleRadio;
    public Fragment_Profile() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        ViewGroup rootView = (ViewGroup)inflater.inflate(R.layout.fragment_profile, container, false);


        maleRadio = (RadioButton)rootView.findViewById(R.id.maleRadio);
        maleRadio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                maleRadio.setChecked(true);
                femaleRadio.setChecked(false);
            }
        });

        femaleRadio = (RadioButton)rootView.findViewById(R.id.femaleRadio);
        femaleRadio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                maleRadio.setChecked(false);
                femaleRadio.setChecked(true);
            }
        });

        BirthSelectBtn = (Button)rootView.findViewById(R.id.Birth_select);
        BirthSelectBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog.OnDateSetListener callback = new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        BirthSelectBtn.setText(year + "/" + (monthOfYear+1) + "/" + dayOfMonth);
                    }
                };

                datePickerDialog = new DatePickerDialog(view.getContext(), android.R.style.Theme_Material_Light_Dialog_Alert, callback, 2018, 0, 19);
                datePickerDialog.show();
            }
        });



        return rootView;

    }

}
