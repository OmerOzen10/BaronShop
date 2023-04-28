package com.example.e_commercial_application;

import android.database.Cursor;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.e_commercial_application.Adapter.BasketAdapter;
import com.example.e_commercial_application.Model.AllProducts;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;

public class BasketFragment extends Fragment{

    AllProducts allProducts;

    private RecyclerView basketRecyclerView;
    private BasketAdapter adapter;
    private ConstraintLayout buyConstraint, emptyConstraint;
    TextView totalPrice,priceBasket;
    Button btnContinue;
    private static final String TAG = "BasketFragment";

    public BasketFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        allProducts = new AllProducts();
        totalPrice = view.findViewById(R.id.totalPrice);
        emptyConstraint = view.findViewById(R.id.emptyConstraint);
        buyConstraint = view.findViewById(R.id.buyConstraint);
        btnContinue = view.findViewById(R.id.btnContinue);
        basketRecyclerView = view.findViewById(R.id.SelectedProducts);
        basketRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapter = new BasketAdapter(HomePage.basketList,getContext(), totalPrice);
        basketRecyclerView.setAdapter(adapter);
        priceBasket = view.findViewById(R.id.priceBasket);

        MaterialToolbar toolbar =view.findViewById(R.id.toolbar3);
        toolbar.setNavigationOnClickListener(v -> {

            HomePage homePage = (HomePage) getActivity();
            BottomNavigationView bottomNavigationView = homePage.findViewById(R.id.bottom_nav);
            bottomNavigationView.setVisibility(View.VISIBLE);

            FragmentManager fragmentManager = ((AppCompatActivity) getContext()).getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.containerFrame,new HomeFragment()).commit();

        });

        if (adapter.getItemCount() == 0){
            buyConstraint.setVisibility(View.GONE);
            emptyConstraint.setVisibility(View.VISIBLE);
            basketRecyclerView.setVisibility(View.GONE);
        }else {
            buyConstraint.setVisibility(View.VISIBLE);
            BasketAdapter adapter1 = new BasketAdapter();
            double total  = adapter1.getTotalPrice();
            totalPrice.setText((total + " $"));
        }

        btnContinue.setOnClickListener(view1 -> {



            FragmentManager fragmentManager = ((AppCompatActivity)getContext()).getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.containerFrame, new HomeFragment()).commit();
        });

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_basket, container, false);
    }

}