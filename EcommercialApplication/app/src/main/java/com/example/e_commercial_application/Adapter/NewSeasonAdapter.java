package com.example.e_commercial_application.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.e_commercial_application.Model.AllProducts;
import com.example.e_commercial_application.Model.NewSeason;
import com.example.e_commercial_application.R;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

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
            holder.ProductPriceNewSeason.setText(newSeason.getProductPrice() + " $");
            holder.ProductNameNewSeason.setText(newSeason.getProductName());
            Glide.with(context).load(newSeason.getProductImg()).into(holder.ProductImgNewSeason);

    }

    @Override
    public int getItemCount() {
        return newSeasonList.size();
    }

    public class NewSeasonViewHolder extends RecyclerView.ViewHolder{


        ImageView ProductImgNewSeason;
        TextView ProductNameNewSeason, ProductPriceNewSeason;

        ImageView favProductNewSeason;

        boolean isClicked = false;



        public NewSeasonViewHolder(@NonNull View itemView) {
            super(itemView);

            ProductImgNewSeason = itemView.findViewById(R.id.ProductImg);
            ProductNameNewSeason = itemView.findViewById(R.id.txtProductName);
            ProductPriceNewSeason = itemView.findViewById(R.id.ProductPrice);
            favProductNewSeason = itemView.findViewById(R.id.fav_Product);
        }
    }
}
