package com.example.e_commercial_application;

import android.animation.LayoutTransition;
import android.app.AlertDialog;
import android.content.DialogInterface;
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
import android.transition.AutoTransition;
import android.transition.ChangeBounds;
import android.transition.ChangeTransform;
import android.transition.TransitionManager;
import android.transition.TransitionSet;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
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

import java.util.Objects;

public class UserProfile extends Fragment {

    private TextView txtWelcome,txtName,txtEmail,txtDOB,txtMobile,txtAddress,txtOmer,txtUpdate,txtLogOut,txtChangePass;
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
        txtOmer = view.findViewById(R.id.txtOmer);
        txtUpdate = view.findViewById(R.id.txtUpdate);
        txtLogOut = view.findViewById(R.id.txtLogOut);
        txtChangePass = view.findViewById(R.id.txtChangePass);
        omerDivider = view.findViewById(R.id.omerDivider);
        updateDivider = view.findViewById(R.id.updateEDivider);
        logOutDivider = view.findViewById(R.id.LogOutDivider);



        auth = FirebaseAuth.getInstance();
        FirebaseUser user = auth.getCurrentUser();

        if (user == null){
            Toast.makeText(getContext(), "Something went wrong! User's details are not available at the moment.", Toast.LENGTH_SHORT).show();
        }else {
            checkEmailVerified(user);
            showUserData(user);
        }

        linear.setLayoutTransition(new LayoutTransition());
        linear.getLayoutTransition().enableTransitionType(LayoutTransition.CHANGE_APPEARING);
        linear.getLayoutTransition().enableTransitionType(LayoutTransition.CHANGE_DISAPPEARING);

        cardViewSetting.setOnClickListener(view1 -> {
            int v = (txtOmer.getVisibility() == View.GONE && txtUpdate.getVisibility() == View.GONE
                    && txtChangePass.getVisibility() == View.GONE &&
                    txtLogOut.getVisibility() == View.GONE
                    && omerDivider.getVisibility() == View.GONE
                    && updateDivider.getVisibility() == View.GONE
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
            txtUpdate.setVisibility(v);
            txtChangePass.setVisibility(v);
            txtLogOut.setVisibility(v);
            omerDivider.setVisibility(v);
            updateDivider.setVisibility(v);
            logOutDivider.setVisibility(v);
        });






    }

    private void showUserData(FirebaseUser user) {

        String userID = user.getUid();

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Registered Users");
        reference.child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                UserDetails userDetails = snapshot.getValue(UserDetails.class);

                if (userDetails !=null){
                    name = userDetails.name;
                    email = user.getEmail();
                    dob = userDetails.dob;
                    mobile = userDetails.mobile;
                    address = userDetails.address;

                    txtWelcome.setText("Welcome, " + name + "!");
                    txtEmail.setText(email);
                    txtDOB.setText(dob);
                    txtMobile.setText(mobile);
                    txtAddress.setText(address);
                    txtName.setText(name);

                    Uri uri = user.getPhotoUrl();

                    Glide.with(requireActivity()).load(uri).into(imagePP);
                    imagePP.setImageTintList(null);
                    ViewGroup.LayoutParams layoutParams = imagePP.getLayoutParams();
                    layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT;
                    layoutParams.height = ViewGroup.LayoutParams.MATCH_PARENT;
                    imagePP.setLayoutParams(layoutParams);
                }else {
                    Toast.makeText(getContext(), "Something went wrong!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(), "Something went wrong!!", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void checkEmailVerified(FirebaseUser user) {
        if (!user.isEmailVerified()) {
            showAlertDialog();

            // Periodically check the email verification status
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    user.reload().addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                if (user.isEmailVerified()) {
                                    // Email has been verified, dismiss the dialog
                                    if (verificationDialog != null && verificationDialog.isShowing()) {
                                        verificationDialog.dismiss();
                                    }
                                }
                            } else {
                                // Failed to reload user data, handle the error
                            }
                        }
                    });
                }
            }, 5000); // Adjust the delay as needed (e.g., every 5 seconds)
        }
    }


    private void showAlertDialog() {

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        verificationDialog = builder.create();
        verificationDialog.show();
        builder.setTitle("Email not verified");
        builder.setMessage("Please verify your email now. You can not login without email verification next time");

        builder.setPositiveButton("Continue", (dialogInterface, i) -> {
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_APP_EMAIL);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            verificationDialog.dismiss();
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();

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