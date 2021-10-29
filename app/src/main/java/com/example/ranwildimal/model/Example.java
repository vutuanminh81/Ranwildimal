package com.example.ranwildimal.model;

public class Example {
    int Ex_Id;
    String Example;
    int Word_Id;

    public int getEx_Id() {
        return Ex_Id;
    }

    public String getExample() {
        return Example;
    }

    public int getWord_Id() {
        return Word_Id;
    }

    public void setEx_Id(int ex_Id) {
        Ex_Id = ex_Id;
    }

    public void setExample(String example) {
        Example = example;
    }

    public void setWord_Id(int word_Id) {
        Word_Id = word_Id;
    }

    public Example() {
    }

    public Example(int ex_Id, String example, int word_Id) {
        Ex_Id = ex_Id;
        Example = example;
        Word_Id = word_Id;
    }
}
