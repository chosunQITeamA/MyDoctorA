package com.example.khseob0715.sanfirst.UserActivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.khseob0715.sanfirst.R;
import com.example.khseob0715.sanfirst.ServerConn.SignUp;

public class SignUPEmailActivity extends AppCompatActivity implements Button.OnClickListener{

    public SignUp signup = new SignUp();
    public static int Duplicate_check = 0;
    private Button Send_email;

    private EditText Email_text;

    public static Context SignupEmailContext;

    public static Activity AActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup_email);

        AActivity = SignUPEmailActivity.this;

        Send_email = (Button)findViewById(R.id.Send_Email_btn);
        Send_email.setOnClickListener(this);

        Email_text = (EditText)findViewById(R.id.input_email);

        SignupEmailContext = this;

        if(Duplicate_check == 1){
            new AlertDialog.Builder(this)
                    .setTitle("E-mail entry Error")
                    .setMessage("Email is invalid or already taken")
                    .setNegativeButton("Admit",null)
                    .setCancelable(false)
                    .show();

            Duplicate_check = 0;
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.Send_Email_btn:
                String e_mail_text = Email_text.getText().toString();
                if(!e_mail_text.equals("")) {
                    signup.signup_email_Asycn(e_mail_text);
                }else{
                    new AlertDialog.Builder(this)
                            .setTitle("E-mail entry Error")
                            .setMessage("No E-mail was entered. \nPlease re-enter")
                            .setNegativeButton("Admit",null)
                            .setCancelable(false)
                            .show();
                }
                break;
        }
    }
}
