package com.example.ranwildimal;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.ranwildimal.adapter.WordDescriptionAdapter;
import com.example.ranwildimal.database.DatabaseAccess;
import com.example.ranwildimal.model.Example;
import com.example.ranwildimal.model.Word;

import java.util.ArrayList;

public class DescriptionActivity extends AppCompatActivity {

    Toolbar description_toolbar;
    String wordID;
    Word selectWord,jpWord;
    TextView jpTxtWord,selectedTxtWord;
    ArrayList<Example> listSelectExample,listJpExample;
    WordDescriptionAdapter exampleAdapter;
    RecyclerView exampleListView;
    ImageView animalImage;

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

        jpTxtWord = findViewById(R.id.text_description_japanese_word);
        selectedTxtWord = findViewById(R.id.txt_animal_description_word);
        exampleListView = findViewById(R.id.description_exxemple_list);
        animalImage = findViewById(R.id.img_description_animal);

        wordID = String.valueOf(getIntent().getIntExtra("GETID",0));
        loadData();

        jpTxtWord.setText(jpWord.getWord());
        selectedTxtWord.setText(selectWord.getWord());

        exampleAdapter = new WordDescriptionAdapter(listSelectExample,listJpExample,this);
        LinearLayoutManager manager = new LinearLayoutManager(DescriptionActivity.this); //Linear Layout Manager use to handling layout for each Lecturer
        exampleListView.setAdapter(exampleAdapter);
        exampleListView.setLayoutManager(manager);
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

    public void HomeIntent(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        this.startActivity(intent);
        this.finish();
    }
}