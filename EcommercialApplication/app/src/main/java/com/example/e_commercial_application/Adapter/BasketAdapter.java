package com.example.e_commercial_application.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.e_commercial_application.BasketFragment;
import com.example.e_commercial_application.HomePage;
import com.example.e_commercial_application.Model.AllProducts;
import com.example.e_commercial_application.R;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class BasketAdapter extends RecyclerView.Adapter<BasketAdapter.ViewHolder> {

    private ArrayList<AllProducts>basketArrayList;
    private AllProducts allProducts;
    Context context;
    private static final String TAG = "BasketAdapter";
    TextView totalPrice;



    public BasketAdapter(ArrayList<AllProducts> basketArrayList, Context context, TextView totalPrice) {
        this.basketArrayList = basketArrayList;
        this.context = context;
        this.totalPrice = totalPrice;
    }
    public BasketAdapter(){}

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.my_basket_layout, parent, false);
        return new ViewHolder(view);
    }
    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        allProducts = new AllProducts();
        AllProducts basketItem = basketArrayList.get(position);

        holder.basketProductName.setText(basketItem.getProductName());
        holder.basketProductPrice.setText(basketItem.getProductPrice() + "$");
        holder.itemPiece.setText(basketItem.getNumber() + "");
        Glide.with(context).load(basketItem.getProductImg()).into(holder.basketImg);

        AllProducts allProducts = basketArrayList.get(holder.getAdapterPosition());

        int quantity = allProducts.getNumber();
        double amount = allProducts.getProductPrice();
        DecimalFormat df = new DecimalFormat("#.##");
        double total = Double.parseDouble(df.format(quantity*amount));
        holder.basketProductPrice.setText((total + " $"));

        holder.decreaseItem.setOnClickListener(view -> {

            if (allProducts.getNumber() > 1) {
                int quantity1 = allProducts.getNumber() - 1;
                allProducts.setNumber(quantity1);
                holder.itemPiece.setText(String.valueOf(allProducts.getNumber()));
                double amount1 = allProducts.getProductPrice();
                DecimalFormat df1 = new DecimalFormat("#.##");
                double totalAmount = Double.parseDouble(holder.basketProductPrice.getText().toString().replace("$", ""));

                double total1 = Double.parseDouble(df1.format(totalAmount - amount1));
                holder.basketProductPrice.setText(String.format("%.2f $", total1));
            }
            totalPrice.setText(getTotalPrice()+ " $");
        });

        holder.increaseItem.setOnClickListener(view -> {
            int quantity1 = allProducts.getNumber() + 1;
            allProducts.setNumber(quantity1);
            holder.itemPiece.setText(String.valueOf(quantity1));
            double amount12 = allProducts.getProductPrice();
            DecimalFormat df12 = new DecimalFormat("#.##");
            double total12 = Double.parseDouble(df12.format(quantity1* amount12));
            holder.basketProductPrice.setText((total12 + " $"));

            totalPrice.setText(getTotalPrice()+ " $");
        });




    }

    public double getTotalPrice() {
        double totalPrice = 0;
        for (AllProducts product : HomePage.basketList) {
            totalPrice += product.getNumber() * product.getProductPrice();
        }
        return totalPrice;
    }


    @Override
    public int getItemCount() {
        return basketArrayList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        ImageView basketImg, increaseItem, decreaseItem;
        TextView basketProductName;
        TextView basketProductPrice,totalPrice;
        ConstraintLayout buyConstraint, emptyConstraint, constraintLayout2;
        Button btnContinue;
        public TextView itemPiece;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            basketImg = itemView.findViewById(R.id.basketImg);
            basketProductName = itemView.findViewById(R.id.basketProductName);
            basketProductPrice = itemView.findViewById(R.id.priceBasket);
            itemPiece = itemView.findViewById(R.id.itemPiece);
            increaseItem = itemView.findViewById(R.id.increaseItem);
            decreaseItem = itemView.findViewById(R.id.decreaseItem);
            buyConstraint = itemView.findViewById(R.id.buyConstraint);
            emptyConstraint = itemView.findViewById(R.id.emptyConstraint);
            btnContinue = itemView.findViewById(R.id.btnContinue);
            totalPrice = itemView.findViewById(R.id.totalPrice);
            constraintLayout2 = itemView.findViewById(R.id.constraintLayout2);


        }
    }
}
