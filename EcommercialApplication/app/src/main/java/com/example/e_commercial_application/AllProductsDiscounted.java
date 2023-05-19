package com.example.e_commercial_application;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.e_commercial_application.Adapter.AllProductsAdapter;
import com.example.e_commercial_application.Adapter.DiscountedAllAdapter;
import com.example.e_commercial_application.Model.AllProducts;
import com.example.e_commercial_application.Model.DiscountedProducts;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class AllProductsDiscounted extends Fragment {

    RecyclerView AllProductsRec;
    DiscountedAllAdapter adapter;
    FirebaseFirestore firebaseFirestore;
    ArrayList<DiscountedProducts> allProductsArrayList;

    CardView AllProducts;
    ImageView backButton;
    private static final String TAG = "AllProductsDiscounted";


    public AllProductsDiscounted() {
        // Required empty public constructor
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        AllProductsRec = view.findViewById(R.id.AllProductsRec);
        AllProductsRec.setHasFixedSize(true);
        AllProductsRec.setVerticalScrollBarEnabled(true);
        AllProductsRec.setLayoutManager(new GridLayoutManager(getContext(),2));

        firebaseFirestore = FirebaseFirestore.getInstance();
        allProductsArrayList = new ArrayList<DiscountedProducts>();
        adapter = new DiscountedAllAdapter(allProductsArrayList,getContext());
        AllProductsRec.setAdapter(adapter);

        AllProducts = view.findViewById(R.id.AllProductsCard);

        EventChangeListener();

        backButton = view.findViewById(R.id.backButton);

        backButton.setOnClickListener(view1 -> {
            Intent intent = new Intent(getContext(),HomePage.class);
            startActivity(intent);
        });
    }

    private void EventChangeListener() {
        firebaseFirestore.collection("DiscountedProducts")
                .orderBy("ProductPrice", Query.Direction.ASCENDING)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        if (error != null) {
                            Log.e(TAG, "onEvent: Error fetching all products.", error);
                            return;
                        }
                        for (DocumentChange documentChange : value.getDocumentChanges()){
                            if (documentChange.getType() == DocumentChange.Type.ADDED){
                                allProductsArrayList.add(documentChange.getDocument().toObject(DiscountedProducts.class));
                            }
                        }
                        adapter.notifyDataSetChanged();
                        Log.d(TAG, "EventChangeListener: " + allProductsArrayList.size() + " items");
                    }
                });
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_all_products, container, false);
    }
}