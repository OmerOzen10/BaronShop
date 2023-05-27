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
import com.example.e_commercial_application.Databases.BasketDBDiscounted;
import com.example.e_commercial_application.HomePage;
import com.example.e_commercial_application.Model.AllProducts;
import com.example.e_commercial_application.Model.DiscountedProducts;
import com.example.e_commercial_application.ProductDetailsDiscounted;
import com.example.e_commercial_application.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.Locale;

public class BasketDiscountedAdapter extends RecyclerView.Adapter<BasketDiscountedAdapter.ViewHolder> {

    public ArrayList<DiscountedProducts> basketArrayList;
    private DiscountedProducts discountedProducts;
    Context context;
    private static final String TAG = "BasketDiscountedAdapter";
    TextView totalPrice;
    BasketDBDiscounted basketDBDiscounted;

    public BasketDiscountedAdapter(ArrayList<DiscountedProducts> basketArrayList, Context context, TextView totalPrice){
        this.basketArrayList = basketArrayList;
        this.context = context;
        this.totalPrice = totalPrice;
    }
    public BasketDiscountedAdapter(){}


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        basketDBDiscounted = new BasketDBDiscounted(context);
        SharedPreferences prefs = context.getSharedPreferences("prefs",Context.MODE_PRIVATE);
        boolean firstStart = prefs.getBoolean("firstStart",true);
        if (firstStart){
            createTableOnFirstStart();
        }
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.my_basket_layout_discounted, parent, false);
        return new ViewHolder(view);
    }
    private void createTableOnFirstStart() {
        basketDBDiscounted.insertEmpty();
        SharedPreferences prefs = context.getSharedPreferences("prefs",Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean("firstStart", false);
        editor.apply();
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        discountedProducts = new DiscountedProducts();
        DiscountedProducts basketItem = basketArrayList.get(position);
        readCursorData(discountedProducts,holder);
        holder.basketProductName.setText(basketItem.getProductName());
        holder.basketProductPrice.setText(basketItem.getProductPrice() + " $");

        holder.oldPrice.setText(basketItem.getOldPrice() + " $");
        holder.itemPiece.setText(basketItem.getNumber() + "");
        Glide.with(context).load(basketItem.getProductImg()).into(holder.basketImg);

        DiscountedProducts products = basketArrayList.get(holder.getAdapterPosition());

        int quantity = products.getNumber();
        double amount = products.getProductPrice();
        DecimalFormat df = new DecimalFormat("#.##", new DecimalFormatSymbols(Locale.getDefault(Locale.Category.FORMAT)));
        DecimalFormat df1 = new DecimalFormat("#.##", new DecimalFormatSymbols(Locale.ENGLISH));

        double total = Double.parseDouble(df1.format(quantity*amount).replace(",", "."));
        holder.basketProductPrice.setText((total + " $"));

        holder.decreaseItem.setOnClickListener(view -> {
            if (products.getNumber() > 1) {
                int quantity1 = products.getNumber() - 1;
                products.setNumber(quantity1);
                holder.itemPiece.setText(String.valueOf(products.getNumber()));
                double amount1 = products.getProductPrice();
                double totalAmount = Double.parseDouble(holder.basketProductPrice.getText().toString().replace("$", "").replace(",", "."));
                double total1 = Double.parseDouble(df1.format(totalAmount - amount1));
                holder.basketProductPrice.setText(String.format("%.2f $", total1));


                DecimalFormat dfTotal = new DecimalFormat("0.00", new DecimalFormatSymbols(Locale.ENGLISH));

                String formattedTotalPrice = dfTotal.format(getTotalPrice());
                totalPrice.setText(String.format(Locale.ENGLISH, "%s $", formattedTotalPrice));

                basketDBDiscounted.updateQuantity(products.getId(), quantity1);
            }
        });

        holder.increaseItem.setOnClickListener(view -> {
            int quantity1 = products.getNumber() + 1;
            products.setNumber(quantity1);
            holder.itemPiece.setText(String.valueOf(quantity1));
                double amount12 = products.getProductPrice();
            double total12 = Double.parseDouble(df1.format(quantity1 * amount12));

            holder.basketProductPrice.setText(String.format("%.2f $", total12));

            DecimalFormat dfTotal = new DecimalFormat("0.00", new DecimalFormatSymbols(Locale.ENGLISH));

            String formattedTotalPrice = dfTotal.format(getTotalPrice());
            totalPrice.setText(String.format(Locale.ENGLISH, "%s $", formattedTotalPrice));

            basketDBDiscounted.updateQuantity(products.getId(), quantity1);
        });

        holder.itemView.setOnClickListener(view -> {

            HomePage homePage = (HomePage) context;
            DiscountedAdapter adapter = new DiscountedAdapter(basketArrayList,homePage);

            BottomNavigationView bottomNavigationView = homePage.findViewById(R.id.bottom_nav);
            bottomNavigationView.setVisibility(View.GONE);

            FragmentManager fragmentManager = ((AppCompatActivity)context).getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction().addToBackStack(null);
            Bundle bundle = new Bundle();
            bundle.putSerializable("productDiscounted",basketArrayList.get(holder.getAdapterPosition()));
            ProductDetailsDiscounted productDetails3 = new ProductDetailsDiscounted();
            productDetails3.setArguments(bundle);
            fragmentTransaction.replace(R.id.containerFrame,productDetails3).commit();

            });


        holder.delete.setOnClickListener(view -> {

            basketDBDiscounted.deleteBasketItem(products.getId());

            for (int i = 0; i < HomePage.basketList2.size(); i++) {
                DiscountedProducts basketProduct = HomePage.basketList2.get(i);
                if (basketProduct.getId().equals(products.getId())) {
                    HomePage.basketList2.remove(i);
                    break;
                }
            }


            notifyDataSetChanged();
            DecimalFormat dfTotal = new DecimalFormat("0.00", new DecimalFormatSymbols(Locale.ENGLISH));
            String formattedTotalPrice = dfTotal.format(getTotalPrice());
            totalPrice.setText(String.format(Locale.ENGLISH, "%s $", formattedTotalPrice));
        });


    }

    @SuppressLint("SuspiciousIndentation")
    private void readCursorData(DiscountedProducts products, BasketDiscountedAdapter.ViewHolder viewHolder) {

        Cursor cursor = basketDBDiscounted.read_all_data(products.getId());
        SQLiteDatabase db = basketDBDiscounted.getReadableDatabase();
        if (cursor!=null && cursor.isClosed())
            cursor.close();
        db.close();

    }

    public double getTotalPrice() {
        double totalPrice = 0;
        double totalPriceDiscounted = 0;
        double total;

        for (AllProducts products : HomePage.basketList){

            totalPrice += products.getNumber() * products.getProductPrice();

        }

        for(DiscountedProducts discountedProducts : HomePage.basketList2){

            totalPriceDiscounted += discountedProducts.getNumber() * discountedProducts.getProductPrice();
        }

        total = totalPriceDiscounted + totalPrice;

        DecimalFormat df = new DecimalFormat("#0.00", new DecimalFormatSymbols(Locale.ENGLISH));
        String formattedPrice = df.format(total);
        notifyDataSetChanged();
        return Double.parseDouble(formattedPrice);
    }


    @Override
    public int getItemCount() {
        return basketArrayList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        ImageView basketImg, increaseItem, decreaseItem, delete;
        TextView basketProductName;
        TextView basketProductPrice,totalPrice,oldPrice;
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
            oldPrice = itemView.findViewById(R.id.OldPriceBasket);
            delete = itemView.findViewById(R.id.delete);
        }
    }
}
