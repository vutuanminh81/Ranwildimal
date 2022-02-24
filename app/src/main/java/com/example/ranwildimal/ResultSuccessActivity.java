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
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.ranwildimal.database.DatabaseAccess;
import com.example.ranwildimal.model.Word;

import org.opencv.android.Utils;
import org.opencv.core.Mat;
import org.opencv.imgproc.Imgproc;

import java.io.File;
import java.util.ArrayList;

public class ResultSuccessActivity extends AppCompatActivity {

    Toolbar successResultActivity_toolbar;
    ImageView currentImage;
    TextView animalName;
    Button btnViewDetail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result_success);
        successResultActivity_toolbar = findViewById(R.id.result_success_toolbar);
        currentImage = findViewById(R.id.img_result_current_image);
        animalName = findViewById(R.id.txt_result_success_name);
        btnViewDetail = findViewById(R.id.btn_success_view);

        //Customize status bar
        statusBarColor();
        //Customize toolbar
        setSupportActionBar(successResultActivity_toolbar);
        getSupportActionBar().setTitle(null);
        String animal = getIntent().getStringExtra("animalName");
        String filePath = getIntent().getStringExtra("filePathImg");
        Bitmap bmImg = BitmapFactory.decodeFile(filePath);

//        Mat mat = new Mat();
//        Mat mat2 = new Mat();
//        Utils.bitmapToMat(bmImg,mat);
//        Imgproc.equalizeHist(mat,mat2);
//        Utils.matToBitmap(mat,bmImg);

        currentImage.setImageBitmap(bmImg);
        File dir = new File(filePath);
        dir.delete();
        btnViewDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatabaseAccess dbAccess = DatabaseAccess.getInstance(getApplicationContext());
                dbAccess.openConn();
                Intent i = new Intent(ResultSuccessActivity.this, DescriptionActivity.class);
                int id = 0;
                ArrayList<Word> list = dbAccess.getWord();
                for (Word w : list){
                    if(w.getWord().toLowerCase().equals(animal.toLowerCase())){
                        id = w.getWord_ID();
                        break;
                    }
                }
                i.putExtra("GETID",id);
                startActivity(i);
            }
        });
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