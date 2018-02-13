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

public class FindPWCodeActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_findpassword);

    }

    public void FPW_Send_Verify_Code(View view) {
        Intent intent = new Intent(getApplicationContext(),FindPWNewActivity.class);
        startActivity(intent);
        overridePendingTransition(0, 0);
        finish();
    }
}
