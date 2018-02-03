package com.example.khseob0715.sanfirst.Activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.LinearLayout;

import com.example.khseob0715.sanfirst.R;

/**
 * Created by khseob0715 on 2018-02-01.
 */

public class FindPWActivity extends AppCompatActivity {
    LinearLayout verifyLayout, updatePWLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.findpassword_activity);

        verifyLayout = (LinearLayout)findViewById(R.id.VerifyCodeLayout);
        updatePWLayout = (LinearLayout)findViewById(R.id.UpdatePasswordLayout);

        verifyLayout.setVisibility(View.INVISIBLE);
        updatePWLayout.setVisibility(View.INVISIBLE);
    }
    public void FPW_Send_Email(View view) {
        verifyLayout.setVisibility(View.VISIBLE);
    }

    public void FPW_Send_Verify_Code(View view) {
        updatePWLayout.setVisibility(View.VISIBLE);
    }

    public void FPW_Update_Password(View view) {
        finish();
    }




}
