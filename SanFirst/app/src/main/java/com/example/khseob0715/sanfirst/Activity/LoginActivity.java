package com.example.khseob0715.sanfirst.Activity;

import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;

import com.example.khseob0715.sanfirst.R;
import com.example.khseob0715.sanfirst.navi_fragment.Fragment_Main;
import com.example.khseob0715.sanfirst.udoo_btchat.BluetoothChatService;

public class LoginActivity extends FragmentActivity implements Button.OnClickListener{
    RadioButton selectUser, selectDoctor;
    private BluetoothAdapter mBluetoothAdapter = null;
    private BluetoothChatService mChatService = null;

    private static final int REQUEST_CONNECT_DEVICE_SECURE = 1;
    private static final int REQUEST_CONNECT_DEVICE_INSECURE = 2;
    private static final int REQUEST_ENABLE_BT = 3;

    int changeIntent = 0;

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

        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        confirmBTonoff();

    }
    public void SignIN(View view) { // 로그인
        //this.finish();

        // 입력한 정보가 일치 할 경우 넘김. // 아닐 경우 메시지 다이얼 로그 출력

        // 일반 사용자와 의사를 구분하여 넘긴다.
        // 1/21 지금은 그냥 넘김.

        Intent UserMainIntent = new Intent(getApplicationContext(),UserMainActivity.class);
        startActivity(UserMainIntent);
        // If the adapter is null, then Bluetooth is not supported
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

    private void confirmBTonoff() {
        // If BT is not on, request that it be enabled.
        // setupChat() will then be called during onActivityResult
        if (!mBluetoothAdapter.isEnabled()) {
            Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableIntent, REQUEST_ENABLE_BT);
            changeIntent = 1;
            // Otherwise, setup the chat session
        } else if (mChatService == null) {
        }
    }
}
