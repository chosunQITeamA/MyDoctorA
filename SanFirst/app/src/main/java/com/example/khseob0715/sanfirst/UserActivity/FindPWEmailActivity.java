package com.example.khseob0715.sanfirst.UserActivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
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

    public static int isUser_id = 0;

    public static Context FindPWEmailContext;

    public static Activity FindPWActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_findpasswordemail);

        FindPWActivity = FindPWEmailActivity.this;

        email = (EditText) findViewById(R.id.email_editText);

        FindPWEmailContext = this;

        if(isUser_id == 1) {
            new AlertDialog.Builder(this)
                    .setTitle("E-mail Error")
                    .setMessage("This email is not signed up")
                    .setNegativeButton("Admit",null)
                    .setCancelable(false)
                    .show();
            isUser_id = 0;
        }
    }

    public void FPW_Send_Email(View view) {
        findpw_email.findpw_Asycn(email.getText().toString());
    }
}
