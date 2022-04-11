package com.example.ranwildimal.model;

public class GuidelineItem {
    String title;
    int descriptionImg;
    String number;



    public GuidelineItem() {
    }

    public GuidelineItem(String title, int descriptionImg, String number) {
        this.title = title;
        this.descriptionImg = descriptionImg;
        this.number = number;
    }

    public String getTitle() {
        return title;
    }

    public int getDescriptionImg() {
        return descriptionImg;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescriptionImg(int descriptionImg) {
        this.descriptionImg = descriptionImg;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

}
