package com.example.ranwildimal.model;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

public class Word_DescriptionModel {
    ArrayList<Word_Description> list;

    public ArrayList<Word_Description> getWordDes(Cursor c, SQLiteDatabase db){
        c = db.rawQuery("select * from Word_Description", new String[]{});
        StringBuffer buffer = new StringBuffer();
        list = new ArrayList<>();
        while(c.moveToNext()){
            int id = c.getInt(0);
            String pronounce = c.getString(1);
            String video = c.getString(2);
            String image = c.getString(3);
            int scan = c.getInt(4);
            int search = c.getInt(6);
            int status = 1;
            Word_Description new_word = new Word_Description(id,pronounce,video,image,status,scan,search);
            list.add(new_word);
        }
        c.close();
        return list;
    }

    public Word_Description getWordDes(String id, Cursor c, SQLiteDatabase db){
        Word_Description word_des = new Word_Description();
        c = db.rawQuery("select * from Word_Description where Word_Des_Id = "+id, new String[]{});
        StringBuffer buffer = new StringBuffer();
        while(c.moveToNext()){
            word_des.setWord_Des_Id(c.getInt(0));
            word_des.setWord_Pronounce(c.getString(1));
            word_des.setWord_Video(c.getString(2));
            word_des.setWord_Image(c.getString(3));
            word_des.setNum_of_Scan(c.getInt(4));
            word_des.setWord_Status(1);
            word_des.setNum_of_Search(c.getInt(6));
        }
        c.close();
        return word_des;
    }

    public void increaseWordSearch(String id, Cursor c, SQLiteDatabase db){
        c = db.rawQuery("update Word_Description set Num_Of_Search = Num_Of_Search + 1  where Word_Des_Id = "+id, null);
        StringBuffer buffer = new StringBuffer();
        c.moveToFirst();
        c.close();
    }

    public void increaseWordScan(String id, Cursor c, SQLiteDatabase db){
        c = db.rawQuery("update Word_Description set Num_Of_Scan = Num_Of_Scan + 1  where Word_Des_Id = "+id, null);
        StringBuffer buffer = new StringBuffer();
        c.moveToFirst();
        c.close();
    }

    public void resetScanSearch(String id, Cursor c, SQLiteDatabase db){
        c = db.rawQuery("update Word_Description set Num_Of_Scan = 0, Num_Of_Search = 0  where Word_Des_Id = "+id, null);
        StringBuffer buffer = new StringBuffer();
        c.moveToFirst();
        c.close();
    }

    public void updateWordDes(Word_Description word, Cursor c, SQLiteDatabase db){
        c = db.rawQuery("update Word_Description set Word_Image = ?, Word_Pronounce = ?, Word_Video = ?, Word_Status = 1  where Word_Des_Id = ?", new String[]{word.getWord_Image(),word.getWord_Pronounce(),word.getWord_Video(),String.valueOf(word.getWord_Des_Id())});
        StringBuffer buffer = new StringBuffer();
        c.moveToFirst();
        c.close();
    }
}
