package com.example.ranwildimal;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class DescriptionActivity extends AppCompatActivity {
    public static final String FILE_PATH = Environment.getDataDirectory().getPath() + "/data/com.example.ranwildimal/";
    public static final String ID_FILE = "idfile.txt";
    private String data = "";
    String getId="";
    Toolbar description_toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_description);
        description_toolbar = findViewById(R.id.description_toolbar);
        //Customize status bar
        statusBarColor();
        //Customize toolbar
        setSupportActionBar(description_toolbar);
        getSupportActionBar().setTitle(null);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            getId = String.valueOf(extras.getInt("GETID"));
        }
        loadID();
        saveID();

    }

    private void statusBarColor(){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            getWindow().setStatusBarColor(getResources().getColor(R.color.main_color,this.getTheme()));
        }else if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            getWindow().setStatusBarColor(getResources().getColor(R.color.main_color));
        }
    }

    public void HomeIntent(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        this.startActivity(intent);
    }

    public void saveID(){
        String path = FILE_PATH + ID_FILE;
        try {
            File file = new File(path);
            if(!file.exists()){
                file.createNewFile();
            }
            FileOutputStream fos = new FileOutputStream(file, false);
            if(data.compareTo("") == 0){
                data = getId;
            }else{
                checkDuplicate();
            }
            byte buff[] = data.getBytes();
            fos.write(buff,0 ,buff.length);
            fos.close();
        }catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void loadID(){
        String path = FILE_PATH + ID_FILE;
        try {
            File file = new File(path);
            if(!file.exists()){
                return;
            }
            FileInputStream fis = new FileInputStream(path);
            int lengh;
            byte buff[] = new byte[1024];
            while((lengh = fis.read(buff)) > 0){
                data+= new String(buff,0,lengh);
            }
            fis.close();
        }catch (IOException e) {
            e.printStackTrace();
        }
    }

    void checkDuplicate(){
        String getdata[] = data.split(",");
        if(getdata.length>1){
            data="";
            for (int i = 0;i < getdata.length;i++) {
                if(getId.compareTo(getdata[i])!= 0){
                    data+=getdata[i]+",";
                }
            }
        }else{
            data+=",";
        }
        data+=getId;

    }
}