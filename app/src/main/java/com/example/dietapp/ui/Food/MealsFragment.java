package com.example.dietapp.ui.Food;

import androidx.lifecycle.ViewModelProviders;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.dietapp.DownloadService;
import com.example.dietapp.MiniFood;
import com.example.dietapp.R;

import java.util.ArrayList;

public class MealsFragment extends Fragment implements MyRecyclerViewAdapter.ItemClickListener {

    private MealsViewModel mViewModel;
    RecyclerView recyclerView;
    MyRecyclerViewAdapter adapter;
    ServiceReciver reciver;
    int act_running_pos;
    ArrayList<MiniFood> list;

    Context context;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }




    public static MealsFragment newInstance() {
        return new MealsFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        registerServiceReceiver();
        return inflater.inflate(R.layout.meals_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        recyclerView = getView().findViewById(R.id.rec_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new MyRecyclerViewAdapter(getContext(),new ArrayList<MiniFood>());
        adapter.setClickListener(this);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(MealsViewModel.class);
        // TODO: Use the ViewModel
    }

    private void registerServiceReceiver(){
        reciver = new ServiceReciver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(DownloadService.RESULT_INFO);
        intentFilter.addAction(DownloadService.RESULT_CARD);
        getActivity().registerReceiver(reciver,intentFilter);

    }

    @Override
    public void onItemClick(int position) {
        if (isOnline()) {
            act_running_pos=position;
            list.get(position).loading=true;
            adapter.updateList(list);
            DownloadService.startActionFoodCard(getActivity(),adapter.getItem(position).id,"null");
        }
        else Toast.makeText(getActivity(),"No internet connection!",Toast.LENGTH_SHORT).show();
    }

    private boolean isOnline() {
        ConnectivityManager cm =
                (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }

    private class ServiceReciver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(DownloadService.RESULT_INFO)) {
                Bundle all=intent.getBundleExtra("result");
                int size=all.getInt("size");
                list=new ArrayList<>();
                Bundle part;
                for (int i=0;i<size;i++) {
                    part=all.getBundle(String.valueOf(i));
                    String id,title,ready,image;
                    id=part.getString("id");
                    title=part.getString("title");
                    image=part.getString("image");
                    list.add(new MiniFood(id,title,image));

                }
                adapter.updateList(list);
            }
            if (intent.getAction().equals(DownloadService.RESULT_CARD)) {
                if (getContext()!=null) {
                    list.get(act_running_pos).loading=false;
                    adapter.updateList(list);
                    Bundle result=intent.getBundleExtra("result");
                    Intent intent1=new Intent(getContext(),FoodCard.class);
                    intent1.putExtra("result",result);
                    intent1.putExtra("from","meals");
                    startActivity(intent1);
                }

            }

        }
    }

}
