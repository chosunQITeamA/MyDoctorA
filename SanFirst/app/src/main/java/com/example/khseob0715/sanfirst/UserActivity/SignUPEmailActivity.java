package com.example.khseob0715.sanfirst.UserActivity;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.khseob0715.sanfirst.R;
import com.example.khseob0715.sanfirst.ServerConn.SignUp;

public class SignUPEmailActivity extends AppCompatActivity implements Button.OnClickListener{

    public SignUp signup = new SignUp();

    private Button Send_email;

    private EditText Email_text;

    public static Context SignupEmailContext;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup_email);

        Send_email = (Button)findViewById(R.id.Send_Email_btn);
        Send_email.setOnClickListener(this);

        Email_text = (EditText)findViewById(R.id.input_email);

        SignupEmailContext = this;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.Send_Email_btn:
                signup.signup_email_Asycn(Email_text.getText().toString());

                break;
        }
    }
}
