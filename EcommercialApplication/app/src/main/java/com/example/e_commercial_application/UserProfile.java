package com.example.e_commercial_application;

import android.animation.LayoutTransition;
import android.app.AlertDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Handler;
import android.transition.ChangeBounds;
import android.transition.ChangeTransform;
import android.transition.TransitionManager;
import android.transition.TransitionSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.e_commercial_application.Model.UserDetails;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class UserProfile extends Fragment {

    private TextView txtWelcome,txtName,txtEmail,txtDOB,txtMobile,txtAddress,txtOmer,txtLogOut,txtChangePass;
    private ProgressBar progressBar3;
    private String name,email,dob,mobile,address;
    private ImageView imagePP,settings;
    CardView cardViewSetting,cardViewElevation;
    LinearLayout linear;
    private FirebaseAuth auth;

    View omerDivider, updateDivider,logOutDivider;
    String TAG = "UserProfile";
    private AlertDialog verificationDialog;
    int elevation;


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        txtWelcome = view.findViewById(R.id.txtWelcome);
        txtName = view.findViewById(R.id.txtName);
        txtEmail = view.findViewById(R.id.txtEmail);
        txtDOB = view.findViewById(R.id.txtDOB);
        txtMobile = view.findViewById(R.id.txtMobile);
        txtAddress = view.findViewById(R.id.txtAddress);
        imagePP = view.findViewById(R.id.imageP);
        settings = view.findViewById(R.id.settings);
        cardViewSetting = view.findViewById(R.id.cardSettings);
        linear = view.findViewById(R.id.linear);
        txtOmer = view.findViewById(R.id.txtUpdateProfile);
        txtLogOut = view.findViewById(R.id.txtLogOut);
        txtChangePass = view.findViewById(R.id.txtChangePass);
        omerDivider = view.findViewById(R.id.omerDivider);
        logOutDivider = view.findViewById(R.id.LogOutDivider);



        auth = FirebaseAuth.getInstance();
        FirebaseUser user = auth.getCurrentUser();

        if (user == null){
            Toast.makeText(getContext(), "Something went wrong! User's details are not available at the moment.", Toast.LENGTH_SHORT).show();
        }else {
            showUserData(user);
        }

        linear.setLayoutTransition(new LayoutTransition());
        linear.getLayoutTransition().enableTransitionType(LayoutTransition.CHANGE_APPEARING);
        linear.getLayoutTransition().enableTransitionType(LayoutTransition.CHANGE_DISAPPEARING);

        cardViewSetting.setOnClickListener(view1 -> {
            int v = (txtOmer.getVisibility() == View.GONE
                    && txtChangePass.getVisibility() == View.GONE &&
                    txtLogOut.getVisibility() == View.GONE
                    && omerDivider.getVisibility() == View.GONE
                    && logOutDivider.getVisibility() == View.GONE)? View.VISIBLE : View.GONE;

            TransitionSet transitionSet = new TransitionSet();
            transitionSet.addTransition(new ChangeBounds());
            transitionSet.addTransition(new ChangeTransform());
            transitionSet.setInterpolator(new DecelerateInterpolator());
            transitionSet.setDuration(400); //
            TransitionManager.beginDelayedTransition(linear, transitionSet);

            if (txtOmer.getVisibility() != View.VISIBLE) {
                new Handler().postDelayed(() -> {
                    cardViewSetting.setCardElevation(60f);
                    cardViewSetting.setRadius(20f);
                }, 100);
            } else {
                cardViewSetting.setCardElevation(0f);
                cardViewSetting.setRadius(0f);
            }

            txtOmer.setVisibility(v);
            txtChangePass.setVisibility(v);
            txtLogOut.setVisibility(v);
            omerDivider.setVisibility(v);
            logOutDivider.setVisibility(v);
        });

        txtLogOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                auth.signOut();
                FragmentManager fragmentManager = ((AppCompatActivity)getContext()).getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction().addToBackStack(null);
                fragmentTransaction.replace(R.id.containerFrame, new UserFragment()).addToBackStack(null).commit();
            }
        });






    }

    private void showUserData(FirebaseUser user) {
                    txtWelcome.setText("Welcome, " + HomePage.currentUser.getName() + "!");
                    txtEmail.setText(user.getEmail());
                    txtDOB.setText(HomePage.currentUser.getDob());
                    txtMobile.setText(HomePage.currentUser.getMobile());
                    txtAddress.setText(HomePage.currentUser.getAddress());
                    txtName.setText(HomePage.currentUser.getName());

                    Uri uri = user.getPhotoUrl();
                    Glide.with(requireActivity()).load(uri).into(imagePP);
                    imagePP.setImageTintList(null);
                    ViewGroup.LayoutParams layoutParams = imagePP.getLayoutParams();
                    layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT;
                    layoutParams.height = ViewGroup.LayoutParams.MATCH_PARENT;
                    imagePP.setLayoutParams(layoutParams);


    }





    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_user_profile, container, false);


    }
}