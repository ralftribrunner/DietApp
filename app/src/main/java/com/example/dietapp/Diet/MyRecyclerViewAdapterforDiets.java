package com.example.dietapp.Diet;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.dietapp.DataBase.Diet;
import com.example.dietapp.R;

import java.text.DateFormatSymbols;
import java.util.ArrayList;

public class MyRecyclerViewAdapterforDiets extends RecyclerView.Adapter<MyRecyclerViewAdapterforDiets.ViewHolder>  {
    private ArrayList<Diet> mData;

    private LayoutInflater mInflater;
    private ItemClickListener mClickListener;
    private ItemLongClickListener mLongListener;


    // data is passed into the constructor
    public MyRecyclerViewAdapterforDiets(Context context, ArrayList<Diet> data) {
        this.mInflater = LayoutInflater.from(context);
        this.mData = data;

    }

    // inflates the row layout from xml when needed
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.recyclerview_row_diets, parent, false);
        return new ViewHolder(view);
    }

    // binds the data to the TextView in each row
    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        Diet diet = mData.get(position);



        String month=new DateFormatSymbols().getMonths()[diet.Enddate.getMonth()];

        String tmp= month+". "+diet.Enddate.getDate()+".";
        holder.tv_date.setText(tmp);
        holder.tv_foodname.setText(diet.name);

        holder.tv_foodname.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mClickListener.onItemClick(position);
            }
        });
        holder.tv_foodname.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                mLongListener.onItemLongClick(position);
                return true;
            }
        });


        ;
        if (diet.loading) holder.progressBar.setVisibility(View.VISIBLE);
        else holder.progressBar.setVisibility(View.GONE);

    }

    // total number of rows
    @Override
    public int getItemCount() {
        return mData.size();
    }


    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener,View.OnLongClickListener {
        TextView tv_foodname;
        ImageView image;
        ProgressBar progressBar;
        TextView tv_date;

        ViewHolder(View itemView) {
            super(itemView);
            tv_foodname = itemView.findViewById(R.id.food_name);
            progressBar=itemView.findViewById(R.id.meals_progressBar);
            image=itemView.findViewById(R.id.foodpic);
            tv_date=itemView.findViewById(R.id.date);
            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (mClickListener != null) mClickListener.onItemClick(getAdapterPosition());
        }

        @Override
        public boolean onLongClick(View view) {

            if (mLongListener != null) mLongListener.onItemLongClick(getAdapterPosition());
            return true;
        }
    }

    // convenience method for getting data at click position
    Diet getItem(int id) {
        return mData.get(id);
    }


    // allows clicks events to be caught
    void setClickListener(ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    void setLongClickListener(ItemLongClickListener itemClickListener) {
        this.mLongListener = itemClickListener;
    }

    // parent activity will implement this method to respond to click events
    public interface ItemClickListener {
        void onItemClick(int position);

    }

    public interface ItemLongClickListener {
        void onItemLongClick(int position);
    }

    void updateList(ArrayList<Diet> list){
        mData = list;
        notifyDataSetChanged();
    }


}



