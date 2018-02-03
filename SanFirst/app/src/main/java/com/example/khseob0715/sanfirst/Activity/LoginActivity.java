package com.example.khseob0715.sanfirst.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;

import com.example.khseob0715.sanfirst.R;
import com.example.khseob0715.sanfirst.navi_fragment.Fragment_Main;

public class LoginActivity extends FragmentActivity implements Button.OnClickListener{
    RadioButton selectUser, selectDoctor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loginmain);

        if (savedInstanceState == null) {
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            Fragment_Main fragment = new Fragment_Main();
        }

        // find
        selectUser = (RadioButton)findViewById(R.id.UserSelect);
        selectUser.setOnClickListener(this);
        selectDoctor = (RadioButton)findViewById(R.id.DoctorSelect);
        selectDoctor.setOnClickListener(this);

    }
    public void SignIN(View view) { // 로그인
        //this.finish();

        // 입력한 정보가 일치 할 경우 넘김. // 아닐 경우 메시지 다이얼 로그 출력

        // 일반 사용자와 의사를 구분하여 넘긴다.
        // 1/21 지금은 그냥 넘김.
        Intent UserMainIntent = new Intent(getApplicationContext(),UserMainActivity.class);
        startActivity(UserMainIntent);
    }

    public void SignUP(View view) {  // 가입
        Intent SignUPIntent = new Intent(getApplicationContext(),SignUPActivity.class);
        startActivity(SignUPIntent);
    }

    public void FindPW(View view) {
        Intent FindPWIntent = new Intent(getApplicationContext(),FindPWActivity.class);
        startActivity(FindPWIntent);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.UserSelect:
                selectUser.setChecked(true);
                selectDoctor.setChecked(false);
                break;
            case R.id.DoctorSelect:
                selectUser.setChecked(false);
                selectDoctor.setChecked(true);
                break;
        }
    }
}
