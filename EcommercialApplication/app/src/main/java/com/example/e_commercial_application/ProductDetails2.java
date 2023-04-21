package com.example.e_commercial_application;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.bumptech.glide.Glide;
import com.example.e_commercial_application.Model.AllProducts;
//import com.example.e_commercial_application.Model.NewSeason;
import com.google.android.material.appbar.MaterialToolbar;

import org.checkerframework.checker.units.qual.A;

public class ProductDetails2 extends Fragment {

//    public NewSeason newSeason;
    public AllProducts allProducts;
    private static final String TAG = "ProductDetails2";
    private ImageView detailedProductImg;
    private TextView detailedProductName, detailedProductPrice;

    private RatingBar ratingBar;

    public static ProductDetails newInstance(AllProducts allProducts) {
        return null;
    }
    public ProductDetails2(){}


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Bundle bundle = getArguments();
        if (bundle != null){
//            newSeason = (NewSeason) bundle.getSerializable("productName2");
            allProducts = (AllProducts) bundle.getSerializable("productName2");
        }else {
            Log.d(TAG, "onViewCreated: bundle is null");
        }
        detailedProductImg = view.findViewById(R.id.detailImg);
        detailedProductName = view.findViewById(R.id.detailedName);
        detailedProductPrice = view.findViewById(R.id.detailedPrice);

//        if (newSeason == null){
//            Log.d(TAG, "onViewCreated: null");
//        }else {
//            Glide.with(requireContext()).load(newSeason.getProductImg()).into(detailedProductImg);
//            detailedProductName.setText(newSeason.getProductName());
//            detailedProductPrice.setText(newSeason.getProductPrice() + "$");
//        }

        if (allProducts == null){
            Log.d(TAG, "onViewCreated: null");
        }else {
            Glide.with(requireContext()).load(allProducts.getProductImg()).into(detailedProductImg);
            detailedProductName.setText(allProducts.getProductName());
            detailedProductPrice.setText(allProducts.getProductPrice() + "$");
        }

        MaterialToolbar toolbar =view.findViewById(R.id.toolbar2);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                FragmentManager fragmentManager = ((AppCompatActivity) getContext()).getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.containerFrame,new HomeFragment()).commit();

            }
        });


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_product_details, container, false);
    }
}
