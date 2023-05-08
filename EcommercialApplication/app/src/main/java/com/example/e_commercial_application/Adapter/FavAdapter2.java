package com.example.e_commercial_application.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.e_commercial_application.Model.DiscountedProducts;
import com.example.e_commercial_application.R;

import org.checkerframework.checker.units.qual.C;

import java.util.ArrayList;

public class FavAdapter2 extends RecyclerView.Adapter<FavAdapter2.ViewHolder> {
    private ArrayList<DiscountedProducts> favArrayList;
    Context context;

    public FavAdapter2(ArrayList<DiscountedProducts> favArrayList, Context context){
        this.favArrayList = favArrayList;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fav_discounted_layour,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        DiscountedProducts discountedProducts = new DiscountedProducts();
        DiscountedProducts favItem = favArrayList.get(position);

        holder.favProductName.setText(favItem.getProductName());
        holder.favProductPrice.setText(favItem.getProductPrice() + " $");
        holder.favOldProduct.setText(favItem.getOldPrice() + " $");
        holder.favRate.setText(String.valueOf(favItem.getProductRate()));
        holder.favRatingBar.setRating(favItem.getProductRate());
        Glide.with(context).load(favItem.getProductImg()).into(holder.favProductImg);
    }

    @Override
    public int getItemCount() {
        return favArrayList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{

        ImageView favProductImg;
        TextView favProductName, favRate, favProductPrice, favOldProduct;
        RatingBar favRatingBar;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            favProductImg = itemView.findViewById(R.id.favImg);
            favProductName = itemView.findViewById(R.id.favProductName);
            favProductPrice = itemView.findViewById(R.id.favProductPrice);
            favRate = itemView.findViewById(R.id.faveRating);
            favRatingBar = itemView.findViewById(R.id.favRatingBar);
            favOldProduct = itemView.findViewById(R.id.OldPrice);
        }
    }

}
