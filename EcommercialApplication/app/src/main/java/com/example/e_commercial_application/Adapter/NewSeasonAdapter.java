package com.example.e_commercial_application.Adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.e_commercial_application.HomePage;
import com.example.e_commercial_application.Model.AllProducts;
//import com.example.e_commercial_application.Model.NewSeason;
import com.example.e_commercial_application.ProductDetails;
import com.example.e_commercial_application.ProductDetails2;
import com.example.e_commercial_application.R;
//import com.example.e_commercial_application.SQL.MyDatabaseHelper;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

public class NewSeasonAdapter extends RecyclerView.Adapter<NewSeasonAdapter.NewSeasonViewHolder> {
    private List<AllProducts> allProductsList;
    private static final String TAG = "NewSeasonAdapter";
    Context context;
public NewSeasonAdapter(List<AllProducts> newSeasonList, Context context) {
    this.allProductsList = newSeasonList;
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

        SharedPreferences prefs = context.getSharedPreferences("FavProducts", Context.MODE_PRIVATE);
        boolean isFav = prefs.getBoolean(String.valueOf(position), false);
        AllProducts allProducts = allProductsList.get(position);
        holder.ProductPriceNewSeason.setText(allProducts.getProductPrice() + " $");
        holder.ProductNameNewSeason.setText(allProducts.getProductName());
        Glide.with(context).load(allProducts.getProductImg()).into(holder.ProductImgNewSeason);

        holder.favProductNewSeason.setBackgroundResource(allProducts.isLiked() ? R.drawable.ic_fav_red : R.drawable.baseline_fav);

        holder.favProductNewSeason.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                allProducts.setLiked(!allProducts.isLiked());
                holder.favProductNewSeason.setBackgroundResource(allProducts.isLiked() ? R.drawable.ic_fav_red : R.drawable.baseline_fav);

                int adapterPosition = holder.getAdapterPosition();
                SharedPreferences.Editor editor = context.getSharedPreferences("FavProducts", Context.MODE_PRIVATE).edit();
                editor.putBoolean(String.valueOf(adapterPosition), allProducts.isLiked());
                editor.apply();

                HomePage.favList.add(allProducts);
                Log.d(TAG, "onClick: favList " + HomePage.favList.size());
            }
        });






        holder.itemView.setOnClickListener(view -> {
            HomePage homePage = (HomePage) context;
            NewSeasonAdapter adapter = new NewSeasonAdapter(allProductsList, homePage);
            BottomNavigationView bottomNavigationView = homePage.findViewById(R.id.bottom_nav);
            bottomNavigationView.setVisibility(View.GONE);
            FragmentManager fragmentManager = ((AppCompatActivity)context).getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            Bundle bundle = new Bundle();
            bundle.putSerializable("productName2",allProductsList.get(holder.getAdapterPosition()));
            ProductDetails2 productDetails2 = new ProductDetails2();
            productDetails2.setArguments(bundle);
            fragmentTransaction.replace(R.id.containerFrame, productDetails2).commit();
        });

    }

    @Override
    public int getItemCount() {
        return allProductsList.size();
    }

    public class NewSeasonViewHolder extends RecyclerView.ViewHolder{


        ImageView ProductImgNewSeason;
        TextView ProductNameNewSeason, ProductPriceNewSeason;

        ImageView favProductNewSeason;





        public NewSeasonViewHolder(@NonNull View itemView) {
            super(itemView);

            ProductImgNewSeason = itemView.findViewById(R.id.ProductImg);
            ProductNameNewSeason = itemView.findViewById(R.id.txtProductName);
            ProductPriceNewSeason = itemView.findViewById(R.id.ProductPrice);
            favProductNewSeason = itemView.findViewById(R.id.fav_Product);
        }
    }
}
