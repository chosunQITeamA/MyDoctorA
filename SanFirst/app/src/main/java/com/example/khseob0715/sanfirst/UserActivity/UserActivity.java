package com.example.khseob0715.sanfirst.UserActivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.khseob0715.sanfirst.PolarBLE.PolarSensor;
import com.example.khseob0715.sanfirst.R;
import com.example.khseob0715.sanfirst.navi_fragment.Fragment_AQIHistory;
import com.example.khseob0715.sanfirst.navi_fragment.Fragment_Account;
import com.example.khseob0715.sanfirst.navi_fragment.Fragment_AirMap;
import com.example.khseob0715.sanfirst.navi_fragment.Fragment_HRHistory;
import com.example.khseob0715.sanfirst.navi_fragment.Fragment_Main;
import com.example.khseob0715.sanfirst.navi_fragment.Fragment_MyDoctor;
import com.example.khseob0715.sanfirst.navi_fragment.Fragment_Profile;
import com.example.khseob0715.sanfirst.navi_fragment.Fragment_TabMain;
import com.example.khseob0715.sanfirst.udoo_btchat.BluetoothAQI;
import com.example.khseob0715.sanfirst.udoo_btchat.BluetoothChatService;
import com.example.khseob0715.sanfirst.udoo_btchat.DeviceListActivity;

public class UserActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    //Fragment fragment = new Fragment();
    private Fragment btchatFragment;
    private Fragment hrhistoryFragment;
    private Fragment airqualfragment;
    private Fragment accountFragment;
    private Fragment profileFragment;

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

    private int usn = 0;
    private String UserID, Username;

    private EditText passwordEdit_profile;

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

        //fragment = new Fragment_Main();
//        btchatFragment = new Fragment_Main();
        btchatFragment = new Fragment_TabMain();
        hrhistoryFragment = new Fragment_HRHistory();
        airqualfragment = new Fragment_AirMap();
        accountFragment = new Fragment_AirMap();
        profileFragment = new Fragment_AirMap();

        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.content_fragment_layout, btchatFragment);
        ft.commit();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        viewlistBTdevice();
        startPolarsensor();

        // If the adapter is null, then Bluetooth is not supported
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
            Toast.makeText(this, UserID +"\n"+Username, Toast.LENGTH_SHORT).show();
        }

//        Intent intent = getIntent();
//        usn = intent.getExtras().getInt("usn1");    // 로그인 결과로 넘어온 사용자 식별번호
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    private void viewlistBTdevice() {
            Intent bluetoothIntent = new Intent(this, DeviceListActivity.class);
            startActivityForResult(bluetoothIntent, 1);
    }

    private void startPolarsensor() {
        // PolarSensor시작
        Intent polarservice = new Intent(getApplicationContext(), PolarSensor.class);
        startService(polarservice);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
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

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    // NavigationMap select item -> view in fragment
    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        Fragment fragment = null;

        String title = getString(R.string.app_name);

        switch (id) {
            case R.id.nav_main :
                fragment = new Fragment_TabMain();
                title = "My Doctor A";
             //   Toast.makeText(this, "nav_main", Toast.LENGTH_SHORT).show();
                break;
            case R.id.nav_hr_history :
                fragment = new Fragment_HRHistory();
                title = "My History";
             //   Toast.makeText(this, "nav_hr_history", Toast.LENGTH_SHORT).show();
                break;
            case R.id.nav_air_history :
                fragment = new Fragment_AQIHistory();
                title = "My Air History";
             //   Toast.makeText(this, "nav_air_history", Toast.LENGTH_SHORT).show();
                break;
            case R.id.nav_aqi_map :
                fragment = new Fragment_AirMap();
                title = "AQI Maps";
                break;
            case R.id.nav_mydoc :
                fragment = new Fragment_MyDoctor();
                title = "My Doctor";
                break;
            case R.id.nav_signout :
                new AlertDialog.Builder(UserActivity.this)
                        .setTitle("Sign-Out")
                        .setMessage("Sign-Out Complete")
                        .setNegativeButton("Login", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                Intent LoginActivityIntent = new Intent(getApplicationContext(),LoginActivity.class);
                                startActivity(LoginActivityIntent);
                            }
                        })
                        .setPositiveButton("Close App", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                moveTaskToBack(true);
                                finish();
                                android.os.Process.killProcess(android.os.Process.myPid());
                            }
                        })
                        .setCancelable(false)
                        .show();

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
        Log.e(this.getClass().getName(), "onDestroy()");
    }


    public void accountbt(View view) {
        // 비밀번호를 입력 받아서 맞으면 실행하고 아니면 못 들어가도록 해야 됨.
        final EditText passwordEdit = new EditText(UserActivity.this);
        new AlertDialog.Builder(UserActivity.this)
                .setTitle("Enter your password")
                .setView(passwordEdit)
                .setNegativeButton("Admit", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if (!passwordEdit.getText().toString().equals("")) {
                            Fragment fragment = new Fragment();
                            fragment = new Fragment_Account();
                            String title = "Account Management";
                            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                            ft.replace(R.id.content_fragment_layout, fragment);
                            ft.commit();
                            getSupportActionBar().setTitle(title);
                        }
                    }
                })
                .show();
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
    }

    public void profilebt(View view) {
        // 비밀번호를 입력 받아서 맞으면 실행하고 아니면 못 들어가도록 해야 됨.
        final EditText passwordEdit = new EditText(UserActivity.this);
        new AlertDialog.Builder(UserActivity.this)
                .setTitle("Enter your password")
                .setView(passwordEdit)
                .setNegativeButton("Admit", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if (!passwordEdit.getText().toString().equals("")) {
                            Fragment fragment = new Fragment();
                            fragment = new Fragment_Profile();
                            String title = "Profile Management";
                            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                            ft.replace(R.id.content_fragment_layout, fragment);
                            ft.commit();
                            getSupportActionBar().setTitle(title);
                        }
                    }
                })
                .show();
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
    }


    // set heartrate value to set the value
    public int getAQIvalue(int i) {
        return val[i];
    }

    // UdooBT
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.e("OnActivityResult", "OnActivityResult-UerMainActivity");
        switch (requestCode) {//블루투스 서비스 실행
            case 1:
                // When DeviceListActivity returns with a device to connect
                if (resultCode == Activity.RESULT_OK) {
                    connectDevice(data, true);
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
        Log.i("MenuActivity_addr", address);

        Intent bluetoothService=new Intent(getApplicationContext(), BluetoothAQI.class);
        bluetoothService.putExtra(DeviceListActivity.EXTRA_DEVICE_ADDRESS, address);
        try{
            startService(bluetoothService);
        }catch(Exception ex){
            Log.e("MenuActivity", "Exception : "+ex.getMessage());
        }
    }
}