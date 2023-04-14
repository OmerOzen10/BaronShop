package com.example.e_commercial_application;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

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

public class AllProductsFragment extends Fragment {

    RecyclerView AllProductsRec;
    NewSeasonAdapter adapter;
    FirebaseFirestore firebaseFirestore;
    ArrayList<NewSeason> list;



    public AllProductsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        AllProductsRec = view.findViewById(R.id.AllProductsRec);
        AllProductsRec.setHasFixedSize(true);
        AllProductsRec.setLayoutManager(new GridLayoutManager(getContext(),2));

        firebaseFirestore = FirebaseFirestore.getInstance();
        list = new ArrayList<NewSeason>();
        adapter = new NewSeasonAdapter(list,getContext());
        AllProductsRec.setAdapter(adapter);

        EventChangeListener();

        MaterialToolbar toolbar =view.findViewById(R.id.toolbar1);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getContext(),HomePage.class);
                startActivity(intent);




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