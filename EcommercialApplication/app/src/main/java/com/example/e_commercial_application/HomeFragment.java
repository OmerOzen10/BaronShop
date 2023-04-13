package com.example.e_commercial_application;

import android.app.ProgressDialog;
import android.content.Intent;
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
import android.widget.TextView;

import com.example.e_commercial_application.Adapter.NewSeasonAdapter;
import com.example.e_commercial_application.Model.NewSeason;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;

import java.util.ArrayList;


public class HomeFragment extends Fragment {


    FirebaseStorage mStorage;
    NewSeasonAdapter newSeasonAdapter;
    FirebaseFirestore firebaseFirestore;
    ArrayList<NewSeason> newSeasonArrayList;
    TextView txtNewSeasonAllProducts;


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
        newSeasonArrayList = new ArrayList<NewSeason>();

        newSeasonAdapter = new NewSeasonAdapter(newSeasonArrayList, getContext());

        newSeasonRecyclerView.setAdapter(newSeasonAdapter);
        
        EventChangeListener();


        txtNewSeasonAllProducts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), AllProducts.class);
                startActivity(intent);

//                FragmentManager fragmentManager = ((AppCompatActivity) getContext()).getSupportFragmentManager();
//                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//                fragmentTransaction.replace(R.id.containerFrame,new BasketFragment()).commit();


            }
        });








    }

    private void EventChangeListener() {

        firebaseFirestore.collection("NewSeason").orderBy("ProductPrice", Query.Direction.ASCENDING).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {

                for (DocumentChange documentChange : value.getDocumentChanges()){

                    if (documentChange.getType() == DocumentChange.Type.ADDED){

                        newSeasonArrayList.add(documentChange.getDocument().toObject(NewSeason.class));

                    }

                    newSeasonAdapter.notifyDataSetChanged();

                }

            }

        });
    }
}