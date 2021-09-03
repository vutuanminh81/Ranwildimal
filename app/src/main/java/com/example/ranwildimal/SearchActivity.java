package com.example.ranwildimal;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Build;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;
public class SearchActivity extends AppCompatActivity {

    Toolbar search_toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        search_toolbar = findViewById(R.id.search_toolbar);
        //Customize status bar
        statusBarColor();
        //Customize toolbar
        setSupportActionBar(search_toolbar);
        getSupportActionBar().setTitle(null);

    }

    private void statusBarColor(){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            getWindow().setStatusBarColor(getResources().getColor(R.color.main_color,this.getTheme()));
        }else if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            getWindow().setStatusBarColor(getResources().getColor(R.color.main_color));
        }
    }
}