package com.example.khseob0715.sanfirst.UserActivity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.LinearLayout;

import com.example.khseob0715.sanfirst.R;

/**
 * Created by khseob0715 on 2018-02-01.
 */

public class FindPWNewActivity extends AppCompatActivity {
    private LinearLayout verifyLayout, updatePWLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_findpasswordnew);

    }


    public void FPW_Update_Password(View view) {
        finish();
    }
}
