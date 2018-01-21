package com.example.khseob0715.sanfirst;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.RadioButton;

public class LoginActivity extends AppCompatActivity {
    RadioButton selectUser, selectDoctor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.loginactivity_main);

        // find
        selectUser = (RadioButton)findViewById(R.id.UserSelect);
        selectDoctor = (RadioButton)findViewById(R.id.DoctorSelect);

    }



    public void SignIN(View view) { // 로그인
        this.finish();
    }
    public void SignUP(View view) {  // 가입
        Intent SignUPIntent = new Intent(getApplicationContext(),SignUPActivity.class);
        startActivity(SignUPIntent);
    }

    public void usercheck(View view) {
        selectUser.setChecked(true);
        selectDoctor.setChecked(false);
    }

    public void doctorcheck(View view) {
        selectUser.setChecked(false);
        selectDoctor.setChecked(true);
    }
}
