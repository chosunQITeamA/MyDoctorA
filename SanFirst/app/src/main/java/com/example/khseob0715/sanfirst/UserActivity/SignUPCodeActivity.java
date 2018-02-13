package com.example.khseob0715.sanfirst.UserActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.khseob0715.sanfirst.R;
import com.example.khseob0715.sanfirst.ServerConn.SignUp;

public class SignUPCodeActivity extends AppCompatActivity implements Button.OnClickListener{
    private Button AdmitBtn;
    private TextView timeText,emailText;
    private EditText verificationCode;
    private Handler Timelimit;

    private int StartTimeInt = 180;
    private String e_mail, code;

    SignUp signup = new SignUp();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup_code);

        verificationCode = (EditText)findViewById(R.id.input_verifactioncode);
        AdmitBtn = (Button)findViewById(R.id.admitBtn);
        AdmitBtn.setOnClickListener(this);

        timeText = (TextView)findViewById(R.id.TimeText);

        Timelimit = new Handler();
        Timelimit.postDelayed(new TimeLimit(),1000);

        new AlertDialog.Builder(this)
                .setTitle("Send an E-mail")
                .setMessage("An email was sent containing \nthe verification code. \nEnter verification code in 3 minutes.")
                .setNegativeButton("Admit",null)
                .setCancelable(false)
                .show();

        emailText = (TextView)findViewById(R.id.EmailText);

        Intent intent = getIntent();
        e_mail = intent.getStringExtra("email");
        code = intent.getStringExtra("code");

        Log.e("SignUPCode = ", e_mail + "/" + code);

        emailText.setText(e_mail);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.admitBtn:
                if(verificationCode.getText().toString().equals(code))   {
                    Timelimit.removeMessages(0); // Handler stop
                    new AlertDialog.Builder(this)
                            .setTitle("Check Verification code")
                            .setMessage("Verification code match")
                            .setNegativeButton("Admit", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    Intent SignUP = new Intent(getApplicationContext(),SignUPActivity.class);
                                    SignUP.putExtra("Email",e_mail);
                                    startActivity(SignUP);
                                    overridePendingTransition(0, 0);
                                    finish();
                                }
                            })
                            .setCancelable(false)
                            .show();
                } else {
                    new AlertDialog.Builder(this)
                            .setTitle("Verification Code Error")
                            .setMessage("Verification code is wrong. \nPlease re-enter")
                            .setNegativeButton("Admit",null)
                            .setPositiveButton("Resend", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    finish();
                                    signup.signup_email_Asycn(e_mail);
                                }
                            })
                            .setCancelable(false)
                            .show();
                }
                break;
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
