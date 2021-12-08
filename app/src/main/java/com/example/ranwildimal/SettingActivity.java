package com.example.ranwildimal;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.google.android.material.navigation.NavigationBarView;

import java.util.Locale;

public class SettingActivity extends AppCompatActivity {

    Toolbar setting_toolbar;
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        setting_toolbar = findViewById(R.id.setting_toolbar);
        //Customize status bar
        statusBarColor();
        //Customize toolbar
        setSupportActionBar(setting_toolbar);
        getSupportActionBar().setTitle(null);
        Spinner spinner = (Spinner) findViewById(R.id.spinner);
        ArrayAdapter<String> languages = new ArrayAdapter<String>(SettingActivity.this,
                android.R.layout.simple_list_item_1,getResources().getStringArray(R.array.languages));
        languages.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(languages);
        Locale locale = getResources().getConfiguration().locale;
        if(locale.toString().equals("en")){
            spinner.setSelection(1);
        }else if(locale.toString().equals("vi")){
            spinner.setSelection(0);
        }else{
            spinner.setSelection(2);
        }
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(spinner.getSelectedItemPosition() == 0){
                    if(!locale.toString().equals("vi")){
                        setLocale("vi");
                        recreate();
                    }else{
                        return;
                    }
                }else if(spinner.getSelectedItemPosition() == 1){
                    if(!locale.toString().equals("en")){
                        setLocale("en");
                        recreate();
                    }else{
                        return;
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                return;
            }
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(this,MainActivity.class);
        startActivity(intent);
        finish();
    }

    private void setLocale(String lang){
        Locale locale = new Locale(lang);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        getBaseContext().getResources().updateConfiguration(config,getBaseContext().getResources().getDisplayMetrics());
        //save data to shared preference
        SharedPreferences.Editor editor = getSharedPreferences("Setting",MODE_PRIVATE).edit();
        editor.putString("My_Lang",lang);
        editor.apply();
        editor.commit();
    }

    private void loadLocale(){
        SharedPreferences pref = getSharedPreferences("Settings", Activity.MODE_PRIVATE);
        String language = pref.getString("My_Lang","");
        setLocale(language);
    }
    private void statusBarColor(){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            getWindow().setStatusBarColor(getResources().getColor(R.color.main_color,this.getTheme()));
        }else if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            getWindow().setStatusBarColor(getResources().getColor(R.color.main_color));
        }
    }

    public void HomeIntent(View view) {
        Intent intent = new Intent(this,MainActivity.class);
        intent.putExtra("value",this.getResources().getConfiguration().locale.toString());
        startActivity(intent);
        finish();
    }
}