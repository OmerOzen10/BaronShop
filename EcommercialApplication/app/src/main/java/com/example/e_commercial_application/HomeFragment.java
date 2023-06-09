package com.example.e_commercial_application;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

//import com.example.e_commercial_application.Adapter.NewSeasonAdapter;
import com.example.e_commercial_application.Adapter.DiscountedAdapter;
import com.example.e_commercial_application.Adapter.NewSeasonAdapter;
//import com.example.e_commercial_application.Model.NewSeason;
import com.example.e_commercial_application.Model.AllProducts;
import com.example.e_commercial_application.Model.DiscountedProducts;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.ArrayList;


public class HomeFragment extends Fragment {


    NewSeasonAdapter newSeasonAdapter;

    DiscountedAdapter discountedAdapter;
    FirebaseFirestore firebaseFirestore;

    ArrayList<AllProducts> allProductsArrayList;

    ArrayList<DiscountedProducts> discountedProductsArrayList;


    TextView txtNewSeasonAllProducts, txtDiscountedAllProducts;

    FrameLayout frameLayout;

    private static final String TAG = "HomeFragment";





    private RecyclerView newSeasonRecyclerView;

    private RecyclerView discountedProducts;
    public HomeFragment() {
        // Required empty public constructor
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);


    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        txtNewSeasonAllProducts = view.findViewById(R.id.newSeasonAllProducts);

        newSeasonRecyclerView = view.findViewById(R.id.newSeasonRecyclerView);
        newSeasonRecyclerView.setHasFixedSize(true);
        newSeasonRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.HORIZONTAL,false));

        firebaseFirestore = FirebaseFirestore.getInstance();

        allProductsArrayList = new ArrayList<AllProducts>();
        newSeasonAdapter = new NewSeasonAdapter(allProductsArrayList,getContext());

        newSeasonRecyclerView.setAdapter(newSeasonAdapter);
        
        EventChangeListener();

        txtDiscountedAllProducts = view.findViewById(R.id.DiscountedAllProducts);
        discountedProducts = view.findViewById(R.id.discountedProducts);
        discountedProducts.setHasFixedSize(true);
        discountedProducts.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.HORIZONTAL,false));

        discountedProductsArrayList = new ArrayList<>();
        discountedAdapter = new DiscountedAdapter(discountedProductsArrayList,getContext());

        discountedProducts.setAdapter(discountedAdapter);

        EventChangeListener2();




        txtNewSeasonAllProducts.setOnClickListener(view1 -> {
            HomePage homePage = (HomePage) getActivity();
            BottomNavigationView bottomNavigationView = homePage.findViewById(R.id.bottom_nav);
            bottomNavigationView.setVisibility(View.GONE);

            FragmentManager fragmentManager = ((AppCompatActivity)getContext()).getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction().addToBackStack(null);
            fragmentTransaction.replace(R.id.containerFrame, new AllProductsFragment()).addToBackStack(null).commit();
        });

        txtDiscountedAllProducts.setOnClickListener(view1 -> {
            HomePage homePage = (HomePage) getActivity();
            BottomNavigationView bottomNavigationView = homePage.findViewById(R.id.bottom_nav);
            bottomNavigationView.setVisibility(View.GONE);

            FragmentManager fragmentManager = ((AppCompatActivity)getContext()).getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction().addToBackStack(null);
            fragmentTransaction.replace(R.id.containerFrame, new AllProductsDiscounted()).addToBackStack(null).commit();
        });

    }

    private void EventChangeListener() {
        firebaseFirestore.collection("AllProducts")
                .orderBy("ProductPrice", Query.Direction.ASCENDING)
                .limit(4) // add this line to limit the number of documents retrieved
                .addSnapshotListener((value, error) -> {
                    if (error != null) {
                        Log.w(TAG, "Listen failed.", error);
                        return;
                    }
                    for (DocumentChange documentChange : value.getDocumentChanges()){
                        if (documentChange.getType() == DocumentChange.Type.ADDED){
                            allProductsArrayList.add(documentChange.getDocument().toObject(AllProducts.class));
                        }
                        newSeasonAdapter.notifyDataSetChanged();
                    }
                });
    }

    private void EventChangeListener2() {
        firebaseFirestore.collection("DiscountedProducts")
                .orderBy("ProductPrice", Query.Direction.ASCENDING)
                .limit(4) // add this line to limit the number of documents retrieved
                .addSnapshotListener((value, error) -> {
                    if (error != null) {
                        Log.w(TAG, "Listen failed.", error);
                        return;
                    }
                    for (DocumentChange documentChange : value.getDocumentChanges()){
                        if (documentChange.getType() == DocumentChange.Type.ADDED){
                            discountedProductsArrayList.add(documentChange.getDocument().toObject(DiscountedProducts.class));
                        }
                        discountedAdapter.notifyDataSetChanged();
                    }
                });
    }

}