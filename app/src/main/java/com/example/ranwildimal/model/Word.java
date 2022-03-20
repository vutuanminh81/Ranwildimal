package com.example.ranwildimal.model;

public class Word {
    private int Word_ID;

    public int getWord_ID() {
        return Word_ID;
    }

    public void setWord_ID(int word_ID) {
        Word_ID = word_ID;
    }

    public String getWord() {
        return Word;
    }

    public void setWord(String word) {
        Word = word;
    }

    public int getLanguage_Id() {
        return Language_Id;
    }

    public void setLanguage_Id(int language_Id) {
        Language_Id = language_Id;
    }

    public int getWord_Des_Id() {
        return Word_Des_Id;
    }

    public void setWord_Des_Id(int word_Des_Id) {
        Word_Des_Id = word_Des_Id;
    }

    public int getWord_Type_Id() {
        return Word_Type_Id;
    }

    public void setWord_Type_Id(int word_Type_Id) {
        Word_Type_Id = word_Type_Id;
    }

    public int getWord_Status() {
        return Word_Status;
    }

    public void setWord_Status(int word_Status) {
        Word_Status = word_Status;
    }

    public int Word_Status;



    private String Word;
    private int Language_Id;
    private int Word_Des_Id;
    private int Word_Type_Id;

    public Word() {
    }

    public Word(int word_ID, String word, int language_Id, int word_Des_Id, int word_Type_Id,int word_Status) {
        Word_ID = word_ID;
        Word = word;
        Language_Id = language_Id;
        Word_Des_Id = word_Des_Id;
        Word_Type_Id = word_Type_Id;
        Word_Status = word_Status;
    }
}
