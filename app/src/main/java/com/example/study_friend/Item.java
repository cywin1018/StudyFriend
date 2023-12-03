package com.example.study_friend;

import java.util.List;

public class Item {
    String name;
    String title;
    String day;
    String num;

    List<String> recommendedPeople;

    int image;
    String CurTutee;

    public Item(){

    }
    public Item(String name, String title, String day, String num) {
        this.name = name;
        this.title = title;
        this.day = day;
        this.num = num;
    }
    public Item(String name, String title, String day, String num,String CurTutee) {
        this.name = name;
        this.title = title;
        this.day = day;
        this.num = num;
        this.CurTutee = CurTutee;
    }
    public Item(String name, String title, String day, String num, List<String> recommendedPeople,String CurTutee) {
        this.name = name;
        this.title = title;
        this.day = day;
        this.num = num;
        this.recommendedPeople = recommendedPeople;
        this.CurTutee = CurTutee;
    }
//    public Item(String name, String title, String day, String num, int image) {
//        this.name = name;
//        this.title = title;
//        this.day = day;
//        this.num = num;
//        this.image = image;
//    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getNum() {
        return num;
    }

    public void setNum(String num) {
        this.num = num;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }

}
