package com.example.ranwildimal;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;

import com.example.ranwildimal.database.DatabaseAccess;
import com.example.ranwildimal.model.Example;
import com.example.ranwildimal.model.Word;
import com.example.ranwildimal.model.Word_Description;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class SplashScreenActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        statusBarColor();
        ConnectivityManager connectivityManager = (ConnectivityManager) SplashScreenActivity.this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo wifiConn = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        if((wifiConn != null && wifiConn.isConnected())){
            System.out.println(" >>>>>>>>>>>>> Network Connected");
            updateDatafromFS();
        }else{
            System.out.println(" >>>>>>>>>>>>> Network DisConnected");
        }
        new Handler().postDelayed(this::slashScreen,3000);
    }
    /**
     * method is used to make activity_slash_screen layout appear 3 seconds whenever the app start
     */
    public void slashScreen(){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }
    private void statusBarColor(){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            getWindow().setStatusBarColor(getResources().getColor(R.color.main_color,this.getTheme()));
        }else if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            getWindow().setStatusBarColor(getResources().getColor(R.color.main_color));
        }
    }

    /*
    Get data from Firestore to SQLite
     */
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
                                        word_des.setWord_Status(1);
                                        word_descriptions.add(word_des);
                                        i++;
                                    }
                                }
                                int j = 0;
                                for (Word_Description w :word_descriptions) {
                                    dbAccess.openConn();
                                    dbAccess.updateWordDes(w);
                                    dbAccess.closeConn();
                                    Map<String, Object> word_des_obj = new HashMap<>();
                                    word_des_obj.put("Word_Des_Id",w.getWord_Des_Id());
                                    word_des_obj.put("Word_Pronounce",w.getWord_Pronounce());
                                    word_des_obj.put("Word_Video",w.getWord_Video());
                                    word_des_obj.put("Word_Image",w.getWord_Image());
                                    word_des_obj.put("Word_Status",1);
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
                                        System.out.println(">>>>>>>>>>>>>>>>"+doc.get("Word_Des_Id",Integer.class)+">>>>>>>>>>>>>>>>"+doc.get("Word_Des_Id",Integer.class));
                                        Word word_des = new Word();
                                        word_des.setWord_Des_Id(doc.get("Word_Des_Id",Integer.class));
                                        word_des.setWord_ID(doc.get("Word_Id",Integer.class));
                                        word_des.setWord(doc.getString("Word"));
                                        word_des.setLanguage_Id(doc.get("Language_Id",Integer.class));
                                        word_des.setWord_Status(1);
                                        word.add(word_des);
                                        i++;
                                    }
                                }
                                int j = 0;
                                for (Word w :word) {
                                    dbAccess.openConn();
                                    dbAccess.updateWord(w);
                                    dbAccess.closeConn();
                                    Map<String, Object> word_des_obj = new HashMap<>();
                                    word_des_obj.put("Word_Des_Id",w.getWord_Des_Id());
                                    word_des_obj.put("Word_Id",w.getWord_ID());
                                    word_des_obj.put("Word",w.getWord());
                                    word_des_obj.put("Language_Id",w.getLanguage_Id());
                                    word_des_obj.put("Word_Status",1);
                                    fs.collection("Word")
                                            .document(doc_Id[j])
                                            .update(word_des_obj);
                                    j++;
                                }
                            }
                        });
                ArrayList<Word> updateword = new ArrayList<>();
                ArrayList<Word_Description> updatedes = new ArrayList<>();
                ArrayList<Word_Description> fsdes = new ArrayList<>();
                updatedes = dbAccess.getWordDes();
                ArrayList<Word_Description> finalUpdatedes = updatedes;
                fs.collection("Word_Description").orderBy("Word_Des_Id", Query.Direction.ASCENDING)
                        .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
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
                                word_des.setWord_Status(1);
                                word_des.setNum_of_Scan(doc.get("num_Of_Scan",Integer.class));
                                word_des.setNum_of_Search(doc.get("num_Of_Search",Integer.class));
                                fsdes.add(word_des);
                                i++;
                            }
                            for (int k=0; k < finalUpdatedes.size();k++){
                                int update_search = fsdes.get(k).getNum_of_Search() + finalUpdatedes.get(k).getNum_of_Search();
                                int update_scan = fsdes.get(k).getNum_of_Scan() + finalUpdatedes.get(k).getNum_of_Scan();
                                Map<String, Object> word_des_obj = new HashMap<>();
                                word_des_obj.put("num_Of_Scan",update_scan);
                                word_des_obj.put("num_Of_Search",update_search);
                                fs.collection("Word_Description")
                                        .document(doc_Id[k])
                                        .update(word_des_obj)
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {

                                            }
                                        });
                                dbAccess.resetScanSearch(String.valueOf(finalUpdatedes.get(k).getWord_Des_Id()));
                            }

                        }
                    }
                });
            }
        });
        thread.run();
    }


}