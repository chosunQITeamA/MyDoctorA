package com.example.khseob0715.sanfirst;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.DatePicker;
import android.widget.RadioButton;
import android.widget.TextView;


public class SignUPActivity extends AppCompatActivity {
    private RadioButton maleRadio, femaleRadio;
    private TextView selected_date;

    private DatePickerDialog datePickerDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup_main);

        maleRadio = (RadioButton)findViewById(R.id.radioButton);
        femaleRadio = (RadioButton)findViewById(R.id.radioButton2);
        selected_date = (TextView)findViewById(R.id.selected_date);
    }


    public void male_check(View view) {
            maleRadio.setChecked(true);
            femaleRadio.setChecked(false);
    }
    public void female_check(View view) {
            femaleRadio.setChecked(true);
            maleRadio.setChecked(false);
    }

    public void showDatePickerDialog(View v) {
        OnDateSetListener callback = new OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                selected_date.setText(year + "/" + (monthOfYear+1) + "/" + dayOfMonth);
            }
        };

        datePickerDialog = new DatePickerDialog(v.getContext(), android.R.style.Theme_Material_Light_Dialog_Alert, callback, 2018, 0, 19);
        datePickerDialog.show();
    }
}


