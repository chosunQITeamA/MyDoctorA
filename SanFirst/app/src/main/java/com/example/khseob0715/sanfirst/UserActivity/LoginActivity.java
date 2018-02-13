package com.example.khseob0715.sanfirst.UserActivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;

import com.example.khseob0715.sanfirst.R;
import com.example.khseob0715.sanfirst.ServerConn.SignIn;
import com.example.khseob0715.sanfirst.udoo_btchat.BluetoothChatService;

public class LoginActivity extends FragmentActivity implements Button.OnClickListener{
    private RadioButton selectUser, selectDoctor;
    private Button Sign_in_Btn, Sign_up_Btn;
    private EditText inputID, inputPW;
    private TextView Find_PW_Text;
    private BluetoothAdapter mBluetoothAdapter = null;
    private BluetoothChatService mChatService = null;

    private static final int REQUEST_CONNECT_DEVICE_SECURE = 1;
    private static final int REQUEST_CONNECT_DEVICE_INSECURE = 2;
    private static final int REQUEST_ENABLE_BT = 3;

    private int who = 0;
    private int changeIntent = 0;

    public static Context LoginContext;
    public SignIn signIn = new SignIn();

    public static int sign_in_complete = 1;
    public static int Password_change_complete = 0;
    public static int Delete_complete = 0;

    public static Activity tempLoginActivity;

    public static String Id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loginmain);

        tempLoginActivity = LoginActivity.this;

        // find
        selectUser = (RadioButton)findViewById(R.id.UserSelect);
        selectUser.setOnClickListener(this);
        selectDoctor = (RadioButton)findViewById(R.id.DoctorSelect);
        selectDoctor.setOnClickListener(this);

        Sign_in_Btn = (Button)findViewById(R.id.sign_in_btn);
        Sign_in_Btn.setOnClickListener(this);

        Sign_up_Btn = (Button)findViewById(R.id.sign_up_btn);
        Sign_up_Btn.setOnClickListener(this);

        Find_PW_Text = (TextView)findViewById(R.id.FindPasswordText);
        Find_PW_Text.setOnClickListener(this);

        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        inputID = (EditText)findViewById(R.id.idEditText);
        inputPW = (EditText)findViewById(R.id.pwEditText);

        if (savedInstanceState == null) {
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        }

        if(sign_in_complete == 0){
            new AlertDialog.Builder(this)
                    .setTitle("Sign In Error")
                    .setMessage("Incorrect username or password.")
                    .setNegativeButton("Admit",null)
                    .setCancelable(false)
                    .show();
            inputID.setText(Id);
            sign_in_complete = 1;
        }


        if(Password_change_complete == 1){
            new AlertDialog.Builder(this)
                    .setTitle("Password Change")
                    .setMessage("Password has changed. \nPlease sign-in again.")
                    .setNegativeButton("Admit",null)
                    .setCancelable(false)
                    .show();
            inputID.setText(Id);
            Password_change_complete = 0;
        }

        if(Delete_complete == 1){
            new AlertDialog.Builder(this)
                    .setTitle("Delete Account")
                    .setMessage("ID cancel complete")
                    .setNegativeButton("Admit",null)
                    .setCancelable(false)
                    .show();
        }

        LoginContext = this;

        confirmBTonoff();
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.UserSelect:
                selectUser.setChecked(true);
                selectDoctor.setChecked(false);
                who = 0;
                break;

            case R.id.DoctorSelect:
                selectUser.setChecked(false);
                selectDoctor.setChecked(true);
                who = 1;
                break;

            case R.id.sign_in_btn:
                Id = inputID.getText().toString();
                String PW = inputPW.getText().toString();

                signIn.signin_Asycn(who, Id, PW);
                break;

            case R.id.sign_up_btn:
                Intent SignUPIntent = new Intent(getApplicationContext(),SignUPEmailActivity.class);
                startActivity(SignUPIntent);
                break;

            case R.id.FindPasswordText:
                Intent FindPWIntent = new Intent(getApplicationContext(),FindPWEmailActivity.class);
                startActivity(FindPWIntent);
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


    public void fake(View view) {
        Intent UserMainIntent = new Intent(getApplicationContext(),UserActivity.class);
        UserMainIntent.putExtra("usn", 0);
        startActivity(UserMainIntent);
    }
}
