package com.example.khseob0715.sanfirst.Activity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.khseob0715.sanfirst.R;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Calendar;


public class SignUPActivity extends AppCompatActivity implements Button.OnClickListener{
    private RadioButton maleRadio, femaleRadio;
    private TextView selected_date, timeText;
    private Button Duplicate_Btn, Send_Email_Btn, Admit_Btn, SignUP_Btn;
    private DatePickerDialog datePickerDialog;
    private LinearLayout Verification_Layout, Personal_Layout;
    private int StartTimeInt = 180;

    private Handler Timelimit;

    private EditText User_email, password, Fname, Lname, phone_num;

    private int gender = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        //Button
        maleRadio = (RadioButton)findViewById(R.id.radioButton);
        maleRadio.setOnClickListener(this);

        femaleRadio = (RadioButton)findViewById(R.id.radioButton2);
        femaleRadio.setOnClickListener(this);

        selected_date = (TextView)findViewById(R.id.selected_date);
        selected_date.setOnClickListener(this);

        Duplicate_Btn = (Button)findViewById(R.id.Duplicate_btn);
        Duplicate_Btn.setOnClickListener(this);

        Send_Email_Btn = (Button)findViewById(R.id.Send_Email_btn);
        Send_Email_Btn.setOnClickListener(this);
        Send_Email_Btn.setVisibility(View.GONE);

        Admit_Btn = (Button)findViewById(R.id.admitBtn);
        Admit_Btn.setOnClickListener(this);

        SignUP_Btn = (Button)findViewById(R.id.SignUPBtn);
        SignUP_Btn.setOnClickListener(this);

        Verification_Layout = (LinearLayout)findViewById(R.id.Verification_Code_Layout);
        Verification_Layout.setVisibility(View.INVISIBLE);

        Personal_Layout = (LinearLayout)findViewById(R.id.Personal_inform_Layout);
        Personal_Layout.setVisibility(View.INVISIBLE);

        timeText = (TextView)findViewById(R.id.TimeText);

        // ------------EditText find
        User_email = (EditText)findViewById(R.id.input_email);
        password = (EditText)findViewById(R.id.input_PW);
        Fname = (EditText)findViewById(R.id.input_firstname);
        Lname = (EditText)findViewById(R.id.input_lastname);
        // selected_date 이미 선언됨
        phone_num = (EditText)findViewById(R.id.input_phone);

        Timelimit = new Handler();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.radioButton:
                maleRadio.setChecked(true);
                femaleRadio.setChecked(false);
                gender = 0;
                break;

            case R.id.radioButton2:
                femaleRadio.setChecked(true);
                maleRadio.setChecked(false);
                gender = 1;
                break;

