package com.example.dietapp;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;


@Entity
public class Food {
    @PrimaryKey @NonNull
    public final String id;

    public final String name;
    public final Boolean vegetarian;
    public final Boolean vegan;
    public final Boolean glutenFree;
    public final Boolean dairyFree;
    public final Boolean veryHealthy;
    public final Boolean cheap;
    public final Boolean very_popular;
    public Boolean favorit;
    public final String link;
    public final String instruction;
    public final String ready;
    public final String image;


    public Food(String id,String name, Boolean vegetarian, Boolean vegan, Boolean glutenFree,
                Boolean dairyFree, Boolean veryHealthy, Boolean cheap, Boolean very_popular, String link,String instruction, String ready, String image) {
        this.id=id;
        this.name = name;
        this.vegetarian = vegetarian;
        this.vegan = vegan;
        this.glutenFree = glutenFree;
        this.dairyFree = dairyFree;
        this.veryHealthy = veryHealthy;
        this.cheap = cheap;
        this.very_popular = very_popular;
        this.instruction = instruction;
        this.ready = ready;
        this.image = image;
        this.favorit=false;
        this.link=link;
    }
}
