package com.example.e_commercial_application;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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

import com.example.e_commercial_application.Adapter.AllProductsAdapter;
//import com.example.e_commercial_application.Adapter.NewSeasonAdapter;
import com.example.e_commercial_application.Adapter.NewSeasonAdapter;
import com.example.e_commercial_application.Model.AllProducts;
//import com.example.e_commercial_application.Model.NewSeason;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;

import java.util.ArrayList;
import java.util.Objects;


public class HomeFragment extends Fragment {


    NewSeasonAdapter newSeasonAdapter;
    FirebaseFirestore firebaseFirestore;

    ArrayList<AllProducts> allProductsArrayList;
    TextView txtNewSeasonAllProducts;

    FrameLayout frameLayout;




    private RecyclerView newSeasonRecyclerView;
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


        txtNewSeasonAllProducts.setOnClickListener(view1 -> {
            HomePage homePage = (HomePage) getActivity();
            BottomNavigationView bottomNavigationView = homePage.findViewById(R.id.bottom_nav);
            bottomNavigationView.setVisibility(View.GONE);

            FragmentManager fragmentManager = ((AppCompatActivity)getContext()).getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.containerFrame, new AllProductsFragment()).addToBackStack(null).commit();
        });

        newSeasonRecyclerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

    }

    private void EventChangeListener() {

        firebaseFirestore.collection("NewSeason").orderBy("ProductPrice", Query.Direction.ASCENDING).addSnapshotListener((value, error) -> {

            for (DocumentChange documentChange : value.getDocumentChanges()){

                if (documentChange.getType() == DocumentChange.Type.ADDED){
                    allProductsArrayList.add(documentChange.getDocument().toObject(AllProducts.class));

                }
                newSeasonAdapter.notifyDataSetChanged();

            }

        });
    }
}