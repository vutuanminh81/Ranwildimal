package com.example.ranwildimal;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ImageButton;

import com.example.ranwildimal.adapter.WordDescriptionAdapter;
import com.example.ranwildimal.database.DatabaseAccess;
import com.example.ranwildimal.model.Word;
import com.example.ranwildimal.model.Example;

import com.example.ranwildimal.model.Word_Description;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;

import java.io.OutputStreamWriter;
import java.util.ArrayList;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Locale;

public class DescriptionActivity extends AppCompatActivity {
    public String FILE_PATH = "";
    public static final String ID_FILE = "idfile.txt";
    private String data = "";
    String getId="";
    Toolbar description_toolbar;
    String wordID;
    Word selectWord,jpWord;
    TextView jpTxtWord,selectedTxtWord;
    ArrayList<Example> listSelectExample,listJpExample;
    AssetManager assetMan;
    Word word;
    ImageButton playbutton;
    WordDescriptionAdapter exampleAdapter;
    RecyclerView exampleListView;
    ImageView animalImage;
    YouTubePlayerView youTubePlayerView;
    String videoLink;
    String filename = "idfile.txt";
    String filepath = "MyFileDir";
    String fileContent = "";
    String worlDesId = "";
    Word_Description wordDescription;
    MediaPlayer media = new MediaPlayer();
    String getIntent = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_description);
        description_toolbar = findViewById(R.id.description_toolbar);
        assetMan = this.getAssets();
        //Customize status bar
        statusBarColor();
        //Customize toolbar
        getIntent = getIntent().getStringExtra("IntentFrom");
        setSupportActionBar(description_toolbar);
        if(this.getSharedPreferences("Setting",MODE_PRIVATE).getString("My_Lang","").equalsIgnoreCase("en")){
            setLocale("en");
        }else if(this.getSharedPreferences("Setting",MODE_PRIVATE).getString("My_Lang","").equalsIgnoreCase("vi")){
            setLocale("vi");
        }else if(this.getSharedPreferences("Setting",MODE_PRIVATE).getString("My_Lang","").equalsIgnoreCase("ja")){
            setLocale("ja");
        }
        getSupportActionBar().setTitle(null);
        Bundle extras = getIntent().getExtras();
        FILE_PATH = getExternalFilesDir(filepath).getPath()+"/"+filename;
        if (extras != null) {
            getId = String.valueOf(extras.getInt("GETID"));
            worlDesId = getId;
        }


        playbutton = findViewById(R.id.btn_des_sound);
        jpTxtWord = findViewById(R.id.text_description_japanese_word);
        selectedTxtWord = findViewById(R.id.txt_animal_description_word);
        exampleListView = findViewById(R.id.description_exxemple_list);
        animalImage = findViewById(R.id.img_description_animal);
        youTubePlayerView = findViewById(R.id.ex_ExampleVideo);



        getLifecycle().addObserver(youTubePlayerView);

        wordID = String.valueOf(getIntent().getIntExtra("GETID",0));
        loadData();
        loadID();
        saveID();

        jpTxtWord.setText(jpWord.getWord());
        selectedTxtWord.setText(selectWord.getWord());

        exampleAdapter = new WordDescriptionAdapter(listSelectExample,listJpExample,this);
        LinearLayoutManager manager = new LinearLayoutManager(DescriptionActivity.this); //Linear Layout Manager use to handling layout for each Lecturer
        exampleListView.setAdapter(exampleAdapter);
        exampleListView.setLayoutManager(manager);

        int des_id = selectWord.getWord_Des_Id();
        wordDescription = new Word_Description();
        wordDescription = loadDescription(String.valueOf(des_id));

        youTubePlayerView.addYouTubePlayerListener(new AbstractYouTubePlayerListener() {
            @Override
            public void onReady(YouTubePlayer youTubePlayer) {
                String videoId = wordDescription.getWord_Video();
                youTubePlayer.cueVideo(videoId, 0);
                youTubePlayer.play();
            }
        });


        String img_id = des_id + ".jpg";
        Bitmap bitmap = null;
        try {
            InputStream is = assetMan.open("avt/"+img_id);
            bitmap = BitmapFactory.decodeStream(is);
        } catch (IOException e) {
            e.printStackTrace();
        }
        animalImage.setImageBitmap(bitmap);

    }



    private void loadData(){
        DatabaseAccess dbAccess = DatabaseAccess.getInstance(getApplicationContext());
        dbAccess.openConn();
        Locale current = getResources().getConfiguration().locale;
        selectWord = dbAccess.getOneWordById(worlDesId,current.toString());
        jpWord = dbAccess.getOneWordByLanguage(String.valueOf(selectWord.getWord_Des_Id()),"3");
        listJpExample = dbAccess.getExampleListByWord(String.valueOf(jpWord.getWord_ID()));
        listSelectExample = dbAccess.getExampleListByWord(String.valueOf(selectWord.getWord_ID()));
        dbAccess.closeConn();
    }

    private Word_Description loadDescription(String wordDesId){
        DatabaseAccess dbAccess = DatabaseAccess.getInstance(getApplicationContext());
        dbAccess.openConn();
        Word_Description word_description = new Word_Description();
        word_description = dbAccess.getWordDes(wordDesId);
        return word_description;
    }

    private void statusBarColor(){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            getWindow().setStatusBarColor(getResources().getColor(R.color.main_color,this.getTheme()));
        }else if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            getWindow().setStatusBarColor(getResources().getColor(R.color.main_color));
        }
    }

