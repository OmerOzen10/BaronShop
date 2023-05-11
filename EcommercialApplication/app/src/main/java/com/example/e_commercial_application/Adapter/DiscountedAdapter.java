package com.example.e_commercial_application.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.e_commercial_application.Databases.FavDB;
import com.example.e_commercial_application.Databases.FavDBDiscounted;
import com.example.e_commercial_application.HomePage;
import com.example.e_commercial_application.Model.DiscountedProducts;
import com.example.e_commercial_application.ProductDetailsDiscounted;
import com.example.e_commercial_application.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.List;

public class DiscountedAdapter extends RecyclerView.Adapter<DiscountedAdapter.ViewHolder> {

    private static List<DiscountedProducts> discountedProductsList;
    private static final String TAG = "DiscountedAdapter";
    Context context;
    private static FavDBDiscounted favDBDiscounted;

    public DiscountedAdapter(List<DiscountedProducts> discountedProductsList, Context context){
        this.discountedProductsList = discountedProductsList;
        this.context = context;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        favDBDiscounted = new FavDBDiscounted(context);
        SharedPreferences prefs = context.getSharedPreferences("prefs",Context.MODE_PRIVATE);
        boolean firstStart = prefs.getBoolean("firstStart",true);
        if (firstStart){
            createTableOnFirstStart();
        }
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.discounted_layout,parent,false);
        return new ViewHolder(view);
    }
    private void createTableOnFirstStart() {
        favDBDiscounted.insertEmpty();
        SharedPreferences prefs = context.getSharedPreferences("prefs",Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean("firstStart", false);
        editor.apply();
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        final DiscountedProducts discountedProducts = discountedProductsList.get(position);
        readCursorData(discountedProducts,holder);
        holder.ProductNameDiscounted.setText(discountedProducts.getProductName());
        holder.ProductPriceDiscounted.setText(discountedProducts.getProductPrice() + " $");
        holder.OldPrice.setText(discountedProducts.getOldPrice() + " $");
        Glide.with(context).load(discountedProducts.getProductImg()).into(holder.ProductImgDiscounted);


        holder.itemView.setOnClickListener(view -> {

            FragmentManager fragmentManager = ((AppCompatActivity)context).getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction().addToBackStack(null);
            Bundle bundle = new Bundle();
            bundle.putSerializable("productDiscounted",discountedProductsList.get(holder.getAdapterPosition()));
            ProductDetailsDiscounted productDetails = new ProductDetailsDiscounted();
            productDetails.setArguments(bundle);
            fragmentTransaction.replace(R.id.containerFrame, productDetails).addToBackStack(null).commit();
            HomePage homePage = (HomePage) context;
            DiscountedAdapter adapter = new DiscountedAdapter(discountedProductsList, homePage);
            BottomNavigationView bottomNavigationView = homePage.findViewById(R.id.bottom_nav);
            bottomNavigationView.setVisibility(View.GONE);

        });

    }

    private void readCursorData(DiscountedProducts discountedProducts, ViewHolder viewHolder) {

        Cursor cursor = favDBDiscounted.read_all_data(discountedProducts.getId());
        SQLiteDatabase db = favDBDiscounted.getReadableDatabase();
        try {
            while (cursor.moveToNext()){
                @SuppressLint("Range") String item_fav_status = cursor.getString(cursor.getColumnIndex(FavDB.FAVORITE_STATUS));
                discountedProducts.setFavStatus(item_fav_status);

                if (item_fav_status != null && item_fav_status.equals("1")){
                    viewHolder.favProductDiscounted.setBackgroundResource(R.drawable.ic_fav_red);
                } else if (item_fav_status !=null && item_fav_status.equals("0")) {
                    viewHolder.favProductDiscounted.setBackgroundResource(R.drawable.baseline_fav);
                }
            }
        }finally {
            if (cursor!=null && cursor.isClosed())
                cursor.close();
            db.close();
        }

    }

    @Override
    public int getItemCount() {
        return discountedProductsList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{

        ImageView ProductImgDiscounted;
        TextView ProductNameDiscounted, ProductPriceDiscounted,OldPrice;

        ImageView favProductDiscounted;
        CardView favProductContainer;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            ProductImgDiscounted = itemView.findViewById(R.id.ProductImg);
            ProductNameDiscounted = itemView.findViewById(R.id.txtProductName);
            ProductPriceDiscounted = itemView.findViewById(R.id.ProductPrice);
            OldPrice = itemView.findViewById(R.id.OldPrice);
            favProductDiscounted = itemView.findViewById(R.id.fav_Product);
            favProductContainer = itemView.findViewById(R.id.favProductContainer);

            favProductContainer.setOnClickListener(view -> {
                int position = getAdapterPosition();
                DiscountedProducts discountedProducts = discountedProductsList.get(position);

                if (discountedProducts.getFavStatus() != null && discountedProducts.getFavStatus().equals("0")){
                    discountedProducts.setFavStatus("1");
                    favDBDiscounted.insertIntoTheDatabase(discountedProducts.getProductName(),discountedProducts.getProductImg(),discountedProducts.getId(), discountedProducts.getFavStatus(),String.valueOf(discountedProducts.getProductRate()),String.valueOf(discountedProducts.getProductPrice()),String.valueOf(discountedProducts.getOldPrice()));
                    favProductDiscounted.setBackgroundResource(R.drawable.ic_fav_red);

                    HomePage.favList2.add(discountedProducts);
                    Log.d(TAG, "onClick: favList" + HomePage.favList.size());

                } else {
                    discountedProducts.setFavStatus("0");
                    favDBDiscounted.remove_fav(discountedProducts.getId());
                    favProductDiscounted.setBackgroundResource(R.drawable.baseline_fav);
                    for (int i = 0; i < HomePage.favList2.size(); i++) {
                        DiscountedProducts favProduct = HomePage.favList2.get(i);
                        if (favProduct.getId().equals(discountedProducts.getId())) {
                            HomePage.favList2.remove(i);
                            break;
                        }
                    }
                    Log.d(TAG, "onClick: favList" + HomePage.favList2.size());

                }

            });
        }

        }
}
