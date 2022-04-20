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
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;


import com.example.ranwildimal.database.DatabaseAccess;
import com.example.ranwildimal.model.Example;
import com.example.ranwildimal.model.Word;
import com.example.ranwildimal.model.Word_Description;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.jetbrains.annotations.NotNull;
import org.opencv.android.OpenCVLoader;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Array;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    public String FILE_PATH = "";
    public static final String ID_FILE = "locale.txt";
    private String data = "";
    DrawerLayout drawer;
    NavigationView sidebar;
    Toolbar toolbar;
    Context context;
    Resources res ;
    String filepath = "MyFileDir";
    Handler handler;
    Locale current;

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
        FILE_PATH=getExternalFilesDir(filepath).getPath();
        initHanlder();
        //Customize status bar
        statusBarColor();
        drawer = findViewById(R.id.drawer_layout);
        sidebar = findViewById(R.id.main_sidebar);
        toolbar = findViewById(R.id.main_toolbar);

        current = getResources().getConfiguration().locale;
        loadID();

        System.out.println("DAta can write??--->"+ Environment.getDataDirectory().canWrite());
        System.out.println("DAta can read??--->"+Environment.getDataDirectory().canRead());

        //Customize toolbar
        setSupportActionBar(toolbar);
        getSupportActionBar();
        getSupportActionBar().setTitle(null);

        //Customize sidebar
        sidebar.bringToFront();
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.sidebar_open, R.string.sidebar_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();


        sidebar.setNavigationItemSelectedListener(this);

    }

    private void initHanlder() {
        handler = new Handler(){
            @Override
            public void handleMessage(@NonNull Message msg) {
                super.handleMessage(msg);
            }
        };
    }



    public void SearchIntent(View view) {
        Intent intent = new Intent(this, SearchActivity.class);
        this.startActivity(intent);
        finish();
    }

    public void CameraIntent(View view) {
        Intent intent = new Intent(this, CameraActivity.class);
        this.startActivity(intent);
        finish();
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



    private void setLocale(String lang){
        Locale locale = new Locale(lang);
        //save data to shared preference
        SharedPreferences.Editor editor = getSharedPreferences("Setting",MODE_PRIVATE).edit();
        editor.putString("My_Lang",lang);
        editor.apply();
        editor.commit();
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.setLocale(locale);
        getBaseContext().getResources().updateConfiguration(config,getBaseContext().getResources().getDisplayMetrics());
    }



    public void loadID(){
        String path = FILE_PATH +"/"+ ID_FILE;
        try {
            File file = new File(path);
            if(!file.exists()){
                file.createNewFile();
                if (current.toString().equals("vi-VN")){
                    setLocale("vi");
                } else if (current.toString().equals("en-US")){
                    setLocale("en");
                } else {
                    setLocale("ja");
                }
                FileOutputStream fos = new FileOutputStream(file, false);
                data += "en";
                byte buff[] = data.getBytes();
                fos.write(buff,0 ,buff.length);
                fos.close();
            }
            if(file.length() == 0){
                setLocale("en");
                FileOutputStream fos = new FileOutputStream(file, false);
                Locale current = getResources().getConfiguration().locale;
                data += current.toString();
                byte buff[] = data.getBytes();
                fos.write(buff,0 ,buff.length);
                fos.close();
            }
            FileInputStream fis = new FileInputStream(path);
            int lengh;
            byte buff[] = new byte[1024];
            Locale current = getResources().getConfiguration().locale;
            while((lengh = fis.read(buff)) > 0){
                data = new String(buff,0,lengh);
            }
            if(data.compareTo("en") == 0){
                setLocale("en");
            }else if(data.compareTo("vi") == 0){
                setLocale("vi");
            }else{
                setLocale("ja");
            }
            fis.close();
            if(data.compareTo(current.toString()) != 0){
                recreate();
            }
        }catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void statusBarColor(){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            getWindow().setStatusBarColor(getResources().getColor(R.color.main_color,this.getTheme()));
        }else if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            getWindow().setStatusBarColor(getResources().getColor(R.color.main_color));
        }
    }
}