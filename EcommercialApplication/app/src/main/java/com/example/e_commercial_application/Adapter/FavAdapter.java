package com.example.e_commercial_application.Adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.e_commercial_application.FavDB;
import com.example.e_commercial_application.Model.AllProducts;
import com.example.e_commercial_application.R;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class FavAdapter extends RecyclerView.Adapter<FavAdapter.ViewHolder> {

    private ArrayList<AllProducts> favArrayList;
    Context context;

    private static final String TAG = "FavAdapter";

    public FavAdapter(ArrayList<AllProducts> favArrayList, Context context) {
        this.favArrayList = favArrayList;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.my_fav_product_layout,parent,false);
        return new ViewHolder(view);
    }



    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        AllProducts allProducts = new AllProducts();
        AllProducts favItem = favArrayList.get(position);

        holder.favProductName.setText(favItem.getProductName());
        holder.favProductPrice.setText(favItem.getProductPrice() + " $");
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
        TextView favProductName, favRate, favProductPrice;
        RatingBar favRatingBar;
        Button favContinue;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            favProductImg = itemView.findViewById(R.id.favImg);
            favProductName = itemView.findViewById(R.id.favProductName);
            favProductPrice = itemView.findViewById(R.id.favProductPrice);
            favRate = itemView.findViewById(R.id.faveRating);
            favRatingBar = itemView.findViewById(R.id.favRatingBar);
            favContinue = itemView.findViewById(R.id.favContinue);
        }
    }
}
