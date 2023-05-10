package com.example.e_commercial_application.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.e_commercial_application.Databases.BasketDB;
import com.example.e_commercial_application.HomePage;
import com.example.e_commercial_application.Model.AllProducts;
import com.example.e_commercial_application.ProductDetails;
import com.example.e_commercial_application.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.Locale;

public class BasketAdapter extends RecyclerView.Adapter<BasketAdapter.ViewHolder> {

    public ArrayList<AllProducts>basketArrayList;
    private AllProducts allProducts;
    Context context;
    private static final String TAG = "BasketAdapter";
    TextView totalPrice;
    BasketDB basketDB;


    public BasketAdapter(ArrayList<AllProducts> basketArrayList, Context context, TextView totalPrice) {
        this.basketArrayList = basketArrayList;
        this.context = context;
        this.totalPrice = totalPrice;
    }
    public BasketAdapter(){}

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        basketDB = new BasketDB(context);
        SharedPreferences prefs = context.getSharedPreferences("prefs",Context.MODE_PRIVATE);
        boolean firstStart = prefs.getBoolean("firstStart",true);
        if (firstStart){
            createTableOnFirstStart();
        }
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.my_basket_layout, parent, false);
        return new ViewHolder(view);
    }
    private void createTableOnFirstStart() {
        basketDB.insertEmpty();
        SharedPreferences prefs = context.getSharedPreferences("prefs",Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean("firstStart", false);
        editor.apply();
    }
    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        allProducts = new AllProducts();
        AllProducts basketItem = basketArrayList.get(position);
        readCursorData(allProducts,holder);
        holder.basketProductName.setText(basketItem.getProductName());
        holder.basketProductPrice.setText(basketItem.getProductPrice() + " $");
        holder.itemPiece.setText(basketItem.getNumber() + "");
        Glide.with(context).load(basketItem.getProductImg()).into(holder.basketImg);

        AllProducts allProducts = basketArrayList.get(holder.getAdapterPosition());

        int quantity = allProducts.getNumber();
        double amount = allProducts.getProductPrice();
        DecimalFormat df = new DecimalFormat("#.##", new DecimalFormatSymbols(Locale.getDefault(Locale.Category.FORMAT)));
        DecimalFormat df1 = new DecimalFormat("#.##", new DecimalFormatSymbols(Locale.ENGLISH));

        double total = Double.parseDouble(df1.format(quantity*amount).replace(",", "."));
        holder.basketProductPrice.setText((total + " $"));

        holder.decreaseItem.setOnClickListener(view -> {
            if (allProducts.getNumber() > 1) {
                int quantity1 = allProducts.getNumber() - 1;
                allProducts.setNumber(quantity1);
                holder.itemPiece.setText(String.valueOf(allProducts.getNumber()));
                double amount1 = allProducts.getProductPrice();
                double totalAmount = Double.parseDouble(holder.basketProductPrice.getText().toString().replace("$", "").replace(",", "."));
                double total1 = Double.parseDouble(df1.format(totalAmount - amount1));
                holder.basketProductPrice.setText(String.format("%.2f $", total1));


                DecimalFormat dfTotal = new DecimalFormat("0.00", new DecimalFormatSymbols(Locale.ENGLISH));

                String formattedTotalPrice = dfTotal.format(getTotalPrice());
                totalPrice.setText(String.format(Locale.ENGLISH, "%s $", formattedTotalPrice));

                basketDB.updateQuantity(allProducts.getId(), quantity1);
            }
        });

        holder.increaseItem.setOnClickListener(view -> {
            int quantity1 = allProducts.getNumber() + 1;
            allProducts.setNumber(quantity1);
            holder.itemPiece.setText(String.valueOf(quantity1));
            double amount12 = allProducts.getProductPrice();
            double total12 = Double.parseDouble(df1.format(quantity1 * amount12));

            holder.basketProductPrice.setText(String.format("%.2f $", total12));

            DecimalFormat dfTotal = new DecimalFormat("0.00", new DecimalFormatSymbols(Locale.ENGLISH));

            String formattedTotalPrice = dfTotal.format(getTotalPrice());
            totalPrice.setText(String.format(Locale.ENGLISH, "%s $", formattedTotalPrice));

            basketDB.updateQuantity(allProducts.getId(), quantity1);
        });

        holder.itemView.setOnClickListener(view -> {

            HomePage homePage = (HomePage) context;
            NewSeasonAdapter adapter = new NewSeasonAdapter(basketArrayList,homePage);

            BottomNavigationView bottomNavigationView = homePage.findViewById(R.id.bottom_nav);
            bottomNavigationView.setVisibility(View.GONE);

            FragmentManager fragmentManager = ((AppCompatActivity)context).getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction().addToBackStack(null);
            Bundle bundle = new Bundle();
            bundle.putSerializable("productName",basketArrayList.get(holder.getAdapterPosition()));
            ProductDetails productDetails3 = new ProductDetails();
            productDetails3.setArguments(bundle);
            fragmentTransaction.replace(R.id.containerFrame,productDetails3).commit();




        });





    }

    @SuppressLint("SuspiciousIndentation")
    private void readCursorData(AllProducts allProducts, BasketAdapter.ViewHolder viewHolder) {

        Cursor cursor = basketDB.read_all_data(allProducts.getId());
        SQLiteDatabase db = basketDB.getReadableDatabase();
            if (cursor!=null && cursor.isClosed())
                cursor.close();
            db.close();

    }

    public double getTotalPrice() {
        double totalPrice = 0;
        for (AllProducts product : HomePage.basketList) {
            totalPrice += product.getNumber() * product.getProductPrice();
        }
        DecimalFormat df = new DecimalFormat("#0.00", new DecimalFormatSymbols(Locale.ENGLISH));
        String formattedPrice = df.format(totalPrice);
        return Double.parseDouble(formattedPrice);
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
        RecyclerView SelectedProducts;


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
            SelectedProducts = itemView.findViewById(R.id.SelectedProducts);



        }
    }
}
