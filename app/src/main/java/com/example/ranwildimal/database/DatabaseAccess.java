package com.example.ranwildimal.database;

import android.content.Context;
import android.content.res.AssetManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;

import com.example.ranwildimal.model.Example;
import com.example.ranwildimal.model.ExampleModel;
import com.example.ranwildimal.model.Word;
import com.example.ranwildimal.model.WordModel;
import com.example.ranwildimal.model.Word_Description;
import com.example.ranwildimal.model.Word_DescriptionModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class DatabaseAccess {
    private SQLiteOpenHelper openHelper;
    private SQLiteDatabase db;
    private static DatabaseAccess instance;
    Cursor c = null;

    WordModel wordModel;
    ExampleModel exampleModel;
    Word_DescriptionModel wordDescriptionModel;


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
        wordModel = new WordModel();
        return wordModel.getWord(c,db);
    }

    public ArrayList<Example> getExample(){
        exampleModel = new ExampleModel();
        return exampleModel.getExample(c,db);
    }

    public ArrayList<Word_Description> getWordDes(){
        wordDescriptionModel = new Word_DescriptionModel();
        return wordDescriptionModel.getWordDes(c,db);
    }

    public ArrayList<Example> getExampleListByWord(String wordId){
        exampleModel = new ExampleModel();
        return exampleModel.getExampleListByWord(wordId,c,db);
    }

    public Word getOneWordById(String wordDesId, String locale){
        wordModel = new WordModel();
        return wordModel.getOneWordById(wordDesId,locale,c,db);
    }

    public Word getOneWordByLanguage(String wordDesId, String languageId){
        wordModel = new WordModel();
        return wordModel.getOneWordByLanguage(wordDesId, languageId, c, db);
    }

    public ArrayList<Word> searchWord(String search, String locale){
        wordModel = new WordModel();
        return wordModel.searchWord(search,locale,c,db);
    }

    public Word_Description getWordDes(String id){
        wordDescriptionModel = new Word_DescriptionModel();
        return wordDescriptionModel.getWordDes(id,c,db);
    }

    public void increaseWordSearch(String id){
        wordDescriptionModel = new Word_DescriptionModel();
        wordDescriptionModel.increaseWordSearch(id,c,db);
    }

    public void increaseWordScan(String id){
        wordDescriptionModel = new Word_DescriptionModel();
        wordDescriptionModel.increaseWordScan(id,c,db);
    }

    public void resetScanSearch(String id){
        wordDescriptionModel = new Word_DescriptionModel();
        wordDescriptionModel.resetScanSearch(id,c,db);
    }

    public void updateWordDes(Word_Description word){
        wordDescriptionModel = new Word_DescriptionModel();
        wordDescriptionModel.updateWordDes(word,c,db);
    }

    public void updateWord(Word word){
        wordModel = new WordModel();
        wordModel.updateWord(word,c,db);
    }

    public int getWordDesIdbyName(String id){
        wordModel = new WordModel();
        return wordModel.getWordDesIdbyName(id,c,db);
    }
}
