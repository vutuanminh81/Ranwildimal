package com.example.ranwildimal;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.example.ranwildimal.database.DatabaseAccess;
import com.example.ranwildimal.model.Word;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

public class DescriptionActivity extends AppCompatActivity {

    Toolbar description_toolbar;
    AssetManager assetMan;
    Word word;
    int wordID;
    ImageButton playbutton;
    @SuppressLint("ResourceType")
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
        ImageView img ;
        img = findViewById(R.id.img_description_animal);
        playbutton = findViewById(R.id.btn_search_back2);
        assetMan = this.getAssets();
        DatabaseAccess dbAccess = DatabaseAccess.getInstance(getApplicationContext());
        dbAccess.openConn();
        wordID = getIntent().getIntExtra("GETID",0);
        word = dbAccess.get1Word(wordID);
        int des_id = word.getWord_Des_Id();
        String img_id = des_id + ".jpg";
        Bitmap bitmap = null;
        try {
            InputStream is = assetMan.open("avt/"+img_id);
            bitmap = BitmapFactory.decodeStream(is);
        } catch (IOException e) {
            e.printStackTrace();
        }
        img.setImageBitmap(bitmap);
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
        wordID = getIntent().getIntExtra("GETID",0);
        word = dbAccess.get1Word(wordID);
        int des_id = word.getWord_Des_Id();
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
    }
}