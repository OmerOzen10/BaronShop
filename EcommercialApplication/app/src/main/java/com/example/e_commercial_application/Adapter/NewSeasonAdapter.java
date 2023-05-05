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
import com.example.e_commercial_application.FavDB;
import com.example.e_commercial_application.HomePage;
import com.example.e_commercial_application.Model.AllProducts;
//import com.example.e_commercial_application.Model.NewSeason;
import com.example.e_commercial_application.ProductDetails;
import com.example.e_commercial_application.ProductDetails2;
import com.example.e_commercial_application.R;
//import com.example.e_commercial_application.SQL.MyDatabaseHelper;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.checkerframework.checker.units.qual.A;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

public class NewSeasonAdapter extends RecyclerView.Adapter<NewSeasonAdapter.NewSeasonViewHolder> {
    private static List<AllProducts> allProductsList;
    private static final String TAG = "NewSeasonAdapter";
    Context context;
    private static FavDB favDB;
public NewSeasonAdapter(List<AllProducts> newSeasonList, Context context) {
    this.allProductsList = newSeasonList;
    this.context = context;
}

    @NonNull
    @Override
    public NewSeasonViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        favDB = new FavDB(context);
        SharedPreferences prefs = context.getSharedPreferences("prefs",Context.MODE_PRIVATE);
        boolean firstStart = prefs.getBoolean("firstStart",true);
        if (firstStart){
            createTableOnFirstStart();
        }
        View view1 = LayoutInflater.from(parent.getContext()).inflate(R.layout.new_season_layout,parent,false);
        return new NewSeasonViewHolder(view1);


    }
    private void createTableOnFirstStart() {
        favDB.insertEmpty();
        SharedPreferences prefs = context.getSharedPreferences("prefs",Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean("firstStart", false);
        editor.apply();
    }

    @Override
    public void onBindViewHolder(@NonNull NewSeasonViewHolder holder, int position) {

     final AllProducts allProducts = allProductsList.get(position);
     readCursorData(allProducts,holder);
     holder.ProductNameNewSeason.setText(allProducts.getProductName());
     holder.ProductPriceNewSeason.setText(allProducts.getProductPrice() + " $");
     Glide.with(context).load(allProducts.getProductImg()).into(holder.ProductImgNewSeason);







        holder.itemView.setOnClickListener(view -> {

            FragmentManager fragmentManager = ((AppCompatActivity)context).getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction().addToBackStack(null);
            Bundle bundle = new Bundle();
            bundle.putSerializable("productName2",allProductsList.get(holder.getAdapterPosition()));
            ProductDetails2 productDetails2 = new ProductDetails2();
            productDetails2.setArguments(bundle);
            fragmentTransaction.replace(R.id.containerFrame, productDetails2).addToBackStack(null).commit();
            HomePage homePage = (HomePage) context;
            NewSeasonAdapter adapter = new NewSeasonAdapter(allProductsList, homePage);
            BottomNavigationView bottomNavigationView = homePage.findViewById(R.id.bottom_nav);
            bottomNavigationView.setVisibility(View.GONE);
        });

    }

    private void readCursorData(AllProducts allProducts, NewSeasonViewHolder viewHolder) {

        Cursor cursor = favDB.read_all_data(allProducts.getId());
        SQLiteDatabase db = favDB.getReadableDatabase();
        try {
            while (cursor.moveToNext()){
                @SuppressLint("Range") String item_fav_status = cursor.getString(cursor.getColumnIndex(FavDB.FAVORITE_STATUS));
                allProducts.setFavStatus(item_fav_status);

                if (item_fav_status != null && item_fav_status.equals("1")){
                    viewHolder.favProductNewSeason.setBackgroundResource(R.drawable.ic_fav_red);
                } else if (item_fav_status !=null && item_fav_status.equals("0")) {
                    viewHolder.favProductNewSeason.setBackgroundResource(R.drawable.baseline_fav);
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
        return allProductsList.size();
    }

    public static class NewSeasonViewHolder extends RecyclerView.ViewHolder {


        ImageView ProductImgNewSeason;
        TextView ProductNameNewSeason, ProductPriceNewSeason;

        ImageView favProductNewSeason;
        CardView favProductContainer;


        public NewSeasonViewHolder(@NonNull View itemView) {
            super(itemView);

            ProductImgNewSeason = itemView.findViewById(R.id.ProductImg);
            ProductNameNewSeason = itemView.findViewById(R.id.txtProductName);
            ProductPriceNewSeason = itemView.findViewById(R.id.ProductPrice);
            favProductNewSeason = itemView.findViewById(R.id.fav_Product);
            favProductContainer = itemView.findViewById(R.id.favProductContainer);

            favProductContainer.setOnClickListener(view -> {
                int position = getAdapterPosition();
                AllProducts allProducts = allProductsList.get(position);

                if (allProducts.getFavStatus() != null && allProducts.getFavStatus().equals("0")){
                    allProducts.setFavStatus("1");
                    favDB.insertIntoTheDatabase(allProducts.getProductName(),allProducts.getProductImg(),allProducts.getId(), allProducts.getFavStatus(),String.valueOf(allProducts.getProductRate()),String.valueOf(allProducts.getProductPrice()));
                    favProductNewSeason.setBackgroundResource(R.drawable.ic_fav_red);

                    HomePage.favList.add(allProducts);
                    Log.d(TAG, "onClick: favList" + HomePage.favList.size());

                } else {
                    allProducts.setFavStatus("0");
                    favDB.remove_fav(allProducts.getId());
                    favProductNewSeason.setBackgroundResource(R.drawable.baseline_fav);
                    for (int i = 0; i < HomePage.favList.size(); i++) {
                        AllProducts favProduct = HomePage.favList.get(i);
                        if (favProduct.getId().equals(allProducts.getId())) {
                            HomePage.favList.remove(i);
                            break;
                        }
                    }

//                        HomePage.favList.remove(allProducts);
                    Log.d(TAG, "onClick: favList" + HomePage.favList.size());

                }

            });
        }


    }
}
