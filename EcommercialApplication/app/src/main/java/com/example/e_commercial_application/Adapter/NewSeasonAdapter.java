package com.example.e_commercial_application.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.e_commercial_application.Model.NewSeason;
import com.example.e_commercial_application.R;

import java.util.List;

public class NewSeasonAdapter extends RecyclerView.Adapter<NewSeasonAdapter.NewSeasonViewHolder> {

    private List<NewSeason> newSeasonList;
    Context context;


    public NewSeasonAdapter(List<NewSeason> newSeasonList, Context context) {
        this.newSeasonList = newSeasonList;
        this.context = context;
    }
    @NonNull
    @Override
    public NewSeasonViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view1 = LayoutInflater.from(parent.getContext()).inflate(R.layout.new_season_layout,parent,false);
        return new NewSeasonViewHolder(view1);
    }

    @Override
    public void onBindViewHolder(@NonNull NewSeasonViewHolder holder, int position) {


        NewSeason newSeason = newSeasonList.get(position);
        holder.ProductPrice.setText(newSeason.getProductPrice() + " $");
        holder.ProductName.setText(newSeason.getProductName());
        Glide.with(context).load(newSeason.getProductImg()).into(holder.ProductImg);

        if (holder.isFavClicked){
            holder.favProduct.setBackgroundResource(R.drawable.ic_fav_red);
        }else {
            holder.favProduct.setBackgroundResource(R.drawable.baseline_fav);
        }
        holder.favProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                holder.isFavClicked = !holder.isFavClicked;
                notifyDataSetChanged();
            }
        });

    }

    @Override
    public int getItemCount() {
        return newSeasonList.size();
    }

    public class NewSeasonViewHolder extends RecyclerView.ViewHolder{


        ImageView ProductImg;
        TextView ProductName, ProductPrice;

        ImageButton favProduct;
        boolean isFavClicked;



        public NewSeasonViewHolder(@NonNull View itemView) {
            super(itemView);

            ProductImg = itemView.findViewById(R.id.ProductImg);
            ProductName = itemView.findViewById(R.id.txtProductName);
            ProductPrice = itemView.findViewById(R.id.ProductPrice);
            favProduct = itemView.findViewById(R.id.fav_Product);
            isFavClicked = false;


        }
    }
}
