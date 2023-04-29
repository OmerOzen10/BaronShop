package com.example.e_commercial_application;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.e_commercial_application.Adapter.FavAdapter;
import com.example.e_commercial_application.Adapter.NewSeasonAdapter;
import com.example.e_commercial_application.Model.AllProducts;

public class FavFragment extends Fragment {

    AllProducts allProducts;
    private RecyclerView favRecyclerView;
    private FavAdapter adapter;
    private ConstraintLayout emptyFavConstraint;
    Button favContinue;


    public FavFragment() {
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
        favRecyclerView = view.findViewById(R.id.favRecyclerView);
        emptyFavConstraint = view.findViewById(R.id.emptyFav);
        favContinue = view.findViewById(R.id.favContinue);

        favRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapter = new FavAdapter(HomePage.favList,getContext());
        favRecyclerView.setAdapter(adapter);

        if (adapter.getItemCount() == 0){
            emptyFavConstraint.setVisibility(View.VISIBLE);
        }else {
            emptyFavConstraint.setVisibility(View.GONE);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_fav, container, false);
    }
}