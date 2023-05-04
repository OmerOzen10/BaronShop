package com.example.e_commercial_application;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.example.e_commercial_application.Model.AllProducts;

import java.util.List;

public class productDetails3 extends Fragment {
    public AllProducts allProducts;
    private static List<AllProducts> allProductsList;
    private static final String TAG = "ProductDetails2";
    private ImageView detailedProductImg,favIcon;
    private TextView detailedProductName, detailedProductPrice,rate;

    private RatingBar ratingBar;
    private Button btnBasket;
    BasketDB basketDB;
    CardView favProduct;
    FavDB favDB;

    public static ProductDetails newInstance(AllProducts allProducts) {
        return null;
    }
    public productDetails3(){}

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Bundle bundle = getArguments();

        if (bundle!=null){
            allProducts = (AllProducts) bundle.getSerializable("productBasket");
        }else {
            Log.d(TAG, "onViewCreated: bundle null");
        }
        detailedProductImg = view.findViewById(R.id.detailImg);
        detailedProductName = view.findViewById(R.id.detailedName);
        detailedProductPrice = view.findViewById(R.id.detailedPrice);
        btnBasket = view.findViewById(R.id.btnBasket);
        basketDB = new BasketDB(getContext());
        favProduct = view.findViewById(R.id.favProductDetail);
        favDB = new FavDB(requireActivity());
        favIcon = view.findViewById(R.id.favIcon);
        ratingBar = view.findViewById(R.id.ratingBar2);
        rate = view.findViewById(R.id.detailedRating);
        if (allProducts == null){
            Log.d(TAG, "onViewCreated: null");
        } else {
            detailedProductName.setText(allProducts.getProductName());
            detailedProductPrice.setText(allProducts.getProductPrice() + "$");
            Glide.with(this).load(allProducts.getProductImg()).into(detailedProductImg);
            ratingBar.setRating(allProducts.getProductRate());
            rate.setText(allProducts.getProductRate() + " ");

            Log.d(TAG, "onViewCreated: " + allProducts.getProductRate());
        }

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_product_details,container,false);
    }
}
