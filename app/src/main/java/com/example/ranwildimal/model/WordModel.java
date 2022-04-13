package com.example.ranwildimal.model;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

public class WordModel {
    ArrayList<Word> list;

    public ArrayList<Word> getWord(Cursor c, SQLiteDatabase db){
        c = db.rawQuery("select * from Word", new String[]{});
        StringBuffer buffer = new StringBuffer();
        list = new ArrayList<>();
        while(c.moveToNext()){
            int id = c.getInt(0);
            String word = c.getString(1);
            int language = c.getInt(2);
            int word_des = c.getInt(3);
            int type = c.getInt(4);
            Word new_word = new Word(id,word,language,word_des,type,1);
            list.add(new_word);
        }
        c.close();
        return list;
    }

    public Word getOneWordById(String wordDesId, String locale, Cursor c, SQLiteDatabase db){
        int langId = 1;
        if(locale.equals("vi") || locale.equalsIgnoreCase("vi_VN")){
            langId = 1;
        }else if(locale.equals("en") || locale.equalsIgnoreCase("en_US")){
            langId = 2;
        }else if(locale.equals("ja") || locale.equalsIgnoreCase("ja_JP")){
            langId = 3;
        }
        c = db.rawQuery("select * from Word where Word_Des_Id = " + wordDesId + " and Language_Id = " + langId, new String[]{});
        StringBuffer buffer = new StringBuffer();
        list = new ArrayList<>();
        while(c.moveToNext()){
            int w_id = c.getInt(0);
            String word = c.getString(1);
            int language = c.getInt(2);
            int word_des = c.getInt(3);
            int type = c.getInt(4);
            Word new_word = new Word(w_id,word,language,word_des,type,1);
            return new_word;
        }
        c.close();
        return null;
    }

    public Word getOneWordByLanguage(String wordDesId, String languageId, Cursor c, SQLiteDatabase db){
        c = db.rawQuery("select * from Word where Word_Des_Id = " + wordDesId + " and Language_Id = " + languageId, new String[]{});
        StringBuffer buffer = new StringBuffer();
        ArrayList<Word> list = new ArrayList<>();
        while(c.moveToNext()){
            int id = c.getInt(0);
            String word = c.getString(1);
            int language = c.getInt(2);
            int word_des = c.getInt(3);
            int type = c.getInt(4);
            Word new_word = new Word(id,word,language,word_des,type,1);
            return new_word;
        }
        c.close();
        return null;
    }

    public ArrayList<Word> searchWord(String search, String locale, Cursor c, SQLiteDatabase db){
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
            Word new_word = new Word(id,word,language,word_des,type,1);
            list.add(new_word);
        }
        c.close();
        return list;
    }

    public void updateWord(Word word, Cursor c, SQLiteDatabase db){
        c = db.rawQuery("update Word set Word = ?, Word_Des_Id = ?, Word_Status = 1, Word_Type_Id = ?, Language_Id = ? where Word_Id = ?", new String[]{word.getWord(), String.valueOf(word.getWord_Des_Id()), String.valueOf(word.getWord_Type_Id()),String.valueOf(word.getLanguage_Id()), String.valueOf(word.getWord_ID())});
        StringBuffer buffer = new StringBuffer();
        c.moveToFirst();
        c.close();
    }

    public int getWordDesIdbyName(String id, Cursor c, SQLiteDatabase db){
        String new_id = id.toLowerCase();
        c = db.rawQuery("select * from Word where Lower(Word) Like '%" +new_id+"%'", new String[]{});
        StringBuffer buffer = new StringBuffer();
        int des_id = 0;
        while(c.moveToNext()){
            des_id = c.getInt(3);
        }
        c.close();
        return des_id;
    }
}
