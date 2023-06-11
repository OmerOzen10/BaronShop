package com.example.e_commercial_application;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
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

    private TextView txtWelcome,txtName,txtEmail,txtDOB,txtMobile,txtAddress;
    private ProgressBar progressBar3;
    private String name,email,dob,mobile,address;
    private ImageView imagePP;
    private FirebaseAuth auth;
    String TAG = "UserProfile";
    private AlertDialog verificationDialog;


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


        auth = FirebaseAuth.getInstance();
        FirebaseUser user = auth.getCurrentUser();

        if (user == null){
            Toast.makeText(getContext(), "Something went wrong! User's details are not available at the moment.", Toast.LENGTH_SHORT).show();
        }else {
            checkEmailVerified(user);
            showUserData(user);
        }

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