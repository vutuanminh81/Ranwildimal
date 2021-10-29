package com.example.ranwildimal.database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.ranwildimal.model.Word;

import java.util.ArrayList;
import java.util.List;

public class DatabaseAccess {
    private SQLiteOpenHelper openHelper;
    private SQLiteDatabase db;
    private static DatabaseAccess instance;
    Cursor c = null;

    /**
     * Private constructor so that object creation from outside the class
     * @param context
     */
    private DatabaseAccess(Context context){
        this.openHelper = new DatabaseOpenHelper(context);

    }

    /**
     * Return the single instance of db
     */
    public static DatabaseAccess getInstance(Context context){
        if(instance==null){
            instance = new DatabaseAccess(context);
        }
        return instance;
    }

    /**
     * Open connection to db
     */
    public void openConn(){
        this.db = openHelper.getWritableDatabase();
    }

    /**
     * Close connection to db
     */
    public void closeConn(){
        if(db!=null){
            this.db.close();
        }
    }

    public ArrayList<Word> getWord(){
        c = db.rawQuery("select * from Word where Language_Id = 1", new String[]{});
        StringBuffer buffer = new StringBuffer();
        ArrayList<Word> list = new ArrayList<>();
        while(c.moveToNext()){
            int id = c.getInt(0);
            String word = c.getString(1);
            int language = c.getInt(2);
            int word_des = c.getInt(3);
            int type = c.getInt(4);
            Word new_word = new Word(id,word,language,word_des,type);
            list.add(new_word);
        }
        return list;
    }

    public Word get1Word(){
        c = db.rawQuery("select * from Word where Word_Id = 1", new String[]{});
        StringBuffer buffer = new StringBuffer();
        ArrayList<Word> list = new ArrayList<>();
        while(c.moveToNext()){
            int id = c.getInt(0);
            String word = c.getString(1);
            int language = c.getInt(2);
            int word_des = c.getInt(3);
            int type = c.getInt(4);
            Word new_word = new Word(id,word,language,word_des,type);
            return new_word;
        }
        return null;
    }

    public ArrayList<Word> searchWord(String search){
        c = db.rawQuery("select * from Word where Word like '%"+search+"%'", new String[]{});
        StringBuffer buffer = new StringBuffer();
        ArrayList<Word> list = new ArrayList<>();
        while(c.moveToNext()){
            int id = c.getInt(0);
            String word = c.getString(1);
            int language = c.getInt(2);
            int word_des = c.getInt(3);
            int type = c.getInt(4);
            Word new_word = new Word(id,word,language,word_des,type);
            list.add(new_word);
        }
        return list;
    }
}
