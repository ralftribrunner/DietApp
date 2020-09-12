package com.example.dietapp.DataBase;

import android.content.Context;
import android.os.AsyncTask;

import androidx.room.Room;

import com.example.dietapp.Food;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class Repository {
    private String DB_NAME = "Appdatabase.db";

    private AppDatabase appDatabase;
    public Repository(Context context) {
        appDatabase = Room.databaseBuilder(context, AppDatabase.class, DB_NAME).fallbackToDestructiveMigration().build();
    }

    public void insertTask(final Food f) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                appDatabase.favoritDao().insertFavorit(f);
            }
        }).start();

    }

    public void insertTask(final Diet d) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                appDatabase.dietDao().insertDiet(d);
            }
        }).start();

    }

    public void updateDiet(final Diet d) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                appDatabase.dietDao().updateDiet(d);
            }
        }).start();

    }

    public List<Food> fetch()  {
        try {
            return new getFavorits().execute().get();
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
        return new ArrayList<Food>();
    }

    public List<Diet> fetchDiets() {
        try {
            return new getDiets().execute().get();
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
        return new ArrayList<Diet>();
    }

    public Boolean exists(String id) {
        String result;
        try {
            result= new GetExists().execute(id).get();
            if (result==null) return false;
            else return true;

        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
        return false;
    }

    public void delete(final Food f) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                appDatabase.favoritDao().delete(f);
            }
        }).start();
    }

    public void delete(final Diet d) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                appDatabase.dietDao().delete(d);
            }
        }).start();
    }

    private class getFavorits extends AsyncTask<Void, Void,List<Food>>
    {
        @Override
        protected List<Food> doInBackground(Void... voids) {
            return appDatabase.favoritDao().getAll();
        }
    }

    private class getDiets extends AsyncTask<Void, Void,List<Diet>>
    {
        @Override
        protected List<Diet> doInBackground(Void... voids) {
            return appDatabase.dietDao().getAll();
        }
    }


    private class GetExists extends AsyncTask<String, Void,String>
    {
        @Override
        protected String doInBackground(String... strings) {
            return appDatabase.favoritDao().getElement(strings[0]);
        }
    }

    public void deleteAllFoods() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                appDatabase.favoritDao().deleteAll();
            }
        }).start();
    }

    public void deleteAllDiets() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                appDatabase.dietDao().deleteAll();
            }
        }).start();
    }

}
