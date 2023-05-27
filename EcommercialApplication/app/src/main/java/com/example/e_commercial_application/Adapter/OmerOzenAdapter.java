package com.example.e_commercial_application.Adapter;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.e_commercial_application.Databases.BasketDB;
import com.example.e_commercial_application.Databases.FavDB;
import com.example.e_commercial_application.Databases.FavDBDiscounted;
import com.example.e_commercial_application.FavFragment;
import com.example.e_commercial_application.HomePage;
import com.example.e_commercial_application.Model.AllProducts;
import com.example.e_commercial_application.Model.DiscountedProducts;
import com.example.e_commercial_application.ProductDetails;
import com.example.e_commercial_application.ProductDetailsDiscounted;
import com.example.e_commercial_application.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;

public class OmerOzenAdapter  extends RecyclerView.Adapter{

    private static List<AllProducts> allProductsList = new ArrayList<>();
    Context context;

    private static FavDB favDB;
    BasketDB basketDB;
    FavDBDiscounted favDBDiscounted;

    public OmerOzenAdapter(List<AllProducts> allProductsList, Context context) {
        this.allProductsList = allProductsList;
        this.context = context;
    }

    @Override
    public int getItemViewType(int position) {
        return (allProductsList.get(position) instanceof DiscountedProducts) ? 1 : 2;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        switch (viewType) {
            case 1:
                View DiscountedLayout = LayoutInflater.from(parent.getContext()).inflate(R.layout.fav_discounted_layour,parent,false);
                return new DiscountedViewHolder(DiscountedLayout);
            case 2:
                View AllProductLayout = LayoutInflater.from(parent.getContext()).inflate(R.layout.my_fav_product_layout,parent,false);
                return new AllProductViewHolder(AllProductLayout);
            default: return null;
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (allProductsList.get(position) instanceof DiscountedProducts) {
            DiscountedProducts favItem = (DiscountedProducts) allProductsList.get(position);
            favDBDiscounted = new FavDBDiscounted(context);

            ((DiscountedViewHolder) holder).favProductName.setText(favItem.getProductName());
            ((DiscountedViewHolder) holder).favProductPrice.setText(favItem.getProductPrice() + " $");
            ((DiscountedViewHolder) holder).favOldProduct.setText(favItem.getOldPrice() + " $");
            ((DiscountedViewHolder) holder).favRate.setText(String.valueOf(favItem.getProductRate()));
            ((DiscountedViewHolder) holder).favRatingBar.setRating(favItem.getProductRate());
            Glide.with(context).load(favItem.getProductImg()).into(((DiscountedViewHolder) holder).favProductImg);

            holder.itemView.setOnClickListener(view -> {

                HomePage homePage = (HomePage) context;

                BottomNavigationView bottomNavigationView = homePage.findViewById(R.id.bottom_nav);
                bottomNavigationView.setVisibility(View.GONE);

                FragmentManager fragmentManager = ((AppCompatActivity)context).getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction().addToBackStack(null);
                Bundle bundle = new Bundle();
                bundle.putSerializable("productDiscounted",allProductsList.get(holder.getAdapterPosition()));
                ProductDetailsDiscounted productDetailsFav = new ProductDetailsDiscounted();
                productDetailsFav.setArguments(bundle);
                fragmentTransaction.replace(R.id.containerFrame,productDetailsFav).commit();




            });

            ((DiscountedViewHolder) holder).deleteFav.setOnClickListener(view -> {

                DiscountedProducts allProducts1 = (DiscountedProducts) allProductsList.get(position);
                allProducts1.setFavStatus("0");
                favDBDiscounted.remove_fav(allProducts1.getId());

                for (int i = 0; i < HomePage.favList.size(); i++) {
                    AllProducts favProduct = HomePage.favList.get(i);
                    if (favProduct.getId().equals(allProducts1.getId())) {
                        HomePage.favList.remove(i);
                        break;
                    }
                }
                FavFragment.ifEmpty();
                notifyDataSetChanged();
            });
        }

        else {
            AllProducts favItem = allProductsList.get(position);
            favDB = new FavDB(context);
            ((AllProductViewHolder) holder).favProductName.setText(favItem.getProductName());
            ((AllProductViewHolder) holder).favProductPrice.setText(favItem.getProductPrice() + " $");
            ((AllProductViewHolder) holder).favRate.setText(String.valueOf(favItem.getProductRate()));
            ((AllProductViewHolder) holder).favRatingBar.setRating(favItem.getProductRate());
            Glide.with(context).load(favItem.getProductImg()).into(((AllProductViewHolder) holder).favProductImg);
            basketDB = new BasketDB(context);

            holder.itemView.setOnClickListener(view -> {

                HomePage homePage = (HomePage) context;

                BottomNavigationView bottomNavigationView = homePage.findViewById(R.id.bottom_nav);
                bottomNavigationView.setVisibility(View.GONE);

                FragmentManager fragmentManager = ((AppCompatActivity)context).getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction().addToBackStack(null);
                Bundle bundle = new Bundle();
                bundle.putSerializable("productName",allProductsList.get(holder.getAdapterPosition()));
                ProductDetails productDetailsFav = new ProductDetails();
                productDetailsFav.setArguments(bundle);
                fragmentTransaction.replace(R.id.containerFrame,productDetailsFav).commit();




            });

            ((AllProductViewHolder) holder).delete.setOnClickListener(view -> {

                AllProducts allProducts1 = allProductsList.get(position);
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
                FavFragment.ifEmpty();


            });
        }
    }

    @Override
    public int getItemCount() {
        return allProductsList.size();
    }

    public static class AllProductViewHolder extends RecyclerView.ViewHolder{
        ImageView favProductImg, delete;
        TextView favProductName, favRate, favProductPrice;
        RatingBar favRatingBar;

        public AllProductViewHolder(@NonNull View itemView) {
            super(itemView);
            favProductImg = itemView.findViewById(R.id.favImg);
            favProductName = itemView.findViewById(R.id.favProductName);
            favProductPrice = itemView.findViewById(R.id.favProductPrice);
            favRate = itemView.findViewById(R.id.faveRating);
            favRatingBar = itemView.findViewById(R.id.favRatingBar);
            delete = itemView.findViewById(R.id.deleteFav);
        }
    }

    public static class DiscountedViewHolder extends RecyclerView.ViewHolder{

        ImageView favProductImg,deleteFav;
        TextView favProductName, favRate, favProductPrice, favOldProduct;
        RatingBar favRatingBar;
        public DiscountedViewHolder(@NonNull View itemView) {
            super(itemView);
            favProductImg = itemView.findViewById(R.id.favImg);
            favProductName = itemView.findViewById(R.id.favProductName);
            favProductPrice = itemView.findViewById(R.id.favProductPrice);
            favRate = itemView.findViewById(R.id.faveRating);
            favRatingBar = itemView.findViewById(R.id.favRatingBar);
            favOldProduct = itemView.findViewById(R.id.OldPrice);
            deleteFav = itemView.findViewById(R.id.deleteFav);
        }
    }
}
