package com.example.khseob0715.sanfirst.UserActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.khseob0715.sanfirst.R;
import com.example.khseob0715.sanfirst.ServerConn.ChangePW;

/**
 * Created by khseob0715 on 2018-02-01.
 */

public class FindPWNewActivity extends AppCompatActivity {
    private TextView emailT;
    private EditText PW1, PW2;

    private int usn = 0;
    private String email = null;

    ChangePW changepw = new ChangePW();

    public static Context FindPWNewContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_findpasswordnew);

        Intent intent = getIntent();
        usn = intent.getIntExtra("usn", 0);
        email = intent.getStringExtra("email");

        emailT = (TextView)findViewById(R.id.emailText);

        emailT.setText(email);

        PW1 = (EditText)findViewById(R.id.newPWText);
        PW2 = (EditText)findViewById(R.id.confirmPWText);

        FindPWNewContext = this;

    }

    public void FPW_Update_Password(View view) {
        if(PW1.getText().toString().equals(PW2.getText().toString()))   {
            changepw.changepw_Asycn(usn, PW1.getText().toString());
            finish();
        }   else    {
            Toast.makeText(this, "Password is wrong", Toast.LENGTH_SHORT).show();
        }

    }
}