//    public void playSound(View view){
//        DatabaseAccess dbAccess = DatabaseAccess.getInstance(getApplicationContext());
//        dbAccess.openConn();
//        int des_id = selectWord.getWord_Des_Id();
//        Word_Description word_des_temp = dbAccess.getWordDes(String.valueOf(des_id));
//        try {
//            MediaPlayer media = new MediaPlayer();
//            media.setDataSource(word_des_temp.getWord_Pronounce());
//            media.setVolume(100,100);
//            if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
//                media.setAudioAttributes(new AudioAttributes.Builder()
//                        .setUsage(AudioAttributes.USAGE_MEDIA)
//                        .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
//                        .build());
//            } else {
//                media.setAudioStreamType(AudioManager.STREAM_MUSIC);
//            }
//            media.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
//                @Override
//                public void onPrepared(MediaPlayer mp) {
//                    if(media.isPlaying()){
//                        media.pause();
//                    }else{
//                        long duration = media.getDuration();
//                        media.start();
//                        playbutton.setImageDrawable(getResources().getDrawable(R.drawable.sound_activate));
//                        new Handler().postDelayed(new Runnable() {
//                            @Override
//                            public void run() {
//                                playbutton.setImageDrawable(getResources().getDrawable(R.drawable.sound_mute));
//                            }
//                        },duration);
//                    }
//                }
//            });
//            media.prepareAsync();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        dbAccess.closeConn();
//
//
//    }

    public void playSound(View view){
        DatabaseAccess dbAccess = DatabaseAccess.getInstance(getApplicationContext());
        dbAccess.openConn();
        int des_id = selectWord.getWord_Des_Id();
        String sound_id = +des_id+"_Pronounce.mp3";
        try {
            AssetFileDescriptor is = assetMan.openFd("pronounce/"+sound_id);
            MediaPlayer media = new MediaPlayer();
            media.setDataSource(is.getFileDescriptor(),is.getStartOffset(),is.getLength());
            media.setVolume(200,200);
            media.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    if(media.isPlaying()){
                        media.pause();
                    }else{
                        long duration = media.getDuration();
                        media.start();
                        playbutton.setImageDrawable(getResources().getDrawable(R.drawable.sound_activate));
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                playbutton.setImageDrawable(getResources().getDrawable(R.drawable.sound_mute));
                            }
                        },duration);
                    }
                }
            });
            media.prepareAsync();
        } catch (IOException e) {
            e.printStackTrace();
        }
        dbAccess.closeConn();
    }


    public void HomeIntent(View view) {
        Intent intent = null;
        if(getIntent.equals("MyEncounter")){
            intent = new Intent(this, MyEncounterActivity.class);
            this.startActivity(intent);
        }else if(getIntent.equals("Search")){
            intent = new Intent(this, SearchActivity.class);
            this.startActivity(intent);
        }
        this.finish();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = null;
        if(getIntent.equals("MyEncounter")){
            intent = new Intent(this, MyEncounterActivity.class);
            this.startActivity(intent);
        }else if(getIntent.equals("Search")){
            intent = new Intent(this, SearchActivity.class);
            this.startActivity(intent);
        }
        this.finish();
    }

    public void saveID(){
        String path = FILE_PATH;
        try {
            File file = new File(path);
            if(!file.exists()){
                file.createNewFile();
            }
            if(data.compareTo("") == 0){
                data = getId;
            }else{
                checkDuplicate();
            }
            FileOutputStream fos = new FileOutputStream(file, false);

            byte buff[] = data.getBytes();
            fos.write(buff,0 ,buff.length);
            fos.close();

        }catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void loadID(){
        String path = FILE_PATH;
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
        if(getdata.length>=1){
            data="";
            for (int i = 0;i < getdata.length;i++) {
                if(getId.compareTo(getdata[i])!= 0){
                    data+=getdata[i] + ",";
                }
            }
            data+=getId;
        }else{
            data+=getId;
        }
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
}