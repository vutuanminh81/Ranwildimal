package com.example.ranwildimal;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ImageButton;

import com.example.ranwildimal.adapter.WordDescriptionAdapter;
import com.example.ranwildimal.database.DatabaseAccess;
import com.example.ranwildimal.model.Word;
import com.example.ranwildimal.model.Example;

import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView;

import java.io.IOException;
import java.io.InputStream;

import java.util.ArrayList;

public class DescriptionActivity extends AppCompatActivity {

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_description);
        description_toolbar = findViewById(R.id.description_toolbar);
        assetMan = this.getAssets();
        //Customize status bar
        statusBarColor();
        //Customize toolbar
        setSupportActionBar(description_toolbar);
        getSupportActionBar().setTitle(null);

        playbutton = findViewById(R.id.btn_des_sound);
        jpTxtWord = findViewById(R.id.text_description_japanese_word);
        selectedTxtWord = findViewById(R.id.txt_animal_description_word);
        exampleListView = findViewById(R.id.description_exxemple_list);
        animalImage = findViewById(R.id.img_description_animal);
        youTubePlayerView = findViewById(R.id.ex_ExampleVideo);

        getLifecycle().addObserver(youTubePlayerView);

        wordID = String.valueOf(getIntent().getIntExtra("GETID",0));
        loadData();

        jpTxtWord.setText(jpWord.getWord());
        selectedTxtWord.setText(selectWord.getWord());

        exampleAdapter = new WordDescriptionAdapter(listSelectExample,listJpExample,this);
        LinearLayoutManager manager = new LinearLayoutManager(DescriptionActivity.this); //Linear Layout Manager use to handling layout for each Lecturer
        exampleListView.setAdapter(exampleAdapter);
        exampleListView.setLayoutManager(manager);

        youTubePlayerView.addYouTubePlayerListener(new AbstractYouTubePlayerListener() {
            @Override
            public void onReady(@NonNull YouTubePlayer youTubePlayer) {
                String videoId = "1HygThMLzGs";
                youTubePlayer.cueVideo(videoId, 0);
            }
        });

        int des_id = selectWord.getWord_Des_Id();
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
        selectWord = dbAccess.getOneWordById(wordID);
        jpWord = dbAccess.getOneWordByLanguage(String.valueOf(selectWord.getWord_Des_Id()),"3");
        listJpExample = dbAccess.getExampleListByWord(String.valueOf(jpWord.getWord_ID()));
        listSelectExample = dbAccess.getExampleListByWord(String.valueOf(selectWord.getWord_ID()));
        dbAccess.closeConn();
    }

    private void statusBarColor(){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            getWindow().setStatusBarColor(getResources().getColor(R.color.main_color,this.getTheme()));
        }else if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            getWindow().setStatusBarColor(getResources().getColor(R.color.main_color));
        }
    }

    public void playSound(View view){
        DatabaseAccess dbAccess = DatabaseAccess.getInstance(getApplicationContext());
        dbAccess.openConn();
        int des_id = selectWord.getWord_Des_Id();
        String sound_id = +des_id+"_Pronounce.mp3";
        try {
            AssetFileDescriptor is = assetMan.openFd("pronounce/"+sound_id);
            MediaPlayer media = new MediaPlayer();
            media.setDataSource(is.getFileDescriptor(),is.getStartOffset(),is.getLength());
            media.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    if(media.isPlaying()){
                        media.pause();
                    }else{
                        media.start();
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
        Intent intent = new Intent(this, MainActivity.class);
        this.startActivity(intent);
        this.finish();
    }
}