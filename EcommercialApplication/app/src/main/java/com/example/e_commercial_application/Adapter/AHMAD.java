package com.example.e_commercial_application.Adapter;

import android.content.Context;
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
import com.example.e_commercial_application.Databases.BasketDBDiscounted;
import com.example.e_commercial_application.HomePage;
import com.example.e_commercial_application.Model.AllProducts;
import com.example.e_commercial_application.Model.DiscountedProducts;
import com.example.e_commercial_application.ProductDetails;
import com.example.e_commercial_application.ProductDetailsDiscounted;
import com.example.e_commercial_application.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class AHMAD extends RecyclerView.Adapter {

    private static List<AllProducts> allProductsList = new ArrayList<>();
    Context context;
    BasketDB basketDB;
    BasketDBDiscounted basketDBDiscounted;

    DiscountedProducts discountedProducts;
    AllProducts allProducts;
    TextView totalPrice;

    public AHMAD(Context context, ArrayList<AllProducts> allProductsList, TextView totalPrice) {
        this.context = context;
        this.allProductsList = allProductsList;
        this.totalPrice = totalPrice;
    }

    @Override
    public int getItemViewType(int position) {
        return (allProductsList.get(position) instanceof DiscountedProducts) ? 1 : 2;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        switch (viewType){
            case 1:
                View DiscountedLayout = LayoutInflater.from(parent.getContext()).inflate(R.layout.my_basket_layout_discounted,parent,false);
                return new DiscountedViewHolder(DiscountedLayout);
            case 2:
                View AllProductLayout = LayoutInflater.from(parent.getContext()).inflate(R.layout.my_basket_layout,parent,false);
                return new AllProductViewHolder(AllProductLayout);
            default:return null;
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        basketDB = new BasketDB(context);
        basketDBDiscounted = new BasketDBDiscounted(context);

        if (allProductsList.get(position) instanceof DiscountedProducts){


            discountedProducts = new DiscountedProducts();
            DiscountedProducts basketItem =(DiscountedProducts) allProductsList.get(position);
            readCursorDataDiscounted(discountedProducts, (DiscountedViewHolder) holder);
            ((DiscountedViewHolder)holder).basketProductName.setText(basketItem.getProductName());
            ((DiscountedViewHolder)holder).basketProductPrice.setText(basketItem.getProductPrice() + " $");

            ((DiscountedViewHolder)holder).oldPrice.setText(basketItem.getOldPrice() + " $");
            ((DiscountedViewHolder)holder).itemPiece.setText(basketItem.getNumber() + "");
            Glide.with(context).load(basketItem.getProductImg()).into(((DiscountedViewHolder)holder).basketImg);

            AllProducts products = allProductsList.get(holder.getAdapterPosition());

            int quantity = products.getNumber();
            double amount = products.getProductPrice();
            DecimalFormat df = new DecimalFormat("#.##", new DecimalFormatSymbols(Locale.getDefault(Locale.Category.FORMAT)));
            DecimalFormat df1 = new DecimalFormat("#.##", new DecimalFormatSymbols(Locale.ENGLISH));

            double total = Double.parseDouble(df1.format(quantity*amount).replace(",", "."));
            ((DiscountedViewHolder)holder).basketProductPrice.setText((total + " $"));

            ((DiscountedViewHolder)holder).decreaseItem.setOnClickListener(view -> {
                if (products.getNumber() > 1) {
                    int quantity1 = products.getNumber() - 1;
                    products.setNumber(quantity1);
                    ((DiscountedViewHolder)holder).itemPiece.setText(String.valueOf(products.getNumber()));
                    double amount1 = products.getProductPrice();
                    double totalAmount = Double.parseDouble(((DiscountedViewHolder)holder).basketProductPrice.getText().toString().replace("$", "").replace(",", "."));
                    double total1 = Double.parseDouble(df1.format(totalAmount - amount1));
                    ((DiscountedViewHolder)holder).basketProductPrice.setText(String.format("%.2f $", total1));


                    DecimalFormat dfTotal = new DecimalFormat("0.00", new DecimalFormatSymbols(Locale.ENGLISH));

                    String formattedTotalPrice = dfTotal.format(getTotalPrice());
                    totalPrice.setText(String.format(Locale.ENGLISH, "%s $", formattedTotalPrice));

                    basketDBDiscounted.updateQuantity(products.getId(), quantity1);
                }
            });

            ((DiscountedViewHolder)holder).increaseItem.setOnClickListener(view -> {
                int quantity1 = products.getNumber() + 1;
                products.setNumber(quantity1);
                ((DiscountedViewHolder)holder).itemPiece.setText(String.valueOf(quantity1));
                double amount12 = products.getProductPrice();
                double total12 = Double.parseDouble(df1.format(quantity1 * amount12));

                ((DiscountedViewHolder)holder).basketProductPrice.setText(String.format("%.2f $", total12));

                DecimalFormat dfTotal = new DecimalFormat("0.00", new DecimalFormatSymbols(Locale.ENGLISH));

                String formattedTotalPrice = dfTotal.format(getTotalPrice());
                totalPrice.setText(String.format(Locale.ENGLISH, "%s $", formattedTotalPrice));

                basketDBDiscounted.updateQuantity(products.getId(), quantity1);
            });

            ((DiscountedViewHolder)holder).itemView.setOnClickListener(view -> {

                HomePage homePage = (HomePage) context;
                BottomNavigationView bottomNavigationView = homePage.findViewById(R.id.bottom_nav);
                bottomNavigationView.setVisibility(View.GONE);

                FragmentManager fragmentManager = ((AppCompatActivity)context).getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction().addToBackStack(null);
                Bundle bundle = new Bundle();
                bundle.putSerializable("productDiscounted",allProductsList.get(holder.getAdapterPosition()));
                ProductDetailsDiscounted productDetails3 = new ProductDetailsDiscounted();
                productDetails3.setArguments(bundle);
                fragmentTransaction.replace(R.id.containerFrame,productDetails3).commit();

            });


            ((DiscountedViewHolder)holder).delete.setOnClickListener(view -> {

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

        else {


            allProducts = new AllProducts();
            AllProducts basketItem = allProductsList.get(position);
            readCursorData(allProducts, (AllProductViewHolder) holder);
            ((AllProductViewHolder)holder).basketProductName.setText(basketItem.getProductName());
            ((AllProductViewHolder)holder).basketProductPrice.setText(basketItem.getProductPrice() + " $");
            ((AllProductViewHolder)holder).itemPiece.setText(basketItem.getNumber() + "");
            Glide.with(context).load(basketItem.getProductImg()).into(((AllProductViewHolder)holder).basketImg);

            AllProducts allProducts = allProductsList.get(holder.getAdapterPosition());

            int quantity = allProducts.getNumber();
            double amount = allProducts.getProductPrice();
            DecimalFormat df1 = new DecimalFormat("#.##", new DecimalFormatSymbols(Locale.ENGLISH));

            double total = Double.parseDouble(df1.format(quantity*amount).replace(",", "."));
            ((AllProductViewHolder)holder).basketProductPrice.setText((total + " $"));

            ((AllProductViewHolder)holder).decreaseItem.setOnClickListener(view -> {
                if (allProducts.getNumber() > 1) {
                    int quantity1 = allProducts.getNumber() - 1;
                    allProducts.setNumber(quantity1);
                    ((AllProductViewHolder)holder).itemPiece.setText(String.valueOf(allProducts.getNumber()));
                    double amount1 = allProducts.getProductPrice();
                    double totalAmount = Double.parseDouble(((AllProductViewHolder)holder).basketProductPrice.getText().toString().replace("$", "").replace(",", "."));
                    double total1 = Double.parseDouble(df1.format(totalAmount - amount1));
                    ((AllProductViewHolder)holder).basketProductPrice.setText(String.format("%.2f $", total1));


                    DecimalFormat dfTotal = new DecimalFormat("0.00", new DecimalFormatSymbols(Locale.ENGLISH));

                    String formattedTotalPrice = dfTotal.format(getTotalPrice());
                    totalPrice.setText(String.format(Locale.ENGLISH, "%s $", formattedTotalPrice));

                    basketDB.updateQuantity(allProducts.getId(), quantity1);
                }
            });

            ((AllProductViewHolder)holder).increaseItem.setOnClickListener(view -> {
                int quantity1 = allProducts.getNumber() + 1;
                allProducts.setNumber(quantity1);
                ((AllProductViewHolder)holder).itemPiece.setText(String.valueOf(quantity1));
                double amount12 = allProducts.getProductPrice();
                double total12 = Double.parseDouble(df1.format(quantity1 * amount12));

                ((AllProductViewHolder)holder).basketProductPrice.setText(String.format("%.2f $", total12));

                DecimalFormat dfTotal = new DecimalFormat("0.00", new DecimalFormatSymbols(Locale.ENGLISH));

                String formattedTotalPrice = dfTotal.format(getTotalPrice());
                totalPrice.setText(String.format(Locale.ENGLISH, "%s $", formattedTotalPrice));

                basketDB.updateQuantity(allProducts.getId(), quantity1);
            });

            ((AllProductViewHolder)holder).itemView.setOnClickListener(view -> {

                HomePage homePage = (HomePage) context;
                BottomNavigationView bottomNavigationView = homePage.findViewById(R.id.bottom_nav);
                bottomNavigationView.setVisibility(View.GONE);

                FragmentManager fragmentManager = ((AppCompatActivity)context).getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction().addToBackStack(null);
                Bundle bundle = new Bundle();
                bundle.putSerializable("productName",allProductsList.get(holder.getAdapterPosition()));
                ProductDetails productDetails3 = new ProductDetails();
                productDetails3.setArguments(bundle);
                fragmentTransaction.replace(R.id.containerFrame,productDetails3).commit();




            });



            ((AllProductViewHolder)holder).delete.setOnClickListener(view -> {

                basketDB.deleteBasketItem(allProducts.getId());

                for (int i = 0; i < HomePage.basketList.size(); i++) {
                    AllProducts basketProduct = HomePage.basketList.get(i);
                    if (basketProduct.getId().equals(allProducts.getId())) {
                        HomePage.basketList.remove(i);
                        break;
                    }
                }

                notifyDataSetChanged();
                DecimalFormat dfTotal = new DecimalFormat("0.00", new DecimalFormatSymbols(Locale.ENGLISH));
                String formattedTotalPrice = dfTotal.format(getTotalPrice());
                totalPrice.setText(String.format(Locale.ENGLISH, "%s $", formattedTotalPrice));

            });


        }

    }

    private void readCursorData(AllProducts allProducts, AHMAD.AllProductViewHolder viewHolder) {

        Cursor cursor = basketDB.read_all_data(allProducts.getId());
        SQLiteDatabase db = basketDB.getReadableDatabase();
        if (cursor!=null && cursor.isClosed())
            cursor.close();
        db.close();

    }


    private void readCursorDataDiscounted(DiscountedProducts products, AHMAD.DiscountedViewHolder viewHolder) {

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
        return allProductsList.size();
    }

    public static class DiscountedViewHolder extends RecyclerView.ViewHolder{

        ImageView basketImg, increaseItem, decreaseItem, delete;
        TextView basketProductName;
        TextView basketProductPrice,totalPrice,oldPrice;
        ConstraintLayout buyConstraint, emptyConstraint, constraintLayout2;
        Button btnContinue;
        public TextView itemPiece;
        RecyclerView SelectedProducts;

        public DiscountedViewHolder(@NonNull View itemView) {
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

    public static class AllProductViewHolder extends RecyclerView.ViewHolder{

        ImageView basketImg, increaseItem, decreaseItem, delete;
        TextView basketProductName;
        TextView basketProductPrice,totalPrice;
        ConstraintLayout buyConstraint, emptyConstraint, constraintLayout2;
        Button btnContinue;
        public TextView itemPiece;
        RecyclerView SelectedProducts;

        public AllProductViewHolder(@NonNull View itemView) {
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
            delete = itemView.findViewById(R.id.delete);
        }
    }


}
