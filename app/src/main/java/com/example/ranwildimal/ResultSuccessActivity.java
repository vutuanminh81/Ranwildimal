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
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.imgproc.Imgproc;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class ResultSuccessActivity extends AppCompatActivity {

    Toolbar successResultActivity_toolbar;
    ImageView currentImage,test;
    TextView animalName;
    Button btnViewDetail;
    Word getWord;
    ArrayList<Word> list = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result_success);
        successResultActivity_toolbar = findViewById(R.id.result_success_toolbar);
        currentImage = findViewById(R.id.img_result_current_image);
        animalName = findViewById(R.id.txt_result_success_name);
        btnViewDetail = findViewById(R.id.btn_success_view);
        test = findViewById(R.id.img_result_app_image);
        //open DB connection
        DatabaseAccess dbAccess = DatabaseAccess.getInstance(getApplicationContext());
        dbAccess.openConn();

        //Customize status bar
        statusBarColor();
        //Customize toolbar
        setSupportActionBar(successResultActivity_toolbar);
        getSupportActionBar().setTitle(null);
        String animal = getIntent().getStringExtra("animalName");
        String filePath = getIntent().getStringExtra("filePathImg");

        Bitmap bmImg = BitmapFactory.decodeFile(filePath);
        Bitmap testImg = BitmapFactory.decodeFile(filePath);
        test.setImageBitmap(testImg);

        Intent i = new Intent(ResultSuccessActivity.this, DescriptionActivity.class);
        list = dbAccess.getWord();
        Mat equ = new Mat();
        Utils.bitmapToMat(bmImg,equ);

        // Applying color
        Imgproc.cvtColor(equ, equ, Imgproc.COLOR_BGR2YCrCb);
        List<Mat> channels = new ArrayList<Mat>();

        // Splitting the channels
        Core.split(equ, channels);

        // Equalizing the histogram of the image
        Imgproc.equalizeHist(channels.get(0), channels.get(0));
        Core.merge(channels, equ);
        Imgproc.cvtColor(equ, equ, Imgproc.COLOR_YCrCb2BGR);

        Utils.matToBitmap(equ,bmImg);

        currentImage.setImageBitmap(bmImg);
        File dir = new File(filePath);
        dir.delete();
        btnViewDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int id = 0;
                for (Word w : list){
                    if(w.getWord().toLowerCase().equals(animal.toLowerCase())){
                        getWord = w;
                        id = w.getWord_Des_Id();
                        break;
                    }
                }
                i.putExtra("GETID",id);
                startActivity(i);
            }
        });
        Locale current = getResources().getConfiguration().locale;
        String animalCurrent = "";
        for (Word w : list){
            if(w.getWord().toLowerCase().equals(animal.toLowerCase())){
                getWord = dbAccess.getOneWordById(String.valueOf(w.getWord_Des_Id()),current.toString());
                break;
            }
        }
        animalName.setText(getWord.getWord());

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