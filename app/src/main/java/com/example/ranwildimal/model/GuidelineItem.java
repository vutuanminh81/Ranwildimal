package com.example.ranwildimal.model;

public class GuidelineItem {
    String title;
    int descriptionImg;

    public GuidelineItem() {
    }

    public GuidelineItem(String title, int descriptionImg) {
        this.title = title;
        this.descriptionImg = descriptionImg;
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
}
