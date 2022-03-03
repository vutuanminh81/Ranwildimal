package com.example.ranwildimal.model;

public class Word_Description {
    int Word_Des_Id;
    String Word_Pronounce;
    String Word_Video;
    String Word_Image;
    int Word_Status = 1;

    public int getWord_Des_Id() {
        return Word_Des_Id;
    }

    public String getWord_Pronounce() {
        return Word_Pronounce;
    }

    public String getWord_Video() {
        return Word_Video;
    }

    public String getWord_Image() {
        return Word_Image;
    }

    public int getWord_Status() {
        return Word_Status;
    }

    public void setWord_Des_Id(int word_Des_Id) {
        Word_Des_Id = word_Des_Id;
    }

    public void setWord_Pronounce(String word_Pronounce) {
        Word_Pronounce = word_Pronounce;
    }

    public void setWord_Video(String word_Video) {
        Word_Video = word_Video;
    }

    public void setWord_Image(String word_Image) {
        Word_Image = word_Image;
    }

    public void setWord_Status(int word_Status) {
        Word_Status = word_Status;
    }

    public Word_Description() {
    }

    public Word_Description(int word_Des_Id, String word_Pronounce, String word_Video, String word_Image, int word_Status) {
        Word_Des_Id = word_Des_Id;
        Word_Pronounce = word_Pronounce;
        Word_Video = word_Video;
        Word_Image = word_Image;
        Word_Status = word_Status;
    }
}
