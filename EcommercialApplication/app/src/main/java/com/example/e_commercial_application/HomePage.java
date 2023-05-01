package com.example.e_commercial_application;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.example.e_commercial_application.Model.AllProducts;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

import java.util.ArrayList;
import java.util.List;

public class HomePage extends AppCompatActivity {

    public static ArrayList <AllProducts> basketList = new ArrayList<>();
    public static ArrayList <AllProducts> favList = new ArrayList<>();
    private FavDB favDB;
    private BasketDB basketDB;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);
        getSupportFragmentManager().beginTransaction().replace(R.id.containerFrame, new HomeFragment()).commit();

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_nav);
        bottomNavigationView.setOnItemSelectedListener(navListener);

        favDB = new FavDB(this);
        basketDB = new BasketDB(this);
        HomePage.favList = (ArrayList<AllProducts>) favDB.getAllFavProducts();
        HomePage.basketList = (ArrayList<AllProducts>) basketDB.getAllBasketProducts();




    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    public ArrayList<AllProducts> getBasketArrayList() {
        return basketList;
    }


    private NavigationBarView.OnItemSelectedListener navListener =
            item -> {
                Fragment selectedFragment = null;
                switch (item.getItemId()) {
                    case R.id.nav_home:
                        selectedFragment = new HomeFragment();
                        break;
                    case R.id.nav_basket:
                        selectedFragment = new BasketFragment();
                        break;
                    case R.id.nav_fav:
                        selectedFragment = new FavFragment();
                        break;
                    case R.id.nav_user:
                        selectedFragment = new UserFragment();
                        break;

                }
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.containerFrame, selectedFragment)
                        .addToBackStack(null)
                        .commit();
                return true;
            };

}