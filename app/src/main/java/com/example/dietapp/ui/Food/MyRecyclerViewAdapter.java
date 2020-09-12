package com.example.dietapp.ui.Food;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.dietapp.MiniFood;
import com.example.dietapp.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class MyRecyclerViewAdapter extends RecyclerView.Adapter<MyRecyclerViewAdapter.ViewHolder>  {
    private ArrayList<MiniFood> mData;

    private LayoutInflater mInflater;
    private ItemClickListener mClickListener;

    // data is passed into the constructor
    public MyRecyclerViewAdapter(Context context, ArrayList<MiniFood> data) {
        this.mInflater = LayoutInflater.from(context);
        this.mData = data;

    }

    // inflates the row layout from xml when needed
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.recyclerview_row, parent, false);
        return new ViewHolder(view);
    }

    // binds the data to the TextView in each row
    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        MiniFood food = mData.get(position);

        holder.tv_foodname.setText(food.title);
        Picasso.get().load(food.image).into(holder.image);
        holder.tv_foodname.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mClickListener.onItemClick(position);
            }
        });
        if (food.loading) holder.progressBar.setVisibility(View.VISIBLE);
        else holder.progressBar.setVisibility(View.GONE);

    }

    // total number of rows
    @Override
    public int getItemCount() {
        return mData.size();
    }


    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView tv_foodname;
        ImageView image;
        ProgressBar progressBar;

        ViewHolder(View itemView) {
            super(itemView);
            tv_foodname = itemView.findViewById(R.id.food_name);
            progressBar=itemView.findViewById(R.id.meals_progressBar);
            image=itemView.findViewById(R.id.foodpic);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (mClickListener != null) mClickListener.onItemClick(getAdapterPosition());
        }
    }

    // convenience method for getting data at click position
    MiniFood getItem(int id) {
        return mData.get(id);
    }


    // allows clicks events to be caught
    void setClickListener(ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;

    }

    // parent activity will implement this method to respond to click events
    public interface ItemClickListener {
        void onItemClick( int position);
    }

    public  void updateList(ArrayList<MiniFood> list){
        mData = list;
        notifyDataSetChanged();
    }


}



