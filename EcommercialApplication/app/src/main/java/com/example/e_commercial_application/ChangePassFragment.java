package com.example.e_commercial_application;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ChangePassFragment extends Fragment {

    private FirebaseAuth auth;
    private TextInputEditText edtCurrentPass,edtNewPass,edtNewPwdConfirm;
    private TextView txtAuthenticatedPwd;
    private Button btnChangePass,btnUpdatePwd;
    private String userPwdCurrent;
    TextInputLayout layoutCurrentPass,layoutNewPass, layoutNewPassConfirm;


    public ChangePassFragment() {
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        edtCurrentPass = view.findViewById(R.id.edtCurrentPass);
        edtNewPass = view.findViewById(R.id.edtNewPwd);
        edtNewPwdConfirm = view.findViewById(R.id.edtNewPwdConfirm);
        txtAuthenticatedPwd = view.findViewById(R.id.txtAuthenticatedPwd);
        btnChangePass = view.findViewById(R.id.btnChangePass);
        btnUpdatePwd = view.findViewById(R.id.btnUpdatePwd);

        edtNewPass.setEnabled(false);
        edtNewPwdConfirm.setEnabled(false);
        btnUpdatePwd.setEnabled(false);

        auth = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = auth.getCurrentUser();

        if (firebaseUser.equals("")){
            Toast.makeText(getContext(), "something went wrong!!", Toast.LENGTH_SHORT).show();
            HomePage homePage = (HomePage) getActivity();
            BottomNavigationView bottomNavigationView = homePage.findViewById(R.id.bottom_nav);
            bottomNavigationView.setVisibility(View.VISIBLE);
            FragmentManager fragmentManager = ((AppCompatActivity)getContext()).getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction().addToBackStack(null);
            fragmentTransaction.replace(R.id.containerFrame, new UserFragment()).commit();
        }else {
            reAuthenticate(firebaseUser);
        }

    }

    private void reAuthenticate(FirebaseUser firebaseUser) {

        btnChangePass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                userPwdCurrent = edtCurrentPass.getText().toString();

                if (TextUtils.isEmpty(userPwdCurrent)){
                    Toast.makeText(getContext(), "Password is required", Toast.LENGTH_SHORT).show();
                    layoutCurrentPass.setError("Please enter your current password");
                    layoutCurrentPass.requestFocus();
                }else {

                    AuthCredential credential = EmailAuthProvider.getCredential(firebaseUser.getEmail(),userPwdCurrent);

                    firebaseUser.reauthenticate(credential).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()){



                                edtNewPass.setEnabled(true);
                                edtNewPwdConfirm.setEnabled(true);
                                btnUpdatePwd.setEnabled(true);
                                edtCurrentPass  .setEnabled(false);
                                btnChangePass.setEnabled(false);

                                txtAuthenticatedPwd.setText("YOU ARE AUTHENTICATED. " +"YOU CAN CHANGE YOUR PASSWORD NOW!!");
                                Toast.makeText(getContext(), "Password has been verified." + "Change password now!!", Toast.LENGTH_SHORT).show();
                                btnUpdatePwd.setBackgroundTintList(ContextCompat.getColorStateList(requireContext(),R.color.orange));

                                btnUpdatePwd.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        changePassword(firebaseUser);
                                    }
                                });
                            }else {
                                try {
                                    throw task.getException();
                                }catch (Exception e){
                                    Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }
                        }
                    });
                }
            }
        });

    }

    private void changePassword(FirebaseUser firebaseUser) {
        String userNewPwd = edtNewPass.getText().toString();
        String userNewPwdConfirm = edtNewPwdConfirm.getText().toString();

        if (TextUtils.isEmpty(userNewPwd)){
            Toast.makeText(getContext(), "New password is Required", Toast.LENGTH_SHORT).show();
            layoutNewPass.setError("Please enter your new password!!");
            layoutNewPass.requestFocus();
        } else if (TextUtils.isEmpty(userNewPwdConfirm)){
            Toast.makeText(getContext(), "Please confirm your password", Toast.LENGTH_SHORT).show();
            layoutNewPassConfirm.setError("Please re-enter your new password!!");
            layoutNewPassConfirm.requestFocus();
        } else if (!userNewPwd.matches(userNewPwdConfirm)) {
            Toast.makeText(getContext(), "Password did not match", Toast.LENGTH_SHORT).show();
            layoutNewPassConfirm.setError("Please re-enter same password");
            layoutNewPassConfirm.requestFocus();
        }else if (userNewPwd.matches(userPwdCurrent)) {
            Toast.makeText(getContext(), "New password cannot be as same sa old password", Toast.LENGTH_SHORT).show();
            layoutNewPass.setError("Please enter a new password");
            layoutNewPass.requestFocus();
        }else {
            firebaseUser.updatePassword(userNewPwd).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()){
                        Toast.makeText(getContext(), "Password has been changed", Toast.LENGTH_SHORT).show();
                        HomePage homePage = (HomePage) getActivity();
                        BottomNavigationView bottomNavigationView = homePage.findViewById(R.id.bottom_nav);
                        bottomNavigationView.setVisibility(View.VISIBLE);
                        FragmentManager fragmentManager = ((AppCompatActivity)getContext()).getSupportFragmentManager();
                        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction().addToBackStack(null);
                        fragmentTransaction.replace(R.id.containerFrame, new UserFragment()).commit();
                    }else {
                        try {
                            throw task.getException();
                        } catch (Exception e) {
                            Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            });
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_change_pass, container, false);
    }
}