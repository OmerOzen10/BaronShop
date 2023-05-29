package com.example.e_commercial_application;
import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.e_commercial_application.Adapter.BasketAdapter;
import com.example.e_commercial_application.Databases.BasketDB;
import com.example.e_commercial_application.Databases.BasketDBDiscounted;
import com.example.e_commercial_application.Model.AllProducts;
import com.example.e_commercial_application.Model.DiscountedProducts;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;


import jp.wasabeef.blurry.Blurry;

public class BasketFragment extends Fragment{

    AllProducts allProducts;
    DiscountedProducts discountedProducts;

    public static RecyclerView basketRecyclerView;

    private static BasketAdapter adapter;

    private static ConstraintLayout buyConstraint, emptyConstraint, rootView;
    static TextView totalPrice,priceBasket;
    ImageView backBasket, backCard;
    Button btnContinue,btnConfirmBasket, btnConfirm;
    View overlay;

    BasketDB basketDB;

    BasketDBDiscounted basketDBDiscounted;

    CardView orderDetails;
    TextInputLayout layoutName, layoutEmail, layoutCity, layoutTown, layoutAddress, layoutMobile;
    TextInputEditText edtName, edtEmail, edtCity, edtTown, edtAddress, edtMobile;

    AlertDialog.Builder builder;

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
        discountedProducts = new DiscountedProducts();
        totalPrice = view.findViewById(R.id.totalPrice);
        emptyConstraint = view.findViewById(R.id.emptyConstraint);
        buyConstraint = view.findViewById(R.id.buyConstraint);
        btnContinue = view.findViewById(R.id.btnContinue);
        builder = new AlertDialog.Builder(getContext());

        basketRecyclerView = view.findViewById(R.id.SelectedProducts);
        basketRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        adapter = new BasketAdapter(getContext(),HomePage.basketList,totalPrice);
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
        basketDBDiscounted = new BasketDBDiscounted(getContext());

        layoutName = view.findViewById(R.id.layoutName);
        layoutEmail = view.findViewById(R.id.layoutEmail);
        layoutCity = view.findViewById(R.id.layoutCity);
        layoutTown = view.findViewById(R.id.layoutTown);
        layoutAddress = view.findViewById(R.id.layoutAddress);
        layoutMobile = view.findViewById(R.id.layoutMobile);

        edtName = view.findViewById(R.id.edtName);
        edtEmail = view.findViewById(R.id.edtEmail);
        edtCity = view.findViewById(R.id.edtCity);
        edtTown = view.findViewById(R.id.edtTown);
        edtAddress = view.findViewById(R.id.edtAddress);
        edtMobile = view.findViewById(R.id.edtMobile);

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

                    if (Correction()){

                        builder.setTitle("Authentication").setMessage("Do you have an Account?").setCancelable(false).setPositiveButton("Yes", (dialogInterface, i) -> {
                            Intent intent = new Intent(getContext(), UserFragment.class);
                            startActivity(intent);
                        }).setNegativeButton("No", (dialogInterface, i) -> {

                            orderDetails.setVisibility(View.GONE);
                            overlay.setVisibility(View.GONE);
                            basketRecyclerView.setVisibility(View.VISIBLE);
                            basketRecyclerView.setEnabled(true);
                            btnConfirmBasket.setEnabled(true);
                            Blurry.delete((ViewGroup) rootView);

                            basketDB.deleteAllBasketItems();
                            HomePage.basketList.clear();
                            basketDBDiscounted.deleteAllBasketItems();
                            HomePage.basketList2.clear();
                            emptyConstraint.setVisibility(View.VISIBLE);
                            backBasket.setEnabled(true);
                            buyConstraint.setVisibility(View.GONE);

                        }).show();




                    }else {
                        Toast.makeText(getContext(), "Something Went Wrong!!", Toast.LENGTH_SHORT).show();
                    }


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


        if (adapter.getItemCount()==0) {
            buyConstraint.setVisibility(View.GONE);
            emptyConstraint.setVisibility(View.VISIBLE);
            basketRecyclerView.setVisibility(View.GONE);
        } else {
            buyConstraint.setVisibility(View.VISIBLE);

            BasketAdapter adapter = new BasketAdapter();
            double total = adapter.getTotalPrice();
            totalPrice.setText((total + " $"));


        }
        Log.d(TAG, "onViewCreated: total "  +  totalPrice.getText().toString());

        btnContinue.setOnClickListener(view1 -> {
            Intent intent = new Intent(getContext(),HomePage.class);
            startActivity(intent);
        });


    }

    public static void ifEmpty(){
        if (adapter.getItemCount()==0) {
            buyConstraint.setVisibility(View.GONE);
            emptyConstraint.setVisibility(View.VISIBLE);
            basketRecyclerView.setVisibility(View.GONE);
        } else {
            buyConstraint.setVisibility(View.VISIBLE);

            BasketAdapter adapter = new BasketAdapter();
            double total = adapter.getTotalPrice();
            totalPrice.setText((total + " $"));


        }
    }

    private boolean Correction(){

        if (edtName.getText().toString().isEmpty()){
            layoutName.setError("Required");
            edtName.requestFocus();
            return false;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(edtEmail.getText().toString()).matches()){
            layoutEmail.setError("Invalid Email Address");
            edtEmail.requestFocus();
            return false;
        }
        if (edtEmail.getText().toString().isEmpty()){
            layoutEmail.setError("Required");
            edtEmail.requestFocus();
            return false;
        }
        if (edtCity.getText().toString().isEmpty()){
            layoutCity.setError("Required");
            edtCity.requestFocus();
            return false;
        }
        if (edtTown.getText().toString().isEmpty()){
            layoutTown.setError("Required");
            edtTown.requestFocus();
            return false;
        }
        if (edtAddress.getText().toString().isEmpty()){
            layoutAddress.setError("Required");
            edtAddress.requestFocus();
            return false;
        }
        if (edtMobile.getText().toString().isEmpty()){
            layoutMobile.setError("Required");
            edtMobile.requestFocus();
            return false;
        }
        return true;

    }





    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_basket, container, false);
    }

}