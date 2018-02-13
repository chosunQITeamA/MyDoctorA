package com.example.khseob0715.sanfirst.UserActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
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

    private TextView emailT,timeText;
    ;
    private EditText verifycode;

    private Handler Timelimit;


    private int StartTimeInt = 180;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_findpassword);

        emailT = (TextView)findViewById(R.id.email_editText);
        verifycode = (EditText)findViewById(R.id.verificationCode);

        timeText = (TextView)findViewById(R.id.threeTimelimit);

        Intent intent = getIntent();
        usn = intent.getIntExtra("usn", 0);
        email = intent.getStringExtra("email");
        code = intent.getStringExtra("code");

        emailT.setText(email);

        Timelimit = new Handler();
        Timelimit.postDelayed(new TimeLimit(),1000);
    }

    public void FPW_Send_Verify_Code(View view) {

        if(verifycode.getText().toString().equals(code))    {
            Intent intent = new Intent(getApplicationContext(),FindPWNewActivity.class);
            intent.putExtra("usn", usn);
            intent.putExtra("email", email);
            Timelimit.removeMessages(0); // Handler stop
            startActivity(intent);
            overridePendingTransition(0, 0);
            finish();
        }   else    {
            Log.e("verify code error", verifycode.getText().toString() +" / "+ code);
        }

    }

    private class TimeLimit implements Runnable {
        @Override
        public void run() {
            StartTimeInt -= 1;
            timeText.setText((StartTimeInt % 3600 / 60) + ":" + (StartTimeInt % 3600 % 60));
            if(StartTimeInt > 0) {
                Timelimit.postDelayed(new TimeLimit(), 1000);
            }else{
//                Timelimit.removeMessages(0); // Handler stop
//                new AlertDialog.Builder(this)
//                        .setTitle("Time Out")
//                        .setMessage("Validation code input timeout")
//                        .setNegativeButton("Admit", new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialogInterface, int i) {
//                                finish();
//                            }
//                        })
//                        .show();
                finish();
            }
        }
    }
}
