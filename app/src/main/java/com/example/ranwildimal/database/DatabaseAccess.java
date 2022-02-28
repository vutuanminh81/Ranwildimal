package com.example.ranwildimal.database;

import android.content.Context;
import android.content.res.AssetManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;

import com.example.ranwildimal.model.Example;
import com.example.ranwildimal.model.Word;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

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
        c = db.rawQuery("select * from Word", new String[]{});
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

    public ArrayList<Example> getExampleListByWord(String wordId){
        c = db.rawQuery("select * from Example where Word_Id = "+wordId, new String[]{});
        StringBuffer buffer = new StringBuffer();
        ArrayList<Example> list = new ArrayList<>();
        while(c.moveToNext()){
            int id = c.getInt(0);
            String example = c.getString(1);
            int word_id = c.getInt(2);
            Example new_example = new Example(id,example,word_id);
            list.add(new_example);
        }
        return list;
    }

    public Word getOneWordById(String wordDesId, String locale){
        int langId = 1;
        if(locale.equals("vi")){
            langId = 1;
        }else if(locale.equals("en")){
            langId = 2;
        }else if(locale.equals("ja")){
            langId = 3;
        }
        c = db.rawQuery("select * from Word where Word_Des_Id = " + wordDesId + " and Language_Id = " + langId, new String[]{});
        StringBuffer buffer = new StringBuffer();
        ArrayList<Word> list = new ArrayList<>();
        while(c.moveToNext()){
            int w_id = c.getInt(0);
            String word = c.getString(1);
            int language = c.getInt(2);
            int word_des = c.getInt(3);
            int type = c.getInt(4);
            Word new_word = new Word(w_id,word,language,word_des,type);
            return new_word;
        }
        return null;
    }

    public Word getOneWordByLanguage(String wordDesId, String languageId){
        c = db.rawQuery("select * from Word where Word_Des_Id = " + wordDesId + " and Language_Id = " + languageId, new String[]{});
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


    public ArrayList<Word> searchWord(String search, String locale){
        int langId = 1;
        if(locale.equals("vi")){
            langId = 1;
        }else if(locale.equals("en")){
            langId = 2;
        }else if(locale.equals("ja")){
            langId = 3;
        }
        c = db.rawQuery("select * from Word where Word like '%"+search+"%' and Language_Id = "+langId, new String[]{});
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


    public int searchAnimalbyname(String search){
        int id = 0;
        c = db.rawQuery("select * from Word where UPPER(Word) like UPPER('%"+search+"%')and Language_Id = 2", new String[]{});
        StringBuffer buffer = new StringBuffer();
        ArrayList<Word> list = new ArrayList<>();
        while(c.moveToNext()){
            id = c.getInt(0);
        }
        return id;
    }
}
