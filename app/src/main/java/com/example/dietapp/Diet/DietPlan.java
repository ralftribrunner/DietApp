package com.example.dietapp.Diet;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import com.example.dietapp.DataBase.Diet;
import com.example.dietapp.DataBase.Repository;
import com.example.dietapp.DownloadService;
import com.example.dietapp.R;
import com.google.android.material.chip.Chip;

import org.jetbrains.annotations.NotNull;

import java.net.MalformedURLException;
import java.util.Calendar;
import java.util.Date;


public class DietPlan extends AppCompatActivity implements View.OnClickListener,CalendarView.OnDateChangeListener {

    Boolean active;
    EditText name;
    Chip dairy,egg,gluten,grain,pea,sea,shell,soy,tree,wheat;
    CheckBox vegetarian,vegan,glutenfree,pesce,keto,lactov,ovov,paleo,primal,whole;
    CalendarView calendar;
    ProgressBar loading;
    TextView loading_tv;
    Date finish_date;
    ServiceReciver reciver;
    Boolean working_service;
    Button btn_plan;
    private Repository repository;
    Diet diet;

    @Override
    protected void onCreate(Bundle savedInstanceState)  {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diet_plan);
        active=true;

        repository=new Repository(getApplicationContext());

        btn_plan=findViewById(R.id.dietplan);
        btn_plan.setOnClickListener(this);

        registerServiceReceiver();
        working_service=false;
        loading=findViewById(R.id.spinner);
        loading_tv=findViewById(R.id.setupplan);
        calendar=findViewById(R.id.calendar2);
        name=findViewById(R.id.dietname);
        vegetarian=findViewById(R.id.search_veget);
        vegan=findViewById(R.id.search_vegan);
        glutenfree=findViewById(R.id.search_gluten);
        keto=findViewById(R.id.search_keto);
        lactov=  findViewById(R.id.search_lactovega);
        ovov=  findViewById(R.id.search_ovovega);
        pesce=  findViewById(R.id.search_pescetarian);
        paleo=  findViewById(R.id.search_paleo);
        primal=  findViewById(R.id.search_primal);
        whole=  findViewById(R.id.search_whole);
        dairy=  findViewById(R.id.chip_dairy);
        egg=  findViewById(R.id.chip_egg);
        gluten=  findViewById(R.id.chip_gluten);
        grain=  findViewById(R.id.chip_grain);
        pea=  findViewById(R.id.chip_peanut);
        sea=  findViewById(R.id.chip_sea);

        shell=  findViewById(R.id.chip_shell);
        soy=  findViewById(R.id.chip_soy);

        tree=  findViewById(R.id.chip_tree);
        wheat=  findViewById(R.id.chip_wheat);

        Calendar enddate=Calendar.getInstance();
        enddate.add(Calendar.MONTH,1);
        Calendar endselecteddate=Calendar.getInstance();
        endselecteddate.add(Calendar.DAY_OF_MONTH,50);
        calendar.setMinDate(System.currentTimeMillis()+ 24 * 60 * 60 * 1000); //plus one day
        finish_date=new Date(System.currentTimeMillis()+ 24 * 60 * 60 * 1000);

