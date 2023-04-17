package com.example.e_commercial_application;

import android.media.Image;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.e_commercial_application.Model.AllProducts;

import java.util.Objects;

public class ProductDetails extends Fragment {

    public AllProducts allProducts;
    private static final String TAG = "ProductDetails";

    private ImageView detailedProductImg;
    private TextView detailedProductName, detailedProductPrice;

    private RatingBar ratingBar;
    
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

        if (allProducts == null){
            Log.d(TAG, "onViewCreated: null" );
        }else {
            Glide.with(requireContext()).load(allProducts.getProductImg()).into(detailedProductImg);
            detailedProductName.setText(allProducts.getProductName());
            detailedProductPrice.setText(allProducts.getProductPrice() + "$");

        }


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_product_details, container, false);
    }
}