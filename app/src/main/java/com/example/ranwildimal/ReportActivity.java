package com.example.ranwildimal;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ranwildimal.database.DatabaseAccess;
import com.example.ranwildimal.model.Word;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.core.Repo;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.lang.reflect.Array;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class ReportActivity extends AppCompatActivity {

    String filePath,animal;
    ImageView currentImage;
    TextView actualRes;
    EditText expectedRes,note;
    Spinner spinner;
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
        setContentView(R.layout.activity_report);
        statusBarColor();
        animal = getIntent().getStringExtra("NAME");
        filePath = getIntent().getStringExtra("Dir");
        currentImage = findViewById(R.id.img_result_current_image_report);
        Bitmap bmImg = BitmapFactory.decodeFile(filePath);
        currentImage.setImageBitmap(bmImg);
        actualRes = findViewById(R.id.txt_actual_result);
        if(animal != null){
            actualRes.setText(animal);
        }else{
            actualRes.setText(R.string.unidentify);
        }

        Locale current = getResources().getConfiguration().locale;
        DatabaseAccess dbAccess = DatabaseAccess.getInstance(getApplicationContext());
        dbAccess.openConn();
        ArrayList<Word> biglist = new ArrayList<>();
        biglist = dbAccess.getWord();
        int langId = 1;
        String locale = current.toString();
        if(locale.equals("vi")){
            langId = 1;
        }else if(locale.equals("en")){
            langId = 2;
        }else if(locale.equals("ja")){
            langId = 3;
        }
        ArrayList<String> filterlist = new ArrayList<>();
        for (Word w: biglist) {
            if(w.getLanguage_Id() == langId){
                filterlist.add(w.getWord());
            }
        }
        ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, filterlist);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner = (Spinner) findViewById(R.id.animal_spinner);
        spinner.setAdapter(adapter);
        note = findViewById(R.id.txt_note);

    }

    public void HomeIntent(View view) {
        ReportActivity.this.finish();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        ReportActivity.this.finish();
    }

    public void Report(View view){
        File dir = new File(filePath);
        dir.delete();
        DatabaseAccess dbAccess = DatabaseAccess.getInstance(getApplicationContext());
        dbAccess.openConn();
        Date d = Calendar.getInstance().getTime(); // Current time
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss"); // Set your date format
        String currentData = sdf.format(d); // Get Date String according to date format



        // Create a new user with a first and last name
        Map<String, Object> user = new HashMap<>();
        user.put("Report_Id", "");
        if(animal != null){
            user.put("Actual_Word_Id",dbAccess.getWordDesIdbyName(animal));
        }else{
            user.put("Actual_Word_Id","Unidentify");
        }
        user.put("Expected_Word_Id", dbAccess.getWordDesIdbyName(spinner.getSelectedItem().toString()));
        user.put("Day_Report",currentData);
        user.put("Note",note.getText().toString());
        user.put("Status",1);
        user.put("Report_Image","");

        FirebaseFirestore fs = FirebaseFirestore.getInstance();
        startLoading();
    // Add a new document with a generated ID
        fs.collection("Report")
                .add(user)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        FirebaseStorage storage = FirebaseStorage.getInstance("gs://ranwildimal.appspot.com");
                        StorageReference storageRef = storage.getReference();


                        // Create a reference to 'images/mountains.jpg'
                        StorageReference mountainImagesRef = storageRef.child("Report/"+documentReference.getId()+".jpg");

                        // While the file names are the same, the references point to different files
                        mountainImagesRef.getName().equals(mountainImagesRef.getName());    // true
                        mountainImagesRef.getPath().equals(mountainImagesRef.getPath());    // false
                        // Get the data from an ImageView as bytes
                        currentImage.setDrawingCacheEnabled(true);
                        currentImage.buildDrawingCache();

                        Bitmap bitmap = ((BitmapDrawable) currentImage.getDrawable()).getBitmap();
                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                        byte[] data = baos.toByteArray();

                        UploadTask uploadTask = mountainImagesRef.putBytes(data);
                        Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                            @Override
                            public Task<Uri> then(@androidx.annotation.NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                                if (!task.isSuccessful()) {
                                    throw task.getException();
                                }

                                // Continue with the task to get the download URL
                                return mountainImagesRef.getDownloadUrl();
                            }
                        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                            @Override
                            public void onComplete(@androidx.annotation.NonNull Task<Uri> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(ReportActivity.this,"Report successful !",Toast.LENGTH_SHORT).show();
                                    Uri downloadUri = task.getResult();
                                    String getUrlImage = downloadUri.toString();

                                    Map<String ,Object> updateData =  new HashMap<>();
                                    updateData.put("Report_Id",documentReference.getId());
                                    updateData.put("Report_Image",getUrlImage);

                                    fs.collection("Report").document(documentReference.getId())
                                            .update(updateData);
                                    finish();
                                } else {
                                    // Handle failures
                                    // ...
                                }
                            }
                        });
                    }
                });
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

    private void startLoading(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        builder.setView(inflater.inflate(R.layout.activity_loading,null));
        builder.setCancelable(false);
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void statusBarColor(){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            getWindow().setStatusBarColor(getResources().getColor(R.color.error_message,this.getTheme()));
        }else if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            getWindow().setStatusBarColor(getResources().getColor(R.color.error_message));
        }
    }
}