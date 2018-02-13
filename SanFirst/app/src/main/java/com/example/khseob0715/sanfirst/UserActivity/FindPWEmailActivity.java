package com.example.khseob0715.sanfirst.UserActivity;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;

import com.example.khseob0715.sanfirst.R;
import com.example.khseob0715.sanfirst.ServerConn.FindPW_Email;

/**
 * Created by khseob0715 on 2018-02-01.
 */

public class FindPWEmailActivity extends AppCompatActivity {

    FindPW_Email findpw_email = new FindPW_Email();
    private EditText email;

    public static Context FindPWEmailContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_findpasswordemail);

        email = (EditText) findViewById(R.id.email_editText);

        FindPWEmailContext = this;
    }

    public void FPW_Send_Email(View view) {
        findpw_email.findpw_Asycn(email.getText().toString());
    }
}
