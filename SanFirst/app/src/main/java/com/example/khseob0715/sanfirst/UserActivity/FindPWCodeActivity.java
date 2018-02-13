package com.example.khseob0715.sanfirst.UserActivity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.example.khseob0715.sanfirst.R;

/**
 * Created by khseob0715 on 2018-02-01.
 */

public class FindPWCodeActivity extends AppCompatActivity {

    int usn = 0;
    String email, code = null;

    private TextView emailT;
    private EditText verifycode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_findpassword);

        emailT = (TextView)findViewById(R.id.email_editText);
        verifycode = (EditText)findViewById(R.id.verificationCode);

        Intent intent = getIntent();
        usn = intent.getIntExtra("usn", 0);
        email = intent.getStringExtra("email");
        code = intent.getStringExtra("code");

        emailT.setText(email);
    }

    public void FPW_Send_Verify_Code(View view) {

        if(verifycode.getText().toString().equals(code))    {
            Intent intent = new Intent(getApplicationContext(),FindPWNewActivity.class);
            intent.putExtra("usn", usn);
            intent.putExtra("email", email);
            startActivity(intent);
            overridePendingTransition(0, 0);
            finish();
        }   else    {
            Log.e("verify code error", verifycode.getText().toString() +" / "+ code);
        }

    }
}
