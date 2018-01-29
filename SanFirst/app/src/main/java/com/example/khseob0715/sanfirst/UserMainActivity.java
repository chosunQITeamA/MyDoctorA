package com.example.khseob0715.sanfirst;

import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

public class UserMainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    Fragment fragment = new Fragment();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        fragment = new BluetoothChatFragment();
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.content_fragment_layout, fragment);
        ft.commit();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
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

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        fragment = null;
        String title = getString(R.string.app_name);

        switch (id) {
            case R.id.nav_main  :
                fragment = new BluetoothChatFragment();
                title = "nav_main";
                Toast.makeText(this, "nav_main", Toast.LENGTH_SHORT).show();
                break;
            case R.id.nav_st_history :
                fragment = new BluetoothChatFragment();
                title = "st_history";
                Toast.makeText(this, "nav_st_history", Toast.LENGTH_SHORT).show();
                break;
            case R.id.nav_ex_history :
                fragment = new BluetoothChatFragment();
                title = "ex_history";
                Toast.makeText(this, "nav_ex_history", Toast.LENGTH_SHORT).show();
                break;
            case R.id.nav_aqi :
                fragment = new BluetoothChatFragment();
                title = "nav_aqi";
                Toast.makeText(this, "nav_AQI", Toast.LENGTH_SHORT).show();
                break;
            case R.id.nav_chat :
                fragment = new BluetoothChatFragment();
                title = "nav_chat";
                Toast.makeText(this, "nav_chat", Toast.LENGTH_SHORT).show();
                break;
            case R.id.nav_settings :
                fragment = new BluetoothChatFragment();
                title = "nav_settings";
                Toast.makeText(this, "nav_settings", Toast.LENGTH_SHORT).show();
                break;
            case R.id.nav_signout :
                fragment = new BluetoothChatFragment();
                title = "nav_signout";
                Toast.makeText(this, "nav_sign_out", Toast.LENGTH_SHORT).show();
                break;
            default:
                fragment = new BluetoothChatFragment();
                title = "nav_default";
                Toast.makeText(this, "nav_default", Toast.LENGTH_SHORT).show();
                break;
            }
        if (fragment != null) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.content_fragment_layout, fragment);
            ft.commit();
        }

        // set the toolbar title
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(title);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
