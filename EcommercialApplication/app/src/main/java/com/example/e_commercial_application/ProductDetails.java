package com.example.e_commercial_application;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.e_commercial_application.Model.AllProducts;
//import com.example.e_commercial_application.Model.NewSeason;
import com.google.android.material.appbar.MaterialToolbar;

import java.util.ArrayList;
import java.util.Objects;

public class ProductDetails extends Fragment {

    public AllProducts allProducts;
    private static final String TAG = "ProductDetails";

    private ImageView detailedProductImg;
    private TextView detailedProductName, detailedProductPrice;

    private RatingBar ratingBar;
    private TextView rate;
    Button addToBasket;
    
    public ProductDetails() {

    }

    public static ProductDetails newInstance(AllProducts allProducts) {
        return null;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);



        Bundle bundle = getArguments();
        if (bundle != null){
            allProducts = (AllProducts) bundle.getSerializable("productName");
        }else {
            Log.d(TAG, "onViewCreated: bundle is null" );
        }

        detailedProductImg = view.findViewById(R.id.detailImg);
        detailedProductName = view.findViewById(R.id.detailedName);
        detailedProductPrice = view.findViewById(R.id.detailedPrice);
        ratingBar = view.findViewById(R.id.ratingBar2);
        rate = view.findViewById(R.id.detailedRating);

        if (allProducts == null){
            Log.d(TAG, "onViewCreated: null" );
        }else {
            Glide.with(requireContext()).load(allProducts.getProductImg()).into(detailedProductImg);
            detailedProductName.setText(allProducts.getProductName());
            detailedProductPrice.setText(allProducts.getProductPrice() + "$");
            ratingBar.setRating(allProducts.getProductRate());
            rate.setText(allProducts.getProductRate() + "");

        }

        addToBasket = view.findViewById(R.id.btnBasket);
        addToBasket.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ((HomePage) requireActivity()).basketList.add(allProducts);
                Toast.makeText(requireContext(), "Product added to basket!", Toast.LENGTH_SHORT).show();

                Log.d(TAG, "onClick: basket" + " " + ((HomePage) requireActivity()).basketList.size());

//                BasketFragment basketFragment = new BasketFragment();
//                FragmentManager fragmentManager = getParentFragmentManager();
//                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//                fragmentTransaction.replace(R.id.containerFrame,basketFragment).commit();

            }
        });

        MaterialToolbar toolbar =view.findViewById(R.id.toolbar2);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                FragmentManager fragmentManager = ((AppCompatActivity) getContext()).getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.containerFrame,new AllProductsFragment()).commit();

            }
        });


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_product_details, container, false);
    }
}