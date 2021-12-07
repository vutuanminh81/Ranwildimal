package com.example.ranwildimal;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ranwildimal.adapter.WordAdapter;
import com.example.ranwildimal.database.DatabaseAccess;
import com.example.ranwildimal.model.Word;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class SearchActivity extends AppCompatActivity {

    Toolbar search_toolbar;
    RecyclerView recyclerView;
    WordAdapter wordAdap;
    ArrayList<Word> listWord;
    TextView searchtxt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        search_toolbar = findViewById(R.id.search_toolbar);
        recyclerView = findViewById(R.id.search_history_list);
        searchtxt = findViewById(R.id.txt_search_field);
        //Customize status bar
        statusBarColor();
        //Customize toolbar
        setSupportActionBar(search_toolbar);
        getSupportActionBar().setTitle(null);
        listWord = new ArrayList<>();
        wordAdap = new WordAdapter(listWord, SearchActivity.this); //Call LecturerAdapter to set data set and show data
        LinearLayoutManager manager = new LinearLayoutManager(SearchActivity.this); //Linear Layout Manager use to handling layout for each Lecturer
        recyclerView.setAdapter(wordAdap);
        recyclerView.setLayoutManager(manager);
        searchtxt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                filter(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }

    private void filter(String s) {
        ArrayList<Word> newlist = new ArrayList<>();
        if (s.isEmpty()) {
            recyclerView.setVisibility(View.INVISIBLE);
        } else {
            DatabaseAccess dbAccess = DatabaseAccess.getInstance(getApplicationContext());
            dbAccess.openConn();
            newlist = dbAccess.searchWord(s);
            if (!newlist.isEmpty()) {
                recyclerView.setVisibility(View.VISIBLE);
            } else {
                recyclerView.setVisibility(View.INVISIBLE);
            }
            wordAdap.ArrayFilter(newlist);
        }


    }

    private void statusBarColor() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().setStatusBarColor(getResources().getColor(R.color.main_color, this.getTheme()));
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(getResources().getColor(R.color.main_color));
        }
    }

    public void HomeIntent(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        this.startActivity(intent);
    }


}