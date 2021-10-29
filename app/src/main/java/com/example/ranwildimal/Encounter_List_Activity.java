package com.example.ranwildimal;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.example.ranwildimal.adapter.WordAdapter;
import com.example.ranwildimal.database.DatabaseAccess;
import com.example.ranwildimal.model.Word;

import java.util.ArrayList;

public class Encounter_List_Activity extends AppCompatActivity {

    Toolbar search_toolbar;
    RecyclerView recyclerView;
    WordAdapter wordAdap;
    ArrayList<Word> listWord;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_encounter_list);
        recyclerView = findViewById(R.id.id_Encounter_Guideline);
    }

}