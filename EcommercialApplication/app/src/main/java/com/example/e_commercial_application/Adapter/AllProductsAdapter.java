package com.example.e_commercial_application.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.e_commercial_application.AllProductsFragment;
import com.example.e_commercial_application.HomePage;
import com.example.e_commercial_application.Model.AllProducts;
import com.example.e_commercial_application.R;

import java.util.List;

public class AllProductsAdapter extends RecyclerView.Adapter<AllProductsAdapter.ViewHolder>{

    private final List<AllProducts> allProductsList;
    Context context;


    public AllProductsAdapter(List<AllProducts> allProductsList, Context context) {
        this.allProductsList = allProductsList;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.new_season_all_products_layout,parent,false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;



    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {


        AllProducts allProducts = allProductsList.get(position);
        String productName = allProducts.getProductName();

        if (productName.length()>35){

            holder.ProductNameAll.setText(productName.substring(0,35) + "...");

        }else {
            holder.ProductNameAll.setText(allProducts.getProductName());
        }


        holder.ProductPriceAll.setText(allProducts.getProductPrice() + "$" );
        Glide.with(context).load(allProducts.getProductImg()).into(holder.ProductImgAll);

        holder.AllProducts.setOnClickListener(view -> {
            Intent intent = new Intent(context,HomePage.class);
            context.startActivity(intent);

        });


    }

    @Override
    public int getItemCount() {
        return allProductsList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        ImageView ProductImgAll;
        TextView ProductNameAll,ProductPriceAll;
        ImageButton ProductFavAll;
        CardView AllProducts;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            ProductImgAll = itemView.findViewById(R.id.ProductImgAll);
            ProductNameAll = itemView.findViewById(R.id.ProductNameAll);
            ProductPriceAll = itemView.findViewById(R.id.ProductPriceAll);;
            ProductFavAll = itemView.findViewById(R.id.favProductAll);
            AllProducts = itemView.findViewById(R.id.AllProductsCard);

        }
    }

}
