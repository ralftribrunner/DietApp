package com.example.dietapp;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.dietapp.Diet.DietMenu;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    public static final String CHANNEL_ID = "DIETMENU.NOTI";;
    Intent food_intent;
    Intent diet_intent;

    @Override
    protected void onCreate(Bundle savedInstanceState)  {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button btn_diet=findViewById(R.id.btn_diet);
        btn_diet.setOnClickListener(this);
        Button btn_food=findViewById(R.id.btn_food);
        btn_food.setOnClickListener(this);
        food_intent=new Intent( this, FoodList.class);
        diet_intent=new Intent(this, DietMenu.class);
        createNotificationChannel();



    }

    @Override
    public void onClick(View v)  {
        switch (v.getId()) {
            case R.id.btn_food:
                if (isOnline()) startActivity(food_intent);
                else Toast.makeText(this,"No internet connection!",Toast.LENGTH_SHORT).show();
                break;
            case R.id.btn_diet:
                startActivity(diet_intent);
        }
    }

    public boolean isOnline() {
        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }


    private void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = getString(R.string.channel_name);
            String description = getString(R.string.channel_description);
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

}
