package com.example.e_commercial_application;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.e_commercial_application.Adapter.NewSeasonAdapter;
import com.example.e_commercial_application.Model.NewSeason;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {

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

        newSeasonRecyclerView = view.findViewById(R.id.newSeasonRecyclerView);
        newSeasonRecyclerView.setHasFixedSize(true);
        newSeasonRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.HORIZONTAL,false));

        List<NewSeason> imageList = new ArrayList<>();
        imageList.add(new NewSeason(R.drawable.newseason));
        imageList.add(new NewSeason(R.drawable.newseason2));
        imageList.add(new NewSeason(R.drawable.newseason3));

        NewSeasonAdapter newSeasonAdapter = new NewSeasonAdapter(imageList);


        newSeasonRecyclerView.setAdapter(newSeasonAdapter);


    }
}