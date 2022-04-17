package com.example.ranwildimal;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import java.io.File;

public class ResultErrorActivity extends AppCompatActivity {

    Toolbar errorResultActivity_toolbar;
    ImageView currentImage;
    Button takePictureButton;
    String filePath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result_error);
        errorResultActivity_toolbar = findViewById(R.id.result_error_toolbar);
        currentImage = findViewById(R.id.img_result_current_error_image);
        takePictureButton = findViewById(R.id.btn_error_take_picture);
        //Customize status bar
        statusBarColor();
        //Customize toolbar
        setSupportActionBar(errorResultActivity_toolbar);
        getSupportActionBar().setTitle(null);
        filePath = getIntent().getStringExtra("filePathImg");
        Bitmap bmImg = BitmapFactory.decodeFile(filePath);
        currentImage.setImageBitmap(bmImg);
        takePictureButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ResultErrorActivity.this, CameraActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private void statusBarColor(){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            getWindow().setStatusBarColor(getResources().getColor(R.color.error_message,this.getTheme()));
        }else if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            getWindow().setStatusBarColor(getResources().getColor(R.color.error_message));
        }
    }

    public void HomeIntent(View view) {
        Intent intent = new Intent(this, CameraActivity.class);
        File dir = new File(filePath);
        dir.delete();
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
        ConnectivityManager connectivityManager = (ConnectivityManager) ResultErrorActivity.this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo wifiConn = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        NetworkInfo mobileConn = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        if((wifiConn != null && wifiConn.isConnected()) || (mobileConn != null && mobileConn.isConnected())){
            Intent intent = new Intent(this, ReportActivity.class);
            intent.putExtra("Dir",filePath);
            this.startActivity(intent);
        }else{
            AlertDialog.Builder builder1 = new AlertDialog.Builder(ResultErrorActivity.this);
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
}