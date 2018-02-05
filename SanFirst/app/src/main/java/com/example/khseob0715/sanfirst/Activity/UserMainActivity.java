package com.example.khseob0715.sanfirst.Activity;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.IBinder;
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
import android.widget.Toast;

import com.example.khseob0715.sanfirst.PolarBLE.HConstants;
import com.example.khseob0715.sanfirst.PolarBLE.PolarBleService;
import com.example.khseob0715.sanfirst.R;
import com.example.khseob0715.sanfirst.navi_fragment.Fragment_AQIHistory;
import com.example.khseob0715.sanfirst.navi_fragment.Fragment_Account;
import com.example.khseob0715.sanfirst.navi_fragment.Fragment_AirMap;
import com.example.khseob0715.sanfirst.navi_fragment.Fragment_HRHistory;
import com.example.khseob0715.sanfirst.navi_fragment.Fragment_Main;
import com.example.khseob0715.sanfirst.navi_fragment.Fragment_MyDoctor;
import com.example.khseob0715.sanfirst.navi_fragment.Fragment_Profile;

import java.util.StringTokenizer;

public class UserMainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    //Fragment fragment = new Fragment();
    private Fragment btchatFragment;
    private Fragment hrhistoryFragment;
    private Fragment airqualfragment;
    private Fragment accountFragment;
    private Fragment profileFragment;

    private SharedPreferences prefs;

    PolarBleService mPolarBleService;
    // String mpolarBleDeviceAddress="00:22:D0:A4:96:72";
    String mpolarBleDeviceAddress="00:22:D0:A4:9D:83";  // 우리꺼
    int batteryLevel=0;

    //------------------------------
    String mDefaultDeviceAddress;

    public static int heartratevalue = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        //fragment = new Fragment_Main();
        btchatFragment = new Fragment_Main();
        hrhistoryFragment = new Fragment_HRHistory();
        airqualfragment = new Fragment_AirMap();
        accountFragment = new Fragment_AirMap();
        profileFragment = new Fragment_AirMap();

        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.content_fragment_layout, btchatFragment);
        ft.commit();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        prefs = getSharedPreferences(HConstants.DEVICE_CONFIG, Context.MODE_MULTI_PROCESS);
        mDefaultDeviceAddress = prefs.getString(HConstants.CONFIG_DEFAULT_DEVICE_ADDRESS, null);

        activatePolar();

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
            case R.id.nav_main  :
                fragment = new Fragment_Main();
                title = "nav_main";
                Toast.makeText(this, "nav_main", Toast.LENGTH_SHORT).show();
                break;
            case R.id.nav_hr_history :
                fragment = new Fragment_HRHistory();
                title = "My History";
                Toast.makeText(this, "nav_hr_history", Toast.LENGTH_SHORT).show();
                break;
            case R.id.nav_air_history :
                fragment = new Fragment_AQIHistory();
                title = "My Air History";
                Toast.makeText(this, "nav_air_history", Toast.LENGTH_SHORT).show();
                break;
            case R.id.nav_aqi_map :
                fragment = new Fragment_AirMap();
                title = "AQI Maps";
                Toast.makeText(this, "nav_AQI", Toast.LENGTH_SHORT).show();
                break;
            case R.id.nav_mydoc :
                fragment = new Fragment_MyDoctor();
                title = "My Doctor";
                break;
            case R.id.nav_chat :
                title = "Chat";
                Toast.makeText(this, "nav_chat", Toast.LENGTH_SHORT).show();
                break;
            case R.id.nav_signout :
                title = "nav_signout";
                Toast.makeText(this, "nav_sign_out", Toast.LENGTH_SHORT).show();
                ActivityCompat.finishAffinity(this);
                android.os.Process.killProcess(android.os.Process.myPid());
                break;
            default:
                title = "nav_default";
                Toast.makeText(this, "nav_default", Toast.LENGTH_SHORT).show();
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

        deactivatePolar();
    }


    public void accountbt(View view) {
        Fragment fragment = new Fragment();
        fragment = new Fragment_Account();
        String title = getString(R.string.app_name);
        title = "Account Management";
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.content_fragment_layout, fragment);
        ft.commit();
        getSupportActionBar().setTitle(title);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
    }

    public void profilebt(View view) {
        Fragment fragment = new Fragment();
        fragment = new Fragment_Profile();
        String title = getString(R.string.app_name);
        title = "My Profile";
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.content_fragment_layout, fragment);
        ft.commit();
        getSupportActionBar().setTitle(title);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
    }
    protected void activatePolar() {
        Log.w(this.getClass().getName(), "** activatePolar()");

        Intent gattactivatePolarServiceIntent = new Intent(this, PolarBleService.class);
        bindService(gattactivatePolarServiceIntent, mPolarBleServiceConnection, BIND_AUTO_CREATE);
        registerReceiver(mPolarBleUpdateReceiver, makePolarGattUpdateIntentFilter());
    }

    protected void deactivatePolar() {
        Log.w(this.getClass().getName(), "deactivatePolar()");
        try{
            if(mPolarBleService!=null){
                Log.w(this.getClass().getName(), "**** unbindService()");
                unbindService(mPolarBleServiceConnection);
                Log.w(this.getClass().getName(), "bindService()");
            }
        }catch(Exception e){
            Log.e(this.getClass().getName(), e.toString());
        }

        try{
            unregisterReceiver(mPolarBleUpdateReceiver);
        }catch(Exception e){
            Log.e(this.getClass().getName(), e.toString());
        }
    }

    private final BroadcastReceiver mPolarBleUpdateReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context ctx, Intent intent) {
            //Log.w(TAG, "####BroadcastReceiver Polar BLE Service ");

            final String action = intent.getAction();
            if (PolarBleService.ACTION_GATT_CONNECTED.equals(action)) {
            } else if (PolarBleService.ACTION_GATT_DISCONNECTED.equals(action)) {
                Log.w(this.getClass().getName(), "mPolarBleUpdateReceiver received ACTION_GATT_DISCONNECTED");
            } else if (PolarBleService.ACTION_HR_DATA_AVAILABLE.equals(action)) {

                //heartRate+";"+pnnPercentage+";"+pnnCount+";"+rrThreshold+";"+bioHarnessSessionData.totalNN
                //String data = intent.getStringExtra(BluetoothLeService.EXTRA_DATA);
                String data = intent.getStringExtra(PolarBleService.EXTRA_DATA);
                StringTokenizer tokens = new StringTokenizer(data, ";");
                int hr = Integer.parseInt(tokens.nextToken());
                int prrPercenteage = Integer.parseInt(tokens.nextToken());
                int prrCount = Integer.parseInt(tokens.nextToken());
                int rrThreshold = Integer.parseInt(tokens.nextToken());	//50%, 30%, etc.
                int rrTotal = Integer.parseInt(tokens.nextToken());
                int rrValue = Integer.parseInt(tokens.nextToken());
                long sid = Long.parseLong(tokens.nextToken());

                heartratevalue = hr;
                Log.e(this.getClass().getName(), "Heart rate is " + heartratevalue);



            }else if (PolarBleService.ACTION_BATTERY_DATA_AVAILABLE.equals(action)) {
                //String data = intent.getStringExtra(BluetoothLeService.EXTRA_DATA);
                String data = intent.getStringExtra(PolarBleService.EXTRA_DATA);
                batteryLevel = Integer.parseInt(data);
            }else if (PolarBleService.ACTION_GATT_SERVICES_DISCOVERED.equals(action)) {
                String data = intent.getStringExtra(PolarBleService.EXTRA_DATA);
                //String data = intent.getStringExtra(BluetoothLeService.EXTRA_DATA);
                StringTokenizer tokens = new StringTokenizer(data, ";");
                int totalNN = Integer.parseInt(tokens.nextToken());
                long lSessionId = Long.parseLong(tokens.nextToken());


            }
        }
    };

    private static IntentFilter makePolarGattUpdateIntentFilter() {
        final IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(PolarBleService.ACTION_GATT_CONNECTED);
        intentFilter.addAction(PolarBleService.ACTION_GATT_DISCONNECTED);
        intentFilter.addAction(PolarBleService.ACTION_GATT_SERVICES_DISCOVERED);
        intentFilter.addAction(PolarBleService.ACTION_HR_DATA_AVAILABLE);
        intentFilter.addAction(PolarBleService.ACTION_BATTERY_DATA_AVAILABLE);
        return intentFilter;
    }

    private final ServiceConnection mPolarBleServiceConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName componentName, IBinder service) {
            mPolarBleService = ((PolarBleService.LocalBinder) service).getService();
            if (!mPolarBleService.initialize()) {
                Log.e(this.getClass().getName(), "Unable to initialize Bluetooth");
                finish();
            }
            // Automatically connects to the device upon successful start-up initialization.
            //mPolarBleService.connect(app.polarBleDeviceAddress, false);
            mPolarBleService.connect(mpolarBleDeviceAddress, false);
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {

            mPolarBleService = null;
        }
    };

    public void measurehr(View view) {
        activatePolar();
        Log.e("Heartratevalueset", "Heartrate -- "+heartratevalue);
    }

    public int getHeartratevalue() {

        return heartratevalue;

    }

}