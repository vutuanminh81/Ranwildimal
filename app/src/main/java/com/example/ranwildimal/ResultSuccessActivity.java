package com.example.ranwildimal;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class ResultSuccessActivity extends AppCompatActivity {

    Toolbar successResultActivity_toolbar;
    ImageView currentImage;
    TextView animalName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result_success);
        successResultActivity_toolbar = findViewById(R.id.result_success_toolbar);
        currentImage = findViewById(R.id.img_result_current_image);
        animalName = findViewById(R.id.txt_result_success_name);
        //Customize status bar
        statusBarColor();
        //Customize toolbar
        setSupportActionBar(successResultActivity_toolbar);
        getSupportActionBar().setTitle(null);
//        byte[] byteArray = getIntent().getByteArrayExtra("imgBitmap");
//        Bitmap bmp = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
        String animal = getIntent().getStringExtra("animalName");
//        currentImage.setImageBitmap(bmp);
        animalName.setText(animal);
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
}