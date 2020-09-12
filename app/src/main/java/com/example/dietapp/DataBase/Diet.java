package com.example.dietapp.DataBase;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.Date;


@Entity
public class Diet {

    @PrimaryKey (autoGenerate = true)
    @NonNull
    public Integer id;
    public  String url;


    public  Date Enddate;
    public  String name;
    public Boolean loading;

    public Diet(){}

    public Diet( String url, Date enddate, String name) {

        this.url = url;

        this.Enddate = enddate;
        this.name = name;
        this.loading=false;
    }



}
