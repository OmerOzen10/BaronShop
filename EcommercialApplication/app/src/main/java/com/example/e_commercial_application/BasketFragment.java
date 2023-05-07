package com.example.e_commercial_application;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
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
import android.widget.ImageView;
import android.widget.TextView;

import com.example.e_commercial_application.Adapter.BasketAdapter;
import com.example.e_commercial_application.Model.AllProducts;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.bottomnavigation.BottomNavigationView;


import java.util.ArrayList;
import java.util.List;

import jp.wasabeef.blurry.Blurry;

public class BasketFragment extends Fragment{

    AllProducts allProducts;

    public RecyclerView basketRecyclerView;
    private BasketAdapter adapter;
    private ConstraintLayout buyConstraint, emptyConstraint, rootView;
    TextView totalPrice,priceBasket;
    ImageView backBasket, backCard;
    Button btnContinue,btnConfirmBasket, btnConfirm;
    View overlay;

    BasketDB basketDB;

    CardView orderDetails;

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
        adapter = new BasketAdapter(HomePage.basketList, getContext(), totalPrice);
        basketRecyclerView.setAdapter(adapter);
        priceBasket = view.findViewById(R.id.priceBasket);
        backBasket = view.findViewById(R.id.backBasket);
        btnConfirmBasket = view.findViewById(R.id.btnConfirmBasket);
        orderDetails = view.findViewById(R.id.orderDetails);
        overlay = view.findViewById(R.id.overlay);
        rootView = view.findViewById(R.id.rootView);
        backCard = view.findViewById(R.id.backCard);
        btnConfirm = view.findViewById(R.id.btnConfirm);
        basketDB = new BasketDB(getContext());

        btnConfirmBasket.setOnClickListener(view1 -> {
            orderDetails.setVisibility(View.VISIBLE);
            overlay.setVisibility(View.VISIBLE);
            Blurry.with(getContext())
                    .radius(25)
                    .sampling(2)
                    .color(Color.argb(66, 0, 0, 0))
                    .animate(500)
                    .onto(rootView);
            basketRecyclerView.setClickable(false);
            basketRecyclerView.setEnabled(false);
            basketRecyclerView.setVisibility(View.GONE);
            btnConfirmBasket.setEnabled(false);
            backBasket.setEnabled(false);

            backCard.setOnClickListener(view2 -> {
                orderDetails.setVisibility(View.GONE);
                overlay.setVisibility(View.GONE);
                basketRecyclerView.setVisibility(View.VISIBLE);
                basketRecyclerView.setEnabled(true);
                btnConfirmBasket.setEnabled(true);
                Blurry.delete((ViewGroup) rootView);

            });

            btnConfirm.setOnClickListener(view2 -> {
                orderDetails.setVisibility(View.GONE);
                overlay.setVisibility(View.GONE);
                basketRecyclerView.setVisibility(View.VISIBLE);
                basketRecyclerView.setEnabled(true);
                btnConfirmBasket.setEnabled(true);
                Blurry.delete((ViewGroup) rootView);

                basketDB.deleteAllBasketItems();
                HomePage.basketList.clear();
                emptyConstraint.setVisibility(View.VISIBLE);
                backBasket.setEnabled(true);
                buyConstraint.setVisibility(View.GONE);

            });



        });


        backBasket.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
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
            }
        });


        if (adapter.getItemCount() == 0) {
            buyConstraint.setVisibility(View.GONE);
            emptyConstraint.setVisibility(View.VISIBLE);
            basketRecyclerView.setVisibility(View.GONE);
        } else {
            buyConstraint.setVisibility(View.VISIBLE);
            BasketAdapter adapter1 = new BasketAdapter();
            double total = adapter1.getTotalPrice();
            totalPrice.setText((total + " $"));
        }

        btnContinue.setOnClickListener(view1 -> {
            Intent intent = new Intent(getContext(),HomePage.class);
            startActivity(intent);
        });


    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_basket, container, false);
    }

}