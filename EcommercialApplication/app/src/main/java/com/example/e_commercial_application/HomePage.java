package com.example.e_commercial_application;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.e_commercial_application.Databases.BasketDB;
import com.example.e_commercial_application.Databases.BasketDBDiscounted;
import com.example.e_commercial_application.Databases.FavDB;
import com.example.e_commercial_application.Databases.FavDBDiscounted;
import com.example.e_commercial_application.Model.AllProducts;
import com.example.e_commercial_application.Model.DiscountedProducts;
import com.example.e_commercial_application.Model.UserDetails;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class HomePage extends AppCompatActivity {

    public static ArrayList <AllProducts> basketList = new ArrayList<>();
    public static  ArrayList<DiscountedProducts> basketList2 = new ArrayList<>();
    public static ArrayList <AllProducts> favList = new ArrayList<>();
    public static ArrayList<DiscountedProducts> favList2 = new ArrayList<>();

    public static UserDetails currentUser;
    private FavDB favDB;
    private FavDBDiscounted favDBDiscounted;
    private BasketDB basketDB;
    private BasketDBDiscounted basketDBDiscounted;
    private FirebaseAuth auth;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);
        getSupportFragmentManager().beginTransaction().replace(R.id.containerFrame, new HomeFragment()).commit();

        loadUserData();

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

        basketList.addAll(basketList2);




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



    public void loadUserData() {
        auth = FirebaseAuth.getInstance();
        FirebaseUser user = auth.getCurrentUser();

        currentUser = null;

        if (user != null) {
            String userID = user.getUid();

            DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Registered Users");
            reference.child(userID).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        UserDetails userDetails = snapshot.getValue(UserDetails.class);
                        if (userDetails != null) {
                            String name = userDetails.getName();
                            String dob = userDetails.getDob();
                            String mobile = userDetails.getMobile();
                            String address = userDetails.getAddress();

                            currentUser = new UserDetails(name, dob, mobile, address);
                        }
                    } else {
                        Toast.makeText(HomePage.this, "User data not found!", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Toast.makeText(HomePage.this, "Database error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Toast.makeText(HomePage.this, "No user logged in!", Toast.LENGTH_SHORT).show();
        }
    }
}

