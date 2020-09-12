package com.example.dietapp.ui.Food;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.dietapp.DataBase.Repository;
import com.example.dietapp.Food;
import com.example.dietapp.MiniFood;
import com.example.dietapp.R;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FavoritFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FavoritFragment extends Fragment implements MyRecyclerViewAdapter.ItemClickListener{

    public FavoritFragment() {
        // Required empty public constructor
    }

    public static FavoritFragment newInstance(String param1, String param2) {
        FavoritFragment fragment = new FavoritFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) { super.onCreate(savedInstanceState); }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_favorit, container, false);
    }

    private RecyclerView recyclerView;
    private MyRecyclerViewAdapter adapter;
    private Repository repository;
    private List<Food> array;
    ArrayList<MiniFood> array2;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        recyclerView = getView().findViewById(R.id.rec_view_fav);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        repository=new Repository(getActivity().getApplicationContext());
        array=repository.fetch();
        array2=new ArrayList<>();
        for (int i=0;i<array.size();i++) {
            array.get(i).favorit=true;
            array2.add(new MiniFood(array.get(i)));
        }
        adapter = new MyRecyclerViewAdapter(getContext(),array2);
        adapter.setClickListener(this);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onResume() {
        super.onResume();
        array=repository.fetch();
        array2=new ArrayList<>();
        for (int i=0;i<array.size();i++) {
            array2.add(new MiniFood(array.get(i)));
        }
        adapter = new MyRecyclerViewAdapter(getContext(),array2);
        adapter.setClickListener(this);
        recyclerView.setAdapter(adapter);
    }


    @Override
    public void onItemClick(int position) {
        Intent intent=new Intent(getActivity(), FoodCard.class);
        Bundle card=new Bundle();
        Food food=array.get(position);
        card.putString("id",food.id);
        card.putString("name",food.name);
        card.putString("instruction",food.instruction);
        card.putString("ready",food.ready);
        card.putString("image",food.image);
        card.putBoolean("vegetarian",food.vegetarian);
        card.putBoolean("vegan",food.vegan);
        card.putBoolean("very_popular",food.very_popular);
        card.putBoolean("very_healthy",food.veryHealthy);
        card.putBoolean("cheap",food.cheap);
        card.putBoolean("dairyfree",food.dairyFree);
        card.putBoolean("glutenfree",food.glutenFree);
        card.putString("link",food.link);
        Log.i("itt",String.valueOf(food.favorit));
        card.putBoolean("favorit",true);
        intent.putExtra("result",card);
        intent.putExtra("from","favorit");
        startActivity(intent);
    }
}
