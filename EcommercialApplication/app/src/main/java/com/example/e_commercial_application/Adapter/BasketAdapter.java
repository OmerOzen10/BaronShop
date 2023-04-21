package com.example.e_commercial_application.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.e_commercial_application.Model.AllProducts;
import com.example.e_commercial_application.R;

import java.util.ArrayList;

public class BasketAdapter extends RecyclerView.Adapter<BasketAdapter.ViewHolder> {

    private ArrayList<AllProducts>basketArrayList;
    Context context;
    private static final String TAG = "BasketAdapter";

    public BasketAdapter(ArrayList<AllProducts> basketArrayList, Context context) {
        this.basketArrayList = basketArrayList;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.my_basket_layout, parent, false);
        return new ViewHolder(view);
    }
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        AllProducts basketItem = basketArrayList.get(position);

        holder.basketProductName.setText(basketItem.getProductName());
        holder.basketProductPrice.setText(basketItem.getProductPrice() + "$");
        holder.itemPiece.setText(basketItem.getNumber() + "");

        Glide.with(context).load(basketItem.getProductImg()).into(holder.basketImg);
    }


    @Override
    public int getItemCount() {
        return basketArrayList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        ImageView basketImg;
        TextView basketProductName, basketProductPrice;

        public TextView itemPiece;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            basketImg = itemView.findViewById(R.id.basketImg);
            basketProductName = itemView.findViewById(R.id.basketProductName);
            basketProductPrice = itemView.findViewById(R.id.priceBasket);
            itemPiece = itemView.findViewById(R.id.itemPiece);


        }
    }
}