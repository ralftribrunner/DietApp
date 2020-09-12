package com.example.dietapp.DataBase;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.dietapp.Food;

import java.util.List;

@Dao
public interface FavoritDao {
    @Query("SELECT * FROM Food")
    List<Food> getAll();

    @Query("Select id FROM Food WHERE id=:mealid")
    String getElement(String mealid);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertFavorit(Food food);

    @Delete
    void delete(Food food);

    @Query("DELETE FROM Food")
    void deleteAll();
}
