package com.example.ranwildimal;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
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
    String filePath;
    String animal;

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
        setContentView(R.layout.activity_result_success);
        successResultActivity_toolbar = findViewById(R.id.result_success_toolbar);
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
        animal = getIntent().getStringExtra("animalName");
        filePath = getIntent().getStringExtra("filePathImg");

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

        int des_Id = dbAccess.getWordDesIdbyName(animal);
        dbAccess.increaseWordScan(String.valueOf(des_Id));
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
                i.putExtra("IntentFrom","Result");
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
        File dir = new File(filePath);
        dir.delete();
        Intent intent = new Intent(this, CameraActivity.class);
        this.startActivity(intent);
        this.finish();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        File dir = new File(filePath);
        dir.delete();
        Intent intent = new Intent(this, CameraActivity.class);
        this.startActivity(intent);
        this.finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        File dir = new File(filePath);
        dir.delete();
    }

    public void ReportIntent(View view){
        ConnectivityManager connectivityManager = (ConnectivityManager) ResultSuccessActivity.this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo wifiConn = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        NetworkInfo mobileConn = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        if((wifiConn != null && wifiConn.isConnected()) || (mobileConn != null && mobileConn.isConnected())){
            Intent intent = new Intent(this, ReportActivity.class);
            intent.putExtra("NAME",animalName.getText());
            intent.putExtra("Dir",filePath);
            this.startActivity(intent);
        }else{
            AlertDialog.Builder builder1 = new AlertDialog.Builder(ResultSuccessActivity.this);
            builder1.setMessage(R.string.report_msg);
            builder1.setCancelable(true);
            builder1.setPositiveButton(
                    "OK",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                        }
                    });
            AlertDialog alert11 = builder1.create();
            alert11.show();
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