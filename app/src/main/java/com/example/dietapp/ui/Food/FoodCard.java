package com.example.dietapp.ui.Food;

import androidx.appcompat.app.AppCompatActivity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dietapp.DataBase.Diet;
import com.example.dietapp.DataBase.Repository;
import com.example.dietapp.Diet.DietMenu;
import com.example.dietapp.DownloadService;
import com.example.dietapp.Food;
import com.example.dietapp.FoodList;
import com.example.dietapp.MainActivity;
import com.example.dietapp.R;
import com.google.android.material.chip.Chip;
import com.google.android.material.snackbar.Snackbar;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Objects;

public class FoodCard extends AppCompatActivity implements View.OnClickListener {

    TextView title;
    TextView ready;
    TextView instr;
    TextView time_tv;
    ImageView pic;
    CheckBox fav;
    Chip vegan;
    Chip vegetarian;
    Chip popular;
    Chip healthy;
    Chip cheap;
    Chip dairy;
    Chip gluten;
    Food food;
    Repository noteRepository;
    Boolean originalfav;
    String from;
    Button backButton;
    Button nextButton;
    ServiceReciver reciver;
    Bundle b;
    String time;
    String dieturl;
    String link;
    Button gotoweb;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_card);
        Intent intent=getIntent();
        dieturl=intent.getStringExtra("diet_url");
        from=intent.getStringExtra("from");
        b=intent.getBundleExtra("result");
        registerServiceReceiver();
        backButton=findViewById(R.id.button_back);
        nextButton=findViewById(R.id.button_next);
        backButton.setOnClickListener(this);
        nextButton.setOnClickListener(this);
        title=findViewById(R.id.card_title);
        ready=findViewById(R.id.card_ready);
        pic=findViewById(R.id.foodcardpic);
        fav=findViewById(R.id.fav_check);
        instr=findViewById(R.id.instruction);
        vegan=findViewById(R.id.vegan);
        vegetarian=findViewById(R.id.veg);
        popular=findViewById(R.id.verypop);
        healthy=findViewById(R.id.veryhealthy);
        cheap=findViewById(R.id.cheap);
        dairy=findViewById(R.id.dairyfree);
        gluten=findViewById(R.id.glutenfree);
        time_tv=findViewById(R.id.foodcard_time);
        gotoweb=findViewById(R.id.gotoweb);
        gotoweb.setOnClickListener(this);

        noteRepository = new Repository(getApplicationContext());

        time=b.getString("time");
        if (time != null) {
            switch (time) {
                case "B":
                    backButton.setVisibility(View.INVISIBLE);
                    nextButton.setVisibility(View.VISIBLE);
                    fav.setVisibility(View.VISIBLE);
                    time_tv.setVisibility(View.VISIBLE);
                    time_tv.setText(R.string.Breakfast);
                    break;
                case "L":
                    backButton.setVisibility(View.VISIBLE);
                    nextButton.setVisibility(View.VISIBLE);
                    fav.setVisibility(View.VISIBLE);
                    time_tv.setVisibility(View.VISIBLE);
                    time_tv.setText(R.string.MainCourse);
                    break;

                case "D":
                    backButton.setVisibility(View.VISIBLE);
                    nextButton.setVisibility(View.INVISIBLE);
                    fav.setVisibility(View.VISIBLE);
                    time_tv.setVisibility(View.VISIBLE);
                    time_tv.setText(R.string.dinner);
                    break;

                default:
                    backButton.setVisibility(View.GONE);
                    nextButton.setVisibility(View.GONE);
                    fav.setVisibility(View.VISIBLE);
                    time_tv.setVisibility(View.GONE);
                    break;
            }
        } else {
            backButton.setVisibility(View.GONE);
            nextButton.setVisibility(View.GONE);
            time_tv.setVisibility(View.GONE);
            fav.setVisibility(View.VISIBLE);
        }


        originalfav=b.getBoolean("favorit");
        if (originalfav==null) originalfav=false;
        if (originalfav) fav.setChecked(true);
        if (!b.getBoolean("vegan")) vegan.setVisibility(View.GONE);
        if (!b.getBoolean("vegetarian")) vegetarian.setVisibility(View.GONE);
        if (!b.getBoolean("very_popular")) popular.setVisibility(View.GONE);
        if (!b.getBoolean("very_healthy")) healthy.setVisibility(View.GONE);
        if (!b.getBoolean("cheap")) cheap.setVisibility(View.GONE);
        if (!b.getBoolean("dairyfree")) dairy.setVisibility(View.GONE);
        if (!b.getBoolean("glutenfree")) gluten.setVisibility(View.GONE);
        instr.setText(b.getString("instruction"));
        title.setText(b.getString("name"));
        ready.setText(b.getString("ready")+" minutes");
        Picasso.get().load(b.getString("image")).into(pic);

        food=new Food(b.getString("id"),b.getString("name"),b.getBoolean("vegetarian"),b.getBoolean("vegan"),b.getBoolean("glutenfree"),
                b.getBoolean("dairyfree"),b.getBoolean("very_healthy"),b.getBoolean("cheap"),b.getBoolean("very_popular"),fixLink(b.getString("link")),
                b.getString("instruction"),b.getString("ready"),b.getString("image"));

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent;
        Log.i("from",from);
        switch (from) {
            case "meals":
                intent=new Intent(this,FoodList.class);
                break;
            case "favorit":
                intent=new Intent(this,FoodList.class);
                break;
            case "dietmenu":
                intent=new Intent(this, DietMenu.class);
                break;
            default: intent=new Intent(this, MainActivity.class);
        }



        saveFavorit();
        startActivity(intent);
    }

    private void saveFavorit() {
        if(!fav.isChecked() && originalfav) {
            noteRepository.delete(food);
        }
        if (fav.isChecked() && !originalfav) {
            noteRepository.insertTask(food);
        }
    }

    private String fixLink(String old) {
        if (old.equals("")) return "https://www.google.com";
        if (!old.startsWith("http://") && !old.startsWith("https://"))
            old = "https://" + old;
        if (!old.contains("https") &&old.contains("http")) {
            old=old.substring(0,4)+"s"+old.substring(4);
        }
        return old;

    }


    @Override
    public void onClick(View v) {
        int override_time_value;

        if (isOnline()) {
            switch (v.getId()) {
                case R.id.button_back:
                    switch (time) {
                        case "L":
                            override_time_value=0; //back to breakfast
                            break;
                        case "D":
                            override_time_value=1; //back to lunch
                            break;
                        default:
                            override_time_value=-1;
                    }

                    DownloadService.startActionDiet(this,dieturl,override_time_value);

                    break;
                case R.id.button_next:
                    switch (time) {
                        case "B":
                            override_time_value=1; //to lunch
                            break;
                        case "L":
                            override_time_value=2; //to dinner
                            break;
                        default:
                            override_time_value=-1;
                    }
                    DownloadService.startActionDiet(this,dieturl,override_time_value);
                    break;
                case R.id.gotoweb:
                    link=b.getString("link");
                    assert link != null;

                    link=fixLink(link);
                    saveFavorit();


                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(link));
                    startActivity(browserIntent);

                    break;

            }
        }
        else {
            Snackbar snackbar = Snackbar
                    .make(findViewById(R.id.Foodcard_root), R.string.nonet, Snackbar.LENGTH_SHORT);
            snackbar.show();
        }

    }

    private void registerServiceReceiver() {
        reciver = new ServiceReciver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(DownloadService.RESULT_CARD);
        registerReceiver(reciver,intentFilter);

    }

    public  boolean isOnline() {
        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }

    private class ServiceReciver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (Objects.equals(intent.getAction(), DownloadService.RESULT_CARD) && getApplicationContext()!=null) {
                Bundle result=intent.getBundleExtra("result");
                Intent intent1=new Intent(getApplicationContext(),FoodCard.class);
                intent1.putExtra("result",result);
                intent1.putExtra("from","dietmenu");
                intent1.putExtra("diet_url",dieturl);
                startActivity(intent1);
            }
            else if(intent.getAction().equals(DownloadService.RESULT_EMPTYDIET)) {
                Toast.makeText(getApplicationContext(),"No data found!",Toast.LENGTH_SHORT).show();
            }

        }
    }
}
