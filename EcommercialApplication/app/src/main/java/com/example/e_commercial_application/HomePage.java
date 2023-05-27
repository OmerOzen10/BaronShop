package com.example.e_commercial_application;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.view.View;

import com.example.e_commercial_application.Databases.BasketDB;
import com.example.e_commercial_application.Databases.BasketDBDiscounted;
import com.example.e_commercial_application.Databases.FavDB;
import com.example.e_commercial_application.Databases.FavDBDiscounted;
import com.example.e_commercial_application.Model.AllProducts;
import com.example.e_commercial_application.Model.DiscountedProducts;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

import java.util.ArrayList;

public class HomePage extends AppCompatActivity {

    public static ArrayList <AllProducts> basketList = new ArrayList<>();
    public static  ArrayList<DiscountedProducts> basketList2 = new ArrayList<>();
    public static ArrayList <AllProducts> favList = new ArrayList<>();
    public static ArrayList<DiscountedProducts> favList2 = new ArrayList<>();

    private FavDB favDB;
    private FavDBDiscounted favDBDiscounted;
    private BasketDB basketDB;
    private BasketDBDiscounted basketDBDiscounted;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);
        getSupportFragmentManager().beginTransaction().replace(R.id.containerFrame, new HomeFragment()).commit();

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_nav);
        bottomNavigationView.setOnItemSelectedListener(navListener);

        favDB = new FavDB(this);
        favDBDiscounted = new FavDBDiscounted(this);
        basketDB = new BasketDB(this);
        basketDBDiscounted = new BasketDBDiscounted(this);
        HomePage.favList = (ArrayList<AllProducts>) favDB.getAllFavProducts();
        HomePage.favList2 = (ArrayList<DiscountedProducts>) favDBDiscounted.getAllFavProducts();

        favList.addAll(favList2);

        HomePage.basketList = (ArrayList<AllProducts>) basketDB.getAllBasketProducts();
        HomePage.basketList2 = (ArrayList<DiscountedProducts>) basketDBDiscounted.getAllBasketProducts();




    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    public void onBackPressed() {
        // make the bottom navigation visible
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_nav);
        bottomNavigationView.setVisibility(View.VISIBLE);
        super.onBackPressed();
    }


    public ArrayList<AllProducts> getBasketArrayList() {
        return basketList;
    }


    private final NavigationBarView.OnItemSelectedListener navListener =
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

    private int getSelectedItem(Fragment fragment) {
        if (fragment instanceof HomeFragment) {
            return R.id.nav_home;
        } else if (fragment instanceof BasketFragment) {
            return R.id.nav_basket;
        } else if (fragment instanceof FavFragment) {
            return R.id.nav_fav;
        } else if (fragment instanceof UserFragment) {
            return R.id.nav_user;
        }
        return 0;
    }




}