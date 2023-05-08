package com.example.e_commercial_application.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.e_commercial_application.Databases.FavDB;
import com.example.e_commercial_application.Databases.FavDB2;
import com.example.e_commercial_application.HomePage;
import com.example.e_commercial_application.Model.AllProducts;
import com.example.e_commercial_application.Model.DiscountedProducts;
import com.example.e_commercial_application.R;

import java.util.List;

public class DiscountedAdapter extends RecyclerView.Adapter<DiscountedAdapter.ViewHolder> {

    private static List<DiscountedProducts> discountedProductsList;
    private static final String TAG = "DiscountedAdapter";
    Context context;
    private static FavDB2 favDB2;

    public DiscountedAdapter(List<DiscountedProducts> discountedProductsList, Context context){
        this.discountedProductsList = discountedProductsList;
        this.context = context;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        favDB2 = new FavDB2(context);
        SharedPreferences prefs = context.getSharedPreferences("prefs",Context.MODE_PRIVATE);
        boolean firstStart = prefs.getBoolean("firstStart",true);
        if (firstStart){
            createTableOnFirstStart();
        }
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.discounted_layout,parent,false);
        return new ViewHolder(view);
    }
    private void createTableOnFirstStart() {
        favDB2.insertEmpty();
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

    }

    private void readCursorData(DiscountedProducts discountedProducts, ViewHolder viewHolder) {

        Cursor cursor = favDB2.read_all_data(discountedProducts.getId());
        SQLiteDatabase db = favDB2.getReadableDatabase();
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
                    favDB2.insertIntoTheDatabase(discountedProducts.getProductName(),discountedProducts.getProductImg(),discountedProducts.getId(), discountedProducts.getFavStatus(),String.valueOf(discountedProducts.getProductRate()),String.valueOf(discountedProducts.getProductPrice()),String.valueOf(discountedProducts.getOldPrice()));
                    favProductDiscounted.setBackgroundResource(R.drawable.ic_fav_red);

                    HomePage.favList2.add(discountedProducts);
                    Log.d(TAG, "onClick: favList" + HomePage.favList.size());

                } else {
                    discountedProducts.setFavStatus("0");
                    favDB2.remove_fav(discountedProducts.getId());
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
