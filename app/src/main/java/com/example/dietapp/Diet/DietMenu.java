package com.example.dietapp.Diet;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
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
import android.widget.DatePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dietapp.DataBase.Diet;
import com.example.dietapp.DataBase.Repository;
import com.example.dietapp.DownloadService;
import com.example.dietapp.MainActivity;
import com.example.dietapp.R;
import com.example.dietapp.ui.Food.FoodCard;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Objects;

interface MyDialogFragmentListener {
    void onReturnValue(Boolean delete);
    void onDatePickerResult(Boolean bool,int year, int month, int day);
}

public class DietMenu extends AppCompatActivity implements MyDialogFragmentListener,MyRecyclerViewAdapterforDiets.ItemClickListener,MyRecyclerViewAdapterforDiets.ItemLongClickListener {


    public Repository repository;
    RecyclerView recyclerView;
    List<Diet> array;
    MyRecyclerViewAdapterforDiets adapter;
    int selected_to_delete;
    ServiceReciver reciver;
    int act_running_pos;
    int act_long_click;
    String active_url;
    Boolean selected;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diet_menu);
        selected=false;







        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, MainActivity.CHANNEL_ID)
                .setCategory(NotificationCompat.CATEGORY_REMINDER)
                .setSmallIcon(R.drawable.dietpic4)
                .setContentTitle(getString(R.string.exp_diet_title))
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setStyle(new NotificationCompat.BigTextStyle()
                        .bigText(getString(R.string.exp_diet)));

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);





        repository=new Repository(getApplicationContext());

        registerServiceReceiver();

        recyclerView = findViewById(R.id.recview_diet);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        repository=new Repository(getApplicationContext());
        array=repository.fetchDiets();
        array=deleteExpired(array);

        // notificationId is a unique int for each notification that you must define
        final int notificationId=0;

        if (scanExpiringDates()) {
            notificationManager.notify(notificationId, builder.build());
        }



        final ArrayList<Diet> array2=new ArrayList<Diet>(array);
        if (array2.size()==0) {
            Snackbar snackbar = Snackbar
                    .make(findViewById(R.id.dietmenu_root), R.string.add_diets, Snackbar.LENGTH_INDEFINITE);
            snackbar.show();
        }
        adapter = new MyRecyclerViewAdapterforDiets(this,array2);
        adapter.setClickListener(this);
        adapter.setLongClickListener(this);
        recyclerView.setAdapter(adapter);

        SwipeCallback swipeCallback= new SwipeCallback(this) {
            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) { //del: 4 new:8
                if (direction==4) {

                    selected_to_delete=viewHolder.getAdapterPosition();
                    DialogFragment newFragment = new AlertFragment1();
                    newFragment.show(getSupportFragmentManager(), "alertdelete");
                } else if(direction==8) {

                    act_running_pos=viewHolder.getLayoutPosition();
                    active_url=array2.get(viewHolder.getLayoutPosition()).url;
                    startFoodCard(act_running_pos);
                }
            }
        };
        ItemTouchHelper itemTouchHelper=new ItemTouchHelper(swipeCallback);
        itemTouchHelper.attachToRecyclerView(recyclerView);


        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getApplicationContext(),DietPlan.class);
                startActivity(intent);

            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        selected=false;
        array=repository.fetchDiets();
        ArrayList<Diet> array2=new ArrayList<Diet>(array);
        adapter = new MyRecyclerViewAdapterforDiets(this,array2);
        adapter.setClickListener(this);
        adapter.setLongClickListener(this);
        recyclerView.setAdapter(adapter);
    }

    private boolean scanExpiringDates() {
        Date dt = new Date();
        Calendar c = Calendar.getInstance();
        c.setTime(dt);
        c.add(Calendar.DATE, 3);
        dt = c.getTime();

        for (Diet act:array) {
            if (act.Enddate.before(dt)) return true;
        }
        return false;
    }

    @Override
    public void onReturnValue(Boolean delete) {
        if (delete) {
            repository.delete(adapter.getItem(selected_to_delete));
            array.remove(selected_to_delete);
            ArrayList<Diet> array2=new ArrayList<Diet>(array);
            adapter.updateList(array2);
        }
        else {
            array=repository.fetchDiets();
            ArrayList<Diet> array2=new ArrayList<Diet>(array);
            adapter.updateList(array2);

        }
    }

    public void onDatePickerResult(Boolean bool,int year,int month,int day) {
        if (bool) {
            Diet d=array.get(act_long_click);
            d.Enddate=new Date(year,month,day);
            repository.updateDiet(d);

            array.set(act_long_click,d);
            ArrayList<Diet> a=new ArrayList<>(array);
            adapter.updateList(a);
            Toast.makeText(this, R.string.date_extended, Toast.LENGTH_SHORT).show();
            act_long_click=-1;


        }
        else {
            Toast.makeText(this, R.string.futur_date, Toast.LENGTH_SHORT).show();
            act_long_click=-1;
        }
    }

    private void registerServiceReceiver() {
        reciver = new ServiceReciver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(DownloadService.RESULT_CARD);
        registerReceiver(reciver,intentFilter);

    }

    public void startFoodCard(int position) {
        if (isOnline()) {
            selected=true;
            adapter.getItem(position);
            array.get(position).loading=true;
            act_running_pos=position;
            ArrayList<Diet> array2=new ArrayList<Diet>(array);
            adapter.updateList(array2);
            active_url=adapter.getItem(position).url;
            DownloadService.startActionDiet(this,adapter.getItem(position).url,-1);
        }
        else Toast.makeText(this,"No internet connection!",Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onItemClick(int position) {

        startFoodCard(position);
    }

    @Override
    public void onItemLongClick(int position) {
        showDatePickerDialog(findViewById(R.id.dietmenu_root));
        act_long_click=position;


    }

    //delete kezelő alert
    public static class AlertFragment1 extends DialogFragment {
        @NotNull
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the Builder class for convenient dialog construction
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setMessage(R.string.remove_diets)
                    .setPositiveButton(R.string.okay, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            MyDialogFragmentListener activity = (MyDialogFragmentListener) getActivity();
                            activity.onReturnValue(true);
                        }
                    }).setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    MyDialogFragmentListener activity = (MyDialogFragmentListener) getActivity();
                    activity.onReturnValue(false);
                }
            });
            // Create the AlertDialog object and return it
            return builder.create();
        }
    }

    public  boolean isOnline() {
        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent=new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    private class ServiceReciver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (Objects.equals(intent.getAction(), DownloadService.RESULT_CARD) && getApplicationContext()!=null && array.size()>0 && selected) {
                array.get(act_running_pos).loading=false;
                ArrayList<Diet> array2=new ArrayList<Diet>(array);
                adapter.updateList(array2);
                Bundle result=intent.getBundleExtra("result");
                Intent intent1=new Intent(getApplicationContext(),FoodCard.class);
                intent1.putExtra("result",result);
                intent1.putExtra("from","dietmenu");

                intent1.putExtra("diet_url",active_url);
                startActivity(intent1);
            }
            else if(intent.getAction().equals(DownloadService.RESULT_EMPTYDIET)) {
                Toast.makeText(getApplicationContext(),"No data found!",Toast.LENGTH_SHORT).show();
                active_url="";
                selected=false;
            }

        }
    }

    private List<Diet> deleteExpired(List<Diet> diets) {
        Date current=new Date();

        for (int i=0;i<diets.size();i++) {
            Log.i("lejárat",diets.get(i).Enddate.toString());
            if(diets.get(i).Enddate.before(current)) {
                repository.delete(diets.get(i));
                diets.remove(i);
            }
        }
        return diets;
    }

    public void showDatePickerDialog(View v) {
        DialogFragment newFragment = new DatePickerFragment();
        newFragment.show(getSupportFragmentManager(), "datePicker");
    }

    public static class DatePickerFragment extends DialogFragment
            implements DatePickerDialog.OnDateSetListener {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current date as the default date in the picker
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH)+1;

            // Create a new instance of DatePickerDialog and return it
            return new DatePickerDialog(getActivity(), this, year, month, day);
        }

        public void onDateSet(DatePicker view, int year, int month, int day) {
            // Do something with the date chosen by the user
            Calendar today = Calendar.getInstance();
            int today_month = today.get(Calendar.MONTH) ;
            int today_day = today.get(Calendar.DAY_OF_MONTH);
            int today_year=today.get(Calendar.YEAR);
            MyDialogFragmentListener activity = (MyDialogFragmentListener) getActivity();
            if (year<today_year || month < today_month || (month == today_month && day <= today_day)) {
                activity.onDatePickerResult(false,year,month,day);

            } else {
                activity.onDatePickerResult(true,year,month,day);
            }
        }
    }
}


