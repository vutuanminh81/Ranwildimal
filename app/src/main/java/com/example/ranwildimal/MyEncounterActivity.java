package com.example.ranwildimal;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.TextView;

import com.example.ranwildimal.adapter.MyEncounterAdapter;
import com.example.ranwildimal.adapter.WordAdapter;
import com.example.ranwildimal.database.DatabaseAccess;
import com.example.ranwildimal.model.Word;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Locale;

public class MyEncounterActivity extends AppCompatActivity {

    String filename = "idfile.txt";
    String filepath = "MyFileDir";
    //public String FILE_PATH = getExternalFilesDir(filepath).getPath() + filename;
    public static final String ID_FILE = "idfile.txt";
    private String data = "";
    Toolbar encounter_toolbar;
    RecyclerView recyclerView;
    MyEncounterAdapter encounterAdapter;
    ArrayList<Word> listJapWord;
    ArrayList<Word> listsearchWord;
    TextView searchtxt;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(this.getSharedPreferences("Setting",MODE_PRIVATE).getString("My_Lang","").equalsIgnoreCase("en")){
            setLocale("en");
        }else if(this.getSharedPreferences("Setting",MODE_PRIVATE).getString("My_Lang","").equalsIgnoreCase("vi")){
            setLocale("vi");
        }else if(this.getSharedPreferences("Setting",MODE_PRIVATE).getString("My_Lang","").equalsIgnoreCase("ja")){
            setLocale("ja");
        }
        setContentView(R.layout.activity_my_encounter);
        encounter_toolbar = findViewById(R.id.encounter_toolbar);
        recyclerView = findViewById(R.id.encounter_list);
        searchtxt = findViewById(R.id.txt_search_field);
        //Customize status bar
        statusBarColor();
        //Customize toolbar
        setSupportActionBar(encounter_toolbar);
        getSupportActionBar().setTitle(null);
        listJapWord = new ArrayList<>();
        listsearchWord = new ArrayList<>();
        encounterAdapter = new MyEncounterAdapter(listsearchWord, MyEncounterActivity.this); //Call LecturerAdapter to set data set and show data
        LinearLayoutManager manager = new LinearLayoutManager(MyEncounterActivity.this); //Linear Layout Manager use to handling layout for each Lecturer
        recyclerView.setAdapter(encounterAdapter);
        recyclerView.setLayoutManager(manager);
        loadID();
        getDataList();
        searchtxt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                filter(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
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
        finish();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(this, MainActivity.class);
        this.startActivity(intent);
        finish();
    }

    private void filter(String s) {
        ArrayList<Word> newlist = new ArrayList<>();
        ArrayList<Word> listall = new ArrayList<>();
        if (!s.isEmpty() && s.compareTo("")!=0) {
            if(listsearchWord != null){
                for (Word w: listsearchWord) {
                    int desID = 0;
                    if (w.getWord().toLowerCase().contains(s)){
                        newlist.add(w);
                    }
                }
                for (Word w: newlist) {
                    for (Word jpw: listJapWord ) {
                        if (w.getWord_Des_Id() == jpw.getWord_Des_Id()){
                            listall.add(jpw);
                        }
                    }
                }
            }
            encounterAdapter.ArrayFilter(newlist, listall);
        } else {
            getDataList();
        }
    }

    public void getDataList(){
        String getId[] = data.split(",");
        ArrayList<Word> newlist = new ArrayList<>();
        ArrayList<Word> languagelist = new ArrayList<>();
        ArrayList<Word> japlist = new ArrayList<>();
        DatabaseAccess dbAccess = DatabaseAccess.getInstance(getApplicationContext());
        dbAccess.openConn();
        newlist = dbAccess.getWord();
        Locale currentLang = getResources().getConfiguration().locale;
        int langId = 0 ;
        if(currentLang.toString().equals("vi")){
            langId = 1;
        }else if(currentLang.toString().equals("en")){
            langId = 2;
        }else if(currentLang.toString().equals("ja")){
            langId = 3;
        }
        if(getId[0] != ""){
            for (int i = getId.length-1; i >= 0 ; i--){
                int desId =0;
                for (Word w: newlist) {
                    if(Integer.parseInt(getId[i]) == w.getWord_Des_Id() && w.getLanguage_Id() == langId){
                        desId = w.getWord_Des_Id();
                        languagelist.add(w);
                    }
                    if(desId == w.getWord_Des_Id() && w.getLanguage_Id() == 3){
                        japlist.add(w);
                    }
                }
            }
            listsearchWord = languagelist;
            listJapWord = japlist;
            encounterAdapter.ArrayFilter(languagelist, japlist);
        }
    }

    public void loadID(){
        String path = getExternalFilesDir(filepath).getPath()+"/"+filename;
        try {
            File file = new File(getExternalFilesDir(filepath), filename);
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
}