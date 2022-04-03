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
import java.util.ArrayList;
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
        loadID();
        initHanlder();
        //Customize status bar
        statusBarColor();
        drawer = findViewById(R.id.drawer_layout);
        sidebar = findViewById(R.id.main_sidebar);
        toolbar = findViewById(R.id.main_toolbar);




        ConnectivityManager connectivityManager = (ConnectivityManager) MainActivity.this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo wifiConn = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        NetworkInfo mobileConn = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        if((wifiConn != null && wifiConn.isConnected())){
            System.out.println(" >>>>>>>>>>>>> Network Connected");
            //updateDatafromFS();
        }else{
            System.out.println(" >>>>>>>>>>>>> Network DisConnected");
        }



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

    private void initHanlder() {
        handler = new Handler(){
            @Override
            public void handleMessage(@NonNull Message msg) {
                super.handleMessage(msg);
            }
        };
    }

    private void updateDatafromFS() {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                DatabaseAccess dbAccess = DatabaseAccess.getInstance(getApplicationContext());
                dbAccess.openConn();
                final String[] doc_Id = new String[100];
                ArrayList<Word> word = new ArrayList<>();
                ArrayList<Word_Description> word_descriptions = new ArrayList<>();
                FirebaseFirestore fs = FirebaseFirestore.getInstance();
                fs.collection("Word_Description")
                        .whereEqualTo("Word_Status",2)
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if(task.isSuccessful() && !task.getResult().isEmpty()){
                                    int i = 0;
                                    for(QueryDocumentSnapshot doc : task.getResult()){
                                        doc_Id[i] = doc.getId();
                                       Word_Description word_des = new Word_Description();
                                       word_des.setWord_Des_Id(doc.get("Word_Des_Id",Integer.class));
                                       word_des.setWord_Image(doc.getString("Word_Image"));
                                       word_des.setWord_Video(doc.getString("Word_Video"));
                                       word_des.setWord_Pronounce(doc.getString("Word_Pronounce"));
                                       word_des.setWord_Status(0);
                                       word_descriptions.add(word_des);
                                       i++;
                                    }
                                }
                                int j = 0;
                                for (Word_Description w :word_descriptions) {
                                    dbAccess.updateWordDes(w);
                                    Map<String, Object> word_des_obj = new HashMap<>();
                                    word_des_obj.put("Word_Des_Id",w.getWord_Des_Id());
                                    word_des_obj.put("Word_Pronounce",w.getWord_Pronounce());
                                    word_des_obj.put("Word_Video",w.getWord_Video());
                                    word_des_obj.put("Word_Image",w.getWord_Image());
                                    word_des_obj.put("Word_Status",0);
                                    fs.collection("Word_Description")
                                            .document(doc_Id[j])
                                            .update(word_des_obj);
                                    j++;
                                }
                            }
                        });
                fs.collection("Word")
                        .whereEqualTo("Word_Status",2)
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if(task.isSuccessful() && !task.getResult().isEmpty()){
                                    int i = 0;
                                    for(QueryDocumentSnapshot doc : task.getResult()){
                                        doc_Id[i] = doc.getId();
                                        Word word_des = new Word();
                                        word_des.setWord_Des_Id(doc.get("Word_Des_Id",Integer.class));
                                        word_des.setWord_Type_Id(doc.get("Word_Type_Id",Integer.class));
                                        word_des.setWord(doc.getString("Word"));
                                        word_des.setLanguage_Id(doc.get("Language_Id",Integer.class));
                                        word_des.setWord_Status(0);
                                        word.add(word_des);
                                        i++;
                                    }
                                }
                                int j = 0;
                                for (Word w :word) {
                                    dbAccess.updateWord(w);
                                    Map<String, Object> word_des_obj = new HashMap<>();
                                    word_des_obj.put("Word_Status",0);
                                    fs.collection("Word")
                                            .document(doc_Id[j])
                                            .update(word_des_obj);
                                    j++;
                                }
                            }
                        });
            }
        });
        thread.run();
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
                setLocale("en");
                FileOutputStream fos = new FileOutputStream(file, false);
                Locale current = getResources().getConfiguration().locale;
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