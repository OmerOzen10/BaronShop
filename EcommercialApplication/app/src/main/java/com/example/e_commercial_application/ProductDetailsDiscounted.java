package com.example.e_commercial_application;

import android.content.Intent;
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
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.bumptech.glide.Glide;
import com.example.e_commercial_application.Databases.BasketDBDiscounted;
import com.example.e_commercial_application.Databases.FavDBDiscounted;
import com.example.e_commercial_application.Model.AllProducts;
import com.example.e_commercial_application.Model.DiscountedProducts;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class ProductDetailsDiscounted extends Fragment {

    public DiscountedProducts discountedProducts;
    private static final String TAG = "ProductDetails";

    private ImageView detailedProductImg;
    private TextView detailedProductName, detailedProductPrice;

    private RatingBar ratingBar;
    private TextView rate,totalPrice,oldPrice;
    Button addToBasket;
    BasketDBDiscounted basketDBDiscounted;
    ImageView favIcon;
    CardView favProduct;
    FavDBDiscounted favDBDiscounted;

    public ProductDetailsDiscounted() {

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Bundle bundle = getArguments();
        if (bundle != null){
            discountedProducts = (DiscountedProducts) bundle.getSerializable("productDiscounted");

        }else {
            Log.d(TAG, "onViewCreated: bundle is null" );
        }

        detailedProductImg = view.findViewById(R.id.detailImg);
        detailedProductName = view.findViewById(R.id.detailedName);
        detailedProductPrice = view.findViewById(R.id.detailedPrice);
        ratingBar = view.findViewById(R.id.ratingBar2);
        rate = view.findViewById(R.id.detailedRating);
        totalPrice = view.findViewById(R.id.totalPrice);
        oldPrice = view.findViewById(R.id.OldPrice);
        basketDBDiscounted = new BasketDBDiscounted(getContext());
        favIcon = view.findViewById(R.id.favIcon);
        favProduct = view.findViewById(R.id.favProductDetail);
        favDBDiscounted = new FavDBDiscounted(getContext());

        if (discountedProducts == null){
            Log.d(TAG, "onViewCreated: null" );
        }else {
            Glide.with(requireContext()).load(discountedProducts.getProductImg()).into(detailedProductImg);
            detailedProductName.setText(discountedProducts.getProductName());
            detailedProductPrice.setText(discountedProducts.getProductPrice() + "$");
            oldPrice.setText(discountedProducts.getOldPrice() + " $");
            ratingBar.setRating(discountedProducts.getProductRate());
            rate.setText(discountedProducts.getProductRate() + "");
            Log.d(TAG, "onViewCreated: ratingbar" + ratingBar.getRating() + rate.getText().toString());
        }

        addToBasket = view.findViewById(R.id.btnBasket);
        addToBasket.setOnClickListener(view1 -> {



            int match = 0;

            for (AllProducts product : HomePage.basketList) {
                if (discountedProducts.getProductName().equals(product.getProductName())) {
                    int currentNumber = product.getNumber();
                    product.setNumber(++currentNumber);

                    match++;

                    basketDBDiscounted.updateQuantity(discountedProducts.getId(),currentNumber);


                }
            }

            if (match == 0) {
                HomePage.basketList.add(discountedProducts);

                basketDBDiscounted.insertIntoTheDatabase(discountedProducts.getProductName(),discountedProducts.getProductImg(),discountedProducts.getId(),String.valueOf(discountedProducts.getProductPrice()),String.valueOf(discountedProducts.getOldPrice()), String.valueOf(discountedProducts.getNumber()),String.valueOf(discountedProducts.getProductRate()),discountedProducts.getFavStatus());

            }







            FragmentManager fragmentManager = ((AppCompatActivity)getContext()).getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.containerFrame, new BasketFragment()).addToBackStack(null).commit();
            Log.d(TAG, "onClick: basket" + " " + ((HomePage) requireActivity()).basketList.size());
            Log.d(TAG, "onViewCreated: oldPrice" + discountedProducts.getOldPrice());

        });

        MaterialToolbar toolbar =view.findViewById(R.id.toolbar2);
        toolbar.setNavigationOnClickListener(v -> {

            HomePage homePage = (HomePage) getActivity();
            BottomNavigationView bottomNavigationView = homePage.findViewById(R.id.bottom_nav);
            bottomNavigationView.setVisibility(View.VISIBLE);

            FragmentManager fragmentManager = getActivity().getSupportFragmentManager();

            if (fragmentManager.getBackStackEntryCount() > 0) {
                fragmentManager.popBackStack();
                Fragment lastFragment = fragmentManager.findFragmentById(R.id.containerFrame);

                if (lastFragment != null) {
                    fragmentManager.beginTransaction().replace(R.id.containerFrame, lastFragment).addToBackStack(null).commit();
                }
            } else {
                Intent intent = new Intent(getContext(), HomePage.class);
                startActivity(intent);
            }

        });

        if (discountedProducts.getFavStatus() != null && discountedProducts.getFavStatus().equals("0")){
            favIcon.setBackgroundResource(R.drawable.baseline_fav);
        } else if (discountedProducts.getFavStatus() !=null && discountedProducts.getFavStatus().equals("1")){
            favIcon.setBackgroundResource(R.drawable.ic_fav_red);
        }

        favProduct.setOnClickListener(view1 -> {

            if (discountedProducts.getFavStatus() != null && discountedProducts.getFavStatus().equals("0")){
                discountedProducts.setFavStatus("1");
                favDBDiscounted.insertIntoTheDatabase(discountedProducts.getProductName(),discountedProducts.getProductImg(),discountedProducts.getId(),discountedProducts.getFavStatus(),String.valueOf(discountedProducts.getProductRate()),String.valueOf(discountedProducts.getProductPrice()),String.valueOf(discountedProducts.getOldPrice()));
                favIcon.setBackgroundResource(R.drawable.ic_fav_red);
                HomePage.favList.add(discountedProducts);
            } else if (discountedProducts.getFavStatus() !=null && discountedProducts.getFavStatus().equals("1")){
                discountedProducts.setFavStatus("0");
                favDBDiscounted.remove_fav(discountedProducts.getId());
                for (int i = 0; i<HomePage.favList.size(); i++){
                    DiscountedProducts favProduct = (DiscountedProducts) HomePage.favList.get(i);
                    if (favProduct.getId().equals(discountedProducts.getId())){
                        HomePage.favList.remove(i);
                        break;
                    }
                }
                favIcon.setBackgroundResource(R.drawable.baseline_fav);
            }

        });

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_product_details_discounted, container,false);
    }
}
