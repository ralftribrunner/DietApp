package com.example.dietapp;

public class MiniFood {
    final public String id;
    final public String title;
    final public String image;
    public Boolean loading;
    public Boolean favorit;


    public MiniFood(String id, String title, String image) {
        this.id = id;
        this.title = title;
        this.image = image;
        loading=false;
        favorit=false;
    }

    public MiniFood(Food food) {
        this.id=food.id;
        this.title=food.name;
        this.image=food.image;
        loading=false;
        favorit=food.favorit;
    }



}
