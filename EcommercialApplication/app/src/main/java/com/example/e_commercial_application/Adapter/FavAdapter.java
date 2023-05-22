package com.example.e_commercial_application.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.e_commercial_application.Databases.BasketDB;
import com.example.e_commercial_application.Databases.FavDB;
import com.example.e_commercial_application.HomePage;
import com.example.e_commercial_application.Model.AllProducts;
import com.example.e_commercial_application.Model.DiscountedProducts;
import com.example.e_commercial_application.ProductDetails;
import com.example.e_commercial_application.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;

public class FavAdapter extends RecyclerView.Adapter<FavAdapter.ViewHolder> {

    private static ArrayList<AllProducts> favArrayList;
    Context context;
    BasketDB basketDB;
    private static FavDB favDB;

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
        favDB = new FavDB(context);
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
            bundle.putSerializable("productName",favArrayList.get(holder.getAdapterPosition()));
            ProductDetails productDetailsFav = new ProductDetails();
            productDetailsFav.setArguments(bundle);
            fragmentTransaction.replace(R.id.containerFrame,productDetailsFav).commit();




        });

        holder.delete.setOnClickListener(view -> {

            int position1 = holder.getAdapterPosition();
            AllProducts allProducts1 = favArrayList.get(position1);
                allProducts1.setFavStatus("0");
                favDB.remove_fav(allProducts1.getId());

                for (int i = 0; i < HomePage.favList.size(); i++) {
                    AllProducts favProduct = HomePage.favList.get(i);
                    if (favProduct.getId().equals(allProducts1.getId())) {
                        HomePage.favList.remove(i);
                        break;
                    }
                }
                notifyDataSetChanged();


        });






    }

    @Override
    public int getItemCount() {
        return favArrayList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{

        ImageView favProductImg,delete;
        TextView favProductName, favRate, favProductPrice;
        RatingBar favRatingBar;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            favProductImg = itemView.findViewById(R.id.favImg);
            favProductName = itemView.findViewById(R.id.favProductName);
            favProductPrice = itemView.findViewById(R.id.favProductPrice);
            favRate = itemView.findViewById(R.id.faveRating);
            favRatingBar = itemView.findViewById(R.id.favRatingBar);
            delete = itemView.findViewById(R.id.deleteFav);
        }
    }
}
