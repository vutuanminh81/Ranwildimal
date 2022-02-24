package com.example.ranwildimal;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;


import com.google.android.material.navigation.NavigationView;

import org.jetbrains.annotations.NotNull;
import org.opencv.android.OpenCVLoader;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    DrawerLayout drawer;
    NavigationView sidebar;
    Toolbar toolbar;
    Context context;
    Resources res ;

    static{
        if(OpenCVLoader.initDebug()){
            System.out.println("............................. Success");
        }else{
            System.out.println("............................. Ngu");
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //Customize status bar
        statusBarColor();
        drawer = findViewById(R.id.drawer_layout);
        sidebar = findViewById(R.id.main_sidebar);
        toolbar = findViewById(R.id.main_toolbar);

        System.out.println("DAta can write??--->"+ Environment.getDataDirectory().canWrite());
        System.out.println("DAta can read??--->"+Environment.getDataDirectory().canRead());

        //Customize toolbar
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(null);

        //Customize sidebar
        sidebar.bringToFront();
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.sidebar_open, R.string.sidebar_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();


        sidebar.setNavigationItemSelectedListener(this);

    }

    public void SearchIntent(View view) {
        Intent intent = new Intent(this, SearchActivity.class);
        this.startActivity(intent);
    }

    public void CameraIntent(View view) {
        Intent intent = new Intent(this, CameraActivity.class);
        this.startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull @NotNull MenuItem sidebarItem) {
        switch (sidebarItem.getItemId()) {
            case R.id.sidebar_home:
                break;
            case R.id.sidebar_encounter:
                Intent myEncouter = new Intent(MainActivity.this, MyEncounterActivity.class);
                startActivity(myEncouter);
                break;
            case R.id.sidebar_aboutus:
                Intent aboutUs = new Intent(MainActivity.this, AbouUsActivity.class);
                startActivity(aboutUs);
                break;
            case R.id.sidebar_settings:
                Intent setting = new Intent(MainActivity.this, SettingActivity.class);
                startActivity(setting);
                break;
        }
        return true;
    }

    private void statusBarColor(){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            getWindow().setStatusBarColor(getResources().getColor(R.color.main_color,this.getTheme()));
        }else if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            getWindow().setStatusBarColor(getResources().getColor(R.color.main_color));
        }
    }
}