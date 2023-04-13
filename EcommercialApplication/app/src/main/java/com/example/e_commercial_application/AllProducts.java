package com.example.e_commercial_application;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.example.e_commercial_application.Adapter.NewSeasonAdapter;
import com.example.e_commercial_application.Model.NewSeason;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class AllProducts extends AppCompatActivity {

    RecyclerView AllProductsRec;
    NewSeasonAdapter adapter;
    FirebaseFirestore firebaseFirestore;
    ArrayList<NewSeason> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_season_all_products);

        AllProductsRec = findViewById(R.id.newSeasonAllProductsRec);
        AllProductsRec.setHasFixedSize(true);
        AllProductsRec.setLayoutManager(new GridLayoutManager(this,2));

        firebaseFirestore = FirebaseFirestore.getInstance();
        list = new ArrayList<NewSeason>();
        adapter = new NewSeasonAdapter(list,this);
        AllProductsRec.setAdapter(adapter);

        EventChangeListener();

        MaterialToolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HomeFragment homeFragment = new HomeFragment();
                FragmentManager fragmentManager = getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.topScreen,homeFragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
                finish();
            }
        });

    }

    private void EventChangeListener() {

        firebaseFirestore.collection("NewSeason").orderBy("ProductPrice", Query.Direction.ASCENDING).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {

                for (DocumentChange documentChange : value.getDocumentChanges()){
                    if (documentChange.getType() == DocumentChange.Type.ADDED){
                        list.add(documentChange.getDocument().toObject(NewSeason.class));
                    }
                    adapter.notifyDataSetChanged();
                }
            }
        });

    }
}