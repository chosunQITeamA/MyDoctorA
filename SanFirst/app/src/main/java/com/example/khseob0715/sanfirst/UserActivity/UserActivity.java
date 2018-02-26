package com.example.khseob0715.sanfirst.UserActivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.text.method.PasswordTransformationMethod;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.khseob0715.sanfirst.Database.AQIHistorySQLiteHelper;
import com.example.khseob0715.sanfirst.Database.AQISQLiteHelper;
import com.example.khseob0715.sanfirst.Database.DBtoJSON;
import com.example.khseob0715.sanfirst.Database.HeartSQLiteHelper;
import com.example.khseob0715.sanfirst.GPSTracker.GPSTracker;
import com.example.khseob0715.sanfirst.PolarBLE.PolarSensor;
import com.example.khseob0715.sanfirst.R;
import com.example.khseob0715.sanfirst.ServerConn.ConfirmPW;
import com.example.khseob0715.sanfirst.ServerConn.SendAQI;
import com.example.khseob0715.sanfirst.ServerConn.SendHR;
import com.example.khseob0715.sanfirst.ServerConn.SendJSON;
import com.example.khseob0715.sanfirst.navi_fragment.D_Fragment_main;
import com.example.khseob0715.sanfirst.navi_fragment.D_Fragment_patientlist;
import com.example.khseob0715.sanfirst.navi_fragment.D_Fragment_usersearch;
import com.example.khseob0715.sanfirst.navi_fragment.Fragment_AQIHistory;
import com.example.khseob0715.sanfirst.navi_fragment.Fragment_Account;
import com.example.khseob0715.sanfirst.navi_fragment.Fragment_AirMap;
import com.example.khseob0715.sanfirst.navi_fragment.Fragment_HRHistory;
import com.example.khseob0715.sanfirst.navi_fragment.Fragment_SearchDoctor;
import com.example.khseob0715.sanfirst.navi_fragment.Fragment_TabMain;
import com.example.khseob0715.sanfirst.udoo_btchat.BluetoothAQI;
import com.example.khseob0715.sanfirst.udoo_btchat.BluetoothChatService;
import com.example.khseob0715.sanfirst.udoo_btchat.DeviceListActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