        calendar.setOnDateChangeListener(this);

    }

    @Override
    public void onClick(View v) {
            if(v.getId()==R.id.dietplan) {
                try {
                    check_form_send_intent();
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }
            }
    }

    @Override
    protected void onResume() {
        super.onResume();
        active=true;
    }

    @Override
    protected void onStart() {
        super.onStart();
        active=true;
    }

    @Override
    protected void onPause() {
        super.onPause();
        active=false;
    }

    @Override
    protected void onStop() {
        super.onStop();
        active=false;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        active=false;
    }

    public  boolean isOnline() {
        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }

    private void check_form_send_intent() throws MalformedURLException {
        if (name.getText().length()==0 ) {
            DialogFragment newFragment = new AlertFragment1();
            newFragment.show(getSupportFragmentManager(), "alert1");
        }
        else if(!isOnline()) {
            Toast.makeText(getApplicationContext(),"No internet connection!",Toast.LENGTH_SHORT).show();
        }
        else {
            setWorking_service(true);
            String url_string="https://api.spoonacular.com/mealplanner/generate?timeFrame=day&apiKey=c4c180798c104131b0a7cd89ea97123b";

            String url_diet="&diet=";
            int size_url_diet=url_diet.length();
            if (vegetarian.isChecked()) url_diet+="vegetarian,";
            if (vegan.isChecked()) url_diet+="vegan,";
            if (glutenfree.isChecked()) url_diet+="glutenfree,";
            if (keto.isChecked()) url_diet+="ketogenic,";
            if (lactov.isChecked()) url_diet+="lacto-vegetarian,";
            if (ovov.isChecked()) url_diet+="ovo-vegetarian,";
            if (pesce.isChecked()) url_diet+="pescetarian,";
            if (paleo.isChecked()) url_diet+="paleo,";
            if (primal.isChecked()) url_diet+="primal,";
            if (whole.isChecked()) url_diet+="whole30,";
            if (url_diet.length()>size_url_diet) {
                url_string+=url_diet.substring(0,url_diet.length()-1);
            }
            String url_intolerance="&exclude=";
            int size_url_intol=url_intolerance.length();
            if (dairy.isChecked()) url_intolerance+="dairy,";
            if (egg.isChecked()) url_intolerance+="egg,";
            if (wheat.isChecked()) url_intolerance+="gluten,";
            if (gluten.isChecked()) url_intolerance+="grain,";
            if (grain.isChecked()) url_intolerance+="peanut,";
            if (pea.isChecked()) url_intolerance+="seafood,";
            if (sea.isChecked()) url_intolerance+="sesame,";

            if (shell.isChecked()) url_intolerance+="soy,";
            if (soy.isChecked()) url_intolerance+="sulfite,";

            if (tree.isChecked()) url_intolerance+="wheat,";
            if (url_intolerance.length()>size_url_intol) {
                url_string+=url_intolerance.substring(0,url_intolerance.length()-1);
            }


            diet=new Diet(url_string,finish_date,name.getText().toString());
            DownloadService.startActionDiet(this,url_string,-1);


        }


    }

    @Override
    public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {

        finish_date=new Date(year,month,dayOfMonth);
        Date thisyear=new Date();
        finish_date.setYear(thisyear.getYear());

    }

    //név kezelő alert
    public static class AlertFragment1 extends DialogFragment {
        @NotNull
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the Builder class for convenient dialog construction
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setMessage(R.string.plan_alert)
                    .setPositiveButton(R.string.okay, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {

                        }
                    });
            // Create the AlertDialog object and return it
            return builder.create();
        }
    }


    //empty diet kezelő alert
    public static class AlertFragment3 extends DialogFragment {
        @NotNull
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the Builder class for convenient dialog construction
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setMessage(R.string.plan_alert3)
                    .setPositiveButton(R.string.okay, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {

                        }
                    });
            // Create the AlertDialog object and return it
            return builder.create();
        }
    }



    private void registerServiceReceiver(){
        reciver = new ServiceReciver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(DownloadService.RESULT_EMPTYDIET);
        intentFilter.addAction(DownloadService.RESULT_SUCCESSDIET);
        getApplicationContext().registerReceiver(reciver,intentFilter);

    }

    private class ServiceReciver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(DownloadService.RESULT_EMPTYDIET) && context!=null && active) {
                setWorking_service(false);
                DialogFragment newFragment = new AlertFragment3();
                newFragment.show(getSupportFragmentManager(), "alert3");


            }
            if (intent.getAction().equals(DownloadService.RESULT_SUCCESSDIET) && context!=null && active) {
                setWorking_service(false);

                if (diet!=null)repository.insertTask(diet);
                else Log.e("dietobject","Diet object is null!!");

                Intent intent1=new Intent(getBaseContext(),DietMenu.class);
                startActivity(intent1);
            }

        }
    }

    private void setWorking_service(boolean bool) {
        if (bool) {
            working_service=bool;
            loading_tv.setVisibility(View.VISIBLE);
            loading.setVisibility(View.VISIBLE);
            btn_plan.setVisibility(View.INVISIBLE);
        } else {
            loading_tv.setVisibility(View.INVISIBLE);
            loading.setVisibility(View.INVISIBLE);
            btn_plan.setVisibility(View.VISIBLE);
        }

    }
}
