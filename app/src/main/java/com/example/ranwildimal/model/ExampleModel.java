package com.example.ranwildimal.model;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

public class ExampleModel {
    ArrayList<Example> list;

    public ArrayList<Example> getExample(Cursor c, SQLiteDatabase db){
        c = db.rawQuery("select * from Example", new String[]{});
        StringBuffer buffer = new StringBuffer();
        list = new ArrayList<>();
        while(c.moveToNext()){
            int id = c.getInt(0);
            String example = c.getString(1);
            int word_id = c.getInt(2);

            Example new_ex = new Example(id,example,word_id);
            list.add(new_ex);
        }
        c.close();
        return list;
    }

    public ArrayList<Example> getExampleListByWord(String wordId, Cursor c, SQLiteDatabase db){
        c = db.rawQuery("select * from Example where Word_Id = "+wordId, new String[]{});
        StringBuffer buffer = new StringBuffer();
        list = new ArrayList<>();
        while(c.moveToNext()){
            int id = c.getInt(0);
            String example = c.getString(1);
            int word_id = c.getInt(2);
            Example new_example = new Example(id,example,word_id);
            list.add(new_example);
        }
        c.close();
        return list;
    }
}
