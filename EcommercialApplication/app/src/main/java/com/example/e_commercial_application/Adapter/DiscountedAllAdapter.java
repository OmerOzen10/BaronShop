package com.example.e_commercial_application.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
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

import java.util.List;

public class DiscountedAllAdapter extends RecyclerView.Adapter<DiscountedAllAdapter.ViewHolder> {

    private final List<DiscountedProducts> allProductsList;
    Context context;
    FavDBDiscounted favDB;

    public DiscountedAllAdapter(List<DiscountedProducts> allProductsList,Context context) {
        this.allProductsList = allProductsList;
        this.context = context;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.new_season_all_products_layout,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        DiscountedProducts allProducts = allProductsList.get(position);
        favDB = new FavDBDiscounted(context);
        readCursorData(allProducts,holder);

        String productName = allProducts.getProductName();

        if (productName.length()>35){

            holder.ProductNameAll.setText(productName.substring(0,35) + "...");

        }else {
            holder.ProductNameAll.setText(allProducts.getProductName());
        }


        holder.ProductPriceAll.setText(allProducts.getProductPrice() + "$" );
        holder.oldPrice.setText(allProducts.getOldPrice() + " $");
        String old = holder.oldPrice.getText().toString();
        if (!old.isEmpty()){
            holder.materialDivider.setVisibility(View.VISIBLE);
            holder.oldPrice.setVisibility(View.VISIBLE);
        }
        Glide.with(context).load(allProducts.getProductImg()).centerInside().into(holder.ProductImgAll);


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                FragmentManager fragmentManager = ((AppCompatActivity)context).getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction().addToBackStack(null);
                Bundle bundle = new Bundle();
                bundle.putSerializable("productDiscounted",allProductsList.get(holder.getAdapterPosition()));
                ProductDetailsDiscounted productDetails = new ProductDetailsDiscounted();
                productDetails.setArguments(bundle);
                fragmentTransaction.replace(R.id.containerFrame, productDetails).commit();
            }
        });

    }

    private void readCursorData(DiscountedProducts allProducts, DiscountedAllAdapter.ViewHolder viewHolder) {

        Cursor cursor = favDB.read_all_data(allProducts.getId());
        SQLiteDatabase db = favDB.getReadableDatabase();
        try {
            while (cursor.moveToNext()) {
                @SuppressLint("Range") String item_fav_status = cursor.getString(cursor.getColumnIndex(FavDB.FAVORITE_STATUS));
                allProducts.setFavStatus(item_fav_status);

                if (item_fav_status != null && item_fav_status.equals("1")) {
                    viewHolder.favIcon.setBackgroundResource(R.drawable.ic_fav_red);
                } else if (item_fav_status != null && item_fav_status.equals("0")) {
                    viewHolder.favIcon.setBackgroundResource(R.drawable.baseline_fav);
                }
            }
        } finally {
            if (cursor != null && cursor.isClosed())
                cursor.close();
            db.close();
        }
    }


        @Override
    public int getItemCount() {
        return allProductsList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        ImageView ProductImgAll;
        TextView ProductNameAll,ProductPriceAll,oldPrice;
        ImageView favIcon;
        CardView AllProducts, ProductFavAll;
        com.google.android.material.divider.MaterialDivider materialDivider;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ProductImgAll = itemView.findViewById(R.id.ProductImgAll);
            ProductNameAll = itemView.findViewById(R.id.ProductNameAll);
            ProductPriceAll = itemView.findViewById(R.id.ProductPriceAll);;
            ProductFavAll = itemView.findViewById(R.id.favProductAll);
            AllProducts = itemView.findViewById(R.id.AllProductsCard);
            favIcon = itemView.findViewById(R.id.favIcon);
            oldPrice = itemView.findViewById(R.id.OldPrice);
            materialDivider = itemView.findViewById(R.id.divider);

            ProductFavAll.setOnClickListener(view -> {
                int position = getAdapterPosition();
                DiscountedProducts allProducts = allProductsList.get(position);

                if (allProducts.getFavStatus() != null && allProducts.getFavStatus().equals("0")){
                    allProducts.setFavStatus("1");
                    favDB.insertIntoTheDatabase(allProducts.getProductName(),allProducts.getProductImg(),allProducts.getId(), allProducts.getFavStatus(),String.valueOf(allProducts.getProductRate()),String.valueOf(allProducts.getProductPrice()),String.valueOf(allProducts.getOldPrice()));
                    favIcon.setBackgroundResource(R.drawable.ic_fav_red);

                    HomePage.favList2.add(allProducts);

                } else {
                    allProducts.setFavStatus("0");
                    favDB.remove_fav(allProducts.getId());
                    favIcon.setBackgroundResource(R.drawable.baseline_fav);
                    for (int i = 0; i < HomePage.favList2.size(); i++) {
                        DiscountedProducts favProduct = (DiscountedProducts) HomePage.favList2.get(i);
                        if (favProduct.getId().equals(allProducts.getId())) {
                            HomePage.favList2.remove(i);
                            break;
                        }
                    }
                }

            });
        }
    }

}
