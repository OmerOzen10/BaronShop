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

import com.example.e_commercial_application.Model.AllProducts;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class AllProductsFragment extends Fragment {

    RecyclerView AllProductsRec;
    AllProductsAdapter allProductsAdapter;
    FirebaseFirestore firebaseFirestore;
    ArrayList<com.example.e_commercial_application.Model.AllProducts> allProductsArrayList;

    CardView AllProducts;
    ImageView backButton;

    private static final String TAG = "AllProductsFragment";



    public AllProductsFragment() {
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
        allProductsArrayList = new ArrayList<AllProducts>();
        allProductsAdapter = new AllProductsAdapter(allProductsArrayList,getContext());
        AllProductsRec.setAdapter(allProductsAdapter);

        AllProducts = view.findViewById(R.id.AllProductsCard);

        Log.d(TAG, "onViewCreated: " + allProductsArrayList.size());

        EventChangeListener();

        backButton = view.findViewById(R.id.backButton);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(),HomePage.class);
                startActivity(intent);
            }
        });


    }

    private void EventChangeListener() {
        firebaseFirestore.collection("AllProducts")
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
                                allProductsArrayList.add(documentChange.getDocument().toObject(AllProducts.class));
                            }
                        }
                        allProductsAdapter.notifyDataSetChanged();
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
        return inflater.inflate(R.layout.fragment_all_products, container, false);
    }
}