            case R.id.selected_date:
                OnDateSetListener callback = new OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        selected_date.setText(year + "/" + (monthOfYear+1) + "/" + dayOfMonth);
                    }
                };
                datePickerDialog = new DatePickerDialog(view.getContext(), android.R.style.Theme_Material_Light_Dialog_Alert, callback, 2018, 0, 19);
                datePickerDialog.show();
                break;

            case R.id.Duplicate_btn:
                //Dialog
                new AlertDialog.Builder(SignUPActivity.this)
                        .setTitle("E-mail")
                        .setMessage("Email available.")
                        .setNegativeButton("Admit", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                Send_Email_Btn.setVisibility(View.VISIBLE);
                            }
                        })
                        .show();
                break;

            case R.id.Send_Email_btn:
                //Dialog
                new AlertDialog.Builder(SignUPActivity.this)
                        .setTitle("E-mail Verification")
                        .setMessage("verification code sent to e-mail. Please enter it in 3 minutes.")
                        .setNegativeButton("Admit", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                Verification_Layout.setVisibility(View.VISIBLE);
                                Timelimit.postDelayed(new TimeLimit(),1000);
                            }
                        })
                        .show();
                //

                break;

            case R.id.admitBtn:
                //Dialog match
                Timelimit.removeMessages(0); // Handler stop

                new AlertDialog.Builder(SignUPActivity.this)
                        .setTitle("Verification code")
                        .setMessage("The Verification code is correct")
                        .setNegativeButton("Admit", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                Personal_Layout.setVisibility(View.VISIBLE);
                            }
                        })
                        .show();


                break;
            case R.id.SignUPBtn:
                String id = User_email.getText().toString();
                String pw = password.getText().toString();
                String fname = Fname.getText().toString();
                String lname = Lname.getText().toString();
                String date = selected_date.getText().toString();
                String phone = phone_num.getText().toString();

                SignupToDB(id, pw, fname, lname, gender, date, phone);



                /*
                //-----------------------under dialog is up when sign success
                //Dialog
                new AlertDialog.Builder(SignUPActivity.this)
                        .setTitle("Sign-UP")
                        .setMessage("Sign-UP Complete")
                        .setNegativeButton("Admit", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                finish();
                            }
                        })
                        .show();
                        */
                break;
        }
    }

    private void SignupToDB(String User_email, String pw, String Fname, String Lname, int gender, String birth, String phone) {
        class SignupData extends AsyncTask<String, Void, String> {
            ProgressDialog loading;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(SignUPActivity.this, "Please Wait", null, true, true);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                Toast.makeText(getApplicationContext(), "Sign up Success", Toast.LENGTH_LONG).show();
            }

            @Override
            protected String doInBackground(String... params) {
                String User_email = (String) params[0];
                String pw = (String) params[1];
                String Fname = (String) params[2];
                String Lname = (String) params[3];
                String gender = (String)params[4];
                String birth = (String)params[5];
                String phone = (String) params[6];
                String signup_date = String.valueOf(Calendar.getInstance().getTime());

                String ServerURL = "http://서버주소/insert.php";
                String postParameter = "User_email=" + User_email;
                postParameter += "&pw=" + pw;
                postParameter += "&Fname=" + Fname;
                postParameter += "&Lname=" + Lname;
                postParameter += "&gender=" + gender;
                postParameter += "&birth=" + birth;
                postParameter += "&phone=" + phone;
                postParameter += "&signup_date=" + signup_date;

                try {
                    URL url = new URL(ServerURL);
                    HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                    httpURLConnection.setReadTimeout(5000);
                    httpURLConnection.setConnectTimeout(5000);
                    httpURLConnection.setRequestMethod("POST");
                    //httpURLConnection.setRequestProperty("content-type", "application/json");
                    httpURLConnection.setDoInput(true);
                    httpURLConnection.connect();

                    OutputStream outputStream = httpURLConnection.getOutputStream();
                    outputStream.write(postParameter.getBytes("UTF-8"));
                    outputStream.flush();
                    outputStream.close();

                    int responseStatusCode = httpURLConnection.getResponseCode();
                    Log.d("PostResponse", "POST response code - " + responseStatusCode);

                    InputStream inputStream;
                    if(responseStatusCode == HttpURLConnection.HTTP_OK) {
                        inputStream = httpURLConnection.getInputStream();
                    }
                    else{
                        inputStream = httpURLConnection.getErrorStream();
                    }


                    InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "UTF-8");
                    BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

                    StringBuilder sb = new StringBuilder();
                    String line = null;

                    while((line = bufferedReader.readLine()) != null){
                        sb.append(line);
                    }


                    bufferedReader.close();


                    return sb.toString();


                } catch (Exception e) {

                    Log.d("InserDataError", "InsertData: Error ", e);

                    return new String("Error: " + e.getMessage());
                }
            }
        }
    }

    private class TimeLimit implements Runnable {
        @Override
        public void run() {
            StartTimeInt -= 1;
            timeText.setText((StartTimeInt % 3600 / 60) + ":" + (StartTimeInt % 3600 % 60));
            if(StartTimeInt > 0) {
                Timelimit.postDelayed(new TimeLimit(), 1000);
            }else{
                new AlertDialog.Builder(SignUPActivity.this)
                        .setTitle("Time Out")
                        .setMessage("Validation code input timeout")
                        .setNegativeButton("Admit", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                finish();
                            }
                        })
                        .show();
            }
        }
    }
}


