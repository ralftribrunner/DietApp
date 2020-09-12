package com.example.dietapp.DataBase;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;
@Dao
public interface DietDao {
    @Query("SELECT * FROM Diet")
    List<Diet> getAll();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertDiet(Diet diet);

    @Update
    int updateDiet(Diet d);


    @Delete
    void delete(Diet diet);

    @Query("DELETE FROM Diet")
    void deleteAll();

}