public class UserActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    public static int RENEW_GPS = 1;
    public static int SEND_PRINT = 2;

    // GPSTracker class
    GPSTracker gps = null;
    public Handler GPSHandler;

    public Double Latitude = 0.00;
    public Double Longitude = 0.00;

    //Fragment fragment = new Fragment();
    private Fragment btchatFragment;
    private Fragment hrhistoryFragment;
    private Fragment airqualfragment;
    private Fragment accountFragment;
    private Fragment profileFragment;
    private Fragment D_mainFragment;
    private Fragment D_listFragment;
    private Fragment D_searchFragment;

    public static int heartratevalue = 0;
    public static int val[] = {0,0,0,0,0};

    public static int udoo_temperature=0;
    public static int udoo_co=0;
    public static int udoo_so2=0;
    public static int udoo_o3=0;
    public static int udoo_no2=0;
    public static int udoo_pm25=0;

    private static final int REQUEST_CONNECT_DEVICE_SECURE = 1;
    private static final int REQUEST_CONNECT_DEVICE_INSECURE = 2;
    private static final int REQUEST_ENABLE_BT = 3;
    private BluetoothAdapter mBluetoothAdapter = null;
    private BluetoothChatService mChatService = null;

    private TextView Navi_ID, Navi_Name;

    public static int usn = 0;
    private String UserID, Username;

    private EditText passwordEdit_profile;

    private Thread gpsThread;

    public SQLiteDatabase db;

    public static UserActivity UserActContext;

    private String TS;
    private Double LAT;
    private Double LNG;
    private Double Heart_rate;
    private Double RR_rate;

    public boolean internetConnCheck = true;

    private TimerTask m_Task;
    private Timer m_Timer;
    ConnectivityManager manager;
    NetworkInfo mobile;
    NetworkInfo wifi;

    ConfirmPW confirmpw = new ConfirmPW();
    HeartSQLiteHelper HRsqlhelper = new HeartSQLiteHelper();
    AQISQLiteHelper AQIsqlhelper = new AQISQLiteHelper();
    AQIHistorySQLiteHelper AQIHsqlhelper = new AQIHistorySQLiteHelper();

    SendHR sendhr = new SendHR();
    SendAQI sendaqi = new SendAQI();
    HeartSQLiteHelper heartsql = new HeartSQLiteHelper();
    AQISQLiteHelper aqisql = new AQISQLiteHelper();
    AQIHistorySQLiteHelper aqihisyorysql = new AQIHistorySQLiteHelper();

    DBtoJSON dbtojson = new DBtoJSON();
    SendJSON sendjson = new SendJSON();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

      //  passwordEdit_profile = (EditText)findViewById(R.id.PWeditText4);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        GPSHandler = new Handler(){
            @Override
            public void handleMessage(Message msg){
                if(msg.what==RENEW_GPS){
                    makeNewGpsService();
                }
                if(msg.what==SEND_PRINT){
                    logPrint((String)msg.obj);
                }
            }
        };

        startGPSSubThread();

        btchatFragment = new Fragment_TabMain();
        hrhistoryFragment = new Fragment_HRHistory();
        airqualfragment = new Fragment_AirMap();
        accountFragment = new Fragment_Account();
        profileFragment = new Fragment_AirMap();

        D_mainFragment = new D_Fragment_main();
        D_listFragment = new D_Fragment_patientlist();
        D_searchFragment = new D_Fragment_usersearch();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        Menu menu = navigationView.getMenu();

        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        if(LoginActivity.who == 0) {
            // 환자용 일때는 지워야 함.
            MenuItem doctor = menu.findItem(R.id.Dr_drawer);
            doctor.setVisible(false);

            MenuItem doctor_main = menu.findItem(R.id.nav_doctor_main);
            doctor_main.setVisible(false);

            ft.replace(R.id.content_fragment_layout, btchatFragment);
        }else {
            // 의사용 일때는 지워야 함.
            MenuItem doctor_search = menu.findItem(R.id.nav_mydoc);
            doctor_search.setVisible(false);

            MenuItem user_history = menu.findItem(R.id.user_history);
            user_history.setVisible(false);

            MenuItem user_main = menu.findItem(R.id.nav_user_main);
            user_main.setVisible(false);

            ft.replace(R.id.content_fragment_layout, D_mainFragment);

        }
        ft.commit();

        viewlistBTdevice(1);
        startPolarsensor();
        DBservice();

        aqihisyorysql.AQIHcreateTable(db);

        m_Task = new TimerTask() {
            @Override
            public void run() {//실제 기능 구현
                manager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
                mobile = manager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
                wifi = manager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
                InternetConnCheck();
            }
        };
        m_Timer = new Timer();
        m_Timer.schedule(m_Task, 3000, 1000);   // 원래 period 5000 임

        UserActContext = this;
    }

    public void InternetConnCheck()    {

        if (mobile.isConnected() || wifi.isConnected()) {
            internetConnCheck = true;
            if(heartsql.Heartexist) {
                ExportJson(0);
                heartsql.Heartexist = false;
                DropHeart();
            }
            if(aqisql.AQIstatus) {
                ExportJson(1);
                aqisql.AQIstatus = false;
                DropAQI();
            }
        } else  {
            internetConnCheck = false;
            if(!heartsql.Heartexist) {
                HRsqlhelper.createTable(db);
                heartsql.Heartexist = true;
            }   else    {
                InsertheartData();
            }
            if(!aqisql.AQIstatus) {
                AQIsqlhelper.AQIcreateTable(db);
                aqisql.AQIstatus = true;
            }
        }
    }

    private void DBservice() {
        db = openOrCreateDatabase("MyDoctorA", Context.MODE_PRIVATE, null);
    }

    private void logPrint(String str) {
    }

    public String getTimeStr(){
        long now = System.currentTimeMillis();
        Date date = new Date(now);
        SimpleDateFormat sdfNow = new SimpleDateFormat("MM/dd HH:mm:ss");
        return sdfNow.format(date);
    }

    private void makeNewGpsService() {
        if(gps == null) {
        }else{
            gps.Update();
        }
    }

    public void startGPSSubThread() {
        //작업스레드 생성(매듭 묶는과정)
        GPStrackerHandler gpsRunnable = new GPStrackerHandler();
        gpsThread = new Thread(gpsRunnable);
        gpsThread.setDaemon(true);
        gpsThread.start();
    }

    android.os.Handler getgpshandler = new android.os.Handler() {
        public void handleMessage(Message msg) {
            if (gps == null) {
                gps = new GPSTracker(UserActivity.this, GPSHandler);
            } else {
                gps.Update();
            }

            // check if GPS enabled
            if (gps.canGetLocation()) {
                Latitude = gps.getLatitude();
                Longitude = gps.getLongitude();
            } else {
            }
        }

        //SendHR_Asycn(final int usn, final String datetime, final Double lat, final Double lon, final Double hr, final Double rr) {}

    };

    public class GPStrackerHandler implements Runnable {
        @Override
        public void run() {
            while (true) {
                Message msg = Message.obtain();
                msg.what = 0;
                getgpshandler.sendMessage(msg);
                try {
                    Thread.sleep(5000); // 갱신주기 1초
                } catch (Exception e) {
                }
            }
        }
    }

    private void getUserInfo() {
        Intent intent = getIntent();
        usn = intent.getExtras().getInt("usn");    // 로그인 결과로 넘어온 사용자 식별번호
        if(usn == 0)    {
            Toast.makeText(this, "Fake Login", Toast.LENGTH_SHORT).show();
        }   else    {
            UserID = intent.getExtras().getString("ID");
            Username = intent.getExtras().getString("name");

            Navi_ID.setText(UserID);
            Navi_Name.setText(Username);
        }
    }

    public static int getUSN() {
        return usn;
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    public void viewlistBTdevice(int i) {
            Intent bluetoothIntent = new Intent(this, DeviceListActivity.class);
            startActivityForResult(bluetoothIntent, i);
    }

    private void startPolarsensor() {
        // PolarSensor시작
        Intent polarservice = new Intent(getApplicationContext(), PolarSensor.class);
        startService(polarservice);
        //startService(new Intent("com.example.khseob0715.sanfirst.PolarBLE.PolarSensor"));
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
            Intent polarservice = new Intent(getApplicationContext
                    (), PolarSensor.class);
            stopService(polarservice);
            ActivityCompat.finishAffinity(this);

            android.os.Process.killProcess(android.os.Process.myPid()); // ProcessKill
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.user_main, menu);
        Navi_ID = (TextView) findViewById(R.id.Navi_user_email);
        Navi_Name = (TextView) findViewById(R.id.Navi_user_name);
        getUserInfo();

        getMenuInflater().inflate(R.menu.bluetooth_menu, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        switch (id) {

            case R.id.secure_connect_scan: {
                Intent serverIntent = new Intent(getApplicationContext(), DeviceListActivity.class);
                startActivityForResult(serverIntent, REQUEST_CONNECT_DEVICE_SECURE);
                return true;
            }

            case R.id.insecure_connect_scan: {
                // Launch the DeviceListActivity to see devices and do scan
                Intent serverIntent = new Intent(getApplicationContext(), DeviceListActivity.class);
                startActivityForResult(serverIntent, REQUEST_CONNECT_DEVICE_INSECURE);
                return true;
            }

            case R.id.drop: {
                DropAQI();
                DropHAQI();
            }
        }

        return false;
    }

    public void InsertheartData()    {
        String TS = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(System.currentTimeMillis()));;
        Double LAT = GPSTracker.latitude;
        Double LNG = GPSTracker.longitude;
        Double Heart_rate = Double.valueOf(PolarSensor.heartrateValue);
        Double RR_rate = Double.valueOf(PolarSensor.RR_value);
        HRsqlhelper.insertData(db, usn, TS, LAT, LNG, Heart_rate, RR_rate);
    }

    public void DropHeart() {
        HRsqlhelper.dropTable(db);
    }

    public void DropAQI()   {
        AQIsqlhelper.AQIdropTable(db);
    }

    public void DropHAQI()   {
        aqihisyorysql.AQIHdropTable(db);
    }

    public void ExportJson(int addr)    {
        JSONArray jsonarrayResult = null;
        if(addr == 0)    {
            jsonarrayResult = dbtojson.getResults("HEART_HISTORY");
        }   else if (addr == 1){
            jsonarrayResult = dbtojson.getResults("AQI_HISTORY");
        }   else if (addr == 2){
            jsonarrayResult = dbtojson.getResults("AQIH_HISTORY");
        }
        int arraylength = jsonarrayResult.length();
        JSONObject arraytoobj = new JSONObject();

        try {
            arraytoobj.put("length", arraylength);
            arraytoobj.put("data", jsonarrayResult);
            sendjson.SendJSON_Asycn(addr, arraylength, String.valueOf(arraytoobj));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        if(addr == 1 || addr == 2)  {
//            DropAQI();
//            DropHAQI();
        }
    }

    // NavigationMap select item -> view in fragment
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        // Handle navigation view item clicks here.
        int id = item.getItemId();
        Fragment fragment = null;
        String title = getString(R.string.app_name);
        switch (id) {
            // 환자용
            case R.id.nav_user_main :
                fragment = new Fragment_TabMain();
                title = "My Doctor A";
                break;
            case R.id.nav_hr_history :
                fragment = new Fragment_HRHistory();
                title = "My Heart History";
                break;
            case R.id.nav_air_history :
                fragment = new Fragment_AQIHistory();
                title = "My Air History";
                break;
            case R.id.nav_aqi_map :
                fragment = new Fragment_AirMap();
                title = "AQI Maps";
                break;
            case R.id.nav_mydoc :
                fragment = new Fragment_SearchDoctor();
                title = "My Doctor";
                break;
            case R.id.nav_signout :
                new AlertDialog.Builder(UserActivity.this)
                        .setTitle("Sign-Out")
                        .setNegativeButton("Login Page", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                Intent LoginActivityIntent = new Intent(getApplicationContext(),LoginActivity.class);
                                startActivity(LoginActivityIntent);
                            }
                        })
                        .setNeutralButton("Cancle", null) // 제일 앞.
                        .setPositiveButton("Close App", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                Intent polarservice = new Intent(getApplicationContext(), PolarSensor.class);
                                stopService(polarservice);
                                moveTaskToBack(true);
                                finish();
                                android.os.Process.killProcess(android.os.Process.myPid());
                            }
                        })
                        .setCancelable(false)
                        .show();

                break;

            // 의사용
            case R.id.nav_doctor_main :
                fragment = new D_Fragment_main();
                title = "My Doctor A";
                break;

            case R.id.nav_patient_list :
                fragment = new D_Fragment_patientlist();
                title = "Patient List";
                break;

            case R.id.nav_patient_search :
                fragment = new D_Fragment_usersearch();
                title = "Patient Search";
                break;
            default:
                title = "nav_default";
             //   Toast.makeText(this, "nav_default", Toast.LENGTH_SHORT).show();
                break;
            }
        if (fragment != null) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.content_fragment_layout, fragment);
            ft.commit();
        }
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(title);
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    public void accountbt(View view) {
        // 비밀번호를 입력 받아서 맞으면 실행하고 아니면 못 들어가도록 해야 됨.
        final EditText passwordEdit = new EditText(this);
        passwordEdit.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD);
        passwordEdit.setTransformationMethod(PasswordTransformationMethod.getInstance());

        AlertDialog alertDialog = new AlertDialog.Builder(this).create();
        alertDialog.setTitle("Enter your password");
        alertDialog.setView(passwordEdit,80,0,200,0);
        alertDialog.setButton("Admit", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
            confirmpw.Confirmpw_Asycn(1, usn, passwordEdit.getText().toString());
            }
        });
        alertDialog.show();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
    }

    public void profilebt(View view) {
        final EditText passwordEdit = new EditText(this);
        passwordEdit.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD);
        passwordEdit.setTransformationMethod(PasswordTransformationMethod.getInstance());

        AlertDialog alertDialog = new AlertDialog.Builder(this).create();
        alertDialog.setTitle("Enter your password");
        alertDialog.setView(passwordEdit,80,0,200,0);
        alertDialog.setButton("Admit", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                confirmpw.Confirmpw_Asycn(2, usn, passwordEdit.getText().toString());
            }
        });

        alertDialog.show();
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
    }


    // set heartrate value to set the value
    public int getAQIvalue(int i) {
        return val[i];
    }

    // UdooBT
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {//블루투스 서비스 실행
            case 1:
                // When DeviceListActivity returns with a device to connect
                if (resultCode == Activity.RESULT_OK) {
                    connectDevice(data, true);
                }
                break;
            case REQUEST_CONNECT_DEVICE_INSECURE:
                // When DeviceListActivity returns with a device to connect
                if (resultCode == Activity.RESULT_OK) {
                    connectDevice(data, false);
                }
                break;
            default:
                break;
        }
    }
    private void connectDevice(Intent data, boolean secure) {
        // Get the device MAC address
        String address = data.getExtras()
                .getString(DeviceListActivity.EXTRA_DEVICE_ADDRESS);

        Intent bluetoothService=new Intent(getApplicationContext(), BluetoothAQI.class);
        bluetoothService.putExtra(DeviceListActivity.EXTRA_DEVICE_ADDRESS, address);
        try{
            startService(bluetoothService);
        }catch(Exception ex){
        }
    }

    public void HeartSendHandler()  {
        TS = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(System.currentTimeMillis()));;
        LAT = GPSTracker.latitude;
        LNG = GPSTracker.longitude;;
        Heart_rate = Double.valueOf(PolarSensor.heartrateValue);
        RR_rate = Double.valueOf(PolarSensor.RR_value);

        int confirmZero = 0;
        if(PolarSensor.heartrateValue == confirmZero)    {
        }   else    {
            if(internetConnCheck)   {
                sendhr.SendHR_Asycn(usn, TS, LAT, LNG, Heart_rate, RR_rate);
            }
            else    {
                if(!heartsql.Heartexist)    {
                    HRsqlhelper.createTable(db);
                }   else    {
                    HRsqlhelper.insertData(db, usn, TS, LAT, LNG, Heart_rate, RR_rate);
                }
            }
        }
    }

    public void AQISendHandler(Double co,Double so2,Double no2,Double o3,Double pm25,Double temp)  {
        TS = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(System.currentTimeMillis()));;
        LAT = GPSTracker.latitude;
        LNG = GPSTracker.longitude;

            sendaqi.SendAQI_Asycn(db, usn, TS, LAT, LNG, co, so2, no2, o3, pm25, temp);
    }
}