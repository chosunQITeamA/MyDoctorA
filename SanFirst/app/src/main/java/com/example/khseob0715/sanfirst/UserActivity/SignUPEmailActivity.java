package com.example.khseob0715.sanfirst.UserActivity;

import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.khseob0715.sanfirst.R;

public class SignUPEmailActivity extends AppCompatActivity implements Button.OnClickListener{

    private Button Send_email;

    private EditText Email_text;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup_email);

        Send_email = (Button)findViewById(R.id.Send_Email_btn);
        Send_email.setOnClickListener(this);

        Email_text = (EditText)findViewById(R.id.input_email);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.Send_Email_btn:
                Intent SignUPCode = new Intent(getApplicationContext(),SignUPCodeActivity.class);

                SignUPCode.putExtra("Email", String.valueOf(Email_text.getText()));
                startActivity(SignUPCode);
                overridePendingTransition(0, 0);
                finish();
                break;
        }
    }
}
