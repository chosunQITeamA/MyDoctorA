package com.example.khseob0715.sanfirst;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.loginactivity_main);
    }

    public void SignIN(View view) { // 로그인
        this.finish();
    }
    public void SignUP(View view) {  // 가입
        Intent SignUPIntent = new Intent(getApplicationContext(),SignUPActivity.class);
        startActivity(SignUPIntent);
    }

}
