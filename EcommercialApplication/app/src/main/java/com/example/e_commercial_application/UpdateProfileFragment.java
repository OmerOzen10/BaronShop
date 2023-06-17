package com.example.e_commercial_application;

import android.app.DatePickerDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Toast;

import com.example.e_commercial_application.Model.UserDetails;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class UpdateProfileFragment extends Fragment {

    private TextInputEditText edtUpdateName, edtUpdateDoB, edtUpdateMobile, edtUpdateAddress;
    private TextInputLayout layoutUpdateName, layoutUpdateDoB, layoutUpdateMobile, layoutUpdateAddress;
    private String txtName, txtDoB, txtAddress, txtMobile;
    private FirebaseAuth auth;

    public UpdateProfileFragment() {

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        edtUpdateName = view.findViewById(R.id.edtUpdateName);
        edtUpdateDoB = view.findViewById(R.id.edtUpdateDoB);
        edtUpdateMobile = view.findViewById(R.id.edtUpdateMobile);
        edtUpdateAddress = view.findViewById(R.id.edtUpdateAddress);

        layoutUpdateAddress = view.findViewById(R.id.layoutUpdateAddress);
        layoutUpdateName = view.findViewById(R.id.layoutUpdateName);
        layoutUpdateDoB = view.findViewById(R.id.layoutUpdateDob);
        layoutUpdateMobile = view.findViewById(R.id.layoutUpdateMobile);


        auth = FirebaseAuth.getInstance();
        FirebaseUser user = auth.getCurrentUser();


        showProfile(user);

        Button btnUpload = view.findViewById(R.id.btnUpload);
        btnUpload.setOnClickListener(view1 -> {
            updateProfile(user);
        });

        edtUpdateDoB.setOnClickListener(view12 -> {
            String txtSADoB[] = txtDoB.split("/");

            int day = Integer.parseInt(txtSADoB[0]);
            int month = Integer.parseInt(txtSADoB[1]) - 1;
            int year = Integer.parseInt(txtSADoB[2]);

            DatePickerDialog picker;

            picker = new DatePickerDialog(getContext(), (datePicker, year1, month1, dayOdMonth) -> edtUpdateDoB.setText(dayOdMonth + "/" + (month1 +1) + "/" + year1),year,month,day);
            picker.show();
        });
    }

    private void updateProfile(FirebaseUser user){
        if (TextUtils.isEmpty(txtName)){
            Toast.makeText(getContext(), "Please enter your new full name", Toast.LENGTH_SHORT).show();
            layoutUpdateName.setError("Full name is required");
            layoutUpdateName.requestFocus();
        }else if (TextUtils.isEmpty(txtDoB)){
            Toast.makeText(getContext(), "Please enter your new Date of Birth", Toast.LENGTH_SHORT).show();
            layoutUpdateDoB.setError("Date of Birth is required");
            layoutUpdateDoB.requestFocus();
        } else if (TextUtils.isEmpty(txtMobile)) {
            Toast.makeText(getContext(), "Please enter your new Mobile number", Toast.LENGTH_SHORT).show();
            layoutUpdateMobile.setError("Mobile number is required");
            layoutUpdateMobile.requestFocus();
        } else if (TextUtils.isEmpty(txtAddress)) {
            Toast.makeText(getContext(), "Please enter your new Address", Toast.LENGTH_SHORT).show();
            layoutUpdateAddress.setError("Address is required");
            layoutUpdateAddress.requestFocus();
        } else if (txtMobile.length()!=9) {
            Toast.makeText(getContext(), "Please re-enter your new Mobile number", Toast.LENGTH_SHORT).show();
            layoutUpdateMobile.setError("Mobile number should be 9 digit");
            layoutUpdateMobile.requestFocus();
        }else {
            txtName = edtUpdateName.getText().toString();
            txtMobile = edtUpdateMobile.getText().toString();
            txtDoB = edtUpdateDoB.getText().toString();
            txtAddress = edtUpdateAddress.getText().toString();

            UserDetails userDetails = new UserDetails(txtName,txtDoB,txtMobile,txtAddress);
            DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Registered Users");
            String userId = user.getUid();

            reference.child(userId).setValue(userDetails).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()){
                        UserProfileChangeRequest profileChangeRequest = new UserProfileChangeRequest.Builder().setDisplayName(txtName).build();
                        user.updateProfile(profileChangeRequest);
                        Toast.makeText(getContext(), "Update Successfull!!", Toast.LENGTH_SHORT).show();
                        HomePage homePage = (HomePage) getActivity();
                        BottomNavigationView bottomNavigationView = homePage.findViewById(R.id.bottom_nav);
                        bottomNavigationView.setVisibility(View.VISIBLE);
                        FragmentManager fragmentManager = ((AppCompatActivity)getContext()).getSupportFragmentManager();
                        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction().addToBackStack(null);
                        fragmentTransaction.replace(R.id.containerFrame, new UserFragment()).addToBackStack(null).commit();
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
    private void showProfile(FirebaseUser user) {

        String userID = user.getUid();

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Registered Users");

        reference.child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                UserDetails userDetails = snapshot.getValue(UserDetails.class);

                if (userDetails !=null){
                    txtName = userDetails.name;
                    txtAddress = userDetails.address;
                    txtMobile = userDetails.mobile;
                    txtDoB = userDetails.dob;

                    edtUpdateName.setText(txtName);
                    edtUpdateMobile.setText(txtMobile);
                    edtUpdateDoB.setText(txtDoB);
                    edtUpdateAddress.setText(txtAddress);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(), "Something went wrong!!", Toast.LENGTH_SHORT).show();
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
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_update_profile, container, false);
    }
}