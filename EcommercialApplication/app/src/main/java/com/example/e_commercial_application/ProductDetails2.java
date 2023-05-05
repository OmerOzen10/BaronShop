package com.example.e_commercial_application;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
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
import com.example.e_commercial_application.Adapter.NewSeasonAdapter;
import com.example.e_commercial_application.Model.AllProducts;
//import com.example.e_commercial_application.Model.NewSeason;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.checkerframework.checker.units.qual.A;

import java.util.List;

public class ProductDetails2 extends Fragment {
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
            allProducts = (AllProducts) bundle.getSerializable("productName2");
        }else {
            Log.d(TAG, "onViewCreated: bundle is null");
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

        if (allProducts.getFavStatus() != null && allProducts.getFavStatus().equals("0")){
            favIcon.setBackgroundResource(R.drawable.baseline_fav);
        } else if (allProducts.getFavStatus() !=null && allProducts.getFavStatus().equals("1")){
            favIcon.setBackgroundResource(R.drawable.ic_fav_red);
        }


        favProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (allProducts.getFavStatus() != null && allProducts.getFavStatus().equals("0")){
                    allProducts.setFavStatus("1");
                    favDB.insertIntoTheDatabase(allProducts.getProductName(),allProducts.getProductImg(),allProducts.getId(),allProducts.getFavStatus(),String.valueOf(allProducts.getProductRate()),String.valueOf(allProducts.getProductPrice()));
                    favIcon.setBackgroundResource(R.drawable.ic_fav_red);
                    HomePage.favList.add(allProducts);
                } else if (allProducts.getFavStatus() !=null && allProducts.getFavStatus().equals("1")){
                    allProducts.setFavStatus("0");
                    favDB.remove_fav(allProducts.getId());
                    for (int i = 0; i<HomePage.favList.size(); i++){
                        AllProducts favProduct = HomePage.favList.get(i);
                        if (favProduct.getId().equals(allProducts.getId())){
                            HomePage.favList.remove(i);
                            break;
                        }
                    }
                    favIcon.setBackgroundResource(R.drawable.baseline_fav);
                }

            }
        });

        btnBasket.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                int match = 0;

                for (AllProducts product : HomePage.basketList) {
                    if (allProducts.getProductName().equals(product.getProductName())) {
                        int currentNumber = product.getNumber();
                        product.setNumber(++currentNumber);

                        match++;

                        basketDB.updateQuantity(allProducts.getId(),currentNumber);


                    }
                }

                if (match == 0) {
                    HomePage.basketList.add(allProducts);
                    Log.d(TAG, "onClick: basketItem" + HomePage.basketList.size());
                    basketDB.insertIntoTheDatabase(allProducts.getProductName(),allProducts.getProductImg(),allProducts.getId(),String.valueOf(allProducts.getProductPrice()),String.valueOf(allProducts.getNumber()),String.valueOf(allProducts.getProductRate()),allProducts.getFavStatus());
                }







                FragmentManager fragmentManager = ((AppCompatActivity)getContext()).getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.containerFrame, new BasketFragment()).addToBackStack(null).commit();
                Log.d(TAG, "onClick: basket" + " " + ((HomePage) requireActivity()).basketList.size());

            }
        });

        if (allProducts == null){
            Log.d(TAG, "onViewCreated: null");
        }else {
            Glide.with(requireContext()).load(allProducts.getProductImg()).into(detailedProductImg);
            detailedProductName.setText(allProducts.getProductName());
            detailedProductPrice.setText(allProducts.getProductPrice() + "$");
            ratingBar.setRating(allProducts.getProductRate());
            rate.setText(allProducts.getProductRate()+ "");
        }

        MaterialToolbar toolbar =view.findViewById(R.id.toolbar2);
        toolbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

//                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
//
//                if (fragmentManager.getBackStackEntryCount() > 0){
//                    fragmentManager.popBackStack();
//                    Fragment lastFragment = fragmentManager.findFragmentById(R.id.containerFrame);
//
//                    if (lastFragment != null){
//                        fragmentManager.beginTransaction().replace(R.id.containerFrame,lastFragment).addToBackStack(null).commit();
//                    }
//                }else {
//                    Intent intent = new Intent(getContext(),HomePage.class);
//                    startActivity(intent);
//                }
//                HomePage homePage = (HomePage) getContext();
//                NewSeasonAdapter adapter = new NewSeasonAdapter(allProductsList, homePage);
//                BottomNavigationView bottomNavigationView = homePage.findViewById(R.id.bottom_nav);
//                bottomNavigationView.setVisibility(View.VISIBLE);

                Intent intent = new Intent(getContext(),HomePage.class);
                startActivity(intent);
            }
        });

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_product_details, container, false);
    }
}
