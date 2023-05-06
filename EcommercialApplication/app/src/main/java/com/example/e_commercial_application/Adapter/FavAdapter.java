package com.example.e_commercial_application.Adapter;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.e_commercial_application.BasketDB;
import com.example.e_commercial_application.BasketFragment;
import com.example.e_commercial_application.FavDB;
import com.example.e_commercial_application.HomePage;
import com.example.e_commercial_application.Model.AllProducts;
import com.example.e_commercial_application.ProductDetailsFav;
import com.example.e_commercial_application.R;
import com.example.e_commercial_application.productDetails3;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class FavAdapter extends RecyclerView.Adapter<FavAdapter.ViewHolder> {

    private ArrayList<AllProducts> favArrayList;
    Context context;
    BasketDB basketDB;

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
        basketDB = new BasketDB(context);

        holder.itemView.setOnClickListener(view -> {

            HomePage homePage = (HomePage) context;
            NewSeasonAdapter adapter = new NewSeasonAdapter(favArrayList,homePage);

            BottomNavigationView bottomNavigationView = homePage.findViewById(R.id.bottom_nav);
            bottomNavigationView.setVisibility(View.GONE);

            FragmentManager fragmentManager = ((AppCompatActivity)context).getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction().addToBackStack(null);
            Bundle bundle = new Bundle();
            bundle.putSerializable("productFav",favArrayList.get(holder.getAdapterPosition()));
            ProductDetailsFav productDetailsFav = new ProductDetailsFav();
            productDetailsFav.setArguments(bundle);
            fragmentTransaction.replace(R.id.containerFrame,productDetailsFav).commit();




        });




    }

    @Override
    public int getItemCount() {
        return favArrayList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{

        ImageView favProductImg;
        TextView favProductName, favRate, favProductPrice;
        RatingBar favRatingBar;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            favProductImg = itemView.findViewById(R.id.favImg);
            favProductName = itemView.findViewById(R.id.favProductName);
            favProductPrice = itemView.findViewById(R.id.favProductPrice);
            favRate = itemView.findViewById(R.id.faveRating);
            favRatingBar = itemView.findViewById(R.id.favRatingBar);
        }
    }
}
