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
import android.widget.Toast;

import com.example.khseob0715.sanfirst.R;

public class SignUPCodeActivity extends AppCompatActivity implements Button.OnClickListener{
    private Button AdmitBtn;
    private TextView timeText,emailText;
    private EditText verificationCode;
    private Handler Timelimit;

    private int StartTimeInt = 180;
    private String e_mail, code;
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
                    Intent SignUP = new Intent(getApplicationContext(),SignUPActivity.class);
                    SignUP.putExtra("Email",e_mail);
                    startActivity(SignUP);
                    Timelimit.removeMessages(0); // Handler stop
                    finish();
                    break;
                }   else    {
                    Toast.makeText(this, "Verification Code is Wrong", Toast.LENGTH_SHORT).show();
                }

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
                new AlertDialog.Builder(getApplicationContext())
                        .setTitle("Time Out")
                        .setMessage("Validation code input timeout")
                        .setNegativeButton("Admit", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                finish();
                            }
                        })
                        .show();
            }
        }
    }
}
