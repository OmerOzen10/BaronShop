package com.example.e_commercial_application;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.auth.User;

import java.util.Objects;

public class UserFragment extends Fragment {

    private FirebaseAuth auth;
    private static final String TAG = "UserFragment";
    private TextInputEditText edtEmail, edtPassword;
    private TextView txtRegister, txtForgot;

    private TextInputLayout layoutEmail, layoutPassword;
    private Button btnLogin;

    public UserFragment() {
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        edtEmail = view.findViewById(R.id.edtEmail);
        edtPassword = view.findViewById(R.id.edtPassword);
        txtRegister = view.findViewById(R.id.txtRegister);
        txtForgot = view.findViewById(R.id.txtForgot);
        btnLogin = view.findViewById(R.id.btnLogin);
        layoutEmail = view.findViewById(R.id.layoutEmail);
        layoutPassword = view.findViewById(R.id.layoutPassword);
        auth = FirebaseAuth.getInstance();


        btnLogin.setOnClickListener(view1 -> {
            String email = edtEmail.getText().toString();
            String password = edtPassword.getText().toString();

            if (email.isEmpty()){
                Toast.makeText(getContext(), "Please enter your Email", Toast.LENGTH_SHORT).show();
                layoutEmail.setError("Email is required");
                layoutEmail.requestFocus();
            } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                Toast.makeText(getContext(), "Please re-enter your email ", Toast.LENGTH_SHORT).show();
                layoutEmail.setError("Invalid Email");
                layoutEmail.requestFocus();
            } else if (password.isEmpty()) {
                Toast.makeText(getContext(), "Please enter your password", Toast.LENGTH_SHORT).show();
                layoutPassword.setError("Password is required");
                layoutPassword.requestFocus();
            }else {
                loginUser(email,password);
            }
        });

        txtRegister.setOnClickListener(view1 -> {
            HomePage homePage = (HomePage) getActivity();
            BottomNavigationView bottomNavigationView = homePage.findViewById(R.id.bottom_nav);
            bottomNavigationView.setVisibility(View.GONE);

            RegisterFragment registerFragment = new RegisterFragment();
            FragmentTransaction transaction = requireActivity().getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.containerFrame, registerFragment); // Replace "fragment_container" with the actual ID of your fragment container in the layout
            transaction.addToBackStack(null); // Optional, to add the transaction to the back stack
            transaction.commit();
        });
    }

    private void loginUser(String email, String password) {

        auth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){

                    FirebaseUser firebaseUser = auth.getCurrentUser();

                    if (firebaseUser.isEmailVerified()){
                        Toast.makeText(getContext(), "You are logged in", Toast.LENGTH_SHORT).show();

                        UserProfile profileFragment = new UserProfile();

                        FragmentTransaction transaction = requireActivity().getSupportFragmentManager().beginTransaction();
                        transaction.replace(R.id.containerFrame, profileFragment); // Replace "fragment_container" with the actual ID of your fragment container in the layout
                        transaction.addToBackStack(null); // Optional, to add the transaction to the back stack
                        transaction.commit();
                    }else {
                        firebaseUser.sendEmailVerification();
                        auth.signOut();
                        showAlertDialog();
                    }

                    Log.d(TAG, "onComplete: email " + firebaseUser.isEmailVerified());

                }else {
                    try {
                        throw task.getException();
                    }catch (FirebaseAuthInvalidUserException e){
                        layoutEmail.setError("User does not exist");
                        layoutEmail.requestFocus();
                    }catch (FirebaseAuthInvalidCredentialsException e){
                        layoutEmail.setError("Invalid credentails");
                        layoutEmail.requestFocus();
                    }catch (Exception e){
                        Log.e(TAG, e.getMessage());
                        Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
                }
        });

    }

    private void showAlertDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setTitle("Email Not Verified");
        builder.setMessage("Please verify your email now. You can not login without email verification");

        builder.setPositiveButton("Continue", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Intent intent = new Intent(Intent.ACTION_MAIN);
                intent.addCategory(Intent.CATEGORY_APP_EMAIL);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
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
        return inflater.inflate(R.layout.fragment_user, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();
        if (auth.getCurrentUser() != null){
            Toast.makeText(getContext(), "Already Logged In", Toast.LENGTH_SHORT).show();
            UserProfile userProfile = new UserProfile();
            FragmentTransaction transaction = requireActivity().getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.containerFrame, userProfile); // Replace "fragment_container" with the actual ID of your fragment container in the layout
            transaction.addToBackStack(null); // Optional, to add the transaction to the back stack
            transaction.commit();
        }else {
            Toast.makeText(getContext(), "You can Log in now!", Toast.LENGTH_SHORT).show();
        }
    }
}