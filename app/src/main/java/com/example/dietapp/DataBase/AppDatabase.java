package com.example.dietapp.DataBase;

import androidx.room.Database;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.example.dietapp.Food;

@Database(entities = {Food.class,Diet.class}, version = 6,exportSchema = false)
@TypeConverters({Converters.class})
public abstract class AppDatabase extends RoomDatabase {
    public abstract FavoritDao favoritDao();
    public abstract DietDao dietDao();

}
